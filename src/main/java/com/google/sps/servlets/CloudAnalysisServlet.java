package com.google.sps.servlets;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.AnalyzeSyntaxRequest;
import com.google.cloud.language.v1.AnalyzeSyntaxResponse;
import com.google.cloud.language.v1.ClassificationCategory;
import com.google.cloud.language.v1.ClassifyTextRequest;
import com.google.cloud.language.v1.ClassifyTextResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.google.cloud.language.v1.Token;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/cloud")
public class CloudAnalysisServlet extends HttpServlet {

  @Override
  public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String message = request.getParameter("message");

    Document doc = Document.newBuilder().setContent(message).setType(Document.Type.PLAIN_TEXT).build();
    LanguageServiceClient languageService = LanguageServiceClient.create();
    // sentiment analysis 
    Sentiment sentiment = languageService.analyzeSentiment(doc).getDocumentSentiment();
    // sentiment score
    float score = sentiment.getScore();

    //entity analysis
    AnalyzeEntitiesRequest requestEntity =
        AnalyzeEntitiesRequest.newBuilder()
            .setDocument(doc)
            .setEncodingType(EncodingType.UTF16)
            .build();
    // entity list
    AnalyzeEntitiesResponse response1 = languageService.analyzeEntities(requestEntity);

    languageService.close();
    
    // Print the response
    response.setContentType("text/html");
    response.getWriter().println("<h1>Sentiment Analysis</h1>");
    response.getWriter().println("<p>Sentiment analysis score: " + score + "</p>");

    response.getWriter().println("<h1>Entity Analysis</h1>");
    for (Entity entity : response1.getEntitiesList()) {
        response.getWriter().printf("Entity: %s", entity.getName());
        response.getWriter().printf("Salience: %.3f\n", entity.getSalience());
        response.getWriter().println("Metadata: ");

        for (Map.Entry<String, String> entry : entity.getMetadataMap().entrySet()) {
        response.getWriter().printf("%s : %s", entry.getKey(), entry.getValue());
        }
        for (EntityMention mention : entity.getMentionsList()) {
        response.getWriter().printf("Begin offset: %d\n", mention.getText().getBeginOffset());
        response.getWriter().printf("Content: %s\n", mention.getText().getContent());
        response.getWriter().printf("Type: %s\n\n", mention.getType());
        }

    }

  }
}