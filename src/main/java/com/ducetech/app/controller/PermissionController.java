package com.ducetech.app.controller;

import com.alibaba.fastjson.JSON;
import com.ducetech.app.model.Permission;
import com.ducetech.app.model.Role;
import com.ducetech.app.model.User;
import com.ducetech.app.service.PermissionService;
import com.ducetech.app.service.RoleService;
import com.ducetech.app.service.UserService;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.model.PagerRS;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.util.DateUtil;
import com.ducetech.framework.util.Digests;
import com.ducetech.framework.web.view.OperationResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 *
 * @ClassName: PermissionController
 * @Description: 人员角色管理Controller
 * @date 2016年9月19日 上午9:23:04
 *
 */
@Controller
@RequestMapping("permissions")
public class PermissionController extends BaseController {

	@Autowired
	private RoleService roleService;

	@Autowired
	private PermissionService permissionService;


	/**
	 * @Title: role
	 * @return String
	 * @Description: 角色管理首页
	 */
	@RequestMapping(value = "/role", method = RequestMethod.GET)
	public String role() {
		return "/permission/role";
	}

	/**
	 * @Title: roleData
	 * @param request
	 * @throws Exception
	 * @Description: 角色管理数据
	 */
	@RequestMapping(value = "/roleData", method = RequestMethod.POST)
	@ResponseBody
	public PagerRS<Role> roleData(HttpServletRequest request) throws Exception {
		BaseQuery<Role> query = Role.getSearchCondition(Role.class, request);
		PagerRS<Role> roles = roleService.getRoleByPager(query);
		return roles;
	}

	/**
	 * @Title: roleStatus
	 * @param roleId
	 * @param isDeleted
	 * @throws java.io.IOException
	 * @Description: 角色禁用启用
	 */
	@RequestMapping(value = "roleStatus", method = RequestMethod.POST)
	@ResponseBody
	public OperationResult roleStatus(String roleId, String isDeleted) throws IOException {
		Role role = new Role();
		role.setRoleId(roleId);
		role.setIsDeleted(isDeleted);
		roleService.updateRole(role);
		return OperationResult.buildSuccessResult("操作成功", "success");
	}

	/**
	 *
	 * @Title: addRole
	 * @Description: 新增角色页面
	 * @param @param model
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	@RequestMapping(value = "/addRole", method = RequestMethod.GET)
	public String addRole() {
		return "/permission/add-role";
	}

	/**
	 * @Title: saveRole
	 * @param request
	 * @throws java.io.IOException
	 * @Description: 新增角色和菜单权限
	 */
	@RequestMapping(value = "/saveRole", method = RequestMethod.POST)
	@ResponseBody
	public OperationResult saveRole(Role role, HttpServletRequest request) throws IOException {
		User user = getLoginUser(request);
		role.setCreatorId(user.getUserId());
		role.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
		roleService.addRoleAndPermission(role);
		return OperationResult.buildSuccessResult("操作成功", "success");
	}

	/**
	 *
	 * @Title: editRole
	 * @Description: 编辑角色
	 * @param @param model
	 * @param @return    设定文件
	 * @return String    返回类型
	 * @throws
	 */
	@RequestMapping(value = "/editRole/{roleId}", method = RequestMethod.GET)
	public String editRole(@PathVariable String roleId, Model model) {
		Role role = roleService.getRoleById(roleId);
		model.addAttribute("role", role);
		return "/permission/edit-role";
	}

	/**
	 * @Title: updateRole
	 * @param role
	 * @throws java.io.IOException
	 * @Description: 更新角色和菜单权限
	 */
	@RequestMapping(value = "/updateRole", method = RequestMethod.POST)
	@ResponseBody
	public OperationResult updateRole(Role role) throws IOException {
		roleService.updateRoleAndPermission(role);
		return OperationResult.buildSuccessResult("更新成功", "success");
	}

	/**
	 * @Title: getAllPermission
	 * @throws java.io.IOException
	 * @Description: 获取所有菜单权限
	 */
	@RequestMapping(value = "/getAllPermission", method = RequestMethod.GET)
	@ResponseBody
	public List<Permission> getAllPermission() throws IOException {
		List<Permission> permissions = permissionService.getAllPermissions();
		return permissions;
	}

	/**
	 * @Title: getCheckedPermission
	 * @throws java.io.IOException
	 * @Description: 获取角色已选的菜单权限
	 */
	@RequestMapping(value = "/getCheckedPermission", method = RequestMethod.POST)
	@ResponseBody
	public List<Permission> getCheckedPermission(String roleId) throws IOException {
		List<Permission> permissions = permissionService.getPermissionsByRoleId(roleId);
		return permissions;
	}
}