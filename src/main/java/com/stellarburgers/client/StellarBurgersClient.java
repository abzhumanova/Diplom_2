package com.stellarburgers.client;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;

public class StellarBurgersClient {

    // Константы для базового URI и пути API
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    public static final String API_PATH = "/api";

    static {
        RestAssured.baseURI = BASE_URI;
        RestAssured.basePath = API_PATH;
    }

    public static Response registerUser(Object body) {
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
        // Создаём запрос с логированием запроса и ответа
        var req = RestAssured
                .given()
                .filter(new RequestLoggingFilter())   // <-- логирует тело и заголовки запроса
                .filter(new ResponseLoggingFilter())  // <-- логирует тело и заголовки ответа
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