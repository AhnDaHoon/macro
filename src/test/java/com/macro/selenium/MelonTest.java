package com.macro.selenium;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.manager.SeleniumManager;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

/**
 * 멜론 티켓 연결 테스트
 */
@SpringBootTest
public class MelonTest {

    String parentWindowHandle;

    Set<String> newWindowHandles;

    WebDriver driver;

    WebElement element;

    Wait<WebDriver> wait;

    @BeforeAll
    static void setupClass() {
    }

    @BeforeEach
    void setupTest() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-popup-blocking");
        driver = new ChromeDriver(options);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));


    }

    @AfterEach
    void teardown() {
        driver.quit();
    }

    @Test
    void 멜론_티켓_URL_연결() {
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .url("https://ticket.melon.com/main/index.htm")
                .build();

        driver.get(melonInfo.getUrl());
        String title = driver.getTitle();

        // Verify
        assertThat(title).contains("멜론 티켓");
    }

    @Test
    void 멜론_티켓_카카오_계정_로그인_버튼_클릭() throws InterruptedException {
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .url("https://member.melon.com/muid/family/ticket/login/web/login_inform.htm?cpId=WP15&returnPage=https://ticket.melon.com/main/readingGate.htm")
                .loginType(LoginTypeEnum.KAKAO)
                .build();
        driver.get(melonInfo.getUrl());
        String title = driver.getTitle();
        assertThat(title).contains("Melon::음악이 필요한 순간, 멜론");
        parentWindowHandle = driver.getWindowHandle();

        element = driver.findElement(By.className("kakao"));
        element.click();
        int usePageNumber = melonInfo.getLoginType().getUsePageNumber();
        windowHandler(usePageNumber); // 카카오 계정 로그인 버튼을 누르면 새탭을 생성하기 때문에 2번째 탭으로 전환

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://accounts.kakao.com/login");
    }
    @Test
    void 멜론_티켓_멜론_아이디_로그인_버튼_클릭(){
        MelonInfo melonInfo = new MelonInfo.MelonInfoBuilder()
                .url("https://member.melon.com/muid/family/ticket/login/web/login_inform.htm?cpId=WP15&returnPage=https://ticket.melon.com/main/readingGate.htm")
                .loginType(LoginTypeEnum.MELON)
                .build();
        driver.get(melonInfo.getUrl());
        String title = driver.getTitle();
        assertThat(title).contains("Melon::음악이 필요한 순간, 멜론");
        parentWindowHandle = driver.getWindowHandle();

        element = driver.findElement(By.className("melon"));
        element.click();
        int usePageNumber = melonInfo.getLoginType().getUsePageNumber();
        windowHandler(usePageNumber);

        String newUrl = driver.getCurrentUrl();
        assertThat(newUrl).contains("https://member.melon.com/muid/family/ticket/login/web/login_informM.htm");

    }

    @Test
    void 멜론_티켓_로그인(){}

    void windowHandler(int usePageNumber){
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

}


//driver.navigate().to("https://selenium.dev");
