package integration.API;

import com.example.snap2transact.services.ExtractaAPIService;
import com.example.snap2transact.services.FireflyAPIService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ExtractaAIApiTest {
    private static final String API_KEY = "LTE0MjY2MDY0NTk=_w6lpmvs7chej9og1hgmpld";
    private static final String BASE_URL = "https://api.extracta.ai/api/v1/";
    private final ObjectMapper objectMapper = new ObjectMapper();
    String normalTransactionExtractionDetailsJson =
            "{\n" +
                    "    \"extractionDetails\": {\n" +
                    "        \"name\": \"Transaction - Extraction\",\n" +
                    "        \"language\": \"English\",\n" +
                    "        \"fields\": [\n" +
                    "            {\n" +
                    "                \"key\": \"amount\",\n" +
                    "                \"example\": \"25\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"key\": \"bank_name\",\n" +
                    "                \"example\": \"WASHINGTON'S OLDEST NATIONAL BANK FIRST NATIONAL BANK\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"key\": \"date\",\n" +
                    "                \"example\": \"2019-10-13\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"key\": \"payer_name\",\n" +
                    "                \"example\": \"HON. GERALD R. FORD MRS. BETTY B. FORD\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"key\": \"receiver_name\",\n" +
                    "                \"example\": \"Presiding Bishop, Episcopal Church\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"key\": \"currency\",\n" +
                    "                \"example\": \"USD\",\n" +
                    "                \"description\": \"currency as string, not symbol\"\n" +
                    "            },\n" +
                    "            {\n" +
                    "                \"key\": \"account_number\",\n" +
                    "                \"example\": \"6111611\",\n" +
                    "                \"description\": \"Part of the MICR line, typically at the end.\"\n" +
                    "            }\n" +
                    "        ]\n" +
                    "    }\n" +
                    "}";

    @InjectMocks
    private ExtractaAPIService extractaAPIService;


    @Test
    void testCreateExtractionTest() throws IOException, InterruptedException {
        // Arrange: create a dummy JSON body for testing

        String jsonBodyResult = "{\n" +
                "  \"status\" : \"created\",\n" +
                "  \"createdAt\" : 1740864032435,\n" +
                "  \"extractionId\" : \"-OKI_FueqSWiExyfS310\"\n" +
                "}";



        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(jsonBodyResult);
        when(mockResponse.statusCode()).thenReturn(200);

        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);


        try (MockedStatic<HttpClient> httpClientStatic = Mockito.mockStatic(HttpClient.class)) {
            httpClientStatic.when(HttpClient::newHttpClient).thenReturn(mockClient);


            JsonNode node = extractaAPIService.createExtraction(normalTransactionExtractionDetailsJson);

            assertNotNull(node, "Response should not be null");
            assertEquals("created", node.get("status").asText());
            assertEquals("1740864032435", node.get("createdAt").asText());
            assertEquals("-OKI_FueqSWiExyfS310", node.get("extractionId").asText());

        }
    }


    @Test
    void testGetBatchResults() throws IOException, InterruptedException {
        // Arrange: create a dummy JSON body for testing

        String jsonBody = "{\n" +
                "  \"extractionId\" : \"-OJDc9njfsJ1-6ZWU6_J\",\n" +
                "  \"batchId\" : \"J7MXFTMYx1\",\n" +
                "  \"files\" : [ {\n" +
                "    \"fileId\" : \"-OJDcAN8dAZIcv3TPLO0\",\n" +
                "    \"fileName\" : \"PHOTO-2024-12-22-20-11-06.jpg\",\n" +
                "    \"status\" : \"processed\",\n" +
                "    \"result\" : {\n" +
                "      \"account_number\" : \"394997\",\n" +
                "      \"amount\" : \"1000.00\",\n" +
                "      \"bank_name\" : \"הפועלים בע״מ\",\n" +
                "      \"date\" : \"23/12/24\",\n" +
                "      \"payer_name\" : \"מחמוד מסארוה\",\n" +
                "      \"receiver_name\" : \"שירין אבו אלפיה\"\n" +
                "    },\n" +
                "    \"url\" : \"https://storage.googleapis.com/ocerizare-ro.appspot.com/extractions/wgQIUTxNiENDmMHW3sJSckbbsfn1/J7MXFTMYx1/J7gJJ7YdWW_%25_PHOTO-2024-12-22-20-11-06.jpg?GoogleAccessId=firebase-adminsdk-j1pqz%40ocerizare-ro.iam.gserviceaccount.com&Expires=16730323200&Signature=lIPlPkO25B5v9w1meBfPD9Mf4Nhj%2FiOmcVt5btOhH8%2FPjLuSNq3iv2bH%2FIbcojg6KqP%2FMzbOf7gemNTDuxsHALXHzv2QEbDcNiyFfDw3vTLXfK4jwnId8PgvqjiZuGmKPzKC9GsDGZziXC02IPFZ6lGsnaRcYHcF%2BCnYiIh0lOL0qSkVZinpc1O%2FZ0A4264mhaQchOaMsHmVaumLqwooRALT3tk4I05uNoEQOW8fK2nGnbz9VMfLU8oyx1wQsmL1SVMqScIj%2FnuliKC8sqkjLYPOBxT%2BQ7jRJjIGG8t1j1Mf9d0lAAcweBn7Go69OitF51oY2RmsiyLgIAa8P1GZ2A%3D%3D\"\n" +
                "  } ]\n" +
                "}";

        // HttpClient mockClient = mock(HttpClient.class);


        HttpResponse mockResponse = mock(HttpResponse.class);
        when(mockResponse.body()).thenReturn(jsonBody);

        HttpClient mockClient = mock(HttpClient.class);
        when(mockClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(mockResponse);


        try (MockedStatic<HttpClient> httpClientStatic = Mockito.mockStatic(HttpClient.class)) {
            httpClientStatic.when(HttpClient::newHttpClient).thenReturn(mockClient);


            JsonNode node = extractaAPIService.getBatchResults("dummyExtractionId","dummyBatchId");

            assertNotNull(node, "Response should not be null");
            assertEquals("שירין אבו אלפיה",node.path("files").get(0).path("result").get("receiver_name").textValue());
            assertEquals("1000.00",node.path("files").get(0).path("result").get("amount").textValue());
        }




    }


}

