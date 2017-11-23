package com.ducetech.app.service;

import com.ducetech.app.model.ScheduleInfoTemplate;
import com.ducetech.app.model.User;

import java.util.List;

public interface ScheduleInfoTemplateService {
    List<ScheduleInfoTemplate> selectScheduleInfoTemplateByModelId(String modelId);

    int insertScheduleInfoTemplateList(String modelId,List<ScheduleInfoTemplate> scheduleInfoTemplateList, User user);

    Integer updateScheduleInfoTemplate(ScheduleInfoTemplate scheduleInfoTemplate);

    void removeByModelAndWeek(String modelId, int weekNumber);
}
