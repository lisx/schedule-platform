package com.ducetech.app.model;

import lombok.Data;

/**
 * 工作流程内容
 */
@Data
public class WorkflowContent {
    private Integer contentId;//流程内容id
    private Integer startTime;
    private Integer endTime;
    private String content;
    private Integer creatorId;//创建者id
    private Integer updatorId;//修改者id
    private Integer isDeleted;//是否删除
    private String createdAt;//创建时间
    private String updatedAt;//修改时间
    private Integer workflowId;//工作流程id
    private Integer startLocation;//开始位置
    private Integer endLocation;//结束位置
    private Integer rowsNum;//第几行
    private String contentColor;//颜色
}
