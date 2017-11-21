package com.ducetech.app.service.impl;

import com.ducetech.app.dao.ScheduleInfoTemplateDAO;
import com.ducetech.app.dao.ScheduleInfoUserDAO;
import com.ducetech.app.model.ScheduleInfoTemplate;
import com.ducetech.app.model.ScheduleInfoUser;
import com.ducetech.app.model.User;
import com.ducetech.app.service.ScheduleInfoTemplateService;
import com.ducetech.app.service.ScheduleInfoUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ScheduleInfoUserServiceImpl implements ScheduleInfoUserService {
    @Autowired
    private ScheduleInfoUserDAO userDao;

    @Override
    public List<ScheduleInfoUser> selectByModelId(String modelId) {
        return userDao.selectByModel(modelId);
    }

    @Override
    public ScheduleInfoUser selectById(Long id) {
        return userDao.selectById(id);
    }

    @Override
    public ScheduleInfoUser selectByModelAndWeek(String modelId, int weekNumber) {
        return userDao.selectByModelAndWeek(modelId,weekNumber);
    }

    @Override
    public void insert(ScheduleInfoUser scheduleInfoUser) {
        userDao.insert(scheduleInfoUser);
    }

    @Override
    public void deleteById(Long id) {
        userDao.deleteById(id);
    }

    @Override
    public ScheduleInfoUser selectByModelAndUser(String modelId, String userId) {
        return userDao.selectByModelAndUser(modelId,userId);
    }
}
