package com.ducetech.app.service;

import com.ducetech.app.model.PostSetting;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;

import java.util.List;

public interface PostSettingService {

    /**
     * @Title: getAllPostSettings
     * @return List<PostSetting>
     * @Description: 获取所有人员
     */
    List<PostSetting> getAllPostSettings();

    /**
     * @Title: getPostSettingByQuery
     * @return List<PostSetting>
     * @Description: 按条件筛选人员,不带分页
     */
    List<PostSetting> getPostSettingByQuery();

    /**
     * @Title: getPostSettingByPager
     * @return PagerRS<PostSetting>
     * @Description: 按条件筛选人员,带分页
     */
    PagerRS<PostSetting> getPostSettingByPager(BaseQuery<PostSetting> query);

    /**
     * 获取新节点
     * @param parentCode
     * @return
     */
    String selectPostSettingByParentCode(String parentCode);

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
     * @Title: deletePostSettingById
     * @param postCode
     * @Description: 启用禁用人员,默认0位启用
     */
    void deletePostSetting(String postCode,int ifUse);

    PostSetting selectPostSettingByPostCode(String positionCode);

    PostSetting selectPostSettingByPostName(String postName);

    /**
     * 获取对应等级
     * @param parentCode
     * @param positionCodeLength
     * @return
     */
    List<PostSetting> selectByParentCode(String parentCode, int positionCodeLength);

    List<PostSetting> queryAll();

    /**
     * 获取岗位设置根据postId
     * @return
     */
    PostSetting getPostSettingByPostId(String postId);

    PostSetting getStationWorkerPost();

    PostSetting getOriginPostSettingByPostId(String postId);

    /**
     * 获取备班人员
     * @return
     */
    List<PostSetting> getBackupPostSettings();
}
