package com.example.snap2transact.controllers;

import com.example.snap2transact.services.ExtractaAPIService;
import com.example.snap2transact.services.FireflyAPIService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.Random;

@RestController
public class UploadController {


    @Autowired
    ExtractaAPIService extractaAPIService;

    @Autowired
    FireflyAPIService fireflyAPIService;

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


    @PostMapping("/upload-normal")
    public ResponseEntity<JsonNode> uploadNormalTransaction(@RequestParam("file") MultipartFile img) throws IOException, InterruptedException {

         JsonNode extraction = extractaAPIService.createExtraction(normalTransactionExtractionDetailsJson);
         JsonNode response =  extractaAPIService.uploadFiles("OJIEFnCmn4cgbtgJdCg"/*extraction.get("extractionId").asText()*/,img);

        JsonNode result = extractaAPIService.getBatchResults("-OJDc9njfsJ1-6ZWU6_J"/*extraction.get("extractionId").asText()*/,/*response.get("batchId").asText()*/"J7MXFTMYx1");

        String accountData = createAccountData(result.path("files").get(0).path("result"),result.path("files").get(0).path("result").get("payer_name").textValue());
        fireflyAPIService.createAccount(accountData);

        accountData = createAccountData(result.path("files").get(0).path("result"),result.path("files").get(0).path("result").get("receiver_name").textValue());
        fireflyAPIService.createAccount(accountData);

        String data = createTransactionData(result.path("files").get(0).path("result"));
        ResponseEntity<HttpResponse<String>> res =  fireflyAPIService.storeTransaction(data);

        return ResponseEntity.ok(result.path("files").get(0).path("result"));
    }


    @PostMapping("/upload-paystub")
    public ResponseEntity<JsonNode> uploadPayStub(@RequestParam("file") MultipartFile file) throws IOException, InterruptedException {
        return uploadNormalTransaction(file);
    }



    public String createTransactionData(JsonNode jsonNode){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode transaction = mapper.createObjectNode();
        ArrayNode transactionsArray = mapper.createArrayNode();
        ObjectNode rootNode = mapper.createObjectNode();
        String modDate = jsonNode.get("date").asText();
        modDate = modDate.replace("/", "-");
        modDate = "20"+modDate;
        transaction.put("type","transfer");
        transaction.put("date",modDate);
        transaction.put("amount",jsonNode.get("amount").textValue());
        transaction.put("description","new transaction");
        transaction.put("source_name",jsonNode.get("payer_name").textValue());
        transaction.put("destination_name",jsonNode.get("receiver_name").textValue());
        transactionsArray.add(transaction);
        rootNode.set("transactions",transactionsArray);
        return rootNode.toString();
    }


    public String createAccountData(JsonNode jsonNode,String payerName){
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode Account = mapper.createObjectNode();
        Random rand = new Random();
        String modDate = jsonNode.get("date").asText();
        modDate = modDate.replace("/", "-");
        modDate = "20"+modDate;
        Account.put("name",payerName);
        Account.put("opening_balance_date",modDate);
        Account.put("type","asset");
        Account.put("account_number",rand.nextInt(1000000));
        Account.put("opening_balance", jsonNode.get("amount").textValue());
        Account.put("account_role","defaultAsset");

        return  Account.toString();

    }

    }




