package com.macro.config;

import lombok.Getter;

@Getter
public enum CalendarTypeEnum {

    LIST(0, "type_list", "dateSelect_"),
    CALENDAR(1, "type_calendar",  "calendar_SelectId_");

    private int index;
    private String btnClassName;
    private String DateType;

    CalendarTypeEnum(int index, String btnClassName, String dateType) {
        this.index = index;
        this.btnClassName = btnClassName;
        this.DateType = dateType;
    }
}
