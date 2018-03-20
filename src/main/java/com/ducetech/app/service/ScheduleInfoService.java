package com.ducetech.app.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.ducetech.app.model.ScheduleInfo;
import com.ducetech.app.model.User;
import com.ducetech.app.model.WorkflowContent;
import com.ducetech.app.model.vo.ChangeShiftVO;
import com.ducetech.app.support.domain.CalculateResult;
import org.apache.ibatis.annotations.Param;


public interface ScheduleInfoService {

	/**
	* @Title: selectScheduleInfo
	* @return List<selectScheduleInfo>
	* @Description: selectScheduleInfo
	*/
    int selectScheduleInfoSumDay(String startAt, String endAt,String userId);
    int selectScheduleInfoSum(String startAt,String endAt,String userId);
	List<ScheduleInfo> selectScheduleInfo(String startAt,String endAt,String userName,String userCode,String postName,String station,String stationArea);
    List<ScheduleInfo> selectScheduleInfoByUser(String startAt,String endAt,String userId);
    void updateScheduleInfo(ScheduleInfo info);
    ScheduleInfo selectScheduleInfoById(String scheduleInfoId);
    void addScheduleInfo(ScheduleInfo scheduleInfo);
    List<ScheduleInfo> selectPreviousRelevant(Date date,String modelId) throws ParseException;
    void insertSchedule(ScheduleInfo schedule) throws ParseException;
    void deleteAreaScheduleInfo(String startAt,String endAt,String userName);
    Integer selectScheduleInfoGroup(String startAt, String endAt,String userId,String shiftName);

    void updateScheduleInfoChangeShift(ChangeShiftVO changeShiftVO);

    void changeData(String modelId, String id1, String id2);

    void saveSchedule(String modelId, JSONObject weeks, String groupName, String postName, String userIds, String startAt, User loginUser) throws ParseException;

    void changeScheduleInfo(String id1, String id2, User loginUser);

    List<WorkflowContent> getWorkflowContentByCode(String shiftId,String text);
}