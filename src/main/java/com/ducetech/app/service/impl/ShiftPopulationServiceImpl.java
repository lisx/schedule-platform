package com.ducetech.app.service.impl;

import com.ducetech.app.dao.ShiftPopulationDAO;
import com.ducetech.app.model.ShiftPopulation;
import com.ducetech.app.service.ShiftPopulationService;
import com.ducetech.app.model.vo.ShiftPopulationVO;
import com.ducetech.framework.util.DateUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BLUE on 2017/5/22.
 */
@Service
public class ShiftPopulationServiceImpl implements ShiftPopulationService {
    @Autowired
    private ShiftPopulationDAO shiftPopulationDAO;

    @Override
    public Integer insertShiftPopulation(ShiftPopulationVO shiftPopulationVO) {
        ShiftPopulation shiftPopulation = new ShiftPopulation();
        BeanUtils.copyProperties(shiftPopulationVO,shiftPopulation);
        shiftPopulationDAO.insertShiftPopulation(shiftPopulation);
        return  shiftPopulation.getPopulationId();
    }

    @Override
    public void updateShiftPopulation(ShiftPopulationVO shiftPopulationVO) {
        ShiftPopulation shiftPopulation = new ShiftPopulation();
        BeanUtils.copyProperties(shiftPopulationVO,shiftPopulation);
        shiftPopulationDAO.updateShiftPopulation(shiftPopulation);
    }

    @Override
    public void deleteShiftPopulation(ShiftPopulationVO shiftPopulationVO) {
        shiftPopulationDAO.deleteShiftPopulation(shiftPopulationVO.getPopulationId());
    }

    @Override
    public List<ShiftPopulationVO> getShiftPopulation(ShiftPopulationVO shiftPopulationVO) {
        ShiftPopulation shiftPopulation = new ShiftPopulation();
        BeanUtils.copyProperties(shiftPopulationVO,shiftPopulation);
        List<ShiftPopulation> sps = shiftPopulationDAO.getShiftPopulation(shiftPopulation);

        List<ShiftPopulationVO> spvos = new ArrayList<ShiftPopulationVO>();

        for(int i = 0; i < sps.size(); i++) {
            ShiftPopulation sp2 =  sps.get(i);
            ShiftPopulationVO spvo2 = new ShiftPopulationVO();
            BeanUtils.copyProperties(sp2,spvo2);
            spvo2.setStartAtStr(DateUtil.minuToTime(spvo2.getStartAt()));
            spvo2.setEndAtStr(DateUtil.minuToTime(spvo2.getEndAt()));
            spvos.add(spvo2);
        }
        return spvos;
    }
}
