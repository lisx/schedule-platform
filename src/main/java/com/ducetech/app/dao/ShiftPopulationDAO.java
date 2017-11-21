package com.ducetech.app.dao;

import com.ducetech.app.model.ShiftPopulation;

import java.util.List;

/**
 * 排班人数映射类
 * Created by BLUE on 2017/5/22.
 */
public interface ShiftPopulationDAO {
    /**
     * 插入排版人数
     * @param shiftPopulation
     * @return
     */
    Integer insertShiftPopulation(ShiftPopulation shiftPopulation);

    /**
     * 修改排版人数
     * @param shiftPopulation
     */
    void updateShiftPopulation(ShiftPopulation shiftPopulation);

    /**
     *  根据populationId删除排班人数
     * @param populationId
     */
    void deleteShiftPopulation(Integer populationId);

    /**
     * 获取排班人数
     * @return
     */
    List<ShiftPopulation> getShiftPopulation(ShiftPopulation shiftPopulation);
}