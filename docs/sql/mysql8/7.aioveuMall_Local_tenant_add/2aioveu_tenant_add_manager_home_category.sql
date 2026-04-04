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
-- Table structure for manager_menu_home_category
-- ----------------------------
DROP TABLE IF EXISTS `manager_menu_home_category`;
CREATE TABLE `manager_menu_home_category`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `category_id` bigint NOT NULL COMMENT '管理端app分类ID',
                                      `home_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理端app首页显示的图标URL',
                                      `home_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '管理端app首页显示名称',
                                      `jump_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/pages/category/category' COMMENT '跳转路径',
                                      `jump_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'navigateTo' COMMENT '跳转类型：navigateTo, redirectTo, switchTab',
                                      `sort` int NULL DEFAULT NULL COMMENT '排序',
                                      `status` tinyint(1) NOT NULL COMMENT '状态：0-隐藏，1-显示',
                                      `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除',
                                      `version` int NOT NULL DEFAULT 0 COMMENT '版本号（用于乐观锁）',
                                      `tenant_id` bigint NOT NULL COMMENT '租户ID',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_general_ci COMMENT = '管理端app首页分类配置表' ROW_FORMAT = DYNAMIC;




