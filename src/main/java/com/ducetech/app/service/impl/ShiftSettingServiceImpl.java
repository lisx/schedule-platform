package com.ducetech.app.service.impl;

import com.ducetech.app.dao.ShiftSettingDAO;
import com.ducetech.app.dao.WorkflowDAO;
import com.ducetech.app.model.ShiftSetting;
import com.ducetech.app.model.Workflow;
import com.ducetech.app.service.ShiftSettingService;
import com.ducetech.app.model.vo.ShiftSettingVO;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.velocity.runtime.directive.Foreach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class ShiftSettingServiceImpl implements ShiftSettingService {
    @Autowired
    private ShiftSettingDAO shiftDAO;
    @Autowired
    private WorkflowDAO workflowDAO;

    @Override
    public List<ShiftSetting> selectShiftSettingByModelId(String modelId) {
        return shiftDAO.selectShiftSettingByModelId(modelId);
    }

    @Override
    public List<ShiftSetting> selectShiftSettingByStation(String station) {
        return shiftDAO.selectShiftSettingByStation(station);
    }

    @Override
    public List<ShiftSetting> selectShiftSetting(ShiftSetting dept) {
        return shiftDAO.selectShiftSetting(dept);
    }

    @Override
    public List<ShiftSetting> getShiftByQuery(ShiftSetting dept) {
        return shiftDAO.selectShiftSetting(dept);
    }

    @Override
    public PagerRS<ShiftSetting> getShiftByPager(BaseQuery<ShiftSetting> query) {
        if (query != null && query.getPage() > 0) {            //如果传入offset大于0,则启用分页查询，否则不启用
            PageHelper.startPage(query.getPage(), query.getRows(), true);
        }
        List<ShiftSetting> deptList = shiftDAO.selectShiftSetting(query.getT());
        @SuppressWarnings({"unchecked", "rawtypes"})
        PageInfo page = new PageInfo(deptList);
        PagerRS<ShiftSetting> pagerRS = new PagerRS<ShiftSetting>(deptList, page.getTotal(), page.getPages());
        return pagerRS;
    }


    @Override
    public void insertShiftSetting(ShiftSettingVO dept) {

        shiftDAO.insertShiftSetting(dept);
    }

    @Override
    public ShiftSetting editShiftSetting(String shiftId) {
        return shiftDAO.editShiftSetting(shiftId);
    }

    @Override
    public void updateShiftSetting(ShiftSetting dept) {
        shiftDAO.updateShiftSetting(dept);
    }

    @Override
    public void deleteShiftSettingByShiftId(String shiftId) {
        shiftDAO.deleteShiftSettingByShiftId(shiftId);
    }

    @Override
    public Integer getShiftSettionTotal(ShiftSetting shiftSetting) {
        return shiftDAO.getShiftSettionTotal(shiftSetting);
    }

    @Override
    public Map<String, List<String>> getWorkFlowMap(List<ShiftSetting> shifts) {
        Map<String, List<String>> result=new HashMap<>();
        for (ShiftSetting s :
                shifts) {
            List<Workflow> workflows = workflowDAO.selectWorkflowsByShiftId(s.getShiftId());
            result.put(s.getShiftId(),new LinkedList<String>());
            for (Workflow w :
                    workflows) {
                result.get(s.getShiftId()).add(s.getShiftCode()+w.getSerialNumber());
            }
        }
        return result;
    }
//
//    @Override
//    public void deleteShiftSetting(String shiftCode) {
//
//    }
//
//    @Override
//    public ShiftSetting selectShiftSettingByGroupCode(String shiftCode) {
//        return null;
//    }
//
//    @Override
//    public List<ShiftSetting> selectByParentCode(String parentCode, int shiftCodeLength) {
//        return null;
//    }
}
