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

 Date: 23/03/2026 17:15:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_oauth_client_wx_app
-- ----------------------------
DROP TABLE IF EXISTS `sys_oauth_client_wx_app`;
CREATE TABLE `sys_oauth_client_wx_app`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `client_id` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'OAuth2 client客户端 ID',
  `wx_appid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信小程序appid',
  `wx_appname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信小程序名称',
  `registered_email` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '微信小程序注册邮箱',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_client_id`(`client_id` ASC) USING BTREE,
  UNIQUE INDEX `uk_wx_appid`(`wx_appid` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'OAuth2客户端与微信小程序映射表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_oauth_client_wx_app
-- ----------------------------
INSERT INTO `sys_oauth_client_wx_app` VALUES (1, 'mall-app', 'wxe510ad6d4d60bfa8', '可我不敌心动', '1', 0, '2026-03-19 17:58:53', '2026-03-21 16:19:45');
INSERT INTO `sys_oauth_client_wx_app` VALUES (2, 'mall-app1', 'wxda125635c09f641a', '可我不敌心软', '1', 0, '2026-03-19 17:59:10', '2026-03-21 16:20:00');
INSERT INTO `sys_oauth_client_wx_app` VALUES (3, '3', 'wxe0e6720ed2db3937', '可我不敌心欢', '2', 0, '2026-03-19 17:59:25', '2026-03-19 17:59:25');
INSERT INTO `sys_oauth_client_wx_app` VALUES (5, '4', '1', '1', '2819850488@qq.com', 0, '2026-03-19 19:17:47', '2026-03-19 19:17:47');

SET FOREIGN_KEY_CHECKS = 1;
