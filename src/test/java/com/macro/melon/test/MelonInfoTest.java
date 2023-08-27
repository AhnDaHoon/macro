package com.macro.melon.test;

import com.macro.melon.config.CalendarTypeEnumTest;
import com.macro.melon.config.LoginTypeEnumTest;
import lombok.*;
import org.openqa.selenium.WebElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MelonInfoTest {
    private String id;

    private String pwd;

    private final String melonTicketUrl = "https://ticket.melon.com/csoon/index.htm#orderType=0&pageIndex=1&schGcode=GENRE_ALL&schText=&schDt=";

    private final String reserveTicketUrl = "https://ticket.melon.com/performance/index.htm?prodId=";

    private final String loginUrl = "https://member.melon.com/muid/family/ticket/login/web/login_inform.htm?cpId=WP15&returnPage=https://ticket.melon.com/main/readingGate.htm";


    // 잔여 좌석이 없을 때 사용하는 변수 milliSecond
    private final int refreshTime = 1500;

    // 티켓 id
    private String prodId;

    private LoginTypeEnumTest loginType;

    private CalendarTypeEnumTest calendarType;

    private MelonTicketServiceTest melonTicketService;

    // 티켓 시간 번호 맨위에 있는 시간이 0 그다음 1 +1씩 올라감
    private int ticketTime;

    // 티켓 날짜 예) 20230909
    private String ticketdate;

    // 티켓 날짜 태그, 예) dateSelect_, calendar_SelectId_
    private String tagId;

    private WebElement element;

    // 예매할 티켓 갯수
    private int rsrvVolume;
}
