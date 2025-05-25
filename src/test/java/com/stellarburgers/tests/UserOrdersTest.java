package com.stellarburgers.tests;

import com.stellarburgers.model.OrderRequest;
import com.stellarburgers.model.User;
import com.stellarburgers.steps.OrderSteps;
import com.stellarburgers.steps.UserSteps;
import com.github.javafaker.Faker;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.*;

public class UserOrdersTest {
    private User user;
    private String token;
    private final Faker faker = new Faker();

    @Before
    public void setUp() {
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        Response r = UserSteps.sendRegisterUserRequest(user);
        token = r.jsonPath().getString("accessToken");
        OrderSteps.sendCreateOrderRequest(token, generateValidOrderRequest());
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.sendDeleteUserRequest(token).then().statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    @DisplayName("Получение заказов пользователя с авторизацией")
    @Description("Проверка успешного получения заказов пользователем при наличии авторизации")
    public void getOrdersAuth() {
        OrderSteps.sendGetOrdersRequest(token)
                .then().statusCode(200)
                .body("orders", notNullValue());
    }

    @Test
    @DisplayName("Получение заказов пользователя без авторизации")
    @Description("Проверка, что неавторизованный пользователь не может получить заказы и получает ошибку 401")
    public void getOrdersNoAuth() {
        OrderSteps.sendGetOrdersRequest("")
                .then().statusCode(401)
                .body("message", is("You should be authorised"));
    }

    private OrderRequest generateValidOrderRequest() {
        List<String> ingredients = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            ingredients.add(UUID.randomUUID().toString());
        }
        return new OrderRequest(ingredients);
    }
}