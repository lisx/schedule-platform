package com.ducetech.app.controller;

import com.ducetech.app.model.User;
import com.ducetech.app.model.WorkflowContent;
import com.ducetech.app.service.WorkflowContentService;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@Controller
public class WorkflowContentController extends BaseController {
    @Autowired
    private WorkflowContentService workflowContentService;

    /**
     * 增加
     *
     * @param
     */
    @RequestMapping(value = "/workflowContentAdd", method = RequestMethod.POST)
    @ResponseBody
    public Object workflowContentAdd(HttpServletRequest request, WorkflowContent workflowContent) throws Exception {
        User u = getLoginUser(request);
        workflowContent.setCreatorId(Integer.parseInt(u.getUserId()));
        workflowContent.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        workflowContentService.insertWorkflowContent(workflowContent);
        return workflowContent;
    }

    /**
     * 删除
     *
     * @param
     */
    @RequestMapping(value = "/workflowContentDel", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult workflowContentDel(Integer contentId) throws Exception {
        workflowContentService.deleteWorkflowContentById(contentId);
        return OperationResult.buildSuccessResult("删除成功", "success");
    }

    /**
     * 修改
     *
     * @param
     */
    @RequestMapping(value = "/workflowContentUpdate", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult workflowContentUpdate(HttpServletRequest request, WorkflowContent workflowContent) throws Exception {
        User u = getLoginUser(request);
        workflowContent.setUpdatorId(Integer.parseInt(u.getUserId()));
        workflowContent.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        workflowContentService.updateWorkflowCotent(workflowContent);
        return OperationResult.buildSuccessResult("修改成功", "success");
    }

    /**
     * 根据id查询
     *
     * @param
     */
    @RequestMapping(value = "/workflowContentGetById", method = RequestMethod.GET)
    @ResponseBody
    public WorkflowContent workflowContentGetById(HttpServletRequest request, Integer contentId) throws Exception {
        return workflowContentService.selectWorkflowContentById(contentId);
    }

    /**
     * 根据班次id查询
     *
     * @param
     */
    @RequestMapping(value = "/workflowContentList", method = RequestMethod.GET)
    @ResponseBody
    public List<WorkflowContent> workflowList(HttpServletRequest request, WorkflowContent workflowContent) throws Exception {
        return workflowContentService.selectWorkflowContents(workflowContent);
    }
}
