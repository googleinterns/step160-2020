package com.google.sps.servlets;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Class that handles incoming and outgoing PANAS survey responses.*/
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
    public static Set<SurveyResponse> queryByFeeling(PanasFeelings feeling) {
        throw new UnsupportedOperationException();
    }

    /** 
    * Queries the project's DataStore and returns the set of {@code SurveyResponse} instances 
    * that represent the survey responses submitted by {@code user}.
    */
    public static Set<SurveyResponse> queryByUser(String user) {
        throw new UnsupportedOperationException();
    }

    /** 
    * Queries the project's DataStore and returns an ordered list of the three {@code PanasFeelings} 
    * with the highest count included in survey responses, descending. Tie breaks are won by the 
    * feeling included most recently in a survey response, and then alphabetically.
    */
    public static List<PanasFeelings> queryMostWidespread() {
        throw new UnsupportedOperationException();
    }

    /** 
    * Queries the project's DataStore and returns an ordered list of the three {@code PanasFeelings} 
    * with the highest average intensities included in survey responses, descending. Tie breaks are 
    * won by the feeling included most recently in a survey response, and then alphabetically.
    */
    public static List<PanasFeelings> queryMostIntense() {
        throw new UnsupportedOperationException();
    }
}
