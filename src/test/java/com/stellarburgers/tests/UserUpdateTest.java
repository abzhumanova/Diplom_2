package com.stellarburgers.tests;

import com.stellarburgers.model.UpdateUserRequest;
import com.stellarburgers.model.User;
import com.stellarburgers.steps.UserSteps;
import com.stellarburgers.util.TestDataGenerator;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.*;

public class UserUpdateTest {
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
    public void updateWithAuth() {
        UpdateUserRequest upd = new UpdateUserRequest().setName("NewName");
        UserSteps.update(token, upd)
                .then().statusCode(200)
                .body("user.name", is("NewName"));
    }

    @Test
    public void updateWithoutAuth() {
        UpdateUserRequest upd = new UpdateUserRequest().setEmail("no@auth.com");
        UserSteps.update("", upd)
                .then().statusCode(401)
                .body("message", containsString("authorised"));
    }
}