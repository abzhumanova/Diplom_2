package com.stellarburgers.tests;

import com.stellarburgers.model.UpdateUserRequest;
import com.stellarburgers.model.User;
import com.stellarburgers.steps.UserSteps;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.junit4.DisplayName;
import io.qameta.allure.Description;

import static org.hamcrest.Matchers.*;

public class UserUpdateTest {

    private User user;
    private String token;
    private Faker faker = new Faker();

    @Before
    public void setUp() {
        // Создаём тестового пользователя и сохраняем токен
        user = new User(
                faker.internet().emailAddress(),
                faker.internet().password(),
                faker.name().firstName()
        );
        Response r = UserSteps.create(user);
        token = r.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        // Удаляем пользователя (если токен есть)
        if (token != null) {
            UserSteps.delete(token)
                    .then().statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    @DisplayName("Изменение имени пользователя с авторизацией")
    @Description("Проверяем, что авторизованный пользователь может изменить своё имя и API вернёт новый name")
    public void updateWithAuth() {
        // Генерируем новое имя и сохраняем в переменную
        String newName = faker.name().firstName();
        UpdateUserRequest upd = new UpdateUserRequest()
                .setName(newName);

        UserSteps.update(token, upd)
                .then()
                .statusCode(200)
                .body("user.name", is(newName));
    }

    @Test
    @DisplayName("Изменение email пользователя с авторизацией")
    @Description("Проверяем, что авторизованный пользователь может изменить свой email и API вернёт новый email")
    public void updateEmailWithAuth() {
        String newEmail = faker.internet().emailAddress();
        UpdateUserRequest upd = new UpdateUserRequest()
                .setEmail(newEmail);

        UserSteps.update(token, upd)
                .then()
                .statusCode(200)
                .body("user.email", is(newEmail));
    }

    @Test
    @DisplayName("Изменение данных пользователя без авторизации")
    @Description("Проверяем, что запрос без токена возвращает 401 и сообщение о необходимости авторизации")
    public void updateWithoutAuth() {
        UpdateUserRequest upd = new UpdateUserRequest()
                .setEmail(faker.internet().emailAddress());

        UserSteps.update("", upd)
                .then()
                .statusCode(401)
                .body("message", is("You should be authorised"));
    }
}