-- MySQL dump 10.13  Distrib 5.7.18, for macos10.12 (x86_64)
--
-- Host: 127.0.0.1    Database: schedule
-- ------------------------------------------------------
-- Server version	5.7.18

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `grouping`
--

DROP TABLE IF EXISTS `grouping`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grouping`
--

LOCK TABLES `grouping` WRITE;
/*!40000 ALTER TABLE `grouping` DISABLE KEYS */;
INSERT INTO `grouping` (`group_id`, `group_name`, `group_code`, `group_order`, `creator_id`, `updator_id`, `is_deleted`, `created_at`, `updated_at`) VALUES (1,'8号线永泰庄站区','000001',1,999,999,0,'2017-05-21 10:06:30','2017-05-21 10:06:30'),(2,'育新站','000001001',2,999,999,0,'2017-05-21 10:06:30','2017-05-21 10:06:30'),(3,'西小口站','000001002',3,999,999,0,'2017-05-21 10:06:30','2017-05-21 10:06:30'),(4,'永泰庄站','000001003',4,999,999,0,'2017-05-21 10:06:30','2017-05-21 10:06:30'),(5,'林萃桥站','000001004',5,999,999,0,'2017-05-21 10:06:30','2017-05-21 10:06:30'),(6,'森林公园南门站','000001005',6,999,999,0,'2017-05-21 10:06:30','2017-05-21 10:06:30'),(7,'13号线霍营站区','000002',0,999,0,0,'2017-07-14 13:49:23',NULL),(8,'龙泽站','000002001',0,999,0,0,'2017-07-14 13:49:35',NULL),(9,'2号线西直门站区','000003',0,999,0,0,'2017-07-15 19:33:40',NULL),(10,'西直门站','000003001',0,999,0,0,'2017-07-15 19:33:50',NULL);
/*!40000 ALTER TABLE `grouping` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permission`
--

DROP TABLE IF EXISTS `permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permission`
--

LOCK TABLES `permission` WRITE;
/*!40000 ALTER TABLE `permission` DISABLE KEYS */;
INSERT INTO `permission` (`permission_id`, `name`, `permission_str`, `parent_permission_id`, `parent_permission_name`, `comment`, `creator_id`, `created_at`, `is_deleted`) VALUES (1,'权限基础字段','权限基础字段',1,NULL,NULL,1,'2017-05-31 10:37:47','0'),(2,'系统设置','系统设置',1,NULL,NULL,1,'2017-05-31 10:39:45','0'),(3,'人员管理','系统设置-人员管理',2,'系统设置',NULL,1,'2017-05-31 10:37:47','0'),(4,'新增人员','人员管理-新增人员',3,'人员管理',NULL,1,'2017-05-31 11:47:35','0'),(5,'编辑人员','人员管理-编辑人员',3,'人员管理',NULL,1,'2017-05-31 11:47:37','0'),(6,'删除人员','人员管理-删除人员',3,'人员管理',NULL,1,'2017-05-31 11:48:36','0'),(7,'导入人员','人员管理-导入人员',3,'人员管理',NULL,1,'2017-05-31 11:49:40','0'),(8,'人员分组','系统设置-人员分组',2,'系统设置',NULL,1,'2017-05-31 10:39:45','0'),(9,'新增站区','人员分组-新增站区',8,'人员分组',NULL,1,'2017-05-31 11:50:19','0'),(10,'修改站区','人员分组-修改站区',8,'人员分组',NULL,1,'2017-05-31 11:50:49','0'),(11,'删除站区','人员分组-删除站区',8,'人员分组',NULL,1,'2017-05-31 11:51:45','0'),(12,'新增站点','人员分组-新增站点',8,'人员分组',NULL,1,'2017-05-31 11:50:19','0'),(13,'修改站点','人员分组-修改站点',8,'人员分组',NULL,1,'2017-05-31 11:50:49','0'),(14,'删除站点','人员分组-删除站点',8,'人员分组',NULL,1,'2017-05-31 11:51:45','0'),(15,'权限管理','系统设置-权限管理',2,'系统设置',NULL,1,'2017-05-31 10:40:30','0'),(16,'新增权限','权限管理-新增权限',15,'权限管理',NULL,1,'2017-05-31 11:50:19','0'),(17,'修改权限','权限管理-修改权限',15,'权限管理',NULL,1,'2017-05-31 11:50:49','0'),(18,'删除权限','权限管理-删除权限',15,'权限管理',NULL,1,'2017-05-31 11:51:45','0'),(19,'排班管理','排班管理',1,NULL,NULL,1,'2017-05-31 10:40:59','0'),(20,'排班表格','排班管理-排班表格',19,'排班管理',NULL,1,'2017-05-31 10:40:59','0'),(21,'新增排班','排班管理-新增排班',19,'排班管理',NULL,1,'2017-05-31 10:40:59','0'),(22,'排班设置','排班设置',1,NULL,NULL,1,'2017-05-31 10:40:59','0'),(23,'班次设置','排班设置-班次设置',22,'排班设置',NULL,1,'2017-05-31 10:40:59','0'),(24,'新增班制','班次设置-新增班制',23,'班次设置',NULL,1,'2017-05-31 11:50:19','0'),(25,'修改班制','班次设置-修改班制',23,'班次设置',NULL,1,'2017-05-31 11:50:49','0'),(26,'删除班制','班次设置-删除班制',23,'班次设置',NULL,1,'2017-05-31 11:51:45','0'),(27,'岗位设置','排班设置-岗位设置',22,'排班设置',NULL,1,'2017-05-31 10:40:59','0'),(28,'新增岗位','岗位设置-新增岗位',27,'岗位设置',NULL,1,'2017-05-31 11:50:19','0'),(29,'修改岗位','岗位设置-修改岗位',27,'岗位设置',NULL,1,'2017-05-31 11:50:49','0'),(30,'删除岗位','岗位设置-删除岗位',27,'岗位设置',NULL,1,'2017-05-31 11:51:45','0'),(31,'统计报表','统计报表',1,NULL,NULL,1,'2017-05-31 10:40:59','0'),(32,'工时报表','统计报表-工时报表',31,'统计报表',NULL,1,'2017-05-31 10:40:59','0'),(33,'临时报表','统计报表-临时报表',31,'统计报表',NULL,1,'2017-05-31 10:40:59','0');
/*!40000 ALTER TABLE `permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `post_setting`
--

DROP TABLE IF EXISTS `post_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `post_setting` (
  `post_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_name` varchar(50) DEFAULT NULL,
  `post_code` varchar(50) DEFAULT NULL,
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
  `is_backup_position` varchar(5) DEFAULT NULL,
  PRIMARY KEY (`post_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `post_setting`
--

LOCK TABLES `post_setting` WRITE;
/*!40000 ALTER TABLE `post_setting` DISABLE KEYS */;
INSERT INTO `post_setting` (`post_id`, `post_name`, `post_code`, `max_weekly_reason`, `min_weekly_reason`, `post_month`, `post_year`, `max_weekly_rest`, `min_weekly_rest`, `creator_id`, `updator_id`, `if_use`, `created_at`, `updated_at`, `is_backup_position`) VALUES (1,'站务员','000001',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-05-18 15:03:29','2017-06-07 17:30:38',NULL),(2,'值班站长','000002',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-05-18 15:13:15','2017-05-25 20:54:07',NULL),(3,'综控员','000003',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-05-18 15:13:59','2017-05-25 20:54:12',NULL),(4,'站区助理','000004',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-05-18 15:13:59','2017-05-25 20:54:12',NULL),(5,'副站长','000005',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-05-18 15:13:59','2017-05-25 20:54:12',NULL),(6,'站区书记','000006',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-05-18 15:13:59','2017-05-25 20:54:12',NULL),(7,'站区长','000007',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-05-18 15:13:59','2017-05-25 20:54:12',NULL),(8,'替班员','000100',NULL,NULL,NULL,NULL,NULL,NULL,999,999,0,'2017-08-23 10:58:21','2017-09-01 14:48:45','on'),(9,'检查','000101',NULL,NULL,NULL,NULL,NULL,NULL,999,0,1,'2017-08-25 15:01:18',NULL,NULL);
/*!40000 ALTER TABLE `post_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(30) DEFAULT NULL,
  `comment` varchar(255) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `created_at` datetime DEFAULT NULL,
  `is_deleted` varchar(10) DEFAULT '0',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` (`role_id`, `role_name`, `comment`, `creator_id`, `created_at`, `is_deleted`) VALUES (1,'超级管理员','最高权限',1,'2016-09-07 10:09:07','0');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role_permission`
--

DROP TABLE IF EXISTS `role_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role_permission` (
  `role_id` int(11) NOT NULL,
  `permission_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role_permission`
--

LOCK TABLES `role_permission` WRITE;
/*!40000 ALTER TABLE `role_permission` DISABLE KEYS */;
INSERT INTO `role_permission` (`role_id`, `permission_id`) VALUES (1,1),(1,2),(1,3),(1,4),(1,5),(1,6),(1,7),(1,8),(1,9),(1,10),(1,11),(1,12),(1,13),(1,14),(1,15),(1,16),(1,17),(1,18),(1,19),(1,20),(1,21),(1,22),(1,23),(1,24),(1,25),(1,26),(1,27),(1,28),(1,29),(1,30),(1,31),(1,32),(1,33);
/*!40000 ALTER TABLE `role_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_info`
--

DROP TABLE IF EXISTS `schedule_info`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `serial_number` varchar(10) DEFAULT NULL,
  `shift_code` varchar(5) DEFAULT NULL,
  `shift_color` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`schedule_info_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_info`
--

LOCK TABLES `schedule_info` WRITE;
/*!40000 ALTER TABLE `schedule_info` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_info` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_info_template`
--

DROP TABLE IF EXISTS `schedule_info_template`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `user_id` varchar(20) DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_info_template`
--

LOCK TABLES `schedule_info_template` WRITE;
/*!40000 ALTER TABLE `schedule_info_template` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_info_template` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_info_user`
--

DROP TABLE IF EXISTS `schedule_info_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_info_user` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `model_id` varchar(20) DEFAULT NULL,
  `user_id` varchar(20) DEFAULT NULL,
  `user_name` varchar(100) DEFAULT NULL,
  `week_number` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_info_user`
--

LOCK TABLES `schedule_info_user` WRITE;
/*!40000 ALTER TABLE `schedule_info_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_info_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_log`
--

DROP TABLE IF EXISTS `schedule_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_log`
--

LOCK TABLES `schedule_log` WRITE;
/*!40000 ALTER TABLE `schedule_log` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_log` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `schedule_user`
--

DROP TABLE IF EXISTS `schedule_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `schedule_user` (
  `C1` int(11) DEFAULT NULL,
  `C2` text,
  `C3` text,
  `C4` text,
  `C5` text,
  `C6` text,
  `C7` text,
  `C8` text,
  `C9` int(11) DEFAULT NULL,
  `C10` text,
  `C11` text,
  `C12` int(11) DEFAULT NULL,
  `C13` int(11) DEFAULT NULL,
  `C14` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `schedule_user`
--

LOCK TABLES `schedule_user` WRITE;
/*!40000 ALTER TABLE `schedule_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `schedule_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift_model`
--

DROP TABLE IF EXISTS `shift_model`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `station` varchar(20) DEFAULT NULL,
  `station_area` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`model_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift_model`
--

LOCK TABLES `shift_model` WRITE;
/*!40000 ALTER TABLE `shift_model` DISABLE KEYS */;
/*!40000 ALTER TABLE `shift_model` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift_population`
--

DROP TABLE IF EXISTS `shift_population`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shift_population` (
  `population_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_at` int(11) DEFAULT NULL,
  `end_at` int(11) DEFAULT NULL,
  `population_count` int(11) DEFAULT NULL,
  `model_id` varchar(11) DEFAULT NULL,
  `post_id` varchar(11) DEFAULT NULL,
  PRIMARY KEY (`population_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift_population`
--

LOCK TABLES `shift_population` WRITE;
/*!40000 ALTER TABLE `shift_population` DISABLE KEYS */;
INSERT INTO `shift_population` (`population_id`, `start_at`, `end_at`, `population_count`, `model_id`, `post_id`) VALUES (1,0,1440,0,'1','1');
/*!40000 ALTER TABLE `shift_population` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `shift_setting`
--

DROP TABLE IF EXISTS `shift_setting`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `shift_setting` (
  `shift_id` int(11) NOT NULL AUTO_INCREMENT,
  `shift_name` varchar(50) DEFAULT NULL,
  `shift_num` varchar(50) DEFAULT NULL,
  `shift_color` varchar(20) DEFAULT NULL,
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
  `shift_code` varchar(20) DEFAULT NULL,
  `shift_explain` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shift_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `shift_setting`
--

LOCK TABLES `shift_setting` WRITE;
/*!40000 ALTER TABLE `shift_setting` DISABLE KEYS */;
/*!40000 ALTER TABLE `shift_setting` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
  `birth_day` varchar(20) DEFAULT NULL,
  `id_code` varchar(20) DEFAULT NULL,
  `on_board_date` varchar(20) DEFAULT NULL,
  `is_married` varchar(10) DEFAULT NULL,
  `has_child` varchar(10) DEFAULT NULL,
  `edu_back_ground` varchar(20) DEFAULT NULL,
  `is_party_member` varchar(10) DEFAULT NULL,
  `join_date` varchar(20) DEFAULT NULL,
  `phone_number` varchar(20) DEFAULT NULL,
  `gender` varchar(10) DEFAULT NULL,
  `home_address` varchar(255) DEFAULT NULL,
  `cert_no` varchar(50) DEFAULT NULL,
  `cert_level` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=1243 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `password`, `user_pass`, `secret_key`, `user_code`, `user_job`, `user_name`, `group_name`, `station_area`, `station`, `created_at`, `creator_id`, `is_deleted`, `is_admin`, `birth_day`, `id_code`, `on_board_date`, `is_married`, `has_child`, `edu_back_ground`, `is_party_member`, `join_date`, `phone_number`, `gender`, `home_address`, `cert_no`, `cert_level`) VALUES (1231,'95829e0d614ffedd4144def9d8c28a09','ducetech!@#','cb7e52304f0d11e6965c00ff2c2e2b3f','admin','000004','admin','000001','000003','',NULL,NULL,0,1,'1990-12-12','110108198212142712','1990-12-12','已婚','未育','高中以下','群众',NULL,'18888888888','男','beijing',NULL,NULL),(1232,'328daaa9f672777ebbfc3fda0a6dbf93','111111','a656358ada5046b0afa053ec7f343fda','10101','000001','要你命三千',NULL,'000001','000001001','2017-12-06 19:17:52','1231',0,1,'1982-12-14','110108198212142712','1982-12-12','已婚','已育','高中以下','群众','','1212121212121','男','北京市海淀区','',''),(1242,'1f0d7a3091b840ea332bfcf0bbf12370','123456','9e790a4bfdd2475e9324b34bf293a97f','10101','000001','狗蛋',NULL,'000001','000003001','2017-12-07 00:03:19','1231',0,0,'1989-12-12','110108198912121212','1998-12-12','未婚','有','本科','团员','','12911111111','男','北京市','','');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_role`
--

DROP TABLE IF EXISTS `user_role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_role` (
  `user_id` int(11) NOT NULL,
  `role_id` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_role`
--

LOCK TABLES `user_role` WRITE;
/*!40000 ALTER TABLE `user_role` DISABLE KEYS */;
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES (999,1),(1229,1),(1232,1),(1231,1);
/*!40000 ALTER TABLE `user_role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflow`
--

DROP TABLE IF EXISTS `workflow`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflow` (
  `workflow_id` int(11) NOT NULL AUTO_INCREMENT,
  `post_id` int(11) DEFAULT NULL,
  `model_id` int(11) DEFAULT NULL,
  `shift_id` int(11) DEFAULT NULL,
  `serial_number` varchar(20) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `updator_id` int(11) DEFAULT NULL,
  `is_deleted` int(1) NOT NULL DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  PRIMARY KEY (`workflow_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflow`
--

LOCK TABLES `workflow` WRITE;
/*!40000 ALTER TABLE `workflow` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflow` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `workflow_content`
--

DROP TABLE IF EXISTS `workflow_content`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `workflow_content` (
  `content_id` int(11) NOT NULL AUTO_INCREMENT,
  `start_time` int(11) DEFAULT NULL,
  `end_time` int(11) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `creator_id` int(11) DEFAULT NULL,
  `updator_id` int(11) DEFAULT NULL,
  `is_deleted` int(1) DEFAULT '0',
  `created_at` datetime DEFAULT NULL,
  `updated_at` datetime DEFAULT NULL,
  `workflow_id` int(11) DEFAULT NULL,
  `start_location` int(11) DEFAULT NULL,
  `end_location` int(11) DEFAULT NULL,
  `rows_num` int(11) DEFAULT NULL,
  `content_color` varchar(6) DEFAULT NULL,
  PRIMARY KEY (`content_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `workflow_content`
--

LOCK TABLES `workflow_content` WRITE;
/*!40000 ALTER TABLE `workflow_content` DISABLE KEYS */;
/*!40000 ALTER TABLE `workflow_content` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-12-07 11:16:14
