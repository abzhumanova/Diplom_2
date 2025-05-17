package com.stellarburgers.base;

import io.qameta.allure.junit4.AllureJunit4;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class BaseTest {
    @BeforeClass
    public static void init() {
        // общая конфигурация логирования, RestAssured и т.п.
    }
}