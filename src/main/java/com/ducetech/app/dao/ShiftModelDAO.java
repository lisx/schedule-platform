package com.ducetech.app.dao;

import com.ducetech.app.model.Grouping;
import com.ducetech.app.model.PostSetting;
import com.ducetech.app.model.ShiftModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface ShiftModelDAO {

	/**
	* @Title: selectShiftModel
	* @param postId
	* @return List<ShiftModel>
	* @Description: ShiftModel通用查询
	*/
	List<ShiftModel> selectShiftModel(@Param("postId") String postId);

	/**
	* @Title: addShiftModel
	* @param dept
	* @Description: 保存新增的人员信息
	*/
	void insertShiftModel(ShiftModel dept);
    void updateShiftModel(ShiftModel dept);
	/**
	* @Title: deleteShiftModel
	* @param modelCode
	* @Description: 启用禁用人员,默认0位启用
	*/
	void deleteShiftModel(@Param("modelCode") String modelCode, @Param("ifUse") int ifUse);
    /**
     * @Title: selectGroupingByGroupName
     * @param modelCode
     * @return Grouping
     * @Description: 按登部门名称查询
     */
    ShiftModel selectShiftModelByModelCode(@Param("modelCode") String modelCode);
    List<ShiftModel> selectByParentCode(@Param("modelCode") String modelCode, @Param("modelCodeLength") int modelCodeLength);
    ShiftModel selectShiftModelByModelCodeId(@Param("modelCode") String modelCode,@Param("modelId") String modelId);

    List<ShiftModel> selectShiftModelByPostGroup(@Param("postId") String postId, @Param("groupCode") String groupCode);

    List<ShiftModel> selectAllModels();
}