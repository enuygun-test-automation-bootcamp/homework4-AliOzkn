import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import models.PetStore;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static org.testng.Assert.*;

public class StoreTestByUniRest extends PetStore implements PetStoreMethods {
    PetStore petStore;

    //Created petStore object in constructor.
    public StoreTestByUniRest() {
        super();
        petStore = new PetStore();
    }
    @Override
    @Test(priority = 1)
    public void verifyPlaceOrderForPet() throws JsonProcessingException, UnirestException {

        //Parameters added from PetStoreMethods Interface.
        petStore.setId(PetStoreMethods.id);
        petStore.setPetId(PetStoreMethods.petId);
        petStore.setQuantity(PetStoreMethods.quantity);
        petStore.setShipDate("2022-07-31T00:00:00.000+0000");
        petStore.setStatus("placed");
        petStore.setComplete(true);

        ObjectMapper mapper = new ObjectMapper();
        //Converting the Object to JSONString
        String jsonString = mapper.writeValueAsString(petStore);

        Unirest.setTimeouts(0, 0);

        HttpResponse<JsonNode> response = Unirest.post(baseURI + "/order")
                .header("Content-Type", "application/json")
                .body(jsonString)
                .asJson();

        //These are verifications about status code and values of body.
        assertEquals(response.getStatus(), 200);
        assertEquals(response.getBody().getObject().get("id"), PetStoreMethods.id);
        assertEquals(response.getBody().getObject().get("petId"), PetStoreMethods.petId);
        assertEquals(response.getBody().getObject().get("quantity"), PetStoreMethods.quantity);
    }

    //The following 1st, 2nd and 3rd tests have GET, DELETE and GET methods respectively. That's why i didn't use petStore object for body in here.
    @Override
    @Test(priority = 2)
    public void verifyFindPurchaseByID() throws UnirestException {

        Unirest.setTimeouts(0, 0);

        HttpResponse<String> response = Unirest.get(baseURI + "/order/" + PetStoreMethods.id)
                .asString();

        assertEquals(response.getStatusText(), "OK");
    }
    @Override
    @Test(dependsOnMethods = "verifyPlaceOrderForPet", priority = 3)
    public void verifyDeletePurchaseByID() throws UnirestException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.delete(baseURI + "/order/" + PetStoreMethods.id)
                .asString();

        assertEquals(response.getStatus(), 200);
    }
    @Override
    @Test(priority = 4)
    public void verifyReturn() throws UnirestException {

        Unirest.setTimeouts(0, 0);
        HttpResponse<String> response = Unirest.get(baseURI + "/inventory")
                .asString();

        assertEquals(response.getStatus(), 200);
    }
}