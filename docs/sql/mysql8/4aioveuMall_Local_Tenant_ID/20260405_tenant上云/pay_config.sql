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

 Date: 05/04/2026 13:58:15
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_config
-- ----------------------------
DROP TABLE IF EXISTS `pay_config`;
CREATE TABLE `pay_config`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `config_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置编码（唯一标识）',
  `config_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '配置名称',
  `platform_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付',
  `pay_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付',
  `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用: 0-禁用, 1-启用',
  `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认配置: 0-否, 1-是',
  `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_tenant_config_code`(`tenant_id` ASC, `config_code` ASC) USING BTREE COMMENT '租户+配置编码唯一索引',
  INDEX `idx_tenant_platform`(`tenant_id` ASC, `platform_type` ASC) USING BTREE COMMENT '租户+平台类型索引',
  INDEX `idx_tenant_pay_type`(`tenant_id` ASC, `pay_type` ASC) USING BTREE COMMENT '租户+支付类型索引',
  INDEX `idx_enabled`(`enabled` ASC) USING BTREE COMMENT '启用状态索引',
  INDEX `idx_is_default`(`is_default` ASC) USING BTREE COMMENT '默认配置索引'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付配置主表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_config
-- ----------------------------
INSERT INTO `pay_config` VALUES (1, 1, 'aa', '', 'wechat', 'app', 1, 1, 1, '', 0, '2026-03-28 17:54:35', '2026-03-28 17:54:35', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
