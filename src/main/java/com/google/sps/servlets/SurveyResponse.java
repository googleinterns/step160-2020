package com.google.sps.servlets;

import com.google.auto.value.AutoValue;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/** A value class that represents PANAS survey responses.*/
@AutoValue
abstract class SurveyResponse {

    /** 
     * Create a SurveyResponse instance representing a single survey response submitted 
     * by {@code user}, processed by the server at {@code timestamp} UNIX time, and containing 
     * the message {@code text}, the zipcode {@code zipcode} (provided by the user), and the 
     * feelings and respective intensities indicated on the survey as the map {@code feelings}. 
     */
    static SurveyResponse create(String user, 
        Map<PanasFeelings, PanasIntensity> feelings,
        String text,
        String zipcode,
        long timestamp) {
        return new AutoValue_SurveyResponse(user, feelings, text, zipcode, timestamp);
    }

    /** Returns the username included in the survey response.*/
    abstract String user();

    /**
    * Returns an immutable map representing the feelings (and their intensities) included 
    * in the survey response.
    */
    abstract Map<PanasFeelings, PanasIntensity> feelings();

    /** Returns the text included in the survey response.*/
    abstract String text();

    /** Returns the zipcode included in the survey response as a String.*/
    abstract String zipcode();

    /**
    * Returns the timestamp at which the survey response was processed by the server, in 
    * UNIX time.
    */
    abstract long timestamp();
}
