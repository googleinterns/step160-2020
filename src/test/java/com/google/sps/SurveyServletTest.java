package com.google.sps.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.testing.LocalDatastoreHelper;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import java.util.ArrayList;
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
        assertEquals(expected, SurveyServlet.queryByFeeling(PanasFeelings.JITTERY));
    }

    @Test
    public void testQueryByFeelingAll() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        loadTestData(false);
        Set<SurveyResponse> expected = new HashSet<>();
        expected.add(expectedData.get("Foo"));
        expected.add(expectedData.get("Bar"));
        expected.add(expectedData.get("Baz"));
        assertEquals(expected, SurveyServlet.queryByFeeling(PanasFeelings.ALERT));
    }

    @Test
    public void testQueryByFeelingNone() {
        loadTestData(false);
        Set<SurveyResponse> expected = new HashSet<>();
        assertEquals(expected, SurveyServlet.queryByFeeling(PanasFeelings.HOSTILE));
    }

    @Test
    public void testQueryByUserSome() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        loadTestData(false);
        Set<SurveyResponse> expected = new HashSet<>();
        expected.add(expectedData.get("Bar"));
        assertEquals(expected, SurveyServlet.queryByUser("Bar"));
    }

    @Test
    public void testQueryByUserAll() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(true);
        loadTestData(true);
        Set<SurveyResponse> expected = new HashSet<>();
        expected.add(expectedData.get("Foo"));
        expected.add(expectedData.get("Bar"));
        expected.add(expectedData.get("Baz"));
        Set<SurveyResponse> result = SurveyServlet.queryByUser("Foo");
        assertEquals(expected, result);
        assertEquals(3, result.size());
    }

    @Test
    public void testQueryByUserNone() {
        loadTestData(false);
        Set<SurveyResponse> expected = new HashSet<>();
        assertEquals(expected, SurveyServlet.queryByUser("Peter"));
    }

    @Test
    public void testQueryMostWidespread() {
        loadTestData(false);
        List<PanasFeelings> expected = new ArrayList<>();
        expected.add(PanasFeelings.ALERT);
        expected.add(PanasFeelings.JITTERY);
        expected.add(PanasFeelings.AFRAID);
        assertEquals(expected, SurveyServlet.queryMostWidespread());
    }

    @Test
    public void testQueryMostWidespreadEmpty() {
        List<PanasFeelings> expected = new ArrayList<>();
        assertEquals(expected, SurveyServlet.queryMostWidespread());
    }

    @Test
    public void testQueryMostIntense() {
        loadTestData(false);
        List<PanasFeelings> expected = new ArrayList<>();
        expected.add(PanasFeelings.JITTERY);
        expected.add(PanasFeelings.ALERT);
        expected.add(PanasFeelings.AFRAID);
        assertEquals(expected, SurveyServlet.queryMostIntense());
    }

    @Test
    public void testQueryMostInteseEmpty() {
        List<PanasFeelings> expected = new ArrayList<>();
        assertEquals(expected, SurveyServlet.queryMostIntense());
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
        Entity fooEntity = Entity.newBuilder(fooKey)
            .set("timestamp", fooTimestamp)
            .set("city", fooCity)
            .set("state", fooState)
            .set("text", fooText)
            .set("JITTERY", fooFeelings.get(PanasFeelings.JITTERY).ordinal())
            .set("ALERT", fooFeelings.get(PanasFeelings.ALERT).ordinal())
            .set("UPSET", fooFeelings.get(PanasFeelings.UPSET).ordinal())
            .build();

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
        Entity barEntity = Entity.newBuilder(barKey)
            .set("timestamp", barTimestamp)
            .set("city", barCity)
            .set("state", barState)
            .set("text", barText)
            .set("JITTERY", barFeelings.get(PanasFeelings.JITTERY).ordinal())
            .set("ALERT", barFeelings.get(PanasFeelings.ALERT).ordinal())
            .set("AFRAID", barFeelings.get(PanasFeelings.AFRAID).ordinal())
            .set("NERVOUS", barFeelings.get(PanasFeelings.NERVOUS).ordinal())
            .build();

        IncompleteKey bazIncompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", bazUser))
            .setKind("SurveyResponse")
            .setProjectId("manage-at-scale-step-2020")
            .newKey();
        Key bazKey = datastore.allocateId(bazIncompleteKey);
        Entity bazEntity = Entity.newBuilder(bazKey)
            .set("timestamp", bazTimestamp)
            .set("city", bazCity)
            .set("state", bazState)
            .set("text", bazText)
            .set("ALERT", bazFeelings.get(PanasFeelings.ALERT).ordinal())
            .set("PROUD", bazFeelings.get(PanasFeelings.PROUD).ordinal())
            .build();

        datastore.add(fooEntity, barEntity, bazEntity);
    }
}
