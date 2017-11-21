package com.ducetech.app.model.vo;

import lombok.Data;

@Data
public class UserScheduleInfo {
    private String userId;
    private String startAt;
    //计划年工时
    private String planYearHour;
    //实际年工时
    private String actualYearHour;
    //计划月工时
    private String planMonthHour;
    //实际月工时
    private String actualMonthHour;
}
