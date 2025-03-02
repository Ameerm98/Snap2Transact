package integration.API;

import com.example.snap2transact.services.FireflyAPIService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.message.BasicHttpResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class FireflyApiTest {
    private static final String API_KEY ="eyJ0eXAiOiJKV1QiLCJhbGciOiJSUzI1NiJ9.eyJhdWQiOiIxIiwianRpIjoiN2QwYmM3OTE4OGI0OTU5ODE1MWZmYjdmOGYzMWIxYmYxOTVkZTcwZjA3MDczODliOWE5NjNmY2MxYjQ3MjIxZGU2MTFjNWE3NmNlMzZmNzIiLCJpYXQiOjE3NDA2NDA2OTIuODI4MDYyLCJuYmYiOjE3NDA2NDA2OTIuODI4MDY1LCJleHAiOjE3NzIxNzY2OTIuNjMyMzM1LCJzdWIiOiIxIiwic2NvcGVzIjpbXX0.j9-AljTOuI3Xrz2MEVw7ZSmXdhFljN0Y2LAkko-c7BZ0AxEUZecRxNpRQnFQvEf_6vSzegAN4Tcdd13c4thAyQt7n9DNylIwREPM8xj5qGPVvhnwiuh3EvLxz2B4mcZpzVlMCD_Ir5ritH7KtXnfQqsblaxKfrjxSIOHccfv0sc1cHMlq2Bscq5AG7mOMsgaLVEtIBWO7r1yqdJhuCrn52UyhntVpI0sKV-BVHa_zLBW8H9Q3ChYu0G6SdKQLf4VfQKLrjamNV0Cbsxda4PoCC1TDvz-qpBLLQbJuIM6yYLLXAKSgPPWPpwPh3VmDBCkc_NbRwOTS8SqliBHHssFrfgHx2YqPdnlVUEETpybu6xs_4pw2B0KY5flVng0ujxJo7yxfsAOAlLHX-q7WA-doR_pC_3iU2sBtkfkjXs_g3mddxVZivI3PyMlDJjNkHwNQ5McU5GdLsON13kAwMB0QIdeYGiWOdcFnOvsvRifcZKRemUDjj1b3KMubesA_9cqI-WlAVd47csJwmpOkFigrwW2FaCAK4nffYEaU61tItYouPkWLDRgVUZIhpP6gITpw4Z9FfQm1NxZQ_rZOtWzHRqDyCWv44O1vIBoLVW66TUlZH0-pDmXPKoEHsVrpr1bE4aRwDSQ1E3G8eu0pLBXmgEVDpokAgPVyNZXgjpluvA";
    private static final String BASE_URL = "https://7c5a-2a06-c701-497e-fb00-f902-1d42-f6fb-f91a.ngrok-free.app/api/v1/";
    private final ObjectMapper objectMapper = new ObjectMapper();

    String accountData="{\n" +
            "  \"name\" : \"מחמוד מסארוה\",\n" +
            "  \"opening_balance_date\" : \"2023-12-24\",\n" +
            "  \"type\" : \"asset\",\n" +
            "  \"account_number\" : 195981,\n" +
            "  \"opening_balance\" : \"1000.00\",\n" +
            "  \"account_role\" : \"defaultAsset\"\n" +
            "}";

    String transactionData = "{\n" +
            "  \"transactions\" : [ {\n" +
            "    \"type\" : \"transfer\",\n" +
            "    \"date\" : \"2023-12-24\",\n" +
            "    \"amount\" : \"1000.00\",\n" +
            "    \"description\" : \"new transaction\",\n" +
            "    \"source_name\" : \"מחמוד מסארוה\",\n" +
            "    \"destination_name\" : \"שירין אבו אלפיה\"\n" +
            "  } ]\n" +
            "}";
    @Mock
    private HttpClient mockHttpClient;

    @InjectMocks
    private FireflyAPIService fireflyAPIService;

    HttpRequest.Builder requestBody = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .header("Accept", "application/json")
            .header("Content-Type","application/json")
            .header("Authorization"," Bearer "+API_KEY);




    @Test
    public void testCreateAccount_Success() throws IOException, InterruptedException {
        // Arrange: create a dummy JSON body for testing

        String jsonBody = "{\n" +
                "  \"data\" : {\n" +
                "    \"type\" : \"accounts\",\n" +
                "    \"id\" : \"1057\",\n" +
                "    \"attributes\" : {\n" +
                "      \"created_at\" : \"2025-03-01T22:51:22+01:00\",\n" +
                "      \"updated_at\" : \"2025-03-01T22:51:22+01:00\",\n" +
                "      \"active\" : true,\n" +
                "      \"order\" : 3,\n" +
                "      \"name\" : \"מחמוד מסארוה\",\n" +
                "      \"type\" : \"asset\",\n" +
                "      \"account_role\" : \"defaultAsset\",\n" +
                "      \"currency_id\" : \"23\",\n" +
                "      \"currency_code\" : \"ILS\",\n" +
                "      \"currency_symbol\" : \"₪\",\n" +
                "      \"currency_decimal_places\" : 2,\n" +
                "      \"native_currency_id\" : null,\n" +
                "      \"native_currency_code\" : null,\n" +
                "      \"native_currency_symbol\" : null,\n" +
                "      \"native_currency_decimal_places\" : null,\n" +
                "      \"current_balance\" : \"1000.00\",\n" +
                "      \"native_current_balance\" : null,\n" +
                "      \"current_balance_date\" : \"2025-03-01T23:59:59+01:00\",\n" +
                "      \"notes\" : null,\n" +
                "      \"monthly_payment_date\" : null,\n" +
                "      \"credit_card_type\" : null,\n" +
                "      \"account_number\" : \"817223\",\n" +
                "      \"iban\" : null,\n" +
                "      \"bic\" : null,\n" +
                "      \"virtual_balance\" : \"0\",\n" +
                "      \"native_virtual_balance\" : null,\n" +
                "      \"opening_balance\" : \"1000.00\",\n" +
                "      \"native_opening_balance\" : \"0\",\n" +
                "      \"opening_balance_date\" : \"2023-12-24T00:00:00+01:00\",\n" +
                "      \"liability_type\" : null,\n" +
                "      \"liability_direction\" : null,\n" +
                "      \"interest\" : null,\n" +
                "      \"interest_period\" : null,\n" +
                "      \"current_debt\" : null,\n" +
                "      \"include_net_worth\" : true,\n" +
                "      \"longitude\" : null,\n" +
                "      \"latitude\" : null,\n" +
                "      \"zoom_level\" : null\n" +
                "    },\n" +
                "    \"links\" : {\n" +
                "      \"self\" : \"https://7c5a-2a06-c701-497e-fb00-f902-1d42-f6fb-f91a.ngrok-free.app/api/v1/accounts/1057\",\n" +
                "      \"0\" : {\n" +
                "        \"rel\" : \"self\",\n" +
                "        \"uri\" : \"/accounts/1057\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        // HttpClient mockClient = mock(HttpClient.class);


        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonBody);


        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);


        try (MockedStatic<HttpClient> httpClientStatic = Mockito.mockStatic(HttpClient.class)) {
            httpClientStatic.when(HttpClient::newHttpClient).thenReturn(mockClient);


            ResponseEntity<HttpResponse<String>> response = fireflyAPIService.createAccount(accountData);


            // Assert: verify that the response is as expected
            assertNotNull(response, "Response should not be null");
            assertEquals(200, response.getBody().statusCode(), "Response status should be Ok");
            JsonNode node = objectMapper.readTree(response.getBody().body());
            assertEquals("1000.00",node.path("data").path("attributes").get("current_balance").asText());
        }

    }

    @Test
    public void testStoreTransaction_Success() throws IOException, InterruptedException {
        // Arrange: create a dummy JSON body for testing

        String jsonBody = "{\n" +
                "  \"data\" : {\n" +
                "    \"type\" : \"transactions\",\n" +
                "    \"id\" : \"767\",\n" +
                "    \"attributes\" : {\n" +
                "      \"created_at\" : \"2025-03-01T22:54:57+01:00\",\n" +
                "      \"updated_at\" : \"2025-03-01T22:54:57+01:00\",\n" +
                "      \"user\" : \"1\",\n" +
                "      \"group_title\" : null,\n" +
                "      \"transactions\" : [ {\n" +
                "        \"user\" : \"1\",\n" +
                "        \"transaction_journal_id\" : \"767\",\n" +
                "        \"type\" : \"transfer\",\n" +
                "        \"date\" : \"2023-12-24T00:00:00+01:00\",\n" +
                "        \"order\" : 0,\n" +
                "        \"currency_id\" : \"23\",\n" +
                "        \"currency_code\" : \"ILS\",\n" +
                "        \"currency_name\" : \"Israeli new shekel\",\n" +
                "        \"currency_symbol\" : \"₪\",\n" +
                "        \"currency_decimal_places\" : 2,\n" +
                "        \"foreign_currency_id\" : null,\n" +
                "        \"foreign_currency_code\" : null,\n" +
                "        \"foreign_currency_symbol\" : null,\n" +
                "        \"foreign_currency_decimal_places\" : 0,\n" +
                "        \"amount\" : \"1000.000000000000\",\n" +
                "        \"foreign_amount\" : null,\n" +
                "        \"description\" : \"new transaction\",\n" +
                "        \"source_id\" : \"1057\",\n" +
                "        \"source_name\" : \"מחמוד מסארוה\",\n" +
                "        \"source_iban\" : null,\n" +
                "        \"source_type\" : \"Asset account\",\n" +
                "        \"destination_id\" : \"1059\",\n" +
                "        \"destination_name\" : \"שירין אבו אלפיה\",\n" +
                "        \"destination_iban\" : null,\n" +
                "        \"destination_type\" : \"Asset account\",\n" +
                "        \"budget_id\" : null,\n" +
                "        \"budget_name\" : null,\n" +
                "        \"category_id\" : null,\n" +
                "        \"category_name\" : null,\n" +
                "        \"bill_id\" : null,\n" +
                "        \"bill_name\" : null,\n" +
                "        \"reconciled\" : false,\n" +
                "        \"notes\" : null,\n" +
                "        \"tags\" : [ ],\n" +
                "        \"internal_reference\" : null,\n" +
                "        \"external_id\" : null,\n" +
                "        \"original_source\" : \"ff3-v6.2.5\",\n" +
                "        \"recurrence_id\" : null,\n" +
                "        \"recurrence_total\" : null,\n" +
                "        \"recurrence_count\" : null,\n" +
                "        \"bunq_payment_id\" : null,\n" +
                "        \"external_url\" : null,\n" +
                "        \"import_hash_v2\" : \"436fdfb4299ca1a6900b21aa5d8754ed9430dc673a0ad7cd082c91518e157581\",\n" +
                "        \"sepa_cc\" : null,\n" +
                "        \"sepa_ct_op\" : null,\n" +
                "        \"sepa_ct_id\" : null,\n" +
                "        \"sepa_db\" : null,\n" +
                "        \"sepa_country\" : null,\n" +
                "        \"sepa_ep\" : null,\n" +
                "        \"sepa_ci\" : null,\n" +
                "        \"sepa_batch_id\" : null,\n" +
                "        \"interest_date\" : null,\n" +
                "        \"book_date\" : null,\n" +
                "        \"process_date\" : null,\n" +
                "        \"due_date\" : null,\n" +
                "        \"payment_date\" : null,\n" +
                "        \"invoice_date\" : null,\n" +
                "        \"longitude\" : null,\n" +
                "        \"latitude\" : null,\n" +
                "        \"zoom_level\" : null,\n" +
                "        \"has_attachments\" : false\n" +
                "      } ]\n" +
                "    },\n" +
                "    \"links\" : {\n" +
                "      \"self\" : \"https://7c5a-2a06-c701-497e-fb00-f902-1d42-f6fb-f91a.ngrok-free.app/api/v1/transactions/767\",\n" +
                "      \"0\" : {\n" +
                "        \"rel\" : \"self\",\n" +
                "        \"uri\" : \"/transactions/767\"\n" +
                "      }\n" +
                "    }\n" +
                "  }\n" +
                "}";

        // HttpClient mockClient = mock(HttpClient.class);


        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(jsonBody);

        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);


        try (MockedStatic<HttpClient> httpClientStatic = Mockito.mockStatic(HttpClient.class)) {
            httpClientStatic.when(HttpClient::newHttpClient).thenReturn(mockClient);


            ResponseEntity<HttpResponse<String>> response = fireflyAPIService.storeTransaction(transactionData);


            // Assert: verify that the response is as expected
            assertNotNull(response, "Response should not be null");
            assertEquals(200,response.getBody().statusCode(), "Response code should be 200");
            JsonNode node = objectMapper.readTree(response.getBody().body());
            assertEquals("transfer", node.path("data").path("attributes").path("transactions").get(0).get("type").asText(), "Status code should match expected");
            assertEquals(HttpStatus.CREATED, response.getStatusCode(), "Response status should be CREATED");
        }

    }




}
