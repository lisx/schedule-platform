package com.ducetech.app.service;

import com.ducetech.app.model.ScheduleInfo;
import com.ducetech.app.model.ScheduleLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ScheduleLogService {

	/**
	* @Title: selectScheduleInfo
	* @return List<selectScheduleInfo>
	* @Description: selectScheduleInfo
	*/

    int selectScheduleLogSum(String scheduleLogId);
	List<ScheduleLog> selectScheduleLog(String startAt, String endAt);
    List<ScheduleLog> selectScheduleLogByUserId(String startAt, String endAt,String userId);
    void insertScheduleLog(ScheduleLog log);
    void updateScheduleLog(ScheduleLog log);
    List<ScheduleLog> getScheduleLogByInfo(String scheduleInfoId);
    List<ScheduleLog> getScheduleLogByInfoAndLogId(String scheduleInfoId,String scheduleLogId);
    ScheduleLog getScheduleLogById(String scheduleLogId);
}