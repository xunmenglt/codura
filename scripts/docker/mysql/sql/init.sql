/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.28 : Database - codura
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`codura` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_bin */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `codura`;

/*Table structure for table `ai_use_log` */

DROP TABLE IF EXISTS `ai_use_log`;

CREATE TABLE `ai_use_log` (
  `id` varchar(300) COLLATE utf8mb4_bin NOT NULL COMMENT '日志唯一ID',
  `type` varchar(50) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '日志类型',
  `timestamp` bigint DEFAULT NULL COMMENT '时间戳',
  `user_id` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '用户ID',
  `ip_address` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IP地址',
  `mac_address` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'mac地址',
  `ide_version` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT 'IDE版本号',
  `value` text COLLATE utf8mb4_bin COMMENT '日志通用记录字段',
  `event_type` varchar(100) COLLATE utf8mb4_bin DEFAULT NULL COMMENT '插件使用类型',
  `input_content` text COLLATE utf8mb4_bin COMMENT '用户输入',
  `output_content` text COLLATE utf8mb4_bin COMMENT '插件输出',
  `input_len` bigint DEFAULT '0' COMMENT '输入长度',
  `output_len` bigint DEFAULT '0' COMMENT '输出长度',
  `input_tokens` bigint DEFAULT '0' COMMENT '输入token长度',
  `output_tokens` bigint DEFAULT '0' COMMENT '输出token长度',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*Table structure for table `sys_menu` */

DROP TABLE IF EXISTS `sys_menu`;

CREATE TABLE `sys_menu` (
  `menu_id` int NOT NULL AUTO_INCREMENT COMMENT '菜单ID',
  `menu_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '菜单名称',
  `parent_id` int DEFAULT '0' COMMENT '父菜单ID',
  `order_num` int DEFAULT '0' COMMENT '显示顺序',
  `path` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '路由地址',
  `component` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '组件路径',
  `is_frame` int DEFAULT '0' COMMENT '是否为外链（1是 0否）',
  `is_cache` int DEFAULT '1' COMMENT '是否缓存（1缓存 0不缓存）',
  `menu_type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '菜单类型（M目录 C菜单 F按钮）',
  `visible` int DEFAULT '1' COMMENT '显示状态（1显示 0隐藏）',
  `status` int DEFAULT '1' COMMENT '菜单状态（1正常 0停用）',
  `perms` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '权限标识',
  `icon` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '#' COMMENT '菜单图标',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`menu_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=71 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_menu` */

insert  into `sys_menu`(`menu_id`,`menu_name`,`parent_id`,`order_num`,`path`,`component`,`is_frame`,`is_cache`,`menu_type`,`visible`,`status`,`perms`,`icon`,`remark`,`create_time`,`update_time`) values 
(1,'系统管理',0,1,'system',NULL,0,1,'M',1,1,NULL,'system','系统管理目录','2024-04-06 16:11:09','2024-04-06 16:11:09'),
(2,'系统监控',0,2,'monitor',NULL,0,1,'M',1,1,NULL,'monitor','系统监控目录','2024-04-06 16:11:15','2024-04-06 16:11:15'),
(5,'用户管理',1,1,'user','system/user/index',0,1,'C',1,1,'system:user:list','user','用户管理菜单','2024-04-06 16:12:32','2024-04-06 16:12:32'),
(6,'角色管理',1,2,'role','system/role/index',0,1,'C',1,1,'system:role:list','peoples','角色管理菜单','2024-04-06 16:12:37','2024-04-06 16:12:37'),
(7,'菜单管理',1,3,'menu','system/menu/index',0,1,'C',1,1,'system:menu:list','tree-table','菜单管理菜单','2024-04-06 16:12:46','2024-04-06 16:12:46'),
(8,'通知公告',1,4,'notice','system/notice/index',0,1,'C',1,1,'system:notice:list','message','通知公告管理菜单','2024-04-06 16:12:51','2024-04-06 16:12:51'),
(10,'操作日志',2,4,'operlog','monitor/operlog/index',0,1,'C',1,1,'system:operlog:list','log','操作日志管理菜单','2024-04-06 16:13:19','2024-04-06 16:13:19'),
(13,'数据监控',2,1,'druid','monitor/druid/index',0,1,'C',1,1,'monitor:druid:list','druid','数据监控菜单','2024-04-06 16:17:31','2024-04-06 16:17:31'),
(14,'在线用户',2,2,'online','monitor/online/index',0,1,'C',1,1,'monitor:online:list','online','在线用户菜单','2024-04-06 16:17:37','2024-04-06 16:17:37'),
(17,'系统工具',0,4,'tool',NULL,0,1,'M',1,1,NULL,'tool','系统工具目录','2024-04-06 16:18:13','2024-04-06 16:18:13'),
(18,'前台接口',17,1,'beforeswagger','tool/beforeswagger/index',0,1,'C',1,1,'tool:beforeswagger:list','swagger','前台接口地址','2024-04-06 16:18:14','2024-04-06 16:18:14'),
(19,'后台接口',17,2,'afterswagger','tool/afterswagger/index',0,1,'C',1,1,'tool:afterswagger:list','swagger','后台接口地址','2024-04-06 16:18:31','2024-04-06 16:18:31'),
(29,'用户修改',5,1,NULL,NULL,1,0,'F',0,1,'system:user:edit','#','','2024-05-05 22:46:09','2024-05-05 22:46:09'),
(30,'用户新增',5,2,NULL,NULL,1,0,'F',0,1,'system:user:add','#','','2024-05-05 22:47:16','2024-05-05 22:47:16'),
(31,'用户删除',5,3,NULL,NULL,1,0,'F',0,1,'system:user:delete','#','','2024-05-05 22:48:03','2024-05-05 22:48:03'),
(32,'重置密码',5,4,NULL,NULL,1,0,'F',0,1,'system:user:resetPwd','#','','2024-05-05 22:48:31','2024-05-05 22:48:31'),
(33,'角色查询',6,1,NULL,NULL,1,0,'F',0,1,'system:role:query','#','','2024-05-05 22:49:50','2024-05-05 22:49:50'),
(34,'角色新增',6,2,NULL,NULL,1,0,'F',0,1,'system:role:add','#','','2024-05-05 22:50:26','2024-05-05 22:50:26'),
(35,'角色修改',6,3,NULL,NULL,1,0,'F',0,1,'system:role:edit','#','','2024-05-05 22:50:44','2024-05-05 22:50:44'),
(36,'角色删除',6,5,NULL,NULL,1,0,'F',0,1,'system:role:delete','#','','2024-05-05 22:51:20','2024-05-05 22:51:20'),
(37,'菜单查询',7,1,NULL,NULL,1,0,'F',0,1,'system:menu:query','#','','2024-05-05 22:53:47','2024-05-05 22:53:47'),
(38,'菜单新增',7,2,NULL,NULL,1,0,'F',0,1,'system:menu:add','#','','2024-05-05 22:55:36','2024-05-05 22:55:36'),
(39,'菜单修改',7,3,NULL,NULL,1,0,'F',0,1,'system:menu:edit','#','','2024-05-05 22:56:04','2024-05-05 22:56:04'),
(40,'菜单删除',7,4,NULL,NULL,1,0,'F',0,1,'system:menu:delete','#','','2024-05-05 22:56:26','2024-05-05 22:56:26'),
(41,'通知新增',8,2,NULL,NULL,1,0,'F',0,1,'system:notice:add','#','','2024-05-05 22:59:03','2024-05-05 22:59:03'),
(42,'通知查询',8,1,NULL,NULL,1,0,'F',0,1,'system:notice:query','#','','2024-05-05 22:59:13','2024-05-05 22:59:13'),
(43,'通知修改',8,3,NULL,NULL,1,0,'F',0,1,'system:notice:edit','#','','2024-05-05 22:59:35','2024-05-05 22:59:35'),
(44,'通知删除',8,4,NULL,NULL,1,0,'F',0,1,'system:notice:delete','#','','2024-05-05 22:59:59','2024-05-05 22:59:59'),
(45,'用户强退',14,1,NULL,NULL,1,0,'F',0,1,'monitor:online:forceLogout','#','','2024-05-05 23:04:32','2024-05-05 23:04:32'),
(46,'日志删除',10,3,NULL,NULL,1,0,'F',0,1,'monitor:operlog:delete','#','','2024-05-05 23:06:25','2024-05-05 23:06:25'),
(47,'日志导出',10,1,NULL,NULL,1,0,'F',0,1,'monitor:operlog:export','#','','2024-05-05 23:07:01','2024-05-05 23:07:01');

/*Table structure for table `sys_notice` */

DROP TABLE IF EXISTS `sys_notice`;

CREATE TABLE `sys_notice` (
  `notice_id` bigint NOT NULL AUTO_INCREMENT COMMENT '公告ID',
  `notice_title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '公告标题',
  `notice_type` int DEFAULT NULL COMMENT '公告类型（1通知 2公告）',
  `notice_content` longblob COMMENT '公告内容',
  `status` int DEFAULT NULL COMMENT '公告状态（1正常 0关闭）',
  `create_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '创建者',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_by` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '更新者',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`notice_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1717045412886 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_notice` */

/*Table structure for table `sys_oper_log` */

DROP TABLE IF EXISTS `sys_oper_log`;

CREATE TABLE `sys_oper_log` (
  `oper_id` bigint NOT NULL AUTO_INCREMENT COMMENT '日志主键',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '模块标题',
  `business_type` int DEFAULT NULL COMMENT '业务类型（0其它 1新增 2修改 3删除）',
  `method` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '方法名称',
  `request_method` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求方式',
  `operator_type` int NOT NULL COMMENT '操作类别（0其它 1后台用户 2手机端用户）',
  `oper_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作人员',
  `oper_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求URL',
  `oper_ip` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '主机地址',
  `oper_location` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '操作地点',
  `oper_param` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '请求参数',
  `json_result` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '返回参数',
  `status` int DEFAULT NULL COMMENT '操作状态（1正常 0异常）',
  `error_msg` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '错误消息',
  `cost_time` bigint DEFAULT NULL COMMENT '耗时（毫秒）',
  `oper_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  PRIMARY KEY (`oper_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=90 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Table structure for table `sys_role` */

DROP TABLE IF EXISTS `sys_role`;

CREATE TABLE `sys_role` (
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '权限字符',
  `role_zh_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色中文名称',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`role_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_role` */

insert  into `sys_role`(`role_name`,`role_zh_name`,`remark`,`create_time`,`update_time`) values 
('admin','超级管理员','超级管理员','2024-04-06 16:55:07','2024-04-06 16:55:07'),
('programmer','程序员','代码助手使用用户角色','2024-11-01 14:46:38','2024-11-01 14:46:38');

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `menu_id` int NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_role_menu` */

/*Table structure for table `sys_user` */

DROP TABLE IF EXISTS `sys_user`;

CREATE TABLE `sys_user` (
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `nick_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户昵称',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '密码',
  `avatar` varchar(250) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '头像url',
  `email` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '邮箱',
  `enabled` int DEFAULT '0' COMMENT '是否启用（ 0禁止 1启用）',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT '' COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_user` */

insert  into `sys_user`(`user_name`,`nick_name`,`password`,`avatar`,`email`,`enabled`,`remark`,`create_time`,`update_time`) values 
('admin','主管理员','$2a$10$jhoSeYE9N73dOQcNmTwpxO.G6FtJZOUwuNdpjYeQp22hJRB1ID/Qy','/profile/avatar/2024/10/15/1728992736481_20241015194536A001.png','3339372755@qq.com',1,'主管理员','2024-04-04 20:17:27','2024-04-04 20:17:27'),
('codura','codura','$2a$10$Ct5u4CyIZuzG5.Xa8VIqbuZpX09PiKdEebvVhQ4Zua4kuU7.S3zC.',NULL,'xmliuteng@163.com',1,'用于编程使用','2024-11-07 09:52:44','2024-11-07 09:52:44');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=50 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_name`,`role_name`) values 
(1,'admin','admin'),
(2,'codura','programmer');

/*Table structure for table `sys_variable` */

DROP TABLE IF EXISTS `sys_variable`;

CREATE TABLE `sys_variable` (
  `variable_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '变量key',
  `variable_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '变量key',
  `variable_type` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '变量类型(int,str,boolen)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL COMMENT '备注',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`variable_key`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC COMMENT='系统变量表';

/*Data for the table `sys_variable` */

/*Table structure for table `user_use_info` */

DROP TABLE IF EXISTS `user_use_info`;

CREATE TABLE `user_use_info` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一ID',
  `user_id` varchar(100) COLLATE utf8mb4_bin NOT NULL COMMENT '用户ID',
  `code_completion_chars` bigint DEFAULT '0' COMMENT '代码补全字符数',
  `code_completion_tokens` bigint DEFAULT '0' COMMENT '代码补全token数',
  `code_completion_qa_times` bigint DEFAULT '0' COMMENT '代码问答qa次数',
  `code_completion_qa_chars` bigint DEFAULT '0' COMMENT '代码问答qa字符数',
  `code_completion_qa_tokens` bigint DEFAULT '0' COMMENT '代码问答qa token数',
  `test_case_writing_chars` bigint DEFAULT '0' COMMENT '测试用例编写次数',
  `test_case_writing_tokens` bigint DEFAULT '0' COMMENT '测试用例编写token数',
  `variable_type_declaration_chars` bigint DEFAULT '0' COMMENT '变量类型声明字符数',
  `variable_type_declaration_tokens` bigint DEFAULT '0' COMMENT '变量类型声明token数',
  `code_explanation_chars` bigint DEFAULT '0' COMMENT '代码解释字符数',
  `code_explanation_tokens` bigint DEFAULT '0' COMMENT '代码解释token数',
  `dcoumention_writing_chars` bigint DEFAULT '0' COMMENT '文档编写字符数',
  `dcoumention_writing_tokens` bigint DEFAULT '0' COMMENT '文档编写token数',
  `code_refactoring_chars` bigint DEFAULT '0' COMMENT '代码重构字符数',
  `code_refactoring_tokens` bigint DEFAULT '0' COMMENT '代码重构token数',
  `qucik_code_insertion_chars` bigint DEFAULT '0' COMMENT '代码插入字符数',
  `qucik_code_insertion_tokens` bigint DEFAULT '0' COMMENT '代码插入token数',
  `editor_usage_time` bigint DEFAULT '0' COMMENT '编辑器使用时间',
  `editor_active_time` bigint DEFAULT '0' COMMENT '编辑器活跃时间',
  `added_code_lines` bigint DEFAULT '0' COMMENT '添加的代码行数',
  `deleted_code_lines` bigint DEFAULT '0' COMMENT '删除的代码行数',
  `total_keyboard_inputs` bigint DEFAULT '0' COMMENT '总键盘输入次数',
  `ctrl_c_count` bigint DEFAULT '0' COMMENT 'CTRL+C次数',
  `ctrl_v_count` bigint DEFAULT '0' COMMENT 'CTRL+V次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=26609 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;

/*Data for the table `user_use_info` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
