package com.stellarburgers.tests;

import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;

import com.stellarburgers.model.OrderRequest;
import com.stellarburgers.model.User;
import com.stellarburgers.steps.IngredientSteps;
import com.stellarburgers.steps.OrderSteps;
import com.stellarburgers.steps.UserSteps;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import javax.xml.bind.DatatypeConverter;
import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;

public class OrderCreationTest {

    private String token;
    private Faker faker = new Faker();

    @Before
    public void setUp() {
        RestAssured.filters(
                new RequestLoggingFilter(),    // логирует HTTP-заголовки и тело запроса
                new ResponseLoggingFilter()    // логирует HTTP-заголовки и тело ответа
        );
        // 1) создаём нового пользователя
        User user = new User(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().firstName()
        );
        Response r = UserSteps.create(user);
        token = r.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.delete(token)
                    .then()
                    .statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    @DisplayName("1. C авторизацией и валидными ингредиентами → 200 + success=true")
    @Description("Проверяем, что при наличии токена и корректного списка ингредиентов создаётся заказ — возвращается код 200 и success = true")
    public void orderWithAuthAndValidIngredients() {
        // берём любые 3 корректных ID
        List<String> ingredients = IngredientSteps.getAllIngredientIds()
                .stream().limit(3).collect(Collectors.toList());

        OrderRequest req = new OrderRequest(ingredients);
        OrderSteps.create(token, req)
                .then()
                .statusCode(200)
                .body("success", is(true));
    }

    @Test
    @DisplayName("2. Без авторизации (валидные ингредиенты) → 401 + You should be authorised")
    @Description("Проверяем, что без токена возвращается 401 и текст You should be authorised")
    public void orderWithoutAuth() {
        List<String> ingredients = IngredientSteps.getAllIngredientIds()
                .stream().limit(1).collect(Collectors.toList());

        OrderRequest req = new OrderRequest(ingredients);
        OrderSteps.create("", req)
                .then()
                .statusCode(401) // В постмане приходит ответ 200
                .body("message", is("You should be authorised"));
    }

    @Test
    @DisplayName("3. С авторизацией, но без ингредиентов → 400 + Ingredient ids must be provided")
    @Description("Проверяем, что при пустом списке ингредиентов возвращается 400 + сообщение об ошибке")
    public void orderWithAuthWithoutIngredients() {
        OrderSteps.create(token, new OrderRequest(Collections.emptyList()))
                .then()
                .statusCode(400)
                .body("message", containsString("Ingredient ids must be provided"));
    }

    @Test
    @DisplayName("4. С авторизацией и валидным по формату, но несуществующим ID → 500")
    @Description("Проверяем, что при указании несуществующего, но корректного по формату ID ингредиента возвращается код 500")
    public void orderWithAuthInvalidIngredientHash() {
        // Генерируем случайный UUID
        String badId = UUID.randomUUID().toString();

        // можно законтролировать длину (для отладки)
        System.out.println("BadId = " + badId + ", length=" + badId.length());

        OrderRequest req = new OrderRequest(Collections.singletonList(badId));
        OrderSteps.create(token, req)
                .then()
                .log().all()
                .statusCode(500);
    }
}