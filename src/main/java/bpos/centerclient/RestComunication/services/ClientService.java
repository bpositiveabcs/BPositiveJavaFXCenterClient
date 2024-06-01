package bpos.centerclient.RestComunication.services;

import bpos.centerclient.CenterResponse;
import bpos.common.model.Center;
import bpos.common.model.Event;
import bpos.common.model.Person;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ClientService {

    private static final String BASE_URL = "http://localhost:55555/centers";
    private static final String BASE_URL2 = "http://localhost:55555";    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ClientService() {
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }
    public void logout() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/logout"))
                .GET()
                .build();

    }
   public Event addEvent(Event addEvent){
       System.out.println(addEvent);
       String eventJson = null;
       try {
           ObjectMapper objectMapper = new ObjectMapper();
           objectMapper.registerModule(new JavaTimeModule());
           objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
           eventJson = objectMapper.writeValueAsString(addEvent);
       } catch (JsonProcessingException ex) {
           throw new RuntimeException(ex);
       }

       HttpRequest request = HttpRequest.newBuilder()
               .uri(URI.create(BASE_URL2 + "/events"))
               .method("POST", HttpRequest.BodyPublishers.ofString(eventJson))
               .header("Content-Type", "application/json")
               .build();
       System.out.println(request);

       HttpResponse<String> response = null;
       try {
           response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

           String responseBody = response.body();
           System.out.println(responseBody);

           if (response.statusCode() == 200) {
               try {
                   ObjectMapper objectMapper = new ObjectMapper();
                   objectMapper.registerModule(new JavaTimeModule());
                   objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                   Event eventNou= objectMapper.readValue(responseBody, Event.class);
                   return eventNou;
               } catch (JsonProcessingException ex) {
                   throw new RuntimeException(ex);
               }
           } else {
               System.err.println("Failed to update event: " + response.body());
               return null;
           }
       } catch (IOException | InterruptedException ex) {
           throw new RuntimeException(ex);
       }


   }
    public Center login(String username, String password) {
        String params = URLEncoder.encode("username", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(username, StandardCharsets.UTF_8) +
                "&" +
                URLEncoder.encode("password", StandardCharsets.UTF_8) + "=" + URLEncoder.encode(password, StandardCharsets.UTF_8);

        String uriWithParams = BASE_URL + "/login?" + params;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uriWithParams))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            String responseBody = response.body();
            System.out.println(responseBody);
            // Deserializare răspuns JSON în obiectul PersonResponse
            CenterResponse personResponse = objectMapper.readValue(responseBody, CenterResponse.class);

            // Acum poți accesa obiectul Person și lista de evenimente
            System.out.println("Center Name: " + personResponse.getCenterName());
            Center center = personResponse.getCenter();
            return center;
// Afișează obiectul Person
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}


