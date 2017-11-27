package com.ducetech.app.controller;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.app.model.*;
import com.ducetech.app.model.vo.PostSettingVO;
import com.ducetech.app.model.vo.ShiftPopulationVO;
import com.ducetech.app.model.vo.ShiftSettingVO;
import com.ducetech.app.service.*;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
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
import java.util.*;

/**
 * 班次设置
 */
@Controller
public class ShiftSettingController extends BaseController {
    @Autowired
    private ShiftSettingService shiftService;
    @Autowired
    private PostSettingService postSettingService;
    @Autowired
    private ShiftModelService shiftModelService;
    @Autowired
    private ShiftPopulationService shiftPopulationService;
    @Autowired
    private GroupingService groupingService;

    @RequestMapping(value = "/shiftSetting", method = RequestMethod.GET)
	public String shift(ModelMap model, HttpServletRequest request) {
        List<PostSetting> postList = postSettingService.getAllPostSettings();
        List<Grouping> groups = groupingService.queryGroupsByLength(6);
        model.put("postList",postList);
        model.addAttribute("groups", groups);
        return "/shiftSetting/index";
	}
    @RequestMapping(value = "/shiftSettingData", method = RequestMethod.GET)
    @ResponseBody
    public PagerRS<ShiftSettingVO> shiftSettingData(HttpServletRequest request) throws Exception {
        BaseQuery<ShiftSetting> query = ShiftSetting.getSearchCondition(ShiftSetting.class, request);
        PagerRS<ShiftSetting> rs = shiftService.getShiftByPager(query);

        List<ShiftSetting> ss = rs.getResults();

        List<ShiftSettingVO> rsvos = new ArrayList<ShiftSettingVO>();

        for(int i = 0; i < ss.size(); i++) {
            ShiftSettingVO rsvo = new ShiftSettingVO();
            BeanUtils.copyProperties(ss.get(i), rsvo);

            rsvo.setStartAtStr(DateUtil.minuToTime(rsvo.getStartAt()));

            rsvo.setEndAtStr(DateUtil.minuToTime(rsvo.getEndAt()));
            rsvo.setTotalAtStr(DateUtil.minuToStr(rsvo.getTotalAt()));
            rsvo.setIntervalAtStr(DateUtil.minuToHour(rsvo.getIntervalAt()));

            rsvos.add(rsvo);
        }

        PagerRS<ShiftSettingVO> prsVO = new PagerRS<ShiftSettingVO> (rsvos,rs.getCount(),rs.getPages());
        return prsVO;
    }

    @RequestMapping(value = "/getShiftSetting", method = RequestMethod.GET)
    @ResponseBody
    public List<ShiftSettingVO> getShiftSetting(ShiftSetting shift) throws Exception {
        List<ShiftSetting> rs = shiftService.selectShiftSetting(shift);
        List<ShiftSettingVO> rsvos = new ArrayList<ShiftSettingVO>(rs.size());

        for(int i = 0; i < rs.size(); i++) {
            ShiftSettingVO shiftSettingVO = new ShiftSettingVO();
            BeanUtils.copyProperties(rs.get(i),shiftSettingVO);
            shiftSettingVO.setStartAtStr(DateUtil.minuToTime( shiftSettingVO.getStartAt()));
            shiftSettingVO.setEndAtStr(DateUtil.minuToTime( shiftSettingVO.getEndAt()));
            shiftSettingVO.setTotalAtStr(DateUtil.minuToStr( shiftSettingVO.getTotalAt()));
            shiftSettingVO.setIntervalAtStr(DateUtil.minuToHour( shiftSettingVO.getIntervalAt()));

            rsvos.add(shiftSettingVO);
        }
        return rsvos;
    }
    @RequestMapping(value = "/addShiftSettingForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult addShiftSettingForm(ShiftSettingVO shift, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);

        ShiftModel model = shiftModelService.selectShiftModelByModelCodeId(null, shift.getModelId());
        shift.setStation(model.getStation());
        shift.setStationArea(model.getStationArea());

        shift.setShiftColor(shift.getShiftColor().substring(1));

        if("请选择".equals(shift.getRelevanceId()) || "".equals(shift.getRelevanceId())) {
            shift.setRelevanceId(null);
        }

        if("".equals(shift.getRelevanceName()) || "请选择".equals(shift.getRelevanceName())) {
            shift.setRelevanceName(null);
        }

        shift.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        shift.setCreatorId(userInfo.getUserId());
        if(shift.getStationArea()==null){
            shift.setStation(userInfo.getStation());
        }
        if(shift.getStationArea()==null){
            shift.setStationArea(userInfo.getStationArea());
        }
        shift.setIfUse(0);
        //将字符串转换为分钟
        shift.setStartAt(DateUtil.timeToMinu(shift.getStartAtStr()));
        String shiftEndAtStr = shift.getEndAtStr();

        if("00:00".equals(shiftEndAtStr)) {
            shiftEndAtStr = "24:00";
        }

        shift.setEndAt(DateUtil.timeToMinu(shiftEndAtStr));
        shift.setTotalAt(DateUtil.strToMinu(shift.getTotalAtStr()));
        shift.setIntervalAt(DateUtil.hourToMinu(shift.getIntervalAtStr()));
        shiftService.insertShiftSetting(shift);
        return OperationResult.buildSuccessResult("新增班次成功", "success");
    }
    @RequestMapping(value = "/editShift", method = RequestMethod.GET)
    @ResponseBody
    public JSONObject editShiftSetting(ShiftSetting shift,String shiftid) throws Exception {
        List<ShiftSetting> rs = shiftService.selectShiftSetting(shift);
        ShiftSetting ss = shiftService.editShiftSetting(shiftid);

        List<ShiftSettingVO> rsvos = new ArrayList<ShiftSettingVO>(rs.size());

        for(int i = 0; i < rs.size(); i++) {
            ShiftSettingVO shiftSettingVO = new ShiftSettingVO();
            BeanUtils.copyProperties(rs.get(i),shiftSettingVO);

            shiftSettingVO.setStartAtStr(DateUtil.minuToTime( rs.get(i).getStartAt()));

            shiftSettingVO.setEndAtStr(DateUtil.minuToTime( rs.get(i).getEndAt()));
            shiftSettingVO.setTotalAtStr(DateUtil.minuToStr( rs.get(i).getTotalAt()));
            shiftSettingVO.setIntervalAtStr(DateUtil.minuToHour( rs.get(i).getIntervalAt()));

            rsvos.add(shiftSettingVO);
        }

       ShiftSettingVO ssvo = new ShiftSettingVO();

        BeanUtils.copyProperties(ss,ssvo);
        ssvo.setStartAtStr(DateUtil.minuToTime( ssvo.getStartAt()));
        ssvo.setEndAtStr(DateUtil.minuToTime( ssvo.getEndAt()));
        ssvo.setTotalAtStr(DateUtil.minuToStr( ssvo.getTotalAt()));
        ssvo.setIntervalAtStr(DateUtil.minuToHour( ssvo.getIntervalAt()));

        JSONObject obj=new JSONObject();
        obj.put("rs",rsvos);
        obj.put("ss",ssvo);
        return obj;
    }
    @RequestMapping(value = "/updateShift", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult updateShift(ShiftSettingVO shiftVO, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        shiftVO.setUpdatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        shiftVO.setUpdatorId(userInfo.getUserId());

        shiftVO.setShiftColor(shiftVO.getShiftColor().substring(1));

        ShiftSetting ss = new ShiftSetting();
        BeanUtils.copyProperties(shiftVO,ss);
        ss.setStartAt(DateUtil.timeToMinu(shiftVO.getStartAtStr()));

        System.out.println("startAt=======================" + ss.getStartAt());

        String endAtStr = shiftVO.getEndAtStr();
        if("00:00".equals(endAtStr)) {
            endAtStr = "24:00";
        }

        if("请选择".equals(ss.getRelevanceId()) || "".equals(ss.getRelevanceId())) {
            ss.setRelevanceId(null);
        }

        if("".equals(ss.getRelevanceName()) || "请选择".equals(ss.getRelevanceName())) {
            ss.setRelevanceName(null);
        }

        ss.setEndAt(DateUtil.timeToMinu(endAtStr));
        ss.setTotalAt(DateUtil.strToMinu(shiftVO.getTotalAtStr()));
        ss.setIntervalAt(DateUtil.hourToMinu(shiftVO.getIntervalAtStr()));

        shiftService.updateShiftSetting(ss);
        return OperationResult.buildSuccessResult("更新班次成功", "success");
    }

    @RequestMapping(value="/delShift",method=RequestMethod.DELETE)
    @ResponseBody
    public OperationResult delShift(ShiftSetting shiftSetting) {
        shiftService.deleteShiftSettingByShiftId(shiftSetting.getShiftId());
        return OperationResult.buildSuccessResult("删除班次成功", "success");
    }

    /**
     * 获取某个岗位的每日总工时
     * @param shiftSetting
     * @return
     */
    @RequestMapping(value="/getShiftSettionTotal",method=RequestMethod.GET)
    @ResponseBody
    public Map<String,String> getShiftSettionTotal(ShiftSetting shiftSetting) {
         Map<String,String> map = new HashMap<String,String>();
         Integer minuTotal = shiftService.getShiftSettionTotal(shiftSetting);
         map.put("total",DateUtil.minuToStr(minuTotal));
         return map;
    }

    /**
     * 方案校验
     * @param postSettingVO
     * @return
     */
    @RequestMapping(value = "/checkPlan", method = RequestMethod.GET)
    @ResponseBody
    public PostSettingVO checkPlan(PostSettingVO postSettingVO) {
        PostSettingVO result = new PostSettingVO();
        ShiftSetting shiftSetting = new ShiftSetting();
        BeanUtils.copyProperties(postSettingVO, shiftSetting);
        //根据岗位id获取岗位
        PostSetting postSetting = postSettingService.getPostSettingByPostId(postSettingVO.getPostId());
        //根据班制id获取班制
        ShiftModel shiftModel = shiftModelService.selectShiftModelByModelCodeId(null,postSettingVO.getModelId());
        //根据
        //获取班次
        List<ShiftSetting> shiftSettingList = shiftService.getShiftByQuery(shiftSetting);
        //总人数
        Integer totalCount = postSettingVO.getTotalCount();

        //****************校验每周休班天数****************
        //排班人数
        Integer currentTotalCount = 0;
        for (ShiftSetting shiftSetting1 : shiftSettingList) {
            currentTotalCount += shiftSetting1.getShiftNum();
        }
        //平均休班天数
        double avgWeeklyRest = (int)((totalCount - currentTotalCount) * 1.0 / totalCount * 7);
        result.setAvgWeeklyRest(avgWeeklyRest);

        Integer min_weekly_rest = shiftModel.getMinWeeklyRest();
        Integer max_weekly_rest = shiftModel.getMaxWeeklyRest();

        if(min_weekly_rest != null && max_weekly_rest != null) {
            if (avgWeeklyRest >= min_weekly_rest && avgWeeklyRest <= max_weekly_rest) {
                result.setStatusAvgWeeklyRest("success");
            } else {
                result.setStatusAvgWeeklyRest("fail");
            }
            result.setMinWeeklyRest(min_weekly_rest);
            result.setMaxWeeklyRest(max_weekly_rest);
        }

        //****************校验工时****************

        boolean flag = true;

        Integer minuTotal = shiftService.getShiftSettionTotal(shiftSetting);

        Double avgHourDay = minuTotal /(60.0 * postSettingVO.getTotalCount());

        result.setAvgHourWeekly(avgHourDay * 7);
        result.setAvgHourMonth(avgHourDay * 30);
        result.setAvgHourYear(avgHourDay * 365);
        result.setTotalCount(postSettingVO.getTotalCount());


        Integer minWeeklyReason = shiftModel.getMinWeeklyReason() / 60;

        if(minWeeklyReason != null) {
            result.setMin_post_week(minWeeklyReason);

            if(result.getAvgHourWeekly() < minWeeklyReason) {
                flag = false;
            }
        }

        Integer maxWeeklyReason = shiftModel.getMaxWeeklyReason() / 60;
        if(maxWeeklyReason != null) {
            result.setMax_post_week(maxWeeklyReason);

            if(result.getAvgHourWeekly() > maxWeeklyReason) {
                flag = false;
            }
        }

        Integer currPostMonth = shiftModel.getPostMonth() / 60;
        if (result.getAvgHourMonth() > currPostMonth) {
            flag = false;
        }

        Integer currPostYear = shiftModel.getPostYear() / 60;
        if (result.getAvgHourYear() > currPostYear) {
            flag = false;
        }

        result.setPostMonth(currPostMonth);
        result.setPostYear(currPostYear);

        if (flag) {
            result.setStatusWorkHour("success");
        } else {
            result.setStatusWorkHour("fail");
        }

        //****************校验值班人数表和班次表*********************
        Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        //将分成30分钟的每个时段的人数加入到集合中
        for (ShiftSetting ss : shiftSettingList) {
            Integer startAt = ss.getStartAt();
            Integer endAt = ss.getEndAt();

            Integer startNum = startAt / 30;
            //为了转换为时间段将截至时间-1
            //这里需要处理endNum为0的情况
            Integer endNum = endAt / 30 - 1;

            Integer shiftNum = ss.getShiftNum();

            for (int i = startNum; i <= endNum; i++) {
                if (map.containsKey(i)) {
                    map.put(i, shiftNum + map.get(i));
                } else {
                    map.put(i, shiftNum);
                }
            }
        }

        //将值班人数表的时间段人数放入集合中
        ShiftPopulationVO shiftPopulationVO = new ShiftPopulationVO();
        BeanUtils.copyProperties(postSettingVO, shiftPopulationVO);

        List<ShiftPopulationVO> shiftPopulationVOList = shiftPopulationService.getShiftPopulation(shiftPopulationVO);

        Map<Integer, Integer> map1 = new HashMap<Integer, Integer>();

        for (ShiftPopulationVO sp : shiftPopulationVOList) {
            Integer startAt = sp.getStartAt();
            Integer endAt = sp.getEndAt();

            Integer startNum = startAt / 30;
            //为了转换为时间段将截至时间-1
            //这里需要处理endNum为0的情况
            Integer endNum = endAt / 30 - 1;

            Integer populationCount = sp.getPopulationCount();

            for (int i = startNum; i <= endNum; i++) {
                if (map1.containsKey(i)) {
                    map1.put(i, populationCount + map.get(i));
                } else {
                    map1.put(i, populationCount);
                }
            }
        }

        String statusPopulationAndSetting = "success";

        Set<Integer> set = map1.keySet();

        //保存出问题的时间段
        Set<Integer> resultSet = new TreeSet<Integer>();

        for (Integer key : set) {
            if (map.get(key) != null) {
                if (!map.get(key).equals(map1.get(key))) {
                    statusPopulationAndSetting = "fail";
                    resultSet.add(key);
                }
            } else {
                statusPopulationAndSetting = "fail";
                resultSet.add(key);
            }
        }

        result.setStatusPopulationAndSetting(statusPopulationAndSetting);

        List<String> list = new ArrayList<String>();

        if(resultSet.size() > 0) {
            Integer[] array = new Integer[resultSet.size()];
            resultSet.toArray(array);

            Integer[][] arrays = new Integer[array.length][array.length];
            int i = 0;
            int j = 0;

            int max = array[0] - 1;
            for (int k = 0; k < array.length; k++) {
                if (array[k] != max + 1) {
                    i++;
                    j = 0;
                    max = array[k] - 1;
                }
                arrays[i][j] = array[k];
                max = array[k];
                j++;
            }

            for (int p = 0; p < arrays.length; p++) {
                if (arrays[p] != null) {
                    int index = -1;
                    for (int l = 0; l < arrays[p].length; l++) {
                        if (arrays[p][l] != null) {
                            index = l;
                        }
                    }
                    if (index != -1) {
                        Integer start = arrays[p][0];
                        Integer end = arrays[p][index];

                        String startStr = DateUtil.startMinuToTime(start);
                        String endStr = DateUtil.endMinuToTime(end);

                        list.add(startStr + "-" + endStr);
                    }
                }
            }
            result.setFailTime(list);
        }

        String status = "fail";
        if("success".equals(result.getStatusAvgWeeklyRest()) && "success".equals(result.getStatusPopulationAndSetting()) && "success".equals(result.getStatusWorkHour())) {
            status = "success";
        }
        result.setStatus(status);
        return result;
    }
}
