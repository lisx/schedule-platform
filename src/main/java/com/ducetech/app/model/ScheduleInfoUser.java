package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;

@Data
public class ScheduleInfoUser extends BaseModel implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;
    //班制类型
    private String modelId;

    private String userId;

    private String userName;

    private int weekNumber;

}
