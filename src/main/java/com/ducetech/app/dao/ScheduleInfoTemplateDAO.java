package com.ducetech.app.dao;

import com.ducetech.app.model.ScheduleInfoTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduleInfoTemplateDAO {

    List<ScheduleInfoTemplate> selectScheduleInfoTemplateByModelId(String modelId);

    void insertScheduleInfoTemplate(ScheduleInfoTemplate scheduleInfoTemplate);

    Integer updateScheduleInfoTemplate(ScheduleInfoTemplate scheduleInfoTemplate);

    void deleteByModelId(String modelId);

    ScheduleInfoTemplate selectByModelIdAndWeek(@Param("modelId") String modelId,@Param("weekNumber")  int weekNumber, @Param("weekDay") int weekDay);
}
