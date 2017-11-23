package com.ducetech.app.service.impl;

import com.ducetech.app.dao.ScheduleInfoTemplateDAO;
import com.ducetech.app.dao.ScheduleInfoUserDAO;
import com.ducetech.app.model.ScheduleInfoTemplate;
import com.ducetech.app.model.ScheduleInfoUser;
import com.ducetech.app.model.User;
import com.ducetech.app.service.ScheduleInfoTemplateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class ScheduleInfoTemplateServiceImpl implements ScheduleInfoTemplateService {
    @Autowired
    private ScheduleInfoTemplateDAO scheduleInfoTemplateDAO;

    @Autowired
    private ScheduleInfoUserDAO infoUserDAO;

    @Override
    public List<ScheduleInfoTemplate> selectScheduleInfoTemplateByModelId(String modelId) {
        return scheduleInfoTemplateDAO.selectScheduleInfoTemplateByModelId(modelId);
    }

    @Override
    @Transactional
    public int insertScheduleInfoTemplateList(String modelId,List<ScheduleInfoTemplate> scheduleInfoTemplateList, User user) {
        scheduleInfoTemplateDAO.deleteByModelId(modelId);
        int result=0;
        for (ScheduleInfoTemplate scheduleInfoTemplate : scheduleInfoTemplateList) {
            int orderIndex = scheduleInfoTemplate.getOrderIndex();
            int week=orderIndex / 7;
            result= Math.max(week,result);
            scheduleInfoTemplate.setWeekNumber(week);
            scheduleInfoTemplate.setWeekDay(orderIndex % 7);
            scheduleInfoTemplate.setUpdatorId(user.getUserId());
            scheduleInfoTemplateDAO.insertScheduleInfoTemplate(scheduleInfoTemplate);
        }
        return result;
    }

    @Override
    public Integer updateScheduleInfoTemplate(ScheduleInfoTemplate scheduleInfoTemplate) {
        int orderIndex = scheduleInfoTemplate.getOrderIndex();
        scheduleInfoTemplate.setWeekNumber(orderIndex / 7);
        scheduleInfoTemplate.setWeekDay(orderIndex % 7);
        return scheduleInfoTemplateDAO.updateScheduleInfoTemplate(scheduleInfoTemplate);
    }

    @Override
    public void removeByModelAndWeek(String modelId, int weekNumber) {
        scheduleInfoTemplateDAO.deleteByModelAndWeek(modelId,weekNumber);
        List<ScheduleInfoTemplate> templates = scheduleInfoTemplateDAO.selectScheduleInfoTemplateByModelId(modelId);
        for (ScheduleInfoTemplate t :
                templates) {
            if (t.getWeekNumber()>weekNumber){
                t.setWeekNumber(t.getWeekNumber()-1);
                scheduleInfoTemplateDAO.updateScheduleInfoTemplate(t);
            }
        }
        List<ScheduleInfoUser> users = infoUserDAO.selectByModel(modelId);
        for (ScheduleInfoUser u: users) {
            if (u.getWeekNumber()>weekNumber){
                u.setWeekNumber(u.getWeekNumber()-1);
                infoUserDAO.updateWeekNumber(u);
            }else if(u.getWeekNumber()==weekNumber){
                infoUserDAO.deleteById(u.getId());
            }
        }
    }
}
