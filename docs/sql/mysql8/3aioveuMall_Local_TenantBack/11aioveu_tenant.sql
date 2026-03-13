/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_tenant

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 13/03/2026 18:36:46
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_table
-- ----------------------------
DROP TABLE IF EXISTS `gen_table`;
CREATE TABLE `gen_table`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名',
  `module_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块名',
  `package_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '包名',
  `business_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务名',
  `entity_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实体类名',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者',
  `parent_menu_id` bigint NULL DEFAULT NULL COMMENT '上级菜单ID，对应sys_menu的id ',
  `remove_table_prefix` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '要移除的表前缀，如: sys_',
  `page_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '页面类型(classic|curd)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tablename`(`table_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table
-- ----------------------------

-- ----------------------------
-- Table structure for gen_table_column
-- ----------------------------
DROP TABLE IF EXISTS `gen_table_column`;
CREATE TABLE `gen_table_column`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `table_id` bigint NOT NULL COMMENT '关联的表配置ID',
  `column_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `column_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `column_length` int NULL DEFAULT NULL,
  `field_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '字段名称',
  `field_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段类型',
  `field_sort` int NULL DEFAULT NULL COMMENT '字段排序',
  `field_comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字段描述',
  `max_length` int NULL DEFAULT NULL,
  `is_required` tinyint(1) NULL DEFAULT NULL COMMENT '是否必填',
  `is_show_in_list` tinyint(1) NULL DEFAULT 0 COMMENT '是否在列表显示',
  `is_show_in_form` tinyint(1) NULL DEFAULT 0 COMMENT '是否在表单显示',
  `is_show_in_query` tinyint(1) NULL DEFAULT 0 COMMENT '是否在查询条件显示',
  `query_type` tinyint NULL DEFAULT NULL COMMENT '查询方式',
  `form_type` tinyint NULL DEFAULT NULL COMMENT '表单类型',
  `dict_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典类型',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_table_id`(`table_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成字段配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_table_column
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置名称',
  `config_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置key',
  `config_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '配置值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '系统限流QPS', 'IP_QPS_THRESHOLD_LIMIT', '10', '单个IP请求的最大每秒查询数（QPS）阈值Key', '2026-02-20 18:09:13', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门名称',
  `code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '部门编号',
  `parent_id` bigint NULL DEFAULT 0 COMMENT '父节点id',
  `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '父节点id路径',
  `sort` smallint NULL DEFAULT 0 COMMENT '显示顺序',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1-正常 0-禁用)',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除标识(1-已删除 0-未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_code`(`tenant_id` ASC, `code` ASC, `is_deleted` ASC) USING BTREE COMMENT '租户内部门编号唯一索引',
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门管理表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, 0, '可我不敌可爱', 'AIOVEU', 0, '0', 1, 1, 1, NULL, 1, '2026-02-20 18:08:45', 0);
INSERT INTO `sys_dept` VALUES (2, 0, '研发部门', 'RD001', 1, '0,1', 1, 1, 2, NULL, 2, '2026-02-20 18:08:45', 0);
INSERT INTO `sys_dept` VALUES (3, 0, '测试部门', 'QA001', 1, '0,1', 1, 1, 2, NULL, 2, '2026-02-20 18:08:46', 0);
INSERT INTO `sys_dept` VALUES (4, 1, '演示公司', 'DEMO_COMPANY', 0, '0', 1, 1, 4, NULL, 4, '2026-02-20 18:08:46', 0);
INSERT INTO `sys_dept` VALUES (5, 1, '演示技术部', 'DEMO_TECH', 4, '0,4', 1, 1, 4, NULL, 5, '2026-02-20 18:08:46', 0);
INSERT INTO `sys_dept` VALUES (6, 1, '演示运营部', 'DEMO_OPER', 4, '0,4', 1, 1, 4, NULL, 6, '2026-02-20 18:08:46', 0);

-- ----------------------------
-- Table structure for sys_dict
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict`;
CREATE TABLE `sys_dict`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键 ',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型编码',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '类型名称',
  `status` tinyint(1) NULL DEFAULT 0 COMMENT '状态(0:正常;1:禁用)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人ID',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '是否删除(1-删除，0-未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_dict_code`(`dict_code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典类型表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'gender', '性别', 1, NULL, '2026-02-20 18:08:48', 1, '2026-02-20 18:08:48', 1, 0);
INSERT INTO `sys_dict` VALUES (2, 'notice_type', '通知类型', 1, NULL, '2026-02-20 18:08:48', 1, '2026-02-20 18:08:48', 1, 0);
INSERT INTO `sys_dict` VALUES (3, 'notice_level', '通知级别', 1, NULL, '2026-02-20 18:08:48', 1, '2026-02-20 18:08:48', 1, 0);

-- ----------------------------
-- Table structure for sys_dict_item
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_item`;
CREATE TABLE `sys_dict_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `dict_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '关联字典编码，与sys_dict表中的dict_code对应',
  `value` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项值',
  `label` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '字典项标签',
  `tag_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '标签类型，用于前端样式展示（如success、warning等）',
  `status` tinyint NULL DEFAULT 0 COMMENT '状态（1-正常，0-禁用）',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人ID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '数据字典项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'gender', '1', '男', 'primary', 1, 1, NULL, '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (2, 'gender', '2', '女', 'danger', 1, 2, NULL, '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (3, 'gender', '0', '保密', 'info', 1, 3, NULL, '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (4, 'notice_type', '1', '系统升级', 'success', 1, 1, '', '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (5, 'notice_type', '2', '系统维护', 'primary', 1, 2, '', '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (6, 'notice_type', '3', '安全警告', 'danger', 1, 3, '', '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (7, 'notice_type', '4', '假期通知', 'success', 1, 4, '', '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (8, 'notice_type', '5', '公司新闻', 'primary', 1, 5, '', '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (9, 'notice_type', '99', '其他', 'info', 1, 99, '', '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (10, 'notice_level', 'L', '低', 'info', 1, 1, '', '2026-02-20 18:08:49', 1, '2026-02-20 18:08:49', 1);
INSERT INTO `sys_dict_item` VALUES (11, 'notice_level', 'M', '中', 'warning', 1, 2, '', '2026-02-20 18:08:50', 1, '2026-02-20 18:08:50', 1);
INSERT INTO `sys_dict_item` VALUES (12, 'notice_level', 'H', '高', 'danger', 1, 3, '', '2026-02-20 18:08:50', 1, '2026-02-20 18:08:50', 1);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  `module` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志模块',
  `request_method` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '请求方式',
  `request_params` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求参数(批量请求参数可能会超过text)',
  `response_content` mediumtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '返回参数',
  `content` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '日志内容',
  `request_uri` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '请求路径',
  `method` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '方法名',
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'IP地址',
  `province` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `execution_time` bigint NULL DEFAULT NULL COMMENT '执行时间(ms)',
  `browser` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器',
  `browser_version` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '浏览器版本',
  `os` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '终端系统',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除标识(1-已删除 0-未删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_create_time`(`create_time`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` bigint NOT NULL COMMENT '父菜单ID',
  `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父节点ID路径',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单类型（C-目录 M-菜单 B-按钮）',
  `route_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由名称（Vue Router 中用于命名路由）',
  `route_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由路径（Vue Router 中定义的 URL 路径）',
  `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径（组件页面完整路径，相对于 src/views/，缺省后缀 .vue）',
  `perm` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '【按钮】权限标识',
  `always_show` tinyint NULL DEFAULT 0 COMMENT '【目录】只有一个子路由是否始终显示（1-是 0-否）',
  `keep_alive` tinyint NULL DEFAULT 0 COMMENT '【菜单】是否开启页面缓存（1-是 0-否）',
  `visible` tinyint(1) NULL DEFAULT 1 COMMENT '显示状态（1-显示 0-隐藏）',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `redirect` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转路径',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `scope` tinyint(1) NOT NULL DEFAULT 2 COMMENT '菜单范围(1=平台菜单 2=业务菜单)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2976 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '0', '平台管理', 'C', '', '/platform', 'Layout', NULL, NULL, NULL, 1, 1, 'el-icon-Platform', '/platform/tenant', '2026-02-20 18:08:50', '2026-02-20 18:08:50', NULL, 1);
INSERT INTO `sys_menu` VALUES (2, 0, '0', '系统管理', 'C', '', '/system', 'Layout', NULL, NULL, NULL, 1, 31, 'system', '/system/user', '2026-02-20 18:08:51', '2026-03-13 15:40:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (3, 0, '0', '代码生成', 'C', '', '/codegen', 'Layout', NULL, NULL, NULL, 1, 33, 'code', '/codegen/index', '2026-02-20 18:08:51', '2026-03-13 15:40:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (5, 0, '0', '平台文档', 'C', '', '/doc', 'Layout', NULL, NULL, NULL, 1, 35, 'document', '', '2026-02-20 18:08:51', '2026-03-13 15:41:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (6, 0, '0', '接口文档', 'C', '', '/api', 'Layout', NULL, NULL, NULL, 1, 36, 'api', '', '2026-02-20 18:08:51', '2026-03-13 15:41:11', NULL, 2);
INSERT INTO `sys_menu` VALUES (7, 0, '0', '组件封装', 'C', '', '/component', 'Layout', NULL, NULL, NULL, 1, 37, 'menu', '', '2026-02-20 18:08:51', '2026-03-13 15:41:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (8, 0, '0', '功能演示', 'C', '', '/function', 'Layout', NULL, NULL, NULL, 1, 38, 'menu', '', '2026-02-20 18:08:51', '2026-03-13 15:41:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (9, 0, '0', '多级菜单', 'C', NULL, '/multi-level', 'Layout', NULL, 1, NULL, 1, 39, 'cascader', '', '2026-02-20 18:08:51', '2026-03-13 15:41:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (10, 0, '0', '路由参数', 'C', '', '/route-param', 'Layout', NULL, NULL, NULL, 1, 40, 'el-icon-ElementPlus', '', '2026-02-20 18:08:52', '2026-03-13 15:41:36', NULL, 2);
INSERT INTO `sys_menu` VALUES (110, 1, '0,1', '租户管理', 'M', 'Tenant', 'tenant', 'system/tenant/index', NULL, NULL, 1, 1, 1, 'el-icon-OfficeBuilding', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (120, 1, '0,1', '租户套餐', 'M', 'TenantPlan', 'tenant-plan', 'system/tenant/plan', NULL, NULL, 1, 1, 2, 'el-icon-CollectionTag', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (210, 2, '0,2', '用户管理', 'M', 'User', 'user', 'system/user/index', NULL, NULL, 1, 1, 1, 'el-icon-User', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (220, 2, '0,2', '角色管理', 'M', 'Role', 'role', 'system/role/index', NULL, NULL, 1, 1, 2, 'role', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (230, 1, '0,1', '菜单管理', 'M', 'SysMenu', 'menu', 'system/menu/index', NULL, NULL, 1, 1, 2, 'menu', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (240, 2, '0,2', '部门管理', 'M', 'Dept', 'dept', 'system/dept/index', NULL, NULL, 1, 1, 4, 'tree', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (250, 2, '0,2', '字典管理', 'M', 'Dict', 'dict', 'system/dict/index', NULL, NULL, 1, 1, 5, 'dict', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (251, 250, '0,2,250', '字典项', 'M', 'DictItem', 'dict-item', 'system/dict/dict-item', NULL, 0, 1, 0, 6, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (260, 2, '0,2', '系统日志', 'M', 'Log', 'log', 'system/log/index', NULL, 0, 1, 1, 7, 'document', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (270, 1, '0,1', '系统配置', 'M', 'Config', 'config', 'system/config/index', NULL, 0, 1, 1, 3, 'setting', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (280, 2, '0,2', '通知公告', 'M', 'Notice', 'notice', 'system/notice/index', NULL, NULL, NULL, 1, 9, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (310, 3, '0,3', '代码生成', 'M', 'Codegen', 'codegen', 'codegen/index', NULL, NULL, 1, 1, 1, 'code', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (501, 5, '0,5', '平台文档(外链)', 'M', NULL, 'https://juejin.cn/post/7228990409909108793', '', NULL, NULL, NULL, 1, 1, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (502, 5, '0,5', '后端文档', 'M', NULL, 'https://youlai.blog.csdn.net/article/details/145178880', '', NULL, NULL, NULL, 1, 2, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (503, 5, '0,5', '移动端文档', 'M', NULL, 'https://youlai.blog.csdn.net/article/details/143222890', '', NULL, NULL, NULL, 1, 3, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (504, 5, '0,5', '内部文档', 'M', NULL, 'internal-doc', 'demo/internal-doc', NULL, NULL, NULL, 1, 4, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (601, 6, '0,6', 'Apifox', 'M', 'Apifox', 'apifox', 'demo/api/apifox', NULL, NULL, 1, 1, 1, 'api', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (701, 7, '0,7', '富文本编辑器', 'M', 'WangEditor', 'wang-editor', 'demo/wang-editor', NULL, NULL, 1, 1, 2, '', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (702, 7, '0,7', '图片上传', 'M', 'Upload', 'upload', 'demo/upload', NULL, NULL, 1, 1, 3, '', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (703, 7, '0,7', '图标选择器', 'M', 'IconSelect', 'icon-select', 'demo/icon-select', NULL, NULL, 1, 1, 4, '', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (704, 7, '0,7', '字典组件', 'M', 'DictDemo', 'dict-demo', 'demo/dictionary', NULL, NULL, 1, 1, 4, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (705, 7, '0,7', '增删改查', 'M', 'Curd', 'curd', 'demo/curd/index', NULL, NULL, 1, 1, 0, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (706, 7, '0,7', '列表选择器', 'M', 'TableSelect', 'table-select', 'demo/table-select/index', NULL, NULL, 1, 1, 1, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (707, 7, '0,7', '拖拽组件', 'M', 'Drag', 'drag', 'demo/drag', NULL, NULL, NULL, 1, 5, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (708, 7, '0,7', '滚动文本', 'M', 'TextScroll', 'text-scroll', 'demo/text-scroll', NULL, NULL, NULL, 1, 6, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (709, 7, '0,7', '自适应表格操作列', 'M', 'AutoOperationColumn', 'operation-column', 'demo/auto-operation-column', NULL, NULL, 1, 1, 1, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (801, 8, '0,8', 'Websocket', 'M', 'WebSocket', '/function/websocket', 'demo/websocket', NULL, NULL, 1, 1, 1, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (802, 8, '0,8', 'Icons', 'M', 'IconDemo', 'icon-demo', 'demo/icons', NULL, NULL, 1, 1, 2, 'el-icon-Notification', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (803, 8, '0,8', '字典实时同步', 'M', 'DictSync', 'dict-sync', 'demo/dict-sync', NULL, NULL, NULL, 1, 3, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (804, 8, '0,8', 'VxeTable', 'M', 'VxeTable', 'vxe-table', 'demo/vxe-table/index', NULL, NULL, 1, 1, 4, 'el-icon-MagicStick', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (805, 8, '0,8', 'CURD单文件', 'M', 'CurdSingle', 'curd-single', 'demo/curd-single', NULL, NULL, 1, 1, 5, 'el-icon-Reading', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (910, 9, '0,9', '菜单一级', 'C', NULL, 'multi-level1', 'Layout', NULL, 1, NULL, 1, 1, '', '', '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (911, 910, '0,9,910', '菜单二级', 'C', NULL, 'multi-level2', 'Layout', NULL, 0, NULL, 1, 1, '', NULL, '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (912, 911, '0,9,910,911', '菜单三级-1', 'M', NULL, 'multi-level3-1', 'demo/multi-level/children/children/level3-1', NULL, 0, 1, 1, 1, '', '', '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (913, 911, '0,9,910,911', '菜单三级-2', 'M', NULL, 'multi-level3-2', 'demo/multi-level/children/children/level3-2', NULL, 0, 1, 1, 2, '', '', '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (1001, 10, '0,10', '参数(type=1)', 'M', 'RouteParamType1', 'route-param-type1', 'demo/route-param', NULL, 0, 1, 1, 1, 'el-icon-Star', NULL, '2026-02-20 18:08:59', '2026-02-20 18:08:59', '{\"type\": \"1\"}', 1);
INSERT INTO `sys_menu` VALUES (1002, 10, '0,10', '参数(type=2)', 'M', 'RouteParamType2', 'route-param-type2', 'demo/route-param', NULL, 0, 1, 1, 2, 'el-icon-StarFilled', NULL, '2026-02-20 18:08:59', '2026-02-20 18:08:59', '{\"type\": \"2\"}', 1);
INSERT INTO `sys_menu` VALUES (1101, 110, '0,1,110', '租户查询', 'B', NULL, '', NULL, 'sys:tenant:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1102, 110, '0,1,110', '租户新增', 'B', NULL, '', NULL, 'sys:tenant:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1103, 110, '0,1,110', '租户编辑', 'B', NULL, '', NULL, 'sys:tenant:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1104, 110, '0,1,110', '租户删除', 'B', NULL, '', NULL, 'sys:tenant:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1105, 110, '0,1,110', '租户启用/禁用', 'B', NULL, '', NULL, 'sys:tenant:change-status', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1106, 110, '0,1,110', '租户切换', 'B', NULL, '', NULL, 'sys:tenant:switch', NULL, NULL, 1, 6, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1107, 110, '0,1,110', '设置套餐', 'B', NULL, '', NULL, 'sys:tenant:plan-assign', NULL, NULL, 1, 7, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1201, 120, '0,1,120', '套餐查询', 'B', NULL, '', NULL, 'sys:tenant-plan:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1202, 120, '0,1,120', '套餐新增', 'B', NULL, '', NULL, 'sys:tenant-plan:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1203, 120, '0,1,120', '套餐编辑', 'B', NULL, '', NULL, 'sys:tenant-plan:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1204, 120, '0,1,120', '套餐删除', 'B', NULL, '', NULL, 'sys:tenant-plan:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1205, 120, '0,1,120', '套餐菜单配置', 'B', NULL, '', NULL, 'sys:tenant-plan:assign', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (2101, 210, '0,2,210', '用户查询', 'B', NULL, '', NULL, 'sys:user:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2102, 210, '0,2,210', '用户新增', 'B', NULL, '', NULL, 'sys:user:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2103, 210, '0,2,210', '用户编辑', 'B', NULL, '', NULL, 'sys:user:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2104, 210, '0,2,210', '用户删除', 'B', NULL, '', NULL, 'sys:user:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2105, 210, '0,2,210', '重置密码', 'B', NULL, '', NULL, 'sys:user:reset-password', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2106, 210, '0,2,210', '用户导入', 'B', NULL, '', NULL, 'sys:user:import', NULL, NULL, 1, 6, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2107, 210, '0,2,210', '用户导出', 'B', NULL, '', NULL, 'sys:user:export', NULL, NULL, 1, 7, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2201, 220, '0,2,220', '角色查询', 'B', NULL, '', NULL, 'sys:role:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2202, 220, '0,2,220', '角色新增', 'B', NULL, '', NULL, 'sys:role:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2203, 220, '0,2,220', '角色编辑', 'B', NULL, '', NULL, 'sys:role:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2204, 220, '0,2,220', '角色删除', 'B', NULL, '', NULL, 'sys:role:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2205, 220, '0,2,220', '角色分配权限', 'B', NULL, '', NULL, 'sys:role:assign', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2301, 230, '0,1,230', '菜单查询', 'B', NULL, '', NULL, 'sys:menu:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (2302, 230, '0,1,230', '菜单新增', 'B', NULL, '', NULL, 'sys:menu:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (2303, 230, '0,1,230', '菜单编辑', 'B', NULL, '', NULL, 'sys:menu:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (2304, 230, '0,1,230', '菜单删除', 'B', NULL, '', NULL, 'sys:menu:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 1);
INSERT INTO `sys_menu` VALUES (2401, 240, '0,2,240', '部门查询', 'B', NULL, '', NULL, 'sys:dept:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2402, 240, '0,2,240', '部门新增', 'B', NULL, '', NULL, 'sys:dept:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2403, 240, '0,2,240', '部门编辑', 'B', NULL, '', NULL, 'sys:dept:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2404, 240, '0,2,240', '部门删除', 'B', NULL, '', NULL, 'sys:dept:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2501, 250, '0,2,250', '字典查询', 'B', NULL, '', NULL, 'sys:dict:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2502, 250, '0,2,250', '字典新增', 'B', NULL, '', NULL, 'sys:dict:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2503, 250, '0,2,250', '字典编辑', 'B', NULL, '', NULL, 'sys:dict:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2504, 250, '0,2,250', '字典删除', 'B', NULL, '', NULL, 'sys:dict:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2511, 251, '0,2,250,251', '字典项查询', 'B', NULL, '', NULL, 'sys:dict-item:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2512, 251, '0,2,250,251', '字典项新增', 'B', NULL, '', NULL, 'sys:dict-item:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2513, 251, '0,2,250,251', '字典项编辑', 'B', NULL, '', NULL, 'sys:dict-item:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2514, 251, '0,2,250,251', '字典项删除', 'B', NULL, '', NULL, 'sys:dict-item:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2701, 270, '0,1,270', '系统配置查询', 'B', NULL, '', NULL, 'sys:config:list', 0, 1, 1, 1, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2702, 270, '0,1,270', '系统配置新增', 'B', NULL, '', NULL, 'sys:config:create', 0, 1, 1, 2, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2703, 270, '0,1,270', '系统配置修改', 'B', NULL, '', NULL, 'sys:config:update', 0, 1, 1, 3, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2704, 270, '0,1,270', '系统配置删除', 'B', NULL, '', NULL, 'sys:config:delete', 0, 1, 1, 4, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2705, 270, '0,1,270', '系统配置刷新', 'B', NULL, '', NULL, 'sys:config:refresh', 0, 1, 1, 5, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2801, 280, '0,2,280', '通知查询', 'B', NULL, '', NULL, 'sys:notice:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2802, 280, '0,2,280', '通知新增', 'B', NULL, '', NULL, 'sys:notice:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2803, 280, '0,2,280', '通知编辑', 'B', NULL, '', NULL, 'sys:notice:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2804, 280, '0,2,280', '通知删除', 'B', NULL, '', NULL, 'sys:notice:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2805, 280, '0,2,280', '通知发布', 'B', NULL, '', NULL, 'sys:notice:publish', 0, 1, 1, 5, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2806, 280, '0,2,280', '通知撤回', 'B', NULL, '', NULL, 'sys:notice:revoke', 0, 1, 1, 6, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2807, 0, '0', '订单管理', 'C', NULL, '/oms', 'Layout', NULL, 1, 1, 1, 1, 'file', NULL, '2026-03-13 15:40:27', '2026-03-13 15:40:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2808, 0, '0', '商品管理', 'C', NULL, '/pms', 'Layout', NULL, 1, 1, 1, 2, 'file', NULL, '2026-03-13 16:33:59', '2026-03-13 16:33:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2809, 0, '0', '营销管理', 'C', NULL, '/sms', 'Layout', NULL, 1, 1, 1, 3, 'file', NULL, '2026-03-13 16:34:20', '2026-03-13 16:34:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2810, 0, '0', '会员管理', 'C', NULL, '/ums', 'Layout', NULL, 1, 1, 1, 4, 'user', NULL, '2026-03-13 16:34:59', '2026-03-13 16:34:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2811, 0, '0', '支付管理', 'C', NULL, '/pay', 'Layout', NULL, 1, 1, 1, 5, 'file', NULL, '2026-03-13 16:35:25', '2026-03-13 16:35:25', NULL, 2);
INSERT INTO `sys_menu` VALUES (2812, 0, '0', '退款管理', 'C', NULL, '/refund', 'Layout', NULL, 1, 1, 1, 6, 'file', NULL, '2026-03-13 16:35:47', '2026-03-13 16:35:47', NULL, 2);
INSERT INTO `sys_menu` VALUES (2813, 2807, '0,2807', '订单详情', 'M', 'OmsOrder', 'oms-order', 'aioveuMallOmsOrder/oms-order/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 16:41:04', '2026-03-13 16:41:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2814, 2813, '0,2807,2813', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:41:28', '2026-03-13 16:41:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2816, 2813, '0,2807,2813', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:42:17', '2026-03-13 16:42:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2817, 2813, '0,2807,2813', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:42:31', '2026-03-13 16:42:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2818, 2813, '0,2807,2813', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:42:47', '2026-03-13 16:42:47', NULL, 2);
INSERT INTO `sys_menu` VALUES (2819, 2807, '0,2807', '订单商品信息', 'M', 'OmsOrderItem', 'oms-order-item', 'aioveuMallOmsOrderItem/oms-order-item/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 16:43:21', '2026-03-13 16:43:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2820, 2819, '0,2807,2819', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:43:35', '2026-03-13 16:43:35', NULL, 2);
INSERT INTO `sys_menu` VALUES (2821, 2819, '0,2807,2819', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:43:50', '2026-03-13 16:43:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2822, 2819, '0,2807,2819', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:44:06', '2026-03-13 16:44:06', NULL, 2);
INSERT INTO `sys_menu` VALUES (2823, 2819, '0,2807,2819', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:44:21', '2026-03-13 16:44:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2824, 2807, '0,2807', '订单物流记录', 'M', 'OmsOrderDelivery', 'oms-order-delivery', 'aioveuMallOmsOrderDelivery/oms-order-delivery/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 16:44:51', '2026-03-13 16:44:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2825, 2824, '0,2807,2824', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:45:36', '2026-03-13 16:45:36', NULL, 2);
INSERT INTO `sys_menu` VALUES (2826, 2824, '0,2807,2824', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:45:51', '2026-03-13 16:45:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2827, 2824, '0,2807,2824', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:46:05', '2026-03-13 16:46:05', NULL, 2);
INSERT INTO `sys_menu` VALUES (2828, 2824, '0,2807,2824', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:46:19', '2026-03-13 16:46:19', NULL, 2);
INSERT INTO `sys_menu` VALUES (2829, 2807, '0,2807', '订单操作历史记录', 'M', 'OmsOrderLog', 'oms-order-log', 'aioveuMallOmsOrderLog/oms-order-log/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 16:46:56', '2026-03-13 16:46:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2830, 2829, '0,2807,2829', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:47:10', '2026-03-13 16:47:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2831, 2829, '0,2807,2829', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:47:24', '2026-03-13 16:47:24', NULL, 2);
INSERT INTO `sys_menu` VALUES (2832, 2829, '0,2807,2829', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:47:38', '2026-03-13 16:47:38', NULL, 2);
INSERT INTO `sys_menu` VALUES (2833, 2829, '0,2807,2829', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:47:52', '2026-03-13 16:47:52', NULL, 2);
INSERT INTO `sys_menu` VALUES (2834, 2807, '0,2807', '支付信息', 'M', 'OmsOrderPay', 'oms-order-pay', 'aioveuMallOmsOrderPay/oms-order-pay/index', NULL, 1, 1, 1, 5, 'menu', NULL, '2026-03-13 16:48:39', '2026-03-13 16:48:39', NULL, 2);
INSERT INTO `sys_menu` VALUES (2835, 2834, '0,2807,2834', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:48:55', '2026-03-13 16:48:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2836, 2834, '0,2807,2834', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:49:10', '2026-03-13 16:49:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2837, 2834, '0,2807,2834', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:49:23', '2026-03-13 16:49:23', NULL, 2);
INSERT INTO `sys_menu` VALUES (2838, 2834, '0,2807,2834', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:49:35', '2026-03-13 16:49:35', NULL, 2);
INSERT INTO `sys_menu` VALUES (2839, 2807, '0,2807', '订单配置信息', 'M', 'OmsOrderSetting', 'oms-order-setting', 'aioveuMallOmsOrderSetting/oms-order-setting/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 16:50:05', '2026-03-13 16:50:05', NULL, 2);
INSERT INTO `sys_menu` VALUES (2840, 2839, '0,2807,2839', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:50:20', '2026-03-13 16:50:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2841, 2839, '0,2807,2839', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:50:33', '2026-03-13 16:50:33', NULL, 2);
INSERT INTO `sys_menu` VALUES (2842, 2839, '0,2807,2839', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:50:46', '2026-03-13 16:50:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2843, 2839, '0,2807,2839', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:50:59', '2026-03-13 16:50:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2844, 2808, '0,2808', '商品上架', 'M', 'GoodsDetail', 'pms-detail', 'aioveuMallPmsGoods/pms-detail/index', NULL, 1, 1, 1, 0, 'menu', NULL, '2026-03-13 16:51:54', '2026-03-13 16:51:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2845, 2808, '0,2808', '商品品牌', 'M', 'PmsBrand', 'pms-brand', 'aioveuMallPmsBrand/pms-brand/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 16:52:24', '2026-03-13 16:54:26', NULL, 2);
INSERT INTO `sys_menu` VALUES (2846, 2845, '0,2808,2845', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:54:41', '2026-03-13 16:54:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2847, 2845, '0,2808,2845', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:55:01', '2026-03-13 16:55:01', NULL, 2);
INSERT INTO `sys_menu` VALUES (2848, 2845, '0,2808,2845', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:55:19', '2026-03-13 16:55:19', NULL, 2);
INSERT INTO `sys_menu` VALUES (2849, 2845, '0,2808,2845', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:55:31', '2026-03-13 16:55:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2850, 2808, '0,2808', '商品分类', 'M', 'PmsCategory', 'pms-category', 'aioveuMallPmsCategory/pms-category/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 16:56:03', '2026-03-13 16:56:03', NULL, 2);
INSERT INTO `sys_menu` VALUES (2851, 2850, '0,2808,2850', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:56:17', '2026-03-13 16:56:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2852, 2850, '0,2808,2850', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:56:43', '2026-03-13 16:56:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2853, 2850, '0,2808,2850', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:56:54', '2026-03-13 16:56:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2854, 2850, '0,2808,2850', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:57:07', '2026-03-13 16:57:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2855, 2808, '0,2808', '商品分类树级', 'M', 'PmsCategoryTree', 'pms-categoryTree', 'aioveuMallPmsCategoryTree/pms-categoryTree/categoryTree', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 16:57:37', '2026-03-13 16:57:37', NULL, 2);
INSERT INTO `sys_menu` VALUES (2856, 2808, '0,2808', '商品分类类型（规格，属性）', 'M', 'PmsCategoryAttribute', 'pms-category-attribute', 'aioveuMallPmsCategoryAttribute/pms-category-attribute/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 16:58:09', '2026-03-13 16:58:09', NULL, 2);
INSERT INTO `sys_menu` VALUES (2857, 2856, '0,2808,2856', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:58:27', '2026-03-13 16:58:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2858, 2856, '0,2808,2856', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:58:40', '2026-03-13 16:58:40', NULL, 2);
INSERT INTO `sys_menu` VALUES (2859, 2856, '0,2808,2856', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:58:52', '2026-03-13 16:58:52', NULL, 2);
INSERT INTO `sys_menu` VALUES (2860, 2856, '0,2808,2856', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:59:03', '2026-03-13 16:59:03', NULL, 2);
INSERT INTO `sys_menu` VALUES (2861, 2808, '0,2808', '商品库存', 'M', 'PmsSku', 'pms-sku', 'aioveuMallPmsSku/pms-sku/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 17:00:01', '2026-03-13 17:00:01', NULL, 2);
INSERT INTO `sys_menu` VALUES (2862, 2861, '0,2808,2861', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:00:18', '2026-03-13 17:00:18', NULL, 2);
INSERT INTO `sys_menu` VALUES (2863, 2861, '0,2808,2861', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:00:32', '2026-03-13 17:00:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2864, 2861, '0,2808,2861', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:00:46', '2026-03-13 17:00:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2865, 2861, '0,2808,2861', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:01:03', '2026-03-13 17:01:03', NULL, 2);
INSERT INTO `sys_menu` VALUES (2866, 2808, '0,2808', '商品', 'M', 'PmsSpu', 'pms-spu', 'aioveuMallPmsSpu/pms-spu/index', NULL, 1, 1, 1, 7, 'menu', NULL, '2026-03-13 17:07:23', '2026-03-13 17:07:23', NULL, 2);
INSERT INTO `sys_menu` VALUES (2867, 2866, '0,2808,2866', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:07:37', '2026-03-13 17:07:37', NULL, 2);
INSERT INTO `sys_menu` VALUES (2868, 2866, '0,2808,2866', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:07:56', '2026-03-13 17:07:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2869, 2866, '0,2808,2866', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:08:10', '2026-03-13 17:08:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2870, 2866, '0,2808,2866', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:08:22', '2026-03-13 17:08:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (2871, 2808, '0,2808', '商品类型（属性/规格）', 'M', 'PmsSpuAttribute', 'pms-spu-attribute', 'aioveuMallPmsSpuAttribute/pms-spu-attribute/index', NULL, 1, 1, 1, 8, 'menu', NULL, '2026-03-13 17:09:00', '2026-03-13 17:09:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (2872, 2871, '0,2808,2871', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:09:42', '2026-03-13 17:09:42', NULL, 2);
INSERT INTO `sys_menu` VALUES (2873, 2871, '0,2808,2871', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:10:13', '2026-03-13 17:10:13', NULL, 2);
INSERT INTO `sys_menu` VALUES (2874, 2871, '0,2808,2871', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:10:28', '2026-03-13 17:10:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2875, 2871, '0,2808,2871', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:10:41', '2026-03-13 17:10:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2876, 2809, '0,2809', '广告', 'M', 'SmsAdvert', 'sms-advert', 'aioveuMallSmsAdvert/sms-advert/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:11:29', '2026-03-13 17:11:29', NULL, 2);
INSERT INTO `sys_menu` VALUES (2877, 2876, '0,2809,2876', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:11:45', '2026-03-13 17:11:45', NULL, 2);
INSERT INTO `sys_menu` VALUES (2878, 2876, '0,2809,2876', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:11:59', '2026-03-13 17:11:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2879, 2876, '0,2809,2876', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:12:11', '2026-03-13 17:12:11', NULL, 2);
INSERT INTO `sys_menu` VALUES (2880, 2876, '0,2809,2876', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:12:24', '2026-03-13 17:12:24', NULL, 2);
INSERT INTO `sys_menu` VALUES (2881, 2809, '0,2809', '优惠券', 'M', 'SmsCoupon', 'sms-coupon', 'aioveuMallSmsCoupon/sms-coupon/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:12:50', '2026-03-13 17:12:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2882, 2881, '0,2809,2881', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:13:04', '2026-03-13 17:13:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2883, 2881, '0,2809,2881', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:13:18', '2026-03-13 17:13:18', NULL, 2);
INSERT INTO `sys_menu` VALUES (2884, 2881, '0,2809,2881', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:13:32', '2026-03-13 17:13:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2885, 2881, '0,2809,2881', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:13:44', '2026-03-13 17:13:44', NULL, 2);
INSERT INTO `sys_menu` VALUES (2886, 2809, '0,2809', '优惠券领取/使用记录', 'M', 'SmsCouponHistory', 'sms-coupon-history', 'aioveuMallSmsCouponHistory/sms-coupon-history/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 17:14:10', '2026-03-13 17:14:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2887, 2886, '0,2809,2886', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:14:21', '2026-03-13 17:14:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2888, 2886, '0,2809,2886', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:14:36', '2026-03-13 17:14:36', NULL, 2);
INSERT INTO `sys_menu` VALUES (2889, 2886, '0,2809,2886', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:14:48', '2026-03-13 17:14:48', NULL, 2);
INSERT INTO `sys_menu` VALUES (2890, 2886, '0,2809,2886', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:15:00', '2026-03-13 17:15:00', NULL, 2);
INSERT INTO `sys_menu` VALUES (2891, 2809, '0,2809', '优惠券适用的具体商品', 'M', 'SmsCouponSpu', 'sms-coupon-spu', 'aioveuMallSmsCouponSpu/sms-coupon-spu/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 17:15:31', '2026-03-13 17:15:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2892, 2891, '0,2809,2891', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:15:51', '2026-03-13 17:15:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2893, 2891, '0,2809,2891', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:16:07', '2026-03-13 17:16:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2894, 2891, '0,2809,2891', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:16:17', '2026-03-13 17:16:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2895, 2891, '0,2809,2891', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:16:28', '2026-03-13 17:16:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2896, 2809, '0,2809', '优惠券适用的具体分类', 'M', 'SmsCouponSpuCategory', 'sms-coupon-spu-category', 'aioveuMallSmsCouponSpuCategory/sms-coupon-spu-category/index', NULL, 1, 1, 1, 5, 'menu', NULL, '2026-03-13 17:16:56', '2026-03-13 17:16:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2897, 2896, '0,2809,2896', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:17:08', '2026-03-13 17:17:08', NULL, 2);
INSERT INTO `sys_menu` VALUES (2898, 2896, '0,2809,2896', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:17:22', '2026-03-13 17:17:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (2899, 2896, '0,2809,2896', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:17:34', '2026-03-13 17:17:34', NULL, 2);
INSERT INTO `sys_menu` VALUES (2900, 2896, '0,2809,2896', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:17:46', '2026-03-13 17:17:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2901, 2809, '0,2809', '首页分类配置', 'M', 'SmsHomeCategory', 'sms-home-category', 'aioveuMallSmsHomeCategory/sms-home-category/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 17:18:14', '2026-03-13 17:18:14', NULL, 2);
INSERT INTO `sys_menu` VALUES (2902, 2901, '0,2809,2901', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:18:30', '2026-03-13 17:18:30', NULL, 2);
INSERT INTO `sys_menu` VALUES (2903, 2901, '0,2809,2901', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:18:41', '2026-03-13 17:18:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2904, 2901, '0,2809,2901', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:18:52', '2026-03-13 17:18:52', NULL, 2);
INSERT INTO `sys_menu` VALUES (2905, 2901, '0,2809,2901', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:19:07', '2026-03-13 17:19:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2906, 2809, '0,2809', '首页广告配置', 'M', 'SmsHomeAdvert', 'sms-home-advert', 'aioveuMallSmsHomeAdvert/sms-home-advert/index', NULL, 1, 1, 1, 7, 'menu', NULL, '2026-03-13 17:19:34', '2026-03-13 17:19:34', NULL, 2);
INSERT INTO `sys_menu` VALUES (2907, 2906, '0,2809,2906', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:19:46', '2026-03-13 17:19:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2908, 2906, '0,2809,2906', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:19:59', '2026-03-13 17:19:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2909, 2906, '0,2809,2906', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:20:12', '2026-03-13 17:20:12', NULL, 2);
INSERT INTO `sys_menu` VALUES (2910, 2906, '0,2809,2906', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:20:23', '2026-03-13 17:20:23', NULL, 2);
INSERT INTO `sys_menu` VALUES (2911, 2810, '0,2810', '会员', 'M', 'UmsMember', 'ums-member', 'aioveuMallUmsMember/ums-member/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:23:31', '2026-03-13 17:23:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2912, 2911, '0,2810,2911', '查询', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:23:54', '2026-03-13 17:23:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2913, 2911, '0,2810,2911', '新增', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:24:07', '2026-03-13 17:24:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2914, 2911, '0,2810,2911', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:24:20', '2026-03-13 17:24:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2915, 2911, '0,2810,2911', '删除', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:24:37', '2026-03-13 17:24:37', NULL, 2);
INSERT INTO `sys_menu` VALUES (2916, 2810, '0,2810', '会员收货地址', 'M', 'UmsMemberAddress', 'ums-member-address', 'aioveuMallUmsMemberAddress/ums-member-address/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:25:01', '2026-03-13 17:25:01', NULL, 2);
INSERT INTO `sys_menu` VALUES (2917, 2916, '0,2810,2916', '查询', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:25:15', '2026-03-13 17:25:15', NULL, 2);
INSERT INTO `sys_menu` VALUES (2918, 2916, '0,2810,2916', '新增', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:25:29', '2026-03-13 17:25:29', NULL, 2);
INSERT INTO `sys_menu` VALUES (2919, 2916, '0,2810,2916', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:25:43', '2026-03-13 17:25:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2920, 2916, '0,2810,2916', '删除', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:25:57', '2026-03-13 17:25:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2921, 2811, '0,2811', '支付订单', 'M', 'PayOrder', 'pay-order', 'aioveuMallPayOrder/pay-order/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:26:43', '2026-03-13 17:26:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2922, 2921, '0,2811,2921', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:26:59', '2026-03-13 17:26:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2923, 2921, '0,2811,2921', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:27:14', '2026-03-13 17:27:14', NULL, 2);
INSERT INTO `sys_menu` VALUES (2924, 2921, '0,2811,2921', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:27:27', '2026-03-13 17:27:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2925, 2921, '0,2811,2921', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:27:41', '2026-03-13 17:27:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2926, 2811, '0,2811', '退款记录', 'M', 'PayRefundRecord', 'pay-refund-record', 'aioveuMallPayRefundRecord/pay-refund-record/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:28:06', '2026-03-13 17:28:06', NULL, 2);
INSERT INTO `sys_menu` VALUES (2927, 2926, '0,2811,2926', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:28:19', '2026-03-13 17:28:19', NULL, 2);
INSERT INTO `sys_menu` VALUES (2928, 2926, '0,2811,2926', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:28:34', '2026-03-13 17:28:34', NULL, 2);
INSERT INTO `sys_menu` VALUES (2929, 2926, '0,2811,2926', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:28:45', '2026-03-13 17:28:45', NULL, 2);
INSERT INTO `sys_menu` VALUES (2930, 2926, '0,2811,2926', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:28:57', '2026-03-13 17:28:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2931, 2811, '0,2811', '支付渠道配置', 'M', 'PayChannelConfig', 'pay-channel-config', 'aioveuMallPayChannelConfig/pay-channel-config/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 17:31:04', '2026-03-13 17:31:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2932, 2931, '0,2811,2931', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:31:16', '2026-03-13 17:31:16', NULL, 2);
INSERT INTO `sys_menu` VALUES (2933, 2931, '0,2811,2931', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:31:27', '2026-03-13 17:31:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2934, 2931, '0,2811,2931', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:31:40', '2026-03-13 17:31:40', NULL, 2);
INSERT INTO `sys_menu` VALUES (2935, 2931, '0,2811,2931', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:31:51', '2026-03-13 17:31:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2936, 2811, '0,2811', '支付对账', 'M', 'PayReconciliation', 'pay-reconciliation', 'aioveuMallPayReconciliation/pay-reconciliation/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 17:32:16', '2026-03-13 17:32:16', NULL, 2);
INSERT INTO `sys_menu` VALUES (2937, 2936, '0,2811,2936', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:32:28', '2026-03-13 17:32:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2938, 2936, '0,2811,2936', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:32:42', '2026-03-13 17:32:42', NULL, 2);
INSERT INTO `sys_menu` VALUES (2939, 2936, '0,2811,2936', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:32:53', '2026-03-13 17:32:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2940, 2936, '0,2811,2936', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:33:05', '2026-03-13 17:33:05', NULL, 2);
INSERT INTO `sys_menu` VALUES (2941, 2812, '0,2812', '订单退款申请', 'M', 'RefundOrder', 'refund-order', 'aioveuMallRefundOrder/refund-order/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:33:40', '2026-03-13 17:33:40', NULL, 2);
INSERT INTO `sys_menu` VALUES (2942, 2941, '0,2812,2941', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:33:53', '2026-03-13 17:33:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2943, 2941, '0,2812,2941', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:34:04', '2026-03-13 17:34:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2944, 2941, '0,2812,2941', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:34:18', '2026-03-13 17:34:18', NULL, 2);
INSERT INTO `sys_menu` VALUES (2945, 2941, '0,2812,2941', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:34:32', '2026-03-13 17:34:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2946, 2812, '0,2812', '退款商品明细', 'M', 'RefundItem', 'refund-item', 'aioveuMallRefundItem/refund-item/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:34:58', '2026-03-13 17:34:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (2947, 2946, '0,2812,2946', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:35:09', '2026-03-13 17:35:09', NULL, 2);
INSERT INTO `sys_menu` VALUES (2948, 2946, '0,2812,2946', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:35:20', '2026-03-13 17:35:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2949, 2946, '0,2812,2946', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:35:31', '2026-03-13 17:35:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2950, 2946, '0,2812,2946', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:35:45', '2026-03-13 17:35:45', NULL, 2);
INSERT INTO `sys_menu` VALUES (2951, 2812, '0,2812', '退款物流信息（用于退货）', 'M', 'RefundDelivery', 'refund-delivery', 'aioveuMallRefundDelivery/refund-delivery/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 17:36:08', '2026-03-13 17:36:08', NULL, 2);
INSERT INTO `sys_menu` VALUES (2952, 2951, '0,2812,2951', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:36:20', '2026-03-13 17:36:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2953, 2951, '0,2812,2951', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:36:31', '2026-03-13 17:36:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2954, 2951, '0,2812,2951', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:36:43', '2026-03-13 17:36:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2955, 2951, '0,2812,2951', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:36:54', '2026-03-13 17:36:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2956, 2812, '0,2812', '退款操作记录（用于审计）', 'M', 'RefundOperationLog', 'refund-operation-log', 'aioveuMallRefundOperationLog/refund-operation-log/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 17:40:53', '2026-03-13 17:40:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2957, 2956, '0,2812,2956', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:41:39', '2026-03-13 17:41:39', NULL, 2);
INSERT INTO `sys_menu` VALUES (2958, 2956, '0,2812,2956', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:41:50', '2026-03-13 17:41:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2959, 2956, '0,2812,2956', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:42:08', '2026-03-13 17:42:08', NULL, 2);
INSERT INTO `sys_menu` VALUES (2960, 2956, '0,2812,2956', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:42:21', '2026-03-13 17:42:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2961, 2812, '0,2812', '退款凭证图片', 'M', 'RefundProof', 'refund-proof', 'aioveuMallRefundProof/refund-proof/index', NULL, 1, 1, 1, 5, 'menu', NULL, '2026-03-13 17:42:50', '2026-03-13 17:42:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2962, 2961, '0,2812,2961', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:43:04', '2026-03-13 17:43:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2963, 2961, '0,2812,2961', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:43:17', '2026-03-13 17:43:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2964, 2961, '0,2812,2961', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:43:32', '2026-03-13 17:43:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2965, 2961, '0,2812,2961', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:43:44', '2026-03-13 17:43:44', NULL, 2);
INSERT INTO `sys_menu` VALUES (2966, 2812, '0,2812', '退款支付记录', 'M', 'RefundPayment', 'refund-payment', 'aioveuMallRefundPayment/refund-payment/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 17:44:11', '2026-03-13 17:44:11', NULL, 2);
INSERT INTO `sys_menu` VALUES (2967, 2966, '0,2812,2966', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:44:22', '2026-03-13 17:44:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (2968, 2966, '0,2812,2966', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:44:35', '2026-03-13 17:44:35', NULL, 2);
INSERT INTO `sys_menu` VALUES (2969, 2966, '0,2812,2966', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:44:49', '2026-03-13 17:44:49', NULL, 2);
INSERT INTO `sys_menu` VALUES (2970, 2966, '0,2812,2966', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:45:00', '2026-03-13 17:45:00', NULL, 2);
INSERT INTO `sys_menu` VALUES (2971, 2812, '0,2812', '退款原因分类', 'M', 'RefundReason', 'refund-reason', 'aioveuMallRefundReason/refund-reason/index', NULL, 1, 1, 1, 7, 'menu', NULL, '2026-03-13 17:45:24', '2026-03-13 17:45:24', NULL, 2);
INSERT INTO `sys_menu` VALUES (2972, 2971, '0,2812,2971', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:45:41', '2026-03-13 17:45:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2973, 2971, '0,2812,2971', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:45:56', '2026-03-13 17:45:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2974, 2971, '0,2812,2971', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:46:07', '2026-03-13 17:46:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2975, 2971, '0,2812,2971', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:46:19', '2026-03-13 17:46:19', NULL, 2);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  `title` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '通知标题',
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '通知内容',
  `type` tinyint NOT NULL COMMENT '通知类型（关联字典编码：notice_type）',
  `level` varchar(5) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知等级（字典code：notice_level）',
  `target_type` tinyint NOT NULL COMMENT '目标类型（1: 全体, 2: 指定）',
  `target_user_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '目标人ID集合（多个使用英文逗号,分割）',
  `publisher_id` bigint NULL DEFAULT NULL COMMENT '发布人ID',
  `publish_status` tinyint NULL DEFAULT 0 COMMENT '发布状态（0: 未发布, 1: 已发布, -1: 已撤回）',
  `publish_time` datetime NULL DEFAULT NULL COMMENT '发布时间',
  `revoke_time` datetime NULL DEFAULT NULL COMMENT '撤回时间',
  `create_by` bigint NOT NULL COMMENT '创建人ID',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '是否删除（0: 未删除, 1: 已删除）',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, 0, 'v3.0.0 版本发布 - 多租户功能上线', '<p>🎉 新版本发布，主要更新内容：</p><p>1. 新增多租户功能，支持租户隔离和数据管理</p><p>2. 优化系统性能，提升响应速度</p><p>3. 完善权限管理，增强安全性</p><p>4. 修复已知问题，提升系统稳定性</p>', 1, 'H', 1, NULL, 1, 1, '2024-12-15 10:00:00', NULL, 1, '2024-12-15 10:00:00', 1, '2024-12-15 10:00:00', 0);
INSERT INTO `sys_notice` VALUES (2, 0, '系统维护通知 - 2024年12月20日', '<p>⏰ 系统维护通知</p><p>系统将于 <strong>2024年12月20日（本周五）凌晨 2:00-4:00</strong> 进行例行维护升级。</p><p>维护期间系统将暂停服务，请提前做好数据备份工作。</p><p>给您带来的不便，敬请谅解！</p>', 2, 'H', 1, NULL, 1, 1, '2024-12-18 14:30:00', NULL, 1, '2024-12-18 14:30:00', 1, '2024-12-18 14:30:00', 0);
INSERT INTO `sys_notice` VALUES (3, 0, '安全提醒 - 防范钓鱼邮件', '<p>⚠️ 安全提醒</p><p>近期发现有不法分子通过钓鱼邮件进行网络攻击，请大家提高警惕：</p><p>1. 不要点击来源不明的邮件链接</p><p>2. 不要下载可疑附件</p><p>3. 遇到可疑邮件请及时联系IT部门</p><p>4. 定期修改密码，使用强密码策略</p>', 3, 'H', 1, NULL, 1, 1, '2024-12-10 09:00:00', NULL, 1, '2024-12-10 09:00:00', 1, '2024-12-10 09:00:00', 0);
INSERT INTO `sys_notice` VALUES (4, 0, '元旦假期安排通知', '<p>📅 元旦假期安排</p><p>根据国家法定节假日安排，公司元旦假期时间为：</p><p><strong>2024年12月30日（周一）至 2025年1月1日（周三）</strong>，共3天。</p><p>2024年12月29日（周日）正常上班。</p><p>祝大家元旦快乐，假期愉快！</p>', 4, 'M', 1, NULL, 1, 1, '2024-12-25 16:00:00', NULL, 1, '2024-12-25 16:00:00', 1, '2024-12-25 16:00:00', 0);
INSERT INTO `sys_notice` VALUES (5, 0, '新产品发布会邀请', '<p>🎊 新产品发布会邀请</p><p>公司将于 <strong>2025年1月15日下午14:00</strong> 在总部会议室举办新产品发布会。</p><p>届时将展示最新研发的产品和技术成果，欢迎全体员工参加。</p><p>请各部门提前安排好工作，准时参加。</p>', 5, 'M', 1, NULL, 1, 1, '2024-12-28 11:00:00', NULL, 1, '2024-12-28 11:00:00', 1, '2024-12-28 11:00:00', 0);
INSERT INTO `sys_notice` VALUES (6, 0, 'v2.16.1 版本更新', '<p>✨ 版本更新</p><p>v2.16.1 版本已发布，主要修复内容：</p><p>1. 修复 WebSocket 重复连接导致的后台线程阻塞问题</p><p>2. 优化通知公告功能，提升用户体验</p><p>3. 修复部分已知bug</p><p>建议尽快更新到最新版本。</p>', 1, 'M', 1, NULL, 1, 1, '2024-12-05 15:30:00', NULL, 1, '2024-12-05 15:30:00', 1, '2024-12-05 15:30:00', 0);
INSERT INTO `sys_notice` VALUES (7, 0, '年终总结会议通知', '<p>📋 年终总结会议通知</p><p>各部门年终总结会议将于 <strong>2024年12月30日上午9:00</strong> 召开。</p><p>请各部门负责人提前准备好年度工作总结和下年度工作计划。</p><p>会议地点：总部大会议室</p>', 5, 'M', 2, '1,2', 1, 1, '2024-12-22 10:00:00', NULL, 1, '2024-12-22 10:00:00', 1, '2024-12-22 10:00:00', 0);
INSERT INTO `sys_notice` VALUES (8, 0, '系统功能优化完成', '<p>✅ 系统功能优化</p><p>已完成以下功能优化：</p><p>1. 优化用户管理界面，提升操作体验</p><p>2. 增强数据导出功能，支持更多格式</p><p>3. 优化搜索功能，提升查询效率</p><p>4. 修复部分界面显示问题</p>', 1, 'L', 1, NULL, 1, 1, '2024-12-12 14:20:00', NULL, 1, '2024-12-12 14:20:00', 1, '2024-12-12 14:20:00', 0);
INSERT INTO `sys_notice` VALUES (9, 0, '员工培训计划', '<p>📚 员工培训计划</p><p>为提升员工专业技能，公司将于 <strong>2025年1月8日-10日</strong> 组织技术培训。</p><p>培训内容：</p><p>1. 新技术框架应用</p><p>2. 代码规范与最佳实践</p><p>3. 系统架构设计</p><p>请各部门合理安排工作，确保培训顺利进行。</p>', 5, 'M', 1, NULL, 1, 1, '2024-12-20 09:30:00', NULL, 1, '2024-12-20 09:30:00', 1, '2024-12-20 09:30:00', 0);
INSERT INTO `sys_notice` VALUES (10, 0, '数据备份提醒', '<p>💾 数据备份提醒</p><p>请各部门注意定期备份重要数据，建议每周至少备份一次。</p><p>备份方式：</p><p>1. 使用系统自带备份功能</p><p>2. 手动导出重要数据</p><p>3. 联系IT部门协助备份</p><p>数据安全，人人有责！</p>', 3, 'L', 1, NULL, 1, 1, '2024-12-08 08:00:00', NULL, 1, '2024-12-08 08:00:00', 1, '2024-12-08 08:00:00', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '角色状态(1-正常 0-停用)',
  `data_scope` tinyint NULL DEFAULT NULL COMMENT '数据权限(1-所有数据 2-部门及子部门数据 3-本部门数据 4-本人数据 5-自定义部门数据)',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人 ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_name`(`tenant_id` ASC, `name` ASC, `is_deleted` ASC) USING BTREE COMMENT '租户内角色名称唯一索引',
  UNIQUE INDEX `uk_tenant_code`(`tenant_id` ASC, `code` ASC, `is_deleted` ASC) USING BTREE COMMENT '租户内角色编码唯一索引',
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 16 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, 0, '超级管理员', 'ROOT', 1, 1, 1, NULL, '2026-02-20 18:09:02', NULL, '2026-02-20 18:09:02', 0);
INSERT INTO `sys_role` VALUES (2, 0, '系统管理员', 'ADMIN', 2, 1, 1, NULL, '2026-02-20 18:09:02', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (3, 0, '访问游客', 'GUEST', 3, 1, 3, NULL, '2026-02-20 18:09:02', NULL, '2026-02-20 18:09:02', 0);
INSERT INTO `sys_role` VALUES (4, 0, '部门主管', 'DEPT_MANAGER', 4, 1, 2, NULL, '2026-02-20 18:09:03', NULL, '2026-02-20 18:09:03', 0);
INSERT INTO `sys_role` VALUES (5, 0, '部门成员', 'DEPT_MEMBER', 5, 1, 3, NULL, '2026-02-20 18:09:03', NULL, '2026-02-20 18:09:03', 0);
INSERT INTO `sys_role` VALUES (6, 0, '普通员工', 'EMPLOYEE', 6, 1, 4, NULL, '2026-02-20 18:09:03', NULL, '2026-02-20 18:09:03', 0);
INSERT INTO `sys_role` VALUES (7, 0, '自定义权限用户', 'CUSTOM_USER', 7, 1, 5, NULL, '2026-02-20 18:09:03', NULL, '2026-02-20 18:09:03', 0);
INSERT INTO `sys_role` VALUES (13, 1, '演示租户管理员', 'DEMO_ADMIN', 1, 1, 1, NULL, '2026-02-20 18:09:03', NULL, '2026-02-20 18:09:03', 0);
INSERT INTO `sys_role` VALUES (14, 1, '演示普通用户', 'DEMO_USER', 2, 1, 3, NULL, '2026-02-20 18:09:03', NULL, '2026-02-20 18:09:03', 0);
INSERT INTO `sys_role` VALUES (15, 1, '演示租户系统管理员', 'ADMIN', 3, 1, 1, NULL, '2026-02-20 18:09:03', NULL, '2026-02-20 18:09:03', 0);

-- ----------------------------
-- Table structure for sys_role_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`  (
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `dept_id` bigint NOT NULL COMMENT '部门ID',
  UNIQUE INDEX `uk_tenant_roleid_deptid`(`tenant_id` ASC, `role_id` ASC, `dept_id` ASC) USING BTREE COMMENT '租户角色部门唯一索引',
  INDEX `idx_role_dept_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_role_dept`(`tenant_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色部门关联表(用于自定义数据权限)' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_dept
-- ----------------------------
INSERT INTO `sys_role_dept` VALUES (0, 7, 1);
INSERT INTO `sys_role_dept` VALUES (0, 7, 2);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  UNIQUE INDEX `uk_roleid_menuid`(`role_id` ASC, `menu_id` ASC) USING BTREE COMMENT '角色菜单唯一索引',
  INDEX `idx_role_menu_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_tenant_role`(`tenant_id` ASC, `role_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (1, 1, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2, 0);
INSERT INTO `sys_role_menu` VALUES (1, 3, 0);
INSERT INTO `sys_role_menu` VALUES (1, 5, 0);
INSERT INTO `sys_role_menu` VALUES (1, 6, 0);
INSERT INTO `sys_role_menu` VALUES (1, 7, 0);
INSERT INTO `sys_role_menu` VALUES (1, 8, 0);
INSERT INTO `sys_role_menu` VALUES (1, 9, 0);
INSERT INTO `sys_role_menu` VALUES (1, 10, 0);
INSERT INTO `sys_role_menu` VALUES (1, 110, 0);
INSERT INTO `sys_role_menu` VALUES (1, 120, 0);
INSERT INTO `sys_role_menu` VALUES (1, 210, 0);
INSERT INTO `sys_role_menu` VALUES (1, 220, 0);
INSERT INTO `sys_role_menu` VALUES (1, 230, 0);
INSERT INTO `sys_role_menu` VALUES (1, 240, 0);
INSERT INTO `sys_role_menu` VALUES (1, 250, 0);
INSERT INTO `sys_role_menu` VALUES (1, 251, 0);
INSERT INTO `sys_role_menu` VALUES (1, 260, 0);
INSERT INTO `sys_role_menu` VALUES (1, 270, 0);
INSERT INTO `sys_role_menu` VALUES (1, 280, 0);
INSERT INTO `sys_role_menu` VALUES (1, 310, 0);
INSERT INTO `sys_role_menu` VALUES (1, 501, 0);
INSERT INTO `sys_role_menu` VALUES (1, 502, 0);
INSERT INTO `sys_role_menu` VALUES (1, 503, 0);
INSERT INTO `sys_role_menu` VALUES (1, 504, 0);
INSERT INTO `sys_role_menu` VALUES (1, 601, 0);
INSERT INTO `sys_role_menu` VALUES (1, 701, 0);
INSERT INTO `sys_role_menu` VALUES (1, 702, 0);
INSERT INTO `sys_role_menu` VALUES (1, 703, 0);
INSERT INTO `sys_role_menu` VALUES (1, 704, 0);
INSERT INTO `sys_role_menu` VALUES (1, 705, 0);
INSERT INTO `sys_role_menu` VALUES (1, 706, 0);
INSERT INTO `sys_role_menu` VALUES (1, 707, 0);
INSERT INTO `sys_role_menu` VALUES (1, 708, 0);
INSERT INTO `sys_role_menu` VALUES (1, 709, 0);
INSERT INTO `sys_role_menu` VALUES (1, 801, 0);
INSERT INTO `sys_role_menu` VALUES (1, 802, 0);
INSERT INTO `sys_role_menu` VALUES (1, 803, 0);
INSERT INTO `sys_role_menu` VALUES (1, 804, 0);
INSERT INTO `sys_role_menu` VALUES (1, 805, 0);
INSERT INTO `sys_role_menu` VALUES (1, 910, 0);
INSERT INTO `sys_role_menu` VALUES (1, 911, 0);
INSERT INTO `sys_role_menu` VALUES (1, 912, 0);
INSERT INTO `sys_role_menu` VALUES (1, 913, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1001, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1002, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1101, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1102, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1103, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1104, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1105, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1106, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1107, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1201, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1202, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1203, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1204, 0);
INSERT INTO `sys_role_menu` VALUES (1, 1205, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2101, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2102, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2103, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2104, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2105, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2106, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2107, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2201, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2202, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2203, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2204, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2205, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2301, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2302, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2303, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2304, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2401, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2402, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2403, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2404, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2501, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2502, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2503, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2504, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2511, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2512, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2513, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2514, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2701, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2702, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2703, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2704, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2705, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2801, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2802, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2803, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2804, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2805, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2806, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2807, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2808, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2809, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2810, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2811, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2812, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2813, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2814, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2816, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2817, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2818, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2819, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2820, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2821, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2822, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2823, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2824, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2825, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2826, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2827, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2828, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2829, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2830, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2831, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2832, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2833, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2834, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2835, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2836, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2837, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2838, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2839, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2840, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2841, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2842, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2843, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2844, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2845, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2846, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2847, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2848, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2849, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2850, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2851, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2852, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2853, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2854, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2855, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2856, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2857, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2858, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2859, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2860, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2861, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2862, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2863, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2864, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2865, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2866, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2867, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2868, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2869, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2870, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2871, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2872, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2873, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2874, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2875, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2876, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2877, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2878, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2879, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2880, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2881, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2882, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2883, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2884, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2885, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2886, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2887, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2888, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2889, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2890, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2891, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2892, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2893, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2894, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2895, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2896, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2897, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2898, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2899, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2900, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2901, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2902, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2903, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2904, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2905, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2906, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2907, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2908, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2909, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2910, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2911, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2912, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2913, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2914, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2915, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2916, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2917, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2918, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2919, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2920, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2921, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2922, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2923, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2924, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2925, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2926, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2927, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2928, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2929, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2930, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2931, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2932, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2933, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2934, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2935, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2936, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2937, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2938, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2939, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2940, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2941, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2942, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2943, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2944, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2945, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2946, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2947, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2948, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2949, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2950, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2951, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2952, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2953, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2954, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2955, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2956, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2957, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2958, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2959, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2960, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2961, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2962, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2963, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2964, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2965, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2966, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2967, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2968, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2969, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2970, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2971, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2972, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2973, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2974, 0);
INSERT INTO `sys_role_menu` VALUES (1, 2975, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2, 0);
INSERT INTO `sys_role_menu` VALUES (2, 3, 0);
INSERT INTO `sys_role_menu` VALUES (2, 5, 0);
INSERT INTO `sys_role_menu` VALUES (2, 6, 0);
INSERT INTO `sys_role_menu` VALUES (2, 7, 0);
INSERT INTO `sys_role_menu` VALUES (2, 8, 0);
INSERT INTO `sys_role_menu` VALUES (2, 9, 0);
INSERT INTO `sys_role_menu` VALUES (2, 10, 0);
INSERT INTO `sys_role_menu` VALUES (2, 110, 0);
INSERT INTO `sys_role_menu` VALUES (2, 120, 0);
INSERT INTO `sys_role_menu` VALUES (2, 210, 0);
INSERT INTO `sys_role_menu` VALUES (2, 220, 0);
INSERT INTO `sys_role_menu` VALUES (2, 230, 0);
INSERT INTO `sys_role_menu` VALUES (2, 240, 0);
INSERT INTO `sys_role_menu` VALUES (2, 250, 0);
INSERT INTO `sys_role_menu` VALUES (2, 251, 0);
INSERT INTO `sys_role_menu` VALUES (2, 260, 0);
INSERT INTO `sys_role_menu` VALUES (2, 270, 0);
INSERT INTO `sys_role_menu` VALUES (2, 280, 0);
INSERT INTO `sys_role_menu` VALUES (2, 310, 0);
INSERT INTO `sys_role_menu` VALUES (2, 501, 0);
INSERT INTO `sys_role_menu` VALUES (2, 502, 0);
INSERT INTO `sys_role_menu` VALUES (2, 503, 0);
INSERT INTO `sys_role_menu` VALUES (2, 504, 0);
INSERT INTO `sys_role_menu` VALUES (2, 601, 0);
INSERT INTO `sys_role_menu` VALUES (2, 701, 0);
INSERT INTO `sys_role_menu` VALUES (2, 702, 0);
INSERT INTO `sys_role_menu` VALUES (2, 703, 0);
INSERT INTO `sys_role_menu` VALUES (2, 704, 0);
INSERT INTO `sys_role_menu` VALUES (2, 705, 0);
INSERT INTO `sys_role_menu` VALUES (2, 706, 0);
INSERT INTO `sys_role_menu` VALUES (2, 707, 0);
INSERT INTO `sys_role_menu` VALUES (2, 708, 0);
INSERT INTO `sys_role_menu` VALUES (2, 709, 0);
INSERT INTO `sys_role_menu` VALUES (2, 801, 0);
INSERT INTO `sys_role_menu` VALUES (2, 802, 0);
INSERT INTO `sys_role_menu` VALUES (2, 803, 0);
INSERT INTO `sys_role_menu` VALUES (2, 804, 0);
INSERT INTO `sys_role_menu` VALUES (2, 805, 0);
INSERT INTO `sys_role_menu` VALUES (2, 910, 0);
INSERT INTO `sys_role_menu` VALUES (2, 911, 0);
INSERT INTO `sys_role_menu` VALUES (2, 912, 0);
INSERT INTO `sys_role_menu` VALUES (2, 913, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1001, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1002, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1101, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1102, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1103, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1104, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1105, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1106, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1107, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1108, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1201, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1202, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1203, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1204, 0);
INSERT INTO `sys_role_menu` VALUES (2, 1205, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2101, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2102, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2103, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2104, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2105, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2106, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2107, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2201, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2202, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2203, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2204, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2205, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2301, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2302, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2303, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2304, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2401, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2402, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2403, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2404, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2501, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2502, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2503, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2504, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2511, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2512, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2513, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2514, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2701, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2702, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2703, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2704, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2705, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2801, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2802, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2803, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2804, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2805, 0);
INSERT INTO `sys_role_menu` VALUES (2, 2806, 0);
INSERT INTO `sys_role_menu` VALUES (4, 2, 0);
INSERT INTO `sys_role_menu` VALUES (4, 210, 0);
INSERT INTO `sys_role_menu` VALUES (4, 220, 0);
INSERT INTO `sys_role_menu` VALUES (4, 240, 0);
INSERT INTO `sys_role_menu` VALUES (4, 250, 0);
INSERT INTO `sys_role_menu` VALUES (4, 260, 0);
INSERT INTO `sys_role_menu` VALUES (4, 280, 0);
INSERT INTO `sys_role_menu` VALUES (4, 2101, 0);
INSERT INTO `sys_role_menu` VALUES (4, 2201, 0);
INSERT INTO `sys_role_menu` VALUES (4, 2401, 0);
INSERT INTO `sys_role_menu` VALUES (4, 2501, 0);
INSERT INTO `sys_role_menu` VALUES (4, 2801, 0);
INSERT INTO `sys_role_menu` VALUES (5, 2, 0);
INSERT INTO `sys_role_menu` VALUES (5, 210, 0);
INSERT INTO `sys_role_menu` VALUES (5, 220, 0);
INSERT INTO `sys_role_menu` VALUES (5, 240, 0);
INSERT INTO `sys_role_menu` VALUES (5, 250, 0);
INSERT INTO `sys_role_menu` VALUES (5, 260, 0);
INSERT INTO `sys_role_menu` VALUES (5, 280, 0);
INSERT INTO `sys_role_menu` VALUES (5, 2101, 0);
INSERT INTO `sys_role_menu` VALUES (5, 2201, 0);
INSERT INTO `sys_role_menu` VALUES (5, 2401, 0);
INSERT INTO `sys_role_menu` VALUES (5, 2501, 0);
INSERT INTO `sys_role_menu` VALUES (5, 2801, 0);
INSERT INTO `sys_role_menu` VALUES (6, 2, 0);
INSERT INTO `sys_role_menu` VALUES (6, 210, 0);
INSERT INTO `sys_role_menu` VALUES (6, 220, 0);
INSERT INTO `sys_role_menu` VALUES (6, 240, 0);
INSERT INTO `sys_role_menu` VALUES (6, 250, 0);
INSERT INTO `sys_role_menu` VALUES (6, 260, 0);
INSERT INTO `sys_role_menu` VALUES (6, 280, 0);
INSERT INTO `sys_role_menu` VALUES (6, 2101, 0);
INSERT INTO `sys_role_menu` VALUES (6, 2201, 0);
INSERT INTO `sys_role_menu` VALUES (6, 2401, 0);
INSERT INTO `sys_role_menu` VALUES (6, 2501, 0);
INSERT INTO `sys_role_menu` VALUES (6, 2801, 0);
INSERT INTO `sys_role_menu` VALUES (7, 2, 0);
INSERT INTO `sys_role_menu` VALUES (7, 210, 0);
INSERT INTO `sys_role_menu` VALUES (7, 220, 0);
INSERT INTO `sys_role_menu` VALUES (7, 240, 0);
INSERT INTO `sys_role_menu` VALUES (7, 250, 0);
INSERT INTO `sys_role_menu` VALUES (7, 260, 0);
INSERT INTO `sys_role_menu` VALUES (7, 280, 0);
INSERT INTO `sys_role_menu` VALUES (7, 2101, 0);
INSERT INTO `sys_role_menu` VALUES (7, 2201, 0);
INSERT INTO `sys_role_menu` VALUES (7, 2401, 0);
INSERT INTO `sys_role_menu` VALUES (7, 2501, 0);
INSERT INTO `sys_role_menu` VALUES (7, 2801, 0);
INSERT INTO `sys_role_menu` VALUES (13, 2, 1);
INSERT INTO `sys_role_menu` VALUES (13, 3, 1);
INSERT INTO `sys_role_menu` VALUES (13, 5, 1);
INSERT INTO `sys_role_menu` VALUES (13, 6, 1);
INSERT INTO `sys_role_menu` VALUES (13, 7, 1);
INSERT INTO `sys_role_menu` VALUES (13, 8, 1);
INSERT INTO `sys_role_menu` VALUES (13, 9, 1);
INSERT INTO `sys_role_menu` VALUES (13, 10, 1);
INSERT INTO `sys_role_menu` VALUES (13, 210, 1);
INSERT INTO `sys_role_menu` VALUES (13, 220, 1);
INSERT INTO `sys_role_menu` VALUES (13, 240, 1);
INSERT INTO `sys_role_menu` VALUES (13, 250, 1);
INSERT INTO `sys_role_menu` VALUES (13, 251, 1);
INSERT INTO `sys_role_menu` VALUES (13, 260, 1);
INSERT INTO `sys_role_menu` VALUES (13, 280, 1);
INSERT INTO `sys_role_menu` VALUES (13, 310, 1);
INSERT INTO `sys_role_menu` VALUES (13, 501, 1);
INSERT INTO `sys_role_menu` VALUES (13, 502, 1);
INSERT INTO `sys_role_menu` VALUES (13, 503, 1);
INSERT INTO `sys_role_menu` VALUES (13, 504, 1);
INSERT INTO `sys_role_menu` VALUES (13, 601, 1);
INSERT INTO `sys_role_menu` VALUES (13, 701, 1);
INSERT INTO `sys_role_menu` VALUES (13, 702, 1);
INSERT INTO `sys_role_menu` VALUES (13, 703, 1);
INSERT INTO `sys_role_menu` VALUES (13, 704, 1);
INSERT INTO `sys_role_menu` VALUES (13, 705, 1);
INSERT INTO `sys_role_menu` VALUES (13, 706, 1);
INSERT INTO `sys_role_menu` VALUES (13, 707, 1);
INSERT INTO `sys_role_menu` VALUES (13, 708, 1);
INSERT INTO `sys_role_menu` VALUES (13, 709, 1);
INSERT INTO `sys_role_menu` VALUES (13, 801, 1);
INSERT INTO `sys_role_menu` VALUES (13, 802, 1);
INSERT INTO `sys_role_menu` VALUES (13, 803, 1);
INSERT INTO `sys_role_menu` VALUES (13, 804, 1);
INSERT INTO `sys_role_menu` VALUES (13, 805, 1);
INSERT INTO `sys_role_menu` VALUES (13, 910, 1);
INSERT INTO `sys_role_menu` VALUES (13, 911, 1);
INSERT INTO `sys_role_menu` VALUES (13, 912, 1);
INSERT INTO `sys_role_menu` VALUES (13, 913, 1);
INSERT INTO `sys_role_menu` VALUES (13, 1001, 1);
INSERT INTO `sys_role_menu` VALUES (13, 1002, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2101, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2102, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2103, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2104, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2105, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2106, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2107, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2201, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2202, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2203, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2204, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2205, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2401, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2402, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2403, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2404, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2501, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2502, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2503, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2504, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2511, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2512, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2513, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2514, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2801, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2802, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2803, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2804, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2805, 1);
INSERT INTO `sys_role_menu` VALUES (13, 2806, 1);
INSERT INTO `sys_role_menu` VALUES (14, 2, 1);
INSERT INTO `sys_role_menu` VALUES (14, 210, 1);
INSERT INTO `sys_role_menu` VALUES (14, 220, 1);
INSERT INTO `sys_role_menu` VALUES (14, 240, 1);
INSERT INTO `sys_role_menu` VALUES (14, 250, 1);
INSERT INTO `sys_role_menu` VALUES (14, 260, 1);
INSERT INTO `sys_role_menu` VALUES (14, 280, 1);
INSERT INTO `sys_role_menu` VALUES (14, 2101, 1);
INSERT INTO `sys_role_menu` VALUES (14, 2201, 1);
INSERT INTO `sys_role_menu` VALUES (14, 2401, 1);
INSERT INTO `sys_role_menu` VALUES (14, 2501, 1);
INSERT INTO `sys_role_menu` VALUES (14, 2801, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2, 1);
INSERT INTO `sys_role_menu` VALUES (15, 3, 1);
INSERT INTO `sys_role_menu` VALUES (15, 5, 1);
INSERT INTO `sys_role_menu` VALUES (15, 6, 1);
INSERT INTO `sys_role_menu` VALUES (15, 7, 1);
INSERT INTO `sys_role_menu` VALUES (15, 8, 1);
INSERT INTO `sys_role_menu` VALUES (15, 9, 1);
INSERT INTO `sys_role_menu` VALUES (15, 10, 1);
INSERT INTO `sys_role_menu` VALUES (15, 210, 1);
INSERT INTO `sys_role_menu` VALUES (15, 220, 1);
INSERT INTO `sys_role_menu` VALUES (15, 240, 1);
INSERT INTO `sys_role_menu` VALUES (15, 250, 1);
INSERT INTO `sys_role_menu` VALUES (15, 251, 1);
INSERT INTO `sys_role_menu` VALUES (15, 260, 1);
INSERT INTO `sys_role_menu` VALUES (15, 280, 1);
INSERT INTO `sys_role_menu` VALUES (15, 310, 1);
INSERT INTO `sys_role_menu` VALUES (15, 501, 1);
INSERT INTO `sys_role_menu` VALUES (15, 502, 1);
INSERT INTO `sys_role_menu` VALUES (15, 503, 1);
INSERT INTO `sys_role_menu` VALUES (15, 504, 1);
INSERT INTO `sys_role_menu` VALUES (15, 601, 1);
INSERT INTO `sys_role_menu` VALUES (15, 701, 1);
INSERT INTO `sys_role_menu` VALUES (15, 702, 1);
INSERT INTO `sys_role_menu` VALUES (15, 703, 1);
INSERT INTO `sys_role_menu` VALUES (15, 704, 1);
INSERT INTO `sys_role_menu` VALUES (15, 705, 1);
INSERT INTO `sys_role_menu` VALUES (15, 706, 1);
INSERT INTO `sys_role_menu` VALUES (15, 707, 1);
INSERT INTO `sys_role_menu` VALUES (15, 708, 1);
INSERT INTO `sys_role_menu` VALUES (15, 709, 1);
INSERT INTO `sys_role_menu` VALUES (15, 801, 1);
INSERT INTO `sys_role_menu` VALUES (15, 802, 1);
INSERT INTO `sys_role_menu` VALUES (15, 803, 1);
INSERT INTO `sys_role_menu` VALUES (15, 804, 1);
INSERT INTO `sys_role_menu` VALUES (15, 805, 1);
INSERT INTO `sys_role_menu` VALUES (15, 910, 1);
INSERT INTO `sys_role_menu` VALUES (15, 911, 1);
INSERT INTO `sys_role_menu` VALUES (15, 912, 1);
INSERT INTO `sys_role_menu` VALUES (15, 913, 1);
INSERT INTO `sys_role_menu` VALUES (15, 1001, 1);
INSERT INTO `sys_role_menu` VALUES (15, 1002, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2101, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2102, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2103, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2104, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2105, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2106, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2107, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2201, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2202, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2203, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2204, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2205, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2401, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2402, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2403, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2404, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2501, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2502, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2503, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2504, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2511, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2512, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2513, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2514, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2801, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2802, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2803, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2804, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2805, 1);
INSERT INTO `sys_role_menu` VALUES (15, 2806, 1);

-- ----------------------------
-- Table structure for sys_tenant
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '租户ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户编码（唯一）',
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人姓名',
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人电话',
  `contact_email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系人邮箱',
  `domain` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户域名（用于域名识别）',
  `logo` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '租户Logo',
  `plan_id` bigint NULL DEFAULT NULL COMMENT '套餐ID',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1-正常 0-禁用)',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `expire_time` datetime NULL DEFAULT NULL COMMENT '过期时间（NULL表示永不过期）',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE,
  UNIQUE INDEX `uk_domain`(`domain` ASC) USING BTREE,
  INDEX `idx_status`(`status` ASC) USING BTREE,
  INDEX `idx_plan_id`(`plan_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统租户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant
-- ----------------------------
INSERT INTO `sys_tenant` VALUES (0, '可我不敌心动（平台租户）', 'PLATFORM', '雒世松', '13061656199', '2819850488@qq.com', 'mall.aioveu.com', NULL, NULL, 1, '可我不敌心动（平台租户）', NULL, '2026-02-20 18:09:18', '2026-03-13 15:12:58');
INSERT INTO `sys_tenant` VALUES (1, '振源超市', 'DEMO', '雒世红', '13061656199', '2819850488@qq.com', 'mall2.aioveu.com', NULL, 3, 1, '振源超市（演示租户）', NULL, '2026-02-20 18:09:18', '2026-03-13 18:02:50');

-- ----------------------------
-- Table structure for sys_tenant_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_menu`;
CREATE TABLE `sys_tenant_menu`  (
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`tenant_id`, `menu_id`) USING BTREE,
  INDEX `idx_tenant_menu_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant_menu
-- ----------------------------
INSERT INTO `sys_tenant_menu` VALUES (0, 1);
INSERT INTO `sys_tenant_menu` VALUES (0, 2);
INSERT INTO `sys_tenant_menu` VALUES (1, 2);
INSERT INTO `sys_tenant_menu` VALUES (0, 3);
INSERT INTO `sys_tenant_menu` VALUES (1, 3);
INSERT INTO `sys_tenant_menu` VALUES (0, 5);
INSERT INTO `sys_tenant_menu` VALUES (1, 5);
INSERT INTO `sys_tenant_menu` VALUES (0, 6);
INSERT INTO `sys_tenant_menu` VALUES (1, 6);
INSERT INTO `sys_tenant_menu` VALUES (0, 7);
INSERT INTO `sys_tenant_menu` VALUES (1, 7);
INSERT INTO `sys_tenant_menu` VALUES (0, 8);
INSERT INTO `sys_tenant_menu` VALUES (1, 8);
INSERT INTO `sys_tenant_menu` VALUES (0, 9);
INSERT INTO `sys_tenant_menu` VALUES (1, 9);
INSERT INTO `sys_tenant_menu` VALUES (0, 10);
INSERT INTO `sys_tenant_menu` VALUES (1, 10);
INSERT INTO `sys_tenant_menu` VALUES (0, 110);
INSERT INTO `sys_tenant_menu` VALUES (0, 120);
INSERT INTO `sys_tenant_menu` VALUES (0, 210);
INSERT INTO `sys_tenant_menu` VALUES (1, 210);
INSERT INTO `sys_tenant_menu` VALUES (0, 220);
INSERT INTO `sys_tenant_menu` VALUES (1, 220);
INSERT INTO `sys_tenant_menu` VALUES (0, 230);
INSERT INTO `sys_tenant_menu` VALUES (0, 240);
INSERT INTO `sys_tenant_menu` VALUES (1, 240);
INSERT INTO `sys_tenant_menu` VALUES (0, 250);
INSERT INTO `sys_tenant_menu` VALUES (1, 250);
INSERT INTO `sys_tenant_menu` VALUES (0, 251);
INSERT INTO `sys_tenant_menu` VALUES (1, 251);
INSERT INTO `sys_tenant_menu` VALUES (0, 260);
INSERT INTO `sys_tenant_menu` VALUES (1, 260);
INSERT INTO `sys_tenant_menu` VALUES (0, 270);
INSERT INTO `sys_tenant_menu` VALUES (0, 280);
INSERT INTO `sys_tenant_menu` VALUES (1, 280);
INSERT INTO `sys_tenant_menu` VALUES (0, 310);
INSERT INTO `sys_tenant_menu` VALUES (1, 310);
INSERT INTO `sys_tenant_menu` VALUES (0, 501);
INSERT INTO `sys_tenant_menu` VALUES (1, 501);
INSERT INTO `sys_tenant_menu` VALUES (0, 502);
INSERT INTO `sys_tenant_menu` VALUES (1, 502);
INSERT INTO `sys_tenant_menu` VALUES (0, 503);
INSERT INTO `sys_tenant_menu` VALUES (1, 503);
INSERT INTO `sys_tenant_menu` VALUES (0, 504);
INSERT INTO `sys_tenant_menu` VALUES (1, 504);
INSERT INTO `sys_tenant_menu` VALUES (0, 601);
INSERT INTO `sys_tenant_menu` VALUES (1, 601);
INSERT INTO `sys_tenant_menu` VALUES (0, 701);
INSERT INTO `sys_tenant_menu` VALUES (1, 701);
INSERT INTO `sys_tenant_menu` VALUES (0, 702);
INSERT INTO `sys_tenant_menu` VALUES (1, 702);
INSERT INTO `sys_tenant_menu` VALUES (0, 703);
INSERT INTO `sys_tenant_menu` VALUES (1, 703);
INSERT INTO `sys_tenant_menu` VALUES (0, 704);
INSERT INTO `sys_tenant_menu` VALUES (1, 704);
INSERT INTO `sys_tenant_menu` VALUES (0, 705);
INSERT INTO `sys_tenant_menu` VALUES (1, 705);
INSERT INTO `sys_tenant_menu` VALUES (0, 706);
INSERT INTO `sys_tenant_menu` VALUES (1, 706);
INSERT INTO `sys_tenant_menu` VALUES (0, 707);
INSERT INTO `sys_tenant_menu` VALUES (1, 707);
INSERT INTO `sys_tenant_menu` VALUES (0, 708);
INSERT INTO `sys_tenant_menu` VALUES (1, 708);
INSERT INTO `sys_tenant_menu` VALUES (0, 709);
INSERT INTO `sys_tenant_menu` VALUES (1, 709);
INSERT INTO `sys_tenant_menu` VALUES (0, 801);
INSERT INTO `sys_tenant_menu` VALUES (1, 801);
INSERT INTO `sys_tenant_menu` VALUES (0, 802);
INSERT INTO `sys_tenant_menu` VALUES (1, 802);
INSERT INTO `sys_tenant_menu` VALUES (0, 803);
INSERT INTO `sys_tenant_menu` VALUES (1, 803);
INSERT INTO `sys_tenant_menu` VALUES (0, 804);
INSERT INTO `sys_tenant_menu` VALUES (1, 804);
INSERT INTO `sys_tenant_menu` VALUES (0, 805);
INSERT INTO `sys_tenant_menu` VALUES (1, 805);
INSERT INTO `sys_tenant_menu` VALUES (0, 910);
INSERT INTO `sys_tenant_menu` VALUES (1, 910);
INSERT INTO `sys_tenant_menu` VALUES (0, 911);
INSERT INTO `sys_tenant_menu` VALUES (1, 911);
INSERT INTO `sys_tenant_menu` VALUES (0, 912);
INSERT INTO `sys_tenant_menu` VALUES (1, 912);
INSERT INTO `sys_tenant_menu` VALUES (0, 913);
INSERT INTO `sys_tenant_menu` VALUES (1, 913);
INSERT INTO `sys_tenant_menu` VALUES (0, 1001);
INSERT INTO `sys_tenant_menu` VALUES (0, 1002);
INSERT INTO `sys_tenant_menu` VALUES (0, 1101);
INSERT INTO `sys_tenant_menu` VALUES (0, 1102);
INSERT INTO `sys_tenant_menu` VALUES (0, 1103);
INSERT INTO `sys_tenant_menu` VALUES (0, 1104);
INSERT INTO `sys_tenant_menu` VALUES (0, 1105);
INSERT INTO `sys_tenant_menu` VALUES (0, 1106);
INSERT INTO `sys_tenant_menu` VALUES (0, 1107);
INSERT INTO `sys_tenant_menu` VALUES (0, 1201);
INSERT INTO `sys_tenant_menu` VALUES (0, 1202);
INSERT INTO `sys_tenant_menu` VALUES (0, 1203);
INSERT INTO `sys_tenant_menu` VALUES (0, 1204);
INSERT INTO `sys_tenant_menu` VALUES (0, 1205);
INSERT INTO `sys_tenant_menu` VALUES (0, 2101);
INSERT INTO `sys_tenant_menu` VALUES (1, 2101);
INSERT INTO `sys_tenant_menu` VALUES (0, 2102);
INSERT INTO `sys_tenant_menu` VALUES (1, 2102);
INSERT INTO `sys_tenant_menu` VALUES (0, 2103);
INSERT INTO `sys_tenant_menu` VALUES (1, 2103);
INSERT INTO `sys_tenant_menu` VALUES (0, 2104);
INSERT INTO `sys_tenant_menu` VALUES (1, 2104);
INSERT INTO `sys_tenant_menu` VALUES (0, 2105);
INSERT INTO `sys_tenant_menu` VALUES (1, 2105);
INSERT INTO `sys_tenant_menu` VALUES (0, 2106);
INSERT INTO `sys_tenant_menu` VALUES (1, 2106);
INSERT INTO `sys_tenant_menu` VALUES (0, 2107);
INSERT INTO `sys_tenant_menu` VALUES (1, 2107);
INSERT INTO `sys_tenant_menu` VALUES (0, 2201);
INSERT INTO `sys_tenant_menu` VALUES (1, 2201);
INSERT INTO `sys_tenant_menu` VALUES (0, 2202);
INSERT INTO `sys_tenant_menu` VALUES (1, 2202);
INSERT INTO `sys_tenant_menu` VALUES (0, 2203);
INSERT INTO `sys_tenant_menu` VALUES (1, 2203);
INSERT INTO `sys_tenant_menu` VALUES (0, 2204);
INSERT INTO `sys_tenant_menu` VALUES (1, 2204);
INSERT INTO `sys_tenant_menu` VALUES (0, 2205);
INSERT INTO `sys_tenant_menu` VALUES (1, 2205);
INSERT INTO `sys_tenant_menu` VALUES (0, 2301);
INSERT INTO `sys_tenant_menu` VALUES (0, 2302);
INSERT INTO `sys_tenant_menu` VALUES (0, 2303);
INSERT INTO `sys_tenant_menu` VALUES (0, 2304);
INSERT INTO `sys_tenant_menu` VALUES (0, 2401);
INSERT INTO `sys_tenant_menu` VALUES (1, 2401);
INSERT INTO `sys_tenant_menu` VALUES (0, 2402);
INSERT INTO `sys_tenant_menu` VALUES (1, 2402);
INSERT INTO `sys_tenant_menu` VALUES (0, 2403);
INSERT INTO `sys_tenant_menu` VALUES (1, 2403);
INSERT INTO `sys_tenant_menu` VALUES (0, 2404);
INSERT INTO `sys_tenant_menu` VALUES (1, 2404);
INSERT INTO `sys_tenant_menu` VALUES (0, 2501);
INSERT INTO `sys_tenant_menu` VALUES (1, 2501);
INSERT INTO `sys_tenant_menu` VALUES (0, 2502);
INSERT INTO `sys_tenant_menu` VALUES (1, 2502);
INSERT INTO `sys_tenant_menu` VALUES (0, 2503);
INSERT INTO `sys_tenant_menu` VALUES (1, 2503);
INSERT INTO `sys_tenant_menu` VALUES (0, 2504);
INSERT INTO `sys_tenant_menu` VALUES (1, 2504);
INSERT INTO `sys_tenant_menu` VALUES (0, 2511);
INSERT INTO `sys_tenant_menu` VALUES (1, 2511);
INSERT INTO `sys_tenant_menu` VALUES (0, 2512);
INSERT INTO `sys_tenant_menu` VALUES (1, 2512);
INSERT INTO `sys_tenant_menu` VALUES (0, 2513);
INSERT INTO `sys_tenant_menu` VALUES (1, 2513);
INSERT INTO `sys_tenant_menu` VALUES (0, 2514);
INSERT INTO `sys_tenant_menu` VALUES (1, 2514);
INSERT INTO `sys_tenant_menu` VALUES (0, 2701);
INSERT INTO `sys_tenant_menu` VALUES (0, 2702);
INSERT INTO `sys_tenant_menu` VALUES (0, 2703);
INSERT INTO `sys_tenant_menu` VALUES (0, 2704);
INSERT INTO `sys_tenant_menu` VALUES (0, 2705);
INSERT INTO `sys_tenant_menu` VALUES (0, 2801);
INSERT INTO `sys_tenant_menu` VALUES (1, 2801);
INSERT INTO `sys_tenant_menu` VALUES (0, 2802);
INSERT INTO `sys_tenant_menu` VALUES (1, 2802);
INSERT INTO `sys_tenant_menu` VALUES (0, 2803);
INSERT INTO `sys_tenant_menu` VALUES (1, 2803);
INSERT INTO `sys_tenant_menu` VALUES (0, 2804);
INSERT INTO `sys_tenant_menu` VALUES (1, 2804);
INSERT INTO `sys_tenant_menu` VALUES (0, 2805);
INSERT INTO `sys_tenant_menu` VALUES (1, 2805);
INSERT INTO `sys_tenant_menu` VALUES (0, 2806);
INSERT INTO `sys_tenant_menu` VALUES (1, 2806);

-- ----------------------------
-- Table structure for sys_tenant_plan
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_plan`;
CREATE TABLE `sys_tenant_plan`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '套餐ID',
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐名称',
  `code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '套餐编码',
  `status` tinyint NULL DEFAULT 1 COMMENT '状态(1-启用 0-停用)',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户套餐表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant_plan
-- ----------------------------
INSERT INTO `sys_tenant_plan` VALUES (1, '基础套餐', 'BASIC', 1, 1, '仅系统管理菜单', '2026-02-20 18:09:01', '2026-02-20 18:09:01');
INSERT INTO `sys_tenant_plan` VALUES (2, '高级套餐', 'PRO', 1, 2, '全部租户菜单', '2026-02-20 18:09:01', '2026-02-20 18:09:01');
INSERT INTO `sys_tenant_plan` VALUES (3, '豪华套餐', 'LUXURY', 1, 3, '包含更多功能', '2026-03-13 15:18:03', '2026-03-13 15:18:03');

-- ----------------------------
-- Table structure for sys_tenant_plan_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_plan_menu`;
CREATE TABLE `sys_tenant_plan_menu`  (
  `plan_id` bigint NOT NULL COMMENT '套餐ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  PRIMARY KEY (`plan_id`, `menu_id`) USING BTREE,
  INDEX `idx_plan_menu_menu_id`(`menu_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户套餐菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_tenant_plan_menu
-- ----------------------------
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 3);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 3);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 5);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 5);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 6);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 6);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 7);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 7);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 8);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 8);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 9);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 9);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 10);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 10);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 210);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 210);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 210);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 220);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 220);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 220);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 240);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 240);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 240);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 250);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 250);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 250);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 251);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 251);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 251);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 260);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 260);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 260);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 280);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 280);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 280);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 310);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 310);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 501);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 501);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 502);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 502);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 503);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 503);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 504);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 504);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 601);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 601);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 701);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 701);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 702);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 702);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 703);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 703);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 704);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 704);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 705);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 705);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 706);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 706);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 707);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 707);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 708);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 708);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 709);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 709);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 801);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 801);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 802);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 802);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 803);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 803);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 804);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 804);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 805);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 805);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 910);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 910);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 911);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 911);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 912);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 912);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 913);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 913);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2101);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2101);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2101);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2102);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2102);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2102);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2103);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2103);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2103);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2104);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2104);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2104);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2105);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2105);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2105);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2106);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2106);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2106);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2107);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2107);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2107);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2201);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2201);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2201);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2202);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2202);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2202);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2203);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2203);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2203);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2204);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2204);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2204);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2205);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2205);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2205);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2401);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2401);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2401);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2402);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2402);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2402);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2403);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2403);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2403);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2404);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2404);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2404);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2501);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2501);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2501);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2502);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2502);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2502);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2503);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2503);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2503);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2504);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2504);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2504);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2511);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2511);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2511);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2512);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2512);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2512);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2513);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2513);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2513);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2514);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2514);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2514);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2801);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2801);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2801);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2802);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2802);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2802);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2803);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2803);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2803);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2804);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2804);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2804);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2805);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2805);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2805);
INSERT INTO `sys_tenant_plan_menu` VALUES (1, 2806);
INSERT INTO `sys_tenant_plan_menu` VALUES (2, 2806);
INSERT INTO `sys_tenant_plan_menu` VALUES (3, 2806);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` tinyint(1) NULL DEFAULT 1 COMMENT '性别((1-男 2-女 0-保密)',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `dept_id` int NULL DEFAULT NULL COMMENT '部门ID',
  `open_id` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信openid，用于微信登录',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(1-正常 0-禁用)',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人ID',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_username_tenant`(`username` ASC, `tenant_id` ASC, `is_deleted` ASC) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE,
  INDEX `idx_openid`(`open_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统用户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 0, 'root', '平台租户超级管理员', 0, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', NULL, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260313/394a14547737403c806261d1022429c4.png', '18812345677', 1, 'youlaitech@163.com', '2026-02-20 18:09:09', NULL, '2026-02-20 18:09:09', NULL, 0);
INSERT INTO `sys_user` VALUES (2, 0, 'admin', '平台租户系统管理员', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 1, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345678', 1, 'youlaitech@163.com', '2026-02-20 18:09:09', NULL, '2026-02-20 18:09:09', NULL, 0);
INSERT INTO `sys_user` VALUES (3, 0, 'test', '平台租户测试天命人', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 3, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345679', 1, 'youlaitech@163.com', '2026-02-20 18:09:10', NULL, '2026-02-20 18:09:10', NULL, 0);
INSERT INTO `sys_user` VALUES (4, 1, 'admin', '演示租户管理员', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 4, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345680', 1, 'demo@youlai.tech', '2026-02-20 18:09:10', NULL, '2026-02-20 18:09:10', NULL, 0);
INSERT INTO `sys_user` VALUES (5, 1, 'test', '演示测试人员', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 6, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345681', 1, 'test@youlai.tech', '2026-02-20 18:09:10', NULL, '2026-02-20 18:09:10', NULL, 0);
INSERT INTO `sys_user` VALUES (6, 0, 'dept_manager', '部门主管', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 1, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345680', 1, 'manager@youlaitech.com', '2026-02-20 18:09:10', NULL, '2026-02-20 18:09:10', NULL, 0);
INSERT INTO `sys_user` VALUES (7, 0, 'dept_member', '部门成员', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 1, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345681', 1, 'member@youlaitech.com', '2026-02-20 18:09:10', NULL, '2026-02-20 18:09:10', NULL, 0);
INSERT INTO `sys_user` VALUES (8, 0, 'employee', '普通员工', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 2, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345682', 1, 'employee@youlaitech.com', '2026-02-20 18:09:10', NULL, '2026-02-20 18:09:10', NULL, 0);
INSERT INTO `sys_user` VALUES (9, 0, 'custom_user', '自定义权限用户', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 3, NULL, 'https://foruda.gitee.com/images/1723603502796844527/03cdca2a_716974.gif', '18812345683', 1, 'custom@youlaitech.com', '2026-02-20 18:09:10', NULL, '2026-02-20 18:09:10', NULL, 0);

-- ----------------------------
-- Table structure for sys_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_notice`;
CREATE TABLE `sys_user_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `notice_id` bigint NOT NULL COMMENT '公共通知id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  `is_read` bigint NULL DEFAULT 0 COMMENT '读取状态（0: 未读, 1: 已读）',
  `read_time` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除(0: 未删除, 1: 已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知公告关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_notice
-- ----------------------------
INSERT INTO `sys_user_notice` VALUES (1, 1, 2, 0, 1, NULL, '2026-02-20 18:09:16', '2026-02-20 18:09:16', 0);
INSERT INTO `sys_user_notice` VALUES (2, 2, 2, 0, 1, NULL, '2026-02-20 18:09:16', '2026-02-20 18:09:16', 0);
INSERT INTO `sys_user_notice` VALUES (3, 3, 2, 0, 1, NULL, '2026-02-20 18:09:16', '2026-02-20 18:09:16', 0);
INSERT INTO `sys_user_notice` VALUES (4, 4, 2, 0, 1, NULL, '2026-02-20 18:09:16', '2026-02-20 18:09:16', 0);
INSERT INTO `sys_user_notice` VALUES (5, 5, 2, 0, 1, NULL, '2026-02-20 18:09:17', '2026-02-20 18:09:17', 0);
INSERT INTO `sys_user_notice` VALUES (6, 6, 2, 0, 1, NULL, '2026-02-20 18:09:17', '2026-02-20 18:09:17', 0);
INSERT INTO `sys_user_notice` VALUES (7, 7, 2, 0, 1, NULL, '2026-02-20 18:09:17', '2026-02-20 18:09:17', 0);
INSERT INTO `sys_user_notice` VALUES (8, 8, 2, 0, 1, NULL, '2026-02-20 18:09:17', '2026-02-20 18:09:17', 0);
INSERT INTO `sys_user_notice` VALUES (9, 9, 2, 0, 1, NULL, '2026-02-20 18:09:17', '2026-02-20 18:09:17', 0);
INSERT INTO `sys_user_notice` VALUES (10, 10, 2, 0, 1, NULL, '2026-02-20 18:09:17', '2026-02-20 18:09:17', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '租户ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE,
  INDEX `idx_user_role_tenant_id`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1, 0);
INSERT INTO `sys_user_role` VALUES (2, 2, 0);
INSERT INTO `sys_user_role` VALUES (3, 3, 0);
INSERT INTO `sys_user_role` VALUES (6, 4, 0);
INSERT INTO `sys_user_role` VALUES (7, 5, 0);
INSERT INTO `sys_user_role` VALUES (8, 6, 0);
INSERT INTO `sys_user_role` VALUES (9, 7, 0);
INSERT INTO `sys_user_role` VALUES (4, 13, 1);
INSERT INTO `sys_user_role` VALUES (4, 15, 1);
INSERT INTO `sys_user_role` VALUES (5, 14, 1);

SET FOREIGN_KEY_CHECKS = 1;
