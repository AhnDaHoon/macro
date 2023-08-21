package com.macro.melon.test.seat;

import com.macro.melon.config.TripleTest;
import com.macro.melon.test.MelonInfoTest;
import com.macro.melon.test.MelonTicketServiceTest;
import lombok.RequiredArgsConstructor;
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
        List<WebElement> rectElements = melonTicketServiceTest.findTagList("rect");

        List<TripleTest> coordinates = new ArrayList<>();
        List<WebElement> seatList = new ArrayList<>();

        for (WebElement rect : rectElements) {
            if(rect.getAttribute("fill").equals("#DDDDDD") && !rect.getAttribute("fill").equals("none")){
                continue;
            }
            coordinates.add(new TripleTest((int) Math.floor(Float.parseFloat(rect.getAttribute("y"))),
                    (int) Math.floor(Float.parseFloat(rect.getAttribute("x"))),
                    rect));

//            if(coordinates.size() >= 100) break;
        }

        Collections.sort(coordinates, Comparator.comparingDouble(TripleTest::getY));

        float seatX = 0;
        for (TripleTest triple : coordinates) {
            if(seatList.size() > 0){
                if(triple.getX() - seatX == 13){
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

        for (WebElement webElement : seatList) {
            webElement.click();
        }

        melonTicketServiceTest.findId("nextTicketSelection").click();
    }
}
