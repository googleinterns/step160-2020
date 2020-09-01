package com.google.sps.servlets;

import static net.javacrumbs.jsonunit.assertj.JsonAssertions.assertThatJson;
import static net.javacrumbs.jsonunit.assertj.JsonAssertions.json;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.testing.LocalDatastoreHelper;
import com.google.gson.*;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * Tests for the SurveyServlet class.
 *
 * Make sure to set the project you're working on in your shell with:
 * gcloud config set project [PROJECT_NAME]
 *
 * Skeleton code for Datastore emulator from:
 * https://stackoverflow.com/questions/40348653/google-datastore-emulator-using-java-not-using-gae
 */
public class SurveyServletTest extends TestData {

    protected static LocalDatastoreHelper localDatastoreHelper;
    protected Datastore datastore;
    protected KeyFactory keyFactory;

    @BeforeClass
    public static void setUpClass() throws InterruptedException, IOException {
        // Create and start a local datastore emulator on a random free port
        System.out.println("[Datastore-Emulator] start");
        localDatastoreHelper = LocalDatastoreHelper.create();
        localDatastoreHelper.start();
        System.out.println("[Datastore-Emulator] listening on port: " + localDatastoreHelper.getPort());

        // Set the system property to tell the gcloud lib to use the datastore emulator
        System.setProperty("DATASTORE_EMULATOR_HOST","localhost:" + localDatastoreHelper.getPort());
    }

    @Before
    public void setUp() {
        // Create the datastore instance
        // Because of the system property set it in setUpClass() this
        // datastore will be connected with the datastore emulator.
        datastore = DatastoreOptions.getDefaultInstance().getService();
        keyFactory = datastore.newKeyFactory().setProjectId("manage-at-scale-step-2020").setKind("TestEntity");
    }

    @After
    public void tearDown() throws IOException {
        System.out.println("[Datastore-Emulator] reset");
        // This resets the datastore after every test
        localDatastoreHelper.reset();
    }

    @AfterClass
    public static void tearDownClass() throws InterruptedException, IOException, TimeoutException {
        System.out.println("[Datastore-Emulator] stop");
        // This stops the datastore emulator after all tests are done
        localDatastoreHelper.stop();
    }


    // SurveyServlet tests

    @Test
    public void testQueryByFeelingSome() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        loadTestData(false);
        Set<SurveyResponse> expected = new HashSet<>();
        expected.add(expectedData.get("Foo"));
        expected.add(expectedData.get("Bar"));
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryByFeeling("JITTERY"));
    }

    @Test
    public void testQueryByFeelingAll() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        loadTestData(false);
        Set<SurveyResponse> expected = new HashSet<>();
        expected.add(expectedData.get("Foo"));
        expected.add(expectedData.get("Bar"));
        expected.add(expectedData.get("Baz"));
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryByFeeling("ALERT"));
    }

    @Test
    public void testQueryByFeelingNone() {
        loadTestData(false);
        Set<SurveyResponse> expected = new HashSet<>();
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryByFeeling("HOSTILE"));
    }

    @Test
    public void testQueryByUserSome() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        loadTestData(false);
        List<SurveyResponse> expected = new ArrayList<>();
        expected.add(expectedData.get("Bar"));
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryByUser("Bar"));
    }

    @Test
    public void testQueryByUserAll() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(true);
        loadTestData(true);
        List<SurveyResponse> expected = new ArrayList<>();
        expected.add(expectedData.get("Bar"));
        expected.add(expectedData.get("Baz"));
        expected.add(expectedData.get("Foo"));
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryByUser("Foo"));
    }

    @Test
    public void testQueryByUserNone() {
        loadTestData(false);
        List<SurveyResponse> expected = new ArrayList<>();
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryByUser("Peter"));
    }

    @Test
    public void testQueryMostWidespread() {
        loadTestData(false);
        List<PanasFeelings> expected = new ArrayList<>();
        expected.add(PanasFeelings.ALERT);
        expected.add(PanasFeelings.JITTERY);
        expected.add(PanasFeelings.AFRAID);
        String dummyData = "foobar";
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryMostWidespread(dummyData));
    }

    @Test
    public void testQueryMostWidespreadEmpty() {
        List<PanasFeelings> expected = new ArrayList<>();
        String dummyData = "foobar";
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryMostWidespread(dummyData));
    }

    @Test
    public void testQueryMostIntense() {
        loadTestData(false);
        List<PanasFeelings> expected = new ArrayList<>();
        expected.add(PanasFeelings.ALERT);
        expected.add(PanasFeelings.JITTERY);
        expected.add(PanasFeelings.AFRAID);
        String dummyData = "foobar";
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryMostIntense(dummyData));
    }

    @Test
    public void testQueryMostInteseEmpty() {
        List<PanasFeelings> expected = new ArrayList<>();
        String dummyData = "foobar";
        assertThatJson(createJson(expected)).isEqualTo(SurveyServlet.queryMostIntense(dummyData));
    }


    /**
     * Loads Entities corresponding to the test data into the local datastore instance.
     */
    private void loadTestData(boolean sameUser) {
        IncompleteKey fooIncompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", "Foo"))
            .setKind("SurveyResponse")
            .setProjectId("manage-at-scale-step-2020")
            .newKey();
        Key fooKey = datastore.allocateId(fooIncompleteKey);
        Entity.Builder fooEntityBuilder = Entity.newBuilder(fooKey)
            .set("timestamp", fooTimestamp)
            .set("city", fooCity)
            .set("state", fooState)
            .set("text", fooText);

        for (PanasFeelings feeling : PanasFeelings.values()) {
            fooEntityBuilder.set(
                feeling.name(),
                fooFeelings.get(feeling).ordinal()
            );
        }

        Entity fooEntity = fooEntityBuilder.build();

        final String barUser;
        final String bazUser;

        // In case we want to test how a method acts when every username is the same:
        if (sameUser) {
            barUser = "Foo";
            bazUser = "Foo";
        } else {
            barUser = "Bar";
            bazUser = "Baz";
        }

        IncompleteKey barIncompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", barUser))
            .setKind("SurveyResponse")
            .setProjectId("manage-at-scale-step-2020")
            .newKey();
        Key barKey = datastore.allocateId(barIncompleteKey);
        Entity.Builder barEntityBuilder = Entity.newBuilder(barKey)
            .set("timestamp", barTimestamp)
            .set("city", barCity)
            .set("state", barState)
            .set("text", barText);

        for (PanasFeelings feeling : PanasFeelings.values()) {
            barEntityBuilder.set(
                feeling.name(),
                barFeelings.get(feeling).ordinal()
            );
        }

        Entity barEntity = barEntityBuilder.build();

        IncompleteKey bazIncompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", bazUser))
            .setKind("SurveyResponse")
            .setProjectId("manage-at-scale-step-2020")
            .newKey();
        Key bazKey = datastore.allocateId(bazIncompleteKey);
        Entity.Builder bazEntityBuilder = Entity.newBuilder(bazKey)
            .set("timestamp", bazTimestamp)
            .set("city", bazCity)
            .set("state", bazState)
            .set("text", bazText);

        for (PanasFeelings feeling : PanasFeelings.values()) {
            bazEntityBuilder.set(
                feeling.name(),
                bazFeelings.get(feeling).ordinal()
            );
        }

        Entity bazEntity = bazEntityBuilder.build();

        datastore.add(fooEntity, barEntity, bazEntity);
    }

    /** Converts the given collection {@code data} to its JSON string equivalent. */
    private static <T> String createJson(Collection<T> responses) {
        String responsesJson = new Gson().toJson(responses);
        return responsesJson;
    }
}
