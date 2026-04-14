package com.agibank.utils;

import io.github.bonigarcia.wdm.WebDriverManager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;


public class Base {
    public static WebDriver driver;

    @BeforeEach
    public void setup() {
        driver = DriverFactory.getDriver();
        driver.get("https://blogdoagi.com.br/");
    }

    @AfterEach
    public void tearDown() {
        DriverFactory.quitDriver();
    }
}