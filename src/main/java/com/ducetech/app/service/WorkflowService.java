package com.ducetech.app.service;

import com.ducetech.app.model.Workflow;

import java.util.List;

public interface WorkflowService {
    Integer insertWorkflow(Workflow workflow);

    Integer deleteWorkflowByWorkflowId(Integer workflowId);

    Integer updateWorkflow(Workflow workflow);

    Workflow selectWorkflowByWorkflowId(Integer workflowId);

    List<Workflow> selectWorkflows(Workflow workflow);
}
