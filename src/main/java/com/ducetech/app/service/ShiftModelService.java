package com.ducetech.app.service;

import com.ducetech.app.model.ShiftModel;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShiftModelService {

    /**
     * @Title: selectShiftModel
     * @return List<ShiftModel>
     * @Description: 获取所有人员
     */
    List<ShiftModel> selectShiftModel(String postId);

    /**
     * @Title: getShiftModelByPager
     * @return PagerRS<ShiftModel>
     * @Description: 按条件筛选人员,带分页
     */
    PagerRS<ShiftModel> getShiftModelByPager(BaseQuery<ShiftModel> query);

    /**
     * 获取新节点
     * @param parentCode
     * @return
     */
    String selectShiftModelByParentCode(String parentCode);

    /**
     * @Title: addShiftModel
     * @param dept
     * @Description: 保存新增的人员信息
     */
    void insertShiftModel(ShiftModel dept);

    /**
     * @Title: deleteShiftModelById
     * @param modelCode
     * @Description: 启用禁用人员,默认0位启用
     */
    void deleteShiftModel(String modelCode);

    void updateShiftModel(ShiftModel dept);

    ShiftModel selectShiftModelByModelCode(String modelCode);

    /**
     * 获取对应等级
     * @param parentCode
     * @param modelCodeLength
     * @return
     */
    List<ShiftModel> selectByParentCode(String parentCode, int modelCodeLength);
    ShiftModel selectShiftModelByModelCodeId(String modelCode, String modelId);

    List<ShiftModel> selectShiftModelByPostGroup(String postId, String groupCode);

    List<ShiftModel> getAllModels();
}
