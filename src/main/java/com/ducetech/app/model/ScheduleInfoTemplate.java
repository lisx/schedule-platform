package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ScheduleInfoTemplate extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    //班制类型
    private String modelId;
    //班次类型
    private String shiftId;

    private String shiftName;

    private String shiftColor;

    private int shiftMinutes;

    private int weekNumber;

    private int weekDay;

    private int orderIndex;
    //修改人
    private String updatorId;

}
