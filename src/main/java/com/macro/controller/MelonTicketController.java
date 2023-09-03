package com.macro.controller;
import com.macro.config.LoginTypeEnum;
import com.macro.dto.MelonInfo;
import com.macro.dto.Order;
import com.macro.service.MelonCaptchaService;
import com.macro.service.MelonSeatService;
import com.macro.service.MelonTicketService;
import com.macro.service.OrderService;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private OrderService orderService;

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
    public String melonTicketing(@ModelAttribute MelonInfo melonInfo, @ModelAttribute Order order){
        // 날짜 선택
        // 시간 선택
        // 예매하기 버튼 클릭
        melonTicketService.selectDateAndTimeClick(javascriptCode);

        Wait<WebDriver> wait = melonTicketService.getWaitDriver();
        WebDriver driver = melonTicketService.getMainDriver();

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
                // 캡쳐 문자 인식 새로고침 버튼
                WebElement btnReload = melonTicketService.findId("btnReload");
                btnReload.click();

                // 클릭하고 텀을 주고 이미지를 다운로드 (이렇게 안하면 새로고침 이전 이미지를 다운로드함)
                Thread.sleep(400);
                targetFileName = melonCaptchaService.imageDownload(melonTicketService, folderPath);

                System.out.println("targetFileName = " + targetFileName);
                melonTicketService.captchaVerification(targetFileName);
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
        melonSeatService.changeFrame();
        try {
            // 자리가 날 때까지 지역 선택, 새로고침을 반복하려고 만든 변수
            boolean selectArea = true;
            while (selectArea){
                selectArea = melonTicketService.isSelectAreaNone(melonInfo);
            }
        } catch (Exception e) {
            // 구역 선택이 없으면 예외가 발생함 그럼 바로 좌석 선택으로 넘어가면 됨
            System.out.println("구역 선택 없음 " + e);
        }

        try {
            melonSeatService.selectSeat(melonInfo);
        } catch (ElementNotInteractableException e){
            System.out.println("e = " + e);
        }

        orderService.selectPrice(melonInfo);
        orderService.order(order);

        return "티켓팅 끝";
    }
}
