package com.google.sps.servlets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.util.Map;
import org.junit.Test;

/**
 * Tests for the SurveyResponse class.
 *
 * Make sure to set the project you're working on in your shell with:
 * gcloud config set project [PROJECT_NAME]
 */
public class SurveyResponseTest extends TestData {

    private static final Map<String, SurveyResponse> expectedData = 
        generateExpectedData(false);

    @Test
    public void testUser() {
        assertEquals("Foo", expectedData.get("Foo").user());
        assertEquals("Bar", expectedData.get("Bar").user());
        assertEquals("Baz", expectedData.get("Baz").user());
    }

    @Test
    public void testFeelings() {
        assertEquals(fooFeelings, expectedData.get("Foo").feelings());
        assertEquals(barFeelings, expectedData.get("Bar").feelings());
        assertEquals(bazFeelings, expectedData.get("Baz").feelings());
        Map<PanasFeelings, PanasIntensity> immutableMap = expectedData.get("Foo").feelings();
        assertThrows(UnsupportedOperationException.class, () -> {
            immutableMap.put(PanasFeelings.PROUD, PanasIntensity.QUITE_A_BIT);
        });
    }

    @Test
    public void testText() {
        assertEquals(fooText, expectedData.get("Foo").text());
        assertEquals(barText, expectedData.get("Bar").text());
        assertEquals(bazText, expectedData.get("Baz").text());
    }

    @Test
    public void testCity() {
        assertEquals(fooCity, expectedData.get("Foo").city());
        assertEquals(barCity, expectedData.get("Bar").city());
        assertEquals(bazCity, expectedData.get("Baz").city());
    }

    @Test
    public void testState() {
        assertEquals(fooState, expectedData.get("Foo").state());
        assertEquals(barState, expectedData.get("Bar").state());
        assertEquals(bazState, expectedData.get("Baz").state());
    }

    @Test
    public void testTimestamp() {
        assertEquals(fooTimestamp, expectedData.get("Foo").timestamp());
        assertEquals(barTimestamp, expectedData.get("Bar").timestamp());
        assertEquals(bazTimestamp, expectedData.get("Baz").timestamp());
    }

}