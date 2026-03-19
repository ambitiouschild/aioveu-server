/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_sms

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 16/03/2026 12:17:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sms_advert
-- ----------------------------
DROP TABLE IF EXISTS `sms_advert`;
CREATE TABLE `sms_advert`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '广告标题',
  `image_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '图片地址',
  `start_time` datetime NULL DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime NULL DEFAULT NULL COMMENT '结束时间',
  `status` tinyint(1) NOT NULL COMMENT '状态(1:开启；0:关闭)',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `redirect_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转链接',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间(新增有值)',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 69 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '广告表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sms_advert
-- ----------------------------
INSERT INTO `sms_advert` VALUES (35, '万事如意', 'https://cdn.aioveu.com/aioveu/1001/image/20260302/7f1677662cf34e24847b01dd054ef2d1.png', '2022-07-04 00:00:00', '2022-10-07 00:00:00', 1, 3, NULL, NULL, '2022-07-04 00:08:10', '2026-03-02 19:04:29', 1);
INSERT INTO `sms_advert` VALUES (36, '百年好合', 'https://cdn.aioveu.com/aioveu/1001/image/20260302/9c10d3acc4b7477bba6e9c4fdefded6b.png', '2022-07-04 00:00:00', '2022-10-07 00:00:00', 1, 2, NULL, NULL, '2022-07-04 00:08:10', '2026-03-02 19:04:42', 1);
INSERT INTO `sms_advert` VALUES (47, '天天开心', 'https://cdn.aioveu.com/aioveu/1001/image/20260302/729348a1509141dd9e808aa97b0095a8.png', '2022-07-04 00:00:00', '2026-01-08 00:00:00', 1, 1, NULL, NULL, '2022-07-04 00:08:10', '2026-03-02 19:05:35', 1);
INSERT INTO `sms_advert` VALUES (67, '长风破浪', 'https://cdn.aioveu.com/aioveu/1001/image/20260302/5c3ad4c5b12144b798d38d21457ec725.png', '2022-07-04 00:00:00', '2025-07-23 00:00:00', 1, 100, NULL, '是是是', '2022-07-04 00:08:10', '2026-03-02 19:07:50', 1);
INSERT INTO `sms_advert` VALUES (68, '新年快乐', 'https://cdn.aioveu.com/aioveu/1001/image/20260302/4ee1acc0b79e40058511da4e34f41704.png', '2026-01-16 01:49:51', '2026-01-16 01:49:55', 1, NULL, NULL, NULL, '2026-01-15 17:50:03', '2026-03-02 23:30:56', 1);

-- ----------------------------
-- Table structure for sms_coupon
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon`;
CREATE TABLE `sms_coupon`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '优惠券名称',
  `type` tinyint NOT NULL DEFAULT 1 COMMENT '优惠券类型(1-满减券;2-直减券;3-折扣券)',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '0' COMMENT '优惠券码',
  `platform` int NULL DEFAULT NULL COMMENT '使用平台(0-全平台;1-APP;2-PC)',
  `face_value_type` tinyint NULL DEFAULT NULL COMMENT '优惠券面值类型',
  `face_value` bigint NULL DEFAULT NULL COMMENT '优惠券面值(分)',
  `discount` decimal(10, 2) NULL DEFAULT NULL COMMENT '折扣',
  `min_point` bigint NULL DEFAULT NULL COMMENT '使用门槛(0:无门槛)',
  `per_limit` int NULL DEFAULT 1 COMMENT '每人限领张数(-1-无限制)',
  `validity_period_type` tinyint NULL DEFAULT NULL COMMENT '有效期类型(1:自领取时起有效天数;2:有效起止时间)',
  `validity_days` int NULL DEFAULT 1 COMMENT '有效期天数',
  `validity_begin_time` datetime NULL DEFAULT NULL COMMENT '有效期起始时间',
  `validity_end_time` datetime NULL DEFAULT NULL COMMENT '有效期截止时间',
  `application_scope` tinyint NULL DEFAULT NULL COMMENT '应用范围(0-全场通用;1-指定商品分类;2-指定商品)',
  `circulation` int NULL DEFAULT 1 COMMENT '发行量(-1-无限制)',
  `received_count` int NULL DEFAULT 0 COMMENT '已领取的优惠券数量(统计)',
  `used_count` int NULL DEFAULT 0 COMMENT '已使用的优惠券数量(统计)',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `update_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_time` datetime NULL DEFAULT NULL COMMENT '修改时间',
  `deleted` tinyint NULL DEFAULT 1 COMMENT '逻辑删除标识(0-正常;1-删除)',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 20 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '优惠券表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sms_coupon
-- ----------------------------
INSERT INTO `sms_coupon` VALUES (1, '新人无门槛优惠券,立减20元', 1, '123456', 1, 2, 100000000000000, 0.80, 10000000000000, 1, 1, 7, '2021-03-17 21:10:35', '2025-03-17 21:10:38', 2, 1, 0, 0, NULL, '2022-11-08 16:29:26', '2021-03-17 21:10:56', 0, 1);
INSERT INTO `sms_coupon` VALUES (2, '满减优惠券', 1, '50', 2, NULL, 12, 0.00, 10000000, 1, 2, 7, NULL, NULL, 2, 100, 0, 0, NULL, '2022-07-16 01:28:42', '2021-03-17 21:19:46', 0, 1);

-- ----------------------------
-- Table structure for sms_coupon_history
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_history`;
CREATE TABLE `sms_coupon_history`  (
  `id` bigint NOT NULL,
  `coupon_id` bigint NULL DEFAULT NULL COMMENT '优惠券ID',
  `member_id` bigint NULL DEFAULT NULL COMMENT '会员ID',
  `member_nickname` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '会员昵称',
  `coupon_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '优惠券码',
  `get_type` tinyint NULL DEFAULT NULL COMMENT '获取类型(1：后台增删；2：主动领取)',
  `status` tinyint NULL DEFAULT NULL COMMENT '状态(0：未使用；1：已使用；2：已过期)',
  `use_time` datetime NULL DEFAULT NULL COMMENT '使用时间',
  `order_id` bigint NULL DEFAULT NULL COMMENT '订单ID',
  `order_sn` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '订单号',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sms_coupon_history
-- ----------------------------

-- ----------------------------
-- Table structure for sms_coupon_spu
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_spu`;
CREATE TABLE `sms_coupon_spu`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `spu_id` bigint NOT NULL COMMENT '商品ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1548148841429663756 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sms_coupon_spu
-- ----------------------------
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663747, 8, 289, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663748, 8, 2, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663749, 10, 2, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663750, 12, 289, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663751, 12, 2, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663752, 12, 287, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663753, 15, 289, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663754, 15, 2, 1);
INSERT INTO `sms_coupon_spu` VALUES (1548148841429663755, 15, 287, 1);

-- ----------------------------
-- Table structure for sms_coupon_spu_category
-- ----------------------------
DROP TABLE IF EXISTS `sms_coupon_spu_category`;
CREATE TABLE `sms_coupon_spu_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `coupon_id` bigint NOT NULL COMMENT '优惠券ID',
  `category_id` bigint NOT NULL COMMENT '商品分类ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sms_coupon_spu_category
-- ----------------------------
INSERT INTO `sms_coupon_spu_category` VALUES (1, 1, 1, 1);
INSERT INTO `sms_coupon_spu_category` VALUES (2, 2, 2, 1);
INSERT INTO `sms_coupon_spu_category` VALUES (3, 1, 1, 1);

-- ----------------------------
-- Table structure for sms_home_advert
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_advert`;
CREATE TABLE `sms_home_advert`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `advert_id` bigint NOT NULL COMMENT '关联广告ID（sms_advert表）',
  `home_advert_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '广告显示的图标URL',
  `home_advert_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '广告显示名称',
  `height` int NULL DEFAULT 210 COMMENT '高度（rpx/upx）',
  `image_mode` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'scaleToFill' COMMENT '图片模式',
  `jump_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT '/pages/category/category' COMMENT '跳转路径',
  `jump_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT 'navigateTo' COMMENT '跳转类型：navigateTo, redirectTo, switchTab',
  `jump_params` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '跳转参数（JSON格式）',
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
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首页广告配置表（增加跳转路径）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sms_home_advert
-- ----------------------------
INSERT INTO `sms_home_advert` VALUES (1, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260304/2a8febb7ea0a43b7a865f708b65ae23f.png', '欢迎来到 可我不敌心动', 210, 'scaleToFill', '/pages/chat/claude', 'navigateTo', NULL, 1, 1, NULL, '2026-03-04 12:26:38', '2026-03-13 19:06:39', 0, 0, 1);

-- ----------------------------
-- Table structure for sms_home_category
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_category`;
CREATE TABLE `sms_home_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_id` bigint NOT NULL COMMENT '关联商品分类ID（pms_category表）',
  `home_icon` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首页显示的图标URL',
  `home_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '首页显示名称',
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
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首页分类配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sms_home_category
-- ----------------------------
INSERT INTO `sms_home_category` VALUES (1, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/6af64dc16abb40918d9ae981fbd1a01d.png', 'ClaudeCode', '/pages/chat/claude', 'navigateTo', 1, 1, NULL, '2026-03-04 11:40:22', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (2, 2, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/d893f866a2df45529555e5a5c74fe10c.png', 'ChatGPT', '/pages/chat/chatgpt', 'navigateTo', 2, 1, NULL, '2026-03-04 11:40:22', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (3, 3, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/1933c1c720d0480090f85e1a5d14e993.png', 'Gemini', '/pages/chat/gemini', 'navigateTo', 3, 1, NULL, '2026-03-04 11:40:22', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (4, 4, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/b015d25c345849c398c8d2756af745aa.png', 'xAI', '/pages/chat/xai', 'navigateTo', 4, 1, NULL, '2026-03-04 11:40:22', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (5, 5, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/b37c234d4fa247eeba2c27f9668fa39c.png', 'DeepSeek', '/pages/chat/deepseek', 'navigateTo', 5, 1, NULL, '2026-03-04 11:40:22', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (6, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/5adb662fa00d4b069040891417074688.png', '阿里千问', '/pages/category/category', 'navigateTo', 6, 1, NULL, '2026-03-04 11:40:22', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (7, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/edc985cd0c9d4765b664fbc307f44d16.png', '豆包', '/pages/category/category', 'navigateTo', 7, 1, NULL, '2026-03-04 19:50:30', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (8, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/fe3fac1ea7b64182ae243bfb77e6b84c.png', '元宝', '/pages/category/category', 'navigateTo', 8, 1, NULL, '2026-03-04 19:51:25', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (9, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/2673e666d7d748ba8fe741c06e89ac62.png', 'Kimi', '/pages/category/category', 'navigateTo', 9, 1, NULL, '2026-03-04 19:51:59', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (10, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/84c2950d540c4b928c2e03db96f6be16.png', '智谱清言', '/pages/category/category', 'navigateTo', 10, 1, NULL, '2026-03-04 19:51:59', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (11, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/b2a97b1400674bb4aac35403c1205374.png', 'MiniMax', '/pages/category/category', 'navigateTo', 11, 1, NULL, '2026-03-04 19:51:59', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (12, 12, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/d765b640bcfd4dc19ae978f0ebaa2083.png', 'Manus', '/pages/category/category', 'navigateTo', 12, 1, NULL, '2026-03-04 19:51:59', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (13, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/f40da023235c45798ad2d67365b7bfd5.png', 'Lovable', '/pages/category/category', 'navigateTo', 13, 1, NULL, '2026-03-04 19:51:59', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (14, 14, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/42f7afbb872449918ec5d579d4f2a286.png', 'Hug Face', '/pages/category/category', 'navigateTo', 14, 1, NULL, '2026-03-04 19:51:59', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (15, 15, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/6b708981131e40c18290cef9c537eecd.png', 'GitHub', '/pages/category/category', 'navigateTo', 15, 1, NULL, '2026-03-04 19:51:59', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (17, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260308/015a5d4ffe084a8cb613e9c7f2c2c348.png', '无敌库库', '/pages/category/category', 'navigateTo', 16, 1, NULL, '2026-03-05 21:51:37', '2026-03-13 19:06:40', 0, 0, 1);
INSERT INTO `sms_home_category` VALUES (18, 1, 'https://cdn.aioveu.com/aioveu/1001/image/20260305/50eb62f4ce134c64bc2f2da8e619c0dd.png', '可我不敌心动', '/pages/category/category', 'navigateTo', 17, 1, NULL, '2026-03-05 21:52:07', '2026-03-13 19:06:40', 0, 0, 1);

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
