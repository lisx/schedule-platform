package com.ducetech.app.dao;

import com.ducetech.app.model.WorkflowContent;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface WorkflowContentDAO {
    Integer insertWorkflowContent(WorkflowContent workflowContent);

    Integer deleteWorkflowContentById(Integer workflowContentId);

    Integer updateWorkflowCotent(WorkflowContent workflowContent);

    WorkflowContent selectWorkflowContentById(Integer workflowContentId);

    List<WorkflowContent> selectWorkflowContents(WorkflowContent workflowContent);

    Integer deleteWorkflowContentByWorkflowId(Integer workflowId);

    List<WorkflowContent> selectWorkflowContentsByWorkflowId(@Param("workflowId") Integer workflowId);
}
