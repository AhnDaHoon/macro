package com.macro.selenium;

import com.macro.selenium.config.LoginTypeEnum;
import lombok.*;

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

}
