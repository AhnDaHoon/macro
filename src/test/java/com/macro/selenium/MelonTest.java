package com.macro.selenium;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * 멜론 티켓 연결 테스트
 */
@SpringBootTest
public class MelonTest {

    MelonTiket melonTiket = new MelonTiket();

    WebDriver driver;

    WebElement element;

    Wait<WebDriver> wait;

    @BeforeEach
    void setupTest() {
        driver = melonTiket.getMainDriver();
        wait = melonTiket.getWaitDriver();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void 멜론_티켓_URL_연결() {
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .url("https://ticket.melon.com/main/index.htm")
                .build();

        driver.get(melonInfo.getUrl());
        String title = driver.getTitle();

        // Verify
        assertThat(title).contains("멜론 티켓");
    }

    @Test
    void 멜론_티켓_로그인_페이지_이동(){
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .url("https://member.melon.com/muid/family/ticket/login/web/login_inform.htm?cpId=WP15&returnPage=https://ticket.melon.com/main/readingGate.htm")
                .loginType(LoginTypeEnum.KAKAO)
                .build();
        driver.get(melonInfo.getUrl());
    }

    @Test
    void 멜론_티켓_카카오_계정_로그인_버튼_클릭() throws InterruptedException {
        MelonInfo melonInfo = melonTiket.moveMelonLoginForm(LoginTypeEnum.KAKAO, element, melonTiket);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://accounts.kakao.com/login");
    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인_버튼_클릭(){
        MelonInfo melonInfo = melonTiket.moveMelonLoginForm(LoginTypeEnum.MELON, element, melonTiket);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://member.melon.com/muid/family/ticket/login/web/login_informM.htm");
    }

    @Test
    void 멜론_티켓_카카오_아이디_로그인(){

    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인(){

    }


}


//driver.navigate().to("https://selenium.dev");
