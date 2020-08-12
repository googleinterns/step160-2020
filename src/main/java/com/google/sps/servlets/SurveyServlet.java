package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.cloud.datastore.PathElement;
import com.google.cloud.datastore.ProjectionEntity;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Value;
import com.google.common.collect.ImmutableMap;
import com.google.cloud.Timestamp;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** Class that handles incoming and outgoing PANAS survey responses. */
@WebServlet("/survey")
public class SurveyServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = request.getParameter("user");
        long timestamp = System.currentTimeMillis();
        String city = request.getParameter("city");
        String state = request.getParameter("state");
        String text = request.getParameter("text");

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        IncompleteKey incompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", user))
            .setKind("SurveyResponse")
            .setProjectId("manage-at-scale-step-2020")
            .newKey();
        Key key = datastore.allocateId(incompleteKey);
        Entity.Builder surveyResponseEntityBuilder = Entity.newBuilder(key)
            .set("timestamp", timestamp)
            .set("city", city)
            .set("state", state)
            .set("text", text);

        for (PanasFeelings feeling : PanasFeelings.values()) {
            String intensity = request.getParameter(feeling.name());
            surveyResponseEntityBuilder.set(
                feeling.name(),
                PanasIntensity.valueOf(intensity).ordinal()
            );
        }

        Entity surveyResponseEntity = surveyResponseEntityBuilder.build();
        datastore.add(surveyResponseEntity);

        response.addHeader("Access-Control-Allow-Origin", "*");  // change to URL of React app once it's deployed
        response.sendRedirect("/index.html");
    }

    /** 
    * Queries the project's DataStore and returns the set of {@code SurveyResponse} instances 
    * that represent the survey responses containing {@code feeling} at an intensity greater 
    * than 0 ("not at all" on the PANAS survey).
    */
    public static Set<SurveyResponse> queryByFeeling(PanasFeelings feeling) {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind("SurveyResponse")
            .setFilter(PropertyFilter.gt(feeling.name(), 0))
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
        Map<PanasFeelings, PanasIntensity> mutableFeelings = new HashMap<>();
        for (PanasFeelings feeling : PanasFeelings.values()) {
            mutableFeelings.put(
                feeling, 
                PanasIntensity.values[(int) entity.getLong(feeling.name())]);
        }

        ImmutableMap<PanasFeelings, PanasIntensity> feelings = ImmutableMap.copyOf(mutableFeelings);
        SurveyResponse result = SurveyResponse.builder()
            .setUser(entity.getKey().getParent().getName())
            .setFeelings(feelings)
            .setText(entity.getString("text"))
            .setCity(entity.getString("city"))
            .setState(entity.getString("state"))
            .setTimestamp(entity.getLong("timestamp"))
            .build();

        return result;
    }

    /** 
    * Queries the project's DataStore and returns an ordered list of the three {@code PanasFeelings} 
    * with the highest count included in survey responses at an intensity greater than 0, descending. Tie
    * breaks are won by the feeling included most recently in a survey response, and then alphabetically.
    */
    public static List<PanasFeelings> queryMostWidespread() {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        List<PanasFeelings> mostWidespread = new ArrayList<>();
        Map<PanasFeelings, Integer> feelingCount = new HashMap<>();
        Map<PanasFeelings, Long> timestamps = new HashMap<>();
        for (PanasFeelings feeling : PanasFeelings.values()) {
            Query<ProjectionEntity> query = Query.newProjectionEntityQueryBuilder()
                .setKind("SurveyResponse")
                .setFilter(PropertyFilter.gt(feeling.name(), 0))
                .setProjection(feeling.name(), "timestamp")
                .build();
            QueryResults<ProjectionEntity> queryResults = datastore.run(query);

            int count = 0;
            while (queryResults.hasNext()) {
                ProjectionEntity entity = queryResults.next();
                count++;
                long entityTimestamp = entity.getLong("timestamp");
                if (!timestamps.containsKey(feeling) || entityTimestamp > timestamps.get(feeling)) {
                    timestamps.put(feeling, entityTimestamp);
                }
            }
            
            if (count > 0) {
                feelingCount.put(feeling, count);
                long timestamp = timestamps.get(feeling);

                int indexToPlaceFeeling = 0;
                for (PanasFeelings rankedFeeling : mostWidespread) {
                    int rankedFeelingCount = feelingCount.get(rankedFeeling);
                    long rankedFeelingTimestamp = timestamps.get(rankedFeeling);
                    if (count > rankedFeelingCount) {
                        break;
                    } else if (count == rankedFeelingCount && timestamp > rankedFeelingTimestamp) {
                        break;
                    } else if (count == rankedFeelingCount && 
                        timestamp == rankedFeelingTimestamp &&
                        (feeling.name()).compareTo(rankedFeeling.name()) < 0) {
                        break;
                    }
                    indexToPlaceFeeling++;
                }
                mostWidespread.add(indexToPlaceFeeling, feeling);
            }
        }
        return mostWidespread.subList(0, (mostWidespread.size() < 3 ? mostWidespread.size() : 3));
    }

    /** 
    * Queries the project's DataStore and returns an ordered list of the three {@code PanasFeelings} 
    * with the highest average intensities included in survey responses, descending. Tie breaks are 
    * won by the feeling included most recently in a survey response, and then alphabetically.
    */
    public static List<PanasFeelings> queryMostIntense() {
        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();

        List<PanasFeelings> mostIntense = new ArrayList<>();
        Map<PanasFeelings, Double> feelingAverages = new HashMap<>();
        Map<PanasFeelings, Long> timestamps = new HashMap<>();
        for (PanasFeelings feeling : PanasFeelings.values()) {
            Query<ProjectionEntity> query = Query.newProjectionEntityQueryBuilder()
                .setKind("SurveyResponse")
                .setProjection(feeling.name(), "timestamp")
                .build();
            QueryResults<ProjectionEntity> queryResults = datastore.run(query);

            double sum = 0;
            int count = 0;
            while (queryResults.hasNext()) {
                ProjectionEntity entity = queryResults.next();
                sum += (int) entity.getLong(feeling.name());
                count++;
                long entityTimestamp = entity.getLong("timestamp");
                // making sure the timestamps map has the most recent timestamp for each feeling
                if (!timestamps.containsKey(feeling) || entityTimestamp > timestamps.get(feeling)) {
                    timestamps.put(feeling, entityTimestamp);
                }
            }
            
            if (count > 0) {
                double average = sum/count;
                feelingAverages.put(feeling, average);
                long timestamp = timestamps.get(feeling);

                int indexToPlaceFeeling = 0;
                for (PanasFeelings rankedFeeling : mostIntense) {
                    double rankedFeelingAverage = feelingAverages.get(rankedFeeling);
                    long rankedFeelingTimestamp = timestamps.get(rankedFeeling);
                    if (average > rankedFeelingAverage) {
                        break;
                    } else if (average == rankedFeelingAverage && timestamp > rankedFeelingTimestamp) {
                        break;
                    } else if (average == rankedFeelingAverage && 
                        timestamp == rankedFeelingTimestamp &&
                        (feeling.name()).compareTo(rankedFeeling.name()) < 0) {
                        break;
                    }
                    indexToPlaceFeeling++;
                }
                mostIntense.add(indexToPlaceFeeling, feeling);
            }
        }
        return mostIntense.subList(0, (mostIntense.size() < 3 ? mostIntense.size() : 3));
    }
}
