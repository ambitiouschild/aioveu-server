/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_ums

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 16/03/2026 12:17:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ums_member
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0=未知,1=男,2=女)',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',
  `point` int NULL DEFAULT 0 COMMENT '会员积分',
  `balance` bigint NULL DEFAULT 1000000000 COMMENT '账户余额(单位:分)',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0=未删除,1=已删除)',
  `openid` char(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信OpenID',
  `session_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信会话密钥',
  `country` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `language` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语言',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_mobile`(`mobile` ASC) USING BTREE COMMENT '手机号唯一索引',
  UNIQUE INDEX `uk_openid`(`openid` ASC) USING BTREE COMMENT 'OpenID唯一索引',
  INDEX `idx_status_deleted`(`status` ASC, `deleted` ASC) USING BTREE COMMENT '状态删除联合索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '会员表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_member
-- ----------------------------
INSERT INTO `ums_member` VALUES (1, '可我不敌可爱', '13061656192', 'https://minio.aioveu.com/aioveu/20251128/9dc40c944d044c8d8ae37b14a35b8b83.png', 1, '2026-01-12', 0, 1000000000, 1, 0, '', NULL, NULL, NULL, NULL, NULL, '2026-01-12 16:04:01', '2026-01-12 16:04:01', 1);
INSERT INTO `ums_member` VALUES (3, '美人', '13061656191', 'https://minio.aioveu.com/aioveu/20251128/9dc40c944d044c8d8ae37b14a35b8b83.png', 1, NULL, 0, 1000000000, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, '2026-01-12 16:04:24', '2026-01-12 16:04:24', 1);
INSERT INTO `ums_member` VALUES (4, '大哈', '13061656197', 'https://minio.aioveu.com/aioveu/20251128/9dc40c944d044c8d8ae37b14a35b8b83.png', NULL, NULL, 0, 1000000000, 1, 0, NULL, NULL, NULL, NULL, NULL, NULL, '2026-01-12 16:05:00', '2026-01-12 16:05:00', 1);
INSERT INTO `ums_member` VALUES (5, '可我不敌可爱', '13061656199', 'https://minio.aioveu.com/aioveu/20251128/9dc40c944d044c8d8ae37b14a35b8b83.png', NULL, NULL, 0, 1000000000, 1, 0, 'ojToH7IdQ8Uccvc1OXt6M33rGdE4', NULL, NULL, NULL, NULL, NULL, '2026-01-14 15:43:03', '2026-01-14 15:43:03', 1);
INSERT INTO `ums_member` VALUES (6, '新注册微信用户', NULL, 'https://cdn.aioveu.com/aioveu/aioveu-server/avatar/avatar.png', NULL, NULL, 0, 1000000000, 1, 0, 'o0b5v3fcRQ-wYgsIPkdZy7TbMFp0', NULL, NULL, NULL, NULL, NULL, '2026-03-08 11:55:57', '2026-03-08 11:55:57', 1);

-- ----------------------------
-- Table structure for ums_member_address
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_address`;
CREATE TABLE `ums_member_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `member_id` bigint NULL DEFAULT NULL COMMENT '会员ID',
  `consignee_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `consignee_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人联系方式',
  `country` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区/县',
  `street` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '街道',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `postal_code` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `defaulted` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认地址(0=否,1=是)',
  `address_tag` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '地址标签(家,公司,学校等)',
  `longitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '经度',
  `latitude` decimal(10, 7) NULL DEFAULT NULL COMMENT '纬度',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0=已删除,1=正常)',
  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_member_id`(`member_id` ASC) USING BTREE COMMENT '会员ID索引',
  INDEX `idx_is_default`(`defaulted` ASC) USING BTREE COMMENT '默认地址索引',
  INDEX `idx_province_city`(`province` ASC, `city` ASC) USING BTREE COMMENT '省市区联合索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 18 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '会员收货地址表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of ums_member_address
-- ----------------------------
INSERT INTO `ums_member_address` VALUES (1, 5, '苏轼', '13061656199', NULL, '河南', '郑州', '二七', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-01-19 20:59:56', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (2, 5, '李白11', '13061656199', '', '天津市', '天津市', '和平区', NULL, '银基大厦', '', 0, '', NULL, NULL, 1, '2026-01-19 21:00:13', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (3, 5, '杜甫', '13061656199', NULL, '山西省', '大同市', '新荣区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-01-19 21:00:30', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (4, 5, '李清照', '13061656199', NULL, '吉林省', '长春市', '朝阳区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-01-19 21:01:03', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (5, 5, '王安石11', '13061656199', '', '内蒙古自治区', '呼和浩特市', '玉泉区', NULL, '银基大厦', '', 0, '', NULL, NULL, 1, '2026-01-19 21:01:28', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (6, 5, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-02-04 18:25:51', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (7, 5, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-02-04 18:25:51', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (8, 5, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-02-04 18:25:51', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (9, 5, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-02-04 18:25:52', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (10, 5, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-02-04 18:25:52', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (11, 5, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 0, NULL, NULL, NULL, 1, '2026-02-04 18:25:52', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (13, 5, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 1, NULL, NULL, NULL, 1, '2026-02-04 18:25:53', '2026-03-13 19:05:32', 1);
INSERT INTO `ums_member_address` VALUES (17, 6, '苏轼', '13061656199', NULL, '河南省', '郑州市', '二七区', NULL, '银基大厦', NULL, 1, NULL, NULL, NULL, 1, '2026-03-08 18:15:10', '2026-03-13 19:05:32', 1);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
