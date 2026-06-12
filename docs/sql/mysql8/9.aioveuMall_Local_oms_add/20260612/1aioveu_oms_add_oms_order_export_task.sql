/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_oms

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 16/03/2026 12:16:12
*/

use aioveu_oms;
# SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for mq_consume_record
-- 消息消费记录表 (mq_consume_record) - 1.消息消费记录表
-- ----------------------------
DROP TABLE IF EXISTS `oms_order_export_task`;
CREATE TABLE `oms_order_export_task`  (
    -- ================= 主键 =================
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '订单导出任务ID',
                                      `order_export_task_no` varchar(64) NOT NULL COMMENT '订单导出任务编号',
    -- ================= 多租户 & 审计 =================

                                      `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID，0表示平台默认',
                                      `operator_id` bigint NOT NULL DEFAULT 0 COMMENT '操作员ID（创建该导出任务的管理员）',
                                      `client_id` varchar(64) NOT NULL COMMENT '客户端ID（小程序/H5/PC）',

    -- ================= 导出条件 =================
                                      `order_status` tinyint NOT NULL DEFAULT 0 COMMENT '订单状态：0-待付款 1-已付款 2-已发货 3-已完成 4-已取消 5-售后中',
                                      `order_start_time` datetime NOT NULL COMMENT '订单起始时间',
                                      `order_end_time` datetime NOT NULL COMMENT '订单结束时间',

                                      `export_format` varchar(16) NOT NULL DEFAULT 'excel' COMMENT '导出格式：excel/csv',
                                      `total_count` int DEFAULT 0 COMMENT '导出数据总量',

    -- ================= 任务状态 =================
                                      `status` varchar(32) NOT NULL DEFAULT 'PENDING' COMMENT '任务状态：PENDING/RUNNING/SUCCESS/FAILED',
                                      `file_url` varchar(255) DEFAULT NULL COMMENT '导出文件地址',
                                      `fail_reason` varchar(512) DEFAULT NULL COMMENT '失败原因',

    -- ================= 执行时间线 =================
                                      `start_time_actual` datetime DEFAULT NULL COMMENT '任务开始执行时间',
                                      `finish_time` datetime DEFAULT NULL COMMENT '任务完成时间',

    -- 系统字段
                                      `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                      PRIMARY KEY (`id`),
                                      KEY `tenant_id` (`tenant_id`),
                                      INDEX idx_tenant_status (tenant_id, status),
                                      KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '订单导出任务表'
  ROW_FORMAT = Dynamic;