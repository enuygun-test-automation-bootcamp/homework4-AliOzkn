import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface PetStoreMethods {
    Faker faker = new Faker();
    int id = faker.number().numberBetween(1, 10); // The value "id" is defined from 1 to 10.
    int petId = faker.number().numberBetween(1, 10);  //  The value "petId" is defined from 1 to 10. That will be used in only verifyPlaceOrderForPet method.
    int quantity = faker.number().numberBetween(1, 10); //  The value "quantity" is defined from 1 to 10. That will be used in only verifyPlaceOrderForPet method.
    int code = 200;


    void verifyPlaceOrderForPet() throws JsonProcessingException, UnirestException;

    void verifyFindPurchaseByID() throws UnirestException;

    void verifyDeletePurchaseByID() throws UnirestException;

    void verifyReturn() throws UnirestException;

}
