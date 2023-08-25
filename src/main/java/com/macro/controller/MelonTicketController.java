package com.macro.controller;
import com.macro.config.CalendarTypeEnum;
import com.macro.config.LoginTypeEnum;
import com.macro.dto.MelonInfo;
import com.macro.file.MelonCaptchaService;
import com.macro.service.MelonSeatService;
import com.macro.service.MelonTicketService;
import org.openqa.selenium.By;
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
import java.util.List;

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
        }

        // 좌석 선택이 아닌 구역 선택이 나왔을 경우
        melonSeatService.changeFrame();
        try {
            // 만든 함수를 사용하지 않고 driver를 가져와서 사용하는 이유는 melonTicketService에서 만든 함수는 무한 대기를 해서 box_stage 클래스를 가진 태그가 나오지 않으면 계속 기다리기 때문.
            // 위에 캡쳐 문자 인식이 끝나면 당연히 좌석 선택이나, 지역 선택이 있을거라고 예상하고 짠 코드임

            WebElement boxStage = driver.findElement(By.className("box_stage"));
            System.out.println("boxStage = " + boxStage);

            // 오른쪽 하단에 좌석 onclick='goSummary' 속성을 가진 tr 태그들을 선택을해 줘야 좌석이 elements에서 조회가 되기 때문에 전부 다 클릭해 준다.
            List<WebElement> goSummaryList = melonTicketService.findCssSelectorList("[onclick*='goSummary']");
            for (WebElement goSummary : goSummaryList) {
                goSummary.click();
            }

            // 좌석을 다 노출한 후에 좌석을 선택한다. 잔여 좌석 갯수를 보고 사용자가 신청한 좌석이랑 같거나 크면 구역을 선택한다.
            List<WebElement> listAreaUlLi = melonTicketService.findCssSelectorList(".list_area ul li");
            for (WebElement li : listAreaUlLi) {
                WebElement stringTag = melonTicketService.findCssSelector("strong");
                int seatResidual = Integer.parseInt(stringTag.getText());
                System.out.println("seatResidual = " + seatResidual);
                if(seatResidual >= melonInfo.getRsrvVolume()){
                    li.click();
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("구역 선택 없음 " + e);
        }

        melonSeatService.selectSeat(melonInfo);

        return "티켓팅 끝";
    }
}
