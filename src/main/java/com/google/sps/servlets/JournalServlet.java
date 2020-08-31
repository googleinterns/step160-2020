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

/** Class that handles incoming and outgoing PANAS survey responses.*/
@WebServlet("/journal")
public class JournalServlet extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        long timestamp = System.currentTimeMillis();
        String title = request.getParameter("title");
        String journal = request.getParameter("journal");

        Datastore datastore = DatastoreOptions.getDefaultInstance().getService();
        IncompleteKey incompleteKey = datastore.newKeyFactory()
            // .addAncestors(PathElement.of("User", user))
            .setKind("JournalResponse")
            .setProjectId("manage-at-scale-step-2020")
            .newKey();
        Key key = datastore.allocateId(incompleteKey);
        Entity JournalEntityBuilder = Entity.newBuilder(key)
            .set("timestamp", timestamp)
            .set("title", title)
            .set("journal", journal)
            .build();
        datastore.add(JournalEntityBuilder);

        response.addHeader("Access-Control-Allow-Origin", "*");  // change to URL of React app once it's deployed
        response.setStatus(HttpServletResponse.SC_OK);
    }
}