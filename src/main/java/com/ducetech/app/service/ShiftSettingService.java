package com.ducetech.app.service;

import com.ducetech.app.model.ShiftSetting;
import com.ducetech.app.model.vo.ShiftSettingVO;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;

import java.util.List;
import java.util.Map;

public interface ShiftSettingService {

    List<ShiftSetting> selectShiftSettingByModelId(String modelId);
    List<ShiftSetting> selectShiftSettingByStation(String station);


    /**
     * 查询
     * @param dept
     * @return
     */
    List<ShiftSetting> selectShiftSetting(ShiftSetting dept);

    /**
     * @Title: getShiftByQuery
     * @return List<Shift>
     * @Description: 按条件筛选人员,不带分页
     */
    List<ShiftSetting> getShiftByQuery(ShiftSetting dept);

    /**
     * @Title: getShiftByPager
     * @return PagerRS<Shift>
     * @Description: 按条件筛选人员,带分页
     */
    PagerRS<ShiftSetting> getShiftByPager(BaseQuery<ShiftSetting> query);


    /**
     * @Title: addShift
     * @param dept
     * @Description: 保存新增的人员信息
     */
    void insertShiftSetting(ShiftSettingVO dept);
    ShiftSetting editShiftSetting(String shiftId);
    void updateShiftSetting(ShiftSetting dept);

    /**
     * 删除班次信息
     * @param shiftId
     */
    void deleteShiftSettingByShiftId(String shiftId);

    /**
     * 获取某个岗位的每日总工时
     * @param shiftSetting
     * @return
     */
    Integer getShiftSettionTotal(ShiftSetting shiftSetting);

    Map<String,List<String>> getWorkFlowMap(List<ShiftSetting> shifts);
//
//    /**
//     * @Title: updateShift
//     * @param dept
//     * @Description: 更新人员信息
//     */
//    void updateShift(Shift dept);
//
//    /**
//     * @Title: deleteShiftById
//     * @param shiftCode
//     * @Description: 启用禁用人员,默认0位启用
//     */
//    void deleteShift(String shiftCode);
//
//    Shift selectShiftByGroupCode(String shiftCode);
//
//    /**
//     * 获取对应等级
//     * @param parentCode
//     * @param shiftCodeLength
//     * @return
//     */
//    List<Shift> selectByParentCode(String parentCode, int shiftCodeLength);
}
