package com.stellarburgers.steps;

import com.stellarburgers.client.StellarBurgersClient;
import com.stellarburgers.model.LoginRequest;
import com.stellarburgers.model.UpdateUserRequest;
import com.stellarburgers.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserSteps {
    @Step("Регистрируем пользователя {user.email}")
    public static Response create(User user) {
        return StellarBurgersClient.registerUser(user);
    }

    @Step("Логинимся как {email}")
    public static Response login(String email, String password) {
        return StellarBurgersClient.loginUser(new LoginRequest(email, password));
    }

    @Step("Обновляем пользователя с токеном {token}")
    public static Response update(String token, UpdateUserRequest req) {
        return StellarBurgersClient.updateUser(token, req);
    }

    @Step("Удаляем пользователя с токеном {token}")
    public static Response delete(String token) {
        return StellarBurgersClient.deleteUser(token);
    }
}