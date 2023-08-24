package com.macro.service;


import com.macro.config.Triple;
import com.macro.dto.MelonInfo;
import lombok.RequiredArgsConstructor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
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

        List<WebElement> seatList = new ArrayList<>();

        // 이선자가 뜰 수도 있기 때문에 시작하는 index를 변수를 둠 그래야 나중에 또 자리를 필터링할 때 이전에 탐색 했던 자리를 탐색하지 않음.
        int startIndex = 0;

        // 좌석 찾기, sort
        List<Triple> coordinates = findSeat();

        // 좌석 선택
        startIndex = seatFilter(rsrvVolume, coordinates, seatList, startIndex);

        // 이선자
        while (true){
            int seatCode = clickAlert(melonTicketService);
            if(seatCode == 0){
                break;
            }

            // 기존 들어 있는 list clear
            seatList.clear();

            // 기존 엘리먼트들을 재사용하면 에러가 발생함 그래서 또 새로 받아야함.
            coordinates.clear();

            // 좌석 찾기, sort
            coordinates = findSeat();

            // 좌석 선택
            startIndex = seatFilter(rsrvVolume, coordinates, seatList, startIndex);
        }

    }

    public List<Triple> findSeat(){
        String cssSelector = "rect:not([fill='#DDDDDD']):not([fill='none'])";
        List<WebElement> rectElements = melonTicketService.findMelonRect(cssSelector);

        List<Triple> coordinates = new ArrayList<>();

        rectElements.parallelStream().forEach(rect -> {
            coordinates.add(new Triple(Float.parseFloat(rect.getAttribute("y")),
                    Float.parseFloat(rect.getAttribute("x")),
                    rect));
        });

        sortCoordinates(coordinates);
        return coordinates;
    }

    public void sortCoordinates(List<Triple> coordinates){
        Collections.sort(coordinates, new Comparator<Triple>() {
            @Override
            public int compare(Triple t1, Triple t2) {
                int yComparison = Float.compare(t1.getY(), t2.getY());

                if (yComparison != 0) {
                    return yComparison;
                } else {
                    return Float.compare(t1.getX(), t2.getX());
                }
            }
        });
    }

    public int seatFilter(int rsrvVolume, List<Triple> coordinates, List<WebElement> seatList, int startIndex){
        if(rsrvVolume == 1){
            seatList.add(coordinates.get(0).getRect());
        }else{
            float preSeatX = 0;
            float preSeatY = 0;
            for (int i = startIndex; i < coordinates.size(); i++) {
                if(seatList.size() > 0){
                    if(coordinates.get(i).getX() - preSeatX == Math.abs(13)){
                        if(preSeatY == coordinates.get(i).getY()){
                            seatList.add(coordinates.get(i).getRect());
                        }
                    }else {
                        seatList.remove(0);
                    }
                }else{
                    seatList.add(coordinates.get(i).getRect());
                }

                preSeatX = coordinates.get(i).getX();
                preSeatY = coordinates.get(i).getY();
                startIndex++;

                if(seatList.size() >= rsrvVolume){
                    break;
                }
            }
        }

        for (WebElement webElement : seatList) {
            webElement.click();
        }

        melonTicketService.findId("nextTicketSelection").click();
        return startIndex;
    }

    // 이선자 클릭
    public int clickAlert(MelonTicketService melonTicketService){
        WebDriver driver = melonTicketService.getMainDriver();
        try {
            Alert alert = driver.switchTo().alert();
            alert.accept();
        } catch (NoAlertPresentException e){
            System.out.println("이선자 알림 없음.");
            return 0;
        }
        return 1;
    }
}
