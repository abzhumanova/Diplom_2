package com.stellarburgers.tests;

import com.stellarburgers.model.User;
import com.stellarburgers.steps.OrderSteps;
import com.stellarburgers.steps.UserSteps;
import com.stellarburgers.util.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserOrdersTest {
    private User user;
    private String token;

    @Before
    public void setUp() {
        user = TestDataGenerator.randomUser();
        Response r = UserSteps.create(user);
        token = r.jsonPath().getString("accessToken");
        OrderSteps.create(token, TestDataGenerator.validOrder());
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.delete(token).then().statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    public void getOrdersAuth() {
        OrderSteps.list(token)
                .then().statusCode(200)
                .body("orders", notNullValue());
    }

    @Test
    public void getOrdersNoAuth() {
        OrderSteps.list("")
                .then().statusCode(401)
                .body("message", containsString("authorised"));
    }
}