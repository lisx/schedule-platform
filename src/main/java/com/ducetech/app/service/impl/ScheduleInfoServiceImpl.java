package com.ducetech.app.service.impl;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ducetech.app.dao.*;
import com.ducetech.app.model.*;
import com.ducetech.app.model.vo.ChangeShiftVO;
import com.ducetech.app.service.ScheduleInfoService;
import com.ducetech.framework.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ScheduleInfoServiceImpl implements ScheduleInfoService {

    @Autowired
    private ScheduleInfoDAO scheduleDAO;
    @Autowired
    private ShiftSettingDAO shiftSettingDAO;
    @Autowired
    private ShiftModelDAO shiftModelDAO;
    @Autowired
    private ScheduleInfoTemplateDAO templateDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private WorkflowDAO workflowDAO;
    @Autowired
    private WorkflowContentDAO contentDAO;

    @Override
    public int selectScheduleInfoSumDay(String startAt, String endAt, String userId) {

        return scheduleDAO.selectScheduleInfoSumDay(startAt, endAt, userId);
    }

    @Override
    public int selectScheduleInfoSum(String startAt, String endAt, String userId) {
        return scheduleDAO.selectScheduleInfoSum(startAt, endAt, userId);
    }

    @Override
    public List<ScheduleInfo> selectScheduleInfo(String startAt, String endAt, String userName, String userCode, String postName, String station, String stationArea) {
        return scheduleDAO.selectScheduleInfo(startAt, endAt, userName, userCode, postName, station, stationArea);
    }


    @Override
    public List<ScheduleInfo> selectScheduleInfoByUser(String startAt, String endAt, String userId) {
        return scheduleDAO.selectScheduleInfoByUser(startAt, endAt, userId);
    }

    @Override
    public void updateScheduleInfo(ScheduleInfo info) {
        scheduleDAO.updateScheduleInfo(info);
    }

    @Override
    public ScheduleInfo selectScheduleInfoById(String ScheduleInfoId) {
        return scheduleDAO.selectScheduleInfoById(ScheduleInfoId);
    }

    @Override
    public void addScheduleInfo(ScheduleInfo scheduleInfo) {
        scheduleDAO.insertScheduleInfo(scheduleInfo);
    }


    @Override
    public List<ScheduleInfo> selectPreviousRelevant(Date date, String modelId) throws ParseException {
        List<ShiftSetting> list = shiftSettingDAO.selectShiftSettingByModelId(modelId);
        List<String> idList = new ArrayList<>();
        for (ShiftSetting s : list) {
            if (s.getRelevanceId() != null) {
                idList.add(s.getShiftId());
            }
        }
        if (idList.size() == 0) {
            return new ArrayList<>();
        }
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        long yesterday = date.getTime() - 86400 * 1000;
        String format = df.format(new Date(yesterday));
        return scheduleDAO.getRelevanceInfo(format, idList);
    }

    @Transactional
    public void insertSchedule(ScheduleInfo info) throws ParseException {
        scheduleDAO.insertScheduleInfo(info);
    }

    @Override
    @Transactional
    public void saveSchedule(String modelId, JSONObject weeks, String groupName, String postName, String userIds, String startAt, User loginUser) throws ParseException {
        DateFormat dayFormat=new SimpleDateFormat("yyyyMMdd");
        List<ShiftSetting> settings = shiftSettingDAO.selectShiftSettingByModelId(modelId);
        Map<String,ShiftSetting> shiftMap=new HashMap<>();
        Map<String,List<Workflow>> workFlowMap=new HashMap<>();
        Map<String,Integer> countMap=new HashMap<>();
        for (ShiftSetting s : settings) {
            shiftMap.put(s.getShiftId(),s);
            List<Workflow> workflows = workflowDAO.selectWorkflowsByShiftId(s.getShiftId());
            workFlowMap.put(s.getShiftId(),workflows);
            countMap.put(s.getShiftId(),0);
        }

        ShiftModel model=shiftModelDAO.selectShiftModelByModelCodeId(null,modelId);
        Date start = new SimpleDateFormat("yyyy-MM-dd").parse(startAt);
        scheduleDAO.deleteExistInfo(modelId, groupName, start);
        String [] userIdArr=userIds.split(",");
        List<ScheduleInfoTemplate> templates = templateDAO.selectScheduleInfoTemplateByModelId(modelId);
        Map<Integer,Map<Integer,ScheduleInfoTemplate>> weekly=new HashMap<>();
        for (ScheduleInfoTemplate t :templates) {
            if(!weekly.containsKey(t.getWeekNumber())){
                weekly.put(t.getWeekNumber(),new HashMap<Integer, ScheduleInfoTemplate>());
            }
            weekly.get(t.getWeekNumber()).put(t.getWeekDay(),t);
        }



        for (int i=0;i<userIdArr.length;i++){
            List<ScheduleInfo> batch=new ArrayList<>();
            String userId=userIdArr[i];
            User user = userDAO.selectUserByUserId(userId);
            for (int j=0;j<userIdArr.length;j++){
                int index=(j+i)%userIdArr.length;
                Map<Integer, ScheduleInfoTemplate> temp = weekly.get(index);
                JSONArray list=weeks.getJSONArray(String.valueOf(index));
                for (int w=0;w<7;w++) {
                    ScheduleInfoTemplate t = temp.get(w);
                    Date date=DateUtil.nDaysAfter(j*7+w,start);
                    ScheduleInfo info=new ScheduleInfo();
                    info.setGroupName(groupName);
                    info.setModelId(modelId);
                    if (t!=null){
                        ShiftSetting shift = shiftSettingDAO.editShiftSetting(t.getShiftId());
                        int count=countMap.get(t.getShiftId());
                        List<Workflow> workflows = workFlowMap.get(t.getShiftId());
                        if (workflows.size()>0){
                            Workflow workflow = workflows.get(count++ % workflows.size());
                            countMap.put(t.getShiftId(),count);
                            info.setSerialNumber(shift.getShiftCode()+workflow.getSerialNumber());
                        }
                        info.setSerialNumber(list.getString(w+1));
                        info.setShiftId(t.getShiftId());
                        info.setShiftName(t.getShiftName());
                        info.setTotalAt(t.getShiftMinutes());
                        info.setShiftColor(shift.getShiftColor());
                        info.setShiftCode(shift.getShiftCode());
                    }else {
                        info.setShiftName("休");
                    }
                    info.setScheduleDate(date);
                    info.setScheduleWeek(getWeekOfDate(date));
                    info.setScheduleDay(dayFormat.format(date));
                    info.setStationArea(model.getStationArea());
                    info.setStation(model.getStation());
                    info.setPostName(postName);
                    info.setUserId(userId);
                    if(null!=user) {
                        info.setUserName(user.getUserName());
                    }
                    info.setCreatedAt(dayFormat.format(date));
                    info.setCreatorId(loginUser.getUserId());
                    batch.add(info);
                }
            }
            scheduleDAO.insertBatch(batch);
        }
    }

    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"日", "一", "二", "三", "四", "五", "六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;
        return weekDays[w];
    }

    @Override
    public void changeScheduleInfo(String id1, String id2, User loginUser) {
        User user1 = userDAO.selectUserByUserId(id1);
        User user2 = userDAO.selectUserByUserId(id2);
        List<ScheduleInfo> infos1 = scheduleDAO.selectFutureScheduleInfoByUser(new Date(), id1);
        List<ScheduleInfo> infos2 = scheduleDAO.selectFutureScheduleInfoByUser(new Date(), id2);
        setNewUser(infos1,user2);
        setNewUser(infos2,user1);
    }

    @Override
    public List<WorkflowContent> getWorkflowContentByCode(String shiftId,String text) {
        List<Workflow> workflows = workflowDAO.selectWorkflowsByShiftId(shiftId);
        for (Workflow w :
                workflows) {
            if (text.endsWith(w.getSerialNumber())) {
                return contentDAO.selectWorkflowContentsByWorkflowId(w.getWorkflowId());
            }
        }
        return null;
    }

    private void setNewUser(List<ScheduleInfo> infoList,User user){
        for (ScheduleInfo info :
                infoList) {
            info.setUserId(user.getUserId());
            info.setUserName(user.getUserName());
            info.setUserCode(user.getUserCode());
            scheduleDAO.changeUser(info);
        }
    }


    @Override
    public void deleteAreaScheduleInfo(String startAt, String endAt, String userName) {
        scheduleDAO.deleteAreaScheduleInfo(startAt, endAt, userName);
    }

    @Override
    public Integer selectScheduleInfoGroup(String startAt, String endAt, String userId, String shiftName) {
        return scheduleDAO.selectScheduleInfoGroup(startAt, endAt, userId, shiftName);
    }

    @Override
    public void updateScheduleInfoChangeShift(ChangeShiftVO changeShiftVO) {
        scheduleDAO.updateScheduleInfoChangeShift(changeShiftVO);
    }

    @Override
    public void changeData(String modelId, String id1, String id2) {
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m1 = p.matcher(id1);
        Matcher m2 = p.matcher(id2);
        m1.find();
        int week1 = Integer.parseInt(m1.group());
        m1.find();
        int day1 = Integer.parseInt(m1.group());
        m2.find();
        int week2 = Integer.parseInt(m2.group());
        m2.find();
        int day2 = Integer.parseInt(m2.group());

        ScheduleInfoTemplate template1 = templateDAO.selectByModelIdAndWeek(modelId, week1,day1);
        ScheduleInfoTemplate template2 = templateDAO.selectByModelIdAndWeek(modelId, week2,day2);
        if (template1!=null){
            template1.setWeekNumber(week2);
            template1.setWeekDay(day2);
            templateDAO.updateScheduleInfoTemplate(template1);
        }
        if (template2!=null){
            template2.setWeekNumber(week1);
            template2.setWeekDay(day1);
            templateDAO.updateScheduleInfoTemplate(template2);
        }

    }

}