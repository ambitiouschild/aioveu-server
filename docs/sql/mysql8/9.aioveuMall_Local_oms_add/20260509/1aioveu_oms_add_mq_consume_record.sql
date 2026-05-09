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

SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;


-- ----------------------------
-- Table structure for mq_consume_record
-- 消息消费记录表 (mq_consume_record) - 1.消息消费记录表
-- ----------------------------
DROP TABLE IF EXISTS `mq_consume_record`;
CREATE TABLE `mq_consume_record`  (
      `id` bigint(20) NOT NULL AUTO_INCREMENT,
      `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID，0表示平台默认',
      `message_id` varchar(64) NOT NULL COMMENT '消息ID',
      `topic` varchar(100) NOT NULL COMMENT 'Topic',
      `tag` varchar(50) DEFAULT NULL COMMENT 'Tag',
      `consumer_group` varchar(100) NOT NULL COMMENT '消费者组',
      `biz_id` varchar(64) NOT NULL COMMENT '业务ID(订单号)',
      `consume_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '消费状态:0-未消费,1-消费中,2-消费成功,3-消费失败,4-进入死信',
      `retry_count` int(11) DEFAULT '0' COMMENT '重试次数',
      `max_retry` int(11) DEFAULT '3' COMMENT '最大重试次数',
      `next_retry_time` datetime DEFAULT NULL COMMENT '下次重试时间',
      `consume_time` datetime DEFAULT NULL COMMENT '消费时间',
      `finish_time` datetime DEFAULT NULL COMMENT '完成时间',
      `error_msg` text COMMENT '错误信息',

    -- 系统字段
      `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

      PRIMARY KEY (`id`),
      UNIQUE KEY `uk_message_consumer` (`message_id`,`consumer_group`),
      KEY `tenant_id` (`tenant_id`),
      KEY `idx_biz_id` (`biz_id`),
      KEY `idx_consumer_status` (`consumer_group`,`consume_status`),
      KEY `idx_retry_time` (`next_retry_time`),
      KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'MQ消息消费记录表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mq_dead_letter
-- 死信队列表 (mq_dead_letter) - 2.死信队列表
-- ----------------------------
DROP TABLE IF EXISTS `mq_dead_letter`;
CREATE TABLE `mq_dead_letter`  (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID，0表示平台默认',
   `message_id` varchar(64) NOT NULL COMMENT '消息ID',
   `topic` varchar(100) NOT NULL COMMENT 'Topic',
   `tag` varchar(50) DEFAULT NULL COMMENT 'Tag',
   `consumer_group` varchar(100) NOT NULL COMMENT '消费者组',
   `biz_id` varchar(64) NOT NULL COMMENT '业务ID',
   `message_body` json NOT NULL COMMENT '消息体',
   `consume_times` int(11) DEFAULT '0' COMMENT '消费次数',
   `error_msg` text COMMENT '错误信息',
   `dead_reason` varchar(500) COMMENT '死信原因',
   `handle_status` tinyint(4) DEFAULT '0' COMMENT '处理状态:0-未处理,1-已处理',
   `handle_time` datetime DEFAULT NULL COMMENT '处理时间',
   `handle_result` varchar(1000) DEFAULT NULL COMMENT '处理结果',

    -- 系统字段
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

   PRIMARY KEY (`id`),
   UNIQUE KEY `uk_message_consumer` (`message_id`,`consumer_group`),
   KEY `tenant_id` (`tenant_id`),
   KEY `idx_biz_id` (`biz_id`),
   KEY `idx_handle_status` (`handle_status`),
   KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'MQ死信队列表'
    ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for mq_consume_idempotent
-- 消费幂等性表 (mq_consume_idempotent) - 3.消费幂等性表
-- ----------------------------
DROP TABLE IF EXISTS `mq_consume_idempotent`;
CREATE TABLE `mq_consume_idempotent`  (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT,
                                  `tenant_id` bigint NOT NULL DEFAULT 0 COMMENT '租户ID，0表示平台默认',
                                  `biz_unique_key` varchar(128) NOT NULL COMMENT '业务唯一键',
                                  `biz_type` varchar(50) NOT NULL COMMENT '业务类型',
                                  `message_id` varchar(64) NOT NULL COMMENT '消息ID',
                                  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '状态:1-已处理',
                                  `consume_time` datetime NOT NULL COMMENT '消费时间',

    -- 系统字段
                                   `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                  PRIMARY KEY (`id`),
                                  UNIQUE KEY `uk_biz_key` (`biz_unique_key`,`biz_type`),
                                  KEY `tenant_id` (`tenant_id`),
                                  KEY `idx_message_id` (`message_id`),
                                  KEY `idx_consume_time` (`consume_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'MQ消费幂等性表'
    ROW_FORMAT = Dynamic;