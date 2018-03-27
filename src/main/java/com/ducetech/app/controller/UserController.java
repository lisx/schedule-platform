package com.ducetech.app.controller;


import com.alibaba.fastjson.JSONArray;
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
import com.ducetech.framework.util.*;
import com.ducetech.framework.web.view.OperationResult;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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
import java.util.*;


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
     *
     * @param request
     * @return
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request) {
        if (request.getSession().getAttribute("DT_LOGIN_NAME") != null) {
            return "redirect:/";
        }
        return "login";
    }


    @RequestMapping(value = "/user/createImpFile", method = RequestMethod.POST)
    @ResponseBody
    @Transactional
    public OperationResult createImpFile(HttpServletRequest request, @RequestParam("fileUpload") CommonsMultipartFile fileUpload) throws UserException {
        User u = getLoginUser(request);
        StringBuffer sb=new StringBuffer();
        try {
            if (fileUpload != null && !fileUpload.isEmpty()) {
                List<List<List<String>>> data = PoiUtil.readExcelToList(fileUpload, 1);
                if (null != data && !data.isEmpty()) {
                    for (List<List<String>> sheet : data) {
                        if (null != sheet && !sheet.isEmpty()) {
//                            for (List<String> row : sheet) {
                            for(int i=0;i<sheet.size();i++){
                                List<String> row=sheet.get(i);
                                String userCode = ExtStringUtil.trim(row.get(0));
                                if(!StringUtils.isBlank(userCode)||userCode.length()!=8){
                                    sb.append("第"+(i+2)+"行员工卡号错误;");
                                }
                                String userName = ExtStringUtil.trim(row.get(1));
                                String gender = ExtStringUtil.trim(row.get(2));
                                String stationArea = u.getStationArea();
                                String station = ExtStringUtil.trim(row.get(3));
                                if (StringUtils.isNotBlank(station)){
                                    station = checkStationArea(station);
                                }
                                String phone = ExtStringUtil.trim(row.get(5));
//                                if(isEmptyUser(userCode,userName,gender,station,phone)){
//                                    continue;
//                                }
//                                if(station!=null&&!station.startsWith(stationArea)){
//                                    continue;
//                                }
                                String userJob = ExtStringUtil.trim(row.get(4));
                                String addr = ExtStringUtil.trim(row.get(6));
                                String idCode = ExtStringUtil.trim(row.get(7));
                                if(idCode.length()!=18){
                                    int index=sb.toString().lastIndexOf("第");
                                    int rowIndex=sb.toString().lastIndexOf("行");
                                    String numstr=sb.toString().substring(index+1,rowIndex);
                                    int num=Integer.parseInt(numstr);
                                    if(num==(i+2)){
                                        sb.append("身份证号码错误;");
                                    }else {
                                        sb.append("第" + (i + 2) + "行身份证号码错误;");
                                    }
                                }
                                String married = ExtStringUtil.trim(row.get(8));
                                String child = ExtStringUtil.trim(row.get(9));
                                String edu = ExtStringUtil.trim(row.get(10));
                                String xfzNo = ExtStringUtil.trim(row.get(11));
                                String certNo = ExtStringUtil.trim(row.get(12));
                                String certLevel = ExtStringUtil.trim(row.get(13));
                                String zwyNo = ExtStringUtil.trim(row.get(14));
                                String zwyLevel = ExtStringUtil.trim(row.get(15));
                                String recruitDate = ExtStringUtil.trim(row.get(16));
                                String political = ExtStringUtil.trim(row.get(17));
                                String joinDate = ExtStringUtil.trim(row.get(18));
                                if (!ExtStringUtil.isBlank(userJob)) {
                                    PostSetting postSetting = postSettingService.selectPostSettingByPostName(userJob);
                                    if (null != postSetting) {
                                        userJob = postSetting.getPostCode();
                                    } else {
                                        userJob = "";
                                    }
                                }
                                if(StringUtils.isBlank(sb.toString())) {
                                    User user = new User();
                                    user.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
                                    user.setOnBoardDate(recruitDate);
                                    user.setPhoneNumber(phone);
                                    user.setHomeAddress(addr);
                                    user.setBirthday(getBirthday(idCode));
                                    user.setIdCode(idCode);
                                    user.setIsMarried(married);
                                    user.setHasChild(child);
                                    user.setGender(gender);
                                    user.setEduBackGround(edu);
                                    user.setIsPartyMember(political);
                                    user.setJoinDate(joinDate);
                                    user.setCertLevel(certLevel);
                                    user.setCertNo(certNo);
                                    user.setXfzNo(xfzNo);
                                    user.setZwyNo(zwyNo);
                                    user.setZwyLevel(zwyLevel);
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
                }
                if(StringUtils.isBlank(sb.toString()))
                return OperationResult.buildFailureResult("文件处理成功", "success");
            }
        } catch (Exception e) {
            throw new ServiceException("Upload file error", e);
        }
        return OperationResult.buildFailureResult(sb.toString()+"请修改后重新导入.", "fail");
    }

    private boolean isEmptyUser(String userCode, String userName, String gender, String station, String phone) {
        if(StringUtils.isBlank(userCode+userName+gender+station+phone)){
            return true;
        }
        return false;
    }

    private String getBirthday(String idCode) {
        if(StringUtils.isBlank(idCode)||idCode.length()!=18){
            return "";
        }
        String birth=idCode.substring(6,14);
        return birth.substring(0,4)+"-"+birth.substring(4,6)+"-"+birth.substring(6);
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
        UsernamePasswordToken token = new UsernamePasswordToken(userCode, password);
        if (!ExtStringUtil.isBlank(userCode) && !ExtStringUtil.isBlank(password)) {
            try {
                subject.login(token);
                User user = (User) subject.getPrincipal();
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
     * @param model
     * @return String
     * @Title: person
     * @Description: 跳转人员首页
     */
    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String person(HttpServletRequest request, Model model) {
        User loginUser = getLoginUser(request);
        Role role = new Role();
        role.setIsDeleted("0");
        List<Grouping> groups = null;

        if(StringUtils.isBlank(loginUser.getStationArea())&&!loginUser.getIsAdmin().equals("0")){
            groups = groupingService.queryGroupsByLength(6);
        }else if (StringUtils.isBlank(loginUser.getStationArea())) {
            groups = new ArrayList<>();
        } else {
            groups = groupingService.selectByParentCode(loginUser.getStationArea(), 9);
        }
        List<PostSetting> posts = postSettingService.queryAll();
        List<Role> roles = roleService.getRoleByQuery(role);
        model.addAttribute("roles", roles);
        model.addAttribute("groups", groups);
        model.addAttribute("posts", posts);
        return "/user/index";
    }

    /**
     * @return void
     * @throws Exception
     * @Title: personData
     * @Description: 人员首页数据
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    @ResponseBody
    public Object personData(HttpServletRequest request) throws Exception {
        User loginUser = getLoginUser(request);
        List<User> rs = userService.getUsersByStationArea(loginUser.getStationArea());
        List<PostSetting> allPostSettings = postSettingService.getAllPostSettings();
        Map<String,PostSetting> postMap=new HashMap<>();
        for (PostSetting p :
                allPostSettings) {
            postMap.put(p.getPostCode(),p);
        }
        List<Grouping> allGroupings = groupingService.getAllGroupings();
        Map<String,Grouping> groupMap=new HashMap<>();
        for (Grouping g :
                allGroupings) {
            groupMap.put(g.getGroupCode(),g);
        }
        JSONArray array = new JSONArray();
        for (User u : rs) {
            JSONArray a = new JSONArray();
            String action = "<a href='javascript:;'" + (u.getIsDeleted() == 0 ? "onclick='editUser(" + u.getUserId() + ")' class='edit'" : "class='edit disabled'")
                    + ">编辑</a><a class='stop' href='javascript:;' onclick='userDel(" + u.getUserId() + ")'>删除</a>";
            a.add(action);
            a.add(u.getUserCode());
            a.add(u.getUserName());

            a.add(u.getIdCode());
            a.add(u.getOnBoardDate());
            a.add(u.getIsMarried());
            a.add(u.getHasChild());
            a.add(u.getEduBackGround());
            a.add(u.getIsPartyMember());
            a.add(u.getJoinDate());
            a.add(u.getHomeAddress());
            a.add(u.getCertNo());
            a.add(u.getCertLevel());

            a.add(u.getGender());
            a.add(u.getPhoneNumber());
            a.add(u.getBirthday());
            PostSetting p = postMap.get(u.getUserJob());
            a.add(p==null?"":p.getPostName());
            Grouping g=groupMap.get(u.getStation());
            a.add(g==null?"":g.getGroupName());
            a.add(u.getIsAdmin());

            array.add(a);
        }
        return array;
    }

    /**
     * 新增
     *
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
        setStationArea(userInfo,user);
        user.setIsDeleted(0);
        user.setCreatorId(userInfo.getUserId());
        user.setCreatedAt(DateUtil.formatDate(new Date(), DateUtil.DEFAULT_TIME_FORMAT));
        userService.addUser(user);
        return OperationResult.buildSuccessResult("操作成功", "success");
    }

    /**
     * 编辑
     *
     * @param userId
     */
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    @ResponseBody
    public User edit(@PathVariable(value = "id") String userId) throws Exception {
        User user = new User();
        if (!ExtStringUtil.isBlank(userId)) {
            user = userService.getUserByUserId(userId);
        }

        return user;
    }

    /**
     * 更新
     *
     * @param user
     */
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult update(User user, HttpServletRequest request) throws Exception {
        User userInfo = getLoginUser(request);
        User _user = userService.getUserByUserId(user.getUserId());
        if (!ExtStringUtil.isBlank(user.getPassword())) {
            user.setUserPass(user.getPassword());
            user.setPassword(Digests.md5Hash(user.getPassword(), _user.getSecretKey()));
        }
        setStationArea(userInfo,user);
        userService.updateUser(user);
        return OperationResult.buildSuccessResult("更新成功", "success");
    }

    private void setStationArea(User loginUser,User custom){
        if(StringUtils.isNotBlank(custom.getStation())&&custom.getStation().length()==6){
            custom.setStationArea(custom.getStation());
            custom.setStation(null);
            return ;
        }
        if(StringUtils.isNotBlank(loginUser.getStationArea())){
            custom.setStationArea(loginUser.getStationArea());
        }else {
            if (StringUtils.isBlank(custom.getStation())){
                throw new RuntimeException("没有分配站区");
            }
            Grouping grouping = groupingService.selectGroupingByGroupCode(custom.getStation().substring(0, 6));
            if(grouping==null){
                throw new RuntimeException("没有分配站区");
            }
            custom.setStationArea(grouping.getGroupCode());
        }
    }
    /**
     * 删除
     *
     * @param userId
     */
    @RequestMapping(value = "/users/{id}/userDel", method = RequestMethod.PUT)
    @ResponseBody
    public OperationResult userDel(@PathVariable(value = "id") String userId) throws Exception {
        userService.deleteUserById(userId, "1");
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
        for (PostSetting ps : backupPostSettings) {
            user.setUserJob(ps.getPostCode());
            userList.addAll(userService.getPrepareUser(user));
        }
        return userList;
    }

    public static void main(String [] z){
        List<String> homework=new ArrayList<>(500);
        for(int i=0;i<150;i++){
            int a= RandomUtils.nextInt(0,11);
            int c=RandomUtils.nextInt(0,11);
            StringBuilder sb=new StringBuilder();
            sb.append(a);
            sb.append("+");
            sb.append(c);
            sb.append("=");
            homework.add(sb.toString());
        }
        for(int i=0;i<150;i++){
            int a= RandomUtils.nextInt(0,21);
            int c=RandomUtils.nextInt(0,a+1);
            StringBuilder sb=new StringBuilder();
            sb.append(a);
            sb.append("-");
            sb.append(c);
            sb.append("=");
            homework.add(sb.toString());
        }
        for(int i=0;i<200;i++){
            int a= RandomUtils.nextInt(0,21);
            boolean b=RandomUtils.nextInt(0,2)==0;
            int c=b?RandomUtils.nextInt(0,21):RandomUtils.nextInt(0,a+1);
            StringBuilder sb=new StringBuilder();
            sb.append(a);
            sb.append(b?"+":"-");
            sb.append(c);
            sb.append("=");
            homework.add(sb.toString());
        }
        for(int i=0;i<100;i++){
            int a= RandomUtils.nextInt(0,51);
            boolean b=RandomUtils.nextInt(0,2)==0;
            int c=b?RandomUtils.nextInt(0,50):RandomUtils.nextInt(0,a+1);
            StringBuilder sb=new StringBuilder();
            sb.append(a);
            sb.append(b?"+":"-");
            sb.append(c);
            sb.append("=");
            homework.add(sb.toString());
        }
        for(int i=0;i<homework.size();i++){
            System.out.print(homework.get(i));
            System.out.print("\t\t\t");
            if(i%5==4){
                System.out.println();
            }
        }
    }
}
