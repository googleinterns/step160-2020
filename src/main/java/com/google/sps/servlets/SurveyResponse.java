package com.google.sps.servlets;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** An immutable data type that represents PANAS survey responses.*/
public class SurveyResponse {

    /** 
     * Create a SurveyResponse instance representing a single survey response submitted 
     * by {@code user}, processed by the server at {@code timestamp} UNIX time, and containing 
     * the message {@code text}, the zipcode {@code zipcode} (provided by the user), and the 
     * feelings and respective intensities indicated on the survey as the map {@code feelings}. 
     */
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