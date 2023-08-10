package com.macro.melon;

import com.macro.melon.config.LoginTypeEnum;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * 멜론 티켓 연결 테스트
 */
@SpringBootTest
@PropertySource("classpath:test.yml")
public class MelonLoginTest {

    @Value("${melonId}")
    private String id;

    @Value("${melonPwd}")
    private String pwd;

    MelonTicket melonTicket = new MelonTicket();

    WebDriver driver;

    WebElement element;

    Wait<WebDriver> wait;

    @BeforeEach
    void setupTest() {
        driver = melonTicket.getMainDriver();
        wait = melonTicket.getWaitDriver();
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
        MelonInfo melonInfo = melonTicket.moveMelonLoginPage(LoginTypeEnum.KAKAO, melonTicket);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://accounts.kakao.com/login");
    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인_버튼_클릭(){
        MelonInfo melonInfo = melonTicket.moveMelonLoginPage(LoginTypeEnum.MELON, melonTicket);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://member.melon.com/muid/family/ticket/login/web/login_informM.htm");
    }

    @Test
    void 멜론_티켓_카카오_아이디_로그인(){
        MelonInfo melonInfo = melonTicket.moveMelonLoginPage(LoginTypeEnum.KAKAO, melonTicket);
        melonInfo.setId(id);
        melonInfo.setPwd(pwd);

        driver.findElement(By.id("loginId--1")).sendKeys(melonInfo.getId());
        driver.findElement(By.id("password--2")).sendKeys(melonInfo.getPwd());
        driver.findElement(By.className("submit")).click();

        // 카카오톡 인증번호 입력은 수동으로 해야함


    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인(){
        MelonInfo melonInfo = melonTicket.moveMelonLoginPage(LoginTypeEnum.MELON, melonTicket);
        melonInfo.setId(id);
        melonInfo.setPwd(pwd);

        System.out.println("id = " + id);
        System.out.println("pwd = " + pwd);

        driver.findElement(By.id("id")).sendKeys(melonInfo.getId());
        driver.findElement(By.id("pwd")).sendKeys(melonInfo.getPwd());
        driver.findElement(By.id("btnLogin")).click();

        String logout  = driver.findElement(By.id("btnLogout")).getText();

        assertThat(logout).contains("로그아웃");
    }


}


//driver.navigate().to("https://selenium.dev");
