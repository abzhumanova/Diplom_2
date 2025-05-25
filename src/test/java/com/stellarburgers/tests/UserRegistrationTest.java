package com.stellarburgers.tests;

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

public class UserRegistrationTest {
    private User user;
    private String token;
    private final Faker faker = new Faker();

    @Before
    public void setUp() {
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.sendDeleteUserRequest(token).then().statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    @DisplayName("Создание уникального пользователя")
    @Description("Проверка успешного создания пользователя с уникальными учетными данными.")
    public void createUniqueUser() {
        Response r = UserSteps.sendRegisterUserRequest(user);
        r.then().statusCode(200).body("success", is(true));
        token = r.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Создание пользователя с дублирующимися данными")
    @Description("Проверка, что попытка создать пользователя с существующими учетными данными приводит к ошибке.")
    public void createDuplicateUser() {
        Response r = UserSteps.sendRegisterUserRequest(user);
        r.then().statusCode(200);
        token = r.jsonPath().getString("accessToken");
        UserSteps.sendRegisterUserRequest(user)
                .then().statusCode(403)
                .body("message", containsString("User already exists"));
    }

    @Test
    @DisplayName("Создание пользователя без имени")
    @Description("Проверка, что создание пользователя без имени приводит к ошибке.")
    public void createUserMissingName() {
        User bad = new User(faker.internet().emailAddress(), faker.internet().password(), null);
        UserSteps.sendRegisterUserRequest(bad)
                .then().statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без email")
    @Description("Проверка, что создание пользователя без email приводит к ошибке.")
    public void createUserMissingEmail() {
        User bad = new User(null, faker.internet().password(), faker.name().firstName());
        UserSteps.sendRegisterUserRequest(bad)
                .then().statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }

    @Test
    @DisplayName("Создание пользователя без пароля")
    @Description("Проверка, что создание пользователя без пароля приводит к ошибке.")
    public void createUserMissingPassword() {
        User bad = new User(faker.internet().emailAddress(), null, faker.name().firstName());
        UserSteps.sendRegisterUserRequest(bad)
                .then().statusCode(403)
                .body("message", is("Email, password and name are required fields"));
    }
}