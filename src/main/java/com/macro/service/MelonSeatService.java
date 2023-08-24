package com.macro.service;


import com.macro.config.Triple;
import com.macro.dto.MelonInfo;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MelonSeatService {

    private final MelonTicketService melonTicketService;

    public void changeFrame(){
        WebElement oneStopFrame = melonTicketService.findFrame("oneStopFrame");
        melonTicketService.switchFrame(oneStopFrame);
    }

    public void selectSeat(MelonInfo melonInfo){
        changeFrame();

        int rsrvVolume = melonInfo.getRsrvVolume();
//        List<WebElement> rectElements = melonTicketServiceTest.findTagList("rect");

        String cssSelector = "rect:not([fill='#DDDDDD']):not([fill='none'])";
        List<WebElement> rectElements = melonTicketService.findMelonRect(cssSelector);

        List<Triple> coordinates = new ArrayList<>();
        List<WebElement> seatList = new ArrayList<>();

        // parallelStream: 병렬 처리로 순서를 보장하지 않는다.
        // parallelStream를 사용한 이유는 순서를 보장하지 않아도 되며 IO를 사용하지 않는 작업이고 로컬에서만 실행하기 때문에 사용자도 1명이여서 괜찮을 것 같아서 사용함
        rectElements.parallelStream().forEach(rect -> {
            coordinates.add(new Triple(Float.parseFloat(rect.getAttribute("y")),
                    Float.parseFloat(rect.getAttribute("x")),
                    rect));
        });

        Collections.sort(coordinates, Comparator.comparingDouble(Triple::getY));

        if(melonInfo.getRsrvVolume() == 1){
            seatList.add(coordinates.get(0).getRect());
        }else{
            float seatX = 0;
            for (Triple triple : coordinates) {
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

        melonTicketService.findId("nextTicketSelection").click();
    }
}
