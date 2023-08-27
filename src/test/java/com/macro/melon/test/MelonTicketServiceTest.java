package com.macro.melon.test;

import com.macro.dto.MelonInfo;
import com.macro.melon.config.CalendarTypeEnumTest;
import com.macro.melon.config.MelonConfigTest;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Service
public class MelonTicketServiceTest {

    private AnnotationConfigApplicationContext ct = new AnnotationConfigApplicationContext(MelonConfigTest.class);

    private WebDriver driver = ct.getBean("getTestMainDriver", WebDriver.class);
    private WebDriverWait wait = ct.getBean("getTestWaitDriver", WebDriverWait.class);
    private Tesseract tesseract = ct.getBean("getTestTesseract", Tesseract.class);

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

    private void moveMelonLoginPage(MelonInfoTest melonInfoTest){
        driver.get(melonInfoTest.getLoginUrl());

        WebElement kakaoBtn = findClass(melonInfoTest.getLoginType().getLoginType());
        kakaoBtn.click();

        int usePageNumber = melonInfoTest.getLoginType().getUsePageNumber();
        windowHandler(usePageNumber);
    }

    public void melonLogin(MelonInfoTest melonInfo){
        moveMelonLoginPage(melonInfo);

        WebElement id = findId("id");
        WebElement pwd = findId("pwd");
        id.sendKeys(melonInfo.getId());
        pwd.sendKeys(melonInfo.getPwd());
        WebElement btnLogin = findId("btnLogin");
        btnLogin.click();


    }

    public void moveReservePage(MelonInfoTest melonInfo){
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

    public void selectDate(MelonInfoTest melonInfo){
        String tagId = melonInfo.getCalendarType().getDateType();
        String ticketdate = melonInfo.getTicketdate();

        changeCalendarType(melonInfo.getCalendarType());

        WebElement dateElement = findId(tagId + ticketdate);
        dateElement.click();
    }

    public void changeCalendarType(CalendarTypeEnumTest calendarTypeEnumTest){
        WebElement dateTypeBtn = findClass(calendarTypeEnumTest.getBtnClassName());
        dateTypeBtn.click();
    }

    public void selectTime(MelonInfoTest info){
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

    public WebElement findName(String name){
        By by = By.name(name);
        waitElement(by);
        return driver.findElement(by);
    }

    public List<WebElement> findNameList(String name){
        By by = By.name(name);
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

    public boolean isSelectAreaNone(MelonInfoTest melonInfo){
        // 만든 함수를 사용하지 않고 driver를 가져와서 사용하는 이유는 melonTicketService에서 만든 함수는 무한 대기를 해서 box_stage 클래스를 가진 태그가 나오지 않으면 계속 기다리기 때문.
        // 위에 캡쳐 문자 인식이 끝나면 당연히 좌석 선택이나, 지역 선택이 있을거라고 예상하고 짠 코드임
        WebElement boxStage = driver.findElement(By.className("box_stage"));
        System.out.println("boxStage = " + boxStage);

        // 오른쪽 하단에 좌석 onclick='goSummary' 속성을 가진 tr 태그들을 선택을해 줘야 좌석이 elements에서 조회가 되기 때문에 전부 다 클릭해 준다.
        List<WebElement> goSummaryList = findCssSelectorList("[onclick*='goSummary']");
        List<WebElement> areaInfo = findClassList("area_info");

        for (int i = 0; i < goSummaryList.size(); i++) {
            String areaInfoClass = areaInfo.get(i).getAttribute("class");

            // 지역 보는 리스트가 open이 되어 있지 않으면 클릭함.
            // open이 되어 있는걸 또 클릭하면 닫기 때문에 추가함
            if(!areaInfoClass.contains("open")){
                goSummaryList.get(i).click();
            }
        }

        // 좌석을 다 노출한 후에 좌석을 선택한다. 잔여 좌석 갯수를 보고 사용자가 신청한 좌석이랑 같거나 크면 구역을 선택한다.
        List<WebElement> listAreaUlLi = findCssSelectorList(".list_area ul li");
        int seatResidual = 0;
        for (WebElement li : listAreaUlLi) {
            WebElement stringTag = findCssSelector("strong");
            seatResidual = Integer.parseInt(stringTag.getText());
            System.out.println("seatResidual = " + seatResidual);
            if(seatResidual >= melonInfo.getRsrvVolume()){
                li.click();
                break;
            }
        }

        if(seatResidual == 0 || seatResidual < melonInfo.getRsrvVolume()){
            WebElement btnReloadSchedule = findId("btnReloadSchedule");
            btnReloadSchedule.click();
            try {
                Thread.sleep(melonInfo.getRefreshTime());
            } catch (InterruptedException e) {
                System.out.println("e = " + e);
            }
            return true;
        }
        return false;
    }

}
