package com.ducetech.app.model.vo;

import com.ducetech.app.model.ShiftSetting;
import lombok.Data;

import java.util.List;

/**
 * 班次VO
 * Created by BLUE on 2017/5/18.
 */
@Data
public class ShiftSettingVO extends ShiftSetting {
    //开始时间
    private String startAtStr;
    //结束时间
    private String endAtStr;
    //总时间
    private String totalAtStr;
    //时间间隔
    private String intervalAtStr;
    //班制id
    private String modelId;
    //工作流程
    private List<WorkflowVO> workflowVOList;
}
