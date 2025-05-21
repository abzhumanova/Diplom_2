package com.stellarburgers.base;

import org.junit.BeforeClass;

public abstract class BaseTest {
    @BeforeClass
    public static void init() {
        // общая конфигурация: логирование, RestAssured.baseURI и т.п.
    }
}