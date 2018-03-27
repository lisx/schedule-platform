package com.ducetech.app.controller;

import com.ducetech.app.model.*;
import com.ducetech.app.service.*;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.schedule.service.SystemSechduleService;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.web.view.OperationResult;
import org.apache.poi.util.SystemOutLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.text.DateFormat;
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
        ScheduleInfo scheduleInfo = scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        log.setScheduleInfoId(scheduleInfo.getScheduleInfoId());
        log.setUserName(scheduleInfo.getUserName());
        scheduleLogService.insertScheduleLog(log);
        scheduleInfo.setIfLeave(6);
        scheduleInfo.setLeaveType(log.getLogType());
        scheduleInfo.setScheduleDesc(log.getRemark());
        scheduleInfo.setLogId(log.getScheduleLogId());
        scheduleInfoService.updateScheduleInfo(scheduleInfo);
        String startAt = log.getStartAt();
        String endAt = log.getEndAt();

        int gh = Math.abs(DateUtil.timeToMinu(startAt) - DateUtil.timeToMinu(endAt));

        log.setTimeAt(-gh);
        scheduleLogService.updateScheduleLog(log);
        return OperationResult.buildSuccessResult("假期编辑成功", "success");
    }


    public static int compare_date(String DATE1, String DATE2) {
        DateFormat df = new SimpleDateFormat("yyyy-M-dd");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                return -1;
            } else if(dt1.getTime() == dt2.getTime()){
                return 2;
            }else{
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    /**
     * 撤销假期
     * @param log
     * @param logId
     * @param request
     * @return
     * @throws ParseException
     */
    @RequestMapping(value = "/editHolidayFormRevoke", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult editHolidayFormRevoke(ScheduleLog log,String logId, HttpServletRequest request) throws ParseException {
        User userInfo = getLoginUser(request);
        ScheduleLog temp=scheduleLogService.getScheduleLogById(logId);
        log.setScheduleInfoId(temp.getScheduleInfoId());
        temp.setIfUse(1);
        scheduleLogService.updateScheduleLog(temp);
        DateFormat df = new SimpleDateFormat("yyyy-M-dd");
        int start=compare_date(log.getStartAt(), temp.getStartAt());
        int end=compare_date(log.getEndAt(), temp.getEndAt());
        int sum;
        Date date;
        Calendar calendar = new GregorianCalendar();
        log.setLogType("撤销"+log.getLogType());
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        log.setUserName(temp.getUserName());
        scheduleLogService.insertScheduleLog(log);
        if(start==1&&end==-1){
            String delend=temp.getEndAt();
            date=df.parse(log.getStartAt());
            calendar.setTime(date);
            calendar.add(calendar.DATE,-1);//把日期往后增加一天.整数往后推,负数往前移动
            date=calendar.getTime();   //这个时间就是日期往后推一天的结果
            String endAt=DateUtil.formatDate(date, "yyyy-M-dd");
            temp.setEndAt(endAt);
            sum=scheduleInfoService.selectScheduleInfoSum(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            temp.setTimeAt(-sum);
            temp.setIfUse(0);
            scheduleLogService.insertScheduleLog(temp);
            List<ScheduleInfo> slist=scheduleInfoService.selectScheduleInfoByUser(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            setLogId(temp, slist);
            temp.setEndAt(delend);
            date=df.parse(log.getEndAt());
            calendar.setTime(date);
            calendar.add(calendar.DATE,1);
            date=calendar.getTime();   //这个时间就是日期往后推一天的结果
            String startAt=DateUtil.formatDate(date, "yyyy-M-dd");
            temp.setStartAt(startAt);
            temp.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
            temp.setCreatorId(userInfo.getUserId());
            temp.setCreatorName(userInfo.getUserName());
            temp.setIfUse(0);
            sum=scheduleInfoService.selectScheduleInfoSum(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            temp.setTimeAt(-sum);
            slist=scheduleInfoService.selectScheduleInfoByUser(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            if(slist!=null&&slist.size()>0){
                temp.setScheduleInfoId(slist.get(0).getScheduleInfoId());
            }

            scheduleLogService.insertScheduleLog(temp);
            if(slist!=null&&slist.size()>0)
            for(ScheduleInfo info:slist){
                info.setLogId(temp.getScheduleLogId());
                scheduleInfoService.updateScheduleInfo(info);
            }
        }else if(start==-1&&end==1){
            log.setStartAt(temp.getStartAt());
            log.setEndAt(temp.getEndAt());
        }else if(start==2&&end==-1){
            date=df.parse(log.getEndAt());
            calendar.setTime(date);
            calendar.add(calendar.DATE,1);
            date=calendar.getTime();   //这个时间就是日期往后推一天的结果
            String startEnd=DateUtil.formatDate(date, "yyyy-M-dd");
            temp.setStartAt(startEnd);
            List<ScheduleInfo> slist=scheduleInfoService.selectScheduleInfoByUser(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            setLogId(temp, slist);
            temp.setIfUse(0);
            sum=scheduleInfoService.selectScheduleInfoSum(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            temp.setTimeAt(-sum);
            scheduleLogService.insertScheduleLog(temp);
        }else if(start==1&&end==2){
            date=df.parse(log.getEndAt());
            calendar.setTime(date);
            calendar.add(calendar.DATE,-1);
            date=calendar.getTime();   //这个时间就是日期往后推一天的结果
            String startEnd=DateUtil.formatDate(date, "yyyy-M-dd");
            temp.setEndAt(startEnd);
            temp.setIfUse(0);
            sum=scheduleInfoService.selectScheduleInfoSum(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            temp.setTimeAt(-sum);
            scheduleLogService.insertScheduleLog(temp);
            List<ScheduleInfo> slist=scheduleInfoService.selectScheduleInfoByUser(temp.getStartAt(),temp.getEndAt(),temp.getUserId());
            setLogId(temp, slist);
        }

        List<ScheduleInfo> sis=scheduleInfoService.selectScheduleInfoByUser(log.getStartAt(),log.getEndAt(),log.getUserId());
        for(ScheduleInfo s:sis){
            s.setIfLeave(0);
            s.setLogId("");
            s.setLeaveType("");
            s.setScheduleDesc("");
            scheduleInfoService.updateScheduleInfo(s);
        }
        return OperationResult.buildSuccessResult("假期编辑成功", "success");
    }

    private void setLogId(ScheduleLog temp, List<ScheduleInfo> slist) {
        if(slist!=null&&slist.size()>0){
            temp.setScheduleInfoId(slist.get(0).getScheduleInfoId());
            for(ScheduleInfo info:slist){
                info.setLogId(temp.getScheduleLogId());
                scheduleInfoService.updateScheduleInfo(info);
            }
        }
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
        log.setLogType(log.getLogType()+"-"+log.getDetailType());
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
     * 撤销临时安排
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/provisionalDispositionFormRevoke", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult provisionalDispositionFormRevoke(ScheduleLog log,String logId, HttpServletRequest request) {
        ScheduleLog temp=scheduleLogService.getScheduleLogById(logId);
        log.setScheduleInfoId(temp.getScheduleInfoId());
        temp.setIfUse(1);
        scheduleLogService.updateScheduleLog(temp);
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        log.setLogType("撤销"+log.getLogType());
        log.setUserName(temp.getUserName());
        scheduleLogService.insertScheduleLog(log);
        ScheduleInfo s =scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        s.setLogId(log.getScheduleLogId());
        scheduleInfoService.updateScheduleInfo(s);
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
     * 撤销旷工缺勤
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/minersAbsenceFormRevoke", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult minersAbsenceFormRevoke(ScheduleLog log,String logId, HttpServletRequest request) {
        ScheduleLog temp=scheduleLogService.getScheduleLogById(logId);
        log.setScheduleInfoId(temp.getScheduleInfoId());
        temp.setIfUse(1);
        scheduleLogService.updateScheduleLog(temp);
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        log.setLogType("撤销"+log.getLogType());
        log.setUserName(temp.getUserName());
        scheduleLogService.insertScheduleLog(log);
        ScheduleInfo s =scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        s.setLogId(log.getScheduleLogId());
        scheduleInfoService.updateScheduleInfo(s);
        return OperationResult.buildSuccessResult("撤销旷工缺勤成功", "success");
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
     * 撤销补班加班
     * @param log
     * @param request
     * @return
     */
    @RequestMapping(value = "/overtimeWorkFormRevoke", method = RequestMethod.DELETE)
    @ResponseBody
    public OperationResult overtimeWorkFormRevoke(ScheduleLog log,String logId, HttpServletRequest request) {
        ScheduleLog temp=scheduleLogService.getScheduleLogById(logId);
        log.setScheduleInfoId(temp.getScheduleInfoId());
        temp.setIfUse(1);
        scheduleLogService.updateScheduleLog(temp);
        User userInfo = getLoginUser(request);
        log.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        log.setCreatorId(userInfo.getUserId());
        log.setCreatorName(userInfo.getUserName());
        log.setIfUse(0);
        log.setLogType("撤销"+log.getLogType());
        log.setUserName(temp.getUserName());
        scheduleLogService.insertScheduleLog(log);
        ScheduleInfo s =scheduleInfoService.selectScheduleInfoById(log.getScheduleInfoId());
        s.setLogId(log.getScheduleLogId());
        scheduleInfoService.updateScheduleInfo(s);
        return OperationResult.buildSuccessResult("撤销补班加班成功", "success");
    }
}
