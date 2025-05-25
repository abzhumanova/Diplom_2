package com.stellarburgers.steps;

import com.stellarburgers.client.StellarBurgersClient;
import com.stellarburgers.model.LoginRequest;
import com.stellarburgers.model.UpdateUserRequest;
import com.stellarburgers.model.User;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class UserSteps {

    @Step("Отправляем запрос на регистрацию пользователя")
    public static Response sendRegisterUserRequest(User user) {
        return StellarBurgersClient.registerUser(user);
    }

    @Step("Отправляем запрос на логин пользователя")
    public static Response sendLoginUserRequest(String email, String password) {
        return StellarBurgersClient.loginUser(new LoginRequest(email, password));
    }

    @Step("Отправляем запрос на обновление пользователя")
    public static Response sendUpdateUserRequest(String token, UpdateUserRequest req) {
        return StellarBurgersClient.updateUser(token, req);
    }

    @Step("Отправляем запрос на удаление пользователя")
    public static Response sendDeleteUserRequest(String token) {
        return StellarBurgersClient.deleteUser(token);
    }
}