package com.ducetech.app.model;

import lombok.Data;

/**
 * 工作流程
 */
@Data
public class Workflow {
    private Integer workflowId;//id
    private Integer postId;//岗位id
    private Integer modelId;//班制id
    private Integer shiftId;//班次id
    private String serialNumber;//流程编号
    private Integer creatorId;//创建者id
    private Integer updatorId;//修改者id
    private Integer isDeleted;//是否删除
    private String createdAt;//创建时间
    private String updatedAt;//修改时间
}
