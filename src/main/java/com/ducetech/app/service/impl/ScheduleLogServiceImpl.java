package com.ducetech.app.service.impl;

import com.ducetech.app.dao.ScheduleLogDAO;
import com.ducetech.app.model.ScheduleLog;
import com.ducetech.app.service.ScheduleLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ScheduleLogServiceImpl implements ScheduleLogService {

    @Autowired
    private ScheduleLogDAO scheduleLogDAO;


    @Override
    public int selectScheduleLogSum(String scheduleLogId) {
        return scheduleLogDAO.selectScheduleLogSum(scheduleLogId);
    }

    @Override
    public List<ScheduleLog> selectScheduleLog(String startAt, String endAt) {
        return scheduleLogDAO.selectScheduleLog(startAt,endAt);
    }

    @Override
    public List<ScheduleLog> selectScheduleLogByUserId(String startAt, String endAt, String userId) {
        return scheduleLogDAO.selectScheduleLogByUserId(startAt,endAt,userId);
    }

    @Override
    public void insertScheduleLog(ScheduleLog log) {
        scheduleLogDAO.insertScheduleLog(log);
    }

    @Override
    public void updateScheduleLog(ScheduleLog log) {
        scheduleLogDAO.updateScheduleLog(log);
    }

    @Override
    public List<ScheduleLog> getScheduleLogByInfo(String scheduleInfoId) {
        return scheduleLogDAO.getScheduleLogByInfo(scheduleInfoId);
    }

    @Override
    public List<ScheduleLog> getScheduleLogByInfoAndLogId(String scheduleInfoId, String scheduleLogId) {
        return scheduleLogDAO.getScheduleLogByInfoAndLogId(scheduleInfoId,scheduleLogId);
    }

    @Override
    public ScheduleLog getScheduleLogById(String scheduleLogId) {
        return scheduleLogDAO.getScheduleLogById(scheduleLogId);
    }
}