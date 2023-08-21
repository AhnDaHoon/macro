package com.macro.melon.config;

import io.github.bonigarcia.wdm.WebDriverManager;
import net.sourceforge.tess4j.Tesseract;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MelonConfigTest {

    @Bean
    public WebDriver getMainDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        WebDriver driver = new ChromeDriver(options);

//        WebDriverManager.edgedriver().setup();
//        EdgeOptions options = new EdgeOptions();
//        options.addArguments("--disable-popup-blocking");
//        WebDriver driver = new EdgeDriver(options);
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
