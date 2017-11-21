package com.ducetech.app.service.impl;

import com.ducetech.app.dao.WorkflowContentDAO;
import com.ducetech.app.dao.WorkflowDAO;
import com.ducetech.app.model.Workflow;
import com.ducetech.app.service.WorkflowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowServiceImpl implements WorkflowService {
    @Autowired
    private WorkflowDAO workflowDAO;
    @Autowired
    private WorkflowContentDAO workflowContentDAO;

    @Override
    public Integer insertWorkflow(Workflow workflow) {
        return workflowDAO.insertWorkflow(workflow);
    }

    @Override
    public Integer deleteWorkflowByWorkflowId(Integer workflowId) {
        workflowContentDAO.deleteWorkflowContentByWorkflowId(workflowId);
        return workflowDAO.deleteWorkflowByWorkflowId(workflowId);
    }

    @Override
    public Integer updateWorkflow(Workflow workflow) {
        return workflowDAO.updateWorkflow(workflow);
    }

    @Override
    public Workflow selectWorkflowByWorkflowId(Integer workflowId) {
        return workflowDAO.selectWorkflowByWorkflowId(workflowId);
    }

    @Override
    public List<Workflow> selectWorkflows(Workflow workflow) {
        return workflowDAO.selectWorkflows(workflow);
    }
}
