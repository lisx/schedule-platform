package com.ducetech.app.service.impl;

import com.ducetech.app.dao.GroupingDAO;
import com.ducetech.app.dao.PostSettingDAO;
import com.ducetech.app.dao.UserDAO;
import com.ducetech.app.model.Grouping;
import com.ducetech.app.model.PostSetting;
import com.ducetech.app.model.Role;
import com.ducetech.app.model.User;
import com.ducetech.app.service.RoleService;
import com.ducetech.app.service.UserService;
import com.ducetech.framework.model.PagerRS;
import com.ducetech.framework.model.BaseQuery;
import com.ducetech.framework.util.Digests;
import com.ducetech.framework.util.ExtStringUtil;
import com.ducetech.framework.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserServiceImpl implements UserService {


    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GroupingDAO groupingDAO;

    @Autowired
    private PostSettingDAO postSettingDAO;
	
	@Autowired
	private RoleService roleService;
	
	@Override
	public User getUserByUserName(String userName) {
		return userDAO.selectUserByUserName(userName);
	}
    @Override
	public List<User> importFile(MultipartFile mFile, String rootPath){
        List<User> secUserList = new ArrayList<>();

        String fileName = mFile.getOriginalFilename();
        String suffix = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
        String ym = new SimpleDateFormat("yyyy-MM").format(new Date());
        String filePath = "uploadFile/" + ym + fileName;
        try {
            File file = new File(rootPath + filePath);
            if (file.exists()) {
                file.delete();
                file.mkdirs();
            }else {
                file.mkdirs();
            }
            mFile.transferTo(file);

            if ("xls".equals(suffix) || "XLS".equals(suffix)) {
                secUserList = importXls(file);
            }else if ("xlsx".equals(suffix) || "XLSX".equals(suffix)) {
                secUserList = importXlsx(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return secUserList;
    }

    private List<User> importXls(File file) {
        List<User> secUserList = new ArrayList<User>();

        InputStream is = null;
        HSSFWorkbook hWorkbook = null;
        try {
            is = new FileInputStream(file);
            hWorkbook = new HSSFWorkbook(is);
            HSSFSheet hSheet = hWorkbook.getSheetAt(0);

            if (null != hSheet){
                for (int i = 1; i < hSheet.getPhysicalNumberOfRows(); i++){
                    User su = new User();
                    HSSFRow hRow = hSheet.getRow(i);
                    su.setUserName(hRow.getCell(0).toString());
                    su.setUserCode(hRow.getCell(1).toString());
                    su.setStationArea(hRow.getCell(2).toString());
                    userDAO.insertUser(su);
                    secUserList.add(su);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return secUserList;
    }
    private List<User> importXlsx(File file) {
        List<User> secUserList = new ArrayList<User>();

        InputStream is = null;
        XSSFWorkbook xWorkbook = null;
        try {
            is = new FileInputStream(file);
            xWorkbook = new XSSFWorkbook(is);
            XSSFSheet xSheet = xWorkbook.getSheetAt(0);

            if (null != xSheet) {
                for (int i = 1; i < xSheet.getPhysicalNumberOfRows(); i++) {
                    User su = new User();
                    XSSFRow hRow = xSheet.getRow(i);
                    su.setUserName(hRow.getCell(0).toString());
                    su.setUserCode(hRow.getCell(1).toString());
                    su.setStationArea(hRow.getCell(2).toString());
                    su.setPassword(genRandomNum(6));
                    userDAO.insertUser(su);
                    secUserList.add(su);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (null != is) {
                try {
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return secUserList;
    }

    public String genRandomNum(int pwd_len){
        //35是因为数组是从0开始的，26个字母+10个数字
        final int  maxNum = 36;
        int i;  //生成的随机数
        int count = 0; //生成的密码的长度
        char[] str = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k',
                'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w',
                'x', 'y', 'z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

        StringBuffer pwd = new StringBuffer("");
        Random r = new Random();
        while(count < pwd_len){
            //生成随机数，取绝对值，防止生成负数，

            i = Math.abs(r.nextInt(maxNum));  //生成的数最大为36-1

            if (i >= 0 && i < str.length) {
                pwd.append(str[i]);
                count ++;
            }
        }

        return pwd.toString();
    }

    @Override
    public User getUserByUserCode(String userCode) {
        return userDAO.selectUserByUserCode(userCode);
    }

    @Override
	public List<String> getUserPermission(String userId) {
		return userDAO.selectPermissionByUserId(userId);
	}
	
	@Override
	public List<User> getUsersByStationArea(String stationArea) {
		User user = new User();
		user.setStationArea(stationArea);
		return userDAO.selectUser(user);
	}
    @Override
    public List<User> getUsersByStation(String station) {
        User user = new User();
        user.setStation(station);
        return userDAO.selectUser(user);
    }

	@Override
	public User getUserByUserId(String userId) {
		User user = userDAO.selectUserByUserId(userId);
		List<Role> roles = roleService.getRolesByUserId(userId);
		user.setRoles(roles);
        if (null != roles && roles.size() > 0) {
            String str = "";
            for (Role role : roles) {
                str += (role.getRoleId() + ",");
            }
            if (!"".equals(str)) {
                str = str.substring(0, str.length() -1);
            }
            user.setRoleIds(str);
        }
		return user;
	}

	@Override
	@Transactional
	public void addUser(User user) {
		user.setSecretKey(RandomUtil.UID());
		user.setPassword(Digests.md5Hash(user.getPassword(), user.getSecretKey()));
		userDAO.insertUser(user);
		roleService.updateUserRoles(user.getUserId(), user.getRoleIds());
	}
	
	@Override
	@Transactional
	public void passwordReset(String userId, String password) {
		User user = userDAO.selectUserByUserId(userId);
		user.setPassword(password);
		userDAO.updateUser(user);
	}

	@Override
	public void deleteUserById(String userId,String isDeleted) {
		userDAO.deleteUserById(userId,isDeleted);
	}

	@Override
	public List<User> getAllUsers() {
		return this.getUserByPager(new BaseQuery<User>(new User())).getResults();
	}

	@Override
	public List<User> getUserByQuery(User user){
		return userDAO.selectUser(user);
	}
	
	@Override
	public PagerRS<User> getUserByPager(BaseQuery<User> query) {
		if( query != null && query.getPage() > 0 ){			//如果传入offset大于0,则启用分页查询，否则不启用
			PageHelper.startPage(query.getPage(), query.getRows(), true);
		}
		List<User> userList = userDAO.selectUser(query.getT());
        for (User user : userList) {
            if (!ExtStringUtil.isBlank(user.getStationArea())) {
                Grouping group = groupingDAO.selectGroupingByGroupCode(user.getStationArea());
                if (null != group) {
                    user.setStationArea(group.getGroupName());
                }
            }
            if (!ExtStringUtil.isBlank(user.getStation())) {
                Grouping group = groupingDAO.selectGroupingByGroupCode(user.getStation());
                if (null != group) {
                    user.setStation(group.getGroupName());
                }
            }
            if (!ExtStringUtil.isBlank(user.getUserJob())) {
                PostSetting postSet = postSettingDAO.selectPostSettingByPostCode(user.getUserJob());
                if (null != postSet) {
                    user.setUserJob(postSet.getPostName());
                }
            }
        }
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageInfo page = new PageInfo(userList);
		PagerRS<User> pagerRS = new PagerRS<>(userList, page.getTotal(), page.getPages());
		return pagerRS;
	}

	@Override
	@Transactional
	public void updateUser(User user) {
		userDAO.updateUser(user);
		roleService.updateUserRoles(user.getUserId(), user.getRoleIds());
	}

	@Override
	public List<User> getUsersByRoleId(String roleId) {
		return userDAO.selectUsersByRoleId(roleId);
	}

	@Override
	public PagerRS<User> getUsersByRoleIdPage(BaseQuery<Role> query) {
		if( query != null && query.getPage() > 0 ){			//如果传入offset大于0,则启用分页查询，否则不启用
			PageHelper.startPage(query.getPage(), query.getRows(), true);
		}
		List<User> userList = userDAO.selectUsersByRoleId(query.getT().getRoleId());
		for (User user : userList) {
		}
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageInfo page = new PageInfo(userList);
		PagerRS<User> pagerRS = new PagerRS<User>(userList, page.getTotal(), page.getPages());
		return pagerRS;
	}

	@Override
	public void updateUserPassword(User user) {
		userDAO.updateUser(user);
	}

	@Override
	public String resetPass(User user) {
	    String pwd = genRandomNum(6);
        user.setPassword(pwd);
	    userDAO.updateUser(user);
	    return pwd;
	}

	@Override
	public void updateUserStatus(User user) {
		userDAO.updateUser(user);
	}
	@Override
    public List<User> selectUserByStation(String station){
        return userDAO.selectUserByStation(station);
    }

    @Override
    public int getUserCountByStation(String station) {
        return userDAO.selectUserCountByStation(station);
    }

    @Override
    public List<User> getPrepareUser(User user) {
        return userDAO.getPrepareUser(user);
    }

    @Override
    public List<User> getLeaveUser(User user) {
        return userDAO.getLeaveUser(user);
    }
}
