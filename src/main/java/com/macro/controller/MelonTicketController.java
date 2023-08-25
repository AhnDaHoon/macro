package com.macro.controller;
import com.macro.config.CalendarTypeEnum;
import com.macro.config.LoginTypeEnum;
import com.macro.dto.MelonInfo;
import com.macro.file.MelonCaptchaService;
import com.macro.service.MelonSeatService;
import com.macro.service.MelonTicketService;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Controller
@RequestMapping("/melon")
public class MelonTicketController {

    @Value("${melon.folderPath}")
    private String folderPath;

    @Value("${melon.javascriptCode}")
    private String javascriptCode;

    @Autowired
    private MelonTicketService melonTicketService;

    @Autowired
    private MelonSeatService melonSeatService;

    @Autowired
    private MelonCaptchaService melonCaptchaService;

    @GetMapping("")
    public String melon(){
        return "view/melon/index";
    }

    @PostMapping("/login")
    @ResponseBody
    public String melonLogin(@ModelAttribute MelonInfo melonInfo){
        melonInfo.setLoginType(LoginTypeEnum.MELON);

        melonTicketService.melonLogin(melonInfo);

        melonTicketService.moveReservePage(melonInfo);
        return "로그인 성공";
    }

    @PostMapping("/ticketing")
    @ResponseBody
    public String melonTicketing(@ModelAttribute MelonInfo melonInfo){
        // 날짜 선택
        // 시간 선택
        // 예매하기 버튼 클릭
        melonTicketService.selectDateAndTimeClick(javascriptCode);

        Wait<WebDriver> wait = melonTicketService.getWaitDriver();
        // 좌석 선택 페이지 대기
        wait.until(ExpectedConditions.numberOfWindowsToBe(2));
        melonTicketService.windowHandler(2);
        System.out.println("새로운 페이지 감지");

        try {
            String targetFileName = melonCaptchaService.imageDownload(melonTicketService, folderPath);
            melonTicketService.captchaVerification(targetFileName);
            WebElement errorMessage = melonTicketService.findId("errorMessage");
            boolean displayed = errorMessage.isDisplayed();

            while (displayed){
                WebElement btnReload = melonTicketService.findId("btnReload");
                btnReload.click();

                // 클릭하고 텀을 주고 이미지를 다운로드 (이렇게 안하면 새로고침 이전 이미지를 다운로드함)
                Thread.sleep(500);
                targetFileName = melonCaptchaService.imageDownload(melonTicketService, folderPath);

                System.out.println("targetFileName = " + targetFileName);
                melonTicketService.captchaVerification(targetFileName);
                displayed = errorMessage.isDisplayed();
            }
        } catch (IOException e) {
            System.out.println("e = " + e);
        } catch (InterruptedException e) {
            System.out.println("e = " + e);
        }

        melonSeatService.selectSeat(melonInfo);

        return "티켓팅 성공";
    }
}
