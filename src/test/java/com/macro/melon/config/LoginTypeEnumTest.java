package com.macro.melon.config;


import lombok.Getter;

@Getter
public enum LoginTypeEnumTest {
    MELON(1, "melon"),
    KAKAO(2, "kakao");

    private int usePageNumber;
    private String loginType;

    LoginTypeEnumTest(int usePageNumber, String loginType) {
        this.usePageNumber = usePageNumber;
        this.loginType = loginType;
    }
}
