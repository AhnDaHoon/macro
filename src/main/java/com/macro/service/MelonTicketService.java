package com.macro.service;

import com.macro.config.CalendarTypeEnum;
import com.macro.config.MelonConfig;
import com.macro.dto.MelonInfo;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Service
public class MelonTicketService {

    private AnnotationConfigApplicationContext ct = new AnnotationConfigApplicationContext(MelonConfig.class);

    private WebDriver driver = ct.getBean("getMainDriver", WebDriver.class);
    private WebDriverWait wait = ct.getBean("getWaitDriver", WebDriverWait.class);
    private Tesseract tesseract = ct.getBean("getTesseract", Tesseract.class);

    private Set<String> newWindowHandles;

    private String parentWindowHandle;

    public WebDriver getMainDriver(){
        return driver;
    }

    public Wait<WebDriver> getWaitDriver(){
        return wait;
    }
    public Tesseract getTesseract(){
        return tesseract;
    }

    public void windowHandler(int usePageNumber){
        parentWindowHandle = driver.getWindowHandle();

        wait.until(numberOfWindowsToBe(usePageNumber));
        newWindowHandles = driver.getWindowHandles();

        // 새로운 창으로 전환
        for (String nwh : newWindowHandles) {
            if (!nwh.equals(parentWindowHandle)) {
                driver.switchTo().window(nwh);
                break;
            }
        }
    }

    private void moveMelonLoginPage(MelonInfo melonInfo){
        driver.get(melonInfo.getLoginUrl());

        switch (melonInfo.getLoginType()){
            case KAKAO -> {
                WebElement kakaoBtn = findClass("kakao");
                kakaoBtn.click();
            }
            case MELON -> {
                WebElement melonBtn = findClass("melon");
                melonBtn.click();
            }
        }

        int usePageNumber = melonInfo.getLoginType().getUsePageNumber();
        windowHandler(usePageNumber);
    }

    public void melonLogin(MelonInfo melonInfo){
        moveMelonLoginPage(melonInfo);

        WebElement id = findId("id");
        WebElement pwd = findId("pwd");
        id.sendKeys(melonInfo.getId());
        pwd.sendKeys(melonInfo.getPwd());
        WebElement btnLogin = findId("btnLogin");
        btnLogin.click();


    }

    public void moveReservePage(MelonInfo melonInfo){
        String reserveTicketUrl = melonInfo.getReserveTicketUrl();
        String prodId = melonInfo.getProdId();

        driver.navigate().to(reserveTicketUrl+prodId);

        // 팝업 제거
        try {
            // 팝업은 안뜰 수도 있어서 대기안함
            driver.findElement(By.id("noticeAlert_layerpopup_cookie")).click();
        }catch (NoSuchElementException e){
        }

    }
    public void selectDate(MelonInfo melonInfo){
        String tagId = melonInfo.getCalendarType().getDateType();
        String ticketdate = melonInfo.getTicketDate();

        changeCalendarType(melonInfo.getCalendarType());

        WebElement dateElement = findId(tagId + ticketdate);
        dateElement.click();
    }

    public void changeCalendarType(CalendarTypeEnum calendarTypeEnumTest){
        WebElement dateTypeBtn = findClass(calendarTypeEnumTest.getBtnClassName());
        dateTypeBtn.click();
    }

    public void selectTime(MelonInfo info){
        // 날짜의 선택의 경우 여러 개여서 List로 받는다.
        int ticketTime = info.getTicketTime();

        List<WebElement> itemTimeList = findClassList("item_time");
        itemTimeList.get(ticketTime).click();

    }

    public void selectDateAndTimeClick(String javascriptCode){
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;

        // 자바스크립트 코드 실행
        String jsCode = javascriptCode;
        jsExecutor.executeScript(jsCode);
    }

    public WebElement findClass(String name){
        By by = By.className(name);
        waitElement(by);
        return driver.findElement(by);
    }

    // to be updated
//    public WebElement findClass(String name){
//        By by = By.className(name);
//        boolean isSuccess = waitElement(by);
//        if(isSuccess){
//            return driver.findElement(by);
//        }
//        return null;
//    }

    public List<WebElement> findClassList(String name){
        By by = By.className(name);
        waitElement(by);
        return driver.findElements(by);
    }

    public WebElement findId(String name){
        By by = By.id(name);
        waitElement(by);
        return driver.findElement(by);
    }

    public List<WebElement> findIdList(String name){
        By by = By.id(name);
        waitElement(by);
        return driver.findElements(by);
    }

    public List<WebElement> findTagList(String name){
        By by = By.tagName(name);
        waitElement(by);
        return driver.findElements(by);
    }

    public WebElement findCssSelector(String name){
        By by = By.cssSelector(name);
        waitElement(by);
        return driver.findElement(by);
    }

    public List<WebElement> findCssSelectorList(String name){
        By by = By.cssSelector(name);
        waitElement(by);
        return driver.findElements(by);
    }

    public WebElement findFrame(String name){
        By by = By.id(name);
        waitElement(by);
        return driver.findElement(by);
    }

    public void waitElement(By by){
        try {
            // 조건 충족까지 대기하고 요소를 찾음
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("요소가 나타나지 않거나 대기 중에 오류가 발생했습니다.");
        }
    }

    // to be updated
//    public boolean waitElement(By by){
//        try {
//            // 조건 충족까지 대기하고 요소를 찾음
//            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
//        } catch (Exception e) {
//            System.out.println("요소가 나타나지 않거나 대기 중에 오류가 발생했습니다.");
//            return false;
//        }
//        return true;
//    }


    public void switchFrame(WebElement iframe){
        driver.switchTo().frame(iframe);
    }


    /**
     *
     * @param targetFileName
     * @return result.substring(0, 6) 멜론 티켓은 문자가 6글자임, 문자를 변환하면 뒤에 이상한 문자가 붙어서 나오기 때문에 6글자만 가져온다.
     */
    public String convertImageToString(String targetFileName){
        String result = null;

        File file = new File(targetFileName);

        if(file.exists() && file.canRead()) {
            try {
                result = tesseract.doOCR(file);
            } catch (TesseractException e) {
                result = "TesseractException";
            }
        } else {
            result = "not exist";
        }
        if(result.equals("TesseractException") || result.equals("not exist")){
            return result;
        }

        if(result.length() > 6){
            return result.substring(0, 6);
        }

        return result;

    }

    public void captchaVerification(String targetFileName){
        // 문자열로 변환된 값
        String convertImageToString = convertImageToString(targetFileName);

        // 캡쳐 입력창
        WebElement captchaInput = findId("label-for-captcha");
        captchaInput.clear();
        captchaInput.sendKeys(convertImageToString);

        // 캡쳐 화면 확인 버튼
        WebElement btnComplete = findId("btnComplete");
        btnComplete.click();
    }
}