package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 班制字典
 * Created by lisn 2017/4/27.
 */
@Data
public class ShiftModel extends BaseModel implements Serializable{
    private static final long serialVersionUID = 1L;
    
    private String modelId;
    // 字典code
    private String modelCode;

    // 字典名称
    private String modelName;

    //站区
    private String stationArea;

    //站点
    private String station;

    //周工时下限
    private Integer minWeeklyReason;

    //周工时上限
    private Integer maxWeeklyReason;

    // 每周最少休班(天)
    private Integer minWeeklyRest;

    //每周最大休班（天）
    private Integer maxWeeklyRest;

    // 月工时上限(时)
    private Integer postMonth;

    // 年工时上限(时)
    private Integer postYear;

    //岗位关联
    private String postId;

    // 是否使用
    private int ifUse;

    // 创建人
    private String creatorId;

    // 创建时间
    private String createdAt;
}
