/*
SQLyog Community v13.1.6 (64 bit)
MySQL - 8.0.27 : Database - amies
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`amies` /*!40100 DEFAULT CHARACTER SET utf8 */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE devassist;

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
(47,'日志导出',10,1,NULL,NULL,1,0,'F',0,1,'monitor:operlog:export','#','','2024-05-05 23:07:01','2024-05-05 23:07:01'),
(58,'数据管理',0,5,'source',NULL,0,1,'M',1,1,NULL,'datamanagement','数据管理','2024-10-15 20:05:03','2024-10-15 20:05:03'),
(59,'模型管理',0,6,'model',NULL,0,1,'M',1,1,NULL,'modelmanagement','模型管理','2024-10-15 20:09:08','2024-10-15 20:09:08'),
(60,'推理中心',0,7,'inference',NULL,0,1,'M',1,1,NULL,'inference','推理中心','2024-10-15 20:09:44','2024-10-15 20:09:44'),
(61,'模型微调',0,8,'fintrain',NULL,0,1,'M',1,1,NULL,'finetrain','模型微调','2024-10-15 20:09:49','2024-10-15 20:09:49'),
(62,'模型应用',0,9,'application',NULL,0,1,'M',1,1,NULL,'application','模型应用','2024-10-15 20:09:51','2024-10-15 20:09:51'),
(63,'文件管理',58,0,'file','source/file/index',0,1,'C',1,1,'source:file:list','data','文件管理','2024-10-15 20:19:06','2024-10-15 20:19:06'),
(64,'数据集',58,0,'data','source/data/index',0,1,'C',1,1,'source:data:list','dataset','数据集','2024-10-15 20:19:12','2024-10-15 20:19:12'),
(65,'数据标注',58,0,'labeling','source/labeling/index',0,1,'C',1,1,'source:labeling:list','datalabeling','数据标注','2024-10-15 20:19:18','2024-10-15 20:19:18'),
(67,'在线推理',60,0,'onlineinference','inference/onlineinference/index',0,1,'C',1,1,'inference:onlineinference:list','onlineinference','在线推理','2024-10-15 20:26:25','2024-10-15 20:26:25'),
(68,'模型仓库',59,0,'warehouse','model/warehouse/index',0,1,'C',1,1,'model:warehouse:list','modelwarehouse','模型仓库','2024-10-15 20:30:51','2024-10-15 20:30:51'),
(69,'大模型微调',61,0,'llmft','fintrain/llmft/index',0,1,'C',1,1,'fintrain:llmft:list','llmfinetrain','大模型微调','2024-10-15 20:30:56','2024-10-15 20:30:56'),
(70,'文档抽取',62,0,'extraction','application/extraction/index',0,1,'C',1,1,'application:extraction:list','extraction','文档抽取','2024-10-15 20:32:43','2024-10-15 20:32:43');

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

insert  into `sys_notice`(`notice_id`,`notice_title`,`notice_type`,`notice_content`,`status`,`create_by`,`create_time`,`update_by`,`update_time`,`remark`) values 
(1715505949319,'重要通知 | 《胸痛中心质控报告(2023)》正式发布',1,'::: hljs-center\n\n<img src=\"http://localhost:8080/profile/upload/notice/2024/05/12/(159)2000x2000_20240512172904A002.png\" width=150>\n\n:::\n\n# 前言\n为稳步推动全国胸痛中心常态化质控工作进程，持续提升胸痛中心建设水平，国家放射与治疗临床医学研究中心胸痛中心专家委员会、苏州工业园区东方华夏心血管健康研究院组织撰写了《胸痛中心质控报告（2023）》（全国版）及《胸痛中心质控报告（2023）》（省级版），现正式发布。\n报告中的数据来源于苏州工业园区东方华夏心血管健康研究院、胸痛中心办公室搭建的胸痛中心认证管理平台及胸痛中心数据填报平台（https：//data.chinacpc.org/）。\n# 一、全国胸痛中心建设现状\n《胸痛中心质控报告（2023）》分析了胸痛中心认证/建设情况、各省二级以上医院胸痛中心建设比例、各省胸痛中心注册数量情况、各省每百万常住人口胸痛中心数量及各省胸痛中心认证数量情况，全面展示了全国胸痛中心建设现状。\n\n\n截至2023年12月注册单位5425家，通过认证2660家（标准版1273家、基层版1387家），全国330个地市（州）至少有一家胸痛中心，全国实现县域96%覆盖。\n\n# 二、胸痛中心建设成效及现存问题\n1、**显著提高了STEMI患者的再灌注治疗比例**：标准版胸痛中心及基层版胸痛中心12小时内达到医院的STEMI患者接受再灌注治疗的比例为为88.9%和87.2%，呈现上升趋势。\n\n\n\n2、**保证短时间内对心肌梗死患者进行救治**：接受PPCI治疗的STEMI患者平均入门到导丝通过（D2W）时间近年来整体呈下降趋势，2012年为115分钟，2023年，标准版胸痛中心单位D2W时间为68.7分钟，基层版胸痛中心单位D2W时间为73.8分钟。基层版胸痛中心单位行溶栓治疗的STEMI患者入门至开始溶栓（D2N）时间呈下降趋势且达标率持续提升，接受溶栓的STEMI患者中73%能够在30分钟内开始溶栓。\n\n\n\n3、**缩短了住院时间、节省了住院费用**：标准版胸痛中心单位STEMI患者平均住院天数由2019年8.5天下降至7.8天，平均住院费用由2019年4.21万下降至2.74万。基层版胸痛中心单位STEMI患者平均住院天数由2019年7.4天下降至6.6天，平均住院费用从2019年2.76万降低至1.76万。\n\n\n\n4、**降低了死亡率：胸痛中心建设之前的急性心肌梗死患者的平均死亡率10%以上**，2023年标准版和基层版胸痛中心单位STEMI患者院内死亡率分别为3.27%和3.69%，通过胸痛中心建设，显著优化救治流程，改善患者预后。\n![001800x600.png](http://localhost:8080/profile/upload/notice/2024/05/12/(001)800x600_20240512172803A001.png)',1,'admin','2024-05-12 17:25:48','admin','2024-05-12 17:38:32',NULL),
(1715506340804,'中心费用报销管理制度',1,'## 中心费用报销管理制度\n\n南京临床核医学中心财务室\n\n\n根据本院费用管理办法的核心精神，结合中心具体的实际情况，中心费用报销管理制度如下，请中心各位全体员工遵守执行。\n\n第一．报销流程方面，请注意费用报销支出凭证形式要件四要素\n\n写明时间，地点，事项事由，粘贴会议事项票据，至少双人签字，既经办人和分管领导合署联签。在右侧发票粘贴区按照要求进行粘贴，大张的横版发票可直接在发票上写明报销事由及双人签字。财务审核报销票据的金额，票据有效性需与具体费用事项及行程明细说明相一致，不一致的票据，财务有权退回给报销人。\n\n费用报销、个人借款，相关凭证必须经中心领导签字批准，且载明经办人员签字和部门负责人审核并注明用途，支出凭证经财务审核后经中心领导签字后方能生效。\n\n第二：报销费用项目要求：日常费用报销请按照费用项目进行分类并粘贴相应的发票，发票内容与事由要一致，支出凭证要件齐全，粘贴规范。\n\n第三：差旅费报销项目细则：,\n\n差旅费的财务核定事项主要如下：\n\n1.行方面：公关交通工具原则上应采用最经济的座位，即火车硬座，高铁动车二等座，轮船三等舱，飞机经济舱，其他市内交通费如出租车凭据报销，特别说明飞机只有在路途较远或出差任务紧急情况下，经领导批准方可乘坐飞机，未按等级乘坐交通工具的，超支部分由个人自付。\n\n2. 餐饮发票，个人伙食按照相应的标准乘上天数实报实销，如需宴请必要的交流合作单位及个人，须开具餐饮发票并写明宴请理由；\n\n3. 住宿发票应写明房间单价，住宿天数；会务费发票需写明具体会议名称，持续天数并据实报销。其他发票比方说礼品发票应在发票上写明赠送的对象及事由。\n\n4.采用借条形式进行差旅会议事项的，除需履行先借后结的常规借款手续外，差旅费用性质及内容的审核参照本制度核定。\n\n5.互联网技术条件情况下，鼓励采用网络订票的方式出行，例如住宿和车票机票，在行程预知较早的情况下，充分利用网络预订来降低费用，\n\n总而言之，上述制度为的是快好省地规范我们的差旅行程，有效的降低差旅费用，提高工作效率。\n\n本制度自公布日起执行，请全员遵守。',1,'admin','2024-05-12 17:32:20','admin','2024-05-12 17:32:24',NULL),
(1715506401785,'关于更改敏乙型肝炎病毒核酸定量、超敏丙型肝炎病毒核酸定量及丙型肝炎病毒分型检测报告时间的通知',1,'**南京临床核医学中心开展的超敏乙型肝炎病毒核酸定量检测（HS-HBV DNA），超敏丙型肝炎病毒核酸定量检测（HS-HCV RNA）及丙型肝炎病毒分型检测（HCV-G）现根据临床需求，将于2017年8月14日起更改检测与报告时间如下**： \n\n项目\n\n原检测、报告时间\n\n现检测、报告时间\n\n超敏乙型肝炎病毒核酸定量\n\n（HS-HBV DNA）\n\n检测时间：周一 9:15止\n\n报告时间：当日下午 16:00 后 \n\n检测时间：周一至周五 9:15止\n\n报告时间：当日下午 16:00 后\n\n超敏丙型肝炎病毒核酸定量\n\n（HS-HCV RNA）\n\n检测时间：周五 9:15止\n\n报告时间：当日下午 16:00 后\n\n检测时间：周一、周四 9:15止\n\n报告时间：当日下午 16:00 后\n\n丙型肝炎病毒分型\n\n（HCV-G）\n\n检测时间：周二 9:15止\n\n报告时间：当日下午 16:00 后\n\n检测时间：周四 9:15止\n\n报告时间：当日下午 16:00 后\n\n    特此通知。\n\n                                            南京临床核医学中心\n\n                                                2017年8月7日\n\n \n',1,'admin','2024-05-12 17:33:21','admin','2024-05-12 17:33:41',NULL),
(1715506475598,'新项目:超敏HCV RNA定量检测通知',1,'**为了满足临床对慢性乙型肝炎及丙型肝炎治疗反应监测的需求，南京市第一医院南京临床核医学中心现开展超敏HCV RNA定量检测。**\n\n超敏HCV RNA定量检测可以检测1-6任何基因型的HCV-RNA，检测下限为50 IU/ml，检测线性范围为30至 >3×106 IU/ml。\n\n ![012500x500.png](http://localhost:8080/profile/upload/notice/2024/05/12/(012)500x500_20240512173443A003.png)\n\n关于超敏HCV RNA定量检测项目的情况如下：\n\n病区医嘱名称：放免超敏HCV定量检测\n\n门诊名称：超敏HCV定量检测\n\n  项目名称：丙型肝炎病毒核糖核酸扩增定量检测\n\n江苏省收费编码：250403013-a  \n\n收费：465元\n\n \n\n标本类型和要求：EDTA抗凝的血浆或血清，血浆或血清样本量至少1mL，采集标本后务必在6小时内分离血浆（800-1600g，20min）。\n\n \n\n检测和报告时间：每周五9:30检测，下午16:00报告。\n\n',1,'admin','2024-05-12 17:34:35','admin','2024-05-12 17:34:48',NULL);

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
) ENGINE=InnoDB AUTO_INCREMENT=85 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_oper_log` */

insert  into `sys_oper_log`(`oper_id`,`title`,`business_type`,`method`,`request_method`,`operator_type`,`oper_name`,`oper_url`,`oper_ip`,`oper_location`,`oper_param`,`json_result`,`status`,`error_msg`,`cost_time`,`oper_time`) values 
(1,'操作日志',9,'com.xunmeng.monitor.SysOperLogController.clear()','DELETE',1,'admin','/monitor/operlog/clear','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,46,'2024-04-17 21:25:44'),
(2,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/11','127.0.0.1','内网IP','{}','{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}',1,NULL,47,'2024-04-17 22:04:29'),
(3,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/11','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,15,'2024-04-17 22:06:05'),
(4,'在线用户',7,'com.xunmeng.monitor.SysUserOnlineController.forceLogout()','DELETE',1,'admin','/monitor/online/a78f4bde-da19-4daf-a5fe-0cd2cef01e7c','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,19,'2024-04-17 22:06:59'),
(5,'个人信息',2,'com.xunmeng.system.SysProfileController.updateProfile()','PUT',1,'admin','/system/user/profile','127.0.0.1','内网IP','{\"admin\":true,\"email\":\"3339372755@qq.com\",\"nickName\":\"主管理员\",\"params\":{},\"remark\":\"主管理员\",\"userName\":\"admin\"}','{\"msg\":\"success\",\"code\":200}',1,NULL,74,'2024-04-18 01:02:31'),
(6,'个人信息',2,'com.xunmeng.system.SysProfileController.updatePwd()','PUT',1,'admin','/system/user/profile/updatePwd','127.0.0.1','内网IP','{}','{\"msg\":\"修改密码失败，旧密码错误\",\"code\":500}',1,NULL,0,'2024-04-18 01:03:57'),
(7,'个人信息',2,'com.xunmeng.system.SysProfileController.updatePwd()','PUT',1,'admin','/system/user/profile/updatePwd','127.0.0.1','内网IP','{}','{\"msg\":\"修改密码失败，旧密码错误\",\"code\":500}',1,NULL,1,'2024-04-18 01:04:11'),
(8,'个人信息',2,'com.xunmeng.system.SysProfileController.updatePwd()','PUT',1,'admin','/system/user/profile/updatePwd','127.0.0.1','内网IP','{}','{\"msg\":\"修改密码失败，旧密码错误\",\"code\":500}',1,NULL,23062,'2024-04-18 01:05:25'),
(9,'个人信息',2,'com.xunmeng.system.SysProfileController.updatePwd()','PUT',1,'admin','/system/user/profile/updatePwd','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,292,'2024-04-18 01:06:42'),
(10,'个人信息',2,'com.xunmeng.system.SysProfileController.updateProfile()','PUT',1,'admin','/system/user/profile','127.0.0.1','内网IP','{\"admin\":true,\"email\":\"3339372755@qq.com\",\"nickName\":\"主管理员\",\"params\":{},\"remark\":\"主管理员-admin\",\"userName\":\"admin\"}','{\"msg\":\"success\",\"code\":200}',1,NULL,81,'2024-04-18 01:20:21'),
(11,'用户头像',2,'com.xunmeng.system.SysProfileController.avatar()','POST',1,'admin','/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"success\",\"imgUrl\":\"/profile/avatar/2024/04/18/1713375598939_20240418013959A001.png\",\"code\":200}',1,NULL,142,'2024-04-18 01:39:59'),
(12,'用户头像',2,'com.xunmeng.system.SysProfileController.avatar()','POST',1,'admin','/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"success\",\"imgUrl\":\"/profile/avatar/2024/04/18/1713375671110_20240418014111A002.png\",\"code\":200}',1,NULL,37,'2024-04-18 01:41:11'),
(13,'用户头像',2,'com.xunmeng.system.SysProfileController.avatar()','POST',1,'admin','/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"success\",\"imgUrl\":\"/profile/avatar/2024/04/18/1713376719019_20240418015839A003.png\",\"code\":200}',1,NULL,33,'2024-04-18 01:58:39'),
(14,'用户头像',2,'com.xunmeng.system.SysProfileController.avatar()','POST',1,'admin','/system/user/profile/avatar','127.0.0.1','内网IP','','{\"msg\":\"success\",\"imgUrl\":\"/profile/avatar/2024/04/23/1713887002718_20240423234322A001.png\",\"code\":200}',1,NULL,100,'2024-04-23 23:43:22'),
(15,'在线用户',7,'com.xunmeng.monitor.SysUserOnlineController.forceLogout()','DELETE',1,'admin','/monitor/online/e7d914d6-7d1c-48d4-a475-c04f767ab693','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,30,'2024-04-24 09:02:29'),
(16,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/21','127.0.0.1','内网IP','{}','{\"msg\":\"存在子菜单,不允许删除\",\"code\":601}',1,NULL,31,'2024-04-29 16:31:24'),
(17,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/22','127.0.0.1','内网IP','{}','{\"msg\":\"菜单已分配,不允许删除\",\"code\":601}',1,NULL,8,'2024-04-29 16:31:29'),
(18,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/20','127.0.0.1','内网IP','{}','{\"msg\":\"存在子菜单,不允许删除\",\"code\":601}',1,NULL,7,'2024-04-29 16:31:49'),
(19,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/22','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,14,'2024-04-29 16:31:53'),
(20,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/21','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,14,'2024-04-29 16:31:55'),
(21,'菜单管理',3,'com.xunmeng.system.SysMenuController.remove()','DELETE',1,'admin','/system/menu/20','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,13,'2024-04-29 16:31:58'),
(22,'自动评论审核',2,'com.xunmeng.article.ArticleCommentController.auto()','PUT',1,'admin','/article/comment/auto/0','127.0.0.1','内网IP','\"0\"','{\"msg\":\"success\",\"code\":200}',1,NULL,74,'2024-05-30 04:06:50'),
(23,'自动索引',2,'com.xunmeng.article.ArticleSearchController.auto()','PUT',1,'admin','/article/search/auto/0','127.0.0.1','内网IP','\"0\"','{\"msg\":\"success\",\"code\":200}',1,NULL,43,'2024-05-30 04:06:53'),
(24,'审核文章',4,'com.xunmeng.article.ArticleAuditController.doAudit()','POST',1,'admin','/article/audit/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"auditInfo\":\"内容非常精彩,继续创作优质论文\",\"auditStatus\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"auditBy\":\"admin\",\"auditInfo\":\"内容非常精彩,继续创作优质论文\",\"auditStatus\":1,\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"isSearch\":0,\"params\":{}}}',1,NULL,116,'2024-05-30 04:07:25'),
(25,'索引审核',4,'com.xunmeng.article.ArticleSearchController.doAudit()','POST',1,'admin','/article/search/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isSearch\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isSearch\":1,\"params\":{}}}',1,NULL,73,'2024-05-30 04:08:21'),
(26,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,68,'2024-05-30 04:10:37'),
(27,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,63,'2024-05-30 04:10:42'),
(28,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,81,'2024-05-30 04:12:42'),
(29,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,61013,'2024-05-30 04:15:32'),
(30,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,67,'2024-05-30 04:16:43'),
(31,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,72,'2024-05-30 04:16:48'),
(32,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,73,'2024-05-30 04:17:14'),
(33,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,17380,'2024-05-30 04:18:04'),
(34,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}',NULL,0,'该操作请勿重复执行',24,'2024-05-30 04:19:02'),
(35,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,70,'2024-05-30 04:19:10'),
(36,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,72,'2024-05-30 04:19:14'),
(37,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,68,'2024-05-30 04:19:42'),
(38,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,28568,'2024-05-30 04:21:57'),
(39,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}',NULL,0,'该操作请勿重复执行',2250,'2024-05-30 04:23:36'),
(40,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,4603,'2024-05-30 04:23:57'),
(41,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,9839,'2024-05-30 04:25:08'),
(42,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,3746,'2024-05-30 04:28:33'),
(43,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,13752,'2024-05-30 04:28:58'),
(44,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}',NULL,0,'该操作请勿重复执行',12959,'2024-05-30 04:32:22'),
(45,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}',NULL,0,'该操作请勿重复执行',7275,'2024-05-30 04:32:37'),
(46,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,117148,'2024-05-30 04:36:41'),
(47,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,33600,'2024-05-30 04:38:38'),
(48,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}',NULL,0,'该操作请勿重复执行',9557,'2024-05-30 04:50:12'),
(49,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}',NULL,0,'该操作请勿重复执行',4416,'2024-05-30 04:51:56'),
(50,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}',NULL,0,'该操作请勿重复执行',2661,'2024-05-30 04:52:16'),
(51,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,3646,'2024-05-30 04:53:04'),
(52,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,11022,'2024-05-30 04:53:25'),
(53,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,7461,'2024-05-30 04:54:15'),
(54,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"38ae3ebac83443258f751be0dd0ea11e\",\"isEmbedding\":1}',NULL,0,'文章处于待审核或审核未通过',2258,'2024-05-30 04:55:04'),
(55,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"38ae3ebac83443258f751be0dd0ea11e\",\"isEmbedding\":1}',NULL,0,'文章处于待审核或审核未通过',5297,'2024-05-30 04:55:17'),
(56,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}',NULL,0,'该操作请勿重复执行',7742,'2024-05-30 04:55:34'),
(57,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,37871,'2024-05-30 04:56:23'),
(58,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,5399,'2024-05-30 04:58:23'),
(59,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,7520,'2024-05-30 04:59:34'),
(60,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,98,'2024-05-30 05:00:03'),
(61,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,71,'2024-05-30 05:00:06'),
(62,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,5483,'2024-05-30 05:13:14'),
(63,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,7662,'2024-05-30 05:13:35'),
(64,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,5088,'2024-05-30 05:15:27'),
(65,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"e18286e338b84ba8993adeae529bc08e\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,6076,'2024-05-30 05:16:06'),
(66,'审核评论',4,'com.xunmeng.article.ArticleCommentController.doAudit()','POST',1,'admin','/article/comment/do','127.0.0.1','内网IP','{\"auditInfo\":\"精彩评论\",\"commentId\":20,\"enable\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"auditInfo\":\"精彩评论\",\"commentId\":20,\"enable\":1,\"params\":{}}}',1,NULL,82,'2024-05-30 10:58:51'),
(67,'自动评论审核',2,'com.xunmeng.article.ArticleCommentController.auto()','PUT',1,'admin','/article/comment/auto/1','127.0.0.1','内网IP','\"1\"','{\"msg\":\"success\",\"code\":200}',1,NULL,44,'2024-05-30 10:58:53'),
(68,'审核评论',4,'com.xunmeng.article.ArticleCommentController.doAudit()','POST',1,'admin','/article/comment/do','127.0.0.1','内网IP','{\"auditInfo\":\"精彩评论\",\"commentId\":24,\"enable\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"auditInfo\":\"精彩评论\",\"commentId\":24,\"enable\":1,\"params\":{}}}',1,NULL,83,'2024-05-30 11:34:24'),
(69,'通知管理',1,'com.xunmeng.system.SysNoticeController.add()','POST',1,'admin','/system/notice/add','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"noticeContent\":\"::: hljs-center\\n\\n居中\\n\\n:::\\n\\n本医院将于2024年6月5日举办心脏病患者健康讲座，届时将有资深心脏病专家分享预防和管理心脏病的知识。欢迎心脏病患者及家属参加。\\n发布日期：2024-05-15\\n发布机构：心血管专科医院\\n\",\"noticeId\":1717045412885,\"noticeTitle\":\"心脏病患者健康讲座\",\"noticeType\":1,\"params\":{},\"status\":1,\"updateBy\":\"admin\"}','{\"msg\":\"success\",\"code\":200,\"data\":1717045412885}',1,NULL,62,'2024-05-30 13:03:34'),
(70,'通知管理',2,'com.xunmeng.system.SysNoticeController.update()','PUT',1,'admin','/system/notice/update','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2024-05-30 13:03:34\",\"noticeContent\":\"::: hljs-center\\n\\n![微信截图_20210124104950.png](http://localhost:8080/profile/upload/notice/2024/05/30/微信截图_20210124104950_20240530130407A001.png)居中\\n\\n:::\\n\\n本医院将于2024年6月5日举办心脏病患者健康讲座，届时将有资深心脏病专家分享预防和管理心脏病的知识。欢迎心脏病患者及家属参加。\\n发布日期：2024-05-15\\n发布机构：心血管专科医院\\n\",\"noticeId\":1717045412885,\"noticeTitle\":\"心脏病患者健康讲座\",\"noticeType\":1,\"params\":{},\"status\":1,\"updateBy\":\"admin\",\"updateTime\":\"2024-05-30 13:04:12\"}','{\"msg\":\"success\",\"code\":200}',1,NULL,52,'2024-05-30 13:04:14'),
(71,'通知管理',2,'com.xunmeng.system.SysNoticeController.update()','PUT',1,'admin','/system/notice/update','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2024-05-30 13:03:34\",\"noticeContent\":\"::: hljs-center\\n\\n![微信截图_20210124104950.png](http://localhost:8080/profile/upload/notice/2024/05/30/微信截图_20210124104950_20240530130407A001.png)\\n\\n:::\\n\\n本医院将于2024年6月5日举办心脏病患者健康讲座，届时将有资深心脏病专家分享预防和管理心脏病的知识。欢迎心脏病患者及家属参加。\\n发布日期：2024-05-15\\n发布机构：心血管专科医院\\n\",\"noticeId\":1717045412885,\"noticeTitle\":\"心脏病患者健康讲座\",\"noticeType\":1,\"params\":{},\"status\":1,\"updateBy\":\"admin\",\"updateTime\":\"2024-05-30 13:04:17\"}','{\"msg\":\"success\",\"code\":200}',1,NULL,46,'2024-05-30 13:04:19'),
(72,'通知管理',2,'com.xunmeng.system.SysNoticeController.update()','PUT',1,'admin','/system/notice/update','127.0.0.1','内网IP','{\"createBy\":\"admin\",\"createTime\":\"2024-05-30 13:03:34\",\"noticeContent\":\"::: hljs-center\\n\\n![微信截图_20210124104950.png](http://localhost:8080/profile/upload/notice/2024/05/30/微信截图_20210124104950_20240530130407A001.png)\\n\\n:::\\n\\n本医院将于2024年6月5日举办心脏病患者健康讲座，届时将有资深心脏病专家分享预防和管理心脏病的知识。欢迎心脏病患者及家属参加。\\n发布日期：2024-05-15\\n发布机构：心血管专科医院\\n\",\"noticeId\":1717045412885,\"noticeTitle\":\"心脏病患者健康讲座\",\"noticeType\":1,\"params\":{},\"status\":1,\"updateBy\":\"admin\",\"updateTime\":\"2024-05-30 13:04:25\"}','{\"msg\":\"success\",\"code\":200}',1,NULL,48,'2024-05-30 13:04:27'),
(73,'通知管理',3,'com.xunmeng.system.SysNoticeController.delete()','DELETE',1,'admin','/system/notice/delete/1717045412885','127.0.0.1','内网IP','{}','{\"msg\":\"success\",\"code\":200}',1,NULL,50,'2024-05-30 13:04:48'),
(74,'审核评论',4,'com.xunmeng.article.ArticleCommentController.doAudit()','POST',1,'admin','/article/comment/do','127.0.0.1','内网IP','{\"auditInfo\":\"精彩评论\",\"commentId\":29,\"enable\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"auditInfo\":\"精彩评论\",\"commentId\":29,\"enable\":1,\"params\":{}}}',1,NULL,98,'2024-06-05 12:28:04'),
(75,'自动向量化',2,'com.xunmeng.article.ArticleEmbeddingController.auto()','PUT',1,'admin','/article/embedding/auto/0','127.0.0.1','内网IP','\"0\"','{\"msg\":\"success\",\"code\":200}',1,NULL,60,'2024-06-05 12:34:51'),
(76,'审核文章',4,'com.xunmeng.article.ArticleAuditController.doAudit()','POST',1,'admin','/article/audit/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"auditInfo\":\"内容非常精彩,继续创作优质论文\",\"auditStatus\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"auditBy\":\"admin\",\"auditInfo\":\"内容非常精彩,继续创作优质论文\",\"auditStatus\":1,\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"isSearch\":0,\"params\":{}}}',1,NULL,134,'2024-06-05 12:42:48'),
(77,'索引审核',4,'com.xunmeng.article.ArticleSearchController.doAudit()','POST',1,'admin','/article/search/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"isSearch\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"ifLike\":false,\"ifTreasure\":false,\"isSearch\":1,\"params\":{}}}',1,NULL,78,'2024-06-05 12:43:40'),
(78,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,75,'2024-06-05 12:45:28'),
(79,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"isEmbedding\":0}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"params\":{}}}',1,NULL,124,'2024-06-05 12:49:32'),
(80,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"isEmbedding\":1}',NULL,0,'文章处于待审核或审核未通过',23,'2024-06-05 12:50:03'),
(81,'审核文章',4,'com.xunmeng.article.ArticleAuditController.doAudit()','POST',1,'admin','/article/audit/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"auditInfo\":\"内容非常精彩,继续创作优质论文\",\"auditStatus\":0}',NULL,0,'非法操作',21,'2024-06-05 12:50:20'),
(82,'审核文章',4,'com.xunmeng.article.ArticleAuditController.doAudit()','POST',1,'admin','/article/audit/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"auditInfo\":\"内容非常精彩,继续创作优质论文\",\"auditStatus\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"auditBy\":\"admin\",\"auditInfo\":\"内容非常精彩,继续创作优质论文\",\"auditStatus\":1,\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":0,\"isSearch\":0,\"params\":{}}}',1,NULL,111,'2024-06-05 12:50:24'),
(83,'索引审核',4,'com.xunmeng.article.ArticleSearchController.doAudit()','POST',1,'admin','/article/search/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"isSearch\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"ifLike\":false,\"ifTreasure\":false,\"isSearch\":1,\"params\":{}}}',1,NULL,64,'2024-06-05 12:50:29'),
(84,'向量编码',4,'com.xunmeng.article.ArticleEmbeddingController.doAudit()','POST',1,'admin','/article/embedding/do','127.0.0.1','内网IP','{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"isEmbedding\":1}','{\"msg\":\"success\",\"code\":200,\"data\":{\"articleId\":\"188a5d5586944d398cc693f5085be2fc\",\"ifLike\":false,\"ifTreasure\":false,\"isEmbedding\":1,\"params\":{}}}',1,NULL,68,'2024-06-05 12:50:34');

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
('employee','普通员工','普通员工','2024-04-06 16:55:42','2024-04-06 16:55:42');

/*Table structure for table `sys_role_menu` */

DROP TABLE IF EXISTS `sys_role_menu`;

CREATE TABLE `sys_role_menu` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一标识',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名称',
  `menu_id` int NOT NULL COMMENT '菜单id',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=207 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_role_menu` */

insert  into `sys_role_menu`(`id`,`role_name`,`menu_id`) values 
(191,'employee',2),
(192,'employee',3),
(193,'employee',15),
(194,'employee',1),
(195,'employee',5),
(196,'employee',6),
(197,'employee',7),
(198,'employee',37),
(199,'employee',38),
(200,'employee',39),
(201,'employee',40),
(202,'employee',8),
(203,'employee',13),
(204,'employee',10),
(205,'employee',56),
(206,'employee',16);

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
('liuteng','刘腾','$2a$10$xS.DTPN9/13UkkNBUvpF0ONlOmVflgRVPJV6t0Akgy7hTEqTpBvKe',NULL,'333972755@qq.com',1,'刘腾-普通员工','2024-04-12 13:03:23','2024-04-12 13:03:23');

/*Table structure for table `sys_user_role` */

DROP TABLE IF EXISTS `sys_user_role`;

CREATE TABLE `sys_user_role` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT '唯一id',
  `user_name` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '用户名',
  `role_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '角色名',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin ROW_FORMAT=DYNAMIC;

/*Data for the table `sys_user_role` */

insert  into `sys_user_role`(`id`,`user_name`,`role_name`) values 
(1,'admin','admin'),
(17,'liuteng','employee');

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

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
