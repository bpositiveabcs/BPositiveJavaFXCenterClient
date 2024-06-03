package bpos.centerclient.RestComunication.services;

import bpos.centerclient.CenterResponse;
import bpos.centerclient.DonationRequest;
import bpos.common.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

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

           if (response.statusCode()/100 == 2) {
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

    public List<Event> allEventsForCenter(int centerId) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL2 + "/events/center-id-event?centruID=" + centerId))
                .GET()
                .build();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);

            // Deserialize the JSON array into a list of Event objects
            List<Event> events = objectMapper.readValue(responseBody, new TypeReference<List<Event>>() {
            });

            return events;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Student> findAllStudents() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL2 + "/personActorService/students"))
                .GET()
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);

            // Deserialize the JSON array into a list of Student objects
            List<Student> students = objectMapper.readValue(responseBody, new TypeReference<List<Student>>() {});
            return students;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Person> findAllPersons() {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL2 + "/personActorService/only-persons"))
                .GET()
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println(responseBody);

            if (statusCode == 200) {
                // Deserialize the JSON array into a list of Person objects
                return objectMapper.readValue(responseBody, new TypeReference<List<Person>>() {});
            } else {
                System.err.println("Error fetching persons: " + statusCode);
                return Collections.emptyList();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Person> findByCnpPerson(String cnp) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL2 + "/personActorService/persons/cnp?cnp=" + cnp))
                .GET()
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println(responseBody);

            if (statusCode == 200) {
                return objectMapper.readValue(responseBody, new TypeReference<List<Person>>() {});
            } else {
                System.err.println("Error fetching persons by CNP: " + statusCode);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public List<Student> findByCnpStudent(String cnp) {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL2 + "/personActorService/students/cnp?cnp=" + cnp))
                .GET()
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            System.out.println(responseBody);

            if (statusCode == 200) {
                return objectMapper.readValue(responseBody, new TypeReference<List<Student>>() {});
            } else {
                System.err.println("Error fetching students by CNP: " + statusCode);
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    public void  addDonation(Donation donation, Person person) {
        System.out.println(donation);
        String requestBody = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            DonationRequest donationRequest = new DonationRequest();
            donationRequest.setDonation(donation);
            donationRequest.setPerson(person);
            requestBody = objectMapper.writeValueAsString(donationRequest);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL2 + "/donation"))
                .method("POST", HttpRequest.BodyPublishers.ofString(requestBody))
                .header("Content-Type", "application/json")
                .build();
        System.out.println(request);

        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            String responseBody = response.body();
            System.out.println(responseBody);

            if (response.statusCode() / 100 == 2) {
                ObjectMapper objectMapper = new ObjectMapper();
                objectMapper.registerModule(new JavaTimeModule());
                objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
                Map<String, String> responseMap = objectMapper.readValue(responseBody, new TypeReference<Map<String, String>>() {});
                System.out.println("Success: " + responseMap.get("message"));
            } else {
                System.err.println("Failed to save donation: " + response.body());
            }
        } catch (IOException | InterruptedException ex) {
            throw new RuntimeException(ex);
        }
    }



    public void uploadFileToServer(File file, String username) {
        try {
            URL url = new URL(BASE_URL+"/upload-medical-info/" + username);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=***");

            DataOutputStream request = new DataOutputStream(connection.getOutputStream());

            request.writeBytes("--***\r\n");
            request.writeBytes("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getName() + "\"\r\n");
            request.writeBytes("\r\n");

            FileInputStream fileInputStream = new FileInputStream(file);
            int bytesRead;
            byte[] buffer = new byte[1024];
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                request.write(buffer, 0, bytesRead);
            }
            fileInputStream.close();

            request.writeBytes("\r\n");
            request.writeBytes("--***--\r\n");
            request.flush();
            request.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                System.out.println("File uploaded to server successfully!");
            } else {
                System.out.println("Failed to upload file to server: " + responseCode);
            }
        } catch (IOException e) {
            System.out.println("Failed to upload file to server: " + e.getMessage());
        }
    }

//    public Donation addDonation(Donation donation) {
//        System.out.println(donation);
//        String donationJson = null;
//        try {
//            ObjectMapper objectMapper = new ObjectMapper();
//            objectMapper.registerModule(new JavaTimeModule());
//            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//            donationJson = objectMapper.writeValueAsString(donation);
//        } catch (JsonProcessingException ex) {
//            throw new RuntimeException(ex);
//        }
//
//        HttpRequest request = HttpRequest.newBuilder()
//                .uri(URI.create(BASE_URL2 + "/donations"))
//                .method("POST", HttpRequest.BodyPublishers.ofString(donationJson))
//                .header("Content-Type", "application/json")
//                .build();
//        System.out.println(request);
//
//        HttpResponse<String> response = null;
//        try {
//            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
//            String responseBody = response.body();
//            System.out.println(responseBody);
//
//            if (response.statusCode() / 100 == 2) {
//                try {
//                    ObjectMapper objectMapper = new ObjectMapper();
//                    objectMapper.registerModule(new JavaTimeModule());
//                    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
//                    return objectMapper.readValue(responseBody, Donation.class);
//                } catch (JsonProcessingException ex) {
//                    throw new RuntimeException(ex);
//                }
//            } else {
//                System.err.println("Failed to save donation: " + response.body());
//                return null;
//            }
//        } catch (IOException | InterruptedException ex) {
//            throw new RuntimeException(ex);
//        }
//    }



}


