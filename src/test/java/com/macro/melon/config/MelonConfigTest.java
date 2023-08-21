package com.macro.melon.config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import io.github.bonigarcia.wdm.WebDriverManager;
import net.sourceforge.tess4j.Tesseract;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MelonConfigTest {

    @Bean
    public WebDriver getTestMainDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        WebDriver driver = new ChromeDriver();
        return driver;
    }

    @Bean
    public WebDriverWait getTestWaitDriver() {
        return new WebDriverWait(getTestMainDriver(), Duration.ofSeconds(60*60*3), Duration.ofMillis(150));
    }

    @Bean
    public Tesseract getTestTesseract() {
        Tesseract instance = new Tesseract();
        instance.setDatapath("C:/Users/user/Desktop/ticket_img/a");
        return instance;
    }

}
