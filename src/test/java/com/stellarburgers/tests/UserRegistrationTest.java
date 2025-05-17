package com.stellarburgers.tests;

import com.stellarburgers.model.User;
import com.stellarburgers.steps.UserSteps;
import com.stellarburgers.util.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserRegistrationTest {
    private User user;
    private String token;

    @Before
    public void setUp() {
        user = TestDataGenerator.randomUser();
    }

    @After
    public void tearDown() {
        if (token != null) {
            UserSteps.delete(token).then().statusCode(anyOf(is(200), is(202)));
        }
    }

    @Test
    public void createUniqueUser() {
        Response r = UserSteps.create(user);
        r.then().statusCode(200).body("success", is(true));
        token = r.jsonPath().getString("accessToken");
    }

    @Test
    public void createDuplicateUser() {
        Response first = UserSteps.create(user);
        first.then().statusCode(200);
        token = first.jsonPath().getString("accessToken");
        UserSteps.create(user)
                .then().statusCode(403)
                .body("message", containsString("User already exists"));
    }

    @Test
    public void createUserMissingField() {
        User bad = new User();
        bad.setPassword("12345678");
        bad.setName("NoEmail");
        UserSteps.create(bad)
                .then().statusCode(403)
                .body("message", containsString("email"));
    }
}