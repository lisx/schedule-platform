package com.ducetech.app.model;

import com.ducetech.framework.model.BaseModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class User extends BaseModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String userId; 			// 用户ID,即用户的唯一标识

	private String password; 		// 用户密码,由于采用shiro自带的MD5加密算法

    private String userPass;

	private String secretKey;		//秘钥,用作生成密码中的盐
	
	private String userCode; 	    // 工号

    private String userJob;         //用户岗位

	private String userName; 		// 真实姓名

	private String stationArea; 	// 站区

	private String station;	        //站点

	private List<Role> roles; 		// 用户拥有的角色列表
	
	private String roleIds;			//角色IDS

	private String roleNames;		//角色名字

	private String birthday;

	private String idCode;

	private String onBoardDate;

	private String isMarried;

	private String hasChild;

	private String eduBackGround;

	private String isPartyMember;

	private String joinDate;

	private String phoneNumber;

	private String gender;

	private String homeAddress;

    private String certNo;

    private String certLevel;

	private String creatorId;		//创建人ID

	private String createdAt;		//创建时间

	private int isDeleted;		//删除标记	0启用	1停用	默认0启用

    private String isAdmin;            //是否是管理员 1是管理员 0是普通 默认0
}
