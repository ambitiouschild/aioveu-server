/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_pay

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 16/03/2026 12:16:12
*/

SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for manager_menu_category
-- 多租户菜单分类表 (manager_menu_category) - - 多租户菜单分类表
-- ----------------------------
DROP TABLE IF EXISTS `manager_menu_category`;
CREATE TABLE `manager_menu_category`  (
       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
       `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID，0表示平台默认',
       `title` varchar(100) NOT NULL COMMENT '分类标题',
       `icon` varchar(255) DEFAULT NULL COMMENT '分类图标',
       `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
       `sort` int(11) DEFAULT 0 COMMENT '排序序号',
       `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
       `type` varchar(50) DEFAULT 'workbench' COMMENT '分类类型：workbench-工作台，sidebar-侧边栏',
       `visible_range` tinyint(1) DEFAULT 0 COMMENT '可见范围：0-所有用户，1-租户管理员，2-普通用户',
       `is_editable` tinyint(1) DEFAULT 1 COMMENT '是否可编辑：0-系统内置，1-可编辑',
       `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
       `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
    -- 系统字段
      `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',


       PRIMARY KEY (`id`) USING BTREE,
       KEY `idx_tenant_sort` (`tenant_id`, `sort`),
       KEY `idx_type_status` (`type`, `status`),
       INDEX `idx_tenant_visible` (`tenant_id`, `visible_range`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理端菜单分类表（多租户）'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for manager_menu_category_item
-- 多租户菜单项表 (manager_menu_category_item) - 多租户菜单项表
-- ----------------------------
DROP TABLE IF EXISTS `manager_menu_category_item`;
CREATE TABLE `manager_menu_category_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint(20) DEFAULT 0 COMMENT '租户ID，0表示平台默认',
  `category_id` bigint(20) NOT NULL COMMENT '分类ID',
  `title` varchar(100) NOT NULL COMMENT '菜单标题',
  `icon` varchar(255) DEFAULT NULL COMMENT '菜单图标',
  `url` varchar(500) NOT NULL COMMENT '跳转路径',
  `path_name` varchar(100) DEFAULT NULL COMMENT '路由名称',
  `permission` varchar(200) DEFAULT NULL COMMENT '权限标识',
  `description` varchar(500) DEFAULT NULL COMMENT '菜单描述',
  `sort` int(11) DEFAULT 0 COMMENT '排序序号',
  `status` tinyint(1) DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
  `type` tinyint(1) DEFAULT 0 COMMENT '菜单类型：0-页面，1-按钮，2-链接',
  `open_type` tinyint(1) DEFAULT 0 COMMENT '打开方式：0-内部打开，1-新标签页',
  `is_visible` tinyint(1) DEFAULT 1 COMMENT '是否可见',
  `is_system` tinyint(1) DEFAULT 0 COMMENT '是否系统菜单',
  `is_editable` tinyint(1) DEFAULT 1 COMMENT '是否可编辑',

    -- 系统字段
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_tenant_category` (`tenant_id`, `category_id`, `sort`),
  KEY `idx_category_status` (`category_id`, `status`),
  KEY `idx_tenant_visible_status` (`tenant_id`, `is_visible`, `status`),
  KEY `idx_permission` (`permission`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '管理系统工作台菜单项表（多租户支持）'
    ROW_FORMAT = Dynamic;


