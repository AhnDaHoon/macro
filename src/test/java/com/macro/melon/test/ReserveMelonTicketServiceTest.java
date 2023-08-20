package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnum;
import com.macro.melon.test.file.MelonCaptcha;
import com.macro.melon.test.seat.MelonSeat;
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

import static org.assertj.core.api.Assertions.assertThat;

/**
 * !! 실제 예매가 가능한 사이트 정보를 입력하고 테스트 돌릴 것
 *
 */
@SpringBootTest
@PropertySource("classpath:test.yml")
public class ReserveMelonTicketServiceTest{

    @Value("${melon.id}")
    private String id;

    @Value("${melon.pwd}")
    private String pwd;

    @Value("${melon.folderPath}")
    private String folderPath;

    MelonTicketServiceTest melonTicketServiceTest = new MelonTicketServiceTest();

    MelonCaptcha file = new MelonCaptcha();

    MelonSeat melonSeat = null;

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
        driver = melonTicketServiceTest.getMainDriver();
        wait = melonTicketServiceTest.getWaitDriver();
        melonSeat = new MelonSeat(melonTicketServiceTest);
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
        melonTicketServiceTest.melonLogin(melonInfo);
    }

    /**
     * 팝업이 있는 페이지의 prodId가 필요함
     */
    @Test
    void 팝업이_있을_때_제거_클릭(){
        melonInfo.setProdId("208514");
        melonTicketServiceTest.melonLogin(melonInfo);
    }

    @Test
    void 리스트_날짜_선택(){
        settingListDate();

        melonTicketServiceTest.melonLogin(melonInfo);

        melonTicketServiceTest.selectDate(melonInfo);
    }

    @Test
    void 캘린더_날짜_선택(){
        settingCalendarDate();

        melonTicketServiceTest.melonLogin(melonInfo);

        melonTicketServiceTest.selectDate(melonInfo);
    }

    @Test
    void 리스트_시간_선택(){
        settingListDate();

        melonTicketServiceTest.melonLogin(melonInfo);

        melonTicketServiceTest.selectDate(melonInfo);

        melonTicketServiceTest.selectTime(melonInfo);
    }

    @Test
    void 캘린더_시간_선택(){
        settingCalendarDate();

        melonTicketServiceTest.melonLogin(melonInfo);

        melonTicketServiceTest.selectDate(melonInfo);

        melonTicketServiceTest.selectTime(melonInfo);

    }


    @Test
    void 예매하기_버튼_클릭(){
        settingListDate();

        melonTicketServiceTest.melonLogin(melonInfo);

        melonTicketServiceTest.selectDate(melonInfo);

        melonTicketServiceTest.selectTime(melonInfo);

        melonTicketServiceTest.findId("ticketReservation_Btn").click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));

        melonTicketServiceTest.windowHandler(2);
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
        melonTicketServiceTest.melonLogin(melonInfo);

        melonTicketServiceTest.selectDate(melonInfo);

        melonTicketServiceTest.selectTime(melonInfo);

        melonTicketServiceTest.findId("ticketReservation_Btn").click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);

        System.out.println("새로운 페이지 감지");

        melonSeat.selectSeat(melonInfo);
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
        melonTicketServiceTest.melonLogin(melonInfo);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfo);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfo);

        // 예매하기 버튼 클릭
        melonTicketServiceTest.findId("ticketReservation_Btn").click();

        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        try {
            file.imageDownload(melonTicketServiceTest, folderPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        WebElement oneStopFrame = melonTicketServiceTest.findFrame("oneStopFrame");
        melonTicketServiceTest.switchFrame(oneStopFrame);
    }

    @Test
    void 캡쳐_이미지_자동_입력(){
        settingListDate();
        // 예매할 좌석 갯수
        melonInfo.setRsrvVolume(3);
        melonInfo.setProdId("208419");
        melonInfo.setTagId("calendar_SelectId_");
        melonInfo.setTicketdate("20230824");
        int rsrvVolume = melonInfo.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfo);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfo);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfo);

        // 예매하기 버튼 클릭
        melonTicketServiceTest.findId("ticketReservation_Btn").click();

        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        String targetFileName = "";
        try {
            targetFileName = file.imageDownload(melonTicketServiceTest, folderPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // 문자열로 변환된 값
        String convertImageToString = melonTicketServiceTest.convertImageToString(targetFileName);
        System.out.println("convertImageToString = " + convertImageToString);

        // 캡쳐 입력창
        WebElement captchaInput = melonTicketServiceTest.findId("label-for-captcha");
        System.out.println("captchaInput = " + captchaInput);
        captchaInput.sendKeys(convertImageToString);

        // 캡쳐 화면 확인 버튼
        WebElement btnComplete = melonTicketServiceTest.findId("btnComplete");
        System.out.println("btnComplete = " + btnComplete);
        btnComplete.click();

        WebElement oneStopFrame = melonTicketServiceTest.findFrame("oneStopFrame");
        melonTicketServiceTest.switchFrame(oneStopFrame);
    }

    @Test
    void 캡쳐_이미지_자동_입력_실패_시_새로고침_후_다시_입력() throws IOException, InterruptedException {
        settingListDate();
        // 예매할 좌석 갯수
        melonInfo.setRsrvVolume(2);
        melonInfo.setProdId("208419");
        melonInfo.setTagId("calendar_SelectId_");
        melonInfo.setTicketdate("20230824");
        int rsrvVolume = melonInfo.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfo);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfo);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfo);

        // 예매하기 버튼 클릭
        melonTicketServiceTest.findId("ticketReservation_Btn").click();

        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        String targetFileName = file.imageDownload(melonTicketServiceTest, folderPath);
        melonTicketServiceTest.captchaVerification(targetFileName);
        WebElement errorMessage = melonTicketServiceTest.findId("errorMessage");
        boolean displayed = errorMessage.isDisplayed();

        while (displayed){
            WebElement btnReload = melonTicketServiceTest.findId("btnReload");
            btnReload.click();
            Thread.sleep(500);
            targetFileName = file.imageDownload(melonTicketServiceTest, folderPath);

            System.out.println("targetFileName = " + targetFileName);
            melonTicketServiceTest.captchaVerification(targetFileName);
            displayed = errorMessage.isDisplayed();
        }
    }

    @Test
    void 캡쳐_이미지_입력_및_좌석_선택(){
        settingListDate();
        // 예매할 좌석 갯수
        melonInfo.setRsrvVolume(3);
        melonInfo.setProdId("208593");
        melonInfo.setTicketdate("20230902");
        int rsrvVolume = melonInfo.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfo);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfo);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfo);

        // 예매하기 버튼 클릭
        melonTicketServiceTest.findId("ticketReservation_Btn").click();

        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        try {
            String targetFileName = file.imageDownload(melonTicketServiceTest, folderPath);
            melonTicketServiceTest.captchaVerification(targetFileName);
            WebElement errorMessage = melonTicketServiceTest.findId("errorMessage");
            boolean displayed = errorMessage.isDisplayed();

            while (displayed){
                    WebElement btnReload = melonTicketServiceTest.findId("btnReload");
                    btnReload.click();

                    // 클릭하고 텀을 주고 이미지를 다운로드 (이렇게 안하면 새로고침 이전 이미지를 다운로드함)
                    Thread.sleep(500);
                    targetFileName = file.imageDownload(melonTicketServiceTest, folderPath);

                    System.out.println("targetFileName = " + targetFileName);
                    melonTicketServiceTest.captchaVerification(targetFileName);
                    displayed = errorMessage.isDisplayed();
            }
        } catch (IOException e) {
            System.out.println("e = " + e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        melonSeat.selectSeat(melonInfo);
    }



}
