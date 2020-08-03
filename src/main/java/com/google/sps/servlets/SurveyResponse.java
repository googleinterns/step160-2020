package com.google.sps.servlets;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableMap;
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
     * Builder method for SurveyResponse.
     */
    static Builder builder() {
        return new AutoValue_SurveyResponse.Builder();
    }

    /**
     * Builder pattern implementation for SurveyResponse, representing a single survey response 
     * submitted by {@code user}, processed by the server at {@code timestamp} UNIX time, and 
     * containing the message {@code text}, the city {@code city} and state {@code state} (provided 
     * by the user), and the feelings and respective intensities indicated on the survey as the 
     * map {@code feelings}.
     */
    @AutoValue.Builder
    abstract static class Builder {
        abstract Builder setUser(String user);
        abstract Builder setFeelings(ImmutableMap<PanasFeelings, PanasIntensity> feelings);
        abstract Builder setText(String text);
        abstract Builder setCity(String city);
        abstract Builder setState(String state);
        abstract Builder setTimestamp(long timestamp);
        abstract SurveyResponse build();
    }

    /** Returns the username included in the survey response.*/
    abstract String user();

    /**
    * Returns an immutable map representing the feelings (and their intensities) included 
    * in the survey response.
    */
    abstract ImmutableMap<PanasFeelings, PanasIntensity> feelings();

    /** Returns the text included in the survey response.*/
    abstract String text();

    /** Returns the city named in the survey response.*/
    abstract String city();

    /** Returns the state named in the survey response.*/
    abstract String state();

    /**
    * Returns the timestamp at which the survey response was processed by the server, in 
    * UNIX time.
    */
    abstract long timestamp();
}
