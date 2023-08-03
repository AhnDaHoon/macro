package com.macro.selenium;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManager;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 크롬 연결 테스트
 */
public class ChromeTest {

    WebDriver driver;

    @BeforeAll
    static void setupClass() {
        ChromeOptions co = new ChromeOptions();
        co.setBrowserVersion("Stable");
        String chPath = SeleniumManager.getInstance().getDriverPath(co, false).getDriverPath();
        System.out.println("chPath = " + chPath);
    }

    @BeforeEach
    void setupTest() {
        driver = new ChromeDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void test() {
        // Exercise
        driver.get("https://bonigarcia.dev/selenium-webdriver-java/");
        String title = driver.getTitle();

        // Verify
        assertThat(title).contains("Hands-On Selenium WebDriver with Java");
    }
}
