package com.ducetech.app.controller;


import com.ducetech.app.model.ShiftModel;
import com.ducetech.app.model.User;
import com.ducetech.app.service.ShiftModelService;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@Controller
public class ShiftModelController extends BaseController {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private ShiftModelService shiftModelService;

	/**
	 * @Title: person
	 * @param model
	 * @return String
	 * @Description: 跳转部门首页
	 */
	@RequestMapping(value = "/shiftModel", method = RequestMethod.GET)
	public String shiftModels(ModelMap model) {
        List<ShiftModel> stationArea = shiftModelService.selectByParentCode("000",6);
        model.put("stationArea",stationArea);
	    return "/shiftModel/index";
	}

	/**
	 * @Title: personData
	 * @return void
	 * @throws Exception
	 * @Description: 获取班制数据
	 */
	@RequestMapping(value = "/shiftModelData", method = RequestMethod.GET)
	@ResponseBody
	public List<ShiftModel> shiftModelData(String postId) throws Exception {
		List<ShiftModel> shiftModels = shiftModelService.selectShiftModel(postId);
		System.out.println(shiftModels.size()+"|||");
		return shiftModels;
	}

    /**
     * @Title: addShiftModel
     * @return void
     * @Description: 新增班制
     */
    @RequestMapping(value = "/addShiftModel", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addShiftModel(ShiftModel model, String parentCode, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        if(userInfo.getStationArea()==null){
            throw new RuntimeException("没有所属站区的用户不能编辑班制");
        }
        model.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        model.setCreatorId(userInfo.getUserId());
        model.setStationArea(userInfo.getStationArea());
        model.setStation(userInfo.getStation());
        model.setIfUse(0);
        if(parentCode==null) {
            model.setModelCode(shiftModelService.selectShiftModelByParentCode("000"));
        }else{
            model.setModelCode(shiftModelService.selectShiftModelByParentCode(parentCode));
        }
        model.setMinWeeklyReason(model.getMinWeeklyReason() * 60);
        model.setMaxWeeklyReason(model.getMaxWeeklyReason() * 60);
        model.setPostMonth(model.getPostMonth() * 60);
        model.setPostYear(model.getPostYear() * 60);
        shiftModelService.insertShiftModel(model);
        return OperationResult.buildSuccessResult("新增班制成功", "success");
    }

    /**
     * 添加站点
     * @param model
     * @param parentCode
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/model/addStationForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addStationForm(ShiftModel model, String parentCode, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        model.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        model.setCreatorId(userInfo.getUserId());
        model.setIfUse(0);
        if(parentCode==null) {
            model.setModelCode(shiftModelService.selectShiftModelByParentCode("000"));
        }else{
            model.setModelCode(shiftModelService.selectShiftModelByParentCode(parentCode));
        }
        shiftModelService.insertShiftModel(model);
        return OperationResult.buildSuccessResult("新增站点成功", "success");
    }

    @RequestMapping(value = "/updateShiftModel", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult updateShiftModel(ShiftModel model, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        model.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        model.setCreatorId(userInfo.getUserId());
        model.setIfUse(0);
        shiftModelService.updateShiftModel(model);
        return OperationResult.buildSuccessResult("修改班制成功", "success");
    }
    /**
     * 编辑部门
     */

    /**
     * @param modelCode
     * @Description: 跳转人员编辑页面
     */
    @RequestMapping(value = "/shiftModel/{modelCode}", method = RequestMethod.GET)
    @ResponseBody
    public ShiftModel edit(@PathVariable(value="modelCode")String modelCode) throws Exception {
        ShiftModel model = new ShiftModel();
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(modelCode)){
            model = shiftModelService.selectShiftModelByModelCode(modelCode);
        }
        return model;
    }

    @RequestMapping(value = "/delShiftModel", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult delShiftModel(String modelCode) throws Exception {
        shiftModelService.deleteShiftModel(modelCode);
        return OperationResult.buildSuccessResult("删除班制成功", "success");
    }
}