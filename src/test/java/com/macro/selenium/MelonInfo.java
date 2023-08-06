package com.macro.selenium;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@Builder
public class MelonInfo {
    private String id;

    private String pwd;

    private String url;

    private LoginTypeEnum loginType;
}
