package com.macro.melon.test;

import com.macro.melon.config.CalendarTypeEnumTest;
import com.macro.melon.config.LoginTypeEnumTest;
import com.macro.melon.test.file.MelonCaptchaTest;
import com.macro.melon.test.order.OrderServiceTest;
import com.macro.melon.test.order.OrderTest;
import com.macro.melon.test.seat.MelonSeatTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.ElementNotInteractableException;
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

    @Value("${melon.javascriptCode}")
    private String javascriptCode;

    MelonTicketServiceTest melonTicketServiceTest = new MelonTicketServiceTest();

    MelonCaptchaTest file = new MelonCaptchaTest();

    OrderServiceTest orderService;

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
        melonInfoTest.setProdId("208505");
        melonInfoTest.setTicketdate("20230826");
        melonInfoTest.setTicketTime(1);
    }

    void settingJavascript(){
        melonInfoTest.setCalendarType(CalendarTypeEnumTest.LIST);
        melonInfoTest.setProdId("208510");
        melonInfoTest.setTicketdate("0");
        melonInfoTest.setTicketTime(0);
    }


    @BeforeEach
    void setupTest() {
        driver = melonTicketServiceTest.getMainDriver();
        wait = melonTicketServiceTest.getWaitDriver();
        melonSeatTest = new MelonSeatTest(melonTicketServiceTest);
        orderService = new OrderServiceTest(melonTicketServiceTest);
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
    void 예매_사이트_이동(){
        settingListDate();
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);
    }

    /**
     * 팝업이 있는 페이지의 prodId가 필요함
     */
    @Test
    void 팝업이_있을_때_제거_클릭(){
        melonInfoTest.setProdId("208514");
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);
    }

    @Test
    void 예매하기_버튼_클릭(){
        settingListDate();

        melonTicketServiceTest.melonLogin(melonInfoTest);

        melonTicketServiceTest.moveReservePage(melonInfoTest);

        melonTicketServiceTest.selectDate(melonInfoTest);

        melonTicketServiceTest.selectTime(melonInfoTest);

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
        melonInfoTest.setRsrvVolume(3);
        melonTicketServiceTest.melonLogin(melonInfoTest);

        melonTicketServiceTest.moveReservePage(melonInfoTest);

        melonTicketServiceTest.selectDate(melonInfoTest);

        melonTicketServiceTest.selectTime(melonInfoTest);

        melonTicketServiceTest.findId("ticketReservation_Btn").click();

        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);

        System.out.println("새로운 페이지 감지");

        melonSeatTest.selectSeat(melonInfoTest);
    }

    @Test
    void 캡쳐_이미지_다운로드(){
        settingListDate();
        // 예매할 좌석 갯수
        melonInfoTest.setRsrvVolume(3);
        melonInfoTest.setProdId("208419");
        melonInfoTest.setTagId("calendar_SelectId_");
        melonInfoTest.setTicketdate("20230824");
        int rsrvVolume = melonInfoTest.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfoTest);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfoTest);

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
        melonInfoTest.setRsrvVolume(3);
        melonInfoTest.setProdId("208419");
        melonInfoTest.setTagId("calendar_SelectId_");
        melonInfoTest.setTicketdate("20230824");
        int rsrvVolume = melonInfoTest.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfoTest);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfoTest);

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
        melonInfoTest.setRsrvVolume(2);
        melonInfoTest.setProdId("208419");
        melonInfoTest.setTagId("calendar_SelectId_");
        melonInfoTest.setTicketdate("20230824");
        int rsrvVolume = melonInfoTest.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfoTest);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfoTest);

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
        settingCommonDate();
        // 예매할 좌석 갯수
        melonInfoTest.setRsrvVolume(2);
        int rsrvVolume = melonInfoTest.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);

        // 날짜 선택
        melonTicketServiceTest.selectDate(melonInfoTest);

        // 시간 선택
        melonTicketServiceTest.selectTime(melonInfoTest);

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
            System.out.println("e = " + e);
        } catch (ElementNotInteractableException e){
            System.out.println("e = " + e);
        }

        melonSeatTest.selectSeat(melonInfoTest);
    }

    @Test
    void 캡쳐_이미지_입력_및_좌석_선택2_javascript_조작_예약(){
        settingJavascript();
        // 예매할 좌석 갯수
        melonInfoTest.setRsrvVolume(2);
        int rsrvVolume = melonInfoTest.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);

        // 날짜 선택
        // 시간 선택
        // 예매하기 버튼 클릭
        melonTicketServiceTest.selectDateAndTimeClick(javascriptCode);

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
            System.out.println("e = " + e);
        } catch (ElementNotInteractableException e){
            System.out.println("e = " + e);
        }

        melonSeatTest.selectSeat(melonInfoTest);
    }

    @Test
    void 구역_선택이_나올_경우_구역_먼저_클릭_후_좌석_선택() {
        settingJavascript();
        // 예매할 좌석 갯수
        melonInfoTest.setRsrvVolume(1);
        int rsrvVolume = melonInfoTest.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);

        // 날짜 선택
        // 시간 선택
        // 예매하기 버튼 클릭
        melonTicketServiceTest.selectDateAndTimeClick(javascriptCode);

        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        try {
            String targetFileName = file.imageDownload(melonTicketServiceTest, folderPath);
            melonTicketServiceTest.captchaVerification(targetFileName);
            WebElement errorMessage = melonTicketServiceTest.findId("errorMessage");
            boolean displayed = errorMessage.isDisplayed();

            while (displayed) {
                // 캡쳐 문자 인식 새로고침 버튼
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
            System.out.println("e = " + e);
        } catch (ElementNotInteractableException e){
            System.out.println("e = " + e);
        }

        // 좌석 선택이 아닌 구역 선택이 나왔을 경우
        melonSeatTest.changeFrame();
        try {
            // 자리가 날 때까지 지역 선택, 새로고침을 반복하려고 만든 변수
            boolean selectArea = true;
            while (selectArea){
                selectArea = melonTicketServiceTest.isSelectAreaNone(melonInfoTest);
            }
        } catch (Exception e) {
            System.out.println("구역 선택 없음 " + e);
        }

        // 좌석 선택
        try {
            melonSeatTest.selectSeat(melonInfoTest);
        } catch (ElementNotInteractableException e){
            System.out.println("e = " + e);
        }
    }

    @Test
    void 좌석_선택_후_무통장입금_및_모든_결제_완료() {
        settingJavascript();
        // 예매할 좌석 갯수
        melonInfoTest.setRsrvVolume(1);
        int rsrvVolume = melonInfoTest.getRsrvVolume();

        // 로그인
        melonTicketServiceTest.melonLogin(melonInfoTest);

        // 페이지 이동
        melonTicketServiceTest.moveReservePage(melonInfoTest);

        // 날짜 선택
        // 시간 선택
        // 예매하기 버튼 클릭
        melonTicketServiceTest.selectDateAndTimeClick(javascriptCode);

        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketServiceTest.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        try {
            String targetFileName = file.imageDownload(melonTicketServiceTest, folderPath);
            melonTicketServiceTest.captchaVerification(targetFileName);
            WebElement errorMessage = melonTicketServiceTest.findId("errorMessage");
            boolean displayed = errorMessage.isDisplayed();

            while (displayed) {
                // 캡쳐 문자 인식 새로고침 버튼
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
            System.out.println("e = " + e);
        } catch (ElementNotInteractableException e){
            System.out.println("e = " + e);
        }

        // 좌석 선택이 아닌 구역 선택이 나왔을 경우
        melonSeatTest.changeFrame();
        try {
            // 자리가 날 때까지 지역 선택, 새로고침을 반복하려고 만든 변수
            boolean selectArea = true;
            while (selectArea){
                selectArea = melonTicketServiceTest.isSelectAreaNone(melonInfoTest);
            }
        } catch (Exception e) {
            System.out.println("구역 선택 없음 " + e);
        }

        // 좌석 선택
        try {
            melonSeatTest.selectSeat(melonInfoTest);
        } catch (ElementNotInteractableException e){
            System.out.println("e = " + e);
        }

        OrderTest order = new OrderTest();
        order.setMiddlePhoneNumber("1111");
        order.setLastPhoneNumber("2222");
        orderService.selectPrice(melonInfoTest);
        orderService.order(order);
    }
}
