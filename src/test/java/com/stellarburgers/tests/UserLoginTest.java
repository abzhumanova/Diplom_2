package com.stellarburgers.tests;

import com.stellarburgers.model.User;
import com.stellarburgers.steps.UserSteps;
import com.github.javafaker.Faker;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import static org.hamcrest.Matchers.*;

public class UserLoginTest {
    private User user;
    private String token;
    private final Faker faker = new Faker();

    @Before
    public void setUp() {
        user = new User(faker.internet().emailAddress(), faker.internet().password(), faker.name().firstName());
        Response r = UserSteps.sendRegisterUserRequest(user);
        token = r.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.sendDeleteUserRequest(token).then().statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    @DisplayName("Успешный вход с валидными учетными данными")
    @Description("Проверка успешного входа с валидными учетными данными.")
    public void loginSuccess() {
        UserSteps.sendLoginUserRequest(user.getEmail(), user.getPassword())
                .then().statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue());
    }

    @Test
    @DisplayName("Неудачный вход с некорректным паролем")
    @Description("Проверка неудачного входа с валидным email и некорректным паролем.")
    public void loginWithIncorrectPassword() {
        UserSteps.sendLoginUserRequest(user.getEmail(), faker.internet().password()) // Correct email, incorrect password
                .then().statusCode(401)
                .body("message", is("email or password are incorrect"));
    }


    @Test
    @DisplayName("Неудачный вход с невалидными учетными данными")
    @Description("Проверка неудачного входа со случайными (невалидными) email и паролем.")
    public void loginFail() {
        UserSteps.sendLoginUserRequest(faker.internet().emailAddress(), faker.internet().password()) // Random data for fail case
                .then().statusCode(401)
                .body("message", is("email or password are incorrect"));
    }
}