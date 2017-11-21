package com.ducetech.app.dao;

import com.ducetech.app.model.PostSetting;
import org.apache.ibatis.annotations.Param;

import java.util.List;


public interface PostSettingDAO {

	/**
	 * 获取岗位列表
	 * @return
	 */
	List<PostSetting> selectPostSetting();

	/**
	* @Title: addPostSetting
	* @param dept
	* @Description: 保存新增的人员信息
	*/
	void insertPostSetting(PostSetting dept);

	/**
	* @Title: updatePostSetting
	* @param dept
	* @Description: 更新人员信息
	*/
	void updatePostSetting(PostSetting dept);

	/**
	* @Title: deletePostSetting
	* @param postCode
	* @Description: 启用禁用人员,默认0位启用
	*/
	void deletePostSetting(@Param("postCode") String postCode,@Param("ifUse") int ifUse);

    /**
     * @Title: selectPostSettingByGroupName
     * @param postCode
     * @return PostSetting
     * @Description: 按登部门名称查询
     */
    PostSetting selectPostSettingByPostCode(@Param("postCode") String postCode);

	PostSetting selectPostSettingByPostName(@Param("postName") String postName);

    List<PostSetting> selectByParentCode(@Param("postCode") String parentCode, @Param("postCodeLength") int postCodeLength);

	/**
	 * 根据岗位id获取岗位设置
	 * @param postId
	 */
	PostSetting selectPostSettingByPostId(String postId);

	/**
	 * 获取备班人员
	 * @return
	 */
	List<PostSetting> selectBackupPostSettings();
}