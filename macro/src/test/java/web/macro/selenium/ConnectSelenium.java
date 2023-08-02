package web.macro.selenium;

import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;


@RequiredArgsConstructor
public class ConnectSelenium {
    private final SeleniumProperties seleniumProperties;

    private WebDriver webDriver;
    private WebElement webElement;
    private String url = "https://www.google.com/";



    public void start(){
        System.setProperty(seleniumProperties.getWebDriverId(), seleniumProperties.getWebDriverPath());
        webDriver = new ChromeDriver();
        try {
            getDataList();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void getDataList() throws InterruptedException {
        System.out.println("실행");
        webDriver.get(url);    //브라우저에서 url로 이동한다.
        Thread.sleep(1000); //브라우저 로딩될때까지 잠시 기다린다.

    }

}
