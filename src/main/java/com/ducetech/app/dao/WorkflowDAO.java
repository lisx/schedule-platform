package com.ducetech.app.dao;

import com.ducetech.app.model.Workflow;

import java.util.List;

public interface WorkflowDAO {
    Integer insertWorkflow(Workflow workflow);

    Integer deleteWorkflowByWorkflowId(Integer workflowId);

    Integer updateWorkflow(Workflow workflow);

    Workflow selectWorkflowByWorkflowId(Integer workflowId);

    List<Workflow> selectWorkflows(Workflow workflow);
    List<Workflow> selectWorkflowsByShiftId(String shiftId);
}