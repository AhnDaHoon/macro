package com.macro.melon.test.order;

import com.macro.melon.test.MelonInfoTest;
import com.macro.melon.test.MelonTicketServiceTest;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderServiceTest {

    private final MelonTicketServiceTest melonTicketServiceTest;

    public void selectPrice(MelonInfoTest melonInfo){
        // 수량 선택
        // .base_pr 클래스와 형제 태그인 .price_wrap_sel 클래스를 가지고 있는 select 태그의 value가 1인 요소를 선택하기 위한 CSS Selector
        String cssSelector = ".base_pr ~ .wrap_sel select";
        WebElement selectSeatGradeElement = melonTicketServiceTest.findCssSelector(cssSelector);
        Select selectSeatGrade = new Select(selectSeatGradeElement);
        selectSeatGrade.selectByValue(String.valueOf(melonInfo.getRsrvVolume()));

        // 다음버튼 클릭
        WebElement nextPayment = melonTicketServiceTest.findId("nextPayment");
        nextPayment.click();
    }

    public void order(OrderTest order){

        WebElement passbook = melonTicketServiceTest.findCssSelector("[title='무통장입금']");
        if(passbook.isEnabled()){
            passbook.click();
        }

        // 은행 선택
        WebElement bankCodeElement = melonTicketServiceTest.findName("bankCode");
        Select selectBank = new Select(bankCodeElement);
        selectBank.selectByValue("88"); // 신한은행

        // 무통장 입금 소득공제 번호 입력
        WebElement cashReceiptRegTelNo2 = melonTicketServiceTest.findId("cashReceiptRegTelNo2");
        cashReceiptRegTelNo2.sendKeys(order.getMiddlePhoneNumber());
        WebElement cashReceiptRegTelNo3 = melonTicketServiceTest.findId("cashReceiptRegTelNo3");
        cashReceiptRegTelNo3.sendKeys(order.getLastPhoneNumber());

        // 필수 약관
        WebElement chkAgreeAll = melonTicketServiceTest.findName("chkAgreeAll");
        chkAgreeAll.click();

        WebElement btnFinalPayment = melonTicketServiceTest.findId("btnFinalPayment");
        btnFinalPayment.click();
    }
}
