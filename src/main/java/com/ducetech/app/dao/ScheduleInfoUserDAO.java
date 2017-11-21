package com.ducetech.app.dao;

import com.ducetech.app.model.ScheduleInfoUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduleInfoUserDAO {

    ScheduleInfoUser selectById(Long id);
    List<ScheduleInfoUser> selectByModel(String modelId);
    ScheduleInfoUser selectByModelAndWeek(@Param("modelId") String modelId, @Param("weekNumber") int weekNumber);
    void insert(ScheduleInfoUser scheduleInfoUser);
    void deleteById(Long id);

    ScheduleInfoUser selectByModelAndUser(@Param("modelId")String modelId, @Param("userId")String userId);
}
