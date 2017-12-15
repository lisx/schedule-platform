package com.ducetech.app.service;

import com.ducetech.app.model.WorkflowContent;

import java.util.List;

public interface WorkflowContentService {
    Integer insertWorkflowContent(WorkflowContent workflowContent);

    Integer deleteWorkflowContentById(Integer workflowContentId);

    Integer updateWorkflowCotent(WorkflowContent workflowContent);

    WorkflowContent selectWorkflowContentById(Integer workflowContentId);

    List<WorkflowContent> selectWorkflowContents(WorkflowContent workflowContent);

    boolean checkTime(int time,int row,Integer workflowId );
}
