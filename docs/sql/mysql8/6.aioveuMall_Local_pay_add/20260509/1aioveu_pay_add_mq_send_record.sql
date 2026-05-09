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
-- Table structure for mq_send_record
-- 消息发送记录表 (mq_send_record) - 1.消息发送记录表
-- ----------------------------
DROP TABLE IF EXISTS `mq_send_record`;
CREATE TABLE `mq_send_record`  (
       `id` bigint(20) NOT NULL AUTO_INCREMENT,
       `message_id` varchar(64) NOT NULL COMMENT '消息ID',
       `biz_id` varchar(64) NOT NULL COMMENT '业务ID(支付单号)',
       `biz_type` varchar(50) NOT NULL COMMENT '业务类型:payment_success',
       `topic` varchar(100) NOT NULL COMMENT 'Topic',
       `tag` varchar(50) DEFAULT NULL COMMENT 'Tag',
       `sharding_key` varchar(100) DEFAULT NULL COMMENT '分片Key',
       `message_body` json NOT NULL COMMENT '消息体(JSON格式)',
       `send_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '发送状态:0-未发送,1-发送中,2-发送成功,3-发送失败',
       `retry_count` int(11) DEFAULT '0' COMMENT '重试次数',
       `max_retry` int(11) DEFAULT '5' COMMENT '最大重试次数',
       `next_retry_time` datetime DEFAULT NULL COMMENT '下次重试时间',
       `send_time` datetime DEFAULT NULL COMMENT '发送时间',
       `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
       `error_msg` varchar(1000) DEFAULT NULL COMMENT '错误信息',

    -- 系统字段
      `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

       PRIMARY KEY (`id`) USING BTREE,
       UNIQUE KEY `uk_message_id` (`message_id`),
       KEY `idx_biz_id` (`biz_id`),
       KEY `idx_topic_status` (`topic`,`send_status`),
       KEY `idx_retry_time` (`next_retry_time`),
       KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'MQ消息发送记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mq_compensation_task
-- 补偿任务表 (mq_compensation_task) - 2.补偿任务表
-- ----------------------------
DROP TABLE IF EXISTS `mq_compensation_task`;
CREATE TABLE `mq_compensation_task`  (
     `id` bigint(20) NOT NULL AUTO_INCREMENT,
     `task_type` varchar(50) NOT NULL COMMENT '任务类型:send_retry',
     `biz_id` varchar(64) NOT NULL COMMENT '业务ID',
     `biz_data` json COMMENT '业务数据',
     `status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '状态:0-待处理,1-处理中,2-成功,3-失败',
     `retry_count` int(11) DEFAULT '0' COMMENT '重试次数',
     `next_execute_time` datetime DEFAULT NULL COMMENT '下次执行时间',
     `execute_result` varchar(1000) DEFAULT NULL COMMENT '执行结果',
     `error_msg` text COMMENT '错误信息',

    -- 系统字段
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

  PRIMARY KEY (`id`) USING BTREE,
     KEY `idx_biz_id` (`biz_id`),
     KEY `idx_status_time` (`status`,`next_execute_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'MQ补偿任务表'
    ROW_FORMAT = Dynamic;

