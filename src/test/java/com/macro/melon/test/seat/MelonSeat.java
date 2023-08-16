package com.macro.melon.test.seat;

import com.macro.melon.config.Triple;
import com.macro.melon.test.MelonInfo;
import com.macro.melon.test.MelonTicket;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MelonSeat {

    private final MelonTicket melonTicket;

    public void changeFrame(){
        WebElement oneStopFrame = melonTicket.findFrame("oneStopFrame");
        melonTicket.switchFrame(oneStopFrame);
    }

    public void selectSeat(MelonInfo melonInfo){
        changeFrame();

        int rsrvVolume = melonInfo.getRsrvVolume();
        List<WebElement> rectElements = melonTicket.findTagList("rect");

        List<Triple> coordinates = new ArrayList<>();
        List<WebElement> seatList = new ArrayList<>();

        for (WebElement rect : rectElements) {
            if(rect.getAttribute("fill").equals("#DDDDDD") && !rect.getAttribute("fill").equals("none")){
                continue;
            }
            coordinates.add(new Triple(Float.parseFloat(rect.getAttribute("y")),
                    Float.parseFloat(rect.getAttribute("x")),
                    rect));

//            if(coordinates.size() >= 100) break;
        }

        Collections.sort(coordinates, Comparator.comparingDouble(Triple::getY));

        float seatX = 0;
        for (Triple triple : coordinates) {
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

        melonTicket.findId("nextTicketSelection").click();
    }
}