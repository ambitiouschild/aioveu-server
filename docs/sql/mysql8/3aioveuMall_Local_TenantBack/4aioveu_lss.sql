/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_lss

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 13/03/2026 18:32:41
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for gen_config
-- ----------------------------
DROP TABLE IF EXISTS `gen_config`;
CREATE TABLE `gen_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `table_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '表名',
  `module_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模块名',
  `package_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '包名',
  `business_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '业务名',
  `entity_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '实体类名',
  `author` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '作者',
  `parent_menu_id` bigint NULL DEFAULT NULL COMMENT '上级菜单ID，对应sys_menu的id ',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` bit(1) NULL DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tablename`(`table_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 42 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成基础配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_config
-- ----------------------------
INSERT INTO `gen_config` VALUES (4, 'oms_order', 'aioveuMallOmsOrder', 'com.aioveu', '订单详情', 'OmsOrder', '可我不敌可爱', 149, '2026-01-07 18:11:10', '2026-01-07 18:11:10', b'0');
INSERT INTO `gen_config` VALUES (5, 'oms_order_item', 'aioveuOmsOrderItem', 'com.aioveu', '订单商品信息', 'OmsOrderItem', '可我不敌可爱', 149, '2026-01-08 19:32:58', '2026-01-08 19:32:58', b'0');
INSERT INTO `gen_config` VALUES (6, 'oms_order_delivery', 'aioveuMallOmsOrderDelivery', 'com.aioveu', '订单物流记录', 'OmsOrderDelivery', '可我不敌可爱', 149, '2026-01-08 20:09:36', '2026-01-08 20:09:36', b'0');
INSERT INTO `gen_config` VALUES (7, 'oms_order_log', 'aioveuMallOmsOrderLog', 'com.aioveu', '订单操作历史记录', 'OmsOrderLog', '可我不敌可爱', 149, '2026-01-10 16:33:09', '2026-01-10 16:33:09', b'0');
INSERT INTO `gen_config` VALUES (8, 'oms_order_pay', 'aioveuMallOmsOrderPay', 'com.aioveu', '支付信息', 'OmsOrderPay', '可我不敌可爱', 149, '2026-01-10 16:53:26', '2026-01-10 16:53:26', b'0');
INSERT INTO `gen_config` VALUES (9, 'oms_order_setting', 'aioveuMallOmsOrderSetting', 'com.aioveu', '订单配置信息', 'OmsOrderSetting', '可我不敌可爱', 149, '2026-01-10 17:10:13', '2026-01-10 17:10:13', b'0');
INSERT INTO `gen_config` VALUES (10, 'undo_log', 'aioveuMallOmsUndoLog', 'com.aioveu', 'AT transaction mode undo table', 'UndoLog', '可我不敌可爱', 149, '2026-01-10 17:30:28', '2026-01-10 17:30:28', b'0');
INSERT INTO `gen_config` VALUES (11, 'pms_brand', 'aioveuMallPmsBrand', 'com.aioveu', '商品品牌', 'PmsBrand', '可我不敌可爱', 150, '2026-01-10 18:49:22', '2026-01-10 18:49:22', b'0');
INSERT INTO `gen_config` VALUES (12, 'pms_category', 'aioveuMallPmsCategory', 'com.aioveu', '商品分类', 'PmsCategory', '可我不敌可爱', 150, '2026-01-11 17:25:11', '2026-01-11 17:25:11', b'0');
INSERT INTO `gen_config` VALUES (13, 'pms_category_attribute', 'aioveuMallPmsCategoryAttribute', 'com.aioveu', '商品类型（规格，属性）', 'PmsCategoryAttribute', '可我不敌可爱', 150, '2026-01-11 19:35:19', '2026-01-11 19:35:19', b'0');
INSERT INTO `gen_config` VALUES (14, 'pms_category_brand', 'aioveuMallPmsCategoryBrand', 'com.aioveu', '商品分类与品牌关联表', 'PmsCategoryBrand', '可我不敌可爱', 150, '2026-01-11 20:00:49', '2026-01-11 20:00:49', b'0');
INSERT INTO `gen_config` VALUES (15, 'pms_sku', 'aioveuMallPmsSku', 'com.aioveu', '商品库存', 'PmsSku', '可我不敌可爱', 150, '2026-01-11 20:56:24', '2026-01-11 20:56:24', b'0');
INSERT INTO `gen_config` VALUES (16, 'pms_spu', 'aioveuMallPmsSpu', 'com.aioveu', '商品', 'PmsSpu', '可我不敌可爱', 150, '2026-01-11 21:21:13', '2026-01-11 21:21:13', b'0');
INSERT INTO `gen_config` VALUES (17, 'pms_spu_attribute', 'aioveuMallPmsSpuAttribute', 'com.aioveu', '商品类型（属性/规格）', 'PmsSpuAttribute', '可我不敌可爱', 150, '2026-01-11 21:58:59', '2026-01-11 21:58:59', b'0');
INSERT INTO `gen_config` VALUES (18, 'sms_advert', 'aioveuMallSmsAdvert', 'com.aioveu', '广告', 'SmsAdvert', '可我不敌可爱', 151, '2026-01-12 10:34:19', '2026-01-12 10:34:19', b'0');
INSERT INTO `gen_config` VALUES (19, 'sms_coupon', 'aioveuMallSmsCoupon', 'com.aioveu', '优惠券', 'SmsCoupon', '可我不敌可爱', 151, '2026-01-12 11:03:17', '2026-01-12 11:03:17', b'0');
INSERT INTO `gen_config` VALUES (20, 'sms_coupon_history', 'aioveuMallSmsCouponHistory', 'com.aioveu', '优惠券领取/使用记录', 'SmsCouponHistory', '可我不敌可爱', 151, '2026-01-12 11:49:08', '2026-01-12 11:49:08', b'0');
INSERT INTO `gen_config` VALUES (21, 'sms_coupon_spu', 'aioveuMallSmsCouponSpu', 'com.aioveu', '优惠券适用的具体商品', 'SmsCouponSpu', '可我不敌可爱', 151, '2026-01-12 12:10:39', '2026-01-12 12:10:39', b'0');
INSERT INTO `gen_config` VALUES (22, 'sms_coupon_spu_category', 'aioveuMallSmsCouponSpuCategory', 'com.aioveu', '优惠券适用的具体分类', 'SmsCouponSpuCategory', '可我不敌可爱', 151, '2026-01-12 13:03:27', '2026-01-12 13:03:27', b'0');
INSERT INTO `gen_config` VALUES (23, 'ums_member', 'aioveuMallUmsMember', 'com.aioveu', '会员', 'UmsMember', '可我不敌可爱', 152, '2026-01-12 14:35:51', '2026-01-12 14:35:51', b'0');
INSERT INTO `gen_config` VALUES (24, 'ums_member_address', 'aioveuMallUmsMemberAddress', 'com.aioveu', '会员收货地址', 'UmsMemberAddress', '可我不敌可爱', 152, '2026-01-12 15:22:21', '2026-01-12 15:22:21', b'0');
INSERT INTO `gen_config` VALUES (25, 'refund_order', 'aioveuMallRefundOrder', 'com.aioveu', '订单退款申请', 'RefundOrder', '可我不敌可爱', 260, '2026-01-31 16:16:35', '2026-01-31 16:16:35', b'0');
INSERT INTO `gen_config` VALUES (26, 'refund_item', 'aioveuMallRefundItem', 'com.aioveu', '退款商品明细', 'RefundItem', '可我不敌可爱', 260, '2026-01-31 16:39:07', '2026-01-31 16:39:07', b'0');
INSERT INTO `gen_config` VALUES (27, 'refund_delivery', 'aioveuMallRefundDelivery', 'com.aioveu', '退款物流信息（用于退货）', 'RefundDelivery', '可我不敌可爱', 260, '2026-01-31 17:56:18', '2026-01-31 17:56:18', b'0');
INSERT INTO `gen_config` VALUES (28, 'refund_operation_log', 'aioveuMallRefundOperationLog', 'com.aioveu', '退款操作记录（用于审计）', 'RefundOperationLog', '可我不敌可爱', 260, '2026-01-31 18:13:51', '2026-01-31 18:13:51', b'0');
INSERT INTO `gen_config` VALUES (29, 'refund_proof', 'aioveuMallRefundProof', 'com.aioveu', '退款凭证图片', 'RefundProof', '可我不敌可爱', 260, '2026-02-01 12:35:36', '2026-02-01 12:35:36', b'0');
INSERT INTO `gen_config` VALUES (30, 'refund_payment', 'aioveuMallRefundPayment', 'com.aioveu', '退款支付记录', 'RefundPayment', '可我不敌可爱', 260, '2026-02-01 13:24:53', '2026-02-01 13:24:53', b'0');
INSERT INTO `gen_config` VALUES (31, 'refund_reason', 'aioveuMallRefundReason', 'com.aioveu', '退款原因分类', 'RefundReason', '可我不敌可爱', 260, '2026-02-01 13:51:04', '2026-02-01 13:51:04', b'0');
INSERT INTO `gen_config` VALUES (32, 'pay_order', 'aioveuMallPayOrder', 'com.aioveu', '支付订单', 'PayOrder', '可我不敌可爱', 296, '2026-02-02 17:18:49', '2026-02-02 17:18:49', b'0');
INSERT INTO `gen_config` VALUES (33, 'pay_refund_record', 'aioveuMallPayRefundRecord', 'com.aioveu', '退款记录', 'PayRefundRecord', '可我不敌可爱', 296, '2026-02-02 18:41:32', '2026-02-02 18:41:32', b'0');
INSERT INTO `gen_config` VALUES (34, 'pay_channel_config', 'aioveuMallPayChannelConfig', 'com.aioveu', '支付渠道配置', 'PayChannelConfig', '可我不敌可爱', 296, '2026-02-02 19:40:23', '2026-02-02 19:40:23', b'0');
INSERT INTO `gen_config` VALUES (35, 'pay_reconciliation', 'aioveuMallPayReconciliation', 'com.aioveu', '支付对账', 'PayReconciliation', '可我不敌可爱', 296, '2026-02-02 20:27:14', '2026-02-02 20:27:14', b'0');
INSERT INTO `gen_config` VALUES (37, 'pay_reconciliation_detail', 'aioveuMallPayReconciliationDetail', 'com.aioveu', '对账明细', 'PayReconciliationDetail', '可我不敌可爱', 296, '2026-02-09 13:56:17', '2026-02-09 13:56:17', b'0');
INSERT INTO `gen_config` VALUES (38, 'pay_flow', 'aioveuMallPayFlow', 'com.aioveu', '支付流水', 'PayFlow', '可我不敌可爱', 296, '2026-02-09 15:45:07', '2026-02-09 15:45:07', b'0');
INSERT INTO `gen_config` VALUES (39, 'pay_notify', 'aioveuMallPayNotify', 'com.aioveu', '支付通知', 'PayNotify', '可我不敌可爱', 296, '2026-02-09 16:08:01', '2026-02-09 16:08:01', b'0');
INSERT INTO `gen_config` VALUES (40, 'pay_account', 'aioveuMallPayAccount', 'com.aioveu', '支付账户', 'PayAccount', '可我不敌可爱', 296, '2026-02-10 16:05:59', '2026-02-10 16:05:59', b'0');
INSERT INTO `gen_config` VALUES (41, 'pay_account_flow', 'aioveuMallPayAccountFlow', 'com.aioveu', '账户流水', 'PayAccountFlow', '可我不敌可爱', 296, '2026-02-10 16:18:25', '2026-02-10 16:18:25', b'0');

-- ----------------------------
-- Table structure for gen_field_config
-- ----------------------------
DROP TABLE IF EXISTS `gen_field_config`;
CREATE TABLE `gen_field_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_id` bigint NOT NULL COMMENT '关联的配置ID',
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
  INDEX `config_id`(`config_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 586 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '代码生成字段配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of gen_field_config
-- ----------------------------
INSERT INTO `gen_field_config` VALUES (1, 1, 'id', 'bigint', NULL, 'id', 'Long', 1, 'id', NULL, 0, 0, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (2, 1, 'order_sn', 'varchar', NULL, 'orderSn', 'String', 2, '订单号', 64, 1, 1, 1, 0, 1, 1, 'notice_type', '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (3, 1, 'total_amount', 'bigint', NULL, 'totalAmount', 'Long', 3, '订单总额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (4, 1, 'total_quantity', 'int', NULL, 'totalQuantity', 'Integer', 4, '商品总数', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (5, 1, 'source', 'tinyint', NULL, 'source', 'Integer', 5, '订单来源(1:APP；2:网页)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (6, 1, 'status', 'int', NULL, 'status', 'Integer', 6, '订单状态：\r\n101->待付款；\r\n102->用户取消；\r\n103->系统取消；\r\n201->已付款；\r\n202->申请退款；\r\n203->已退款；\r\n301->待发货；\r\n401->已发货；\r\n501->用户收货；\r\n502->系统收货；\r\n901->已完成；', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (7, 1, 'remark', 'varchar', NULL, 'remark', 'String', 7, '订单备注', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (8, 1, 'member_id', 'bigint', NULL, 'memberId', 'Long', 8, '会员id', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (9, 1, 'coupon_id', 'bigint', NULL, 'couponId', 'Long', 9, '使用的优惠券', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (10, 1, 'coupon_amount', 'bigint', NULL, 'couponAmount', 'Long', 10, '优惠券抵扣金额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (11, 1, 'freight_amount', 'bigint', NULL, 'freightAmount', 'Long', 11, '运费金额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (12, 1, 'payment_amount', 'bigint', NULL, 'paymentAmount', 'Long', 12, '应付总额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (13, 1, 'payment_time', 'datetime', NULL, 'paymentTime', 'LocalDateTime', 13, '支付时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (14, 1, 'payment_method', 'tinyint', NULL, 'paymentMethod', 'Integer', 14, '支付方式(1：微信JSAPI；2：支付宝；3：余额；4：微信APP)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (15, 1, 'out_trade_no', 'varchar', NULL, 'outTradeNo', 'String', 15, '微信支付等第三方支付平台的商户订单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (16, 1, 'transaction_id', 'varchar', NULL, 'transactionId', 'String', 16, '微信支付订单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (17, 1, 'out_refund_no', 'varchar', NULL, 'outRefundNo', 'String', 17, '商户退款单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (18, 1, 'refund_id', 'varchar', NULL, 'refundId', 'String', 18, '微信退款单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (19, 1, 'delivery_time', 'datetime', NULL, 'deliveryTime', 'LocalDateTime', 19, '发货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (20, 1, 'receive_time', 'datetime', NULL, 'receiveTime', 'LocalDateTime', 20, '确认收货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (21, 1, 'comment_time', 'datetime', NULL, 'commentTime', 'LocalDateTime', 21, '评价时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (22, 1, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 22, '逻辑删除【0->正常；1->已删除】', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (23, 1, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 23, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (24, 1, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 24, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-07 17:41:45', '2026-01-07 17:41:45');
INSERT INTO `gen_field_config` VALUES (49, 4, 'id', 'bigint', NULL, 'id', 'Long', 1, 'id', NULL, 1, 0, 0, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (50, 4, 'order_sn', 'varchar', NULL, 'orderSn', 'String', 2, '订单号', 64, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (51, 4, 'total_amount', 'bigint', NULL, 'totalAmount', 'Long', 3, '订单总额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (52, 4, 'total_quantity', 'int', NULL, 'totalQuantity', 'Integer', 4, '商品总数', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (53, 4, 'source', 'tinyint', NULL, 'source', 'Integer', 5, '订单来源(1:APP；2:网页)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (54, 4, 'status', 'int', NULL, 'status', 'Integer', 6, '订单状态：\r\n101->待付款；\r\n102->用户取消；\r\n103->系统取消；\r\n201->已付款；\r\n202->申请退款；\r\n203->已退款；\r\n301->待发货；\r\n401->已发货；\r\n501->用户收货；\r\n502->系统收货；\r\n901->已完成；', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (55, 4, 'remark', 'varchar', NULL, 'remark', 'String', 7, '订单备注', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (56, 4, 'member_id', 'bigint', NULL, 'memberId', 'Long', 8, '会员id', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (57, 4, 'coupon_id', 'bigint', NULL, 'couponId', 'Long', 9, '使用的优惠券', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (58, 4, 'coupon_amount', 'bigint', NULL, 'couponAmount', 'Long', 10, '优惠券抵扣金额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (59, 4, 'freight_amount', 'bigint', NULL, 'freightAmount', 'Long', 11, '运费金额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (60, 4, 'payment_amount', 'bigint', NULL, 'paymentAmount', 'Long', 12, '应付总额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (61, 4, 'payment_time', 'datetime', NULL, 'paymentTime', 'LocalDateTime', 13, '支付时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (62, 4, 'payment_method', 'tinyint', NULL, 'paymentMethod', 'Integer', 14, '支付方式(1：微信JSAPI；2：支付宝；3：余额；4：微信APP)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (63, 4, 'out_trade_no', 'varchar', NULL, 'outTradeNo', 'String', 15, '微信支付等第三方支付平台的商户订单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (64, 4, 'transaction_id', 'varchar', NULL, 'transactionId', 'String', 16, '微信支付订单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (65, 4, 'out_refund_no', 'varchar', NULL, 'outRefundNo', 'String', 17, '商户退款单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (66, 4, 'refund_id', 'varchar', NULL, 'refundId', 'String', 18, '微信退款单号', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (67, 4, 'delivery_time', 'datetime', NULL, 'deliveryTime', 'LocalDateTime', 19, '发货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (68, 4, 'receive_time', 'datetime', NULL, 'receiveTime', 'LocalDateTime', 20, '确认收货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (69, 4, 'comment_time', 'datetime', NULL, 'commentTime', 'LocalDateTime', 21, '评价时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (70, 4, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 22, '逻辑删除【0->正常；1->已删除】', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (71, 4, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 23, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (72, 4, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 24, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-07 18:11:10', '2026-01-07 18:11:10');
INSERT INTO `gen_field_config` VALUES (73, 5, 'id', 'bigint', NULL, 'id', 'Long', 1, 'id', NULL, 1, 0, 0, 0, 1, 1, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (74, 5, 'order_id', 'bigint', NULL, 'orderId', 'Long', 2, '订单ID', NULL, 1, 1, 0, 0, 1, 1, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (75, 5, 'spu_name', 'varchar', NULL, 'spuName', 'String', 3, '商品名称', 128, 0, 1, 1, 1, 11, 1, 'gender', '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (76, 5, 'sku_id', 'bigint', NULL, 'skuId', 'Long', 4, '商品ID', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (77, 5, 'sku_sn', 'varchar', NULL, 'skuSn', 'String', 5, '商品编码', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (78, 5, 'sku_name', 'varchar', NULL, 'skuName', 'String', 6, '规格名称', 128, 1, 1, 1, 1, 11, 1, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (79, 5, 'pic_url', 'varchar', NULL, 'picUrl', 'String', 7, '商品图片', 255, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (80, 5, 'price', 'bigint', NULL, 'price', 'Long', 8, '商品单价(单位：分)', NULL, 1, 1, 1, 0, 1, 5, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (81, 5, 'quantity', 'int', NULL, 'quantity', 'Integer', 9, '商品数量', NULL, 0, 1, 1, 0, 1, 5, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (82, 5, 'total_amount', 'bigint', NULL, 'totalAmount', 'Long', 10, '商品总价(单位：分)', NULL, 1, 1, 1, 0, 1, 5, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (83, 5, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 11, '逻辑删除标识(1:已删除；0:正常)', NULL, 0, 1, 1, 1, 11, 6, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (84, 5, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 12, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (85, 5, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 13, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-08 19:33:00', '2026-01-08 19:33:00');
INSERT INTO `gen_field_config` VALUES (86, 6, 'id', 'bigint', NULL, 'id', 'Long', 1, 'id', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (87, 6, 'order_id', 'bigint', NULL, 'orderId', 'Long', 2, '订单id', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (88, 6, 'delivery_company', 'varchar', NULL, 'deliveryCompany', 'String', 3, '物流公司(配送方式)', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (89, 6, 'delivery_sn', 'varchar', NULL, 'deliverySn', 'String', 4, '物流单号', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (90, 6, 'receiver_name', 'varchar', NULL, 'receiverName', 'String', 5, '收货人姓名', 100, 1, 1, 1, 1, 11, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (91, 6, 'receiver_phone', 'varchar', NULL, 'receiverPhone', 'String', 6, '收货人电话', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (92, 6, 'receiver_post_code', 'varchar', NULL, 'receiverPostCode', 'String', 7, '收货人邮编', 32, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (93, 6, 'receiver_province', 'varchar', NULL, 'receiverProvince', 'String', 8, '省份/直辖市', 32, 1, 1, 1, 0, 1, 4, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (94, 6, 'receiver_city', 'varchar', NULL, 'receiverCity', 'String', 9, '城市', 32, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (95, 6, 'receiver_region', 'varchar', NULL, 'receiverRegion', 'String', 10, '区', 32, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (96, 6, 'receiver_detail_address', 'varchar', NULL, 'receiverDetailAddress', 'String', 11, '详细地址', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (97, 6, 'remark', 'varchar', NULL, 'remark', 'String', 12, '备注', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (98, 6, 'delivery_status', 'tinyint', NULL, 'deliveryStatus', 'Integer', 13, '物流状态【0->运输中；1->已收货】', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (99, 6, 'delivery_time', 'datetime', NULL, 'deliveryTime', 'LocalDateTime', 14, '发货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (100, 6, 'receive_time', 'datetime', NULL, 'receiveTime', 'LocalDateTime', 15, '确认收货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (101, 6, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 16, '逻辑删除【0->正常；1->已删除】', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (102, 6, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 17, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (103, 6, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 18, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38');
INSERT INTO `gen_field_config` VALUES (104, 7, 'id', 'bigint', NULL, 'id', 'Long', 1, 'id', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (105, 7, 'order_id', 'bigint', NULL, 'orderId', 'Long', 2, '订单id', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (106, 7, 'user', 'varchar', NULL, 'user', 'String', 3, '操作人[用户；系统；后台管理员]', 100, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (107, 7, 'detail', 'varchar', NULL, 'detail', 'String', 4, '操作详情', 255, 1, 1, 1, 0, 1, 1, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (108, 7, 'order_status', 'int', NULL, 'orderStatus', 'Integer', 5, '操作时订单状态', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (109, 7, 'remark', 'varchar', NULL, 'remark', 'String', 6, '备注', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (110, 7, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 7, '逻辑删除【0->正常；1->已删除】', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (111, 7, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 8, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (112, 7, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 9, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10');
INSERT INTO `gen_field_config` VALUES (113, 8, 'id', 'bigint', NULL, 'id', 'Long', 1, 'id', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (114, 8, 'order_id', 'bigint', NULL, 'orderId', 'Long', 2, '订单id', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (115, 8, 'pay_sn', 'varchar', NULL, 'paySn', 'String', 3, '支付流水号', 128, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (116, 8, 'pay_amount', 'bigint', NULL, 'payAmount', 'Long', 4, '应付总额(分)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (117, 8, 'pay_time', 'datetime', NULL, 'payTime', 'LocalDateTime', 5, '支付时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (118, 8, 'pay_type', 'tinyint', NULL, 'payType', 'Integer', 6, '支付方式【1->支付宝；2->微信；3->银联； 4->货到付款；】', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (119, 8, 'pay_status', 'tinyint', NULL, 'payStatus', 'Integer', 7, '支付状态', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (120, 8, 'confirm_time', 'datetime', NULL, 'confirmTime', 'LocalDateTime', 8, '确认时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (121, 8, 'callback_content', 'varchar', NULL, 'callbackContent', 'String', 9, '回调内容', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (122, 8, 'callback_time', 'datetime', NULL, 'callbackTime', 'LocalDateTime', 10, '回调时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (123, 8, 'pay_subject', 'varchar', NULL, 'paySubject', 'String', 11, '交易内容', 200, 1, 1, 1, 0, 1, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (124, 8, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 12, '逻辑删除【0->正常；1->已删除】', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (125, 8, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 13, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (126, 8, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 14, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 16:53:27', '2026-01-10 16:53:27');
INSERT INTO `gen_field_config` VALUES (127, 9, 'id', 'bigint', NULL, 'id', 'Long', 1, 'id', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (128, 9, 'flash_order_overtime', 'int', NULL, 'flashOrderOvertime', 'Integer', 2, '秒杀订单超时关闭时间(分)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (129, 9, 'normal_order_overtime', 'int', NULL, 'normalOrderOvertime', 'Integer', 3, '正常订单超时时间(分)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (130, 9, 'confirm_overtime', 'int', NULL, 'confirmOvertime', 'Integer', 4, '发货后自动确认收货时间（天）', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (131, 9, 'finish_overtime', 'int', NULL, 'finishOvertime', 'Integer', 5, '自动完成交易时间，不能申请退货（天）', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (132, 9, 'comment_overtime', 'int', NULL, 'commentOvertime', 'Integer', 6, '订单完成后自动好评时间（天）', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (133, 9, 'member_level', 'tinyint', NULL, 'memberLevel', 'Integer', 7, '会员等级【0-不限会员等级，全部通用；其他-对应的其他会员等级】', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (134, 9, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 8, '逻辑删除【0->正常；1->已删除】', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (135, 9, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 9, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (136, 9, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 10, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14');
INSERT INTO `gen_field_config` VALUES (137, 10, 'branch_id', 'bigint', NULL, 'branchId', 'Long', 1, '分支事务的唯一标识。branch transaction id', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:30:30', '2026-01-10 17:30:30');
INSERT INTO `gen_field_config` VALUES (138, 10, 'xid', 'varchar', NULL, 'xid', 'String', 2, '全局事务的唯一标识。global transaction id', 100, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:30:30', '2026-01-10 17:30:30');
INSERT INTO `gen_field_config` VALUES (139, 10, 'context', 'varchar', NULL, 'context', 'String', 3, '记录上下文信息 undo_log context,such as serialization', 128, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:30:30', '2026-01-10 17:30:30');
INSERT INTO `gen_field_config` VALUES (140, 10, 'rollback_info', 'longblob', NULL, 'rollbackInfo', NULL, 4, '核心字段。存储序列化后的回滚数据，包含前后镜像（beforeImage和afterImage）。rollback info', -1, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:30:30', '2026-01-10 17:30:30');
INSERT INTO `gen_field_config` VALUES (141, 10, 'log_status', 'int', NULL, 'logStatus', 'Integer', 5, '日志状态。0：正常状态；1：全局事务已完成，用于防悬挂（防止第二阶段回滚请求在一阶段日志产生前到达）0:normal status,1:defense status', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 17:30:30', '2026-01-10 17:30:30');
INSERT INTO `gen_field_config` VALUES (142, 10, 'log_created', 'datetime', NULL, 'logCreated', 'LocalDateTime', 6, '记录的创建时间create datetime', NULL, 1, 1, 0, 0, 1, 9, NULL, '2026-01-10 17:30:30', '2026-01-10 17:30:30');
INSERT INTO `gen_field_config` VALUES (143, 10, 'log_modified', 'datetime', NULL, 'logModified', 'LocalDateTime', 7, '记录的最后修改时间。modify datetime', NULL, 1, 1, 0, 0, 1, 9, NULL, '2026-01-10 17:30:30', '2026-01-10 17:30:30');
INSERT INTO `gen_field_config` VALUES (144, 11, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22');
INSERT INTO `gen_field_config` VALUES (145, 11, 'name', 'varchar', NULL, 'name', 'String', 2, '品牌名称', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22');
INSERT INTO `gen_field_config` VALUES (146, 11, 'logo_url', 'varchar', NULL, 'logoUrl', 'String', 3, 'LOGO图片', 255, 1, 1, 1, 0, 1, 1, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22');
INSERT INTO `gen_field_config` VALUES (147, 11, 'sort', 'int', NULL, 'sort', 'Integer', 4, '排序', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22');
INSERT INTO `gen_field_config` VALUES (148, 11, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 5, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22');
INSERT INTO `gen_field_config` VALUES (149, 11, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 6, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22');
INSERT INTO `gen_field_config` VALUES (150, 12, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (151, 12, 'name', 'varchar', NULL, 'name', 'String', 2, '商品分类名称', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (152, 12, 'parent_id', 'bigint', NULL, 'parentId', 'Long', 3, '父级ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (153, 12, 'level', 'int', NULL, 'level', 'Integer', 4, '层级', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (154, 12, 'icon_url', 'varchar', NULL, 'iconUrl', 'String', 5, '图标地址', 255, 0, 1, 1, 1, 11, 1, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (155, 12, 'sort', 'int', NULL, 'sort', 'Integer', 6, '排序', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (156, 12, 'visible', 'tinyint', NULL, 'visible', 'Integer', 7, '显示状态:( 0:隐藏 1:显示)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (157, 12, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 8, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (158, 12, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 9, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13');
INSERT INTO `gen_field_config` VALUES (159, 13, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 19:35:20', '2026-01-11 19:35:20');
INSERT INTO `gen_field_config` VALUES (160, 13, 'category_id', 'bigint', NULL, 'categoryId', 'Long', 2, '分类ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 19:35:20', '2026-01-11 19:35:20');
INSERT INTO `gen_field_config` VALUES (161, 13, 'name', 'varchar', NULL, 'name', 'String', 3, '属性名称', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 19:35:20', '2026-01-11 19:35:20');
INSERT INTO `gen_field_config` VALUES (162, 13, 'type', 'tinyint', NULL, 'type', 'Integer', 4, '类型(1:规格;2:属性;)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 19:35:20', '2026-01-11 19:35:20');
INSERT INTO `gen_field_config` VALUES (163, 13, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 5, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 19:35:20', '2026-01-11 19:35:20');
INSERT INTO `gen_field_config` VALUES (164, 13, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 6, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 19:35:20', '2026-01-11 19:35:20');
INSERT INTO `gen_field_config` VALUES (165, 14, 'category_id', 'bigint', NULL, 'categoryId', 'Long', 1, '商品分类', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 20:00:50', '2026-01-11 20:00:50');
INSERT INTO `gen_field_config` VALUES (166, 14, 'brand_id', 'bigint', NULL, 'brandId', 'Long', 2, '商品品牌', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 20:00:50', '2026-01-11 20:00:50');
INSERT INTO `gen_field_config` VALUES (167, 15, 'id', 'bigint', NULL, 'id', 'Long', 1, '库存ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (168, 15, 'sku_sn', 'varchar', NULL, 'skuSn', 'String', 2, '商品编码', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (169, 15, 'spu_id', 'bigint', NULL, 'spuId', 'Long', 3, 'SPU ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (170, 15, 'name', 'varchar', NULL, 'name', 'String', 4, '商品名称', 128, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (171, 15, 'spec_ids', 'varchar', NULL, 'specIds', 'String', 5, '商品规格值，以英文逗号(,)分割', 255, 0, 1, 1, 1, 11, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (172, 15, 'price', 'bigint', NULL, 'price', 'Long', 6, '商品价格(单位：分)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (173, 15, 'stock', 'int', NULL, 'stock', 'Integer', 7, '库存数量', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (174, 15, 'locked_stock', 'int', NULL, 'lockedStock', 'Integer', 8, '库存锁定数量', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (175, 15, 'pic_url', 'varchar', NULL, 'picUrl', 'String', 9, '商品图片地址', 255, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (176, 15, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 10, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (177, 15, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 11, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25');
INSERT INTO `gen_field_config` VALUES (178, 16, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (179, 16, 'name', 'varchar', NULL, 'name', 'String', 2, '商品名称', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (180, 16, 'category_id', 'bigint', NULL, 'categoryId', 'Long', 3, '商品类型ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (181, 16, 'brand_id', 'bigint', NULL, 'brandId', 'Long', 4, '商品品牌ID', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (182, 16, 'origin_price', 'bigint', NULL, 'originPrice', 'Long', 5, '原价【起】', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (183, 16, 'price', 'bigint', NULL, 'price', 'Long', 6, '现价【起】', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (184, 16, 'sales', 'int', NULL, 'sales', 'Integer', 7, '销量', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (185, 16, 'pic_url', 'varchar', NULL, 'picUrl', 'String', 8, '商品主图', 255, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (186, 16, 'album', 'json', NULL, 'album', 'String', 9, '商品图册', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (187, 16, 'unit', 'varchar', NULL, 'unit', 'String', 10, '单位', 16, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (188, 16, 'description', 'varchar', NULL, 'description', 'String', 11, '商品简介', 255, 0, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (189, 16, 'detail', 'text', NULL, 'detail', 'String', 12, '商品详情', 65535, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (190, 16, 'status', 'tinyint', NULL, 'status', 'Integer', 13, '商品状态(0:下架 1:上架)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (191, 16, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 14, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (192, 16, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 15, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14');
INSERT INTO `gen_field_config` VALUES (193, 17, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (194, 17, 'spu_id', 'bigint', NULL, 'spuId', 'Long', 2, '产品ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (195, 17, 'attribute_id', 'bigint', NULL, 'attributeId', 'Long', 3, '属性ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (196, 17, 'name', 'varchar', NULL, 'name', 'String', 4, '属性名称', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (197, 17, 'value', 'varchar', NULL, 'value', 'String', 5, '属性值', 128, 1, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (198, 17, 'type', 'tinyint', NULL, 'type', 'Integer', 6, '类型(1:规格;2:属性;)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (199, 17, 'pic_url', 'varchar', NULL, 'picUrl', 'String', 7, '规格图片', 255, 0, 1, 1, 0, 1, 1, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (200, 17, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 8, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (201, 17, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 9, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00');
INSERT INTO `gen_field_config` VALUES (202, 18, 'id', 'bigint', NULL, 'id', 'Long', 1, '广告ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (203, 18, 'title', 'varchar', NULL, 'title', 'String', 2, '广告标题', 100, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (204, 18, 'image_url', 'varchar', NULL, 'imageUrl', 'String', 3, '图片地址', 255, 1, 1, 1, 0, 1, 1, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (205, 18, 'start_time', 'datetime', NULL, 'startTime', 'LocalDateTime', 4, '开始时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (206, 18, 'end_time', 'datetime', NULL, 'endTime', 'LocalDateTime', 5, '结束时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (207, 18, 'status', 'tinyint', NULL, 'status', 'Integer', 6, '状态(1:开启；0:关闭)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (208, 18, 'sort', 'int', NULL, 'sort', 'Integer', 7, '排序', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (209, 18, 'redirect_url', 'varchar', NULL, 'redirectUrl', 'String', 8, '跳转链接', 255, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (210, 18, 'remark', 'varchar', NULL, 'remark', 'String', 9, '备注', 255, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (211, 18, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 10, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (212, 18, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 11, '更新时间(新增有值)', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20');
INSERT INTO `gen_field_config` VALUES (213, 19, 'id', 'bigint', NULL, 'id', 'Long', 1, 'ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (214, 19, 'name', 'varchar', NULL, 'name', 'String', 2, '优惠券名称', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (215, 19, 'type', 'tinyint', NULL, 'type', 'Integer', 3, '优惠券类型(1-满减券;2-直减券;3-折扣券)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (216, 19, 'code', 'varchar', NULL, 'code', 'String', 4, '优惠券码', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (217, 19, 'platform', 'int', NULL, 'platform', 'Integer', 5, '使用平台(0-全平台;1-APP;2-PC)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (218, 19, 'face_value_type', 'tinyint', NULL, 'faceValueType', 'Integer', 6, '优惠券面值类型', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (219, 19, 'face_value', 'bigint', NULL, 'faceValue', 'Long', 7, '优惠券面值(分)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (220, 19, 'discount', 'decimal', NULL, 'discount', 'BigDecimal', 8, '折扣', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (221, 19, 'min_point', 'bigint', NULL, 'minPoint', 'Long', 9, '使用门槛(0:无门槛)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (222, 19, 'per_limit', 'int', NULL, 'perLimit', 'Integer', 10, '每人限领张数(-1-无限制)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (223, 19, 'validity_period_type', 'tinyint', NULL, 'validityPeriodType', 'Integer', 11, '有效期类型(1:自领取时起有效天数;2:有效起止时间)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (224, 19, 'validity_days', 'int', NULL, 'validityDays', 'Integer', 12, '有效期天数', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (225, 19, 'validity_begin_time', 'datetime', NULL, 'validityBeginTime', 'LocalDateTime', 13, '有效期起始时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (226, 19, 'validity_end_time', 'datetime', NULL, 'validityEndTime', 'LocalDateTime', 14, '有效期截止时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (227, 19, 'application_scope', 'tinyint', NULL, 'applicationScope', 'Integer', 15, '应用范围(0-全场通用;1-指定商品分类;2-指定商品)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (228, 19, 'circulation', 'int', NULL, 'circulation', 'Integer', 16, '发行量(-1-无限制)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (229, 19, 'received_count', 'int', NULL, 'receivedCount', 'Integer', 17, '已领取的优惠券数量(统计)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (230, 19, 'used_count', 'int', NULL, 'usedCount', 'Integer', 18, '已使用的优惠券数量(统计)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (231, 19, 'remark', 'varchar', NULL, 'remark', 'String', 19, '备注', 255, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (232, 19, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 20, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (233, 19, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 21, '修改时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (234, 19, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 22, '逻辑删除标识(0-正常;1-删除)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18');
INSERT INTO `gen_field_config` VALUES (235, 20, 'id', 'bigint', NULL, 'id', 'Long', 1, '优惠券领取/使用记录ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (236, 20, 'coupon_id', 'bigint', NULL, 'couponId', 'Long', 2, '优惠券ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (237, 20, 'member_id', 'bigint', NULL, 'memberId', 'Long', 3, '会员ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (238, 20, 'member_nickname', 'varchar', NULL, 'memberNickname', 'String', 4, '会员昵称', 50, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (239, 20, 'coupon_code', 'varchar', NULL, 'couponCode', 'String', 5, '优惠券码', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (240, 20, 'get_type', 'tinyint', NULL, 'getType', 'Integer', 6, '获取类型(1：后台增删；2：主动领取)', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (241, 20, 'status', 'tinyint', NULL, 'status', 'Integer', 7, '状态(0：未使用；1：已使用；2：已过期)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (242, 20, 'use_time', 'datetime', NULL, 'useTime', 'LocalDateTime', 8, '使用时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (243, 20, 'order_id', 'bigint', NULL, 'orderId', 'Long', 9, '订单ID', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (244, 20, 'order_sn', 'varchar', NULL, 'orderSn', 'String', 10, '订单号', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (245, 20, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 11, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (246, 20, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 12, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09');
INSERT INTO `gen_field_config` VALUES (247, 21, 'id', 'bigint', NULL, 'id', 'Long', 1, '', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 12:10:40', '2026-01-12 12:10:40');
INSERT INTO `gen_field_config` VALUES (248, 21, 'coupon_id', 'bigint', NULL, 'couponId', 'Long', 2, '优惠券ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 12:10:40', '2026-01-12 12:10:40');
INSERT INTO `gen_field_config` VALUES (249, 21, 'spu_id', 'bigint', NULL, 'spuId', 'Long', 3, '商品ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 12:10:40', '2026-01-12 12:10:40');
INSERT INTO `gen_field_config` VALUES (250, 22, 'id', 'bigint', NULL, 'id', 'Long', 1, '', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 13:03:28', '2026-01-12 13:03:28');
INSERT INTO `gen_field_config` VALUES (251, 22, 'coupon_id', 'bigint', NULL, 'couponId', 'Long', 2, '优惠券ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 13:03:28', '2026-01-12 13:03:28');
INSERT INTO `gen_field_config` VALUES (252, 22, 'category_id', 'bigint', NULL, 'categoryId', 'Long', 3, '商品分类ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 13:03:28', '2026-01-12 13:03:28');
INSERT INTO `gen_field_config` VALUES (253, 23, 'id', 'bigint', NULL, 'id', 'Long', 1, '会员ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (254, 23, 'nick_name', 'varchar', NULL, 'nickName', 'String', 2, '昵称', 50, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (255, 23, 'mobile', 'varchar', NULL, 'mobile', 'String', 3, '手机号', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (256, 23, 'avatar_url', 'varchar', NULL, 'avatarUrl', 'String', 4, '头像URL', 255, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (257, 23, 'gender', 'tinyint', NULL, 'gender', 'Integer', 5, '性别(0=未知,1=男,2=女)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (258, 23, 'birthday', 'date', NULL, 'birthday', 'LocalDate', 6, '生日', NULL, 0, 1, 1, 1, 4, 8, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (259, 23, 'point', 'int', NULL, 'point', 'Integer', 7, '会员积分', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (260, 23, 'balance', 'bigint', NULL, 'balance', 'Long', 8, '账户余额(单位:分)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (261, 23, 'status', 'tinyint', NULL, 'status', 'Integer', 9, '状态(0=禁用,1=正常)', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (262, 23, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 10, '删除标志(0=未删除,1=已删除)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (263, 23, 'openid', 'char', NULL, 'openid', 'String', 11, '微信OpenID', 28, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (264, 23, 'session_key', 'varchar', NULL, 'sessionKey', 'String', 12, '微信会话密钥', 32, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (265, 23, 'country', 'varchar', NULL, 'country', 'String', 13, '国家', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (266, 23, 'province', 'varchar', NULL, 'province', 'String', 14, '省份', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (267, 23, 'city', 'varchar', NULL, 'city', 'String', 15, '城市', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (268, 23, 'language', 'varchar', NULL, 'language', 'String', 16, '语言', 10, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (269, 23, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 17, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (270, 23, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 18, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52');
INSERT INTO `gen_field_config` VALUES (271, 24, 'id', 'bigint', NULL, 'id', 'Long', 1, '地址ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (272, 24, 'member_id', 'bigint', NULL, 'memberId', 'Long', 2, '会员ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (273, 24, 'consignee_name', 'varchar', NULL, 'consigneeName', 'String', 3, '收货人姓名', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (274, 24, 'consignee_mobile', 'varchar', NULL, 'consigneeMobile', 'String', 4, '收货人联系方式', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (275, 24, 'country', 'varchar', NULL, 'country', 'String', 5, '国家', 50, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (276, 24, 'province', 'varchar', NULL, 'province', 'String', 6, '省份', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (277, 24, 'city', 'varchar', NULL, 'city', 'String', 7, '城市', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (278, 24, 'district', 'varchar', NULL, 'district', 'String', 8, '区/县', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (279, 24, 'street', 'varchar', NULL, 'street', 'String', 9, '街道', 200, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (280, 24, 'detail_address', 'varchar', NULL, 'detailAddress', 'String', 10, '详细地址', 255, 1, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (281, 24, 'postal_code', 'char', NULL, 'postalCode', 'String', 11, '邮政编码', 6, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (282, 24, 'defaulted', 'tinyint', NULL, 'defaulted', 'Integer', 12, '是否默认地址(0=否,1=是)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (283, 24, 'address_tag', 'varchar', NULL, 'addressTag', 'String', 13, '地址标签(家,公司,学校等)', 20, 0, 1, 1, 1, 11, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (284, 24, 'longitude', 'decimal', NULL, 'longitude', 'BigDecimal', 14, '经度', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (285, 24, 'latitude', 'decimal', NULL, 'latitude', 'BigDecimal', 15, '纬度', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (286, 24, 'status', 'tinyint', NULL, 'status', 'Integer', 16, '状态(0=已删除,1=正常)', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (287, 24, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 17, '创建时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (288, 24, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 18, '更新时间', NULL, 0, 1, 0, 0, 1, 9, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22');
INSERT INTO `gen_field_config` VALUES (289, 25, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (290, 25, 'order_id', 'bigint', NULL, 'orderId', 'Long', 2, '订单ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (291, 25, 'order_sn', 'varchar', NULL, 'orderSn', 'String', 3, '订单编号', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (292, 25, 'refund_sn', 'varchar', NULL, 'refundSn', 'String', 4, '退款单号', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (293, 25, 'user_id', 'bigint', NULL, 'userId', 'Long', 5, '用户ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (294, 25, 'refund_type', 'tinyint', NULL, 'refundType', 'Integer', 6, '退款类型：1-仅退款 2-退货退款 3-换货', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (295, 25, 'refund_reason', 'varchar', NULL, 'refundReason', 'String', 7, '退款原因', 200, 1, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (296, 25, 'description', 'varchar', NULL, 'description', 'String', 8, '补充说明', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (297, 25, 'proof_images', 'json', NULL, 'proofImages', 'String', 9, '退款凭证图片（JSON数组）', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (298, 25, 'refund_amount', 'decimal', NULL, 'refundAmount', 'BigDecimal', 10, '申请退款金额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (299, 25, 'actual_refund_amount', 'decimal', NULL, 'actualRefundAmount', 'BigDecimal', 11, '实际退款金额（分）', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (300, 25, 'status', 'tinyint', NULL, 'status', 'Integer', 12, '退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败', NULL, 1, 1, 1, 1, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (301, 25, 'handle_note', 'varchar', NULL, 'handleNote', 'String', 13, '处理备注', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (302, 25, 'handle_by', 'varchar', NULL, 'handleBy', 'String', 14, '处理人', 50, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (303, 25, 'handle_time', 'datetime', NULL, 'handleTime', 'LocalDateTime', 15, '处理时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (304, 25, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 16, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (305, 25, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 17, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (306, 25, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 18, '逻辑删除：0-正常 1-删除', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (307, 25, 'version', 'int', NULL, 'version', 'Integer', 19, '版本号（用于乐观锁）', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:16:37', '2026-01-31 16:16:37');
INSERT INTO `gen_field_config` VALUES (308, 26, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (309, 26, 'refund_id', 'bigint', NULL, 'refundId', 'Long', 2, '退款申请ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (310, 26, 'refund_type', 'tinyint', NULL, 'refundType', 'Integer', 3, '退款类型（冗余字段，与主表一致）', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (311, 26, 'order_item_id', 'bigint', NULL, 'orderItemId', 'Long', 4, '订单项ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (312, 26, 'spu_id', 'bigint', NULL, 'spuId', 'Long', 5, '商品ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (313, 26, 'spu_name', 'varchar', NULL, 'spuName', 'String', 6, '商品名称', 200, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (314, 26, 'sku_id', 'bigint', NULL, 'skuId', 'Long', 7, 'SKU ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (315, 26, 'sku_name', 'varchar', NULL, 'skuName', 'String', 8, 'SKU名称', 200, 0, 1, 1, 1, 11, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (316, 26, 'pic_url', 'varchar', NULL, 'picUrl', 'String', 9, '商品图片', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (317, 26, 'price', 'decimal', NULL, 'price', 'BigDecimal', 10, '商品单价（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (318, 26, 'quantity', 'int', NULL, 'quantity', 'Integer', 11, '退款数量', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (319, 26, 'refund_amount', 'decimal', NULL, 'refundAmount', 'BigDecimal', 12, '退款金额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (320, 26, 'refund_reason', 'varchar', NULL, 'refundReason', 'String', 13, '该商品的退款原因', 200, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (321, 26, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 14, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (322, 26, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 15, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (323, 26, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 16, '逻辑删除', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08');
INSERT INTO `gen_field_config` VALUES (324, 27, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (325, 27, 'refund_id', 'bigint', NULL, 'refundId', 'Long', 2, '退款申请ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (326, 27, 'delivery_type', 'tinyint', NULL, 'deliveryType', 'Integer', 3, '物流类型：1-买家发货 2-卖家发货 3-换货发货', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (327, 27, 'delivery_company', 'varchar', NULL, 'deliveryCompany', 'String', 4, '物流公司', 100, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (328, 27, 'delivery_sn', 'varchar', NULL, 'deliverySn', 'String', 5, '物流单号', 100, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (329, 27, 'receiver_name', 'varchar', NULL, 'receiverName', 'String', 6, '收货人姓名', 100, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (330, 27, 'receiver_phone', 'varchar', NULL, 'receiverPhone', 'String', 7, '收货人电话', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (331, 27, 'receiver_address', 'varchar', NULL, 'receiverAddress', 'String', 8, '收货地址', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (332, 27, 'delivery_time', 'datetime', NULL, 'deliveryTime', 'LocalDateTime', 9, '发货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (333, 27, 'receive_time', 'datetime', NULL, 'receiveTime', 'LocalDateTime', 10, '收货时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (334, 27, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 11, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (335, 27, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 12, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (336, 27, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 13, '逻辑删除', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19');
INSERT INTO `gen_field_config` VALUES (337, 28, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (338, 28, 'refund_id', 'bigint', NULL, 'refundId', 'Long', 2, '退款申请ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (339, 28, 'operation_type', 'tinyint', NULL, 'operationType', 'Integer', 3, '操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (340, 28, 'operation_content', 'varchar', NULL, 'operationContent', 'String', 4, '操作内容', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (341, 28, 'operator_id', 'bigint', NULL, 'operatorId', 'Long', 5, '操作人ID', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (342, 28, 'operator_name', 'varchar', NULL, 'operatorName', 'String', 6, '操作人名称', 50, 0, 1, 1, 1, 11, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (343, 28, 'operator_type', 'tinyint', NULL, 'operatorType', 'Integer', 7, '操作人类型：1-用户 2-客服 3-商家 4-系统', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (344, 28, 'before_status', 'tinyint', NULL, 'beforeStatus', 'Integer', 8, '操作前状态', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (345, 28, 'after_status', 'tinyint', NULL, 'afterStatus', 'Integer', 9, '操作后状态', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (346, 28, 'remark', 'varchar', NULL, 'remark', 'String', 10, '备注', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (347, 28, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 11, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52');
INSERT INTO `gen_field_config` VALUES (348, 29, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37');
INSERT INTO `gen_field_config` VALUES (349, 29, 'refund_id', 'bigint', NULL, 'refundId', 'Long', 2, '退款申请ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37');
INSERT INTO `gen_field_config` VALUES (350, 29, 'proof_type', 'tinyint', NULL, 'proofType', 'Integer', 3, '凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37');
INSERT INTO `gen_field_config` VALUES (351, 29, 'image_url', 'varchar', NULL, 'imageUrl', 'String', 4, '图片URL', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37');
INSERT INTO `gen_field_config` VALUES (352, 29, 'image_desc', 'varchar', NULL, 'imageDesc', 'String', 5, '图片描述', 200, 0, 1, 1, 1, 11, 1, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37');
INSERT INTO `gen_field_config` VALUES (353, 29, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 6, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37');
INSERT INTO `gen_field_config` VALUES (354, 29, 'deleted', 'tinyint', NULL, 'deleted', 'Integer', 7, '逻辑删除', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37');
INSERT INTO `gen_field_config` VALUES (355, 30, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (356, 30, 'refund_id', 'bigint', NULL, 'refundId', 'Long', 2, '退款申请ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (357, 30, 'payment_sn', 'varchar', NULL, 'paymentSn', 'String', 3, '退款支付单号', 64, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (358, 30, 'payment_type', 'tinyint', NULL, 'paymentType', 'Integer', 4, '支付类型：1-微信 2-支付宝 3-银行卡 4-余额', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (359, 30, 'payment_amount', 'decimal', NULL, 'paymentAmount', 'BigDecimal', 5, '支付金额（分）', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (360, 30, 'payment_status', 'tinyint', NULL, 'paymentStatus', 'Integer', 6, '支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (361, 30, 'payment_time', 'datetime', NULL, 'paymentTime', 'LocalDateTime', 7, '支付时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (362, 30, 'payment_channel', 'varchar', NULL, 'paymentChannel', 'String', 8, '支付渠道', 50, 0, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (363, 30, 'payment_trade_no', 'varchar', NULL, 'paymentTradeNo', 'String', 9, '支付交易号', 100, 0, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (364, 30, 'payment_fee', 'decimal', NULL, 'paymentFee', 'BigDecimal', 10, '支付手续费（分）', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (365, 30, 'remark', 'varchar', NULL, 'remark', 'String', 11, '备注', 200, 0, 1, 1, 0, 1, 1, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (366, 30, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 12, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (367, 30, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 13, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54');
INSERT INTO `gen_field_config` VALUES (368, 31, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05');
INSERT INTO `gen_field_config` VALUES (369, 31, 'reason_type', 'tinyint', NULL, 'reasonType', 'Integer', 2, '原因类型：1-仅退款原因 2-退货退款原因 3-换货原因', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05');
INSERT INTO `gen_field_config` VALUES (370, 31, 'reason_content', 'varchar', NULL, 'reasonContent', 'String', 3, '原因内容', 100, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05');
INSERT INTO `gen_field_config` VALUES (371, 31, 'sort', 'int', NULL, 'sort', 'Integer', 4, '排序', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05');
INSERT INTO `gen_field_config` VALUES (372, 31, 'status', 'tinyint', NULL, 'status', 'Integer', 5, '状态：0-禁用 1-启用', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05');
INSERT INTO `gen_field_config` VALUES (373, 31, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 6, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05');
INSERT INTO `gen_field_config` VALUES (374, 31, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 7, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05');
INSERT INTO `gen_field_config` VALUES (375, 32, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (376, 32, 'payment_no', 'varchar', NULL, 'paymentNo', 'String', 2, '支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (377, 32, 'order_no', 'varchar', NULL, 'orderNo', 'String', 3, '业务订单号（如退款单号、订单号）', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (378, 32, 'biz_type', 'varchar', NULL, 'bizType', 'String', 4, '业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (379, 32, 'user_id', 'bigint', NULL, 'userId', 'Long', 5, '用户ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (380, 32, 'payment_amount', 'decimal', NULL, 'paymentAmount', 'BigDecimal', 6, '支付/退款金额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (381, 32, 'payment_status', 'tinyint', NULL, 'paymentStatus', 'Integer', 7, '支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (382, 32, 'payment_channel', 'varchar', NULL, 'paymentChannel', 'String', 8, '支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (383, 32, 'payment_method', 'varchar', NULL, 'paymentMethod', 'String', 9, '支付方式：APP-APP支付 H5-H5支付 JSAPI-小程序/公众号 NATIVE-扫码支付', 20, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (384, 32, 'payment_time', 'datetime', NULL, 'paymentTime', 'LocalDateTime', 10, '支付时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (385, 32, 'payment_expire_time', 'datetime', NULL, 'paymentExpireTime', 'LocalDateTime', 11, '支付过期时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (386, 32, 'third_payment_no', 'varchar', NULL, 'thirdPaymentNo', 'String', 12, '第三方支付单号', 128, 0, 1, 1, 1, 11, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (387, 32, 'third_transaction_no', 'varchar', NULL, 'thirdTransactionNo', 'String', 13, '第三方交易流水号', 128, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (388, 32, 'attach_data', 'json', NULL, 'attachData', 'String', 14, '附加数据，JSON格式', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (389, 32, 'notify_url', 'varchar', NULL, 'notifyUrl', 'String', 15, '异步通知地址', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (390, 32, 'return_url', 'varchar', NULL, 'returnUrl', 'String', 16, '同步返回地址', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (391, 32, 'client_ip', 'varchar', NULL, 'clientIp', 'String', 17, '客户端IP', 50, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (392, 32, 'device_info', 'varchar', NULL, 'deviceInfo', 'String', 18, '设备信息', 200, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (393, 32, 'subject', 'varchar', NULL, 'subject', 'String', 19, '订单标题', 200, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (394, 32, 'body', 'varchar', NULL, 'body', 'String', 20, '订单描述', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (395, 32, 'currency', 'varchar', NULL, 'currency', 'String', 21, '币种，默认人民币', 3, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (396, 32, 'error_code', 'varchar', NULL, 'errorCode', 'String', 22, '错误代码', 50, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (397, 32, 'error_message', 'varchar', NULL, 'errorMessage', 'String', 23, '错误信息', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (398, 32, 'notify_status', 'tinyint', NULL, 'notifyStatus', 'Integer', 24, '通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (399, 32, 'notify_count', 'int', NULL, 'notifyCount', 'Integer', 25, '通知次数', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (400, 32, 'last_notify_time', 'datetime', NULL, 'lastNotifyTime', 'LocalDateTime', 26, '最后通知时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (401, 32, 'next_notify_time', 'datetime', NULL, 'nextNotifyTime', 'LocalDateTime', 27, '下次通知时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (402, 32, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 28, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (403, 32, 'create_by', 'varchar', NULL, 'createBy', 'String', 29, '创建人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (404, 32, 'update_by', 'varchar', NULL, 'updateBy', 'String', 30, '更新人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (405, 32, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 31, '创建时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (406, 32, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 32, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (407, 32, 'version', 'int', NULL, 'version', 'Integer', 33, '版本号（用于乐观锁）', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50');
INSERT INTO `gen_field_config` VALUES (408, 33, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (409, 33, 'refund_no', 'varchar', NULL, 'refundNo', 'String', 2, '退款单号，格式：RFyyyyMMddHHmmss+6位随机', 32, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (410, 33, 'payment_no', 'varchar', NULL, 'paymentNo', 'String', 3, '原支付单号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (411, 33, 'order_no', 'varchar', NULL, 'orderNo', 'String', 4, '业务订单号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (412, 33, 'biz_type', 'varchar', NULL, 'bizType', 'String', 5, '业务类型：REFUND-退款 ORDER-订单退款', 20, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (413, 33, 'refund_amount', 'decimal', NULL, 'refundAmount', 'BigDecimal', 6, '退款金额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (414, 33, 'refund_status', 'tinyint', NULL, 'refundStatus', 'Integer', 7, '退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (415, 33, 'refund_channel', 'varchar', NULL, 'refundChannel', 'String', 8, '退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (416, 33, 'refund_reason', 'varchar', NULL, 'refundReason', 'String', 9, '退款原因', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (417, 33, 'apply_time', 'datetime', NULL, 'applyTime', 'LocalDateTime', 10, '申请时间', NULL, 1, 1, 1, 0, 1, 9, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (418, 33, 'refund_time', 'datetime', NULL, 'refundTime', 'LocalDateTime', 11, '退款完成时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (419, 33, 'third_refund_no', 'varchar', NULL, 'thirdRefundNo', 'String', 12, '第三方退款单号', 128, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (420, 33, 'third_transaction_no', 'varchar', NULL, 'thirdTransactionNo', 'String', 13, '第三方退款流水号', 128, 0, 1, 1, 1, 11, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (421, 33, 'currency', 'varchar', NULL, 'currency', 'String', 14, '币种', 3, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (422, 33, 'error_code', 'varchar', NULL, 'errorCode', 'String', 15, '错误代码', 50, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (423, 33, 'error_message', 'varchar', NULL, 'errorMessage', 'String', 16, '错误信息', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (424, 33, 'notify_status', 'tinyint', NULL, 'notifyStatus', 'Integer', 17, '通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (425, 33, 'notify_count', 'int', NULL, 'notifyCount', 'Integer', 18, '通知次数', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (426, 33, 'last_notify_time', 'datetime', NULL, 'lastNotifyTime', 'LocalDateTime', 19, '最后通知时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (427, 33, 'next_notify_time', 'datetime', NULL, 'nextNotifyTime', 'LocalDateTime', 20, '下次通知时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (428, 33, 'callback_data', 'text', NULL, 'callbackData', 'String', 21, '回调数据', 65535, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (429, 33, 'callback_time', 'datetime', NULL, 'callbackTime', 'LocalDateTime', 22, '回调时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (430, 33, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 23, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (431, 33, 'create_by', 'varchar', NULL, 'createBy', 'String', 24, '创建人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (432, 33, 'update_by', 'varchar', NULL, 'updateBy', 'String', 25, '更新人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (433, 33, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 26, '创建时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (434, 33, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 27, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33');
INSERT INTO `gen_field_config` VALUES (435, 34, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (436, 34, 'channel_code', 'varchar', NULL, 'channelCode', 'String', 2, '渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (437, 34, 'channel_name', 'varchar', NULL, 'channelName', 'String', 3, '渠道名称', 50, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (438, 34, 'channel_type', 'varchar', NULL, 'channelType', 'String', 4, '渠道类型：ONLINE-线上 OFFLINE-线下', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (439, 34, 'config_key', 'varchar', NULL, 'configKey', 'String', 5, '配置键', 50, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (440, 34, 'config_value', 'text', NULL, 'configValue', 'String', 6, '配置值', 65535, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (441, 34, 'config_type', 'varchar', NULL, 'configType', 'String', 7, '配置类型：CERT-证书 KEY-密钥 URL-地址', 20, 0, 1, 1, 1, 11, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (442, 34, 'config_desc', 'varchar', NULL, 'configDesc', 'String', 8, '配置描述', 200, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (443, 34, 'is_enabled', 'tinyint', NULL, 'isEnabled', 'Integer', 9, '是否启用：0-禁用 1-启用', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (444, 34, 'is_default', 'tinyint', NULL, 'isDefault', 'Integer', 10, '是否默认：0-否 1-是', NULL, 0, 1, 1, 1, 11, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (445, 34, 'priority', 'int', NULL, 'priority', 'Integer', 11, '优先级，数值越大优先级越高', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (446, 34, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 12, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (447, 34, 'create_by', 'varchar', NULL, 'createBy', 'String', 13, '创建人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (448, 34, 'update_by', 'varchar', NULL, 'updateBy', 'String', 14, '更新人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (449, 34, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 15, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (450, 34, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 16, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25');
INSERT INTO `gen_field_config` VALUES (451, 35, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (452, 35, 'reconciliation_no', 'varchar', NULL, 'reconciliationNo', 'String', 2, '对账单号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (453, 35, 'channel_code', 'varchar', NULL, 'channelCode', 'String', 3, '渠道编码', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (454, 35, 'bill_date', 'date', NULL, 'billDate', 'LocalDate', 4, '对账日期', NULL, 1, 1, 1, 0, 1, 8, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (455, 35, 'bill_type', 'varchar', NULL, 'billType', 'String', 5, '账单类型：PAYMENT-支付 REFUND-退款 ALL-全部', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (456, 35, 'total_count', 'int', NULL, 'totalCount', 'Integer', 6, '总笔数', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (457, 35, 'total_amount', 'decimal', NULL, 'totalAmount', 'BigDecimal', 7, '总金额', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (458, 35, 'success_count', 'int', NULL, 'successCount', 'Integer', 8, '成功笔数', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (459, 35, 'success_amount', 'decimal', NULL, 'successAmount', 'BigDecimal', 9, '成功金额', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (460, 35, 'failure_count', 'int', NULL, 'failureCount', 'Integer', 10, '失败笔数', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (461, 35, 'failure_amount', 'decimal', NULL, 'failureAmount', 'BigDecimal', 11, '失败金额', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (462, 35, 'difference_count', 'int', NULL, 'differenceCount', 'Integer', 12, '差异笔数', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (463, 35, 'reconcile_status', 'tinyint', NULL, 'reconcileStatus', 'Integer', 13, '对账状态：0-未对账 1-对账中 2-对账完成 3-对账异常', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (464, 35, 'download_status', 'tinyint', NULL, 'downloadStatus', 'Integer', 14, '下载状态：0-未下载 1-下载中 2-下载完成 3-下载失败', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (465, 35, 'download_time', 'datetime', NULL, 'downloadTime', 'LocalDateTime', 15, '下载时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (466, 35, 'reconcile_time', 'datetime', NULL, 'reconcileTime', 'LocalDateTime', 16, '对账时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (467, 35, 'bill_file_url', 'varchar', NULL, 'billFileUrl', 'String', 17, '账单文件URL', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (468, 35, 'error_message', 'varchar', NULL, 'errorMessage', 'String', 18, '错误信息', 1000, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (469, 35, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 19, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (470, 35, 'create_by', 'varchar', NULL, 'createBy', 'String', 20, '创建人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (471, 35, 'update_by', 'varchar', NULL, 'updateBy', 'String', 21, '更新人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (472, 35, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 22, '创建时间', NULL, 1, 1, 1, 0, 1, 9, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (473, 35, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 23, '更新时间', NULL, 1, 1, 1, 0, 1, 9, NULL, '2026-02-02 20:27:16', '2026-02-02 20:27:16');
INSERT INTO `gen_field_config` VALUES (494, 37, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (495, 37, 'reconciliation_id', 'bigint', NULL, 'reconciliationId', 'Long', 2, '对账单ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (496, 37, 'channel_code', 'varchar', NULL, 'channelCode', 'String', 3, '渠道编码', 20, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (497, 37, 'bill_date', 'date', NULL, 'billDate', 'LocalDate', 4, '对账日期', NULL, 1, 1, 1, 1, 4, 8, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (498, 37, 'third_transaction_no', 'varchar', NULL, 'thirdTransactionNo', 'String', 5, '第三方交易流水号', 128, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (499, 37, 'third_order_no', 'varchar', NULL, 'thirdOrderNo', 'String', 6, '第三方订单号', 128, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (500, 37, 'payment_no', 'varchar', NULL, 'paymentNo', 'String', 7, '系统支付单号', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (501, 37, 'order_no', 'varchar', NULL, 'orderNo', 'String', 8, '业务订单号', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (502, 37, 'trade_type', 'varchar', NULL, 'tradeType', 'String', 9, '交易类型：PAYMENT-支付 REFUND-退款', 20, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (503, 37, 'trade_time', 'datetime', NULL, 'tradeTime', 'LocalDateTime', 10, '交易时间', NULL, 1, 1, 1, 0, 1, 9, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (504, 37, 'trade_amount', 'decimal', NULL, 'tradeAmount', 'BigDecimal', 11, '交易金额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (505, 37, 'trade_status', 'varchar', NULL, 'tradeStatus', 'String', 12, '交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中', 20, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (506, 37, 'reconcile_status', 'tinyint', NULL, 'reconcileStatus', 'Integer', 13, '对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (507, 37, 'difference_reason', 'varchar', NULL, 'differenceReason', 'String', 14, '差异原因', 200, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (508, 37, 'reconcile_result', 'varchar', NULL, 'reconcileResult', 'String', 15, '对账结果：MATCH-匹配 SYS_MORE-系统多 CHANNEL_MORE-渠道多', 20, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (509, 37, 'reconcile_time', 'datetime', NULL, 'reconcileTime', 'LocalDateTime', 16, '对账时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (510, 37, 'remark', 'varchar', NULL, 'remark', 'String', 17, '备注', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (511, 37, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 18, '逻辑删除：0-未删除 1-已删除', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (512, 37, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 19, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (513, 37, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 20, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-09 13:56:18', '2026-02-09 13:56:18');
INSERT INTO `gen_field_config` VALUES (514, 38, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (515, 38, 'flow_no', 'varchar', NULL, 'flowNo', 'String', 2, '流水号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (516, 38, 'payment_no', 'varchar', NULL, 'paymentNo', 'String', 3, '支付单号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (517, 38, 'refund_no', 'varchar', NULL, 'refundNo', 'String', 4, '退款单号', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (518, 38, 'order_no', 'varchar', NULL, 'orderNo', 'String', 5, '业务订单号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (519, 38, 'user_id', 'bigint', NULL, 'userId', 'Long', 6, '用户ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (520, 38, 'flow_type', 'varchar', NULL, 'flowType', 'String', 7, '流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (521, 38, 'flow_direction', 'varchar', NULL, 'flowDirection', 'String', 8, '资金方向：IN-入金 OUT-出金', 10, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (522, 38, 'amount', 'decimal', NULL, 'amount', 'BigDecimal', 9, '流水金额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (523, 38, 'balance_before', 'decimal', NULL, 'balanceBefore', 'BigDecimal', 10, '交易前余额', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (524, 38, 'balance_after', 'decimal', NULL, 'balanceAfter', 'BigDecimal', 11, '交易后余额', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (525, 38, 'channel_code', 'varchar', NULL, 'channelCode', 'String', 12, '渠道编码', 20, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (526, 38, 'third_flow_no', 'varchar', NULL, 'thirdFlowNo', 'String', 13, '第三方流水号', 128, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (527, 38, 'flow_status', 'tinyint', NULL, 'flowStatus', 'Integer', 14, '流水状态：0-处理中 1-成功 2-失败', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (528, 38, 'trade_time', 'datetime', NULL, 'tradeTime', 'LocalDateTime', 15, '交易时间', NULL, 1, 1, 1, 1, 4, 9, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (529, 38, 'complete_time', 'datetime', NULL, 'completeTime', 'LocalDateTime', 16, '完成时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (530, 38, 'error_code', 'varchar', NULL, 'errorCode', 'String', 17, '错误代码', 50, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (531, 38, 'error_message', 'varchar', NULL, 'errorMessage', 'String', 18, '错误信息', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (532, 38, 'remark', 'varchar', NULL, 'remark', 'String', 19, '备注', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (533, 38, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 20, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (534, 38, 'create_by', 'varchar', NULL, 'createBy', 'String', 21, '创建人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (535, 38, 'update_by', 'varchar', NULL, 'updateBy', 'String', 22, '更新人', 64, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (536, 38, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 23, '创建时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (537, 38, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 24, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-09 15:45:09', '2026-02-09 15:45:09');
INSERT INTO `gen_field_config` VALUES (538, 39, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (539, 39, 'notify_no', 'varchar', NULL, 'notifyNo', 'String', 2, '通知编号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (540, 39, 'payment_no', 'varchar', NULL, 'paymentNo', 'String', 3, '支付单号', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (541, 39, 'refund_no', 'varchar', NULL, 'refundNo', 'String', 4, '退款单号', 32, 0, 1, 1, 1, 11, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (542, 39, 'notify_type', 'varchar', NULL, 'notifyType', 'String', 5, '通知类型：PAYMENT-支付 REFUND-退款', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (543, 39, 'notify_url', 'varchar', NULL, 'notifyUrl', 'String', 6, '通知地址', 500, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (544, 39, 'request_data', 'text', NULL, 'requestData', 'String', 7, '请求数据', 65535, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (545, 39, 'response_data', 'text', NULL, 'responseData', 'String', 8, '响应数据', 65535, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (546, 39, 'notify_status', 'tinyint', NULL, 'notifyStatus', 'Integer', 9, '通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (547, 39, 'notify_count', 'int', NULL, 'notifyCount', 'Integer', 10, '通知次数', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (548, 39, 'max_notify_count', 'int', NULL, 'maxNotifyCount', 'Integer', 11, '最大通知次数', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (549, 39, 'next_notify_time', 'datetime', NULL, 'nextNotifyTime', 'LocalDateTime', 12, '下次通知时间', NULL, 1, 1, 1, 1, 4, 9, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (550, 39, 'last_notify_time', 'datetime', NULL, 'lastNotifyTime', 'LocalDateTime', 13, '最后通知时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (551, 39, 'success_time', 'datetime', NULL, 'successTime', 'LocalDateTime', 14, '成功时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (552, 39, 'error_message', 'varchar', NULL, 'errorMessage', 'String', 15, '错误信息', 1000, 0, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (553, 39, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 16, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (554, 39, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 17, '创建时间', NULL, 1, 1, 1, 1, 4, 9, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (555, 39, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 18, '更新时间', NULL, 1, 1, 1, 0, 1, 9, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03');
INSERT INTO `gen_field_config` VALUES (556, 40, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (557, 40, 'account_no', 'varchar', NULL, 'accountNo', 'String', 2, '账户编号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (558, 40, 'user_id', 'bigint', NULL, 'userId', 'Long', 3, '用户ID', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (559, 40, 'account_type', 'varchar', NULL, 'accountType', 'String', 4, '账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (560, 40, 'balance', 'decimal', NULL, 'balance', 'BigDecimal', 5, '账户余额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (561, 40, 'frozen_balance', 'decimal', NULL, 'frozenBalance', 'BigDecimal', 6, '冻结余额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (562, 40, 'available_balance', 'decimal', NULL, 'availableBalance', 'BigDecimal', 7, '可用余额', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (563, 40, 'total_income', 'decimal', NULL, 'totalIncome', 'BigDecimal', 8, '总收入', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (564, 40, 'total_expend', 'decimal', NULL, 'totalExpend', 'BigDecimal', 9, '总支出', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (565, 40, 'currency', 'varchar', NULL, 'currency', 'String', 10, '币种', 3, 0, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (566, 40, 'status', 'tinyint', NULL, 'status', 'Integer', 11, '账户状态：0-冻结 1-正常 2-注销', NULL, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (567, 40, 'version', 'int', NULL, 'version', 'Integer', 12, '版本号，用于乐观锁', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (568, 40, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 13, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (569, 40, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 14, '创建时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (570, 40, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 15, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00');
INSERT INTO `gen_field_config` VALUES (571, 41, 'id', 'bigint', NULL, 'id', 'Long', 1, '主键ID', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (572, 41, 'flow_no', 'varchar', NULL, 'flowNo', 'String', 2, '流水号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (573, 41, 'account_no', 'varchar', NULL, 'accountNo', 'String', 3, '账户编号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (574, 41, 'biz_no', 'varchar', NULL, 'bizNo', 'String', 4, '业务单号', 32, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (575, 41, 'biz_type', 'varchar', NULL, 'bizType', 'String', 5, '业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (576, 41, 'flow_type', 'varchar', NULL, 'flowType', 'String', 6, '流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻', 20, 1, 1, 1, 1, 11, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (577, 41, 'amount', 'decimal', NULL, 'amount', 'BigDecimal', 7, '变动金额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (578, 41, 'balance_before', 'decimal', NULL, 'balanceBefore', 'BigDecimal', 8, '变动前余额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (579, 41, 'balance_after', 'decimal', NULL, 'balanceAfter', 'BigDecimal', 9, '变动后余额', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (580, 41, 'frozen_before', 'decimal', NULL, 'frozenBefore', 'BigDecimal', 10, '变动前冻结', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (581, 41, 'frozen_after', 'decimal', NULL, 'frozenAfter', 'BigDecimal', 11, '变动后冻结', NULL, 0, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (582, 41, 'remark', 'varchar', NULL, 'remark', 'String', 12, '备注', 500, 0, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (583, 41, 'is_deleted', 'tinyint', NULL, 'isDeleted', 'Integer', 13, '逻辑删除：0-未删除 1-已删除', NULL, 1, 1, 1, 0, 1, 1, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (584, 41, 'create_time', 'datetime', NULL, 'createTime', 'LocalDateTime', 14, '创建时间', NULL, 0, 1, 1, 1, 4, 9, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');
INSERT INTO `gen_field_config` VALUES (585, 41, 'update_time', 'datetime', NULL, 'updateTime', 'LocalDateTime', 15, '更新时间', NULL, 0, 1, 1, 0, 1, 9, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27');

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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_account_no`(`account_no` ASC) USING BTREE COMMENT '账户编号唯一',
  UNIQUE INDEX `uk_user_account_type`(`user_id` ASC, `account_type` ASC) USING BTREE COMMENT '用户ID 和 账户类型 唯一',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_status`(`status` ASC) USING BTREE COMMENT '账户状态索引'
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_flow_no`(`flow_no` ASC) USING BTREE COMMENT '流水号唯一',
  INDEX `idx_account_no`(`account_no` ASC) USING BTREE COMMENT '账户编号索引',
  INDEX `idx_biz_no`(`biz_no` ASC) USING BTREE COMMENT '业务单号索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引'
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_channel_config`(`channel_code` ASC, `config_key` ASC) USING BTREE COMMENT '渠道编码 和 配置键唯一',
  INDEX `idx_channel_code`(`channel_code` ASC) USING BTREE COMMENT '渠道编码索引',
  INDEX `idx_is_enabled`(`is_enabled` ASC) USING BTREE COMMENT '逻辑删除索引'
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付渠道配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_channel_config
-- ----------------------------
INSERT INTO `pay_channel_config` VALUES (1, 'ALIPAY', '支付宝', 'ONLINE', 'app_id', 'your_app_id', 'STRING', '应用ID', 1, 1, 100, 0, '', '', '2026-02-02 17:10:55', '2026-02-02 17:10:55');
INSERT INTO `pay_channel_config` VALUES (2, 'ALIPAY', '支付宝', 'ONLINE', 'private_key', 'your_private_key', 'SECRET', '应用私钥', 1, 1, 100, 0, '', '', '2026-02-02 17:10:55', '2026-02-02 17:10:55');
INSERT INTO `pay_channel_config` VALUES (3, 'ALIPAY', '支付宝', 'ONLINE', 'alipay_public_key', 'your_public_key', 'SECRET', '支付宝公钥', 1, 1, 100, 0, '', '', '2026-02-02 17:10:55', '2026-02-02 17:10:55');
INSERT INTO `pay_channel_config` VALUES (4, 'ALIPAY', '支付宝', 'ONLINE', 'gateway_url', 'https://openapi.alipay.com/gateway.do', 'URL', '网关地址', 1, 1, 100, 0, '', '', '2026-02-02 17:10:55', '2026-02-02 17:10:55');
INSERT INTO `pay_channel_config` VALUES (5, 'WECHAT', '微信支付', 'ONLINE', 'app_id', 'your_app_id', 'STRING', '应用ID', 1, 0, 90, 0, '', '', '2026-02-02 17:10:55', '2026-02-02 17:10:55');
INSERT INTO `pay_channel_config` VALUES (6, 'WECHAT', '微信支付', 'ONLINE', 'mch_id', 'your_mch_id', 'STRING', '商户号', 1, 0, 90, 0, '', '', '2026-02-02 17:10:55', '2026-02-02 17:10:55');

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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_flow_no`(`flow_no` ASC) USING BTREE COMMENT '流水号唯一',
  UNIQUE INDEX `uk_third_flow`(`channel_code` ASC, `third_flow_no` ASC, `flow_type` ASC) USING BTREE COMMENT '渠道编码 和 第三方流水号 和 流水类型唯一',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '支付单号索引',
  INDEX `idx_refund_no`(`refund_no` ASC) USING BTREE COMMENT '退款单号索引',
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE COMMENT '业务订单号索引',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_trade_time`(`trade_time` ASC) USING BTREE COMMENT '交易时间索引',
  INDEX `idx_flow_type`(`flow_type` ASC) USING BTREE COMMENT '流水类型索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引'
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_notify_no`(`notify_no` ASC) USING BTREE COMMENT '通知编号唯一',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '支付单号索引',
  INDEX `idx_refund_no`(`refund_no` ASC) USING BTREE COMMENT '退款单号索引',
  INDEX `idx_notify_status`(`notify_status` ASC) USING BTREE COMMENT '通知状态索引',
  INDEX `idx_next_notify_time`(`next_notify_time` ASC) USING BTREE COMMENT '下次通知时间索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引'
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_payment_no`(`payment_no` ASC) USING BTREE COMMENT '支付单号唯一',
  UNIQUE INDEX `uk_order_no_biz_type`(`order_no` ASC, `biz_type` ASC, `payment_channel` ASC) USING BTREE COMMENT '业务订单号，业务类型，支付渠道唯一',
  INDEX `idx_user_id`(`user_id` ASC) USING BTREE COMMENT '用户ID索引',
  INDEX `idx_payment_status`(`payment_status` ASC) USING BTREE COMMENT '支付状态索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_payment_channel`(`payment_channel` ASC) USING BTREE COMMENT '支付渠道索引',
  INDEX `idx_payment_time`(`payment_time` ASC) USING BTREE COMMENT '支付时间索引',
  INDEX `idx_third_payment_no`(`third_payment_no` ASC) USING BTREE COMMENT '第三方支付单号索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付订单主表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_order
-- ----------------------------

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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_bill_date_channel`(`bill_date` ASC, `channel_code` ASC, `bill_type` ASC) USING BTREE COMMENT '对账日期 和 渠道编码 和 账单类型唯一',
  INDEX `idx_reconcile_status`(`reconcile_status` ASC) USING BTREE COMMENT '对账状态索引',
  INDEX `idx_bill_date`(`bill_date` ASC) USING BTREE COMMENT '对账日期索引'
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_channel_transaction`(`channel_code` ASC, `third_transaction_no` ASC, `bill_date` ASC) USING BTREE COMMENT '渠道编码 和 第三方交易流水号 和 对账日期唯一',
  INDEX `idx_reconciliation_id`(`reconciliation_id` ASC) USING BTREE COMMENT '对账单ID索引',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '系统支付单号索引',
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE COMMENT '业务订单号索引',
  INDEX `idx_trade_time`(`trade_time` ASC) USING BTREE COMMENT '交易时间索引',
  INDEX `idx_reconcile_status`(`reconcile_status` ASC) USING BTREE COMMENT '对账状态索引',
  INDEX `idx_bill_date`(`bill_date` ASC) USING BTREE COMMENT '对账日期索引'
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
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_refund_no`(`refund_no` ASC) USING BTREE COMMENT '退款单号唯一',
  UNIQUE INDEX `uk_payment_refund`(`payment_no` ASC, `refund_channel` ASC) USING BTREE COMMENT '防止同一支付单重复退款',
  INDEX `idx_order_no`(`order_no` ASC) USING BTREE COMMENT '业务订单号索引',
  INDEX `idx_payment_no`(`payment_no` ASC) USING BTREE COMMENT '原支付单号索引',
  INDEX `idx_refund_status`(`refund_status` ASC) USING BTREE COMMENT '退款状态索引',
  INDEX `idx_create_time`(`create_time` ASC) USING BTREE COMMENT '创建时间索引',
  INDEX `idx_refund_time`(`refund_time` ASC) USING BTREE COMMENT '退款完成时间索引',
  INDEX `idx_third_refund_no`(`third_refund_no` ASC) USING BTREE COMMENT '第三方退款流水号索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '退款记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_refund_record
-- ----------------------------

-- ----------------------------
-- Table structure for sys_config
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `config_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置名称',
  `config_key` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置key',
  `config_value` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL COMMENT '配置值',
  `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `is_deleted` tinyint NOT NULL DEFAULT 0 COMMENT '逻辑删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci COMMENT = '系统配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_config
-- ----------------------------
INSERT INTO `sys_config` VALUES (1, '系统限流QPS', 'IP_QPS_THRESHOLD_LIMIT', '10', '单个IP请求的最大每秒查询数（QPS）阈值Key', '2025-12-30 22:14:06', 1, NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_dept
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
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
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE COMMENT '部门编号唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '部门表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES (1, '可我不敌可爱', 'Aioveu', 0, '0', 1, 1, 1, NULL, 1, '2025-12-30 22:13:39', 0);
INSERT INTO `sys_dept` VALUES (2, '研发部门', 'RD001', 1, '0,1', 1, 1, 2, NULL, 2, '2025-12-30 22:13:39', 0);
INSERT INTO `sys_dept` VALUES (3, '测试部门', 'QA001', 1, '0,1', 1, 1, 2, NULL, 2, '2025-12-30 22:13:39', 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict
-- ----------------------------
INSERT INTO `sys_dict` VALUES (1, 'gender', '性别', 1, NULL, '2025-12-30 22:13:41', 1, '2025-12-30 22:13:41', 1, 0);
INSERT INTO `sys_dict` VALUES (2, 'notice_type', '通知类型', 1, NULL, '2025-12-30 22:13:41', 1, '2025-12-30 22:13:41', 1, 0);
INSERT INTO `sys_dict` VALUES (3, 'notice_level', '通知级别', 1, NULL, '2025-12-30 22:13:41', 1, '2025-12-30 22:13:41', 1, 0);

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
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '字典项表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_dict_item
-- ----------------------------
INSERT INTO `sys_dict_item` VALUES (1, 'gender', '1', '男', 'primary', 1, 1, NULL, '2025-12-30 22:13:41', 1, '2025-12-30 22:13:41', 1);
INSERT INTO `sys_dict_item` VALUES (2, 'gender', '2', '女', 'danger', 1, 2, NULL, '2025-12-30 22:13:41', 1, '2025-12-30 22:13:41', 1);
INSERT INTO `sys_dict_item` VALUES (3, 'gender', '0', '保密', 'info', 1, 3, NULL, '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (4, 'notice_type', '1', '系统升级', 'success', 1, 1, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (5, 'notice_type', '2', '系统维护', 'primary', 1, 2, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (6, 'notice_type', '3', '安全警告', 'danger', 1, 3, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (7, 'notice_type', '4', '假期通知', 'success', 1, 4, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (8, 'notice_type', '5', '公司新闻', 'primary', 1, 5, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (9, 'notice_type', '99', '其他', 'info', 1, 99, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (10, 'notice_level', 'L', '低', 'info', 1, 1, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (11, 'notice_level', 'M', '中', 'warning', 1, 2, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);
INSERT INTO `sys_dict_item` VALUES (12, 'notice_level', 'H', '高', 'danger', 1, 3, '', '2025-12-30 22:13:42', 1, '2025-12-30 22:13:42', 1);

-- ----------------------------
-- Table structure for sys_log
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
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
  INDEX `idx_create_time`(`create_time`) USING BTREE
) ENGINE = MyISAM AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统日志表' ROW_FORMAT = Dynamic;

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
  `type` tinyint NOT NULL COMMENT '菜单类型（1-菜单 2-目录 3-外链 4-按钮）',
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 352 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '菜单管理' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '0', '系统管理', 2, '', '/system', 'Layout', NULL, NULL, NULL, 1, 51, 'system', '/system/user', '2025-12-30 22:13:43', '2026-01-31 16:06:52', NULL);
INSERT INTO `sys_menu` VALUES (2, 1, '0,1', '用户管理', 1, 'User', 'user', 'system/user/index', NULL, NULL, 1, 1, 1, 'el-icon-User', NULL, '2025-12-30 22:13:43', '2025-12-30 22:13:43', NULL);
INSERT INTO `sys_menu` VALUES (3, 1, '0,1', '角色管理', 1, 'Role', 'role', 'system/role/index', NULL, NULL, 1, 1, 2, 'role', NULL, '2025-12-30 22:13:43', '2025-12-30 22:13:43', NULL);
INSERT INTO `sys_menu` VALUES (4, 1, '0,1', '菜单管理', 1, 'SysMenu', 'menu', 'system/menu/index', NULL, NULL, 1, 1, 3, 'menu', NULL, '2025-12-30 22:13:43', '2025-12-30 22:13:43', NULL);
INSERT INTO `sys_menu` VALUES (5, 1, '0,1', '部门管理', 1, 'Dept', 'dept', 'system/dept/index', NULL, NULL, 1, 1, 4, 'tree', NULL, '2025-12-30 22:13:43', '2025-12-30 22:13:43', NULL);
INSERT INTO `sys_menu` VALUES (6, 1, '0,1', '字典管理', 1, 'Dict', 'dict', 'system/dict/index', NULL, NULL, 1, 1, 5, 'dict', NULL, '2025-12-30 22:13:43', '2025-12-30 22:13:43', NULL);
INSERT INTO `sys_menu` VALUES (20, 0, '0', '多级菜单', 2, NULL, '/multi-level', 'Layout', NULL, 1, NULL, 1, 55, 'cascader', '', '2025-12-30 22:13:44', '2026-01-31 16:07:14', NULL);
INSERT INTO `sys_menu` VALUES (21, 20, '0,20', '菜单一级', 1, NULL, 'multi-level1', 'demo/multi-level/level1', NULL, 1, NULL, 1, 1, '', '', '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (22, 21, '0,20,21', '菜单二级', 1, NULL, 'multi-level2', 'demo/multi-level/children/level2', NULL, 0, NULL, 1, 1, '', NULL, '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (23, 22, '0,20,21,22', '菜单三级-1', 1, NULL, 'multi-level3-1', 'demo/multi-level/children/children/level3-1', NULL, 0, 1, 1, 1, '', '', '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (24, 22, '0,20,21,22', '菜单三级-2', 1, NULL, 'multi-level3-2', 'demo/multi-level/children/children/level3-2', NULL, 0, 1, 1, 2, '', '', '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (26, 0, '0', '平台文档', 2, '', '/doc', 'Layout', NULL, NULL, NULL, 1, 54, 'document', 'https://juejin.cn/post/7228990409909108793', '2025-12-30 22:13:44', '2026-01-31 16:07:09', NULL);
INSERT INTO `sys_menu` VALUES (30, 26, '0,26', '平台文档(外链)', 3, NULL, 'https://juejin.cn/post/7228990409909108793', '', NULL, NULL, NULL, 1, 2, 'document', '', '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (31, 2, '0,1,2', '用户新增', 4, NULL, '', NULL, 'sys:user:add', NULL, NULL, 1, 1, '', '', '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (32, 2, '0,1,2', '用户编辑', 4, NULL, '', NULL, 'sys:user:edit', NULL, NULL, 1, 2, '', '', '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (33, 2, '0,1,2', '用户删除', 4, NULL, '', NULL, 'sys:user:delete', NULL, NULL, 1, 3, '', '', '2025-12-30 22:13:44', '2025-12-30 22:13:44', NULL);
INSERT INTO `sys_menu` VALUES (36, 0, '0', '组件封装', 2, NULL, '/component', 'Layout', NULL, NULL, NULL, 1, 56, 'menu', '', '2025-12-30 22:13:45', '2026-01-31 16:07:21', NULL);
INSERT INTO `sys_menu` VALUES (37, 36, '0,36', '富文本编辑器', 1, NULL, 'wang-editor', 'demo/wang-editor', NULL, NULL, 1, 1, 2, '', '', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (38, 36, '0,36', '图片上传', 1, NULL, 'upload', 'demo/upload', NULL, NULL, 1, 1, 3, '', '', '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (39, 36, '0,36', '图标选择器', 1, NULL, 'icon-selector', 'demo/icon-selector', NULL, NULL, 1, 1, 4, '', '', '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (40, 0, '0', '接口文档', 2, NULL, '/api', 'Layout', NULL, 1, NULL, 1, 53, 'api', '', '2025-12-30 22:13:45', '2026-01-31 16:07:04', NULL);
INSERT INTO `sys_menu` VALUES (41, 40, '0,40', 'Apifox', 1, NULL, 'apifox', 'demo/api/apifox', NULL, NULL, 1, 1, 1, 'api', '', '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (70, 3, '0,1,3', '角色新增', 4, NULL, '', NULL, 'sys:role:add', NULL, NULL, 1, 2, '', NULL, '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (71, 3, '0,1,3', '角色编辑', 4, NULL, '', NULL, 'sys:role:edit', NULL, NULL, 1, 3, '', NULL, '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (72, 3, '0,1,3', '角色删除', 4, NULL, '', NULL, 'sys:role:delete', NULL, NULL, 1, 4, '', NULL, '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (73, 4, '0,1,4', '菜单新增', 4, NULL, '', NULL, 'sys:menu:add', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (74, 4, '0,1,4', '菜单编辑', 4, NULL, '', NULL, 'sys:menu:edit', NULL, NULL, 1, 3, '', NULL, '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (75, 4, '0,1,4', '菜单删除', 4, NULL, '', NULL, 'sys:menu:delete', NULL, NULL, 1, 3, '', NULL, '2025-12-30 22:13:45', '2025-12-30 22:13:45', NULL);
INSERT INTO `sys_menu` VALUES (76, 5, '0,1,5', '部门新增', 4, NULL, '', NULL, 'sys:dept:add', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (77, 5, '0,1,5', '部门编辑', 4, NULL, '', NULL, 'sys:dept:edit', NULL, NULL, 1, 2, '', NULL, '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (78, 5, '0,1,5', '部门删除', 4, NULL, '', NULL, 'sys:dept:delete', NULL, NULL, 1, 3, '', NULL, '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (79, 6, '0,1,6', '字典新增', 4, NULL, '', NULL, 'sys:dict:add', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (81, 6, '0,1,6', '字典编辑', 4, NULL, '', NULL, 'sys:dict:edit', NULL, NULL, 1, 2, '', NULL, '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (84, 6, '0,1,6', '字典删除', 4, NULL, '', NULL, 'sys:dict:delete', NULL, NULL, 1, 3, '', NULL, '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (88, 2, '0,1,2', '重置密码', 4, NULL, '', NULL, 'sys:user:reset-password', NULL, NULL, 1, 4, '', NULL, '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (89, 0, '0', '功能演示', 2, NULL, '/function', 'Layout', NULL, NULL, NULL, 1, 58, 'menu', '', '2025-12-30 22:13:46', '2026-01-31 16:07:33', NULL);
INSERT INTO `sys_menu` VALUES (90, 89, '0,89', 'Websocket', 1, NULL, '/function/websocket', 'demo/websocket', NULL, NULL, 1, 1, 3, '', '', '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (95, 36, '0,36', '字典组件', 1, NULL, 'dict-demo', 'demo/dictionary', NULL, NULL, 1, 1, 4, '', '', '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (97, 89, '0,89', 'Icons', 1, NULL, 'icon-demo', 'demo/icons', NULL, NULL, 1, 1, 2, 'el-icon-Notification', '', '2025-12-30 22:13:46', '2025-12-30 22:13:46', NULL);
INSERT INTO `sys_menu` VALUES (102, 26, '0,26', 'document', 3, '', 'internal-doc', 'demo/internal-doc', NULL, NULL, NULL, 1, 1, 'document', '', '2025-12-30 22:13:47', '2025-12-30 22:13:47', NULL);
INSERT INTO `sys_menu` VALUES (105, 2, '0,1,2', '用户查询', 4, NULL, '', NULL, 'sys:user:query', 0, 0, 1, 0, '', NULL, '2025-12-30 22:13:47', '2025-12-30 22:13:47', NULL);
INSERT INTO `sys_menu` VALUES (106, 2, '0,1,2', '用户导入', 4, NULL, '', NULL, 'sys:user:import', NULL, NULL, 1, 5, '', NULL, '2025-12-30 22:13:47', '2025-12-30 22:13:47', NULL);
INSERT INTO `sys_menu` VALUES (107, 2, '0,1,2', '用户导出', 4, NULL, '', NULL, 'sys:user:export', NULL, NULL, 1, 6, '', NULL, '2025-12-30 22:13:47', '2025-12-30 22:13:47', NULL);
INSERT INTO `sys_menu` VALUES (108, 36, '0,36', '增删改查', 1, NULL, 'curd', 'demo/curd/index', NULL, NULL, 1, 1, 0, '', '', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (109, 36, '0,36', '列表选择器', 1, NULL, 'table-select', 'demo/table-select/index', NULL, NULL, 1, 1, 1, '', '', NULL, NULL, NULL);
INSERT INTO `sys_menu` VALUES (110, 0, '0', '路由参数', 2, NULL, '/route-param', 'Layout', NULL, 1, 1, 1, 57, 'el-icon-ElementPlus', NULL, '2025-12-30 22:13:47', '2026-01-31 16:07:28', NULL);
INSERT INTO `sys_menu` VALUES (111, 110, '0,110', '参数(type=1)', 1, NULL, 'route-param-type1', 'demo/route-param', NULL, 0, 1, 1, 1, 'el-icon-Star', NULL, '2025-12-30 22:13:47', '2025-12-30 22:13:47', '{\"type\": \"1\"}');
INSERT INTO `sys_menu` VALUES (112, 110, '0,110', '参数(type=2)', 1, NULL, 'route-param-type2', 'demo/route-param', NULL, 0, 1, 1, 2, 'el-icon-StarFilled', NULL, '2025-12-30 22:13:47', '2025-12-30 22:13:47', '{\"type\": \"2\"}');
INSERT INTO `sys_menu` VALUES (117, 1, '0,1', '系统日志', 1, 'Log', 'log', 'system/log/index', NULL, 0, 1, 1, 6, 'document', NULL, '2025-12-30 22:13:47', '2025-12-30 22:13:47', NULL);
INSERT INTO `sys_menu` VALUES (118, 0, '0', '系统工具', 2, NULL, '/codegen', 'Layout', NULL, 0, 1, 1, 52, 'menu', NULL, '2025-12-30 22:13:47', '2026-01-31 16:06:58', NULL);
INSERT INTO `sys_menu` VALUES (119, 118, '0,118', '代码生成', 1, 'Codegen', 'codegen', 'codegen/index', NULL, 0, 1, 1, 1, 'code', NULL, '2025-12-30 22:13:47', '2025-12-30 22:13:47', NULL);
INSERT INTO `sys_menu` VALUES (120, 1, '0,1', '系统配置', 1, 'Config', 'config', 'system/config/index', NULL, 0, 1, 1, 7, 'setting', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (121, 120, '0,1,120', '系统配置查询', 4, NULL, '', NULL, 'sys:config:query', 0, 1, 1, 1, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (122, 120, '0,1,120', '系统配置新增', 4, NULL, '', NULL, 'sys:config:add', 0, 1, 1, 2, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (123, 120, '0,1,120', '系统配置修改', 4, NULL, '', NULL, 'sys:config:update', 0, 1, 1, 3, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (124, 120, '0,1,120', '系统配置删除', 4, NULL, '', NULL, 'sys:config:delete', 0, 1, 1, 4, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (125, 120, '0,1,120', '系统配置刷新', 4, NULL, '', NULL, 'sys:config:refresh', 0, 1, 1, 5, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (126, 1, '0,1', '通知公告', 1, 'Notice', 'notice', 'system/notice/index', NULL, NULL, NULL, 1, 9, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (127, 126, '0,1,126', '通知查询', 4, NULL, '', NULL, 'sys:notice:query', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (128, 126, '0,1,126', '通知新增', 4, NULL, '', NULL, 'sys:notice:add', NULL, NULL, 1, 2, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (129, 126, '0,1,126', '通知编辑', 4, NULL, '', NULL, 'sys:notice:edit', NULL, NULL, 1, 3, '', NULL, '2025-12-30 22:13:48', '2025-12-30 22:13:48', NULL);
INSERT INTO `sys_menu` VALUES (130, 126, '0,1,126', '通知删除', 4, NULL, '', NULL, 'sys:notice:delete', NULL, NULL, 1, 4, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (133, 126, '0,1,126', '通知发布', 4, NULL, '', NULL, 'sys:notice:publish', 0, 1, 1, 5, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (134, 126, '0,1,126', '通知撤回', 4, NULL, '', NULL, 'sys:notice:revoke', 0, 1, 1, 6, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (135, 1, '0,1', '字典项', 1, 'DictItem', 'dict-item', 'system/dict/dict-item', NULL, 0, 1, 0, 6, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (136, 135, '0,1,135', '字典项新增', 4, NULL, '', NULL, 'sys:dict-item:add', NULL, NULL, 1, 2, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (137, 135, '0,1,135', '字典项编辑', 4, NULL, '', NULL, 'sys:dict-item:edit', NULL, NULL, 1, 3, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (138, 135, '0,1,135', '字典项删除', 4, NULL, '', NULL, 'sys:dict-item:delete', NULL, NULL, 1, 4, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (139, 3, '0,1,3', '角色查询', 4, NULL, '', NULL, 'sys:role:query', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (140, 4, '0,1,4', '菜单查询', 4, NULL, '', NULL, 'sys:menu:query', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (141, 5, '0,1,5', '部门查询', 4, NULL, '', NULL, 'sys:dept:query', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (142, 6, '0,1,6', '字典查询', 4, NULL, '', NULL, 'sys:dict:query', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (143, 135, '0,1,135', '字典项查询', 4, NULL, '', NULL, 'sys:dict-item:query', NULL, NULL, 1, 1, '', NULL, '2025-12-30 22:13:49', '2025-12-30 22:13:49', NULL);
INSERT INTO `sys_menu` VALUES (144, 26, '0,26', '后端文档', 3, NULL, 'https://youlai.blog.csdn.net/article/details/145178880', '', NULL, NULL, NULL, 1, 3, 'document', '', '2024-10-05 23:36:03', '2024-10-05 23:36:03', NULL);
INSERT INTO `sys_menu` VALUES (145, 26, '0,26', '移动端文档', 3, NULL, 'https://youlai.blog.csdn.net/article/details/143222890', '', NULL, NULL, NULL, 1, 4, 'document', '', '2024-10-05 23:36:03', '2024-10-05 23:36:03', NULL);
INSERT INTO `sys_menu` VALUES (146, 36, '0,36', '拖拽组件', 1, NULL, 'drag', 'demo/drag', NULL, NULL, NULL, 1, 5, '', '', '2025-03-31 14:14:45', '2025-03-31 14:14:52', NULL);
INSERT INTO `sys_menu` VALUES (147, 36, '0,36', '滚动文本', 1, NULL, 'text-scroll', 'demo/text-scroll', NULL, NULL, NULL, 1, 6, '', '', '2025-03-31 14:14:49', '2025-03-31 14:14:56', NULL);
INSERT INTO `sys_menu` VALUES (148, 89, '0,89', '字典实时同步', 1, NULL, 'dict-sync', 'demo/dict-sync', NULL, NULL, NULL, 1, 3, '', '', '2025-03-31 14:14:49', '2025-03-31 14:14:56', NULL);
INSERT INTO `sys_menu` VALUES (149, 0, '0', '订单管理', 2, NULL, '/oms', 'Layout', NULL, 1, 1, 1, 1, 'message', NULL, '2026-01-07 17:36:40', '2026-01-07 17:36:40', NULL);
INSERT INTO `sys_menu` VALUES (150, 0, '0', '商品管理', 2, NULL, '/pms', 'Layout', NULL, 1, 1, 1, 2, 'message', NULL, '2026-01-07 17:37:27', '2026-01-07 17:37:27', NULL);
INSERT INTO `sys_menu` VALUES (151, 0, '0', '营销管理', 2, NULL, '/sms', 'Layout', NULL, 1, 1, 1, 3, 'file', NULL, '2026-01-07 17:38:35', '2026-01-07 17:38:35', NULL);
INSERT INTO `sys_menu` VALUES (152, 0, '0', '会员管理', 2, NULL, '/ums', 'Layout', NULL, 0, 1, 1, 4, 'user', NULL, '2026-01-07 17:39:03', '2026-01-07 17:39:03', NULL);
INSERT INTO `sys_menu` VALUES (153, 149, '0,149', '订单详情', 1, 'OmsOrder', 'oms-order', 'aioveuMallOmsOrder/oms-order/index', NULL, 0, 0, 1, 1, NULL, NULL, '2026-01-07 17:41:44', '2026-01-07 18:19:25', NULL);
INSERT INTO `sys_menu` VALUES (154, 153, '0,149,153', '查询', 4, NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:query', 0, 0, 1, 1, NULL, NULL, '2026-01-07 17:41:44', '2026-01-08 19:22:13', NULL);
INSERT INTO `sys_menu` VALUES (155, 153, '0,149,153', '新增', 4, NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:add', 0, 0, 1, 2, NULL, NULL, '2026-01-07 17:41:45', '2026-01-08 19:22:22', NULL);
INSERT INTO `sys_menu` VALUES (156, 153, '0,149,153', '编辑', 4, NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-07 17:41:45', '2026-01-08 19:22:28', NULL);
INSERT INTO `sys_menu` VALUES (157, 153, '0,149,153', '删除', 4, NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-07 17:41:45', '2026-01-08 19:22:33', NULL);
INSERT INTO `sys_menu` VALUES (158, 149, '0,149', '订单商品信息', 1, 'OmsOrderItem', 'oms-order-item', 'aioveuMallOmsOrderItem/oms-order-item/index', NULL, 0, 0, 1, 2, NULL, NULL, '2026-01-08 19:32:58', '2026-01-08 20:03:42', NULL);
INSERT INTO `sys_menu` VALUES (159, 158, '0,149,158', '查询', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:query', 0, 0, 1, 1, NULL, NULL, '2026-01-08 19:32:59', '2026-01-10 16:29:41', NULL);
INSERT INTO `sys_menu` VALUES (160, 158, '0,149,158', '新增', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:add', 0, 0, 1, 2, NULL, NULL, '2026-01-08 19:32:59', '2026-01-10 16:29:47', NULL);
INSERT INTO `sys_menu` VALUES (161, 158, '0,149,158', '编辑', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-08 19:33:00', '2026-01-10 16:29:53', NULL);
INSERT INTO `sys_menu` VALUES (162, 158, '0,149,158', '删除', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-08 19:33:00', '2026-01-10 16:29:58', NULL);
INSERT INTO `sys_menu` VALUES (163, 149, '0,149', '订单物流记录', 1, 'OmsOrderDelivery', 'oms-order-delivery', 'aioveuMallOmsOrderDelivery/oms-order-delivery/index', NULL, 0, 0, 1, 3, NULL, NULL, '2026-01-08 20:09:37', '2026-01-08 20:09:37', NULL);
INSERT INTO `sys_menu` VALUES (164, 163, '0,149,164', '查询', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:query', 0, 0, 1, 1, NULL, NULL, '2026-01-08 20:09:37', '2026-01-08 20:09:37', NULL);
INSERT INTO `sys_menu` VALUES (165, 163, '0,149,165', '新增', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:add', 0, 0, 1, 2, NULL, NULL, '2026-01-08 20:09:37', '2026-01-08 20:09:37', NULL);
INSERT INTO `sys_menu` VALUES (166, 163, '0,149,166', '编辑', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-08 20:09:37', '2026-01-08 20:09:37', NULL);
INSERT INTO `sys_menu` VALUES (167, 163, '0,149,167', '删除', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-08 20:09:38', '2026-01-08 20:09:38', NULL);
INSERT INTO `sys_menu` VALUES (168, 149, '0,149', '订单操作历史记录', 1, 'OmsOrderLog', 'oms-order-log', 'aioveuMallOmsOrderLog/oms-order-log/index', NULL, 0, 0, 1, 4, NULL, NULL, '2026-01-10 16:33:09', '2026-01-10 16:33:09', NULL);
INSERT INTO `sys_menu` VALUES (169, 168, '0,149,169', '查询', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:query', 0, 0, 1, 1, NULL, NULL, '2026-01-10 16:33:09', '2026-01-10 16:33:09', NULL);
INSERT INTO `sys_menu` VALUES (170, 168, '0,149,170', '新增', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:add', 0, 0, 1, 2, NULL, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10', NULL);
INSERT INTO `sys_menu` VALUES (171, 168, '0,149,171', '编辑', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10', NULL);
INSERT INTO `sys_menu` VALUES (172, 168, '0,149,172', '删除', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-10 16:33:10', '2026-01-10 16:33:10', NULL);
INSERT INTO `sys_menu` VALUES (173, 149, '0,149', '支付信息', 1, 'OmsOrderPay', 'oms-order-pay', 'aioveuMallOmsOrderPay/oms-order-pay/index', NULL, 0, 0, 1, 5, NULL, NULL, '2026-01-10 16:53:26', '2026-01-10 16:53:26', NULL);
INSERT INTO `sys_menu` VALUES (174, 173, '0,149,174', '查询', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:query', 0, 0, 1, 1, NULL, NULL, '2026-01-10 16:53:26', '2026-01-10 16:53:26', NULL);
INSERT INTO `sys_menu` VALUES (175, 173, '0,149,175', '新增', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:add', 0, 0, 1, 2, NULL, NULL, '2026-01-10 16:53:26', '2026-01-10 16:53:26', NULL);
INSERT INTO `sys_menu` VALUES (176, 173, '0,149,176', '编辑', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-10 16:53:26', '2026-01-10 16:53:26', NULL);
INSERT INTO `sys_menu` VALUES (177, 173, '0,149,177', '删除', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-10 16:53:26', '2026-01-10 16:53:26', NULL);
INSERT INTO `sys_menu` VALUES (178, 149, '0,149', '订单配置信息', 1, 'OmsOrderSetting', 'oms-order-setting', 'aioveuMallOmsOrderSetting/oms-order-setting/index', NULL, 0, 0, 1, 6, NULL, NULL, '2026-01-10 17:10:13', '2026-01-10 17:10:13', NULL);
INSERT INTO `sys_menu` VALUES (179, 178, '0,149,179', '查询', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:query', 0, 0, 1, 1, NULL, NULL, '2026-01-10 17:10:13', '2026-01-10 17:10:13', NULL);
INSERT INTO `sys_menu` VALUES (180, 178, '0,149,180', '新增', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:add', 0, 0, 1, 2, NULL, NULL, '2026-01-10 17:10:13', '2026-01-10 17:10:13', NULL);
INSERT INTO `sys_menu` VALUES (181, 178, '0,149,181', '编辑', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-10 17:10:13', '2026-01-10 17:10:13', NULL);
INSERT INTO `sys_menu` VALUES (182, 178, '0,149,182', '删除', 4, NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-10 17:10:14', '2026-01-10 17:10:14', NULL);
INSERT INTO `sys_menu` VALUES (183, 149, '0,149', 'AT transaction mode undo table', 1, 'UndoLog', 'undo-log', 'aioveuMallOmsUndoLog/undo-log/index', NULL, 0, 0, 1, 7, NULL, NULL, '2026-01-10 17:30:29', '2026-01-10 17:30:29', NULL);
INSERT INTO `sys_menu` VALUES (184, 183, '0,149,184', '查询', 4, NULL, NULL, NULL, 'aioveuMallOmsUndoLog:undo-log:query', 0, 0, 1, 1, NULL, NULL, '2026-01-10 17:30:29', '2026-01-10 17:30:29', NULL);
INSERT INTO `sys_menu` VALUES (185, 183, '0,149,185', '新增', 4, NULL, NULL, NULL, 'aioveuMallOmsUndoLog:undo-log:add', 0, 0, 1, 2, NULL, NULL, '2026-01-10 17:30:29', '2026-01-10 17:30:29', NULL);
INSERT INTO `sys_menu` VALUES (186, 183, '0,149,186', '编辑', 4, NULL, NULL, NULL, 'aioveuMallOmsUndoLog:undo-log:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-10 17:30:29', '2026-01-10 17:30:29', NULL);
INSERT INTO `sys_menu` VALUES (187, 183, '0,149,187', '删除', 4, NULL, NULL, NULL, 'aioveuMallOmsUndoLog:undo-log:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-10 17:30:29', '2026-01-10 17:30:29', NULL);
INSERT INTO `sys_menu` VALUES (188, 150, '0,150', '商品品牌', 1, 'PmsBrand', 'pms-brand', 'aioveuMallPmsBrand/pms-brand/index', NULL, 0, 1, 1, 1, NULL, NULL, '2026-01-10 18:49:22', '2026-01-27 14:33:27', NULL);
INSERT INTO `sys_menu` VALUES (189, 188, '0,150,188', '查询', 4, NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:query', 0, 0, 1, 1, NULL, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22', NULL);
INSERT INTO `sys_menu` VALUES (190, 188, '0,150,188', '新增', 4, NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:add', 0, 0, 1, 2, NULL, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22', NULL);
INSERT INTO `sys_menu` VALUES (191, 188, '0,150,188', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22', NULL);
INSERT INTO `sys_menu` VALUES (192, 188, '0,150,188', '删除', 4, NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-10 18:49:22', '2026-01-10 18:49:22', NULL);
INSERT INTO `sys_menu` VALUES (193, 150, '0,150', '商品分类', 1, 'PmsCategory', 'pms-category', 'aioveuMallPmsCategory/pms-category/index', NULL, 0, 0, 1, 2, NULL, NULL, '2026-01-11 17:25:12', '2026-01-11 17:25:12', NULL);
INSERT INTO `sys_menu` VALUES (194, 193, '0,150,194', '查询', 4, NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:query', 0, 0, 1, 1, NULL, NULL, '2026-01-11 17:25:12', '2026-01-11 17:25:12', NULL);
INSERT INTO `sys_menu` VALUES (195, 193, '0,150,195', '新增', 4, NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:add', 0, 0, 1, 2, NULL, NULL, '2026-01-11 17:25:12', '2026-01-11 17:25:12', NULL);
INSERT INTO `sys_menu` VALUES (196, 193, '0,150,196', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-11 17:25:12', '2026-01-11 17:25:12', NULL);
INSERT INTO `sys_menu` VALUES (197, 193, '0,150,197', '删除', 4, NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-11 17:25:13', '2026-01-11 17:25:13', NULL);
INSERT INTO `sys_menu` VALUES (198, 150, '0,150', '商品分类树级', 1, 'PmsCategoryTree', 'pms-categoryTree', 'aioveuMallPmsCategoryTree/pms-categoryTree/categoryTree', NULL, 1, 1, 1, 3, 'message', NULL, '2026-01-11 18:52:25', '2026-01-27 19:09:52', NULL);
INSERT INTO `sys_menu` VALUES (199, 150, '0,150', '商品分类类型（规格，属性）', 1, 'PmsCategoryAttribute', 'pms-category-attribute', 'aioveuMallPmsCategoryAttribute/pms-category-attribute/index', NULL, 0, 0, 1, 4, NULL, NULL, '2026-01-11 19:35:19', '2026-01-11 19:55:22', NULL);
INSERT INTO `sys_menu` VALUES (200, 199, '0,150,199', '查询', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:query', 0, 0, 1, 1, NULL, NULL, '2026-01-11 19:35:19', '2026-01-11 19:35:19', NULL);
INSERT INTO `sys_menu` VALUES (201, 199, '0,150,199', '新增', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:add', 0, 0, 1, 2, NULL, NULL, '2026-01-11 19:35:19', '2026-01-11 19:35:19', NULL);
INSERT INTO `sys_menu` VALUES (202, 199, '0,150,199', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-11 19:35:19', '2026-01-11 19:35:19', NULL);
INSERT INTO `sys_menu` VALUES (203, 199, '0,150,199', '删除', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-11 19:35:20', '2026-01-11 19:35:20', NULL);
INSERT INTO `sys_menu` VALUES (204, 150, '0,150', '商品分类与品牌关联表', 1, 'PmsCategoryBrand', 'pms-category-brand', 'aioveuMallPmsCategoryBrand/pms-category-brand/index', NULL, 0, 0, 1, 5, NULL, NULL, '2026-01-11 20:00:49', '2026-01-11 20:00:49', NULL);
INSERT INTO `sys_menu` VALUES (205, 204, '0,150,205', '查询', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryBrand:pms-category-brand:query', 0, 0, 1, 1, NULL, NULL, '2026-01-11 20:00:49', '2026-01-11 20:00:49', NULL);
INSERT INTO `sys_menu` VALUES (206, 204, '0,150,206', '新增', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryBrand:pms-category-brand:add', 0, 0, 1, 2, NULL, NULL, '2026-01-11 20:00:49', '2026-01-11 20:00:49', NULL);
INSERT INTO `sys_menu` VALUES (207, 204, '0,150,207', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryBrand:pms-category-brand:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-11 20:00:49', '2026-01-11 20:00:49', NULL);
INSERT INTO `sys_menu` VALUES (208, 204, '0,150,208', '删除', 4, NULL, NULL, NULL, 'aioveuMallPmsCategoryBrand:pms-category-brand:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-11 20:00:50', '2026-01-11 20:00:50', NULL);
INSERT INTO `sys_menu` VALUES (209, 150, '0,150', '商品库存', 1, 'PmsSku', 'pms-sku', 'aioveuMallPmsSku/pms-sku/index', NULL, 0, 0, 1, 6, NULL, NULL, '2026-01-11 20:56:24', '2026-01-11 20:56:24', NULL);
INSERT INTO `sys_menu` VALUES (210, 209, '0,150,210', '查询', 4, NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:query', 0, 0, 1, 1, NULL, NULL, '2026-01-11 20:56:24', '2026-01-11 20:56:24', NULL);
INSERT INTO `sys_menu` VALUES (211, 209, '0,150,211', '新增', 4, NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:add', 0, 0, 1, 2, NULL, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25', NULL);
INSERT INTO `sys_menu` VALUES (212, 209, '0,150,212', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25', NULL);
INSERT INTO `sys_menu` VALUES (213, 209, '0,150,213', '删除', 4, NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-11 20:56:25', '2026-01-11 20:56:25', NULL);
INSERT INTO `sys_menu` VALUES (214, 150, '0,150', '商品', 1, 'PmsSpu', 'pms-spu', 'aioveuMallPmsSpu/pms-spu/index', NULL, 0, 0, 1, 7, NULL, NULL, '2026-01-11 21:21:13', '2026-01-11 21:21:13', NULL);
INSERT INTO `sys_menu` VALUES (215, 214, '0,150,215', '查询', 4, NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:query', 0, 0, 1, 1, NULL, NULL, '2026-01-11 21:21:13', '2026-01-11 21:21:13', NULL);
INSERT INTO `sys_menu` VALUES (216, 214, '0,150,216', '新增', 4, NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:add', 0, 0, 1, 2, NULL, NULL, '2026-01-11 21:21:13', '2026-01-11 21:21:13', NULL);
INSERT INTO `sys_menu` VALUES (217, 214, '0,150,217', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14', NULL);
INSERT INTO `sys_menu` VALUES (218, 214, '0,150,218', '删除', 4, NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-11 21:21:14', '2026-01-11 21:21:14', NULL);
INSERT INTO `sys_menu` VALUES (219, 150, '0,150', '商品类型（属性/规格）', 1, 'PmsSpuAttribute', 'pms-spu-attribute', 'aioveuMallPmsSpuAttribute/pms-spu-attribute/index', NULL, 0, 0, 1, 8, NULL, NULL, '2026-01-11 21:58:59', '2026-01-11 21:58:59', NULL);
INSERT INTO `sys_menu` VALUES (220, 219, '0,150,220', '查询', 4, NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:query', 0, 0, 1, 1, NULL, NULL, '2026-01-11 21:58:59', '2026-01-11 21:58:59', NULL);
INSERT INTO `sys_menu` VALUES (221, 219, '0,150,221', '新增', 4, NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:add', 0, 0, 1, 2, NULL, NULL, '2026-01-11 21:58:59', '2026-01-11 21:58:59', NULL);
INSERT INTO `sys_menu` VALUES (222, 219, '0,150,222', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00', NULL);
INSERT INTO `sys_menu` VALUES (223, 219, '0,150,223', '删除', 4, NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-11 21:59:00', '2026-01-11 21:59:00', NULL);
INSERT INTO `sys_menu` VALUES (224, 151, '0,151', '广告', 1, 'SmsAdvert', 'sms-advert', 'aioveuMallSmsAdvert/sms-advert/index', NULL, 0, 0, 1, 1, NULL, NULL, '2026-01-12 10:34:19', '2026-01-12 10:34:19', NULL);
INSERT INTO `sys_menu` VALUES (225, 224, '0,151,225', '查询', 4, NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:query', 0, 0, 1, 1, NULL, NULL, '2026-01-12 10:34:19', '2026-01-12 10:34:19', NULL);
INSERT INTO `sys_menu` VALUES (226, 224, '0,151,226', '新增', 4, NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:add', 0, 0, 1, 2, NULL, NULL, '2026-01-12 10:34:19', '2026-01-12 10:34:19', NULL);
INSERT INTO `sys_menu` VALUES (227, 224, '0,151,227', '编辑', 4, NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20', NULL);
INSERT INTO `sys_menu` VALUES (228, 224, '0,151,228', '删除', 4, NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-12 10:34:20', '2026-01-12 10:34:20', NULL);
INSERT INTO `sys_menu` VALUES (229, 151, '0,151', '优惠券', 1, 'SmsCoupon', 'sms-coupon', 'aioveuMallSmsCoupon/sms-coupon/index', NULL, 0, 0, 1, 2, NULL, NULL, '2026-01-12 11:03:17', '2026-01-12 11:03:17', NULL);
INSERT INTO `sys_menu` VALUES (230, 229, '0,151,230', '查询', 4, NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:query', 0, 0, 1, 1, NULL, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18', NULL);
INSERT INTO `sys_menu` VALUES (231, 229, '0,151,231', '新增', 4, NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:add', 0, 0, 1, 2, NULL, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18', NULL);
INSERT INTO `sys_menu` VALUES (232, 229, '0,151,232', '编辑', 4, NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18', NULL);
INSERT INTO `sys_menu` VALUES (233, 229, '0,151,233', '删除', 4, NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-12 11:03:18', '2026-01-12 11:03:18', NULL);
INSERT INTO `sys_menu` VALUES (234, 151, '0,151', '优惠券领取/使用记录', 1, 'SmsCouponHistory', 'sms-coupon-history', 'aioveuMallSmsCouponHistory/sms-coupon-history/index', NULL, 0, 0, 1, 3, NULL, NULL, '2026-01-12 11:49:08', '2026-01-12 11:49:08', NULL);
INSERT INTO `sys_menu` VALUES (235, 234, '0,151,235', '查询', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:query', 0, 0, 1, 1, NULL, NULL, '2026-01-12 11:49:08', '2026-01-12 11:49:08', NULL);
INSERT INTO `sys_menu` VALUES (236, 234, '0,151,236', '新增', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:add', 0, 0, 1, 2, NULL, NULL, '2026-01-12 11:49:08', '2026-01-12 11:49:08', NULL);
INSERT INTO `sys_menu` VALUES (237, 234, '0,151,237', '编辑', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-12 11:49:08', '2026-01-12 11:49:08', NULL);
INSERT INTO `sys_menu` VALUES (238, 234, '0,151,238', '删除', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-12 11:49:09', '2026-01-12 11:49:09', NULL);
INSERT INTO `sys_menu` VALUES (239, 151, '0,151', '优惠券适用的具体商品', 1, 'SmsCouponSpu', 'sms-coupon-spu', 'aioveuMallSmsCouponSpu/sms-coupon-spu/index', NULL, 0, 0, 1, 4, NULL, NULL, '2026-01-12 12:10:39', '2026-01-12 12:10:39', NULL);
INSERT INTO `sys_menu` VALUES (240, 239, '0,151,240', '查询', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:query', 0, 0, 1, 1, NULL, NULL, '2026-01-12 12:10:39', '2026-01-12 12:10:39', NULL);
INSERT INTO `sys_menu` VALUES (241, 239, '0,151,241', '新增', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:add', 0, 0, 1, 2, NULL, NULL, '2026-01-12 12:10:40', '2026-01-12 12:10:40', NULL);
INSERT INTO `sys_menu` VALUES (242, 239, '0,151,242', '编辑', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-12 12:10:40', '2026-01-12 12:10:40', NULL);
INSERT INTO `sys_menu` VALUES (243, 239, '0,151,243', '删除', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-12 12:10:40', '2026-01-12 12:10:40', NULL);
INSERT INTO `sys_menu` VALUES (244, 151, '0,151', '优惠券适用的具体分类', 1, 'SmsCouponSpuCategory', 'sms-coupon-spu-category', 'aioveuMallSmsCouponSpuCategory/sms-coupon-spu-category/index', NULL, 0, 0, 1, 5, NULL, NULL, '2026-01-12 13:03:27', '2026-01-12 13:03:27', NULL);
INSERT INTO `sys_menu` VALUES (245, 244, '0,151,245', '查询', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:query', 0, 0, 1, 1, NULL, NULL, '2026-01-12 13:03:27', '2026-01-12 13:03:27', NULL);
INSERT INTO `sys_menu` VALUES (246, 244, '0,151,246', '新增', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:add', 0, 0, 1, 2, NULL, NULL, '2026-01-12 13:03:27', '2026-01-12 13:03:27', NULL);
INSERT INTO `sys_menu` VALUES (247, 244, '0,151,247', '编辑', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-12 13:03:27', '2026-01-12 13:03:27', NULL);
INSERT INTO `sys_menu` VALUES (248, 244, '0,151,248', '删除', 4, NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-12 13:03:28', '2026-01-12 13:03:28', NULL);
INSERT INTO `sys_menu` VALUES (249, 152, '0,152', '会员', 1, 'UmsMember', 'ums-member', 'aioveuMallUmsMember/ums-member/index', NULL, 0, 0, 1, 1, NULL, NULL, '2026-01-12 14:35:51', '2026-01-12 14:35:51', NULL);
INSERT INTO `sys_menu` VALUES (250, 249, '0,152,250', '查询', 4, NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:query', 0, 0, 1, 1, NULL, NULL, '2026-01-12 14:35:51', '2026-01-12 14:35:51', NULL);
INSERT INTO `sys_menu` VALUES (251, 249, '0,152,251', '新增', 4, NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:add', 0, 0, 1, 2, NULL, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52', NULL);
INSERT INTO `sys_menu` VALUES (252, 249, '0,152,252', '编辑', 4, NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52', NULL);
INSERT INTO `sys_menu` VALUES (253, 249, '0,152,253', '删除', 4, NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-12 14:35:52', '2026-01-12 14:35:52', NULL);
INSERT INTO `sys_menu` VALUES (254, 152, '0,152', '会员收货地址', 1, 'UmsMemberAddress', 'ums-member-address', 'aioveuMallUmsMemberAddress/ums-member-address/index', NULL, 0, 0, 1, 2, NULL, NULL, '2026-01-12 15:22:21', '2026-01-12 15:22:21', NULL);
INSERT INTO `sys_menu` VALUES (255, 254, '0,152,255', '查询', 4, NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:query', 0, 0, 1, 1, NULL, NULL, '2026-01-12 15:22:21', '2026-01-12 15:22:21', NULL);
INSERT INTO `sys_menu` VALUES (256, 254, '0,152,256', '新增', 4, NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:add', 0, 0, 1, 2, NULL, NULL, '2026-01-12 15:22:21', '2026-01-12 15:22:21', NULL);
INSERT INTO `sys_menu` VALUES (257, 254, '0,152,257', '编辑', 4, NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22', NULL);
INSERT INTO `sys_menu` VALUES (258, 254, '0,152,258', '删除', 4, NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-12 15:22:22', '2026-01-12 15:22:22', NULL);
INSERT INTO `sys_menu` VALUES (259, 150, '0,150', '商品上架', 1, 'GoodsDetail', 'pms-detail', 'aioveuMallPmsGoods/pms-detail/index', NULL, 1, 0, 1, 0, 'bilibili', NULL, '2026-01-17 17:51:13', '2026-01-27 18:01:23', NULL);
INSERT INTO `sys_menu` VALUES (260, 0, '0', '退款管理', 2, NULL, '/refund', 'Layout', NULL, 0, 1, 1, 5, 'cnblogs', NULL, '2026-01-31 16:05:24', '2026-01-31 16:05:24', NULL);
INSERT INTO `sys_menu` VALUES (261, 260, '0,260', '订单退款申请', 1, 'RefundOrder', 'refund-order', 'aioveuMallRefundOrder/refund-order/index', NULL, 0, 0, 1, 1, NULL, NULL, '2026-01-31 16:16:36', '2026-01-31 16:16:36', NULL);
INSERT INTO `sys_menu` VALUES (262, 261, '0,260,262', '查询', 4, NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:query', 0, 0, 1, 1, NULL, NULL, '2026-01-31 16:16:36', '2026-01-31 16:16:36', NULL);
INSERT INTO `sys_menu` VALUES (263, 261, '0,260,263', '新增', 4, NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:add', 0, 0, 1, 2, NULL, NULL, '2026-01-31 16:16:36', '2026-01-31 16:16:36', NULL);
INSERT INTO `sys_menu` VALUES (264, 261, '0,260,264', '编辑', 4, NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-31 16:16:36', '2026-01-31 16:16:36', NULL);
INSERT INTO `sys_menu` VALUES (265, 261, '0,260,265', '删除', 4, NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-31 16:16:36', '2026-01-31 16:16:36', NULL);
INSERT INTO `sys_menu` VALUES (266, 260, '0,260', '退款商品明细', 1, 'RefundItem', 'refund-item', 'aioveuMallRefundItem/refund-item/index', NULL, 0, 0, 1, 2, NULL, NULL, '2026-01-31 16:39:07', '2026-01-31 16:39:07', NULL);
INSERT INTO `sys_menu` VALUES (267, 266, '0,260,267', '查询', 4, NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:query', 0, 0, 1, 1, NULL, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08', NULL);
INSERT INTO `sys_menu` VALUES (268, 266, '0,260,268', '新增', 4, NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:add', 0, 0, 1, 2, NULL, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08', NULL);
INSERT INTO `sys_menu` VALUES (269, 266, '0,260,269', '编辑', 4, NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08', NULL);
INSERT INTO `sys_menu` VALUES (270, 266, '0,260,270', '删除', 4, NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-31 16:39:08', '2026-01-31 16:39:08', NULL);
INSERT INTO `sys_menu` VALUES (271, 260, '0,260', '退款物流信息（用于退货）', 1, 'RefundDelivery', 'refund-delivery', 'aioveuMallRefundDelivery/refund-delivery/index', NULL, 0, 0, 1, 3, NULL, NULL, '2026-01-31 17:56:18', '2026-01-31 17:56:18', NULL);
INSERT INTO `sys_menu` VALUES (272, 271, '0,260,272', '查询', 4, NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:query', 0, 0, 1, 1, NULL, NULL, '2026-01-31 17:56:18', '2026-01-31 17:56:18', NULL);
INSERT INTO `sys_menu` VALUES (273, 271, '0,260,273', '新增', 4, NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:add', 0, 0, 1, 2, NULL, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19', NULL);
INSERT INTO `sys_menu` VALUES (274, 271, '0,260,274', '编辑', 4, NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19', NULL);
INSERT INTO `sys_menu` VALUES (275, 271, '0,260,275', '删除', 4, NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-31 17:56:19', '2026-01-31 17:56:19', NULL);
INSERT INTO `sys_menu` VALUES (276, 260, '0,260', '退款操作记录（用于审计）', 1, 'RefundOperationLog', 'refund-operation-log', 'aioveuMallRefundOperationLog/refund-operation-log/index', NULL, 0, 0, 1, 4, NULL, NULL, '2026-01-31 18:13:51', '2026-01-31 18:13:51', NULL);
INSERT INTO `sys_menu` VALUES (277, 276, '0,260,277', '查询', 4, NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:query', 0, 0, 1, 1, NULL, NULL, '2026-01-31 18:13:51', '2026-01-31 18:13:51', NULL);
INSERT INTO `sys_menu` VALUES (278, 276, '0,260,278', '新增', 4, NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:add', 0, 0, 1, 2, NULL, NULL, '2026-01-31 18:13:51', '2026-01-31 18:13:51', NULL);
INSERT INTO `sys_menu` VALUES (279, 276, '0,260,279', '编辑', 4, NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:edit', 0, 0, 1, 3, NULL, NULL, '2026-01-31 18:13:51', '2026-01-31 18:13:51', NULL);
INSERT INTO `sys_menu` VALUES (280, 276, '0,260,280', '删除', 4, NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:delete', 0, 0, 1, 4, NULL, NULL, '2026-01-31 18:13:52', '2026-01-31 18:13:52', NULL);
INSERT INTO `sys_menu` VALUES (281, 260, '0,260', '退款凭证图片', 1, 'RefundProof', 'refund-proof', 'aioveuMallRefundProof/refund-proof/index', NULL, 0, 0, 1, 5, NULL, NULL, '2026-02-01 12:35:36', '2026-02-01 12:35:36', NULL);
INSERT INTO `sys_menu` VALUES (282, 281, '0,260,282', '查询', 4, NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:query', 0, 0, 1, 1, NULL, NULL, '2026-02-01 12:35:36', '2026-02-01 12:35:36', NULL);
INSERT INTO `sys_menu` VALUES (283, 281, '0,260,283', '新增', 4, NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:add', 0, 0, 1, 2, NULL, NULL, '2026-02-01 12:35:36', '2026-02-01 12:35:36', NULL);
INSERT INTO `sys_menu` VALUES (284, 281, '0,260,284', '编辑', 4, NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37', NULL);
INSERT INTO `sys_menu` VALUES (285, 281, '0,260,285', '删除', 4, NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-01 12:35:37', '2026-02-01 12:35:37', NULL);
INSERT INTO `sys_menu` VALUES (286, 260, '0,260', '退款支付记录', 1, 'RefundPayment', 'refund-payment', 'aioveuMallRefundPayment/refund-payment/index', NULL, 0, 0, 1, 6, NULL, NULL, '2026-02-01 13:24:53', '2026-02-01 13:24:53', NULL);
INSERT INTO `sys_menu` VALUES (287, 286, '0,260,287', '查询', 4, NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:query', 0, 0, 1, 1, NULL, NULL, '2026-02-01 13:24:53', '2026-02-01 13:24:53', NULL);
INSERT INTO `sys_menu` VALUES (288, 286, '0,260,288', '新增', 4, NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:add', 0, 0, 1, 2, NULL, NULL, '2026-02-01 13:24:53', '2026-02-01 13:24:53', NULL);
INSERT INTO `sys_menu` VALUES (289, 286, '0,260,289', '编辑', 4, NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54', NULL);
INSERT INTO `sys_menu` VALUES (290, 286, '0,260,290', '删除', 4, NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-01 13:24:54', '2026-02-01 13:24:54', NULL);
INSERT INTO `sys_menu` VALUES (291, 260, '0,260', '退款原因分类', 1, 'RefundReason', 'refund-reason', 'aioveuMallRefundReason/refund-reason/index', NULL, 0, 0, 1, 7, NULL, NULL, '2026-02-01 13:51:04', '2026-02-01 13:51:04', NULL);
INSERT INTO `sys_menu` VALUES (292, 291, '0,260,292', '查询', 4, NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:query', 0, 0, 1, 1, NULL, NULL, '2026-02-01 13:51:04', '2026-02-01 13:51:04', NULL);
INSERT INTO `sys_menu` VALUES (293, 291, '0,260,293', '新增', 4, NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:add', 0, 0, 1, 2, NULL, NULL, '2026-02-01 13:51:04', '2026-02-01 13:51:04', NULL);
INSERT INTO `sys_menu` VALUES (294, 291, '0,260,294', '编辑', 4, NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-01 13:51:04', '2026-02-01 13:51:04', NULL);
INSERT INTO `sys_menu` VALUES (295, 291, '0,260,295', '删除', 4, NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-01 13:51:05', '2026-02-01 13:51:05', NULL);
INSERT INTO `sys_menu` VALUES (296, 0, '0', '支付管理', 2, NULL, '/pay', 'Layout', NULL, 0, 1, 1, 6, 'java', NULL, '2026-02-02 17:15:43', '2026-02-02 17:15:43', NULL);
INSERT INTO `sys_menu` VALUES (297, 296, '0,296', '支付订单', 1, 'PayOrder', 'pay-order', 'aioveuMallPayOrder/pay-order/index', NULL, 0, 0, 1, 1, NULL, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50', NULL);
INSERT INTO `sys_menu` VALUES (298, 297, '0,296,298', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:query', 0, 0, 1, 1, NULL, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50', NULL);
INSERT INTO `sys_menu` VALUES (299, 297, '0,296,299', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:add', 0, 0, 1, 2, NULL, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50', NULL);
INSERT INTO `sys_menu` VALUES (300, 297, '0,296,300', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50', NULL);
INSERT INTO `sys_menu` VALUES (301, 297, '0,296,301', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-02 17:18:50', '2026-02-02 17:18:50', NULL);
INSERT INTO `sys_menu` VALUES (302, 296, '0,296', '退款记录', 1, 'PayRefundRecord', 'pay-refund-record', 'aioveuMallPayRefundRecord/pay-refund-record/index', NULL, 0, 0, 1, 2, NULL, NULL, '2026-02-02 18:41:32', '2026-02-02 18:41:32', NULL);
INSERT INTO `sys_menu` VALUES (303, 302, '0,296,303', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:query', 0, 0, 1, 1, NULL, NULL, '2026-02-02 18:41:32', '2026-02-02 18:41:32', NULL);
INSERT INTO `sys_menu` VALUES (304, 302, '0,296,304', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:add', 0, 0, 1, 2, NULL, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33', NULL);
INSERT INTO `sys_menu` VALUES (305, 302, '0,296,305', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33', NULL);
INSERT INTO `sys_menu` VALUES (306, 302, '0,296,306', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-02 18:41:33', '2026-02-02 18:41:33', NULL);
INSERT INTO `sys_menu` VALUES (307, 296, '0,296', '支付渠道配置', 1, 'PayChannelConfig', 'pay-channel-config', 'aioveuMallPayChannelConfig/pay-channel-config/index', NULL, 0, 0, 1, 3, NULL, NULL, '2026-02-02 19:40:23', '2026-02-02 19:40:23', NULL);
INSERT INTO `sys_menu` VALUES (308, 307, '0,296,308', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:query', 0, 0, 1, 1, NULL, NULL, '2026-02-02 19:40:24', '2026-02-02 19:40:24', NULL);
INSERT INTO `sys_menu` VALUES (309, 307, '0,296,309', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:add', 0, 0, 1, 2, NULL, NULL, '2026-02-02 19:40:24', '2026-02-02 19:40:24', NULL);
INSERT INTO `sys_menu` VALUES (310, 307, '0,296,310', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-02 19:40:24', '2026-02-02 19:40:24', NULL);
INSERT INTO `sys_menu` VALUES (311, 307, '0,296,311', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-02 19:40:25', '2026-02-02 19:40:25', NULL);
INSERT INTO `sys_menu` VALUES (312, 296, '0,296', '支付对账', 1, 'PayReconciliation', 'pay-reconciliation', 'aioveuMallPayReconciliation/pay-reconciliation/index', NULL, 0, 0, 1, 4, NULL, NULL, '2026-02-02 20:27:14', '2026-02-02 20:27:14', NULL);
INSERT INTO `sys_menu` VALUES (313, 312, '0,296,313', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:query', 0, 0, 1, 1, NULL, NULL, '2026-02-02 20:27:15', '2026-02-02 20:27:15', NULL);
INSERT INTO `sys_menu` VALUES (314, 312, '0,296,314', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:add', 0, 0, 1, 2, NULL, NULL, '2026-02-02 20:27:15', '2026-02-02 20:27:15', NULL);
INSERT INTO `sys_menu` VALUES (315, 312, '0,296,315', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-02 20:27:15', '2026-02-02 20:27:15', NULL);
INSERT INTO `sys_menu` VALUES (316, 312, '0,296,316', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-02 20:27:15', '2026-02-02 20:27:15', NULL);
INSERT INTO `sys_menu` VALUES (317, 296, '0,296', '对账明细', 1, 'PayReconciliationDetail', 'pay-reconciliation-detail', 'aioveuMallPayReconciliationDetail/pay-reconciliation-detail/index', NULL, 0, 0, 1, 5, NULL, NULL, '2026-02-09 13:51:56', '2026-02-09 13:51:56', NULL);
INSERT INTO `sys_menu` VALUES (318, 317, '0,296,318', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliationDetail:pay-reconciliation-detail:query', 0, 0, 1, 1, NULL, NULL, '2026-02-09 13:51:56', '2026-02-09 13:51:56', NULL);
INSERT INTO `sys_menu` VALUES (319, 317, '0,296,319', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliationDetail:pay-reconciliation-detail:add', 0, 0, 1, 2, NULL, NULL, '2026-02-09 13:51:56', '2026-02-09 13:51:56', NULL);
INSERT INTO `sys_menu` VALUES (320, 317, '0,296,320', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliationDetail:pay-reconciliation-detail:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-09 13:51:56', '2026-02-09 13:51:56', NULL);
INSERT INTO `sys_menu` VALUES (321, 317, '0,296,321', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayReconciliationDetail:pay-reconciliation-detail:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-09 13:51:57', '2026-02-09 13:51:57', NULL);
INSERT INTO `sys_menu` VALUES (322, 296, '0,296', '支付流水', 1, 'PayFlow', 'pay-flow', 'aioveuMallPayFlow/pay-flow/index', NULL, 0, 0, 1, 6, NULL, NULL, '2026-02-09 15:45:07', '2026-02-09 15:45:07', NULL);
INSERT INTO `sys_menu` VALUES (323, 322, '0,296,323', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayFlow:pay-flow:query', 0, 0, 1, 1, NULL, NULL, '2026-02-09 15:45:08', '2026-02-09 15:45:08', NULL);
INSERT INTO `sys_menu` VALUES (324, 322, '0,296,324', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayFlow:pay-flow:add', 0, 0, 1, 2, NULL, NULL, '2026-02-09 15:45:08', '2026-02-09 15:45:08', NULL);
INSERT INTO `sys_menu` VALUES (325, 322, '0,296,325', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayFlow:pay-flow:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-09 15:45:08', '2026-02-09 15:45:08', NULL);
INSERT INTO `sys_menu` VALUES (326, 322, '0,296,326', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayFlow:pay-flow:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-09 15:45:08', '2026-02-09 15:45:08', NULL);
INSERT INTO `sys_menu` VALUES (327, 296, '0,296', '支付通知', 1, 'PayNotify', 'pay-notify', 'aioveuMallPayNotify/pay-notify/index', NULL, 0, 0, 1, 7, NULL, NULL, '2026-02-09 16:08:01', '2026-02-09 16:08:01', NULL);
INSERT INTO `sys_menu` VALUES (328, 327, '0,296,328', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayNotify:pay-notify:query', 0, 0, 1, 1, NULL, NULL, '2026-02-09 16:08:02', '2026-02-09 16:08:02', NULL);
INSERT INTO `sys_menu` VALUES (329, 327, '0,296,329', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayNotify:pay-notify:add', 0, 0, 1, 2, NULL, NULL, '2026-02-09 16:08:02', '2026-02-09 16:08:02', NULL);
INSERT INTO `sys_menu` VALUES (330, 327, '0,296,330', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayNotify:pay-notify:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-09 16:08:02', '2026-02-09 16:08:02', NULL);
INSERT INTO `sys_menu` VALUES (331, 327, '0,296,331', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayNotify:pay-notify:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-09 16:08:03', '2026-02-09 16:08:03', NULL);
INSERT INTO `sys_menu` VALUES (332, 296, '0,296', '支付账户', 1, 'PayAccount', 'pay-account', 'aioveuMallPayAccount/pay-account/index', NULL, 0, 0, 1, 8, NULL, NULL, '2026-02-10 16:05:59', '2026-02-10 16:05:59', NULL);
INSERT INTO `sys_menu` VALUES (333, 332, '0,296,333', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayAccount:pay-account:query', 0, 0, 1, 1, NULL, NULL, '2026-02-10 16:05:59', '2026-02-10 16:05:59', NULL);
INSERT INTO `sys_menu` VALUES (334, 332, '0,296,334', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayAccount:pay-account:add', 0, 0, 1, 2, NULL, NULL, '2026-02-10 16:05:59', '2026-02-10 16:05:59', NULL);
INSERT INTO `sys_menu` VALUES (335, 332, '0,296,335', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayAccount:pay-account:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00', NULL);
INSERT INTO `sys_menu` VALUES (336, 332, '0,296,336', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayAccount:pay-account:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-10 16:06:00', '2026-02-10 16:06:00', NULL);
INSERT INTO `sys_menu` VALUES (337, 296, '0,296', '账户流水', 1, 'PayAccountFlow', 'pay-account-flow', 'aioveuMallPayAccountFlow/pay-account-flow/index', NULL, 0, 0, 1, 9, NULL, NULL, '2026-02-10 16:18:26', '2026-02-10 16:18:26', NULL);
INSERT INTO `sys_menu` VALUES (338, 337, '0,296,338', '查询', 4, NULL, NULL, NULL, 'aioveuMallPayAccountFlow:pay-account-flow:query', 0, 0, 1, 1, NULL, NULL, '2026-02-10 16:18:26', '2026-02-10 16:18:26', NULL);
INSERT INTO `sys_menu` VALUES (339, 337, '0,296,339', '新增', 4, NULL, NULL, NULL, 'aioveuMallPayAccountFlow:pay-account-flow:add', 0, 0, 1, 2, NULL, NULL, '2026-02-10 16:18:26', '2026-02-10 16:18:26', NULL);
INSERT INTO `sys_menu` VALUES (340, 337, '0,296,340', '编辑', 4, NULL, NULL, NULL, 'aioveuMallPayAccountFlow:pay-account-flow:edit', 0, 0, 1, 3, NULL, NULL, '2026-02-10 16:18:26', '2026-02-10 16:18:26', NULL);
INSERT INTO `sys_menu` VALUES (341, 337, '0,296,341', '删除', 4, NULL, NULL, NULL, 'aioveuMallPayAccountFlow:pay-account-flow:delete', 0, 0, 1, 4, NULL, NULL, '2026-02-10 16:18:27', '2026-02-10 16:18:27', NULL);
INSERT INTO `sys_menu` VALUES (342, 151, '0,151', '首页分类配置', 1, 'SmsHomeCategory', 'sms-home-category', 'aioveuMallSmsHomeCategory/sms-home-category/index', NULL, 1, 1, 1, 6, 'project', NULL, '2026-03-08 11:58:38', '2026-03-08 11:58:38', NULL);
INSERT INTO `sys_menu` VALUES (343, 151, '0,151', '首页广告配置', 1, 'SmsHomeAdvert', 'sms-home-advert', 'aioveuMallSmsHomeAdvert/sms-home-advert/index', NULL, 1, 1, 1, 7, 'message', NULL, '2026-03-08 11:59:09', '2026-03-08 11:59:09', NULL);
INSERT INTO `sys_menu` VALUES (344, 342, '0,151,342', ' 查询', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:query', 0, 1, 1, 1, NULL, NULL, '2026-03-08 11:59:26', '2026-03-08 11:59:26', NULL);
INSERT INTO `sys_menu` VALUES (345, 342, '0,151,342', '新增', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:add', 0, 1, 1, 2, NULL, NULL, '2026-03-08 11:59:45', '2026-03-08 11:59:45', NULL);
INSERT INTO `sys_menu` VALUES (346, 342, '0,151,342', ' 编辑', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-08 12:00:01', '2026-03-08 12:00:01', NULL);
INSERT INTO `sys_menu` VALUES (347, 342, '0,151,342', ' 删除', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-08 12:00:18', '2026-03-08 12:00:18', NULL);
INSERT INTO `sys_menu` VALUES (348, 343, '0,151,343', '查询', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:query', 0, 1, 1, 1, NULL, NULL, '2026-03-08 12:01:57', '2026-03-08 12:01:57', NULL);
INSERT INTO `sys_menu` VALUES (349, 343, '0,151,343', '新增', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:add', 0, 1, 1, 2, NULL, NULL, '2026-03-08 12:02:14', '2026-03-08 12:02:14', NULL);
INSERT INTO `sys_menu` VALUES (350, 343, '0,151,343', ' 编辑', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-08 12:02:28', '2026-03-08 12:02:28', NULL);
INSERT INTO `sys_menu` VALUES (351, 343, '0,151,343', '删除', 4, NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-08 12:02:41', '2026-03-08 12:02:41', NULL);

-- ----------------------------
-- Table structure for sys_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
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
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_notice
-- ----------------------------
INSERT INTO `sys_notice` VALUES (1, 'v2.12.0 新增系统日志，访问趋势统计功能。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 1, 'L', 1, '2', 1, 1, '2025-12-30 22:14:07', '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 1, '2025-12-30 22:14:07', 0);
INSERT INTO `sys_notice` VALUES (2, 'v2.13.0 新增菜单搜索。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 1, 'L', 1, '2', 1, 1, '2025-12-30 22:14:07', '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 1, '2025-12-30 22:14:07', 0);
INSERT INTO `sys_notice` VALUES (3, 'v2.14.0 新增个人中心。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 1, 'L', 1, '2', 2, 1, '2025-12-30 22:14:07', '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 0);
INSERT INTO `sys_notice` VALUES (4, 'v2.15.0 登录页面改造。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 1, 'L', 1, '2', 2, 1, '2025-12-30 22:14:07', '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 0);
INSERT INTO `sys_notice` VALUES (5, 'v2.16.0 通知公告、字典翻译组件。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 1, 'L', 1, '2', 2, 1, '2025-12-30 22:14:07', '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 0);
INSERT INTO `sys_notice` VALUES (6, '系统将于本周六凌晨 2 点进行维护，预计维护时间为 2 小时。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 2, 'H', 1, '2', 2, 1, '2025-12-30 22:14:07', '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 0);
INSERT INTO `sys_notice` VALUES (7, '最近发现一些钓鱼邮件，请大家提高警惕，不要点击陌生链接。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 3, 'L', 1, '2', 2, 1, '2025-12-30 22:14:07', '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 2, '2025-12-30 22:14:07', 0);
INSERT INTO `sys_notice` VALUES (8, '国庆假期从 10 月 1 日至 10 月 7 日放假，共 7 天。', '<p>1. 消息通知</p><p>2. 字典重构</p><p>3. 代码生成</p>', 4, 'L', 1, '2', 2, 1, '2025-12-30 22:14:08', '2025-12-30 22:14:08', 2, '2025-12-30 22:14:08', 2, '2025-12-30 22:14:08', 0);
INSERT INTO `sys_notice` VALUES (9, '公司将在 10 月 15 日举办新产品发布会，敬请期待。', '公司将在 10 月 15 日举办新产品发布会，敬请期待。', 5, 'H', 1, '2', 2, 1, '2025-12-30 22:14:08', '2025-12-30 22:14:08', 2, '2025-12-30 22:14:08', 2, '2025-12-30 22:14:08', 0);
INSERT INTO `sys_notice` VALUES (10, 'v2.16.1 版本发布。', 'v2.16.1 版本修复了 WebSocket 重复连接导致的后台线程阻塞问题，优化了通知公告。', 1, 'M', 1, '2', 2, 1, '2025-12-30 22:14:08', '2025-12-30 22:14:08', 2, '2025-12-30 22:14:08', 2, '2025-12-30 22:14:08', 0);

-- ----------------------------
-- Table structure for sys_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色名称',
  `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '角色编码',
  `sort` int NULL DEFAULT NULL COMMENT '显示顺序',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '角色状态(1-正常 0-停用)',
  `data_scope` tinyint NULL DEFAULT NULL COMMENT '数据权限(1-所有数据 2-部门及子部门数据 3-本部门数据 4-本人数据)',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人 ID',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识(0-未删除 1-已删除)',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_name`(`name` ASC) USING BTREE COMMENT '角色名称唯一索引',
  UNIQUE INDEX `uk_code`(`code` ASC) USING BTREE COMMENT '角色编码唯一索引'
) ENGINE = InnoDB AUTO_INCREMENT = 13 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES (1, '超级管理员', 'ROOT', 1, 1, 1, NULL, '2025-12-30 22:13:51', NULL, '2025-12-30 22:13:51', 0);
INSERT INTO `sys_role` VALUES (2, '系统管理员', 'ADMIN', 2, 1, 1, NULL, '2025-12-30 22:13:51', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (3, '访问游客', 'GUEST', 3, 1, 3, NULL, '2025-12-30 22:13:51', NULL, '2025-12-30 22:13:51', 0);
INSERT INTO `sys_role` VALUES (4, '系统管理员1', 'ADMIN1', 4, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (5, '系统管理员2', 'ADMIN2', 5, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (6, '系统管理员3', 'ADMIN3', 6, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (7, '系统管理员4', 'ADMIN4', 7, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (8, '系统管理员5', 'ADMIN5', 8, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (9, '系统管理员6', 'ADMIN6', 9, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (10, '系统管理员7', 'ADMIN7', 10, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (11, '系统管理员8', 'ADMIN8', 11, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);
INSERT INTO `sys_role` VALUES (12, '系统管理员9', 'ADMIN9', 12, 1, 1, NULL, '2025-12-30 22:13:52', NULL, NULL, 0);

-- ----------------------------
-- Table structure for sys_role_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu`  (
  `role_id` bigint NOT NULL COMMENT '角色ID',
  `menu_id` bigint NOT NULL COMMENT '菜单ID',
  UNIQUE INDEX `uk_roleid_menuid`(`role_id` ASC, `menu_id` ASC) USING BTREE COMMENT '角色菜单唯一索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '角色和菜单关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES (2, 1);
INSERT INTO `sys_role_menu` VALUES (2, 2);
INSERT INTO `sys_role_menu` VALUES (2, 3);
INSERT INTO `sys_role_menu` VALUES (2, 4);
INSERT INTO `sys_role_menu` VALUES (2, 5);
INSERT INTO `sys_role_menu` VALUES (2, 6);
INSERT INTO `sys_role_menu` VALUES (2, 20);
INSERT INTO `sys_role_menu` VALUES (2, 21);
INSERT INTO `sys_role_menu` VALUES (2, 22);
INSERT INTO `sys_role_menu` VALUES (2, 23);
INSERT INTO `sys_role_menu` VALUES (2, 24);
INSERT INTO `sys_role_menu` VALUES (2, 26);
INSERT INTO `sys_role_menu` VALUES (2, 30);
INSERT INTO `sys_role_menu` VALUES (2, 31);
INSERT INTO `sys_role_menu` VALUES (2, 32);
INSERT INTO `sys_role_menu` VALUES (2, 33);
INSERT INTO `sys_role_menu` VALUES (2, 36);
INSERT INTO `sys_role_menu` VALUES (2, 37);
INSERT INTO `sys_role_menu` VALUES (2, 38);
INSERT INTO `sys_role_menu` VALUES (2, 39);
INSERT INTO `sys_role_menu` VALUES (2, 40);
INSERT INTO `sys_role_menu` VALUES (2, 41);
INSERT INTO `sys_role_menu` VALUES (2, 70);
INSERT INTO `sys_role_menu` VALUES (2, 71);
INSERT INTO `sys_role_menu` VALUES (2, 72);
INSERT INTO `sys_role_menu` VALUES (2, 73);
INSERT INTO `sys_role_menu` VALUES (2, 74);
INSERT INTO `sys_role_menu` VALUES (2, 75);
INSERT INTO `sys_role_menu` VALUES (2, 76);
INSERT INTO `sys_role_menu` VALUES (2, 77);
INSERT INTO `sys_role_menu` VALUES (2, 78);
INSERT INTO `sys_role_menu` VALUES (2, 79);
INSERT INTO `sys_role_menu` VALUES (2, 81);
INSERT INTO `sys_role_menu` VALUES (2, 84);
INSERT INTO `sys_role_menu` VALUES (2, 88);
INSERT INTO `sys_role_menu` VALUES (2, 89);
INSERT INTO `sys_role_menu` VALUES (2, 90);
INSERT INTO `sys_role_menu` VALUES (2, 95);
INSERT INTO `sys_role_menu` VALUES (2, 97);
INSERT INTO `sys_role_menu` VALUES (2, 102);
INSERT INTO `sys_role_menu` VALUES (2, 105);
INSERT INTO `sys_role_menu` VALUES (2, 106);
INSERT INTO `sys_role_menu` VALUES (2, 107);
INSERT INTO `sys_role_menu` VALUES (2, 108);
INSERT INTO `sys_role_menu` VALUES (2, 109);
INSERT INTO `sys_role_menu` VALUES (2, 110);
INSERT INTO `sys_role_menu` VALUES (2, 111);
INSERT INTO `sys_role_menu` VALUES (2, 112);
INSERT INTO `sys_role_menu` VALUES (2, 117);
INSERT INTO `sys_role_menu` VALUES (2, 118);
INSERT INTO `sys_role_menu` VALUES (2, 119);
INSERT INTO `sys_role_menu` VALUES (2, 120);
INSERT INTO `sys_role_menu` VALUES (2, 121);
INSERT INTO `sys_role_menu` VALUES (2, 122);
INSERT INTO `sys_role_menu` VALUES (2, 123);
INSERT INTO `sys_role_menu` VALUES (2, 124);
INSERT INTO `sys_role_menu` VALUES (2, 125);
INSERT INTO `sys_role_menu` VALUES (2, 126);
INSERT INTO `sys_role_menu` VALUES (2, 127);
INSERT INTO `sys_role_menu` VALUES (2, 128);
INSERT INTO `sys_role_menu` VALUES (2, 129);
INSERT INTO `sys_role_menu` VALUES (2, 130);
INSERT INTO `sys_role_menu` VALUES (2, 133);
INSERT INTO `sys_role_menu` VALUES (2, 134);
INSERT INTO `sys_role_menu` VALUES (2, 135);
INSERT INTO `sys_role_menu` VALUES (2, 136);
INSERT INTO `sys_role_menu` VALUES (2, 137);
INSERT INTO `sys_role_menu` VALUES (2, 138);
INSERT INTO `sys_role_menu` VALUES (2, 139);
INSERT INTO `sys_role_menu` VALUES (2, 140);
INSERT INTO `sys_role_menu` VALUES (2, 141);
INSERT INTO `sys_role_menu` VALUES (2, 142);
INSERT INTO `sys_role_menu` VALUES (2, 143);
INSERT INTO `sys_role_menu` VALUES (2, 144);
INSERT INTO `sys_role_menu` VALUES (2, 145);
INSERT INTO `sys_role_menu` VALUES (2, 146);
INSERT INTO `sys_role_menu` VALUES (2, 147);
INSERT INTO `sys_role_menu` VALUES (2, 148);
INSERT INTO `sys_role_menu` VALUES (2, 149);
INSERT INTO `sys_role_menu` VALUES (2, 150);
INSERT INTO `sys_role_menu` VALUES (2, 151);
INSERT INTO `sys_role_menu` VALUES (2, 152);
INSERT INTO `sys_role_menu` VALUES (2, 153);
INSERT INTO `sys_role_menu` VALUES (2, 154);
INSERT INTO `sys_role_menu` VALUES (2, 155);
INSERT INTO `sys_role_menu` VALUES (2, 156);
INSERT INTO `sys_role_menu` VALUES (2, 157);
INSERT INTO `sys_role_menu` VALUES (2, 158);
INSERT INTO `sys_role_menu` VALUES (2, 159);
INSERT INTO `sys_role_menu` VALUES (2, 160);
INSERT INTO `sys_role_menu` VALUES (2, 161);
INSERT INTO `sys_role_menu` VALUES (2, 162);
INSERT INTO `sys_role_menu` VALUES (2, 163);
INSERT INTO `sys_role_menu` VALUES (2, 164);
INSERT INTO `sys_role_menu` VALUES (2, 165);
INSERT INTO `sys_role_menu` VALUES (2, 166);
INSERT INTO `sys_role_menu` VALUES (2, 167);
INSERT INTO `sys_role_menu` VALUES (2, 168);
INSERT INTO `sys_role_menu` VALUES (2, 169);
INSERT INTO `sys_role_menu` VALUES (2, 170);
INSERT INTO `sys_role_menu` VALUES (2, 171);
INSERT INTO `sys_role_menu` VALUES (2, 172);
INSERT INTO `sys_role_menu` VALUES (2, 173);
INSERT INTO `sys_role_menu` VALUES (2, 174);
INSERT INTO `sys_role_menu` VALUES (2, 175);
INSERT INTO `sys_role_menu` VALUES (2, 176);
INSERT INTO `sys_role_menu` VALUES (2, 177);
INSERT INTO `sys_role_menu` VALUES (2, 178);
INSERT INTO `sys_role_menu` VALUES (2, 179);
INSERT INTO `sys_role_menu` VALUES (2, 180);
INSERT INTO `sys_role_menu` VALUES (2, 181);
INSERT INTO `sys_role_menu` VALUES (2, 182);
INSERT INTO `sys_role_menu` VALUES (2, 183);
INSERT INTO `sys_role_menu` VALUES (2, 184);
INSERT INTO `sys_role_menu` VALUES (2, 185);
INSERT INTO `sys_role_menu` VALUES (2, 186);
INSERT INTO `sys_role_menu` VALUES (2, 187);
INSERT INTO `sys_role_menu` VALUES (2, 188);
INSERT INTO `sys_role_menu` VALUES (2, 189);
INSERT INTO `sys_role_menu` VALUES (2, 190);
INSERT INTO `sys_role_menu` VALUES (2, 191);
INSERT INTO `sys_role_menu` VALUES (2, 192);
INSERT INTO `sys_role_menu` VALUES (2, 193);
INSERT INTO `sys_role_menu` VALUES (2, 194);
INSERT INTO `sys_role_menu` VALUES (2, 195);
INSERT INTO `sys_role_menu` VALUES (2, 196);
INSERT INTO `sys_role_menu` VALUES (2, 197);
INSERT INTO `sys_role_menu` VALUES (2, 198);
INSERT INTO `sys_role_menu` VALUES (2, 199);
INSERT INTO `sys_role_menu` VALUES (2, 200);
INSERT INTO `sys_role_menu` VALUES (2, 201);
INSERT INTO `sys_role_menu` VALUES (2, 202);
INSERT INTO `sys_role_menu` VALUES (2, 203);
INSERT INTO `sys_role_menu` VALUES (2, 204);
INSERT INTO `sys_role_menu` VALUES (2, 205);
INSERT INTO `sys_role_menu` VALUES (2, 206);
INSERT INTO `sys_role_menu` VALUES (2, 207);
INSERT INTO `sys_role_menu` VALUES (2, 208);
INSERT INTO `sys_role_menu` VALUES (2, 209);
INSERT INTO `sys_role_menu` VALUES (2, 210);
INSERT INTO `sys_role_menu` VALUES (2, 211);
INSERT INTO `sys_role_menu` VALUES (2, 212);
INSERT INTO `sys_role_menu` VALUES (2, 213);
INSERT INTO `sys_role_menu` VALUES (2, 214);
INSERT INTO `sys_role_menu` VALUES (2, 215);
INSERT INTO `sys_role_menu` VALUES (2, 216);
INSERT INTO `sys_role_menu` VALUES (2, 217);
INSERT INTO `sys_role_menu` VALUES (2, 218);
INSERT INTO `sys_role_menu` VALUES (2, 219);
INSERT INTO `sys_role_menu` VALUES (2, 220);
INSERT INTO `sys_role_menu` VALUES (2, 221);
INSERT INTO `sys_role_menu` VALUES (2, 222);
INSERT INTO `sys_role_menu` VALUES (2, 223);
INSERT INTO `sys_role_menu` VALUES (2, 224);
INSERT INTO `sys_role_menu` VALUES (2, 225);
INSERT INTO `sys_role_menu` VALUES (2, 226);
INSERT INTO `sys_role_menu` VALUES (2, 227);
INSERT INTO `sys_role_menu` VALUES (2, 228);
INSERT INTO `sys_role_menu` VALUES (2, 229);
INSERT INTO `sys_role_menu` VALUES (2, 230);
INSERT INTO `sys_role_menu` VALUES (2, 231);
INSERT INTO `sys_role_menu` VALUES (2, 232);
INSERT INTO `sys_role_menu` VALUES (2, 233);
INSERT INTO `sys_role_menu` VALUES (2, 234);
INSERT INTO `sys_role_menu` VALUES (2, 235);
INSERT INTO `sys_role_menu` VALUES (2, 236);
INSERT INTO `sys_role_menu` VALUES (2, 237);
INSERT INTO `sys_role_menu` VALUES (2, 238);
INSERT INTO `sys_role_menu` VALUES (2, 239);
INSERT INTO `sys_role_menu` VALUES (2, 240);
INSERT INTO `sys_role_menu` VALUES (2, 241);
INSERT INTO `sys_role_menu` VALUES (2, 242);
INSERT INTO `sys_role_menu` VALUES (2, 243);
INSERT INTO `sys_role_menu` VALUES (2, 244);
INSERT INTO `sys_role_menu` VALUES (2, 245);
INSERT INTO `sys_role_menu` VALUES (2, 246);
INSERT INTO `sys_role_menu` VALUES (2, 247);
INSERT INTO `sys_role_menu` VALUES (2, 248);
INSERT INTO `sys_role_menu` VALUES (2, 249);
INSERT INTO `sys_role_menu` VALUES (2, 250);
INSERT INTO `sys_role_menu` VALUES (2, 251);
INSERT INTO `sys_role_menu` VALUES (2, 252);
INSERT INTO `sys_role_menu` VALUES (2, 253);
INSERT INTO `sys_role_menu` VALUES (2, 254);
INSERT INTO `sys_role_menu` VALUES (2, 255);
INSERT INTO `sys_role_menu` VALUES (2, 256);
INSERT INTO `sys_role_menu` VALUES (2, 257);
INSERT INTO `sys_role_menu` VALUES (2, 258);
INSERT INTO `sys_role_menu` VALUES (2, 259);
INSERT INTO `sys_role_menu` VALUES (2, 260);
INSERT INTO `sys_role_menu` VALUES (2, 261);
INSERT INTO `sys_role_menu` VALUES (2, 262);
INSERT INTO `sys_role_menu` VALUES (2, 263);
INSERT INTO `sys_role_menu` VALUES (2, 264);
INSERT INTO `sys_role_menu` VALUES (2, 265);
INSERT INTO `sys_role_menu` VALUES (2, 266);
INSERT INTO `sys_role_menu` VALUES (2, 267);
INSERT INTO `sys_role_menu` VALUES (2, 268);
INSERT INTO `sys_role_menu` VALUES (2, 269);
INSERT INTO `sys_role_menu` VALUES (2, 270);
INSERT INTO `sys_role_menu` VALUES (2, 271);
INSERT INTO `sys_role_menu` VALUES (2, 272);
INSERT INTO `sys_role_menu` VALUES (2, 273);
INSERT INTO `sys_role_menu` VALUES (2, 274);
INSERT INTO `sys_role_menu` VALUES (2, 275);
INSERT INTO `sys_role_menu` VALUES (2, 276);
INSERT INTO `sys_role_menu` VALUES (2, 277);
INSERT INTO `sys_role_menu` VALUES (2, 278);
INSERT INTO `sys_role_menu` VALUES (2, 279);
INSERT INTO `sys_role_menu` VALUES (2, 280);
INSERT INTO `sys_role_menu` VALUES (2, 281);
INSERT INTO `sys_role_menu` VALUES (2, 282);
INSERT INTO `sys_role_menu` VALUES (2, 283);
INSERT INTO `sys_role_menu` VALUES (2, 284);
INSERT INTO `sys_role_menu` VALUES (2, 285);
INSERT INTO `sys_role_menu` VALUES (2, 286);
INSERT INTO `sys_role_menu` VALUES (2, 287);
INSERT INTO `sys_role_menu` VALUES (2, 288);
INSERT INTO `sys_role_menu` VALUES (2, 289);
INSERT INTO `sys_role_menu` VALUES (2, 290);
INSERT INTO `sys_role_menu` VALUES (2, 291);
INSERT INTO `sys_role_menu` VALUES (2, 292);
INSERT INTO `sys_role_menu` VALUES (2, 293);
INSERT INTO `sys_role_menu` VALUES (2, 294);
INSERT INTO `sys_role_menu` VALUES (2, 295);
INSERT INTO `sys_role_menu` VALUES (2, 296);
INSERT INTO `sys_role_menu` VALUES (2, 297);
INSERT INTO `sys_role_menu` VALUES (2, 298);
INSERT INTO `sys_role_menu` VALUES (2, 299);
INSERT INTO `sys_role_menu` VALUES (2, 300);
INSERT INTO `sys_role_menu` VALUES (2, 301);
INSERT INTO `sys_role_menu` VALUES (2, 302);
INSERT INTO `sys_role_menu` VALUES (2, 303);
INSERT INTO `sys_role_menu` VALUES (2, 304);
INSERT INTO `sys_role_menu` VALUES (2, 305);
INSERT INTO `sys_role_menu` VALUES (2, 306);
INSERT INTO `sys_role_menu` VALUES (2, 307);
INSERT INTO `sys_role_menu` VALUES (2, 308);
INSERT INTO `sys_role_menu` VALUES (2, 309);
INSERT INTO `sys_role_menu` VALUES (2, 310);
INSERT INTO `sys_role_menu` VALUES (2, 311);
INSERT INTO `sys_role_menu` VALUES (2, 312);
INSERT INTO `sys_role_menu` VALUES (2, 313);
INSERT INTO `sys_role_menu` VALUES (2, 314);
INSERT INTO `sys_role_menu` VALUES (2, 315);
INSERT INTO `sys_role_menu` VALUES (2, 316);
INSERT INTO `sys_role_menu` VALUES (2, 317);
INSERT INTO `sys_role_menu` VALUES (2, 318);
INSERT INTO `sys_role_menu` VALUES (2, 319);
INSERT INTO `sys_role_menu` VALUES (2, 320);
INSERT INTO `sys_role_menu` VALUES (2, 321);
INSERT INTO `sys_role_menu` VALUES (2, 322);
INSERT INTO `sys_role_menu` VALUES (2, 323);
INSERT INTO `sys_role_menu` VALUES (2, 324);
INSERT INTO `sys_role_menu` VALUES (2, 325);
INSERT INTO `sys_role_menu` VALUES (2, 326);
INSERT INTO `sys_role_menu` VALUES (2, 327);
INSERT INTO `sys_role_menu` VALUES (2, 328);
INSERT INTO `sys_role_menu` VALUES (2, 329);
INSERT INTO `sys_role_menu` VALUES (2, 330);
INSERT INTO `sys_role_menu` VALUES (2, 331);
INSERT INTO `sys_role_menu` VALUES (2, 332);
INSERT INTO `sys_role_menu` VALUES (2, 333);
INSERT INTO `sys_role_menu` VALUES (2, 334);
INSERT INTO `sys_role_menu` VALUES (2, 335);
INSERT INTO `sys_role_menu` VALUES (2, 336);
INSERT INTO `sys_role_menu` VALUES (2, 337);
INSERT INTO `sys_role_menu` VALUES (2, 338);
INSERT INTO `sys_role_menu` VALUES (2, 339);
INSERT INTO `sys_role_menu` VALUES (2, 340);
INSERT INTO `sys_role_menu` VALUES (2, 341);
INSERT INTO `sys_role_menu` VALUES (2, 342);
INSERT INTO `sys_role_menu` VALUES (2, 343);
INSERT INTO `sys_role_menu` VALUES (2, 344);
INSERT INTO `sys_role_menu` VALUES (2, 345);
INSERT INTO `sys_role_menu` VALUES (2, 346);
INSERT INTO `sys_role_menu` VALUES (2, 347);
INSERT INTO `sys_role_menu` VALUES (2, 348);
INSERT INTO `sys_role_menu` VALUES (2, 349);
INSERT INTO `sys_role_menu` VALUES (2, 350);
INSERT INTO `sys_role_menu` VALUES (2, 351);

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户名',
  `nickname` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '昵称',
  `gender` tinyint(1) NULL DEFAULT 1 COMMENT '性别((1-男 2-女 0-保密)',
  `password` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '密码',
  `dept_id` int NULL DEFAULT NULL COMMENT '部门ID',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户头像',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '联系方式',
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(1-正常 0-禁用)',
  `email` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '用户邮箱',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `update_by` bigint NULL DEFAULT NULL COMMENT '修改人ID',
  `is_deleted` tinyint(1) NULL DEFAULT 0 COMMENT '逻辑删除标识(0-未删除 1-已删除)',
  `openid` char(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信 openid',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `login_name`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES (1, 'root', '可我不敌可爱', 0, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', NULL, 'https://minio.aioveu.com/aioveu/20250810/352485413d814dee87d9b7160dfe916e.png', '13061656199', 1, 'ambitiouschild@qq.com', '2025-12-30 22:14:01', NULL, '2025-12-30 22:14:01', NULL, 0, NULL);
INSERT INTO `sys_user` VALUES (2, 'admin', '系统管理员', 1, '$2a$10$8/8PxGHA.30EeWg8x4/4BuWuCUJubFbGJXyUYRs7RaJEdVvEMRbWe', 1, 'https://cdn.aioveu.com/aioveu/1001/user-avatar/20260302/842f5908e7b6408da8ec641db0f68320.png', '13061656199', 1, 'ambitiouschild@qq.com', '2025-12-30 22:14:01', NULL, '2026-03-02 23:26:01', NULL, 0, NULL);
INSERT INTO `sys_user` VALUES (3, 'test', '测试小用户', 1, '$2a$10$xVWsNOhHrCxh5UbpCE7/HuJ.PAOKcYAqRxD2CO2nVnJS.IAXkr5aq', 3, 'https://minio.aioveu.com/aioveu/20250810/352485413d814dee87d9b7160dfe916e.png', '13061656199', 1, 'ambitiouschild@qq.com', '2025-12-30 22:14:02', NULL, '2025-12-30 22:14:02', NULL, 0, NULL);

-- ----------------------------
-- Table structure for sys_user_notice
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_notice`;
CREATE TABLE `sys_user_notice`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  `notice_id` bigint NOT NULL COMMENT '公共通知id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `is_read` bigint NULL DEFAULT 0 COMMENT '读取状态（0: 未读, 1: 已读）',
  `read_time` datetime NULL DEFAULT NULL COMMENT '阅读时间',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `is_deleted` tinyint NULL DEFAULT 0 COMMENT '逻辑删除(0: 未删除, 1: 已删除)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户通知公告表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_notice
-- ----------------------------
INSERT INTO `sys_user_notice` VALUES (1, 1, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (2, 2, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (3, 3, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (4, 4, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (5, 5, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (6, 6, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (7, 7, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (8, 8, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (9, 9, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);
INSERT INTO `sys_user_notice` VALUES (10, 10, 2, 1, NULL, '2025-12-30 22:14:09', '2025-12-30 22:14:09', 0);

-- ----------------------------
-- Table structure for sys_user_role
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role`  (
  `user_id` bigint NOT NULL COMMENT '用户ID',
  `role_id` bigint NOT NULL COMMENT '角色ID',
  PRIMARY KEY (`user_id`, `role_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '用户和角色关联表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES (1, 1);
INSERT INTO `sys_user_role` VALUES (2, 2);
INSERT INTO `sys_user_role` VALUES (3, 3);

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
