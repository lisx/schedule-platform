package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 排版详情
 * Created by lisx on 2017/4/27.
 */
@Data
public class ScheduleInfo extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String scheduleInfoId;
    //最后一个logId
    private String logId;
    //班制类型
    private String modelId;
    //班次类型
    private String shiftId;
    //班次名称
    private String shiftName;
    //班次个数
    private int shiftNum;
    //岗位
    private String postName;
    //当班总时间
    private Integer totalAt;
    //排班人员
    private String userId;
    //排班人名
    private String userName;
    //排班编号防止重名
    private String userCode;
    //排班日期
    private Date scheduleDate;

    private String scheduleDay;
    //星期几
    private String scheduleWeek;
    //综控员分组
    private String groupName;
    //所属站点
    private String station;
    //所属站区
    private String stationArea;
    //备注
    private String scheduleDesc;
    //是否请假
    private int ifLeave;
    //是否使用
    private int ifUse;
    //请假类型  1假期编辑 2班次变更 3临时安排 4旷工缺勤 5补办加班 6替班
    private String leaveType;
    //创建人
    private String creatorId;
    //创建时间
    private String createdAt;
    //流程编号
    private String serialNumber;
    //班次代号
    private String shiftCode;
    //班次颜色
    private String shiftColor;
}
