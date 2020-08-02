package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Value;
import com.google.common.collect.ImmutableMap;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind("SurveyResponse")
            .setFilter(PropertyFilter.ge(feeling.name(), 0))
            .build();
        QueryResults<Entity> queryResults = datastore.run(query);

        Set<SurveyResponse> processedResults = new HashSet<>();
        for ( ; queryResults.hasNext(); ) {
            processedResults.add(convertEntityToSurveyResponse(queryResults.next()));
        }

        return processedResults;
    }

    /** 
    * Queries the project's DataStore and returns the set of {@code SurveyResponse} instances 
    * that represent the survey responses submitted by {@code user}.
    */
    public static Set<SurveyResponse> queryByUser(String user) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind("SurveyResponse")
            .setFilter(PropertyFilter.hasAncestor(
                datastore.newKeyFactory().setKind("User").newKey(user)))
            .build();
        QueryResults<Entity> queryResults = datastore.run(query);

        Set<SurveyResponse> processedResults = new HashSet<>();
        for ( ; queryResults.hasNext(); ) {
            processedResults.add(convertEntityToSurveyResponse(queryResults.next()));
        }

        return processedResults;
    }
    
    /** 
     * Reads the data in the given entity {@code entity} to build and return the corresponding 
     * {@code SurveyResponse} instance.
     */
    private static SurveyResponse convertEntityToSurveyResponse(Entity entity) {
        Set<String> allProperties = entity.getNames();
        Set<String> knownProperties = new HashSet<>();
        knownProperties.add("text");
        knownProperties.add("city");
        knownProperties.add("state");
        knownProperties.add("timestamp");

        Map<PanasFeelings, PanasIntensity> mutableFeelings = new HashMap<>();
        for(String property : allProperties) {
            if (!knownProperties.contains(property)) {
                mutableFeelings.put(PanasFeelings.valueOf(property), 
                    PanasIntensity.values[(int) entity.getLong(property)]);
            }
        }

        ImmutableMap<PanasFeelings, PanasIntensity> feelings = ImmutableMap.copyOf(mutableFeelings);

        SurveyResponse result = SurveyResponse.create(
            entity.getKey().getParent().getName(), 
            feelings,
            entity.getString("text"),
            entity.getString("city"),
            entity.getString("state"),
            entity.getLong("timestamp"));

        return result;
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
