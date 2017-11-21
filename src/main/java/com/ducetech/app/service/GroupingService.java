package com.ducetech.app.service;

import com.ducetech.app.model.Grouping;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;

import java.util.List;

public interface GroupingService {

    /**
     * @Title: getAllGroupings
     * @return List<Grouping>
     * @Description: 获取所有人员
     */
    List<Grouping> getAllGroupings();

    /**
     * @Title: getGroupingByQuery
     * @return List<Grouping>
     * @Description: 按条件筛选人员,不带分页
     */
    List<Grouping> getGroupingByQuery(Grouping dept);

    /**
     * @Title: getGroupingByPager
     * @return PagerRS<Grouping>
     * @Description: 按条件筛选人员,带分页
     */
    PagerRS<Grouping> getGroupingByPager(BaseQuery<Grouping> query);

    /**
     * 获取新节点
     * @param parentCode
     * @return
     */
    String selectGroupingByParentCode(String parentCode);

    /**
     * @Title: addGrouping
     * @param dept
     * @Description: 保存新增的人员信息
     */
    void insertGrouping(Grouping dept);

    /**
     * @Title: updateGrouping
     * @param dept
     * @Description: 更新人员信息
     */
    void updateGrouping(Grouping dept);

    /**
     * @Title: deleteGroupingById
     * @param groupCode
     * @Description: 启用禁用人员,默认0位启用
     */
    void deleteGrouping(String groupCode);

    Grouping selectGroupingByGroupCode(String groupCode);

    Grouping selectGroupingByGroupId(String groupId);
    /**
     * 获取对应等级
     * @param parentCode
     * @param groupCodeLength
     * @return
     */
    List<Grouping> selectByParentCode(String parentCode, int groupCodeLength);

    /**
     * 根据编码长度获取分组列表
     * @param len
     * @return
     */
    List<Grouping> queryGroupsByLength(int len);

    Grouping selectGroupingByGroupName(String groupName);


}
