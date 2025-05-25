package com.stellarburgers.steps;

import com.stellarburgers.client.StellarBurgersClient;
import com.stellarburgers.model.OrderRequest;
import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Отправляем запрос на создание заказа")
    public static Response sendCreateOrderRequest(String token, OrderRequest body) {
        RequestSpecification req = given()
                .contentType(ContentType.JSON)
                .body(body);

        // Добавляем Authorization только если токен не пуст и не null
        if (token != null && !token.isBlank()) {
            req.header("Authorization", token);
        }

        return req.when()
                .post("/orders");
    }

    @Step("Отправляем запрос на получение списка заказов")
    public static Response sendGetOrdersRequest(String token) {
        return StellarBurgersClient.getOrders(token);
    }
}