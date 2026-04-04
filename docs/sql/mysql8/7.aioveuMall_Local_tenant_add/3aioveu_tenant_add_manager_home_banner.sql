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
-- Table structure for manager_menu_home_banner
-- ----------------------------
DROP TABLE IF EXISTS `manager_menu_home_banner`;
CREATE TABLE `manager_menu_home_banner`  (
                                      `id` bigint NOT NULL AUTO_INCREMENT,
                                      `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '滚播栏标题',
                                      `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '滚播栏图片地址',
                                      `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
                                      `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
                                      `status` tinyint(1) NOT NULL COMMENT '状态(1:开启；0:关闭)',
                                      `sort` int NULL DEFAULT NULL COMMENT '排序',
                                      `redirect_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转链接',
                                      `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除',
                                      `version` int NOT NULL DEFAULT 0 COMMENT '版本号（用于乐观锁）',
                                      `tenant_id` bigint NOT NULL COMMENT '租户ID',
                                      PRIMARY KEY (`id`) USING BTREE,
                                      INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4
    COLLATE = utf8mb4_general_ci COMMENT = '管理端app首页滚播栏' ROW_FORMAT = DYNAMIC;




