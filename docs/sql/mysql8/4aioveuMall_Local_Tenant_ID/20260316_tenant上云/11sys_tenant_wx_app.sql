/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-tenant_Cloud_98_3309
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 139.224.209.98:3309
 Source Schema         : aioveu_tenant

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 19/03/2026 18:00:12
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_tenant_wx_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_wx_app`;
CREATE TABLE `sys_tenant_wx_app`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `wx_appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信小程序ID',
  `wx_appname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信小程序名称',
  `registered_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信小程序注册邮箱',
  `is_default` tinyint NULL DEFAULT 0 COMMENT '是否为默认小程序',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_wx`(`tenant_id` ASC, `wx_appid` ASC) USING BTREE,
  INDEX `idx_wx_appid`(`wx_appid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '租户与微信小程序关联表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_tenant_wx_app
-- ----------------------------
INSERT INTO `sys_tenant_wx_app` VALUES (1, 1, 'wxe510ad6d4d60bfa8', '可我不敌心动', '1', 0, 0, '2026-03-19 17:56:12', '2026-03-19 17:56:12');
INSERT INTO `sys_tenant_wx_app` VALUES (2, 1, 'wxda125635c09f641a', '可我不敌心软', '1', 0, 0, '2026-03-19 17:56:25', '2026-03-19 17:56:25');
INSERT INTO `sys_tenant_wx_app` VALUES (3, 1, 'wxe0e6720ed2db3937', '可我不敌心欢', '1', 0, 0, '2026-03-19 17:56:42', '2026-03-19 17:56:42');

SET FOREIGN_KEY_CHECKS = 1;
