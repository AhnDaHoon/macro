package com.macro.melon.test;

import com.macro.melon.config.CalendarTypeEnumTest;
import com.macro.melon.config.LoginTypeEnumTest;
import com.macro.melon.test.seat.MelonSeatTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MelonCalendarTest {


    @Value("${melon.id}")
    private String id;

    @Value("${melon.pwd}")
    private String pwd;

    @Value("${melon.folderPath}")
    private String folderPath;

    MelonTicketServiceTest melonTicketServiceTest = new MelonTicketServiceTest();

    MelonSeatTest melonSeatTest = null;

    MelonInfoTest melonInfoTest;

    WebDriver driver;

    Wait<WebDriver> wait;

    void settingListDate(){
        melonInfoTest.setCalendarType(CalendarTypeEnumTest.LIST);
        melonInfoTest.setProdId("208510");
        melonInfoTest.setTicketdate("20230909");
        melonInfoTest.setTicketTime(0);
    }

    void settingCalendarDate(){
        melonInfoTest.setCalendarType(CalendarTypeEnumTest.CALENDAR);
        melonInfoTest.setProdId("208169");
        melonInfoTest.setTicketdate("20230822");
        melonInfoTest.setTicketTime(0);
    }

    void settingCommonDate(){
        melonInfoTest.setCalendarType(CalendarTypeEnumTest.LIST);
        melonInfoTest.setProdId("208419");
        melonInfoTest.setTicketdate("20230908");
        melonInfoTest.setTicketTime(0);
    }


    @BeforeEach
    void setupTest() {
        driver = melonTicketServiceTest.getMainDriver();
        wait = melonTicketServiceTest.getWaitDriver();
        melonSeatTest = new MelonSeatTest(melonTicketServiceTest);
        melonInfoTest = MelonInfoTest.builder()
                .id(id)
                .pwd(pwd)
                .loginType(LoginTypeEnumTest.MELON)
                .build();
    }

//    @AfterEach
//    void teardown() {
//        driver.quit();
//    }

    @Test
    void 리스트_날짜_선택(){
        settingListDate();

        melonTicketServiceTest.melonLogin(melonInfoTest);

        melonTicketServiceTest.selectDate(melonInfoTest);
    }

    @Test
    void 캘린더_날짜_선택(){
        settingCalendarDate();

        melonTicketServiceTest.melonLogin(melonInfoTest);

        melonTicketServiceTest.selectDate(melonInfoTest);
    }

    @Test
    void 리스트_시간_선택(){
        settingListDate();

        melonTicketServiceTest.melonLogin(melonInfoTest);

        melonTicketServiceTest.selectDate(melonInfoTest);

        melonTicketServiceTest.selectTime(melonInfoTest);
    }

    @Test
    void 캘린더_시간_선택(){
        settingCalendarDate();

        melonTicketServiceTest.melonLogin(melonInfoTest);

        melonTicketServiceTest.selectDate(melonInfoTest);

        melonTicketServiceTest.selectTime(melonInfoTest);
    }

    @Test
    void 리스트_날짜_형식으로_변경_후_날짜_선택(){
        settingCommonDate();

        melonTicketServiceTest.melonLogin(melonInfoTest);

        melonTicketServiceTest.selectDate(melonInfoTest);

        melonTicketServiceTest.selectTime(melonInfoTest);
    }
}
