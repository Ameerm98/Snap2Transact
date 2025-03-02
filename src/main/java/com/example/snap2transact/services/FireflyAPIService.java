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

    private static final String API_KEY ="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiN2QwYmM3OTE4OGI0OTU5ODE1MWZmYjdmOGYzMWIxYmYxOTVkZTcwZjA3MDczODliOWE5NjNmY2MxYjQ3MjIxZGU2MTFjNWE3NmNlMzZmNzIiLCJpYXQiOjE3NDA2NDA2OTIuODI4MDYyLCJuYmYiOjE3NDA2NDA2OTIuODI4MDY1LCJleHAiOjE3NzIxNzY2OTIuNjMyMzM1LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.j9-AljTOuI3Xrz2MEVw7ZSmXdhFljN0Y2LAkko-c7BZ0AxEUZecRxNpRQnFQvEf_6vSzegAN4Tcdd13c4thAyQt7n9DNylIwREPM8xj5qGPVvhnwiuh3EvLxz2B4mcZpzVlMCD_Ir5ritH7KtXnfQqsblaxKfrjxSIOHccfv0sc1cHMlq2Bscq5AG7mOMsgaLVEtIBWO7r1yqdJhuCrn52UyhntVpI0sKV-BVHa_zLBW8H9Q3ChYu0G6SdKQLf4VfQKLrjamNV0Cbsxda4PoCC1TDvz-qpBLLQbJuIM6yYLLXAKSgPPWPpwPh3VmDBCkc_NbRwOTS8SqliBHHssFrfgHx2YqPdnlVUEETpybu6xs_4pw2B0KY5flVng0ujxJo7yxfsAOAlLHX-q7WA-doR_pC_3iU2sBtkfkjXs_g3mddxVZivI3PyMlDJjNkHwNQ5McU5GdLsON13kAwMB0QIdeYGiWOdcFnOvsvRifcZKRemUDjj1b3KMubesA_9cqI-WlAVd47csJwmpOkFigrwW2FaCAK4nffYEaU61tItYouPkWLDRgVUZIhpP6gITpw4Z9FfQm1NxZQ_rZOtWzHRqDyCWv44O1vIBoLVW66TUlZH0-pDmXPKoEHsVrpr1bE4aRwDSQ1E3G8eu0pLBXmgEVDpokAgPVyNZXgjpluvA";
    private static final String BASE_URL = "https://7c5a-2a06-c701-497e-fb00-f902-1d42-f6fb-f91a.ngrok-free.app/api/v1/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    HttpRequest.Builder requestBody = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .header("Accept", "application/json")
            .header("Content-Type","application/json")
            .header("Authorization"," Bearer "+API_KEY);



    public ResponseEntity<HttpResponse<String>> createAccount(String jsonBody) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = requestBody.uri(URI.create(BASE_URL+"accounts")).method("POST", HttpRequest.BodyPublishers.ofString(jsonBody)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    public ResponseEntity<HttpResponse<String>> storeTransaction(String jsonBody) throws IOException, InterruptedException {
        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = requestBody.uri(URI.create(BASE_URL+"transactions")).method("POST", HttpRequest.BodyPublishers.ofString(jsonBody)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }catch (Exception e){
            e.printStackTrace();
        }


        return null;
    }













}
