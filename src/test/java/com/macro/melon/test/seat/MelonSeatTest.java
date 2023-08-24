package com.macro.melon.test.seat;

import com.macro.melon.config.TripleTest;
import com.macro.melon.test.MelonInfoTest;
import com.macro.melon.test.MelonTicketServiceTest;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MelonSeatTest {

    private final MelonTicketServiceTest melonTicketServiceTest;

    public void changeFrame(){
        WebElement oneStopFrame = melonTicketServiceTest.findFrame("oneStopFrame");
        melonTicketServiceTest.switchFrame(oneStopFrame);
    }

    public void selectSeat(MelonInfoTest melonInfoTest){
        changeFrame();

        int rsrvVolume = melonInfoTest.getRsrvVolume();
//        List<WebElement> rectElements = melonTicketServiceTest.findTagList("rect");

        String cssSelector = "rect:not([fill='#DDDDDD']):not([fill='none'])";
        List<WebElement> rectElements = melonTicketServiceTest.findMelonRect(cssSelector);

        List<TripleTest> coordinates = new ArrayList<>();
        List<WebElement> seatList = new ArrayList<>();

        rectElements.parallelStream().forEach(rect -> {
            coordinates.add(new TripleTest(Float.parseFloat(rect.getAttribute("y")),
                    Float.parseFloat(rect.getAttribute("x")),
                    rect));
        });

        Collections.sort(coordinates, Comparator.comparingDouble(TripleTest::getY));

        if(melonInfoTest.getRsrvVolume() == 1){
            seatList.add(coordinates.get(0).getRect());
        }else{
            float seatX = 0;
            for (TripleTest triple : coordinates) {
                if(seatList.size() > 0){
                    if(Math.floor(triple.getX()) - Math.floor(seatX) == 13){
                        seatList.add(triple.getRect());
                    }else {
                        seatList.clear();
                    }
                }else{
                    seatList.add(triple.getRect());
                }

                if(seatList.size() >= rsrvVolume){
                    break;
                }
                seatX = triple.getX();
            }
        }

        for (WebElement webElement : seatList) {
            webElement.click();
        }
        System.out.println("태그 좌석 클리함");

        melonTicketServiceTest.findId("nextTicketSelection").click();
    }

    // 이선자 클릭
    public void clickAlert(MelonTicketServiceTest melonTicketServiceTest){
        WebDriver driver = melonTicketServiceTest.getMainDriver();

        Alert alert = driver.switchTo().alert();
        alert.accept();
    }
}
