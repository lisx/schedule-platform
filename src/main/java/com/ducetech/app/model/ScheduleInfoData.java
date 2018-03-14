package com.ducetech.app.model;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 排版详情
 * Created by lisx on 2017/4/27.
 */
public class ScheduleInfoData{
    public String scheduleId;
    //用户
    public String userId;
    //分组
    public String groupName;
    //用户人名
    public String userName;
    //用户人名
    public String userCode;
    public String station;
    public String stationArea;
    public Map<String,String> stations=new HashMap<>();
    //岗位
    public String postName;
    //排班详情
    public List<ScheduleInfo> scheduleInfoList;
    //计划工时
    public int planHours;
    //实际工时
    public int actualHours;
    //请假天数
    public int leave;

    public String shiftId;
}

