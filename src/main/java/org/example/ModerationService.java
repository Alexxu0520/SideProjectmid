package org.example;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ModerationService {

    private static final String API_URL = "https://api.openai.com/v1/moderations"; // The URL of the OpenAI API endpoint for moderations.
    private final String apiKey; // The API key used to authenticate with the OpenAI API.
    private final HttpClient httpClient;// An instance of Java's HttpClient, which handles sending HTTP requests and receiving responses.
    private final ObjectMapper objectMapper; // An instance of Jackson's ObjectMapper class, used for converting between Java objects and JSON.

    public ModerationService(String apiKey) {
        this.apiKey = apiKey;
        this.httpClient = HttpClient.newHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    //Sending a moderation request to the OpenAI API. It takes a ModerationNewParams object as an argument (which contains the text to be moderated) and returns a ModerationNewResponse object (the result of the moderation).
    public ModerationNewResponse moderate(ModerationNewParams params) throws IOException, InterruptedException {
        String requestBody = objectMapper.writeValueAsString(params); //String (requestBody) will be used as the body of the HTTP POST request.

        //This block creates a new HTTP POST request using the HttpRequest builder. The request includes the API URL, headers for content type and authorization, the request body, and the HTTP method (POST).
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        //The request is sent using the HttpClient instance, and the response is stored in the response variable.
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {  // If the response has a successful status code (200), the JSON body of the response is deserialized (converted) into a ModerationNewResponse object using objectMapper.readValue()
            return objectMapper.readValue(response.body(), ModerationNewResponse.class);
        } else {
            throw new IOException("Error during API call: " + response.body());
        }
    }
}
