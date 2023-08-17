package com.macro.config;

import net.sourceforge.tess4j.Tesseract;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MelonConfig {

    @Bean
    public WebDriver getMainDriver() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        WebDriver driver = new ChromeDriver(options);
        return driver;
    }

    @Bean
    public WebDriverWait getWaitDriver() {
        return new WebDriverWait(getMainDriver(), Duration.ofSeconds(60*60*3), Duration.ofMillis(150));
    }

    @Bean
    public Tesseract getTesseract() {
        Tesseract instance = new Tesseract();
        instance.setDatapath("C:/Users/user/Desktop/ticket_img/a");
        return instance;
    }

}
