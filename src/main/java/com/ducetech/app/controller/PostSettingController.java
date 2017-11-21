package com.ducetech.app.controller;


import com.alibaba.fastjson.JSONObject;
import com.ducetech.app.model.PostSetting;
import com.ducetech.app.model.User;
import com.ducetech.app.service.PostSettingService;
import com.ducetech.app.service.ShiftPopulationService;
import com.ducetech.app.service.ShiftSettingService;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
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


@Controller
public class PostSettingController extends BaseController {
    protected Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private PostSettingService postSettingService;
    @Autowired
    private ShiftSettingService shiftService;
    @Autowired
    private ShiftPopulationService shiftPopulationService;

    @RequestMapping(value = "/postSetting", method = RequestMethod.GET)
    public String postSettings(ModelMap model) {
        return "/postSetting/index";
    }




    /**
     * @return void
     * @throws Exception
     * @Title: personData
     * @Description: 岗位设置数据
     */
    @RequestMapping(value = "/postSettings", method = RequestMethod.GET)
    @ResponseBody
    public PagerRS<PostSetting> postSettingData(HttpServletRequest request) throws Exception {
        BaseQuery<PostSetting> query = PostSetting.getSearchCondition(PostSetting.class, request);
        PagerRS<PostSetting> rs = postSettingService.getPostSettingByPager(query);
        return rs;
    }
    @RequestMapping(value = "/getMarvinsGymRecords", method = RequestMethod.GET)
    @ResponseBody
    public Object getMarvinsGymRecords(HttpServletRequest request) throws Exception {
        JSONObject js=new JSONObject();
        js.put("name","marvin");
        return js;
    }

    /**
     * @return void
     * @Title: addPostSetting
     * @Description: 新增岗位设置
     */
    @RequestMapping(value = "/addPostSettingForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addPostSettingForm(PostSetting postSetting, String parentCode, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        postSetting.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        postSetting.setCreatorId(userInfo.getUserId());
        postSetting.setIfUse(0);
        if (parentCode == null) {
            postSetting.setPostCode(postSettingService.selectPostSettingByParentCode("000"));
        } else {
            postSetting.setPostCode(postSettingService.selectPostSettingByParentCode(parentCode));
        }
        postSettingService.insertPostSetting(postSetting);
        return OperationResult.buildSuccessResult("新增岗位设置成功", "success");
    }

    /**
     * @Description: 更新岗位设置
     */
    @RequestMapping(value = "/editPostSettingForm", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult editPostSettingForm(PostSetting postSetting, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        postSetting.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        postSetting.setUpdatorId(userInfo.getUserId());
        postSettingService.updatePostSetting(postSetting);
        return OperationResult.buildSuccessResult("更新岗位设置成功", "success");
    }


    /**
     * @param postSettingCode
     * @Description: 跳转编辑页面
     */
    @RequestMapping(value = "/postSetting/{postSettingCode}", method = RequestMethod.GET)
    @ResponseBody
    public PostSetting edit(@PathVariable(value = "postSettingCode") String postSettingCode) throws Exception {
        PostSetting postSetting = new PostSetting();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(postSettingCode)) {
            postSetting = postSettingService.selectPostSettingByPostCode(postSettingCode);
        }
        return postSetting;
    }

    /**
     * 删除岗位
     *
     * @param postCode
     * @param ifUse
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/delPostSettingForm", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult delPostSettingForm(String postCode, int ifUse) throws Exception {
        postSettingService.deletePostSetting(postCode, ifUse);
        return OperationResult.buildSuccessResult("删除岗位设置成功", "success");
    }

    @RequestMapping(value = "/getPostSettingByPostId", method = RequestMethod.GET)
    @ResponseBody
    public PostSetting getPostSettingByPostId(String postId) throws Exception {
        return postSettingService.getPostSettingByPostId(postId);
    }
}