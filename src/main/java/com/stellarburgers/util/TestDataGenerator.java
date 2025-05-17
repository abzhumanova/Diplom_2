package com.stellarburgers.util;

import com.github.javafaker.Faker;
import com.stellarburgers.model.User;
import com.stellarburgers.model.OrderRequest;

import java.util.List;

public class TestDataGenerator {
    private static final Faker faker = new Faker();

    public static User randomUser() {
        return new User(
                faker.internet().emailAddress(),
                faker.internet().password(8, 16),
                faker.name().fullName()
        );
    }

    public static OrderRequest validOrder() {
        return new OrderRequest(List.of(
                "61c0c5a71d1f82001bdaaa6d",
                "61c0c5a71d1f82001bdaaa6f"
        ));
    }

    public static OrderRequest invalidOrder() {
        return new OrderRequest(List.of("invalid_hash"));
    }
}