package com.stellarburgers.steps;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class IngredientSteps {
    /** Делает GET /ingredients и возвращает Response */
    public static Response getIngredientsResponse() {
        return RestAssured.given()
                .when().get("/ingredients");
    }
}