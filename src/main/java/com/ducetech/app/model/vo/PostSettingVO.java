package com.ducetech.app.model.vo;

import com.ducetech.app.model.PostSetting;
import lombok.Data;

import java.util.List;

/**
 *  岗位设置vo
 * Created by BLUE on 2017/5/23.
 */
@Data
public class PostSettingVO extends PostSetting {
    //员工平均每周休班天数
    private Double avgWeeklyRest;
    //总结果
    private String status;
    //每周休班天数结果
    private String statusAvgWeeklyRest;
    //员工平均每周工时结果
    private String statusWorkHour;
    //24小时值班表结果
    private String statusPopulationAndSetting;
    //班制id
    private String modelId;
    //总排班人数
    private Integer totalCount;
    //周工时合理最低
    private Integer min_post_week;
    //周工时合理最高
    private Integer max_post_week;
    //员工平均周工时
    private Double avgHourWeekly;
    //员工平均月工时
    private Double avgHourMonth;
    //员工平均年工时
    private Double avgHourYear;
    //不合格时间段
    private List<String> failTime;
}


