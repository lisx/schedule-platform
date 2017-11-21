package com.ducetech.app.model;

/**
 * 工时报表
 * Created by lisx on 2017/4/27.
 */
public class LaboReport {
    /**
     * 根据排班表检索 每个人每种班次的个数*班次总时间 得到单班次总时间 累加所有班次总时间 得到 这个人检索时间段内的总时间.
     */
    //时间名称
    public String spanName;
    //统计类型
    public String spanType;
    //用户
    public String userId;
}
