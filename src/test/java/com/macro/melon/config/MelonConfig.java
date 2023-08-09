package com.macro.melon.config;

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
//        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60*60*3));
        return driver;
    }

    @Bean
    public WebDriverWait getWaitDriver() {
        return new WebDriverWait(getMainDriver(), Duration.ofSeconds(60*60*3), Duration.ofMillis(150));
    }

}
