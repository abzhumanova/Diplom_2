package com.stellarburgers.steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import java.util.List;

public class IngredientSteps {
    /** Делает GET /ingredients, возвращает список всех _id */
    public static List<String> getAllIngredientIds() {
        Response r = RestAssured
                .given()
                .when().get("/ingredients")
                .then().statusCode(200)
                .extract().response();
        return r.jsonPath().getList("data._id", String.class);
    }
}