package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 班次设置
 * Created by lisx on 2017/4/27.
 */
@Data
public class ShiftSetting extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;
    private String shiftId;
    //班次名称
    private String shiftName;
    //班次人数
    private int shiftNum;
    //班次颜色
    private String shiftColor;
    //班次代号
    private String shiftCode;
    //班次注意事项
    private String shiftExplain;
    //开始时间
    private int startAt;
    //结束时间
    private int endAt;
    //总时间
    private Integer totalAt;
    //间隔时间
    private Integer intervalAt;
    //关联班次
    private String relevanceName;
    //关联班次
    private String relevanceId;
    //所属站点
    private String station;
    //所属站区
    private String stationArea;
    //岗位设置
    private String postId;
    //班制设置
    private String modelId;
    //是否删除
    private Integer ifUse;
    //创建人
    private String creatorId;
    //更新人
    private String updatorId;
    //创建时间
    private String createdAt;
    //更新时间
    private String updatedAt;
}
