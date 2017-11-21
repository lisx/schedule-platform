package com.ducetech.app.model.vo;

import com.ducetech.app.model.Workflow;
import com.ducetech.app.model.WorkflowContent;
import lombok.Data;

import java.util.List;

@Data
public class WorkflowVO extends Workflow {
    private List<WorkflowContent> workflowContentList;
}
