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

 Date: 16/03/2026 12:16:36
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_account
-- ----------------------------
DROP TABLE IF EXISTS `pay_account`;
CREATE TABLE `pay_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `account_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户编号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `account_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户',
  `balance` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '账户余额',
  `frozen_balance` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '冻结余额',
  `available_balance` decimal(15, 2) GENERATED ALWAYS AS ((`balance` - `frozen_balance`)) VIRTUAL COMMENT '可用余额' NULL,
  `total_income` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '总收入',
  `total_expend` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '总支出',
  `currency` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'CNY' COMMENT '币种',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '账户状态：0-冻结 1-正常 2-注销',
  `version` int NOT NULL DEFAULT 0 COMMENT '版本号，用于乐观锁',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account_no`(`account_no` ASC) USING BTREE COMMENT '账户编号唯一',
  UNIQUE INDEX `uk_user_account_type`(`user_id` ASC, `account_type` ASC) USING BTREE COMMENT '用户ID 和 账户类型 唯一',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '账户状态索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付账户表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_account
-- ----------------------------

-- ----------------------------
-- Table structure for pay_account_flow
-- ----------------------------
DROP TABLE IF EXISTS `pay_account_flow`;
CREATE TABLE `pay_account_flow`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水号',
  `account_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账户编号',
  `biz_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务单号',
  `biz_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现',
  `flow_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '变动金额',
  `balance_before` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '变动前余额',
  `balance_after` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '变动后余额',
  `frozen_before` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '变动前冻结',
  `frozen_after` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '变动后冻结',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_flow_no`(`flow_no` ASC) USING BTREE COMMENT '流水号唯一',
  INDEX `idx_account_no`(`account_no` ASC) USING BTREE COMMENT '账户编号索引',
  INDEX `idx_biz_no`(`biz_no` ASC) USING BTREE COMMENT '业务单号索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '账户流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_account_flow
-- ----------------------------

-- ----------------------------
-- Table structure for pay_channel_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_channel_config`;
CREATE TABLE `pay_channel_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `channel_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联',
  `channel_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道名称',
  `channel_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道类型：ONLINE-线上 OFFLINE-线下',
  `config_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置键',
  `config_value` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置值',
  `config_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置类型：CERT-证书 KEY-密钥 URL-地址',
  `config_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '配置描述',
  `is_enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用：0-禁用 1-启用',
  `is_default` tinyint(1) NULL DEFAULT 0 COMMENT '是否默认：0-否 1-是',
  `priority` int NULL DEFAULT 0 COMMENT '优先级，数值越大优先级越高',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_channel_config`(`channel_code` ASC, `config_key` ASC) USING BTREE COMMENT '渠道编码 和 配置键唯一',
  INDEX `idx_channel_code`(`channel_code` ASC) USING BTREE COMMENT '渠道编码索引',
  INDEX `idx_is_enabled`(`is_enabled` ASC) USING BTREE COMMENT '逻辑删除索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付渠道配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_channel_config
-- ----------------------------
INSERT INTO `pay_channel_config` VALUES (1, 'ALIPAY', '支付宝', 'ONLINE', 'app_id', 'your_app_id', 'STRING', '应用ID', 1, 1, 100, 0, '', '', '2026-02-02 16:45:14', '2026-03-13 19:10:29', 1);
INSERT INTO `pay_channel_config` VALUES (2, 'ALIPAY', '支付宝', 'ONLINE', 'private_key', 'your_private_key', 'SECRET', '应用私钥', 1, 1, 100, 0, '', '', '2026-02-02 16:45:14', '2026-03-13 19:10:29', 1);
INSERT INTO `pay_channel_config` VALUES (3, 'ALIPAY', '支付宝', 'ONLINE', 'alipay_public_key', 'your_public_key', 'SECRET', '支付宝公钥', 1, 1, 100, 0, '', '', '2026-02-02 16:45:14', '2026-03-13 19:10:29', 1);
INSERT INTO `pay_channel_config` VALUES (4, 'ALIPAY', '支付宝', 'ONLINE', 'gateway_url', 'https://openapi.alipay.com/gateway.do', 'URL', '网关地址', 1, 1, 100, 0, '', '', '2026-02-02 16:45:14', '2026-03-13 19:10:29', 1);
INSERT INTO `pay_channel_config` VALUES (5, 'WECHAT', '微信支付', 'ONLINE', 'app_id', 'your_app_id', 'STRING', '应用ID', 1, 0, 90, 0, '', '', '2026-02-02 16:45:14', '2026-03-13 19:10:29', 1);
INSERT INTO `pay_channel_config` VALUES (6, 'WECHAT', '微信支付', 'ONLINE', 'mch_id', 'your_mch_id', 'STRING', '商户号', 1, 0, 90, 0, '', '', '2026-02-02 16:45:14', '2026-03-13 19:10:29', 1);

-- ----------------------------
-- Table structure for pay_flow
-- ----------------------------
DROP TABLE IF EXISTS `pay_flow`;
CREATE TABLE `pay_flow`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `flow_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水号',
  `payment_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号',
  `refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款单号',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务订单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `flow_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账',
  `flow_direction` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '资金方向：IN-入金 OUT-出金',
  `amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '流水金额',
  `balance_before` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '交易前余额',
  `balance_after` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '交易后余额',
  `channel_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道编码',
  `third_flow_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方流水号',
  `flow_status` tinyint NOT NULL DEFAULT 0 COMMENT '流水状态：0-处理中 1-成功 2-失败',
  `trade_time` datetime NOT NULL COMMENT '交易时间',
  `complete_time` datetime NULL DEFAULT NULL COMMENT '完成时间',
  `error_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误代码',
  `error_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_flow_no`(`flow_no` ASC) USING BTREE COMMENT '流水号唯一',
  UNIQUE INDEX `uk_third_flow`(`channel_code` ASC, `third_flow_no` ASC, `flow_type` ASC) USING BTREE COMMENT '渠道编码 和 第三方流水号 和 流水类型唯一',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '支付单号索引',
  INDEX `idx_refund_no`(`refund_no` ASC) USING BTREE COMMENT '退款单号索引',
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE COMMENT '业务订单号索引',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_trade_time`(`trade_time` ASC) USING BTREE COMMENT '交易时间索引',
  INDEX `idx_flow_type`(`flow_type` ASC) USING BTREE COMMENT '流水类型索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付流水表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_flow
-- ----------------------------

-- ----------------------------
-- Table structure for pay_notify
-- ----------------------------
DROP TABLE IF EXISTS `pay_notify`;
CREATE TABLE `pay_notify`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `notify_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知编号',
  `payment_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付单号',
  `refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款单号',
  `notify_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知类型：PAYMENT-支付 REFUND-退款',
  `notify_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '通知地址',
  `request_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '请求数据',
  `response_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '响应数据',
  `notify_status` tinyint NOT NULL DEFAULT 0 COMMENT '通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败',
  `notify_count` int NOT NULL DEFAULT 0 COMMENT '通知次数',
  `max_notify_count` int NOT NULL DEFAULT 5 COMMENT '最大通知次数',
  `next_notify_time` datetime NOT NULL COMMENT '下次通知时间',
  `last_notify_time` datetime NULL DEFAULT NULL COMMENT '最后通知时间',
  `success_time` datetime NULL DEFAULT NULL COMMENT '成功时间',
  `error_message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_notify_no`(`notify_no` ASC) USING BTREE COMMENT '通知编号唯一',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '支付单号索引',
  INDEX `idx_refund_no`(`refund_no` ASC) USING BTREE COMMENT '退款单号索引',
  INDEX `idx_notify_status`(`notify_status` ASC) USING BTREE COMMENT '通知状态索引',
  INDEX `idx_next_notify_time`(`next_notify_time` ASC) USING BTREE COMMENT '下次通知时间索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付通知表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_notify
-- ----------------------------

-- ----------------------------
-- Table structure for pay_order
-- ----------------------------
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `payment_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务订单号（如退款单号、订单号）',
  `biz_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `payment_amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '支付/退款金额',
  `payment_status` tinyint NOT NULL DEFAULT 0 COMMENT '支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款',
  `payment_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额',
  `payment_method` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付方式：APP-APP支付 H5-H5支付 JSAPI-小程序/公众号 NATIVE-扫码支付',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `payment_expire_time` datetime NULL DEFAULT NULL COMMENT '支付过期时间',
  `third_payment_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方支付单号',
  `third_transaction_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方交易流水号',
  `attach_data` json NULL COMMENT '附加数据，JSON格式',
  `notify_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异步通知地址',
  `return_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '同步返回地址',
  `client_ip` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '客户端IP',
  `device_info` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '设备信息',
  `subject` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单标题',
  `body` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '订单描述',
  `currency` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'CNY' COMMENT '币种，默认人民币',
  `error_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误代码',
  `error_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `notify_status` tinyint NULL DEFAULT 0 COMMENT '通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败',
  `notify_count` int NULL DEFAULT 0 COMMENT '通知次数',
  `last_notify_time` datetime NULL DEFAULT NULL COMMENT '最后通知时间',
  `next_notify_time` datetime NULL DEFAULT NULL COMMENT '下次通知时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `version` int NOT NULL DEFAULT 0 COMMENT '版本号（用于乐观锁）',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_payment_no`(`payment_no` ASC) USING BTREE COMMENT '支付单号唯一',
  UNIQUE INDEX `uk_order_no_biz_type`(`order_no` ASC, `biz_type` ASC, `payment_channel` ASC) USING BTREE COMMENT '业务订单号，业务类型，支付渠道唯一',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_payment_status`(`payment_status` ASC) USING BTREE COMMENT '支付状态索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_payment_channel`(`payment_channel` ASC) USING BTREE COMMENT '支付渠道索引',
  INDEX `idx_payment_time`(`payment_time` ASC) USING BTREE COMMENT '支付时间索引',
  INDEX `idx_third_payment_no`(`third_payment_no` ASC) USING BTREE COMMENT '第三方支付单号索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 79 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------
INSERT INTO `pay_order` VALUES (8, 'PAY1770885438143405623', '202602120000053047', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000053047', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 16:37:18', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (9, 'PAY1770886697348179660', '202602120000053039', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000053039', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 16:58:17', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (10, 'PAY1770886966552660119', '202602120000050752', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000050752', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 17:02:47', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (11, 'PAY1770887243204543050', '202602120000056938', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000056938', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 17:07:23', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (13, 'PAY1770897691986898565', '202602120000058090', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000058090', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:01:32', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (14, 'PAY1770898039326852421', '202602120000055625', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000055625', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:07:19', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (15, 'PAY1770898202506241011', '202602120000055650', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000055650', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:10:03', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (16, 'PAY1770899727788777511', '202602120000054524', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000054524', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:35:28', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (17, 'PAY1770899860227885295', '202602120000058668', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000058668', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:37:40', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (18, 'PAY1770900069402313072', '202602120000055126', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000055126', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:41:09', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (19, 'PAY1770900186027952055', '202602120000050311', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000050311', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:43:06', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (20, 'PAY1770900444839522667', '202602120000057015', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000057015', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:47:25', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (21, 'PAY1770900791736228025', '202602120000051301', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000051301', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:53:12', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (22, 'PAY1770900863620975387', '202602120000051673', 'bizType', 5, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202602120000051673', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-02-12 20:54:24', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (45, 'PAY1773067398713689691', '202603090000066539', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603090000066539', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-09 22:43:19', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (46, 'PAY1773112737173362233', '202603100000068842', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000068842', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:18:57', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (47, 'PAY1773113151800384625', '202603100000068119', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000068119', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:25:52', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (48, 'PAY1773113382933987287', '202603100000062600', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000062600', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:29:43', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (49, 'PAY1773113671082465991', '202603100000065503', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000065503', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:34:31', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (50, 'PAY1773113747063470142', '202603100000061736', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000061736', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:35:47', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (51, 'PAY1773113803468326819', '202603100000060946', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000060946', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:36:43', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (52, 'PAY1773113840334676899', '202603100000061241', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000061241', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:37:20', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (53, 'PAY1773113940719287054', '202603100000062206', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000062206', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:39:01', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (54, 'PAY1773113983536365560', '202603100000061351', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000061351', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:39:44', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (55, 'PAY1773114607713566414', '202603100000067768', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000067768', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:50:08', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (56, 'PAY1773114824613522898', '202603100000062934', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000062934', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 11:53:45', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (57, 'PAY1773116804036310629', '202603100000069356', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000069356', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 12:26:44', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (58, 'PAY1773128148528257481', '202603100000066934', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000066934', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 15:35:49', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (59, 'PAY1773129065471623579', '202603100000060208', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000060208', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 15:51:06', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (60, 'PAY1773129346728854266', '202603100000067621', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000067621', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 15:55:47', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (61, 'PAY1773129607202242139', '202603100000066180', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000066180', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:00:07', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (62, 'PAY1773129781318543529', '202603100000062071', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000062071', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:03:01', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (63, 'PAY1773130012064313339', '202603100000066210', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000066210', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:06:52', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (64, 'PAY1773130409823497455', '202603100000063499', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000063499', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:13:30', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (65, 'PAY1773130559098789765', '202603100000062596', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000062596', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:15:59', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (66, 'PAY1773130671848515239', '202603100000067609', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000067609', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:17:52', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (67, 'PAY1773130992280788715', '202603100000060920', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000060920', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:23:12', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (68, 'PAY1773132207709111972', '202603100000062267', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000062267', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:43:28', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (69, 'PAY1773133069184979352', '202603100000067604', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000067604', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 16:57:49', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (70, 'PAY1773133871226661969', '202603100000068005', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000068005', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 17:11:11', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (71, 'PAY1773133960976717408', '202603100000069241', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000069241', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 17:12:41', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (72, 'PAY1773134089641913775', '202603100000064752', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000064752', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 17:14:50', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (73, 'PAY1773134516225639380', '202603100000069512', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000069512', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 17:21:56', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (74, 'PAY1773134765779594539', '202603100000064553', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000064553', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 17:26:06', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (75, 'PAY1773135806799621217', '202603100000061547', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000061547', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 17:43:27', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (76, 'PAY1773136561391430329', '202603100000061348', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000061348', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 17:56:01', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (77, 'PAY1773138505802164761', '202603100000060752', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000060752', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 18:28:26', '2026-03-13 19:10:34', 0, 1);
INSERT INTO `pay_order` VALUES (78, 'PAY1773138653284754998', '202603100000060421', 'bizType', 6, 0.00, 0, 'channel', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '商品购买', '订单号：202603100000060421', 'CNY', NULL, NULL, 0, 0, NULL, NULL, 0, '', '', '2026-03-10 18:30:53', '2026-03-13 19:10:34', 0, 1);

-- ----------------------------
-- Table structure for pay_reconciliation
-- ----------------------------
DROP TABLE IF EXISTS `pay_reconciliation`;
CREATE TABLE `pay_reconciliation`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `reconciliation_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '对账单号',
  `channel_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道编码',
  `bill_date` date NOT NULL COMMENT '对账日期',
  `bill_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '账单类型：PAYMENT-支付 REFUND-退款 ALL-全部',
  `total_count` int NULL DEFAULT 0 COMMENT '总笔数',
  `total_amount` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '总金额',
  `success_count` int NULL DEFAULT 0 COMMENT '成功笔数',
  `success_amount` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '成功金额',
  `failure_count` int NULL DEFAULT 0 COMMENT '失败笔数',
  `failure_amount` decimal(15, 2) NULL DEFAULT 0.00 COMMENT '失败金额',
  `difference_count` int NULL DEFAULT 0 COMMENT '差异笔数',
  `reconcile_status` tinyint NOT NULL DEFAULT 0 COMMENT '对账状态：0-未对账 1-对账中 2-对账完成 3-对账异常',
  `download_status` tinyint NULL DEFAULT 0 COMMENT '下载状态：0-未下载 1-下载中 2-下载完成 3-下载失败',
  `download_time` datetime NULL DEFAULT NULL COMMENT '下载时间',
  `reconcile_time` datetime NULL DEFAULT NULL COMMENT '对账时间',
  `bill_file_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '账单文件URL',
  `error_message` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_bill_date_channel`(`bill_date` ASC, `channel_code` ASC, `bill_type` ASC) USING BTREE COMMENT '对账日期 和 渠道编码 和 账单类型唯一',
  INDEX `idx_reconcile_status`(`reconcile_status` ASC) USING BTREE COMMENT '对账状态索引',
  INDEX `idx_bill_date`(`bill_date` ASC) USING BTREE COMMENT '对账日期索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付对账表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_reconciliation
-- ----------------------------

-- ----------------------------
-- Table structure for pay_reconciliation_detail
-- ----------------------------
DROP TABLE IF EXISTS `pay_reconciliation_detail`;
CREATE TABLE `pay_reconciliation_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `reconciliation_id` bigint NOT NULL COMMENT '对账单ID',
  `channel_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '渠道编码',
  `bill_date` date NOT NULL COMMENT '对账日期',
  `third_transaction_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '第三方交易流水号',
  `third_order_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方订单号',
  `payment_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '系统支付单号',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '业务订单号',
  `trade_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易类型：PAYMENT-支付 REFUND-退款',
  `trade_time` datetime NOT NULL COMMENT '交易时间',
  `trade_amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '交易金额',
  `trade_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中',
  `reconcile_status` tinyint NOT NULL DEFAULT 0 COMMENT '对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多',
  `difference_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '差异原因',
  `reconcile_result` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '对账结果：MATCH-匹配 SYS_MORE-系统多 CHANNEL_MORE-渠道多',
  `reconcile_time` datetime NULL DEFAULT NULL COMMENT '对账时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_channel_transaction`(`channel_code` ASC, `third_transaction_no` ASC, `bill_date` ASC) USING BTREE COMMENT '渠道编码 和 第三方交易流水号 和 对账日期唯一',
  INDEX `idx_reconciliation_id`(`reconciliation_id` ASC) USING BTREE COMMENT '对账单ID索引',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '系统支付单号索引',
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE COMMENT '业务订单号索引',
  INDEX `idx_trade_time`(`trade_time` ASC) USING BTREE COMMENT '交易时间索引',
  INDEX `idx_reconcile_status`(`reconcile_status` ASC) USING BTREE COMMENT '对账状态索引',
  INDEX `idx_bill_date`(`bill_date` ASC) USING BTREE COMMENT '对账日期索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '对账明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_reconciliation_detail
-- ----------------------------

-- ----------------------------
-- Table structure for pay_refund_record
-- ----------------------------
DROP TABLE IF EXISTS `pay_refund_record`;
CREATE TABLE `pay_refund_record`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `refund_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款单号，格式：RFyyyyMMddHHmmss+6位随机',
  `payment_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原支付单号',
  `order_no` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务订单号',
  `biz_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务类型：REFUND-退款 ORDER-订单退款',
  `refund_amount` decimal(15, 2) NOT NULL DEFAULT 0.00 COMMENT '退款金额',
  `refund_status` tinyint NOT NULL DEFAULT 0 COMMENT '退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭',
  `refund_channel` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额',
  `refund_reason` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款原因',
  `apply_time` datetime NOT NULL COMMENT '申请时间',
  `refund_time` datetime NULL DEFAULT NULL COMMENT '退款完成时间',
  `third_refund_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方退款单号',
  `third_transaction_no` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '第三方退款流水号',
  `currency` varchar(3) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT 'CNY' COMMENT '币种',
  `error_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误代码',
  `error_message` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '错误信息',
  `notify_status` tinyint NULL DEFAULT 0 COMMENT '通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败',
  `notify_count` int NULL DEFAULT 0 COMMENT '通知次数',
  `last_notify_time` datetime NULL DEFAULT NULL COMMENT '最后通知时间',
  `next_notify_time` datetime NULL DEFAULT NULL COMMENT '下次通知时间',
  `callback_data` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '回调数据',
  `callback_time` datetime NULL DEFAULT NULL COMMENT '回调时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '创建人',
  `update_by` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT '' COMMENT '更新人',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_refund_no`(`refund_no` ASC) USING BTREE COMMENT '退款单号唯一',
  UNIQUE INDEX `uk_payment_refund`(`payment_no` ASC, `refund_channel` ASC) USING BTREE COMMENT '防止同一支付单重复退款',
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE COMMENT '业务订单号索引',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '原支付单号索引',
  INDEX `idx_refund_status`(`refund_status` ASC) USING BTREE COMMENT '退款状态索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_refund_time`(`refund_time` ASC) USING BTREE COMMENT '退款完成时间索引',
  INDEX `idx_third_refund_no`(`third_refund_no` ASC) USING BTREE COMMENT '第三方退款流水号索引',
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_refund_record
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回滚日志表 AT transaction mode undo table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
