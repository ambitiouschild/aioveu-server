/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_refund

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 13/03/2026 18:36:23
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for refund_delivery
-- ----------------------------
DROP TABLE IF EXISTS `refund_delivery`;
CREATE TABLE `refund_delivery`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `refund_id` bigint NOT NULL COMMENT '退款申请ID',
  `delivery_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '物流类型：1-买家发货 2-卖家发货 3-换货发货',
  `delivery_company` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流公司',
  `delivery_sn` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '物流单号',
  `receiver_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人姓名',
  `receiver_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货人电话',
  `receiver_address` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '收货地址',
  `delivery_time` datetime NULL DEFAULT NULL COMMENT '发货时间',
  `receive_time` datetime NULL DEFAULT NULL COMMENT '收货时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_refund_id_type`(`refund_id` ASC, `delivery_type` ASC) USING BTREE COMMENT '退款申请ID 和 物流类型唯一',
  INDEX `idx_delivery_sn`(`delivery_sn` ASC) USING BTREE COMMENT '物流单号索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款物流信息表（用于退货）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_delivery
-- ----------------------------

-- ----------------------------
-- Table structure for refund_item
-- ----------------------------
DROP TABLE IF EXISTS `refund_item`;
CREATE TABLE `refund_item`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `refund_id` bigint NOT NULL COMMENT '退款申请ID',
  `refund_type` tinyint(1) NULL DEFAULT NULL COMMENT '退款类型（冗余字段，与主表一致）',
  `order_item_id` bigint NOT NULL COMMENT '订单项ID',
  `spu_id` bigint NOT NULL COMMENT '商品ID',
  `spu_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `sku_id` bigint NOT NULL COMMENT 'SKU ID',
  `sku_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'SKU名称',
  `pic_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片',
  `price` decimal(10, 2) NOT NULL COMMENT '商品单价（分）',
  `quantity` int NOT NULL DEFAULT 1 COMMENT '退款数量',
  `refund_amount` decimal(10, 2) NOT NULL COMMENT '退款金额（分）',
  `refund_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '该商品的退款原因',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_refund_id`(`refund_id` ASC) USING BTREE COMMENT '退款申请ID索引',
  INDEX `idx_order_item_id`(`order_item_id` ASC) USING BTREE COMMENT '订单项ID索引',
  INDEX `idx_sku_id`(`sku_id` ASC) USING BTREE COMMENT 'SKU ID索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款商品明细表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_item
-- ----------------------------

-- ----------------------------
-- Table structure for refund_operation_log
-- ----------------------------
DROP TABLE IF EXISTS `refund_operation_log`;
CREATE TABLE `refund_operation_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `refund_id` bigint NOT NULL COMMENT '退款申请ID',
  `operation_type` tinyint NOT NULL COMMENT '操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动',
  `operation_content` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '操作内容',
  `operator_id` bigint NULL DEFAULT NULL COMMENT '操作人ID',
  `operator_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '操作人名称',
  `operator_type` tinyint(1) NULL DEFAULT 1 COMMENT '操作人类型：1-用户 2-客服 3-商家 4-系统',
  `before_status` tinyint NULL DEFAULT NULL COMMENT '操作前状态',
  `after_status` tinyint NULL DEFAULT NULL COMMENT '操作后状态',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_refund_id`(`refund_id` ASC) USING BTREE COMMENT '退款申请ID索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款操作记录表（用于审计）' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_operation_log
-- ----------------------------

-- ----------------------------
-- Table structure for refund_order
-- ----------------------------
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `order_id` bigint NOT NULL COMMENT '订单ID',
  `order_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '订单编号',
  `refund_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款单号',
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `refund_type` tinyint(1) NOT NULL COMMENT '退款类型：1-仅退款 2-退货退款 3-换货',
  `refund_reason` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款原因',
  `description` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '补充说明',
  `proof_images` json NULL COMMENT '退款凭证图片（JSON数组）',
  `refund_amount` decimal(10, 2) NOT NULL COMMENT '申请退款金额（分）',
  `actual_refund_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '实际退款金额（分）',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败',
  `handle_note` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理备注',
  `handle_by` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '处理人',
  `handle_time` datetime NULL DEFAULT NULL COMMENT '处理时间',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-正常 1-删除',
  `version` int NOT NULL DEFAULT 0 COMMENT '版本号（用于乐观锁）',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_refund_sn`(`refund_sn` ASC) USING BTREE COMMENT '退款单号唯一',
  INDEX `idx_order_id`(`order_id` ASC) USING BTREE COMMENT '订单ID索引',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '退款状态索引',
  INDEX `idx_user_status`(`user_id` ASC, `status` ASC) USING BTREE COMMENT '用户+退款状态复合索引',
  INDEX `idx_order_status`(`order_id` ASC, `status` ASC) USING BTREE COMMENT '订单+退款状态复合索引',
  INDEX `idx_time_status`(`create_time` ASC, `status` ASC) USING BTREE COMMENT '创建时间+退款状态复合索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单退款申请表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_order
-- ----------------------------

-- ----------------------------
-- Table structure for refund_payment
-- ----------------------------
DROP TABLE IF EXISTS `refund_payment`;
CREATE TABLE `refund_payment`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `refund_id` bigint NOT NULL COMMENT '退款申请ID',
  `payment_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '退款支付单号',
  `payment_type` tinyint(1) NOT NULL COMMENT '支付类型：1-微信 2-支付宝 3-银行卡 4-余额',
  `payment_amount` decimal(10, 2) NOT NULL COMMENT '支付金额（分）',
  `payment_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败',
  `payment_time` datetime NULL DEFAULT NULL COMMENT '支付时间',
  `payment_channel` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付渠道',
  `payment_trade_no` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付交易号',
  `payment_fee` decimal(10, 2) NULL DEFAULT 0.00 COMMENT '支付手续费（分）',
  `remark` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_payment_sn`(`payment_sn` ASC) USING BTREE COMMENT '退款支付单号唯一',
  INDEX `idx_refund_id`(`refund_id` ASC) USING BTREE COMMENT '退款申请ID索引',
  INDEX `idx_payment_status`(`payment_status` ASC) USING BTREE COMMENT '支付状态索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款支付记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_payment
-- ----------------------------

-- ----------------------------
-- Table structure for refund_proof
-- ----------------------------
DROP TABLE IF EXISTS `refund_proof`;
CREATE TABLE `refund_proof`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `refund_id` bigint NOT NULL COMMENT '退款申请ID',
  `proof_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他',
  `image_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '图片URL',
  `image_desc` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图片描述',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_refund_id`(`refund_id` ASC) USING BTREE COMMENT '退款申请ID索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款凭证图片表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_proof
-- ----------------------------

-- ----------------------------
-- Table structure for refund_reason
-- ----------------------------
DROP TABLE IF EXISTS `refund_reason`;
CREATE TABLE `refund_reason`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `reason_type` tinyint(1) NOT NULL COMMENT '原因类型：1-仅退款原因 2-退货退款原因 3-换货原因',
  `reason_content` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '原因内容',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `status` tinyint(1) NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_reason_type`(`reason_type` ASC) USING BTREE COMMENT '原因类型索引'
) ENGINE = InnoDB AUTO_INCREMENT = 17 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款原因分类表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of refund_reason
-- ----------------------------
INSERT INTO `refund_reason` VALUES (1, 1, '拍错/不想要了', 1, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (2, 1, '商品信息描述不符', 2, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (3, 1, '质量问题', 3, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (4, 1, '快递/物流问题', 4, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (5, 1, '未按时发货', 5, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (6, 1, '其他原因', 6, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (7, 2, '商品信息描述不符', 1, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (8, 2, '质量问题', 2, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (9, 2, '收到商品破损', 3, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (10, 2, '商品错发/漏发', 4, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (11, 2, '其他原因', 5, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (12, 3, '商品信息描述不符', 1, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (13, 3, '质量问题', 2, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (14, 3, '收到商品破损', 3, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (15, 3, '商品错发/漏发', 4, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');
INSERT INTO `refund_reason` VALUES (16, 3, '尺码不合适', 5, 1, '2026-01-31 12:59:11', '2026-01-31 12:59:11');

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
