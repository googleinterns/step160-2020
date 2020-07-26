package com.google.sps.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Class that handles survey data.*/
public class SurveyServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        throw new UnsupportedOperationException();
    }

    /** 
    * Queries the project's DataStore and returns the set of {@code SurveyResponse} instances 
    * that represent the survey responses containing {@code feeling}.
    */
    public Set<SurveyResponse> queryByFeeling(PanasFeelings feeling) {}

    /** 
    * Queries the project's DataStore and returns the set of {@code SurveyResponse} instances 
    * that represent the survey responses submitted by {@code user}.
    */
    public Set<SurveyResponse> queryByUser(String user) {}

    /** 
    * Queries the project's DataStore and returns an ordered list of the three {@code PanasFeelings} 
    * with the highest count included in survey responses, descending. Tie breaks are won by the 
    * feeling included most recently in a survey response, and then alphabetically.
    */
    public List<PanasFeelings> queryMostWidespread() {}

    /** 
    * Queries the project's DataStore and returns an ordered list of the three {@code PanasFeelings} 
    * with the highest average intensities included in survey responses, descending. Tie breaks are 
    * won by the feeling included most recently in a survey response, and then alphabetically.
    */
    public List<PanasFeelings> queryMostIntense() {}

    // TODO add all the comments
    class SurveyResponse {

        public SurveyResponse(String user, 
            Map<PanasFeelings, PanasIntensity> feelings,
            String text,
            String zipcode
            long timestamp) {}

        /** Returns the username included in the survey response.*/
        public String getUser() {}

        /**
        * Returns an immutable map representing the feelings (and their intensities) included 
        * in the survey response.
        */
        public Map<PanasFeelings, PanasIntensity> getFeelings() {}

        /** Returns the text included in the survey response.*/
        public String getText() {}

        /** Returns the zipcode included in the survey response as a String.*/
        public String getZipcode() {}

        /**
        * Returns the timestamp at which the survey response was processed by the server, in 
        * UNIX time.
        */
        public long getTimpestamp() {}

    }
}
