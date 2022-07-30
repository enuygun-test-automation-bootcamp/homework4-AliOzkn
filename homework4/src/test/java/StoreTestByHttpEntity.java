import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.PetStore;
import org.json.JSONObject;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static org.testng.Assert.*;

public class StoreTestByHttpEntity extends PetStore implements PetStoreMethods {
    HttpHeaders headers;
    RestTemplate restTemplate;
    ObjectMapper objectMapper;
    public StoreTestByHttpEntity() {
        super();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseURI = "https://petstore.swagger.io/v2/store";
    }
    @Override
    @Test(priority = 1)
    public void verifyPlaceOrderForPet() throws JsonProcessingException {

        restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", PetStoreMethods.id);
        requestBody.put("petId", PetStoreMethods.petId);
        requestBody.put("quantity", PetStoreMethods.quantity);
        requestBody.put("shipDate", "2022-07-31T20:00:00.760Z");
        requestBody.put("status", "placed");
        requestBody.put("complete", true);

        HttpEntity<String> request =
                new HttpEntity<String>(requestBody.toString(), headers);

        String personResultAsJsonStr =
                restTemplate.postForObject(baseURI + "/order", request, String.class);

        objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(personResultAsJsonStr);

        assertNotNull(personResultAsJsonStr);
        assertNotNull(root);
        assertNotNull(root.path("id").asText());
        assertNotNull(root.path("petId").asText());
        assertNotNull(root.path("quantity").asText());
    }
    @Override
    @Test(priority = 2)
    public void verifyFindPurchaseByID() throws JsonProcessingException {

        restTemplate = new RestTemplate();
        ResponseEntity<String> response =
                restTemplate.getForEntity((baseURI + "/order/" + PetStoreMethods.id), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode id = root.path("id");
        assertNotNull(id.asText());
    }
    @Override
    @Ignore
    @Test(dependsOnMethods = "verifyPlaceOrderForPet", priority = 3)
    public void verifyDeletePurchaseByID() throws JsonProcessingException {}

    @Override
    @Test(priority = 4)
    public void verifyReturn() throws JsonProcessingException {

        restTemplate = new RestTemplate();
        String fooResourceUrl = baseURI;
        ResponseEntity<String> response =
                restTemplate.getForEntity((fooResourceUrl + "/inventory"), String.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);

        objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(response.getBody());
        JsonNode sold = root.path("sold");
        assertNotNull(sold.asText());
    }
}

