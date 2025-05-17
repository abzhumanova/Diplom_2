package com.stellarburgers.tests;

import com.stellarburgers.model.OrderRequest;
import com.stellarburgers.model.User;
import com.stellarburgers.steps.OrderSteps;
import com.stellarburgers.steps.UserSteps;
import com.stellarburgers.util.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class OrderCreationTest {
    private User user;
    private String token;

    @Before
    public void setUp() {
        user = TestDataGenerator.randomUser();
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
    public void orderWithAuth() {
        OrderSteps.create(token, new OrderRequest())
                .then().statusCode(400)
                .body("message", containsString("Ingredient ids must be provided"));
    }

    @Test
    public void orderNoAuth() {
        OrderSteps.create("", TestDataGenerator.validOrder())
                .then().statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void orderWithIngredients() {
        OrderSteps.create(token, TestDataGenerator.validOrder())
                .then().statusCode(200)
                .body("success", is(true));
    }

    @Test
    public void orderWithoutIngredients() {
        OrderSteps.create(token, new OrderRequest())
                .then().statusCode(400)
                .body("message", containsString("Ingredient ids must be provided"));
    }

    @Test
    public void orderInvalidHash() {
        OrderSteps.create(token, TestDataGenerator.invalidOrder())
                .then().statusCode(500);
    }
}