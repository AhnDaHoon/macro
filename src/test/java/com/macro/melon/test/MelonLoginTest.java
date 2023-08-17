package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnum;
import net.sourceforge.tess4j.Tesseract;
import org.junit.jupiter.api.*;
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

    MelonTicketService melonTicketService = new MelonTicketService();

    MelonInfo melonInfo;

    WebDriver driver;

    Tesseract tesseract;

    WebElement element;

    Wait<WebDriver> wait;

    @BeforeEach
    void setupTest() {
        driver = melonTicketService.getMainDriver();
        wait = melonTicketService.getWaitDriver();
        tesseract = melonTicketService.getTesseract();

        melonInfo = MelonInfo.builder()
                .id(id)
                .pwd(pwd)
                .loginType(LoginTypeEnum.MELON)
                .build();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void 멜론_티켓_URL_연결() {
        driver.get(melonInfo.getMelonTicketUrl());
        String title = driver.getTitle();

        // Verify
        assertThat(title).contains("멜론 티켓");
    }

    @Test
    void 멜론_티켓_로그인_페이지_이동(){
        driver.get(melonInfo.getReserveTicketUrl());
    }

    @Test
    void 멜론_티켓_카카오_계정_로그인_버튼_클릭() throws InterruptedException {
        melonInfo.setLoginType(LoginTypeEnum.KAKAO);
        moveMelonLoginPage(melonInfo);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://accounts.kakao.com/login");
    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인_버튼_클릭(){
        moveMelonLoginPage(melonInfo);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://member.melon.com/muid/family/ticket/login/web/login_informM.htm");
    }

    @Test
    void 멜론_티켓_카카오_아이디_로그인(){
        melonInfo.setLoginType(LoginTypeEnum.KAKAO);
        moveMelonLoginPage(melonInfo);

        WebElement inputId = melonTicketService.findId("loginId--1");
        WebElement inputPwd = melonTicketService.findId("password--2");
        WebElement submitBtn = melonTicketService.findClass("submit");

        inputId.sendKeys(melonInfo.getId());
        inputPwd.sendKeys(melonInfo.getPwd());
        submitBtn.click();

        // 카카오톡 인증번호 입력은 수동으로 해야함


    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인(){
        moveMelonLoginPage(melonInfo);

        WebElement inputId = melonTicketService.findId("id");
        WebElement inputPwd = melonTicketService.findId("pwd");
        WebElement btnLogin = melonTicketService.findId("btnLogin");

        inputId.sendKeys(melonInfo.getId());
        inputPwd.sendKeys(melonInfo.getPwd());
        btnLogin.click();

        WebElement btnLogout = melonTicketService.findId("btnLogout");

        String logout  = btnLogout.getText();

        assertThat(logout).contains("로그아웃");
    }

    public void moveMelonLoginPage(MelonInfo melonInfo){
        driver.get(melonInfo.getLoginUrl());

        switch (melonInfo.getLoginType()){
            case KAKAO -> {
                WebElement kakaoBtn = melonTicketService.findClass("kakao");
                kakaoBtn.click();
            }
            case MELON -> {
                WebElement melonBtn = melonTicketService.findClass("melon");
                melonBtn.click();
            }
        }

        int usePageNumber = melonInfo.getLoginType().getUsePageNumber();
        melonTicketService.windowHandler(usePageNumber);
    }


}


//driver.navigate().to("https://selenium.dev");
