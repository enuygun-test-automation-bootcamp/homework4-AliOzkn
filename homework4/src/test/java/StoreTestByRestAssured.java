import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import models.PetStore;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.baseURI;

public class StoreTestByRestAssured extends PetStore implements PetStoreMethods {

    PetStore petStore;
    Response response;
    JsonPath actualBody;

    public StoreTestByRestAssured() {
        super();
        petStore = new PetStore();
    }

    @Override
    @Test(priority = 1)
    public void verifyPlaceOrderForPet() throws JsonProcessingException {

        petStore.setId(PetStoreMethods.id);
        petStore.setPetId(PetStoreMethods.petId);
        petStore.setQuantity(PetStoreMethods.quantity);
        petStore.setShipDate("2022-07-31T00:00:00.000+0000");
        petStore.setStatus("placed");
        petStore.setComplete(true);

        ObjectMapper mapper = new ObjectMapper();
        String jsonString = mapper.writeValueAsString(petStore);

        response = given().
                header("Content-Type", "application/json").
                when().
                body(jsonString).
                post(baseURI + "/order").
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response();

        //Verifications
        actualBody = response.jsonPath();

        assertEquals(actualBody.getInt("id"), PetStoreMethods.id);
        assertEquals(actualBody.getInt("petId"), PetStoreMethods.petId);
        assertEquals(actualBody.getInt("quantity"), PetStoreMethods.quantity);
        assertEquals(actualBody.get("status"), petStore.getStatus());
        assertEquals(actualBody.get("shipDate"), petStore.getShipDate());
        assertTrue(actualBody.getBoolean("complete"), "False!");
    }

    @Override
    @Test(priority = 2)
    public void verifyFindPurchaseByID() {

        petStore.setId(PetStoreMethods.id);

        response = given().
                when().
                get(baseURI + "/order/" + PetStoreMethods.id).
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response();

        actualBody = response.jsonPath();

        assertEquals(actualBody.getInt("id"), PetStoreMethods.id);
    }

    @Override
    @Test(dependsOnMethods = "verifyPlaceOrderForPet", priority = 3)
    public void verifyDeletePurchaseByID() {

        petStore.setCode(PetStoreMethods.code);

        response = given().
                when().
                delete(baseURI + "/order/" + PetStoreMethods.id).
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response();

        actualBody = response.jsonPath();

        assertEquals(actualBody.getInt("code"), petStore.getCode());
    }

    @Override
    @Test(priority = 4)
    public void verifyReturn() {

        response = given().
                when().
                get(baseURI + "/inventory").
                then().
                statusCode(200).
                contentType(ContentType.JSON).
                extract().response();
    }
}
