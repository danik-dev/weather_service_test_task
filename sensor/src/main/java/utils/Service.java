package utils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class Service {
    private Service(){}
    public static String register(String sensorName) {
        String jsonBody = "{\"name\": \"" + sensorName + "\"}";
        HttpClient httpClient = HttpClient.newHttpClient();

        HttpRequest request ;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/sensors/registration"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            String responseBody = response.body();
            if (statusCode == 200) {
                System.out.println("Registration completed for " + sensorName);
                System.out.println(sensorName + " " + responseBody);
                return responseBody.substring(responseBody.indexOf(" ") + 1);
            } else {
                System.err.println((sensorName + ". " + responseBody));
                return null;
            }
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void send_data(String sensorName, String key, Double value, boolean raining) {
        HttpClient httpClient = HttpClient.newHttpClient();
        String jsonBody = "{\"value\": \"" + value + "\", \"raining\": \"" + raining +"\"}";
        HttpRequest request;
        try {
            request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/sensors/" + key + "/measurement"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(jsonBody, StandardCharsets.UTF_8))
                    .build();
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            System.out.println(sensorName + " sends data. Response Code: " + statusCode);
        } catch (URISyntaxException | IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
