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
import java.util.HashMap;
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
 * Tests for the SurveyServlet and SurveyResponse classes.
 *
 * Skeleton code for Datastore emulator from:
 * https://stackoverflow.com/questions/40348653/google-datastore-emulator-using-java-not-using-gae
 */
public class SurveyServletAndResponseTest {

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
        keyFactory = datastore.newKeyFactory().setKind("TestEntity");
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

    @Test
    public void test1() {
        // Stores an entity in the datastore and retrieves it later

        // Create an Entity "TestEntity"
        Entity.Builder builder = Entity.newBuilder(keyFactory.newKey(42));
        builder.set("name", "Test1");

        // Store it in datastore
        datastore.put(builder.build());

        // Retrieve entity by key
        Entity entity = datastore.get(keyFactory.newKey(42));
        assertNotNull(entity);
        assertEquals("Test1", entity.getString("name"));
    }

    @Test
    public void test2() {
        // Try to access the entity created in test1, shouldn't work because
        // of calling reset in tearDown() after each test.

        // Try to retrieve entity by key
        Entity entity = datastore.get(keyFactory.newKey(42));
        assertNull(entity);
    }

    
    // Creating SurveyResponse instances to use in tests:

    private static final Map<PanasFeelings, PanasIntensity> fooFeelings = new HashMap<>();
    private static final Map<PanasFeelings, PanasIntensity> barFeelings = new HashMap<>();
    private static final Map<PanasFeelings, PanasIntensity> bazFeelings = new HashMap<>();

    @BeforeClass
    public static void buildFeelingMaps() {
        fooFeelings.put(PanasFeelings.JITTERY, PanasIntensity.EXTREMELY);
        fooFeelings.put(PanasFeelings.ALERT, PanasIntensity.QUITE_A_BIT);
        fooFeelings.put(PanasFeelings.UPSET, PanasIntensity.QUITE_A_BIT);
        barFeelings.put(PanasFeelings.JITTERY, PanasIntensity.EXTREMELY);
        barFeelings.put(PanasFeelings.ALERT, PanasIntensity.EXTREMELY);
        barFeelings.put(PanasFeelings.AFRAID, PanasIntensity.QUITE_A_BIT);
        barFeelings.put(PanasFeelings.NERVOUS, PanasIntensity.QUITE_A_BIT);
        bazFeelings.put(PanasFeelings.ALERT, PanasIntensity.QUITE_A_BIT);
        bazFeelings.put(PanasFeelings.PROUD, PanasIntensity.QUITE_A_BIT);
    }

    private static final String fooText = "I feel many things";
    private static final String barText = "COVID is making me sad";
    private static final String bazText = "I'm a movie star";

    private static final String fooZipcode = "02142";
    private static final String barZipcode = "33442";
    private static final String bazZipcode = "90210";

    private static final long fooTimestamp = 1595706791802L;
    private static final long barTimestamp = 1595795898000L;
    private static final long bazTimestamp = 1595706828426L;

    /**
     * Builds SurveyResponse instances out of the test data.
     */
    private Map<String, SurveyResponse> generateExpectedData(boolean sameUser) {
        
        final SurveyResponse fooSurveyResponse = new SurveyResponse(
            "Foo",
            fooFeelings,
            fooText,
            fooZipcode,
            fooTimestamp
        );

        final String barUser;
        final String bazUser;

        if (sameUser) {
            barUser = "Foo";
            bazUser = "Foo";
        } else {
            barUser = "Bar";
            bazUser = "Baz";
        }

        final SurveyResponse barSurveyResponse = new SurveyResponse(
            barUser,
            barFeelings,
            barText,
            barZipcode,
            barTimestamp
        );
        final SurveyResponse bazSurveyResponse = new SurveyResponse(
            bazUser,
            bazFeelings,
            bazText,
            bazZipcode,
            bazTimestamp
        );

        final Map<String, SurveyResponse> expectedData = new HashMap<>();
        expectedData.put("Foo", fooSurveyResponse);
        expectedData.put("Bar", barSurveyResponse);
        expectedData.put("Baz", bazSurveyResponse);

        return expectedData;
    }

    /**
     * Loads Entities corresponding to the test data into the local datastore instance.
     */
    private void loadTestData(boolean sameUser) {
        IncompleteKey fooIncompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", "Foo"))
            .setKind("SurveyResponse")
            .newKey();
        Key fooKey = datastore.allocateId(fooIncompleteKey);
        Entity fooEntity = Entity.newBuilder(fooKey)
            .set("timestamp", fooTimestamp)
            .set("zipcode", fooZipcode)
            .set("text", fooText)
            .set("JITTERY", fooFeelings.get(PanasFeelings.JITTERY).getValue())
            .set("ALERT", fooFeelings.get(PanasFeelings.ALERT).getValue())
            .set("UPSET", fooFeelings.get(PanasFeelings.UPSET).getValue())
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
            .newKey();
        Key barKey = datastore.allocateId(barIncompleteKey);
        Entity barEntity = Entity.newBuilder(barKey)
            .set("timestamp", barTimestamp)
            .set("zipcode", barZipcode)
            .set("text", barText)
            .set("JITTERY", barFeelings.get(PanasFeelings.JITTERY).getValue())
            .set("ALERT", barFeelings.get(PanasFeelings.ALERT).getValue())
            .set("AFRAID", barFeelings.get(PanasFeelings.AFRAID).getValue())
            .set("NERVOUS", barFeelings.get(PanasFeelings.NERVOUS).getValue())
            .build();

        IncompleteKey bazIncompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", bazUser))
            .setKind("SurveyResponse")
            .newKey();
        Key bazKey = datastore.allocateId(bazIncompleteKey);
        Entity bazEntity = Entity.newBuilder(bazKey)
            .set("timestamp", bazTimestamp)
            .set("zipcode", bazZipcode)
            .set("text", bazText)
            .set("ALERT", bazFeelings.get(PanasFeelings.ALERT).getValue())
            .set("PROUD", bazFeelings.get(PanasFeelings.PROUD).getValue())
            .build();

        datastore.add(fooEntity, barEntity, bazEntity);
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


    // SurveyResponse tests

    @Test
    public void testGetUser() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        assertEquals("Foo", expectedData.get("Foo").getUser());
        assertEquals("Bar", expectedData.get("Bar").getUser());
        assertEquals("Baz", expectedData.get("Baz").getUser());
    }

    @Test
    public void testGetFeelings() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        assertEquals(fooFeelings, expectedData.get("Foo").getFeelings());
        assertEquals(barFeelings, expectedData.get("Bar").getFeelings());
        assertEquals(bazFeelings, expectedData.get("Baz").getFeelings());
        Map<PanasFeelings, PanasIntensity> immutableMap = expectedData.get("Foo").getFeelings();
        assertThrows(UnsupportedOperationException.class, () -> {
            immutableMap.put(PanasFeelings.PROUD, PanasIntensity.QUITE_A_BIT);
        });
    }

    @Test
    public void testGetText() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        assertEquals(fooText, expectedData.get("Foo").getText());
        assertEquals(barText, expectedData.get("Bar").getText());
        assertEquals(bazText, expectedData.get("Baz").getText());
    }

    @Test
    public void testGetZipcode() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        assertEquals(fooZipcode, expectedData.get("Foo").getZipcode());
        assertEquals(barZipcode, expectedData.get("Bar").getZipcode());
        assertEquals(bazZipcode, expectedData.get("Baz").getZipcode());
    }

    @Test
    public void testGetTimestamp() {
        Map<String, SurveyResponse> expectedData = generateExpectedData(false);
        assertEquals(fooTimestamp, expectedData.get("Foo").getTimestamp());
        assertEquals(barTimestamp, expectedData.get("Bar").getTimestamp());
        assertEquals(bazTimestamp, expectedData.get("Baz").getTimestamp());
    }
}
