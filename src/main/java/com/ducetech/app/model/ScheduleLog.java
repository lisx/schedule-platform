package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 排班临时操作
 * Created by lisx on 2017/4/27.
 */
@Data
public class ScheduleLog extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private String scheduleLogId;
    //排版详情
    private String scheduleInfoId;
    //排班人员
    private String userId;
    //排班人员姓名
    private String userName;
    //临时类型
    private String logType;
    //详细类型
    private String detailType;
    //开始时间
    private String startAt;
    //结束类型
    private String endAt;
    //占用时间
    private int timeAt;
    //操作记录
    private String remark;
    //是否使用
    private int ifUse;
    //创建人
    private String creatorId;
    //创建人姓名
    private String creatorName;
    //创建时间
    private String createdAt;
}
