package com.ducetech.app.dao;

import com.ducetech.app.model.ScheduleLog;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ScheduleLogDAO {

	/**
	* @Title: selectScheduleLog
	* @return List<ScheduleLog>
	* @Description: selectScheduleLog
	*/
    int selectScheduleLogSum(@Param("scheduleInfoId") String scheduleInfoId);
	List<ScheduleLog> selectScheduleLog(@Param("startAt") String startAt, @Param("endAt") String endAt);
    List<ScheduleLog> selectScheduleLogByUserId(@Param("startAt") String startAt, @Param("endAt") String endAt, @Param("userId") String userId);
    void insertScheduleLog(ScheduleLog log);
    void updateScheduleLog(ScheduleLog log);
    List<ScheduleLog> getScheduleLogByInfo(@Param("scheduleInfoId") String scheduleInfoId);
    List<ScheduleLog> getScheduleLogByInfoAndLogId(@Param("scheduleInfoId") String scheduleInfoId,@Param("scheduleLogId") String scheduleLogId);
    ScheduleLog getScheduleLogById(@Param("scheduleLogId") String scheduleLogId);
}