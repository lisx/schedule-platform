package com.ducetech.app.service.impl;

import com.ducetech.app.dao.WorkflowContentDAO;
import com.ducetech.app.model.WorkflowContent;
import com.ducetech.app.service.WorkflowContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WorkflowContentServiceImpl implements WorkflowContentService {
    @Autowired
    private WorkflowContentDAO workflowContentDAO;

    @Override
    public Integer insertWorkflowContent(WorkflowContent workflowContent) {
        workflowContent.setIsDeleted(0);
        return workflowContentDAO.insertWorkflowContent(workflowContent);
    }

    @Override
    public Integer deleteWorkflowContentById(Integer workflowContentId) {
        return workflowContentDAO.deleteWorkflowContentById(workflowContentId);
    }

    @Override
    public Integer updateWorkflowCotent(WorkflowContent workflowContent) {
        return workflowContentDAO.updateWorkflowCotent(workflowContent);
    }

    @Override
    public WorkflowContent selectWorkflowContentById(Integer workflowContentId) {
        return workflowContentDAO.selectWorkflowContentById(workflowContentId);
    }

    @Override
    public List<WorkflowContent> selectWorkflowContents(WorkflowContent workflowContent) {
        return workflowContentDAO.selectWorkflowContents(workflowContent);
    }

    @Override
    public boolean checkTime(int time,int row,Integer id) {
        return workflowContentDAO.selectCovered(time,row,id)>0;
    }
}
