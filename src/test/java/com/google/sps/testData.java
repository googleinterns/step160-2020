package com.google.sps.servlets;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import org.junit.BeforeClass;

/**
 * Test data and test objects for the SurveyResponseTest and SurveyServletTest classes.
 *
 * Make sure to set the project you're working on in your shell with:
 * gcloud config set project [PROJECT_NAME]
 */
public class testData {

    protected static ImmutableMap<PanasFeelings, PanasIntensity> fooFeelings;
    protected static ImmutableMap<PanasFeelings, PanasIntensity> barFeelings;
    protected static ImmutableMap<PanasFeelings, PanasIntensity> bazFeelings;

    protected static final String fooText = "I feel many things";
    protected static final String barText = "COVID is making me sad";
    protected static final String bazText = "I'm a movie star";

    protected static final String fooCity = "Cambridge";
    protected static final String barCity = "Deerfield Beach";
    protected static final String bazCity = "Beverly Hills";

    protected static final String fooState = "MA";
    protected static final String barState = "FL";
    protected static final String bazState = "CA";

    protected static final long fooTimestamp = 1595706791802L;
    protected static final long barTimestamp = 1595795898000L;
    protected static final long bazTimestamp = 1595706828426L;

    @BeforeClass
    public static void buildFeelingMaps() {
        final Map<PanasFeelings, PanasIntensity> fooMutableFeelings = new HashMap<>();
        final Map<PanasFeelings, PanasIntensity> barMutableFeelings = new HashMap<>();
        final Map<PanasFeelings, PanasIntensity> bazMutableFeelings = new HashMap<>();

        fooMutableFeelings.put(PanasFeelings.JITTERY, PanasIntensity.EXTREMELY);
        fooMutableFeelings.put(PanasFeelings.ALERT, PanasIntensity.QUITE_A_BIT);
        fooMutableFeelings.put(PanasFeelings.UPSET, PanasIntensity.QUITE_A_BIT);
        barMutableFeelings.put(PanasFeelings.JITTERY, PanasIntensity.EXTREMELY);
        barMutableFeelings.put(PanasFeelings.ALERT, PanasIntensity.EXTREMELY);
        barMutableFeelings.put(PanasFeelings.AFRAID, PanasIntensity.QUITE_A_BIT);
        barMutableFeelings.put(PanasFeelings.NERVOUS, PanasIntensity.QUITE_A_BIT);
        bazMutableFeelings.put(PanasFeelings.ALERT, PanasIntensity.QUITE_A_BIT);
        bazMutableFeelings.put(PanasFeelings.PROUD, PanasIntensity.QUITE_A_BIT);

        fooFeelings = ImmutableMap.copyOf(fooMutableFeelings); 
        barFeelings = ImmutableMap.copyOf(barMutableFeelings); 
        bazFeelings = ImmutableMap.copyOf(bazMutableFeelings);
    }

    /**
     * Builds SurveyResponse instances out of the test data.
     */
    protected static Map<String, SurveyResponse> generateExpectedData(boolean sameUser) {
        
        final SurveyResponse fooSurveyResponse = SurveyResponse.create(
            "Foo",
            fooFeelings,
            fooText,
            fooCity,
            fooState,
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

        final SurveyResponse barSurveyResponse = SurveyResponse.create(
            barUser,
            barFeelings,
            barText,
            barCity,
            barState,
            barTimestamp
        );
        final SurveyResponse bazSurveyResponse = SurveyResponse.create(
            bazUser,
            bazFeelings,
            bazText,
            bazCity,
            bazState,
            bazTimestamp
        );

        final Map<String, SurveyResponse> expectedData = new HashMap<>();
        expectedData.put("Foo", fooSurveyResponse);
        expectedData.put("Bar", barSurveyResponse);
        expectedData.put("Baz", bazSurveyResponse);

        return expectedData;
    }
}