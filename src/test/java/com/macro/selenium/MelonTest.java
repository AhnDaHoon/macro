package com.macro.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 멜론 티켓 연결 테스트
 */
@SpringBootTest
public class MelonTest {

    String url = "https://ticket.melon.com/main/index.htm";

    WebDriver driver;

    WebElement element;

    @BeforeAll
    static void setupClass() {
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        driver = new ChromeDriver(options);


    }

    @AfterEach
    @Disabled
    void teardown() {
        driver.quit();
    }

    @Test
    void 멜론_티켓_URL_연결() {
        driver.get(url);
        String title = driver.getTitle();

        // Verify
        assertThat(title).contains("멜론 티켓");
    }

    @Test
    void 멜론_티켓_로그인(){
        driver.get(url);
        String title = driver.getTitle();

        element = driver.findElement(By.id("login_area"));
        element.click();
    }


}
