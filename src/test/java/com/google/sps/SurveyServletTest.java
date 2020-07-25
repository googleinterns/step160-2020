
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;
import static org.junit.Assert.assertEquals;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SurveyServletTest {

    private final LocalServiceTestHelper helper =
        new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());

    @Before
    public void setUp() {
        helper.setUp();
    }

    @After
    public void tearDown() {
        helper.tearDown();
    }

    // Run this test twice to prove we're not leaking any state across tests.
    private void doTest() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        assertEquals(0, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
        ds.put(new Entity("yam"));
        ds.put(new Entity("yam"));
        assertEquals(2, ds.prepare(new Query("yam")).countEntities(withLimit(10)));
    }

    @Test
    public void testInsert1() {
        doTest();
    }

    @Test
    public void testInsert2() {
        doTest();
    }


    // Creating SurveyResponse instances to use in queryByFeeling tests:

    private static final Map<PanasFeelings, PanasIntensity> fooFeelings = 
        new HashMap<>();
    fooFeelings.put(PanasFeelings.JITTERY, PanasIntensity.EXTREMELY);
    fooFeelings.put(PanasFeelings.ALERT, PanasIntensity.VERY_SLIGHTLY);
    fooFeelings.put(PanasFeelings.UPSET, PanasIntensity.QUITE_A_BIT);

    private static final Map<PanasFeelings, PanasIntensity> barFeelings = 
        new HashMap<>();
    barFeelings.put(PanasFeelings.JITTERY, PanasIntensity.MODERATELY);
    barFeelings.put(PanasFeelings.ALERT, PanasIntensity.A_LITTLE);
    barFeelings.put(PanasFeelings.AFRAID, PanasIntensity.QUITE_A_BIT);
    barFeelings.put(PanasFeelings.NERVOUS, PanasIntensity.QUITE_A_BIT);

    private static final Map<PanasFeelings, PanasIntensity> bazFeelings = 
        new HashMap<>();
    bazFeelings.put(PanasFeelings.ALERT, PanasIntensity.QUITE_A_BIT);
    bazFeelings.put(PanasFeelings.PROUD, PanasIntensity.QUITE_A_BIT);

    private static final String fooText = "I feel many things";
    private static final String barText = "COVID is making me sad";
    private static final String bazText = "I'm a movie star";

    private static final String fooZipcode = "02142";
    private static final String barZipcode = "33442";
    private static final String bazZipcode = "90210";

    private static final long fooTimestamp = 1595706791802;
    private static final long barTimestamp = 1595706815986;
    private static final long bazTimestamp = 1595706828426;

    private static final SurveyResponse fooSurveyResponse = new SurveyResponse(
        "Foo",
        fooFeelings,
        fooText,
        fooZipcode,
        fooTimestamp
    );
    private static final SurveyResponse barSurveyResponse = new SurveyResponse(
        "Bar",
        barFeelings,
        barText,
        barZipcode,
        barTimestamp
    );
    private static final SurveyResponse bazSurveyResponse = new SurveyResponse(
        "Baz",
        bazFeelings,
        bazText,
        bazZipcode,
        bazTimestamp
    );


    private void loadQueryByFeelingTestData(DatastoreService ds) {
        Key fooKey = ds.newKeyFactory()
            .addAncestors(PathElement.of("User", "Foo"))
            .setKind("SurveyResponse")
            .newKey();
        Entity fooEntity = Entity.newBuilder(fooKey)
            .set("timestamp", fooTimestamp)
            .set("zipcode", fooZipcode)
            .set("text", fooText)
            .set("JITTERY", fooFeelings.get(PanasFeelings.JITTERY).getValue());
            .set("ALERT", fooFeelings.get(PanasFeelings.ALERT).getValue()))
            .set("UPSET", fooFeelings.get(PanasFeelings.UPSET).getValue()))
            .build();

        Key barKey = ds.newKeyFactory()
            .addAncestors(PathElement.of("User", "Bar"))
            .setKind("SurveyResponse")
            .newKey();
        Entity barEntity = Entity.newBuilder(barKey)
            .set("timestamp", barTimestamp)
            .set("zipcode", barZipcode)
            .set("text", barText)
            .set("JITTERY", barFeelings.get(PanasFeelings.JITTERY).getValue())
            .set("ALERT", barFeelings.get(PanasFeelings.ALERT).getValue())
            .set("AFRAID", barFeelings.get(PanasFeelings.AFRAID).getValue())
            .set("NERVOUS", barFeelings.get(PanasFeelings.NERVOUS).getValue())
            .build();

        Key bazKey = ds.newKeyFactory()
            .addAncestors(PathElement.of("User", "Baz"))
            .setKind("SurveyResponse")
            .newKey();
        Entity bazEntity = Entity.newBuilder(bazKey)
            .set("timestamp", bazTimestamp)
            .set("zipcode", bazZipcode
            .set("text", bazText)
            .set("ALERT", bazFeelings.get(PanasFeelings.ALERT).getValue())
            .set("PROUD", bazFeelings.get(PanasFeelings.PROUD).getValue())
            .build();

        ds.add(fooEntity, barEntity, bazEntity);
    }

    @Test
    public void testQueryByFeelingSome() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        loadQueryByFeelingTestData(ds);
        Set<SurveyResponse> expected = Set.of(fooSurveyResponse, barSurveyResponse);
        assertEquals(expected, SurveyServlet.queryByFeeling(PanasFeelings.JITTERY));
    }

    @Test
    public void testQueryByFeelingAll() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        loadQueryByFeelingTestData(ds);
        Set<SurveyResponse> expected = Set.of(fooSurveyResponse, barSurveyResponse, bazSurveyResponse);
        assertEquals(expected, SurveyServlet.queryByFeeling(PanasFeelings.ALERT));
    }

    @Test
    public void testQueryByFeelingNone() {
        DatastoreService ds = DatastoreServiceFactory.getDatastoreService();
        loadQueryByFeelingTestData(ds);
        Set<SurveyResponse> expected = Set.of();
        assertEquals(expected, SurveyServlet.queryByFeeling(PanasFeelings.HOSTILE));
    }
}