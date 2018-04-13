package com.ducetech.app.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ducetech.app.model.vo.ChangeShiftVO;
import org.apache.ibatis.annotations.Param;

import com.ducetech.app.model.ScheduleInfo;


public interface ScheduleInfoDAO {

	/**
	* @Title: selectScheduleInfo
	* @return List<selectScheduleInfo>
	* @Description: selectScheduleInfo
	*/
    int selectScheduleInfoSumDay(@Param("startAt") String startAt,@Param("endAt") String endAt,@Param("userId")String userId);
    int selectScheduleInfoSum(@Param("startAt") String startAt,@Param("endAt") String endAt,@Param("userId")String userId);
    List<ScheduleInfo> selectScheduleInfoNotInfoId(@Param("logId") String logId);
    List<ScheduleInfo> selectScheduleInfoSort(@Param("startAt") String startAt,@Param("endAt") String endAt,@Param("userName") String userName,@Param("userCode") String userCode,@Param("postName") String userJob,@Param("station") String station,@Param("stationArea") String stationArea);
	List<ScheduleInfo> selectScheduleInfo(@Param("startAt") String startAt,@Param("endAt") String endAt,@Param("userName") String userName,@Param("userCode") String userCode,@Param("postName") String userJob,@Param("station") String station,@Param("stationArea") String stationArea);
    List<ScheduleInfo> selectScheduleInfoByUser(@Param("startAt") String startAt,@Param("endAt") String endAt,@Param("userId")String userId);
    List<ScheduleInfo> selectFutureScheduleInfoByUser(@Param("startAt") Date startAt,@Param("userId")String userId);
    void updateScheduleInfo(ScheduleInfo info);
    ScheduleInfo selectScheduleInfoById(@Param("scheduleInfoId") String scheduleInfoId);
	void insertScheduleInfo (ScheduleInfo scheduleInfo);
	List<ScheduleInfo> getRelevanceInfo(@Param("date") String date, @Param("idList") List<String> idList);
	void deleteExistInfo(@Param("modelId") String modelId,@Param("groupName") String groupName, @Param("scheduleDate")Date scheduleDate);
    void deleteAreaScheduleInfo(@Param("startAt") String startAt,@Param("endAt") String endAt,@Param("userName")String userName);
    Integer selectScheduleInfoGroup(@Param("startAt") String startAt,@Param("endAt") String endAt,@Param("userId")String userId,@Param("shiftName")String shiftName);

	void updateScheduleInfoChangeShift(ChangeShiftVO changeShiftVO);

    void insertBatch(@Param("batch")List<ScheduleInfo> batch);

	void changeUser(ScheduleInfo info);

    Integer leaveUser(@Param("scheduleInfoId") String scheduleInfoId,@Param("userName") String userName,@Param("userCode") String userCode,@Param("userId") String userId,@Param("oldUser") String oldUser);
    Integer setUserLeave(@Param("userId")String userId,@Param("userLeave")String userLeave);
    ScheduleInfo selectInfoReplace(@Param("userId")String userId,@Param("scheduleDay")String scheduleDay);
}