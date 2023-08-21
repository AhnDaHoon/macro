package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnumTest;
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

    @Value("${melon.id}")
    private String id;

    @Value("${melon.pwd}")
    private String pwd;

    MelonTicketServiceTest melonTicketServiceTest = new MelonTicketServiceTest();

    MelonInfoTest melonInfoTest;

    WebDriver driver;

    Tesseract tesseract;

    WebElement element;

    Wait<WebDriver> wait;

    @BeforeEach
    void setupTest() {
        driver = melonTicketServiceTest.getMainDriver();
        wait = melonTicketServiceTest.getWaitDriver();
        tesseract = melonTicketServiceTest.getTesseract();

        melonInfoTest = MelonInfoTest.builder()
                .id(id)
                .pwd(pwd)
                .loginType(LoginTypeEnumTest.MELON)
                .build();
    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void 멜론_티켓_URL_연결() {
        driver.get(melonInfoTest.getMelonTicketUrl());
        String title = driver.getTitle();

        // Verify
        assertThat(title).contains("멜론 티켓");
    }

    @Test
    void 멜론_티켓_로그인_페이지_이동(){
        driver.get(melonInfoTest.getReserveTicketUrl());
    }

    @Test
    void 멜론_티켓_카카오_계정_로그인_버튼_클릭() throws InterruptedException {
        melonInfoTest.setLoginType(LoginTypeEnumTest.KAKAO);
        moveMelonLoginPage(melonInfoTest);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://accounts.kakao.com/login");
    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인_버튼_클릭(){
        moveMelonLoginPage(melonInfoTest);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://member.melon.com/muid/family/ticket/login/web/login_informM.htm");
    }

    @Test
    void 멜론_티켓_카카오_아이디_로그인(){
        melonInfoTest.setLoginType(LoginTypeEnumTest.KAKAO);
        moveMelonLoginPage(melonInfoTest);

        WebElement inputId = melonTicketServiceTest.findId("loginId--1");
        WebElement inputPwd = melonTicketServiceTest.findId("password--2");
        WebElement submitBtn = melonTicketServiceTest.findClass("submit");

        inputId.sendKeys(melonInfoTest.getId());
        inputPwd.sendKeys(melonInfoTest.getPwd());
        submitBtn.click();

        // 카카오톡 인증번호 입력은 수동으로 해야함


    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인(){
        moveMelonLoginPage(melonInfoTest);

        WebElement inputId = melonTicketServiceTest.findId("id");
        WebElement inputPwd = melonTicketServiceTest.findId("pwd");
        WebElement btnLogin = melonTicketServiceTest.findId("btnLogin");

        inputId.sendKeys(melonInfoTest.getId());
        inputPwd.sendKeys(melonInfoTest.getPwd());
        btnLogin.click();

        WebElement btnLogout = melonTicketServiceTest.findId("btnLogout");

        String logout  = btnLogout.getText();

        assertThat(logout).contains("로그아웃");
    }

    public void moveMelonLoginPage(MelonInfoTest melonInfoTest){
        driver.get(melonInfoTest.getLoginUrl());

        switch (melonInfoTest.getLoginType()){
            case KAKAO -> {
                WebElement kakaoBtn = melonTicketServiceTest.findClass("kakao");
                kakaoBtn.click();
            }
            case MELON -> {
                WebElement melonBtn = melonTicketServiceTest.findClass("melon");
                melonBtn.click();
            }
        }

        int usePageNumber = melonInfoTest.getLoginType().getUsePageNumber();
        melonTicketServiceTest.windowHandler(usePageNumber);
    }


}


//driver.navigate().to("https://selenium.dev");
