package com.example.snap2transact.services;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class FireflyAPIService {

    private static final String API_KEY = "eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiNWY3ZDVmMjAxMTZlOTE0NGQxYjY5NzUwNGVkYzAwMTI4YjM1ODA1ZWE2MzliY2JjYWUyZTY4MjY1NjBlMjM3ZTZmY2VlZjBlNDVjMjRiZTQiLCJpYXQiOjE3NDA0MjAwNTkuOTgyNDk2LCJuYmYiOjE3NDA0MjAwNTkuOTgyNDk4LCJleHAiOjE3NzE5NTYwNTkuOTEwMTk2LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.OmUxzwaFTK_Cn01gYZTMqprXPQDMM32ebQBgdYx4U5jRWig6_oO_d3Qhn-VAY8tNpa5gMlgwNW4CpCAmYykVOXu-Z6aDagsou_k42sYHImE4aBIB3pLb1GOXZDapCeoaguADb4waWdPmuIn90y7xZqJNZjhmrTLrk89M3jT0FiKCGpGs6IDUbkSmY7y5NRfpE6ZTaRnl2dCIH8p6XAfNSeZjLP2RF6-e6UdUple-wcPlseDrMpDVRXWnV4q5zbc2BXZqWAE20QF_drAV0ZAzuQSs1Y0eLnaQLxrzPpQQHOW26lMmIG68FxqPvXpvVth0rmyMfjGV2hP6dbFuDT8k-ORqxT2K1rjjrkFj2O5jm4pyArxvbsW4GSIDK74DkxwhkYREKRAOaFQwbfEMSj33N7pmwTRSrvU0S0vr14hsx618hOmVNrhsqIm5jmZ693AQLb_JvuMOwppA_6waQd6QAVEEIFQxzi9aN2k_BlBK_rclCgXTU1LTZ5TeCNtQ5TPsQUHxAbUNlayOWuTTXDh9wdjaauy_mIgGrhg1geKlFA0PF_mpThcjM4AUmKhGtYXwP9HUCEC1bIh00Hoj_RMBp5N5neqMnHiRboolUNScJI-FJeU6SEnirnBWzkBvADeLaMDPLM92sA_plJ1uXba9k4qgy5iyotjGNmjyxepUaHI";
    private static final String BASE_URL = "http://localhost:80/api/v1/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    HttpRequest.Builder requestBody = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .header("Accept", "application/json")
            .header("Content-Type","application/json")
            .header("Authorization"," Bearer "+API_KEY);



    public ResponseEntity<Integer> createAccount(String jsonBody) throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = requestBody.uri(URI.create(BASE_URL+"accounts")).method("POST", HttpRequest.BodyPublishers.ofString(jsonBody)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return new ResponseEntity<>(response.statusCode(), HttpStatus.CREATED);
    }

    public ResponseEntity<Integer> storeTransaction(String jsonBody) throws IOException, InterruptedException {
        try {
            System.out.println(jsonBody);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = requestBody.uri(URI.create(BASE_URL+"transactions")).method("POST", HttpRequest.BodyPublishers.ofString(jsonBody)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ResponseEntity<>(response.statusCode(), HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }













}
