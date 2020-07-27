package com.google.sps.servlets;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.HashSet;
import java.util.ArrayList;

// TODO add all the comments
public class SurveyResponse {

    public SurveyResponse(String user, 
        Map<PanasFeelings, PanasIntensity> feelings,
        String text,
        String zipcode,
        long timestamp) {
        throw new UnsupportedOperationException();
    }

    /** Returns the username included in the survey response.*/
    public String getUser() {
        throw new UnsupportedOperationException();
    }

    /**
    * Returns an immutable map representing the feelings (and their intensities) included 
    * in the survey response.
    */
    public Map<PanasFeelings, PanasIntensity> getFeelings() {
        throw new UnsupportedOperationException();
    }

    /** Returns the text included in the survey response.*/
    public String getText() {
        throw new UnsupportedOperationException();
    }

    /** Returns the zipcode included in the survey response as a String.*/
    public String getZipcode() {
        throw new UnsupportedOperationException();
    }

    /**
    * Returns the timestamp at which the survey response was processed by the server, in 
    * UNIX time.
    */
    public long getTimestamp() {
        throw new UnsupportedOperationException();
    }

}