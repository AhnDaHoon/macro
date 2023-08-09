package com.macro.melon;

import com.macro.melon.config.LoginTypeEnum;
import com.macro.melon.config.MelonConfig;
import org.openqa.selenium.*;
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
            case KAKAO -> driver.findElement(By.className("kakao")).click();
            case MELON -> driver.findElement(By.className("melon")).click();
        }

        int usePageNumber = melonInfo.getLoginType().getUsePageNumber();
        melonTicket.windowHandler(usePageNumber);
        return melonInfo;
    }

    public WebElement findClass(String name){
        WebElement result = null;
        while (true){
            try {
                result = driver.findElement(By.className(name));
                break;
            } catch (NoSuchElementException e){
                System.out.println("NoSuchElementException className = "+name);
            }
        }
        return result;
    }

    public void selectDate(MelonInfo info){
        WebElement element = info.getElement();
        String tagId = info.getTagId();
        String ticketdate = info.getTicketdate();

        while (true) {
            try {
                element = driver.findElement(By.id(tagId+ticketdate));
                break;
            } catch (NoSuchElementException e) {
                System.out.println("날짜 못찾음");
            }
        }
        element.click();
    }

    public void selectTime(MelonInfo info){
        // 날짜의 선택의 경우 여러 개여서 List로 받는다.
        MelonTicket melonTicket = info.getMelonTicket();
        int ticketTime = info.getTicketTime();

        List<WebElement> itemTimeList = melonTicket.findClassList("item_time");
        while (melonTicket.findClassList("item_time") == null || itemTimeList.size() == 0){
            itemTimeList = melonTicket.findClassList("item_time");
        }

        System.out.println("itemTimeList = " + itemTimeList.size());
        itemTimeList.get(ticketTime).click();

    }

    public List<WebElement> findClassList(String name){
        return driver.findElements(By.className(name));
    }

    public WebElement findId(String name){
        return driver.findElement(By.id(name));
    }

    public List<WebElement> findIdList(String name){
        return driver.findElements(By.id(name));
    }

    // 새로운 페이지가 감지될 때 까지 while문 돌리는 메서드, 메서드명 고민중...
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
}
