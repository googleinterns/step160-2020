package com.google.sps.servlets;

import com.google.cloud.datastore.Datastore;
import com.google.cloud.datastore.DatastoreOptions;
import com.google.cloud.datastore.Entity;
import com.google.cloud.datastore.ProjectionEntity;
import com.google.cloud.datastore.StructuredQuery.OrderBy;
import com.google.cloud.datastore.StructuredQuery.PropertyFilter;
import com.google.cloud.datastore.Query;
import com.google.cloud.datastore.QueryResults;
import com.google.cloud.datastore.Value;
import com.google.cloud.datastore.IncompleteKey;
import com.google.cloud.datastore.Key;
import com.google.cloud.datastore.KeyFactory;
import com.google.common.collect.ImmutableMap;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query.SortDirection;
import com.google.cloud.Timestamp;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.gson.Gson;
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

/** Class that handles incoming and outgoing PANAS survey responses.*/
@WebServlet("/journal")
public class JournalServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long timestamp = System.currentTimeMillis();
        String title = request.getParameter("title");
        String journal = request.getParameter("journal");
        String user = request.getParameter("user");

        Document doc = Document.newBuilder().setContent(journal).setType(Document.Type.PLAIN_TEXT).build();
        LanguageServiceClient languageService = LanguageServiceClient.create();
        Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
        double score = sentiment.getScore();

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        IncompleteKey incompleteKey = datastore.newKeyFactory()
            .addAncestors(PathElement.of("User", user))
            .setKind("JournalResponse")
            .setProjectId("manage-at-scale-step-2020")
            .newKey();
        Key key = datastore.allocateId(incompleteKey);
        Entity JournalEntityBuilder = Entity.newBuilder(key)
            .set("timestamp", timestamp)
            .set("title", title)
            .set("journal", journal)
            .set("score", score)
            .build();
        datastore.add(JournalEntityBuilder);
        languageService.close();

        response.setStatus(HttpServletResponse.SC_OK);
        response.sendRedirect("/");
    }


    
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

    Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        Query<Entity> query = Query.newEntityQueryBuilder()
            .setKind("JournalResponse")
            .setFilter(PropertyFilter.hasAncestor(
                datastore.newKeyFactory().setKind("User").newKey(user)))
            .setOrderBy(OrderBy.desc("timestamp"))
            .build();
        QueryResults<Entity> results = datastore.run(query);

        List<JournalBuild> userJournals = new ArrayList<>();
        for (Entity entity : results.asIterable()) {
            String title = (String) entity.getProperty("title");
            String journal = (String) entity.getProperty("journal");
            long timestamp = (long) entity.getProperty("timestamp");
            double score = (double) entity.getProperty("score");

            JournalBuild singleJournal = new JournalBuild(title, journal, timestamp, score);
            userJournals.add(singleJournal);
            }

            Gson gson = new Gson();
            response.setContentType("application/json;");
            response.getWriter().println(gson.toJson(userJournals));
                
            }
  
    
        public class JournalBuild {
            private final String title;
            private final String journal;
            private final long timestamp;
            private final double score;

        public JournalBuild(String title, String journal, long timestamp, double score){
            this.title = title;
            this.journal = journal;
            this.timestamp = timestamp;
            this.score = score;
      }
  }

}