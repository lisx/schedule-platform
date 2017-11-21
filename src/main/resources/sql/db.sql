-- ----------------------------
-- Table structure for grouping
-- ----------------------------
DROP TABLE IF EXISTS `grouping`;
CREATE TABLE `grouping` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(50) DEFAULT NULL,
  `group_code` varchar(50) DEFAULT NULL,
  `group_order` int(11) DEFAULT '0',
  `creator_id` int(11) DEFAULT '0',
  `updator_id` int(11) DEFAULT '0',
  `is_deleted` int(1) DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of grouping
-- ----------------------------
INSERT INTO `grouping` VALUES ('1', '8号线永泰庄站区', '000001', '1', '999', '999', '0', '2017-05-21 10:06:30', '2017-05-21 10:06:30');
INSERT INTO `grouping` VALUES ('2', '育新站', '000001001', '2', '999', '999', '0', '2017-05-21 10:06:30', '2017-05-21 10:06:30');
INSERT INTO `grouping` VALUES ('3', '西小口站', '000001002', '3', '999', '999', '0', '2017-05-21 10:06:30', '2017-05-21 10:06:30');
INSERT INTO `grouping` VALUES ('4', '永泰庄站', '000001003', '4', '999', '999', '0', '2017-05-21 10:06:30', '2017-05-21 10:06:30');
INSERT INTO `grouping` VALUES ('5', '林萃桥站', '000001004', '5', '999', '999', '0', '2017-05-21 10:06:30', '2017-05-21 10:06:30');
INSERT INTO `grouping` VALUES ('6', '森林公园南门站', '000001005', '6', '999', '999', '0', '2017-05-21 10:06:30', '2017-05-21 10:06:30');

-- ----------------------------
-- Table structure for permission
-- ----------------------------
DROP TABLE IF EXISTS `permission`;
CREATE TABLE `permission` (
  `permission_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限ID',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单、连接、按钮权限',
  `permission_str` varchar(50) DEFAULT NULL COMMENT '菜单权限辨识字符串',
  `parent_permission_id` int(11) DEFAULT NULL COMMENT '父级菜单ID',
  `parent_permission_name` varchar(50) DEFAULT NULL COMMENT '父级菜单名字',
  `comment` text COMMENT '备注,说明',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人ID',
  `created_at` datetime DEFAULT NULL COMMENT '创建时间',
  `is_deleted` varchar(10) DEFAULT '0' COMMENT '删除标记',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of permission
-- ----------------------------
INSERT INTO `permission` VALUES ('1', '权限基础字段', '权限基础字段', '1', null, null, '1', '2017-05-31 10:37:47', '0');
INSERT INTO `permission` VALUES ('2', '系统设置', '系统设置', '1', null, null, '1', '2017-05-31 10:39:45', '0');
INSERT INTO `permission` VALUES ('3', '人员管理', '系统设置-人员管理', '2', '系统设置', null, '1', '2017-05-31 10:37:47', '0');
INSERT INTO `permission` VALUES ('4', '新增人员', '人员管理-新增人员', '3', '人员管理', null, '1', '2017-05-31 11:47:35', '0');
INSERT INTO `permission` VALUES ('5', '编辑人员', '人员管理-编辑人员', '3', '人员管理', null, '1', '2017-05-31 11:47:37', '0');
INSERT INTO `permission` VALUES ('6', '删除人员', '人员管理-删除人员', '3', '人员管理', null, '1', '2017-05-31 11:48:36', '0');
INSERT INTO `permission` VALUES ('7', '导入人员', '人员管理-导入人员', '3', '人员管理', null, '1', '2017-05-31 11:49:40', '0');
INSERT INTO `permission` VALUES ('8', '人员分组', '系统设置-人员分组', '2', '系统设置', null, '1', '2017-05-31 10:39:45', '0');
INSERT INTO `permission` VALUES ('9', '新增站区', '人员分组-新增站区', '8', '人员分组', null, '1', '2017-05-31 11:50:19', '0');
INSERT INTO `permission` VALUES ('10', '修改站区', '人员分组-修改站区', '8', '人员分组', null, '1', '2017-05-31 11:50:49', '0');
INSERT INTO `permission` VALUES ('11', '删除站区', '人员分组-删除站区', '8', '人员分组', null, '1', '2017-05-31 11:51:45', '0');
INSERT INTO `permission` VALUES ('12', '新增站点', '人员分组-新增站点', '8', '人员分组', null, '1', '2017-05-31 11:50:19', '0');
INSERT INTO `permission` VALUES ('13', '修改站点', '人员分组-修改站点', '8', '人员分组', null, '1', '2017-05-31 11:50:49', '0');
INSERT INTO `permission` VALUES ('14', '删除站点', '人员分组-删除站点', '8', '人员分组', null, '1', '2017-05-31 11:51:45', '0');
INSERT INTO `permission` VALUES ('15', '权限管理', '系统设置-权限管理', '2', '系统设置', null, '1', '2017-05-31 10:40:30', '0');
INSERT INTO `permission` VALUES ('16', '新增权限', '权限管理-新增权限', '15', '权限管理', null, '1', '2017-05-31 11:50:19', '0');
INSERT INTO `permission` VALUES ('17', '修改权限', '权限管理-修改权限', '15', '权限管理', null, '1', '2017-05-31 11:50:49', '0');
INSERT INTO `permission` VALUES ('18', '删除权限', '权限管理-删除权限', '15', '权限管理', null, '1', '2017-05-31 11:51:45', '0');
INSERT INTO `permission` VALUES ('19', '排班管理', '排班管理', '1', null, null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('20', '排班表格', '排班管理-排班表格', '19', '排班管理', null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('21', '新增排班', '排班管理-新增排班', '19', '排班管理', null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('22', '排班设置', '排班设置', '1', null, null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('23', '班次设置', '排班设置-班次设置', '22', '排班设置', null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('24', '新增班制', '班次设置-新增班制', '23', '班次设置', null, '1', '2017-05-31 11:50:19', '0');
INSERT INTO `permission` VALUES ('25', '修改班制', '班次设置-修改班制', '23', '班次设置', null, '1', '2017-05-31 11:50:49', '0');
INSERT INTO `permission` VALUES ('26', '删除班制', '班次设置-删除班制', '23', '班次设置', null, '1', '2017-05-31 11:51:45', '0');
INSERT INTO `permission` VALUES ('27', '岗位设置', '排班设置-岗位设置', '22', '排班设置', null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('28', '新增岗位', '岗位设置-新增岗位', '27', '岗位设置', null, '1', '2017-05-31 11:50:19', '0');
INSERT INTO `permission` VALUES ('29', '修改岗位', '岗位设置-修改岗位', '27', '岗位设置', null, '1', '2017-05-31 11:50:49', '0');
INSERT INTO `permission` VALUES ('30', '删除岗位', '岗位设置-删除岗位', '27', '岗位设置', null, '1', '2017-05-31 11:51:45', '0');
INSERT INTO `permission` VALUES ('31', '统计报表', '统计报表', '1', null, null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('32', '工时报表', '统计报表-工时报表', '31', '统计报表', null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('33', '临时报表', '统计报表-临时报表', '31', '统计报表', null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('34', '流程设置', '排班设置-流程设置', '22', '排班设置', null, '1', '2017-05-31 10:40:59', '0');
INSERT INTO `permission` VALUES ('35', '新增流程', '流程设置-新增流程', '34', '流程设置', null, '1', '2017-05-31 11:50:19', '0');
INSERT INTO `permission` VALUES ('36', '修改流程', '流程设置-修改流程', '34', '流程设置', null, '1', '2017-05-31 11:50:49', '0');
INSERT INTO `permission` VALUES ('37', '删除流程', '流程设置-删除流程', '34', '流程设置', null, '1', '2017-05-31 11:51:45', '0');
-- ----------------------------
-- Table structure for post_setting
-- ----------------------------
DROP TABLE IF EXISTS `post_setting`;
CREATE TABLE `post_setting` (
  `post_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_name` varchar(50) DEFAULT NULL,
  `post_code` varchar(50) DEFAULT NULL,
  `is_backup_position` varchar(5) DEFAULT NULL,
  `max_weekly_reason` int(11) DEFAULT NULL,
  `min_weekly_reason` int(11) DEFAULT NULL,
  `post_month` int(11) DEFAULT '0',
  `post_year` int(11) DEFAULT '0',
  `max_weekly_rest` int(11) DEFAULT NULL,
  `min_weekly_rest` int(11) DEFAULT NULL,
  `creator_id` int(11) NOT NULL DEFAULT '0',
  `updator_id` int(11) DEFAULT '0',
  `if_use` int(1) DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `is_backup_position` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of post_setting
-- ----------------------------
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('站务员', '000001', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:03:29', '2017-06-07 17:30:38');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('值班站长', '000002', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:15', '2017-05-25 20:54:07');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('综控员', '000003', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:59', '2017-05-25 20:54:12');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('站区助理', '000004', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:59', '2017-05-25 20:54:12');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('副站长', '000005', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:59', '2017-05-25 20:54:12');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('站区书记', '000006', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:59', '2017-05-25 20:54:12');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('站区长', '000007', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:59', '2017-05-25 20:54:12');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('替班员', '000008', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:59', '2017-05-25 20:54:12');
INSERT INTO schedule.post_setting (post_name, post_code, max_weekly_reason, min_weekly_reason, post_month, post_year, max_weekly_rest, min_weekly_rest, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('其他人员', '000009', null, null, null, null, null, null, 999, 999, 0, '2017-05-18 15:13:59', '2017-05-25 20:54:12');

-- ----------------------------
-- Table structure for role
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `is_deleted` varchar(10) DEFAULT '0',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('1', '超级管理员', '最高权限', '1', '2016-09-07 10:09:07', '0');

-- ----------------------------
-- Table structure for role_permission
-- ----------------------------
DROP TABLE IF EXISTS `role_permission`;
CREATE TABLE `role_permission` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of role_permission
-- ----------------------------
INSERT INTO `role_permission` VALUES ('1', '2');
INSERT INTO `role_permission` VALUES ('1', '3');
INSERT INTO `role_permission` VALUES ('1', '4');
INSERT INTO `role_permission` VALUES ('1', '5');
INSERT INTO `role_permission` VALUES ('1', '6');
INSERT INTO `role_permission` VALUES ('1', '7');
INSERT INTO `role_permission` VALUES ('1', '8');
INSERT INTO `role_permission` VALUES ('1', '9');
INSERT INTO `role_permission` VALUES ('1', '10');
INSERT INTO `role_permission` VALUES ('1', '11');
INSERT INTO `role_permission` VALUES ('1', '12');
INSERT INTO `role_permission` VALUES ('1', '13');
INSERT INTO `role_permission` VALUES ('1', '14');
INSERT INTO `role_permission` VALUES ('1', '15');
INSERT INTO `role_permission` VALUES ('1', '16');
INSERT INTO `role_permission` VALUES ('1', '17');
INSERT INTO `role_permission` VALUES ('1', '18');
INSERT INTO `role_permission` VALUES ('1', '19');
INSERT INTO `role_permission` VALUES ('1', '20');
INSERT INTO `role_permission` VALUES ('1', '21');
INSERT INTO `role_permission` VALUES ('1', '22');
INSERT INTO `role_permission` VALUES ('1', '23');
INSERT INTO `role_permission` VALUES ('1', '24');
INSERT INTO `role_permission` VALUES ('1', '25');
INSERT INTO `role_permission` VALUES ('1', '26');
INSERT INTO `role_permission` VALUES ('1', '27');
INSERT INTO `role_permission` VALUES ('1', '28');
INSERT INTO `role_permission` VALUES ('1', '29');
INSERT INTO `role_permission` VALUES ('1', '30');
INSERT INTO `role_permission` VALUES ('1', '34');
INSERT INTO `role_permission` VALUES ('1', '35');
INSERT INTO `role_permission` VALUES ('1', '36');
INSERT INTO `role_permission` VALUES ('1', '37');
INSERT INTO `role_permission` VALUES ('1', '31');
INSERT INTO `role_permission` VALUES ('1', '32');
INSERT INTO `role_permission` VALUES ('1', '33');

-- ----------------------------
-- Table structure for schedule_info
-- ----------------------------
DROP TABLE IF EXISTS `schedule_info`;
CREATE TABLE `schedule_info` (
  `schedule_info_id` int(11) NOT NULL AUTO_INCREMENT,
  `log_id` varchar(11) DEFAULT NULL,
  `model_id` varchar(11) DEFAULT NULL,
  `shift_id` varchar(11) DEFAULT NULL,
  `shift_name` varchar(11) DEFAULT NULL,
  `user_id` varchar(11) DEFAULT NULL,
  `user_code` varchar(20) DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  `group_name` varchar(20) DEFAULT NULL,
  `schedule_date` datetime DEFAULT NULL,
  `schedule_day` varchar(11) DEFAULT NULL,
  `schedule_week` varchar(20) DEFAULT NULL,
  `station` varchar(20) DEFAULT NULL,
  `station_area` varchar(20) DEFAULT NULL,
  `schedule_desc` varchar(500) DEFAULT NULL,
  `if_leave` int(1) DEFAULT '0',
  `leave_type` varchar(20) DEFAULT NULL,
  `post_name` varchar(20) DEFAULT NULL,
  `total_at` int(10) DEFAULT '0',
  `if_use` int(1) DEFAULT '0',
  `creator_id` varchar(11) DEFAULT NULL,
  `created_at` varchar(20) DEFAULT NULL,
  `serial_number` varchar(20) DEFAULT NULL,
  `shift_code` varchar(20) DEFAULT NULL,
  `shift_color` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`schedule_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for schedule_log
-- ----------------------------
DROP TABLE IF EXISTS `schedule_log`;
CREATE TABLE `schedule_log` (
  `schedule_log_id` int(11) NOT NULL AUTO_INCREMENT,
  `schedule_info_id` varchar(11) DEFAULT NULL,
  `user_id` varchar(11) DEFAULT NULL,
  `user_name` varchar(20) DEFAULT NULL,
  `log_type` varchar(20) DEFAULT NULL,
  `detail_type` varchar(20) DEFAULT NULL,
  `start_at` varchar(11) DEFAULT NULL,
  `end_at` varchar(11) DEFAULT NULL,
  `time_at` int(11) DEFAULT '0',
  `remark` varchar(500) DEFAULT NULL,
  `if_use` int(1) DEFAULT '0',
  `creator_id` varchar(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `creator_name` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`schedule_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shift_model
-- ----------------------------
DROP TABLE IF EXISTS `shift_model`;
CREATE TABLE `shift_model` (
  `model_id` int(11) NOT NULL AUTO_INCREMENT,
  `model_name` varchar(50) DEFAULT NULL,
  `model_code` varchar(50) DEFAULT NULL,
  `creator_id` int(11) DEFAULT '0',
  `if_use` int(1) DEFAULT '0',
  `post_id` int(11) DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `min_weekly_reason` int(11) DEFAULT NULL,
  `max_weekly_reason` int(11) DEFAULT NULL,
  `min_weekly_rest` int(11) DEFAULT NULL,
  `max_weekly_rest` int(11) DEFAULT NULL,
  `post_month` int(11) DEFAULT NULL,
  `post_year` int(11) DEFAULT NULL,
  `station_area` varchar(50) DEFAULT NULL,
  `station` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shift_population
-- ----------------------------
DROP TABLE IF EXISTS `shift_population`;
CREATE TABLE `shift_population` (
  `population_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_at` int(11) DEFAULT NULL,
  `end_at` int(11) DEFAULT NULL,
  `population_count` int(11) DEFAULT NULL,
  `model_id` varchar(11) DEFAULT NULL,
  `post_id` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`population_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Table structure for shift_setting
-- ----------------------------
DROP TABLE IF EXISTS `shift_setting`;
CREATE TABLE `shift_setting` (
  `shift_id` int(11) NOT NULL AUTO_INCREMENT,
  `shift_name` varchar(50) DEFAULT NULL,
  `shift_num` varchar(50) DEFAULT NULL,
  `shift_color` varchar(20) DEFAULT NULL,
  `shift_code` varchar(20) DEFAULT NULL,
  `shift_explain` varchar(255) DEFAULT NULL,
  `start_at` int(11) DEFAULT NULL,
  `end_at` int(11) DEFAULT NULL,
  `total_at` int(11) DEFAULT '0',
  `interval_at` int(11) DEFAULT '0',
  `relevance_id` varchar(20) DEFAULT NULL,
  `relevance_name` varchar(20) DEFAULT NULL,
  `station` varchar(20) DEFAULT NULL,
  `station_area` varchar(20) DEFAULT NULL,
  `post_id` int(11) DEFAULT NULL,
  `model_id` int(11) DEFAULT NULL,
  `creator_id` int(11) DEFAULT '0',
  `updator_id` int(11) DEFAULT '0',
  `if_use` int(1) DEFAULT '1',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`shift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('大白', '2', null, 420, 1020, 600, 720, null, null, null, null, 1, 1, 999, 0, 0, '2017-05-26 10:54:30', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('下', '2', null, 0, 540, 540, 1080, null, null, null, null, 1, 1, 999, 0, 0, '2017-05-26 10:55:07', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜', '2', null, 1020, 1440, 420, 0, '26', null, null, null, 1, 1, 999, 0, 0, '2017-05-26 10:55:46', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('中白', '1', null, 420, 960, 540, 720, null, null, null, null, 1, 1, 999, 0, 0, '2017-05-26 10:56:31', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('小夜', '2', null, 960, 1260, 300, 720, null, null, null, null, 1, 1, 999, 0, 0, '2017-05-26 10:57:11', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('大白', '2', null, 420, 1020, 600, 720, null, null, null, null, 1, 2, 999, 0, 0, '2017-05-26 10:58:13', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('下', '2', null, 0, 420, 420, 1080, null, null, null, null, 1, 2, 999, 0, 0, '2017-05-26 10:58:39', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜', '2', null, 1020, 1440, 420, 0, '31', '下', null, null, 1, 2, 999, 0, 0, '2017-05-26 10:59:03', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('早', '1', null, 420, 780, 360, 720, null, null, null, null, 1, 2, 999, 0, 0, '2017-05-26 10:59:36', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('中', '1', null, 780, 1140, 360, 720, null, null, null, null, 1, 2, 999, 0, 0, '2017-05-26 11:02:04', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('白班', '2', null, 420, 1020, 600, 0, '36', '夜班', null, null, 3, 3, 999, 0, 0, '2017-05-26 13:48:08', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜班', '2', null, 1020, 420, 840, 2880, null, null, null, null, 3, 3, 999, 0, 0, '2017-05-26 13:49:08', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜班', '2', null, 1020, 420, 840, 2880, null, null, null, null, 3, 4, 999, 0, 0, '2017-05-26 13:51:45', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('白班', '2', null, 420, 1020, 600, 0, '38', '夜班', null, null, 3, 4, 999, 0, 0, '2017-05-26 13:52:14', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('白班', '2', null, 420, 1020, 600, 0, null, null, null, null, 3, 5, 999, 0, 0, '2017-05-26 13:52:38', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜班', '2', null, 1020, 420, 840, 2880, null, null, null, null, 3, 5, 999, 0, 0, '2017-05-26 13:53:04', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜班', '2', null, 1020, 420, 840, 2880, null, null, null, null, 3, 6, 999, 0, 0, '2017-05-26 13:53:20', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('白班', '2', null, 420, 1020, 600, 0, '42', '夜班', null, null, 3, 6, 999, 0, 0, '2017-05-26 13:53:44', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜班', '1', null, 1020, 420, 840, 2880, null, null, null, null, 2, 7, 999, 0, 0, '2017-05-26 19:08:47', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('白班', '1', null, 420, 1020, 600, 0, '44', '夜班', null, null, 2, 7, 999, 0, 0, '2017-05-26 19:09:10', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('大白', '2', null, 540, 900, 360, 540, null, null, null, null, 1, 8, 999, 0, 0, '2017-05-31 17:30:24', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('下', '2', null, 0, 540, 540, 1440, null, null, null, null, 1, 8, 999, 0, 0, '2017-05-31 17:30:42', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('夜', '2', null, 1020, 1440, 420, 0, '47', '下', null, null, 1, 8, 999, 0, 0, '2017-05-31 17:31:01', null);
INSERT INTO schedule.shift_setting (shift_name, shift_num, shift_color, start_at, end_at, total_at, interval_at, relevance_id, relevance_name, station, station_area, post_id, model_id, creator_id, updator_id, if_use, created_at, updated_at) VALUES ('中', '1', null, 600, 1080, 480, 360, null, null, null, null, 1, 8, 999, 0, 0, '2017-05-31 17:31:25', null);
-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `password` varchar(100) NOT NULL,
  `user_pass` varchar(100) NOT NULL,
  `secret_key` varchar(64) DEFAULT NULL,
  `user_code` varchar(50) DEFAULT NULL COMMENT '工号',
  `user_job` varchar(64) DEFAULT NULL,
  `user_name` varchar(30) DEFAULT NULL,
  `group_name` varchar(30) DEFAULT NULL,
  `station_area` varchar(30) DEFAULT NULL,
  `station` varchar(30) DEFAULT '1',
  `created_at` datetime DEFAULT NULL,
  `creator_id` varchar(30) DEFAULT NULL,
  `is_deleted` int(1) DEFAULT '0',
  `is_admin` int(1) DEFAULT '0',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('999', 'e7b68248d0782b49f0d4efaefb0e508c', '123456', 'cb7e52304f0d11e6965c00ff2c2e2b3f', 'admin', null, '赵世令', null, null, null, '2015-10-10 12:14:17', '0', '0', '1');

-- ----------------------------
-- Table structure for user_role
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('999', '1');

-- ----------------------------
-- Table structure for workflow
-- ----------------------------
DROP TABLE IF EXISTS `workflow`;
CREATE TABLE `workflow` (
  `workflow_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) DEFAULT NULL,
  `model_id` int(11) DEFAULT NULL,
  `shift_id` int(11) DEFAULT NULL,
  `serial_number` varchar(20) DEFAULT NULL,,
  `creator_id` int(11) DEFAULT NULL,
  `updator_id` int(11) DEFAULT NULL,
  `is_deleted` int(1) DEFAULT '0' NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`workflow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ------------------------------------
-- Table structure for workflow_content
-- ------------------------------------
DROP TABLE IF EXISTS `workflow_content`;
CREATE TABLE `workflow_content` (
  `content_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_time` int(11) DEFAULT NULL,
  `end_time` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `updator_id` int(11) DEFAULT NULL,
  `is_deleted` int(1) DEFAULT '0' NOT NULL ,
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `workflow_id` int(11) DEFAULT NULL,
  `start_location` int(11) DEFAULT NULL,
  `end_location` int(11) DEFAULT NULL,
  `rows_num` int(11) DEFAULT NULL,
  `content_color` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ------------------------------------------
-- Table structure for schedule_info_template
-- ------------------------------------------
DROP TABLE IF EXISTS `schedule_info_template`;
CREATE TABLE `schedule_info_template` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `model_id` varchar(20) DEFAULT NULL,
  `shift_id` varchar(20) DEFAULT NULL,
  `shift_name` varchar(255) DEFAULT NULL,
  `shift_color` varchar(6) DEFAULT NULL,
  `shift_minutes` int(11) DEFAULT NULL,
  `week_number` int(11) DEFAULT NULL,
  `week_day` int(11) DEFAULT NULL,
  `order_index` int(11) DEFAULT NULL,
  `updator_id` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
SET FOREIGN_KEY_CHECKS=1;