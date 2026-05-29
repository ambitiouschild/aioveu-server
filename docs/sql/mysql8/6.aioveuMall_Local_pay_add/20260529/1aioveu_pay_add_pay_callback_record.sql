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

 Date: 29/05/2026 17:45:12
 Target Schema: aioveu_pay
 Table: pay_callback_record
*/

SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for pay_callback_record
-- 支付回调记录表 (pay_callback_record) - 1.支付回调记录表
-- ----------------------------
DROP TABLE IF EXISTS `pay_callback_record`;
CREATE TABLE `pay_callback_record`  (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    -- ==================== 租户 & 业务 ====================
                                   `tenant_id` bigint(20) NOT NULL DEFAULT 0 COMMENT '租户ID，0表示平台',
                                   `payment_no` varchar(64) NOT NULL COMMENT '内部支付单号',
                                   `order_no` varchar(64) NOT NULL COMMENT '业务订单号',
                                   `transaction_id` varchar(64) NOT NULL COMMENT '支付渠道交易号（微信transaction_id / 支付宝trade_no）',
                                   `channel` varchar(32) NOT NULL COMMENT '支付渠道：WECHAT / ALIPAY / UNION',
    -- ==================== 回调状态 ====================
                                   `notify_status` tinyint(2) NOT NULL DEFAULT 0 COMMENT '回调状态：0-接收 1-处理成功 2-处理失败',
                                   `notify_count` int(11) NOT NULL DEFAULT 1 COMMENT '回调次数（防风暴）',
                                   `last_notify_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '最后一次回调时间',

    -- ==================== 金额 & 校验 ====================
                                   `paid_amount` decimal(10,2) DEFAULT NULL COMMENT '实际支付金额',
                                   `mch_id` varchar(32) DEFAULT NULL COMMENT '商户号',
                                   `app_id` varchar(64) DEFAULT NULL COMMENT '应用ID',
    -- ==================== 原始数据 ====================
                                   `raw_data` text COMMENT '原始回调报文（XML / JSON）',
                                   `error_msg` varchar(255) DEFAULT NULL COMMENT '处理失败原因',
    -- 系统字段
                                   `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE KEY uk_transaction_channel (transaction_id, channel),
                                   KEY `idx_tenant_id` (`tenant_id`),
                                   KEY `idx_payment_no` (`payment_no`),
                                   KEY `idx_order_no` (`order_no`),
                                   KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付回调记录表'
  ROW_FORMAT = Dynamic;


