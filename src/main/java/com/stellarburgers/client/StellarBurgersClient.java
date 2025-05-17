package com.stellarburgers.client;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class StellarBurgersClient {
    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.basePath = "/api";
    }

    public static io.restassured.response.Response registerUser(Object body) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/auth/register");
    }

    public static Response loginUser(Object body) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/auth/login");
    }

    public static Response updateUser(String token, Object body) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .body(body)
                .when()
                .patch("/auth/user");
    }

    public static Response deleteUser(String token) {
        return RestAssured
                .given()
                .header("Content-Type", "application/json")
                .header("Authorization", token)
                .when()
                .delete("/auth/user");
    }

    public static Response createOrder(String token, Object body) {
        var req = RestAssured
                .given()
                .header("Content-Type", "application/json")
                .body(body);
        if (token != null && !token.isEmpty()) {
            req.header("Authorization", token);
        }
        return req
                .when()
                .post("/orders");
    }

    public static Response getOrders(String token) {
        var req = RestAssured
                .given()
                .header("Content-Type", "application/json");
        if (token != null && !token.isEmpty()) {
            req.header("Authorization", token);
        }
        return req
                .when()
                .get("/orders");
    }
}