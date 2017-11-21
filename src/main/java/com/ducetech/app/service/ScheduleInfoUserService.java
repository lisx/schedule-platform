package com.ducetech.app.service;

import com.ducetech.app.model.ScheduleInfoTemplate;
import com.ducetech.app.model.ScheduleInfoUser;
import com.ducetech.app.model.User;

import java.util.List;

public interface ScheduleInfoUserService {
    List<ScheduleInfoUser> selectByModelId(String modelId);
    ScheduleInfoUser selectById(Long id);
    ScheduleInfoUser selectByModelAndWeek(String modelId,int weekNumber);
    void insert(ScheduleInfoUser scheduleInfoUser);
    void deleteById(Long id);

    ScheduleInfoUser selectByModelAndUser(String modelId, String userId);
}
