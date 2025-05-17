package com.stellarburgers.tests;

import com.stellarburgers.model.User;
import com.stellarburgers.steps.UserSteps;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserLoginTest {
    private User user;
    private String token;

    @Before
    public void setUp() {
        user = new User("abzhumanova@yandex.ru","UhamakKp19","Айслу");
        Response r = UserSteps.create(user);
        token = r.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.delete(token).then().statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    public void loginSuccess() {
        UserSteps.login(user.getEmail(), user.getPassword())
                .then().statusCode(200)
                .body("success", is(true))
                .body("accessToken", notNullValue());
    }

    @Test
    public void loginFail() {
        UserSteps.login("wrong@mail.com", "badpass")
                .then().statusCode(401)
                .body("message", containsString("incorrect"));
    }
}