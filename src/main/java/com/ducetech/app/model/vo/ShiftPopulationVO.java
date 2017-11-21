package com.ducetech.app.model.vo;

import com.ducetech.app.model.ShiftPopulation;
import lombok.Data;

/**
 * 排班人数表
 * Created by BLUE on 2017/5/22.
 */
@Data
public class ShiftPopulationVO extends ShiftPopulation {
    //开始时间
    private String startAtStr;
    //结束时间
    private String endAtStr;
}
