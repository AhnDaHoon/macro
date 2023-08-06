package com.macro.selenium;


import lombok.Getter;

@Getter
public enum LoginTypeEnum {
    MELON(1, "melon"),
    KAKAO(2, "kakao");

    private int usePageNumber;
    private String loginType;

    LoginTypeEnum(int usePageNumber, String loginType) {
        this.usePageNumber = usePageNumber;
        this.loginType = loginType;
    }
}
