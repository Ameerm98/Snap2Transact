package com.example.snap2transact.services;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.http.*;
import java.net.URI;
import java.io.IOException;
import java.net.http.HttpResponse.BodyHandlers;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;


@Service
public class ExtractaAPIService {

    private static final String API_KEY = "LTE0MjY2MDY0NTk=_w6lpmvs7chej9og1hgmpld";
    private static final String BASE_URL = "https://api.extracta.ai/api/v1/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    public JsonNode createExtraction(String extractionDetailsJson ) {
        String url = "https://api.extracta.ai/api/v1/createExtraction";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + API_KEY)
                .POST(BodyPublishers.ofString(extractionDetailsJson))
                .build();

        try {
            HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
            if (response.statusCode() >= 200 && response.statusCode() < 300) {
                return objectMapper.readTree(response.body());
            } else {
                System.err.println("Error: " + response.statusCode() + " " + response.body());
                return null;
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public JsonNode uploadFiles(String extractionId, MultipartFile file) throws IOException, InterruptedException {
        String boundary = UUID.randomUUID().toString();
        HttpClient client = HttpClient.newHttpClient();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(new OutputStreamWriter(bos, StandardCharsets.UTF_8), true);

        // Write the extractionId part
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"extractionId\"").append("\r\n");
        writer.append("Content-Type: text/plain; charset=UTF-8").append("\r\n\r\n");
        writer.append(extractionId).append("\r\n");

        // Write the file part
        writer.append("--").append(boundary).append("\r\n");
        writer.append("Content-Disposition: form-data; name=\"files\"; filename=\"")
                .append(file.getOriginalFilename()).append("\"").append("\r\n");
        writer.append("Content-Type: application/octet-stream").append("\r\n\r\n");
        writer.flush();

        // Write the file's raw bytes
        bos.write(file.getBytes());
        bos.flush();
        writer.append("\r\n");
        writer.flush();

        // End boundary
        writer.append("--").append(boundary).append("--").append("\r\n");
        writer.close();

        byte[] multipartBody = bos.toByteArray();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "uploadFiles"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "multipart/form-data; boundary=" + boundary)
                .POST(HttpRequest.BodyPublishers.ofByteArray(multipartBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() >= 200 && response.statusCode() < 300){
            return objectMapper.readTree(response.body());
        } else {
            System.err.println("Upload error: " + response.statusCode() + " " + response.body());
            return null;
        }
    }



    public JsonNode getBatchResults(String extractionId, String batchId) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        String jsonPayload = "{" +
                "\"extractionId\": \"" + extractionId + "\"," +
                "\"batchId\": \"" + batchId + "\"" +
                "}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "getBatchResults"))
                .header("Authorization", "Bearer " + API_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return objectMapper.readTree(response.body());
    }






}
