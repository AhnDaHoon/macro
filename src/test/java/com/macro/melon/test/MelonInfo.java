package com.macro.melon.test;

import com.macro.melon.config.LoginTypeEnum;
import lombok.*;
import org.openqa.selenium.WebElement;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class MelonInfo {
    private String id;

    private String pwd;

    private final String melonTicketUrl = "https://ticket.melon.com/csoon/index.htm#orderType=0&pageIndex=1&schGcode=GENRE_ALL&schText=&schDt=";

    private final String reserveTicketUrl = "https://ticket.melon.com/performance/index.htm?prodId=";

    private final String loginUrl = "https://member.melon.com/muid/family/ticket/login/web/login_inform.htm?cpId=WP15&returnPage=https://ticket.melon.com/main/readingGate.htm";

    private String prodId;

    private LoginTypeEnum loginType;

    private MelonTicket melonTicket;

    private int ticketTime;

    private String ticketdate;

    private String tagId;

    private WebElement element;

}
