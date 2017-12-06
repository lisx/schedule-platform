package com.ducetech.app.controller;


import com.ducetech.app.model.Grouping;
import com.ducetech.app.model.PostSetting;
import com.ducetech.app.model.Role;
import com.ducetech.app.model.User;
import com.ducetech.app.service.GroupingService;
import com.ducetech.app.service.PostSettingService;
import com.ducetech.app.service.RoleService;
import com.ducetech.app.service.UserService;
import com.ducetech.framework.controller.BaseController;
import com.ducetech.framework.exception.ServiceException;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.model.PagerRS;
import com.ducetech.framework.util.*;
import com.ducetech.framework.web.view.OperationResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Controller
public class UserController extends BaseController {
	protected Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private GroupingService groupingService;

	@Autowired
	private PostSettingService postSettingService;

    /**
     * 登录
     * @param request
     * @return
     */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String loginPage(HttpServletRequest request) {
		if (request.getSession().getAttribute("DT_LOGIN_NAME")!=null) {
			return "redirect:/";
		}
		return "login";
	}


	@RequestMapping(value = "/user/createImpFile", method = RequestMethod.POST)
	@ResponseBody
    public OperationResult createImpFile(HttpServletRequest request, @RequestParam("fileUpload") CommonsMultipartFile fileUpload) {
		User u = getLoginUser(request);
		try {
			if (fileUpload != null && !fileUpload.isEmpty()) {
				List<List<List<String>>> data = PoiUtil.readExcelToList(fileUpload, 1);
				if (null != data && !data.isEmpty()) {
					for (List<List<String>> sheet : data) {
						if (null != sheet && !sheet.isEmpty()) {
							for (List<String> row : sheet) {
								String userCode = ExtStringUtil.trim(row.get(0));
								String userName = ExtStringUtil.trim(row.get(1));
								String gender = ExtStringUtil.trim(row.get(2));
								String birthday =ExtStringUtil.trim(row.get(3));
								String stationArea = u.getStationArea();
								String station = ExtStringUtil.trim(row.get(4));
								String userJob = ExtStringUtil.trim(row.get(5));
								String phone = ExtStringUtil.trim(row.get(6));
								String addr = ExtStringUtil.trim(row.get(7));
								String idCode = ExtStringUtil.trim(row.get(8));
								String married = ExtStringUtil.trim(row.get(9));
								String child = ExtStringUtil.trim(row.get(10));
								String edu = ExtStringUtil.trim(row.get(11));
								String certNo = ExtStringUtil.trim(row.get(12));
								String certLevel = ExtStringUtil.trim(row.get(13));
								String recruitDate = ExtStringUtil.trim(row.get(14));
								String political = ExtStringUtil.trim(row.get(15));
								String joinDate = ExtStringUtil.trim(row.get(16));
								stationArea=checkStationArea(stationArea);
								station=checkStationArea(station);
								if (!ExtStringUtil.isBlank(userJob)) {
									PostSetting postSetting = postSettingService.selectPostSettingByPostName(userJob);
									if (null != postSetting) {
										userJob = postSetting.getPostCode();
									} else {
										userJob = "";
									}
								}
								User user = new User();
								user.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
								user.setOnBoardDate(recruitDate);
								user.setPhoneNumber(phone);
								user.setHomeAddress(addr);
								user.setBirthday(birthday);
								user.setIdCode(idCode);
								user.setIsMarried(married);
								user.setHasChild(child);
								user.setGender(gender);
								user.setEduBackGround(edu);
								user.setIsPartyMember(political);
								user.setJoinDate(joinDate);
								user.setCertLevel(certLevel);
								user.setCertNo(certNo);
								user.setCreatorId(u.getUserId());
								user.setIsAdmin("0");
								user.setIsDeleted(0);
								user.setPassword("123456");
								user.setUserPass("123456");
								user.setStation(station);
								user.setStationArea(stationArea);
								user.setUserCode(userCode);
								user.setUserJob(userJob);
								user.setUserName(userName);
								userService.addUser(user);
							}
						}
					}
				}
				return OperationResult.buildFailureResult("文件处理成功", "success");
			}
		} catch (Exception e) {
			throw new ServiceException("Upload file error", e);
		}
		return OperationResult.buildFailureResult("文件处理失败", "fail");
	}

	private String checkStationArea(String stationArea) {
		if (!ExtStringUtil.isBlank(stationArea)) {
			Grouping grouping = groupingService.selectGroupingByGroupName(stationArea);
			if (null != grouping) {
				return grouping.getGroupCode();
			}
		}
		return "";
	}

	@RequestMapping(value = "/logout", method = RequestMethod.GET)
	public String logoutPage() {
		SecurityUtils.getSubject().logout();
		return "redirect:/";
	}

	@RequestMapping(value = "/printUserTemplate", method = RequestMethod.GET)
	@ResponseBody
	public void printUserTemplate(HttpServletRequest request, HttpServletResponse response) {
		OutputStream os = null;
		InputStream inputStream = null;
		ByteArrayOutputStream baos = null;

		try {
			inputStream = UserController.class.getClassLoader().getResourceAsStream("./template/user.xlsx");
			String fileName = "人员信息模板.xlsx";
			response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
			response.setContentType("application/octet-stream; charset=utf-8");
			os = response.getOutputStream();
			baos = new ByteArrayOutputStream();
			byte[] temp = new byte[1024];
			int len = 0;
			while ((len = (inputStream.read(temp))) != -1) {
				baos.write(temp, 0, len);
			}
			baos.writeTo(os);
			baos.flush();
			response.flushBuffer();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (null != baos) {
				try {
					baos.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (null != inputStream) {
				try {
					inputStream.close();
				} catch (Exception e) {
					e.printStackTrace();

				}
			}
		}
	}

	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public String login(HttpServletRequest request, HttpServletResponse response, RedirectAttributesModelMap modelMap) {
		String userCode = request.getParameter("userCode");
		String password = request.getParameter("password");
		Subject subject = SecurityUtils.getSubject();
		UsernamePasswordToken token = new UsernamePasswordToken(userCode,password);
		if (!ExtStringUtil.isBlank(userCode) && !ExtStringUtil.isBlank(password)) {
			try {
				subject.login(token);
				User user = (User)subject.getPrincipal();
				log.info("{}:{} login.", userCode, user.getUserName());
				subject.getSession().setAttribute("DT_LOGIN_USER", user);
				CookieUtil.setCookie(response, "", user.getUserId());
			} catch (Exception ex) {
				ex.printStackTrace();
				modelMap.addFlashAttribute("message", "登录失败，请联系管理员！");
				return "redirect:/login";
			}
		}
		return "redirect:/";
	} 

	@RequestMapping(value = "/users/info", method = RequestMethod.GET)
	public String findUser(HttpServletRequest request, Model model) {
		User user = getLoginUser(request);
		model.addAttribute("user", user);
		return "user/user-info";
	}
	
	@RequestMapping(value = "/users/info", method = RequestMethod.POST)
	public String editUser(@ModelAttribute("form") User user, HttpServletRequest request, Model model) {
		String name = user.getUserName();
		
		User userInfo = getLoginUser(request);
		userInfo.setUserName(name);
		
//		userService.editUserInfo(userInfo);
		model.addAttribute("msg", "success");
		model.addAttribute("user", userInfo);
		return "user/user-info";
	}

	@RequestMapping(value = "/users/password", method = RequestMethod.GET)
	public String password(Model model) {
		return "/user/password";
	}

	/**
	 * @Title: person
	 * @param model
	 * @return String
	 * @Description: 跳转人员首页
	 */
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public String person(HttpServletRequest request, Model model) {
		User loginUser = getLoginUser(request);
		Role role = new Role();
		role.setIsDeleted("0");
		List<Grouping> groups=null;
		if (StringUtils.isBlank(loginUser.getStationArea())){
			groups=new ArrayList<>();
		}else {
			groups = groupingService.selectByParentCode(loginUser.getStationArea(),9);
		}
		List<PostSetting> posts = postSettingService.queryAll();
		List<Role> roles = roleService.getRoleByQuery(role);
		model.addAttribute("roles", roles);
		model.addAttribute("groups", groups);
		model.addAttribute("posts", posts);
		return "/user/index";
	}

	/**
	 * @Title: personData
	 * @return void
	 * @throws Exception
	 * @Description: 人员首页数据
	 */
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	@ResponseBody
	public PagerRS<User> personData(HttpServletRequest request) throws Exception {
		BaseQuery<User> query = User.getSearchCondition(User.class, request);
		PagerRS<User> rs = userService.getUserByPager(query);
		return rs;
	}

	/**
     * 新增
	 * @Title: addUser
	 */
	@RequestMapping(value = "/user/addUser", method = RequestMethod.POST)
	@ResponseBody
	public OperationResult create(User user, HttpServletRequest request) throws Exception {
		User userInfo = getLoginUser(request);
		if ("1".equals(user.getIsAdmin()) && ExtStringUtil.isBlank(user.getUserCode())) {
			return OperationResult.buildFailureResult("管理员编码不能为空", "fail");
		}
		if (user.getPassword().equals("") || null == user.getPassword()) {
            user.setPassword(userService.genRandomNum(6));
        }
		user.setUserPass(user.getPassword());
		user.setIsDeleted(0);
		user.setCreatorId(userInfo.getUserId());
		user.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
		userService.addUser(user);
		return OperationResult.buildSuccessResult("操作成功", "success");
	}

    /**
     * 编辑
     * @param userId
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User edit(@PathVariable(value="id") String userId) throws Exception {
        User user = new User();
        if (!ExtStringUtil.isBlank(userId)) {
            user = userService.getUserByUserId(userId);
        }

        return user;
    }
    /**
     * 更新
     * @param user
     */
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult update(User user,HttpServletRequest request) throws Exception {
		User _user = userService.getUserByUserId(user.getUserId());
		if (!ExtStringUtil.isBlank(user.getPassword())) {
			user.setUserPass(user.getPassword());
			user.setPassword(Digests.md5Hash(user.getPassword(), _user.getSecretKey()));
		}
        userService.updateUser(user);
        return OperationResult.buildSuccessResult("更新成功", "success");
    }
    /**
     * 删除
     * @param userId
     */
    @RequestMapping(value = "/users/{id}/userDel", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult userDel(@PathVariable(value="id") String userId) throws Exception {
        userService.deleteUserById(userId,"1");
		return OperationResult.buildSuccessResult("删除成功", "success");
    }

	/**
	 * 根据站区和站点获取备班人员
	 */
	@RequestMapping(value = "/getPrepareUser", method = RequestMethod.GET)
	@ResponseBody
    public List<User> getPrepareUser(User user) {
		//获取备班人员
		List<PostSetting> backupPostSettings = postSettingService.getBackupPostSettings();
		List<User> userList = new ArrayList<User>();
		for(PostSetting ps : backupPostSettings) {
			user.setUserJob(ps.getPostCode());
			userList.addAll(userService.getPrepareUser(user));
		}
		return userList;
	}
}
