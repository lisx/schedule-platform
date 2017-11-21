package com.ducetech.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.app.model.*;
import com.ducetech.app.model.vo.ShiftSettingVO;
import com.ducetech.app.model.vo.WorkflowVO;
import com.ducetech.app.service.*;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class WorkflowController extends BaseController {
    @Autowired
    private WorkflowService workflowService;
    @Autowired
    private ShiftSettingService shiftSettingService;
    @Autowired
    private WorkflowContentService workflowContentService;
    @Autowired
    private ShiftModelService modelService;

    @Autowired
    private ScheduleInfoService scheduleInfoService;

    @RequestMapping(value = "/workflowSetting", method = RequestMethod.GET)
    public String workflow(ModelMap model, HttpServletRequest request) {
        List<ShiftModel> models=modelService.getAllModels();
        model.put("models", models);
        return "/workflow/index";
    }

    @RequestMapping(value = "/getAllWorkflowContent", method = RequestMethod.GET)
    @ResponseBody
    public List<ShiftSettingVO> selectAllWorkflowContent(String modelId) {
        List<ShiftSettingVO> shiftSettingVOList = new ArrayList<>();
        List<WorkflowVO> workflowVOList;
        ShiftSettingVO settingVO;

        List<ShiftSetting> shiftSettingList = shiftSettingService.selectShiftSettingByModelId(modelId);
        for (ShiftSetting ss : shiftSettingList) {
            workflowVOList = new ArrayList<>();

            settingVO = new ShiftSettingVO();
            BeanUtils.copyProperties(ss, settingVO);

            Workflow workflow = new Workflow();
            workflow.setShiftId(Integer.parseInt(ss.getShiftId()));
            List<Workflow> workflowList = workflowService.selectWorkflows(workflow);

            for (Workflow wf : workflowList) {
                WorkflowContent workflowContent = new WorkflowContent();
                workflowContent.setWorkflowId(wf.getWorkflowId());
                List<WorkflowContent> workflowContentList = workflowContentService.selectWorkflowContents(workflowContent);

                WorkflowVO workflowVO = new WorkflowVO();
                BeanUtils.copyProperties(wf, workflowVO);

                workflowVO.setWorkflowContentList(workflowContentList);

                workflowVOList.add(workflowVO);
            }
            settingVO.setWorkflowVOList(workflowVOList);
            shiftSettingVOList.add(settingVO);
        }
        return shiftSettingVOList;
    }

    /**
     * 增加
     *
     * @param
     */
    @RequestMapping(value = "/workflowAdd", method = RequestMethod.POST)
    @ResponseBody
    public Object workflowAdd(HttpServletRequest request, Workflow workflow) throws Exception {
        User u = getLoginUser(request);
        workflow.setIsDeleted(0);
        workflow.setCreatorId(Integer.parseInt(u.getUserId()));
        workflow.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        workflowService.insertWorkflow(workflow);
        return workflow;
    }

    /**
     * 删除
     *
     * @param
     */
    @RequestMapping(value = "/workflowDel", method = RequestMethod.GET)
    @ResponseBody
    public OperationResult workflowDel(Integer workflowId) throws Exception {
        workflowService.deleteWorkflowByWorkflowId(workflowId);
        return OperationResult.buildSuccessResult("删除成功", "success");
    }

    /**
     * 修改
     *
     * @param
     */
    @RequestMapping(value = "/workflowUpdate", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult workflowUpdate(HttpServletRequest request, Workflow workflow) throws Exception {
        User u = getLoginUser(request);
        workflow.setUpdatorId(Integer.parseInt(u.getUserId()));
        workflow.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        workflowService.updateWorkflow(workflow);
        return OperationResult.buildSuccessResult("修改成功", "success");
    }

    /**
     * 根据id查询
     *
     * @param
     */
    @RequestMapping(value = "/workflowGetById", method = RequestMethod.GET)
    @ResponseBody
    public Workflow workflowGetById(HttpServletRequest request, Integer workflowId) throws Exception {
        return workflowService.selectWorkflowByWorkflowId(workflowId);
    }

    /**
     * 根据班次id查询
     *
     * @param
     */
    @RequestMapping(value = "/workflowList", method = RequestMethod.GET)
    @ResponseBody
    public List<Workflow> workflowList(HttpServletRequest request, Workflow workflow) throws Exception {
        return workflowService.selectWorkflows(workflow);
    }

    @RequestMapping(value = "/getWorkflowContentByCode", method = RequestMethod.GET)
    public
    @ResponseBody
    Object getWorkflowContentByCode(String shiftId,String text) {
        long start = System.currentTimeMillis();
        JSONObject result = new JSONObject();
        try {
            if (text==null){
                return null;
            }
            List<WorkflowContent> list= scheduleInfoService.getWorkflowContentByCode(shiftId,text);
            result.put("data", list);
        } catch (Exception e) {
            logger.error("error:", e);
            return "error";
        }
        logger.debug("running time: " + (System.currentTimeMillis() - start));
        return result;
    }
}
