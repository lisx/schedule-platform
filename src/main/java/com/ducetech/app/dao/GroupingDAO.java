package com.ducetech.app.dao;

import com.ducetech.app.model.Grouping;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface GroupingDAO {

	/** 
	* @Title: selectGrouping  
	* @param dept
	* @return List<Grouping>
	* @Description: Grouping通用查询
	*/
	List<Grouping> selectGrouping(Grouping dept);
	
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
	* @Title: deleteGrouping
	* @param groupCode
	* @Description: 启用禁用人员,默认0位启用
	*/
	void deleteGrouping(@Param("groupCode") String groupCode);


    Grouping selectGroupingByGroupCode(@Param("groupCode") String groupCode);

    Grouping selectGroupingByGroupId(@Param("groupId")String groupId);

    List<Grouping> selectByParentCode(@Param("groupCode") String parentCode, @Param("groupCodeLength") int groupCodeLength);

	/**
	 * 根据编码长度获取分组列表
	 * @param len
	 * @return
	 */
	List<Grouping> queryGroupsByLength(@Param("len") int len);

	/**
	 * @Title: selectGroupingByGroupName
	 * @return Grouping
	 * @Description: 按登部门名称查询
	 */
	Grouping selectGroupingByGroupName(@Param("groupName") String groupName);

}