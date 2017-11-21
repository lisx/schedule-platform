package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

/**
 * 排班人数
 * Created by BLUE on 2017/5/22.
 */
@Data
public class ShiftPopulation extends BaseModel implements Serializable {
    //排班人数表id
    private Integer populationId;
    //开始时间
    private Integer startAt;
    //结束时间
    private Integer endAt;
    //人数
    private Integer populationCount;
    //岗位设置
    private String postId;
    //关联班制id
    private String modelId;
}
