package com.ducetech.app.service;

import com.ducetech.app.model.Role;
import com.ducetech.app.model.User;
import com.ducetech.framework.model.PagerRS;
import com.ducetech.framework.model.BaseQuery;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {

	
	/** 
	* @Title: getUserByLoginName  
	* @param userName
	* @return User
	* @Description: 按登录名获取人员
	*/
	User getUserByUserName(String userName);

    User getUserByUserCode(String userCode);

    List<User> importFile(MultipartFile mFile,String rootPath);
    String genRandomNum(int pwd_len);
	/** 
	* @Title: getUserPermission  
	* @param userId
	* @return List<String>
	* @Description: 获得用户相关菜单权限
	*/
	List<String> getUserPermission(String userId);
	
	/** 
	* @Title: getUsersByDeptId  
	* @param deptId
	* @return List<User>
	* @Description: 按部门ID获取人员
	*/
	List<User> getUsersByStationArea(String deptId);

    /**
     * @Title: getUsersByDeptId
     * @param deptId
     * @return List<User>
     * @Description: 按部门ID获取人员
     */
    List<User> getUsersByStation(String deptId);

	/** 
	* @Title: getUserByUserId  
	* @param userId
	* @return String
	* @Description: 通过人员ID获取人员信息
	*/
	User getUserByUserId(String userId);

	/** 
	* @Title: addUser  
	* @param user
	* @Description: 新增用户
	*/
	void addUser(User user);
	
	/** 
	* @Title: passwordReset  
	* @param userId,password
	* @Description: 人员重置密码
	*/
	void passwordReset(String userId, String password);

	/** 
	* @Title: deleteUser  
	* @param userId
	* @Description: 删除人员,这里是假删除,只是更改isDeleted的状态
	*/
	void deleteUserById(String userId,String isDeleted);

	/** 
	* @Title: getAllUsers  
	* @return List<User>
	* @Description: 获取所有人员
	*/
	List<User> getAllUsers();
	
	/** 
	* @Title: getUserByQuery  
	* @return List<User>
	* @Description: 按条件筛选人员,不带分页
	*/
	List<User> getUserByQuery(User user);
	
	/** 
	* @Title: getUserByPager  
	* @return PagerRS<User>
	* @Description: 按条件筛选人员,带分页
	*/
	PagerRS<User> getUserByPager(BaseQuery<User> query);
	
	/** 
	* @Title: updateUser  
	* @param user
	* @return void
	* @Description: 更新人员
	*/
	void updateUser(User user);

	/** 
	* @Title: getUserIdByRoleId  
	* @param roleId
	* @return List<String>
	* @Description: 通过角色ID获取人员
	*/
	List<User> getUsersByRoleId(String roleId);

	/** 
	* @Title: getUsersByRoleIdPage  
	* @param query
	* @return PagerRS<User>
	* @Description: 查询角色已有人员带分页
	*/
	PagerRS<User> getUsersByRoleIdPage(BaseQuery<Role> query);
	
	/**
	 * 
	* @Title: updateUser 
	* @Description: 修改密码
	* @param @param user    设定文件 
	* @return void    返回类型 
	* @throws
	 */
	void updateUserPassword(User user);

	/** 
	* @Title: resetPass  
	* @param user
	* @return void
	* @Description: 重置密码
	*/
	String resetPass(User user);

	/** 
	* @Title: updateUserStatus  
	* @param user
	* @return void
	* @Description: 人员启用停用
	*/  
	void updateUserStatus(User user);

    List<User> selectUserByStation(String station);

    int getUserCountByStation(String station);

	/**
	 * 根据站区和站点获取备班人员
	 * @param user
	 * @return
	 */
	List<User> getPrepareUser(User user);
    List<User> getLeaveUser(String station,String userJob,String startAt,String endAt);
}
