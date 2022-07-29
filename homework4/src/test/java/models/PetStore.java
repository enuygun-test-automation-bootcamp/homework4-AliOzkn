package models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import static io.restassured.RestAssured.baseURI;

public @Data class PetStore {
    public @Getter @Setter int id;
    public @Getter @Setter int petId;
    public @Getter @Setter int quantity;
    public @Getter @Setter String shipDate;
    public @Getter @Setter String status;
    public @Getter @Setter boolean complete;
    public @Getter @Setter int code;

    public PetStore(){
        baseURI = "https://petstore.swagger.io/v2/store";
    }
    }
