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

 Date: 05/04/2026 13:58:22
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_config_alipay
-- ----------------------------
DROP TABLE IF EXISTS `pay_config_alipay`;
CREATE TABLE `pay_config_alipay`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` bigint NOT NULL COMMENT '支付配置主表ID',
  `tenant_id` bigint NOT NULL COMMENT '所属租户ID',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '阿里应用ID',
  `merchant_private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '应用私钥',
  `alipay_public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '支付宝公钥',
  `alipay_root_cert` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '支付宝根证书',
  `app_cert_public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '应用公钥证书',
  `notify_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '异步通知地址',
  `return_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '同步通知地址',
  `encrypt_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'AES加密密钥',
  `sign_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'RSA2' COMMENT '签名类型: RSA/RSA2',
  `charset` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'UTF-8' COMMENT '字符编码',
  `format` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'JSON' COMMENT '数据格式',
  `gateway_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'https://openapi.alipay.com/gateway.do' COMMENT '网关地址',
  `sandbox_gateway_url` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'https://openapi.alipaydev.com/gateway.do' COMMENT '沙箱网关地址',
  `sandbox` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否沙箱环境: 0-否, 1-是',
  `encrypt_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'AES' COMMENT '加密方式: AES',
  `connect_timeout` int NOT NULL DEFAULT 10 COMMENT '连接超时时间(秒)',
  `read_timeout` int NOT NULL DEFAULT 10 COMMENT '读取超时时间(秒)',
  `proxy_host` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '代理主机',
  `proxy_port` int NULL DEFAULT NULL COMMENT '代理端口',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_id`(`config_id` ASC) USING BTREE COMMENT '支付配置ID唯一索引',
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE COMMENT '租户索引',
  INDEX `idx_app_id`(`app_id` ASC) USING BTREE COMMENT '应用ID索引',
  INDEX `idx_sandbox`(`sandbox` ASC) USING BTREE COMMENT '沙箱环境索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付宝支付配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_config_alipay
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
