/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_oauth2_server

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 10/06/2026 2339:12
*/

SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;

use aioveu_oauth2_server;

DROP TABLE IF EXISTS `oauth2_registered_client_biz`;

CREATE TABLE `oauth2_registered_client_biz`  (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                      `client_uuid` varchar(64) NOT NULL COMMENT 'OAuth2 客户端UUID',
                                      `client_id` varchar(64) NOT NULL COMMENT 'OAuth2 客户端ID',
                                      `tenant_id` bigint DEFAULT NULL COMMENT '租户ID',
                                      `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用：1-启用 0-禁用',
                                      `remark` varchar(255) DEFAULT NULL COMMENT '备注',

    -- 系统字段
                                      `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_client_id` (`client_id`),
                                      KEY `idx_tenant_enabled` (`tenant_id`, `enabled`),
                                      KEY `idx_create_time` (`create_time`)
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = 'OAuth2 客户端业务状态表（auth 服务本地校验用）'
  ROW_FORMAT = Dynamic;