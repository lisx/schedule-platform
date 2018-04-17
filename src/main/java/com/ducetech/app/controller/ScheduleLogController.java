package com.ducetech.app.controller;

import com.ducetech.app.model.*;
import com.ducetech.app.service.*;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.schedule.service.SystemSechduleService;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 排班日志
 */
@Controller
public class ScheduleLogController extends BaseController {
    private final static Logger logger = LoggerFactory.getLogger(SystemSechduleService.class);
    @Autowired
    private ScheduleLogService scheduleLogService;
    @Autowired
    private ScheduleInfoService scheduleInfoService;
    @Autowired
    private ShiftModelService shiftModelService;
    @Autowired
    private ShiftSettingService shiftService;
    @Autowired
    private UserService userService;

    /**
     * 获取单个排班所以编辑记录
     * @param scheduleInfoId
     * @return
     */
    @RequestMapping(value = "/getScheduleLogByInfo", method = RequestMethod.POST)
    @ResponseBody
    public List<ScheduleLog> getScheduleLogByInfo(String scheduleInfoId){
        List<ScheduleLog> logs=scheduleLogService.getScheduleLogByInfo(scheduleInfoId);
        return logs;
    }

    /**
     * 假期编辑
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/editHolidayForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editHolidayForm(ScheduleLog log, HttpServletRequest request) {
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        log.setLogType(log.getLogType()+"-"+log.getDetailType());
        List<ScheduleInfo> sis=scheduleInfoService.selectScheduleInfoByUser(log.getStartAt(),log.getEndAt(),log.getUserId());
        if(null!=sis&&sis.size()>0) {
            log.setScheduleInfoId(sis.get(0).getScheduleInfoId());
            log.setUserName(sis.get(0).getUserName());
        }
        scheduleLogService.insertScheduleLog(log);
        int gh=0;
        for(ScheduleInfo s:sis){
            s.setIfLeave(1);
            s.setLeaveType(log.getLogType());
            s.setScheduleDesc(log.getRemark());
            s.setLogId(log.getScheduleLogId());
            if(null!=s.getTotalAt()) {
                gh += s.getTotalAt();
            }
            List<ScheduleLog> list=scheduleLogService.getScheduleLogByInfoAndLogId(s.getScheduleInfoId(),log.getScheduleLogId());
            for(ScheduleLog slog:list){
                slog.setIfUse(1);
                scheduleLogService.updateScheduleLog(slog);
            }
            scheduleInfoService.updateScheduleInfo(s);
        }
        log.setTimeAt(-gh);

        scheduleLogService.updateScheduleLog(log);
        return OperationResult.buildSuccessResult("假期编辑成功", "success");
    }

    /**
     * 班次变更
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/shiftChangeForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult shiftChangeForm(ScheduleLog log, HttpServletRequest request) {
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        log.setRemark(log.getRemark()==null?"":log.getRemark());
        ScheduleInfo s =scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        ShiftModel sm=shiftModelService.selectShiftModelByModelCodeId("",s.getModelId());
        List<ShiftSetting> ssList=shiftService.selectShiftSettingByModelId(sm.getModelId());
        for(ShiftSetting ss:ssList){
            if(ss.getShiftName().equals(log.getDetailType())){
                log.setTimeAt(ss.getTotalAt()-s.getTotalAt());
            }
        }
        log.setLogType(s.getShiftName()+"变更为"+log.getDetailType());
        log.setUserName(s.getUserName());
        log.setStartAt(DateUtil.formatDate(s.getScheduleDate(),"yyyy-MM-dd"));
        log.setEndAt(DateUtil.formatDate(s.getScheduleDate(),"yyyy-MM-dd"));
        scheduleLogService.insertScheduleLog(log);
        List<ScheduleLog> list=scheduleLogService.getScheduleLogByInfoAndLogId(s.getScheduleInfoId(),log.getScheduleLogId());
        for(ScheduleLog slog:list){
            slog.setIfUse(1);
            scheduleLogService.updateScheduleLog(slog);
        }
        s.setIfLeave(2);
        s.setLogId(log.getScheduleLogId());
        s.setShiftName(log.getDetailType());
        s.setLeaveType(log.getLogType());
        s.setScheduleDesc(log.getRemark());
        scheduleInfoService.updateScheduleInfo(s);
        return OperationResult.buildSuccessResult("班次变更成功", "success");
    }

    /**
     * 临时安排
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/provisionalDispositionForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult provisionalDispositionForm(ScheduleLog log, HttpServletRequest request) {
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setTimeAt(log.getTimeAt()*60);
        log.setIfUse(0);
        log.setLogType(log.getLogType()+"-"+log.getDetailType()+(log.getTimeAt()/60)+"小时");
        scheduleLogService.insertScheduleLog(log);
        ScheduleInfo s =scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        s.setLogId(log.getScheduleLogId());
        s.setIfLeave(3);
        scheduleInfoService.updateScheduleInfo(s);
        log.setScheduleInfoId(s.getScheduleInfoId());
        log.setUserName(s.getUserName());
        scheduleLogService.updateScheduleLog(log);
        return OperationResult.buildSuccessResult("临时安排成功", "success");
    }

    /**
     * 旷工缺勤
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/minersAbsenceForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult minersAbsenceForm(ScheduleLog log,String logId, HttpServletRequest request) {
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setTimeAt(-log.getTimeAt()*60);
        log.setIfUse(0);
        log.setLogType(log.getLogType()+(log.getTimeAt()/60)+"小时");
        scheduleLogService.insertScheduleLog(log);
        ScheduleInfo s =scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        s.setLogId(log.getScheduleLogId());
        s.setIfLeave(4);
        scheduleInfoService.updateScheduleInfo(s);
        log.setScheduleInfoId(s.getScheduleInfoId());
        log.setUserName(s.getUserName());
        scheduleLogService.updateScheduleLog(log);
        return OperationResult.buildSuccessResult("旷工缺勤成功", "success");
    }

    /**
     * 补班加班
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/overtimeWorkForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult overtimeWorkForm(ScheduleLog log,String logId, HttpServletRequest request) {
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setTimeAt(log.getTimeAt()*60);
        log.setIfUse(0);
        log.setLogType(log.getLogType()+(log.getTimeAt()/60)+"小时");
        scheduleLogService.insertScheduleLog(log);
        ScheduleInfo s =scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        s.setLogId(log.getScheduleLogId());
        s.setIfLeave(5);
        scheduleInfoService.updateScheduleInfo(s);
        log.setUserName(s.getUserName());
        log.setScheduleInfoId(s.getScheduleInfoId());
        scheduleLogService.updateScheduleLog(log);
        return OperationResult.buildSuccessResult("补班加班成功", "success");
    }

    /**
     * 替班
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/editReplaceShiftForm", method = RequestMethod.POST)
    @ResponseBody
    public OperationResult editReplaceShiftForm(ScheduleLog log, HttpServletRequest request) {
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        String startAt = log.getStartAt();
        String endAt = log.getEndAt();
        if(!StringUtils.isBlank(startAt)&&!StringUtils.isBlank(endAt)) {
            int gh = Math.abs(DateUtil.timeToMinu(startAt) - DateUtil.timeToMinu(endAt));
            log.setTimeAt(-gh);
        }else{
            return OperationResult.buildSuccessResult("替班编辑失败,请选择时间.", "error");
        }
        ScheduleInfo scheduleInfo = scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        ScheduleInfo replace=scheduleInfoService.selectInfoReplace(log.getDetailType(),scheduleInfo.getScheduleDay());
        User user=userService.getUserByUserId(log.getDetailType());
        if(null!=replace){
            replace.setShiftName(scheduleInfo.getShiftName());
            //replace.setSerialNumber(scheduleInfo.getSerialNumber());
            replace.setShiftCode(scheduleInfo.getShiftCode());
            replace.setShiftColor(scheduleInfo.getShiftColor());
            replace.setTotalAt(scheduleInfo.getTotalAt());
            scheduleInfoService.updateScheduleInfo(replace);
        }else{
            replace=new ScheduleInfo();
            replace.setScheduleInfoId("");
            replace.setUserName(user.getUserName());
            replace.setUserId(user.getUserId());
            replace.setUserCode(user.getUserCode());
            replace.setShiftName(scheduleInfo.getShiftName());
            //replace.setSerialNumber(scheduleInfo.getSerialNumber());
            replace.setShiftCode(scheduleInfo.getShiftCode());
            replace.setShiftColor(scheduleInfo.getShiftColor());
            replace.setTotalAt(scheduleInfo.getTotalAt());
            try {
                scheduleInfoService.insertSchedule(replace);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        log.setUserName(scheduleInfo.getUserName());
        log.setLogType(user.getUserName()+log.getLogType()+log.getUserName());
        scheduleLogService.insertScheduleLog(log);
        scheduleInfo.setIfLeave(6);
        scheduleInfo.setLeaveType(log.getLogType());
        scheduleInfo.setScheduleDesc(log.getRemark());
        scheduleInfo.setLogId(log.getScheduleLogId());
        scheduleInfoService.updateScheduleInfo(scheduleInfo);

        scheduleLogService.updateScheduleLog(log);
        ScheduleLog rlog=new ScheduleLog();
        try {
            BeanUtils.copyProperties(rlog,log);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        rlog.setScheduleLogId("");
        rlog.setUserId(user.getUserId());
        rlog.setUserName(user.getUserName());
        rlog.setScheduleInfoId(replace.getScheduleInfoId());
        rlog.setTimeAt(-rlog.getTimeAt());
        scheduleLogService.insertScheduleLog(rlog);
        replace.setIfLeave(6);
        replace.setLeaveType(rlog.getLogType());
        replace.setScheduleDesc(rlog.getRemark());
        replace.setLogId(rlog.getScheduleLogId());
        scheduleInfoService.updateScheduleInfo(replace);
        return OperationResult.buildSuccessResult("替班编辑成功", "success");
    }

    /**
     * 离职
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/editLeaveShiftForm", method = RequestMethod.POST)
    @ResponseBody

    public OperationResult editLeaveShiftForm(ScheduleLog log, HttpServletRequest request) throws ParseException {
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        User user=userService.getUserByUserId(log.getDetailType());
        scheduleInfoService.setUserLeave(log.getUserId(),"0");
        scheduleInfoService.leaveUser(log.getScheduleInfoId(),user.getUserName(),user.getUserCode(),user.getUserId(),log.getUserId());
        scheduleInfoService.setUserLeave(user.getUserId(),"1");
        log.setUserName(user.getUserName());
        log.setLogType(log.getUserName()+log.getLogType()+user.getUserName());
        scheduleLogService.insertScheduleLog(log);
        return OperationResult.buildSuccessResult("离职编辑成功", "success");
    }

    /**
     * 撤销按钮
     * @param infoId
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/formRevoke", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult formRevoke(String  infoId) throws ParseException {
        List<ScheduleLog> list=scheduleLogService.getScheduleLogByInfo(infoId);
        if(list!=null&&list.size()>0) {
            for (ScheduleLog slog : list) {
                slog.setIfUse(1);
                scheduleLogService.updateScheduleLog(slog);
                List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfoNotInfoId(slog.getScheduleLogId());
                for (ScheduleInfo info : sis) {
                    info.setIfLeave(0);
                    info.setLogId("");
                    info.setLeaveType("");
                    info.setScheduleDesc("");
                    scheduleInfoService.updateScheduleInfo(info);
                }
            }
        }else{
            ScheduleInfo scheduleInfo=scheduleInfoService.selectScheduleInfoById(infoId);
            ScheduleLog log=scheduleLogService.getScheduleLogById(scheduleInfo.getLogId());
            log.setIfUse(1);
            scheduleLogService.updateScheduleLog(log);
            List<ScheduleInfo> sis = scheduleInfoService.selectScheduleInfoNotInfoId(scheduleInfo.getLogId());
            for (ScheduleInfo info : sis) {
                info.setIfLeave(0);
                info.setLogId("");
                info.setLeaveType("");
                info.setScheduleDesc("");
                scheduleInfoService.updateScheduleInfo(info);
            }
        }
        return OperationResult.buildSuccessResult("撤销安排成功", "success");
    }
}
