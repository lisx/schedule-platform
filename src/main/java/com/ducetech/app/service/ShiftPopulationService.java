package com.ducetech.app.service;

import com.ducetech.app.model.vo.ShiftPopulationVO;

import java.util.List;

/**
 * 排班人数表
 * Created by BLUE on 2017/5/22.
 */
public interface ShiftPopulationService {
    /**
     * 插入默认排班人数表
     * @param shiftPopulationVO
     */
    Integer insertShiftPopulation(ShiftPopulationVO shiftPopulationVO);

    /**
     * 修改排班人数表
     * @param shiftPopulationVO
     */
    void updateShiftPopulation(ShiftPopulationVO shiftPopulationVO);

    /**
     * 删除排班人数
     * @param shiftPopulationVO
     */
    void deleteShiftPopulation(ShiftPopulationVO shiftPopulationVO);

    /**
     * 获取排版人数集合
     * @param shiftPopulationVO
     * @return
     */
    List<ShiftPopulationVO> getShiftPopulation(ShiftPopulationVO shiftPopulationVO);
}