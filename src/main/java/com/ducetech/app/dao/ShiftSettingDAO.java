package com.ducetech.app.dao;

import com.ducetech.app.model.ShiftSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ShiftSettingDAO {
    List<ShiftSetting> selectShiftSettingByModelId(@Param("modelId")String modelId);
	/**
	* @Title: selectShift
	* @param dept
	* @return List<Shift>
	* @Description: Shift通用查询
	*/
	List<ShiftSetting> selectShiftSetting(ShiftSetting dept);

	List<ShiftSetting> selectShiftSettingByStation(@Param("station") String station);

	/**
	* @Title: addShift
	* @param dept
	* @Description: 保存新增的人员信息
	*/
	void insertShiftSetting(ShiftSetting dept);
    ShiftSetting editShiftSetting(@Param("shiftId") String shiftId);
	/**
	* @Title: updateShift
	* @param dept
	* @Description: 更新人员信息
	*/
	void updateShiftSetting(ShiftSetting dept);

	/**
	 * 删除根据id删除班次
	 * @param shiftId
	 */
	void deleteShiftSettingByShiftId(@Param("shiftId")String shiftId);

	/**
	 * 获取某个岗位的每日总工时
	 * @param shiftSetting
	 */
	Integer getShiftSettionTotal(ShiftSetting shiftSetting);

	/**
	 * 根据modelId删除班次
	 * @param modelId
	 */
	void deleteShiftSettingByModelId(@Param("modelId")String modelId);

//	/**
//	* @Title: deleteShift
//	* @param groupCode
//	* @Description: 启用禁用人员,默认0位启用
//	*/
//	void deleteShift(@Param("groupCode") String groupCode, @Param("ifUse") int ifUse);
//
//    /**
//     * @Title: selectShiftByGroupName
//     * @param groupCode
//     * @return Shift
//     * @Description: 按登部门名称查询
//     */
//    Shift selectShiftByGroupCode(@Param("groupCode") String groupCode);
//
//    List<Shift> selectByParentCode(@Param("groupCode") String parentCode, @Param("groupCodeLength") int groupCodeLength);

}