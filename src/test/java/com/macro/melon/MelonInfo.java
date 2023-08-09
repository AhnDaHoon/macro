package com.macro.melon;

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

    private String url;

    private LoginTypeEnum loginType;

    private MelonTicket melonTicket;

    private int ticketTime;

    private String ticketdate;

    private String tagId;

    private WebElement element;

}
