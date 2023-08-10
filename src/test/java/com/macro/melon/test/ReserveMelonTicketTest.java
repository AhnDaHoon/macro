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

    MelonInfo melonInfo;

    WebDriver driver;

    WebElement element;

    Wait<WebDriver> wait;


    @BeforeEach
    void setupTest() {
        driver = melonTicket.getMainDriver();
        wait = melonTicket.getWaitDriver();

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
    void 예매_사이트_이동(){
        melonInfo.setProdId("208510");
        melonInfo.setTicketdate("20230909");
        melonInfo.setTicketTime(0);
        melonTicket.melonLogin(melonInfo);
    }

    /**
     * 팝업이 있는 페이지의 prodId가 필요함
     */
    @Test
    void 팝업이_있을_때_제거_클릭(){
        melonInfo.setProdId("208514");
        melonTicket.melonLogin(melonInfo);
    }

    @Test
    void 리스트_날짜_선택(){
        melonInfo.setTagId("dateSelect_");
        melonInfo.setProdId("208510");
        melonInfo.setTicketdate("20230909");
        melonInfo.setTicketTime(0);

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);
    }

    @Test
    void 캘린더_날짜_선택(){
        melonInfo.setTagId("calendar_SelectId_");
        melonInfo.setProdId("208169");
        melonInfo.setTicketdate("20230812");
        melonInfo.setTicketTime(0);



        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);
    }

    @Test
    void 리스트_시간_선택(){
        melonInfo.setTagId("dateSelect_");
        melonInfo.setProdId("208510");
        melonInfo.setTicketdate("20230909");
        melonInfo.setTicketTime(0);

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);
    }

    @Test
    void 캘린더_시간_선택(){
        melonInfo.setTagId("calendar_SelectId_");
        melonInfo.setProdId("208169");
        melonInfo.setTicketdate("20230812");
        melonInfo.setTicketTime(0);

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);

    }


    @Test
    void 예매하기_버튼_클릭(){
        melonInfo.setTagId("dateSelect_");
        melonInfo.setProdId("208510");
        melonInfo.setTicketdate("20230909");
        melonInfo.setTicketTime(0);

        melonTicket.melonLogin(melonInfo);

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

}
