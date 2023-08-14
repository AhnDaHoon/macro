package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnum;
import com.macro.melon.config.Triple;
import com.macro.melon.test.file.CaptchaImgDownloadMelon;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * !! 실제 예매가 가능한 사이트 정보를 입력하고 테스트 돌릴 것
 *
 */
@SpringBootTest
@PropertySource("classpath:test.yml")
public class ReserveMelonTicketTest {

    @Value("${melon.id}")
    private String id;

    @Value("${melon.pwd}")
    private String pwd;

    @Value("${melon.folderPath}")
    private String folderPath;

    MelonTicket melonTicket = new MelonTicket();

    CaptchaImgDownloadMelon file = new CaptchaImgDownloadMelon();

    MelonInfo melonInfo;

    WebDriver driver;

    Wait<WebDriver> wait;

    void settingListDate(){
        melonInfo.setTagId("dateSelect_");
        melonInfo.setProdId("208510");
        melonInfo.setTicketdate("20230909");
        melonInfo.setTicketTime(0);
    }

    void settingCalendarDate(){
        melonInfo.setTagId("calendar_SelectId_");
        melonInfo.setProdId("208169");
        melonInfo.setTicketdate("20230822");
        melonInfo.setTicketTime(0);
    }


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

//    @AfterEach
//    void teardown() {
//        driver.quit();
//    }

    @Test
    void 예매_사이트_이동(){
        settingListDate();
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
        settingListDate();

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);
    }

    @Test
    void 캘린더_날짜_선택(){
        settingCalendarDate();

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);
    }

    @Test
    void 리스트_시간_선택(){
        settingListDate();

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);
    }

    @Test
    void 캘린더_시간_선택(){
        settingCalendarDate();

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);

    }


    @Test
    void 예매하기_버튼_클릭(){
        settingListDate();

        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);

        melonTicket.findId("ticketReservation_Btn").click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        melonTicket.windowHandler(2);
        String currentUrl = driver.getCurrentUrl();
        while (currentUrl.equals("about:blank")){
            currentUrl = driver.getCurrentUrl();
        }
        System.out.println("currentUrl = " + currentUrl);
        assertThat(currentUrl).contains("https://ticket.melon.com/reservation/popup/onestop.htm");
    }

    @Test
    void 좌석_선택(){
        settingListDate();
        melonInfo.setRsrvVolume(3);
        int rsrvVolume = melonInfo.getRsrvVolume();
        melonTicket.melonLogin(melonInfo);

        melonTicket.selectDate(melonInfo);

        melonTicket.selectTime(melonInfo);

        melonTicket.findId("ticketReservation_Btn").click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicket.windowHandler(2);

        System.out.println("새로운 페이지 감지");

        WebElement oneStopFrame = melonTicket.findFrame("oneStopFrame");
        melonTicket.switchFrame(oneStopFrame);

        List<WebElement> rectElements = melonTicket.findTagList("rect");

        List<Triple> coordinates = new ArrayList<>();
        List<WebElement> seatList = new ArrayList<>();

        for (WebElement rect : rectElements) {
            if(rect.getAttribute("fill").equals("#DDDDDD") && !rect.getAttribute("fill").equals("none")){
                continue;
            }
            coordinates.add(new Triple(Float.parseFloat(rect.getAttribute("y")),
                    Float.parseFloat(rect.getAttribute("x")),
                    rect));

            if(coordinates.size() >= 100) break;
        }

        Collections.sort(coordinates, Comparator.comparingDouble(Triple::getY));

        float seatX = 0;
        for (Triple triple : coordinates) {
            if(seatList.size() > 0){
                if(triple.getX() - seatX == 13){
                    seatList.add(triple.getRect());
                }else {
                    seatList.clear();
                }
            }else{
                seatList.add(triple.getRect());
            }

            if(seatList.size() >= rsrvVolume){
                break;
            }
            seatX = triple.getX();
        }

        for (WebElement webElement : seatList) {
            webElement.click();
        }

//        melonTicket.findId("nextTicketSelection").click();

    }

    @Test
    void 캡쳐_이미지_다운로드(){
        settingListDate();
        // 예매할 좌석 갯수
        melonInfo.setRsrvVolume(3);
        melonInfo.setProdId("208419");
        melonInfo.setTagId("calendar_SelectId_");
        melonInfo.setTicketdate("20230824");
        int rsrvVolume = melonInfo.getRsrvVolume();

        // 로그인
        melonTicket.melonLogin(melonInfo);

        // 날짜 선택
        melonTicket.selectDate(melonInfo);

        // 시간 선택
        melonTicket.selectTime(melonInfo);

        // 예매하기 버튼 클릭
        melonTicket.findId("ticketReservation_Btn").click();

        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicket.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        WebElement captchaImg = melonTicket.findId("captchaImg");
        String src = captchaImg.getAttribute("src");
        System.out.println("src = " + src);
        try {
            file.downloadImage(src, folderPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WebElement oneStopFrame = melonTicket.findFrame("oneStopFrame");
        melonTicket.switchFrame(oneStopFrame);
    }

    /**
     * 좌석 선택까지 완료하고 작업
     */
 



}
