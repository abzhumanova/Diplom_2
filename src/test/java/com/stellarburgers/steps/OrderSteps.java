package com.stellarburgers.steps;

import com.stellarburgers.client.StellarBurgersClient;
import com.stellarburgers.model.OrderRequest;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderSteps {
    @Step("Создаём заказ")
    public static Response create(String token, OrderRequest req) {
        return StellarBurgersClient.createOrder(token, req);
    }

    @Step("Получаем список заказов")
    public static Response list(String token) {
        return StellarBurgersClient.getOrders(token);
    }
}