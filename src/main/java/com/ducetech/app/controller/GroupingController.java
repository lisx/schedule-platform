package com.ducetech.app.controller;


import com.ducetech.app.model.Grouping;
import com.ducetech.app.model.User;
import com.ducetech.app.service.GroupingService;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;


@Controller
public class GroupingController extends BaseController {
	protected Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private GroupingService groupingService;

	/**
	 * @Title: person
	 * @param model
	 * @return String
	 * @Description: 跳转部门首页
	 */
	@RequestMapping(value = "/grouping", method = RequestMethod.GET)
	public String groupings(ModelMap model) {
        List<Grouping> stationArea = groupingService.selectByParentCode("000",6);
        model.put("stationArea",stationArea);
	    return "/grouping/index";
	}

	/**
	 * @Title: personData
	 * @return void
	 * @throws Exception
	 * @Description: 部门首页数据
	 */
	@RequestMapping(value = "/groupings", method = RequestMethod.GET)
	@ResponseBody
	public List<Grouping> groupingData(HttpServletRequest request) throws Exception {
		List<Grouping> stationArea = groupingService.selectByParentCode("000",6);
		return stationArea;
	}

    /**
     * @Title: addGrouping
     * @return void
     * @Description: 新增站区
     */
    @RequestMapping(value = "/group/addStationAreaForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addStationAreaForm(Grouping group, String parentCode, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        group.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        group.setCreatorId(userInfo.getUserId());
        group.setIsDeleted(0);
        group.setGroupOrder(0);
        group.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        group.setUpdatorId(userInfo.getUserId());
        if (parentCode==null) {
            group.setGroupCode(groupingService.selectGroupingByParentCode("000"));
        }else{
            group.setGroupCode(groupingService.selectGroupingByParentCode(parentCode));
        }
        groupingService.insertGrouping(group);
        return OperationResult.buildSuccessResult("新增站区成功", "success");
    }
    /**
     * @Description: 更新站区
     */
    @RequestMapping(value = "/group/editStationAreaForm", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult editStationAreaForm(Grouping group, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        group.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        group.setUpdatorId(userInfo.getUserId());
        groupingService.updateGrouping(group);
        return OperationResult.buildSuccessResult("更新站区成功", "success");
    }

    /**
     * 添加站点
     * @param group
     * @param parentCode
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/group/addStationForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addStationForm(Grouping group, String parentCode, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        group.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        group.setCreatorId(userInfo.getUserId());
        group.setIsDeleted(0);
        group.setGroupOrder(0);
        group.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        group.setUpdatorId(userInfo.getUserId());
        if(parentCode==null) {
            group.setGroupCode(groupingService.selectGroupingByParentCode("000"));
        }else{
            group.setGroupCode(groupingService.selectGroupingByParentCode(parentCode));
        }
        groupingService.insertGrouping(group);
        return OperationResult.buildSuccessResult("新增站点成功", "success");
    }
    /**
     * 添加站点
     * @param group
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/group/editStation", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult editStation(Grouping group, HttpServletRequest request) throws Exception {
        System.out.println("|||"+group.getGroupName());
        User userInfo = getLoginUser(request);
        group.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        group.setUpdatorId(userInfo.getUserId());
        groupingService.updateGrouping(group);
        return OperationResult.buildSuccessResult("更新站点成功", "success");
    }
    /**
     * 编辑部门
     */

    /**
     * @param groupCode
     * @Description: 跳转人员编辑页面
     */
    @RequestMapping(value = "/grouping/{groupCode}", method = RequestMethod.GET)
    @ResponseBody
    public Grouping edit(@PathVariable(value="groupCode")String groupCode) throws Exception {
        Grouping group = new Grouping();
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(groupCode)){
            group = groupingService.selectGroupingByGroupCode(groupCode);
        }
        return group;
    }

    @RequestMapping(value = "/grouping/delStationAreaForm", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult delStationArea(String groupCode) throws Exception {
        groupingService.deleteGrouping(groupCode);
        String msg;
        if(groupCode.length()==6){
            msg = "删除站区成功";
        }else{
            msg = "删除站点成功";
        }

        return OperationResult.buildSuccessResult(msg, "success");
    }

    @RequestMapping(value = "/group/querySubNodes", method = RequestMethod.GET)
    @ResponseBody
    public List<Grouping> querySubNodes(String oneCode) throws Exception {
        List<Grouping> groups = groupingService.selectByParentCode(oneCode, oneCode.length() + 3);
        return groups;
    }
}