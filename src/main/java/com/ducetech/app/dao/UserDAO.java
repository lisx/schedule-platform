package com.ducetech.app.dao;

import com.ducetech.app.model.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserDAO {


	/**
	* @Title: selectUserName
	* @param userName
	* @return User
	* @Description: 按登录名查询用户姓名
	*/
	User selectUserByUserName(@Param("userName") String userName);

    User selectUserByUserCode(@Param("userCode") String userCode);
	/**
	* @Title: selectPermissionByUserId  
	* @param userId
	* @return List<String>
	* @Description: 获取某用户的全部菜单权限
	*/
	List<String> selectPermissionByUserId(@Param("userId") String userId);

	/** 
	* @Title: selectUserByUserId  
	* @return userId
	* @Description: 通过人员ID获取信息
	*/
	User selectUserByUserId(@Param("userId") String userId);

	/** 
	* @Title: selectUser  
	* @param user
	* @return List<User>
	* @Description: User通用查询
	*/
	List<User> selectUser(User user);
	
	/** 
	* @Title: addUser  
	* @param user
	* @Description: 保存新增的人员信息
	*/
	void insertUser(User user);
	
	/** 
	* @Title: updateUser  
	* @param user
	* @Description: 更新人员信息
	*/
	void updateUser(User user);

	/** 
	* @Title: deleteUserById 
	* @param userId , isDeleted
	* @Description: 启用禁用人员,默认0位启用
	*/
	void deleteUserById(@Param("userId") String userId, @Param("isDeleted") String isDeleted);

	/** 
	* @Title: selectUsersByRoleId  
	* @param roleId
	* @return List<User>
	* @Description: 通过角色ID获取人员
	*/
	List<User> selectUsersByRoleId(@Param("roleId") String roleId);
    List<User> selectUserByStation(@Param("station") String station);

    int selectUserCountByStation(String station);

	/**
	 * 根据站区和站点获取备班人员
	 * @param user
	 * @return
	 */
	List<User> getPrepareUser(User user);
    List<User> getLeaveUser(@Param("station") String station,@Param("userJob")String userJob,@Param("startAt")String startAt,@Param("endAt")String endAt);
}