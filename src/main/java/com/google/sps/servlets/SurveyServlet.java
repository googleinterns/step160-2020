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

    public Set<SurveyResponse> queryByUser(String user) {}

    public List<PanasFeelings> queryMostWidespread() {}

    public List<PanasFeelings> queryMostIntense() {}

    class SurveyResponse {

        public SurveyResponse(String user, 
            Map<PanasFeelings, PanasIntensity> feelings,
            String text,
            String zipcode
            long timestamp) {}

        public String getUser() {}

        public Map<PanasFeelings, PanasIntensity> getFeelings() {}

        public String getText() {}

        public String getZipcode() {}

        public String getTimpestamp() {}

    }
}
