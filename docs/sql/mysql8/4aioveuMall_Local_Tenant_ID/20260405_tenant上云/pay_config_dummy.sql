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

 Date: 05/04/2026 13:58:27
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_config_dummy
-- ----------------------------
DROP TABLE IF EXISTS `pay_config_dummy`;
CREATE TABLE `pay_config_dummy`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` bigint NOT NULL COMMENT '支付配置主表ID',
  `tenant_id` bigint NOT NULL COMMENT '所属租户ID',
  `success_rate` int NOT NULL DEFAULT 100 COMMENT '支付成功率(0-100)',
  `mock_delay` int NOT NULL DEFAULT 0 COMMENT '模拟延迟(毫秒)',
  `callback_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模拟回调地址',
  `callback_delay` int NOT NULL DEFAULT 0 COMMENT '回调延迟(毫秒)',
  `success_response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '成功响应模板',
  `fail_response` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '失败响应模板',
  `simulate_error` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否模拟异常: 0-否, 1-是',
  `error_code` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模拟错误码',
  `error_msg` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '模拟错误信息',
  `auto_refund` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否自动退款: 0-否, 1-是',
  `refund_delay` int NOT NULL DEFAULT 0 COMMENT '退款延迟(毫秒)',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_id`(`config_id` ASC) USING BTREE COMMENT '支付配置ID唯一索引',
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE COMMENT '租户ID索引',
  INDEX `idx_simulate_error`(`simulate_error` ASC) USING BTREE COMMENT '模拟错误索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '模拟支付配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_config_dummy
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
