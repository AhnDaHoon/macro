package com.macro.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Set;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

@Component
public class MelonTiket {

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

    /**
     * 파라미터가 여러개 있으면 결합도가 높음 수정이 필요함
     * @param loginTypeEnum
     * @param element
     * @param melonTiket
     * @return
     */
    MelonInfo moveMelonLoginForm(LoginTypeEnum loginTypeEnum, WebElement element, MelonTiket melonTiket){
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .url("https://member.melon.com/muid/family/ticket/login/web/login_inform.htm?cpId=WP15&returnPage=https://ticket.melon.com/main/readingGate.htm")
                .loginType(loginTypeEnum)
                .build();
        driver.get(melonInfo.getUrl());

        switch (loginTypeEnum){
            case KAKAO -> element = driver.findElement(By.className("kakao"));
            case MELON -> element = driver.findElement(By.className("melon"));
        }

        element.click();
        int usePageNumber = melonInfo.getLoginType().getUsePageNumber();
        melonTiket.windowHandler(usePageNumber);
        return melonInfo;
    }

}
