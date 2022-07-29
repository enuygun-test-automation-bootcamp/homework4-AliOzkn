import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONObject;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;

import static org.testng.Assert.assertNotNull;

public class StoreTestByHttpEntity implements PetStoreMethods {

    HttpHeaders headers;
    RestTemplate restTemplate;

    public StoreTestByHttpEntity() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        baseURI = "https://petstore.swagger.io/v2/store";
    }


    @Override
    @Test(priority = 1)
    public void verifyPlaceOrderForPet() throws JsonProcessingException {

        restTemplate = new RestTemplate();
        JSONObject requestBody = new JSONObject();
        requestBody.put("id", id);
        requestBody.put("petId", petId);
        requestBody.put("quantity", quantity);
        requestBody.put("shipDate", "2022-07-31T20:00:00.760Z");
        requestBody.put("status", "placed");
        requestBody.put("complete", true);
        final ObjectMapper objectMapper = new ObjectMapper();

        HttpEntity<String> request =
                new HttpEntity<String>(requestBody.toString(), headers);

        String personResultAsJsonStr =
                restTemplate.postForObject(baseURI + "/order", request, String.class);

        JsonNode root = objectMapper.readTree(personResultAsJsonStr);

        assertNotNull(personResultAsJsonStr);
        assertNotNull(root);
        assertNotNull(root.path("id").asText());
        assertNotNull(root.path("petId").asText());
        assertNotNull(root.path("quantity").asText());

    }

    @Override
    @Test(priority = 2)
    public void verifyFindPurchaseByID() {

    }

    @Override
    @Test(dependsOnMethods = "verifyPlaceOrderForPet", priority = 3)
    public void verifyDeletePurchaseByID() {

    }

    @Override
    @Test(priority = 4)
    public void verifyReturn() {

    }
}

