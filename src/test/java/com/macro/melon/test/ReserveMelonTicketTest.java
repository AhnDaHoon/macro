package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * !!테스트코드 실행하기 전 ReserveTicketUrl에 예매가 가능한 url로 수정한 뒤 실행
 *
 */
@SpringBootTest
@PropertySource("classpath:test.yml")
public class ReserveMelonTicketTest {

    @Value("${melonId}")
    private String id;

    @Value("${melonPwd}")
    private String pwd;

    MelonTicket melonTicket = new MelonTicket();

    WebDriver driver;

    WebElement element;

    Wait<WebDriver> wait;
    String ReserveTicketUrl = "https://ticket.melon.com/performance/index.htm?prodId=";

    // 웹으로 만들 때 사용자 입력을 받을 input
    // 예매할 티켓의 prodId
    String prodId = "208510";
    // 예매할 티켓의 날짜
    String ticketdate = "20230909";

    // 예매할 시간 첫번 째는 0번
    int ticketTime = 0;


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
    void 예매_사이트_이동(){
        melonLogin();
    }

    @Test
    void 팝업이_있을_때_제거_클릭(){
        melonLogin();
    }

    @Test
    void 리스트_날짜_선택(){
        prodId = "208510";
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .tagId("dateSelect_")
                .ticketdate("20230909")
                .melonTicket(melonTicket)
                .build();
        melonLogin();

        melonTicket.selectDate(melonInfo);
    }

    @Test
    void 캘린더_날짜_선택(){
        prodId = "208169";
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .tagId("calendar_SelectId_")
                .ticketdate("20230812")
                .melonTicket(melonTicket)
                .build();

        melonLogin();

        melonTicket.selectDate(melonInfo);
    }

    @Test
    void 리스트_시간_선택(){
        prodId = "208510";

        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .tagId("dateSelect_")
                .ticketdate("20230909")
                .ticketTime(0)
                .melonTicket(melonTicket)
                .build();

        melonLogin();

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);
    }

    @Test
    void 캘린더_시간_선택(){
        prodId = "208169";

        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .tagId("calendar_SelectId_")
                .ticketdate("20230812")
                .ticketTime(0)
                .melonTicket(melonTicket)
                .build();

        melonLogin();

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);

    }


    @Test
    void 예매하기_버튼_클릭(){
        prodId = "208510";

        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .tagId("dateSelect_")
                .ticketdate("20230909")
                .ticketTime(0)
                .melonTicket(melonTicket)
                .build();

        melonLogin();

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);

        driver.findElement(By.id("ticketReservation_Btn")).click();

        melonTicket.newPage();

        melonTicket.windowHandler(2);
        String currentUrl = driver.getCurrentUrl();
        while (currentUrl.equals("about:blank")){
            currentUrl = driver.getCurrentUrl();
        }

        System.out.println("currentUrl = " + currentUrl);
        assertThat(currentUrl).contains("https://ticket.melon.com/reservation/popup/onestop.htm");
        
        
//        String asds = melonTicket.findClass("box_consert").getText();
//        System.out.println("asds = " + asds);
//        String titSTxtProdName = melonTicket.findClass("txt_prod_name").getText();
//        System.out.println("titSTxtProdName = " + titSTxtProdName);
    }

    /**
     * 좌석 선택까지 완료하고 작업
     */
    @Test
    void 캡쳐_이미지_자동_입력(){

    }

    @Test
    void 좌석_선택(){
//        prodId = "208510";
//        ticketdate = "20230909";
//        melonLogin();
//
//        selectDate("dateSelect_");
//
//        driver.findElement(By.id("ticketReservation_Btn")).click();
    }

    void melonLogin(){
        MelonInfo melonInfo = melonTicket.moveMelonLoginPage(LoginTypeEnum.MELON, melonTicket);
        melonInfo.setId(id);
        melonInfo.setPwd(pwd);

        WebElement id = melonTicket.findId("id");
        WebElement pwd = melonTicket.findId("pwd");
        id.sendKeys(melonInfo.getId());
        pwd.sendKeys(melonInfo.getPwd());
        WebElement btnLogin = melonTicket.findId("btnLogin");
        btnLogin.click();

        WebElement btnLogout = melonTicket.findId("btnLogout");
        String logout = btnLogout.getText();
        assertThat(logout).contains("로그아웃");

        driver.navigate().to(ReserveTicketUrl+prodId);
        String ticketReservationBtn = melonTicket.findId("ticketReservation_Btn").getText();

        // 팝업 제거
        try {
            // 팝업은 안뜰 수도 있어서 대기안함
            driver.findElement(By.id("noticeAlert_layerpopup_cookie")).click();
        }catch (NoSuchElementException e){
        }
    }

}
