package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnum;
import com.macro.melon.config.MelonConfig;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Component
public class MelonTicket {

    private AnnotationConfigApplicationContext ct = new AnnotationConfigApplicationContext(MelonConfig.class);

    private WebDriver driver = ct.getBean("getMainDriver", WebDriver.class);
    private WebDriverWait wait = ct.getBean("getWaitDriver", WebDriverWait.class);

    private Set<String> newWindowHandles;

    private String parentWindowHandle;

    public WebDriver getMainDriver(){
        return driver;
    }

    public Wait<WebDriver> getWaitDriver(){
        return wait;
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

    public MelonInfo moveMelonLoginPage(LoginTypeEnum loginTypeEnum, MelonTicket melonTicket){
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .url("https://member.melon.com/muid/family/ticket/login/web/login_inform.htm?cpId=WP15&returnPage=https://ticket.melon.com/main/readingGate.htm")
                .loginType(loginTypeEnum)
                .build();
        driver.get(melonInfo.getUrl());

        switch (loginTypeEnum){
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
        melonTicket.windowHandler(usePageNumber);
        return melonInfo;
    }

    public void selectDate(MelonInfo info){
        String tagId = info.getTagId();
        String ticketdate = info.getTicketdate();

        WebElement dateElement = findId(tagId + ticketdate);
        dateElement.click();
    }

    public void selectTime(MelonInfo info){
        // 날짜의 선택의 경우 여러 개여서 List로 받는다.
        MelonTicket melonTicket = info.getMelonTicket();
        int ticketTime = info.getTicketTime();

        List<WebElement> itemTimeList = melonTicket.findClassList("item_time");
        itemTimeList.get(ticketTime).click();

    }

    public WebElement findClass(String name){
        By by = By.className(name);
        waitElement(by);
        return driver.findElement(by);
    }

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

    // 새로운 페이지가 감지될 때까지 while 문 돌리는 메서드, 메서드명 고민 중...
    public void newPage (){
        boolean isNotFound = true;
        while (isNotFound){
            if(driver.getWindowHandles().size() > 1){
                Set<String> elementSet = driver.getWindowHandles();
                isNotFound = false;
            }
            System.out.println("not found");
        }
    }

    public void waitElement(By by){
        try {
            // 조건 충족까지 대기하고 요소를 찾음
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
        } catch (Exception e) {
            System.out.println("요소가 나타나지 않거나 대기 중에 오류가 발생했습니다.");
        }
    }

}
