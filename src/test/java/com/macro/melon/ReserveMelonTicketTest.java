package com.macro.melon;

import com.macro.melon.config.LoginTypeEnum;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * !!테스트코드 실행하기 전 ReserveTicketUrl에 예매가 가능한 url로 수정한 뒤 실행
 *
 */
@SpringBootTest
public class ReserveMelonTicketTest {

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

//    String prodId = "208169";
//    String ticketdate = "20230812";

    // 예매할 시간 첫번 째는 0번
    int ticketTime = 0;


    @BeforeEach
    void setupTest() {
        driver = melonTicket.getMainDriver();
        wait = melonTicket.getWaitDriver();
    }

//    @AfterEach
//    void teardown() {
//        driver.quit();
//    }

    @Test
    void 멜론_티켓_예매_사이트_이동(){
        melonLogin();
    }

    @Test
    void 멜론_티켓_팝업이_있을_때_제거_클릭(){
        melonLogin();
    }

    @Test
    void 멜론_티켓_리스트_날짜_선택(){
        prodId = "208510";
        ticketdate = "20230909";
        melonLogin();

        selectDate("dateSelect_");
    }

    @Test
    void 멜론_티켓_캘린더_날짜_선택(){
        prodId = "208169";
        ticketdate = "20230812";
        melonLogin();

        selectDate("calendar_SelectId_");
    }

    @Test
    void 멜론_티켓_리스트_시간_선택(){
        prodId = "208510";
        ticketdate = "20230909";
        melonLogin();

        selectDate("dateSelect_");

        selectTime();
    }

    @Test
    void 멜론_티켓_캘린더_시간_선택(){
        prodId = "208169";
        ticketdate = "20230812";
        melonLogin();

        selectDate("calendar_SelectId_");

        selectTime();
    }


    @Test
    void 멜론_티켓_예매하기_버튼_클릭(){
        prodId = "208510";
        ticketdate = "20230909";
        melonLogin();

        selectDate("dateSelect_");

        driver.findElement(By.id("ticketReservation_Btn")).click();
    }

    void melonLogin(){
        MelonInfo melonInfo = melonTicket.moveMelonLoginPage(LoginTypeEnum.MELON, melonTicket);
        melonInfo.setId("id");
        melonInfo.setPwd("pwd");

        driver.findElement(By.id("id")).sendKeys(melonInfo.getId());
        driver.findElement(By.id("pwd")).sendKeys(melonInfo.getPwd());
        driver.findElement(By.id("btnLogin")).click();
        String logout  = driver.findElement(By.id("btnLogout")).getText();
        assertThat(logout).contains("로그아웃");

        driver.navigate().to(ReserveTicketUrl+prodId);
        String ticketReservationBtn = driver.findElement(By.id("ticketReservation_Btn")).getText();

        // 팝업 제거
        try {
            driver.findElement(By.id("noticeAlert_layerpopup_cookie")).click();
        }catch (NoSuchElementException e){
            // 예외를 무시하고 아무 작업도 수행하지 않음
        }
    }

    void selectDate(String tagId){
        element = driver.findElement(By.id(tagId+ticketdate));
        System.out.println("element = " + element);
        element.click();
    }

    void selectTime(){
        // 날짜의 선택의 경우 여러 개여서 List로 받는다.
        List<WebElement> itemTimeList = driver.findElements(By.className("item_time"));
        while (itemTimeList.size() == 0){
            System.out.println("실패" );
            itemTimeList = driver.findElements(By.className("item_time"));
        }
        System.out.println("itemTimeList = " + itemTimeList);
        itemTimeList.get(ticketTime).click();

    }
}
