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

 Date: 05/04/2026 13:58:33
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pay_config_wechat
-- ----------------------------
DROP TABLE IF EXISTS `pay_config_wechat`;
CREATE TABLE `pay_config_wechat`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` bigint NOT NULL COMMENT '支付配置主表ID',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  `app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信应用ID(公众号/小程序/APP等)',
  `mch_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '微信商户号(10位数字)',
  `mch_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户API密钥V2',
  `api_v3_key` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT 'APIv3密钥(32位字符)',
  `mch_serial_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户证书序列号',
  `private_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商户私钥内容(PKCS#8格式)',
  `private_key_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商户私钥文件路径',
  `wechatpay_public_key_id` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信支付公钥ID',
  `wechatpay_public_key` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '微信支付公钥内容',
  `wechatpay_public_key_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '微信支付公钥文件路径',
  `platform_cert_serial_no` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台证书序列号',
  `platform_cert_path` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '平台证书文件路径',
  `api_domain` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'https://api.mch.weixin.qq.com' COMMENT '网关地址',
  `connect_timeout` int NOT NULL DEFAULT 10 COMMENT '连接超时时间(秒)',
  `read_timeout` int NOT NULL DEFAULT 10 COMMENT '读取超时时间(秒)',
  `notify_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '支付异步通知地址',
  `refund_notify_url` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '退款异步通知地址',
  `sandbox` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否沙箱环境: 0-否, 1-是',
  `sign_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'RSA' COMMENT '签名类型: RSA, HMAC-SHA256',
  `cert_store_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT 'STRING' COMMENT '证书存储方式: FILE-文件, STRING-字符串, CLOUD-云存储',
  `auto_download_cert` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否自动下载平台证书: 0-否, 1-是',
  `sub_app_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子商户应用ID',
  `sub_mch_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '子商户号',
  `profit_sharing` tinyint(1) NULL DEFAULT 0 COMMENT '是否支持分账: 0-否, 1-是',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint NULL DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint NULL DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `uk_config_id`(`config_id` ASC) USING BTREE COMMENT '支付配置ID唯一',
  INDEX `idx_tenant_id`(`tenant_id` ASC) USING BTREE COMMENT '租户ID索引',
  INDEX `idx_app_id`(`app_id` ASC) USING BTREE COMMENT '应用ID索引',
  INDEX `idx_mch_id`(`mch_id` ASC) USING BTREE COMMENT '商户号索引',
  INDEX `idx_sandbox`(`sandbox` ASC) USING BTREE COMMENT '沙箱环境索引'
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '微信支付配置表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pay_config_wechat
-- ----------------------------
INSERT INTO `pay_config_wechat` VALUES (1, 1, 1, 'wxe510ad6d4d60bfa8', '1107195457', '', 'dCzmYhzYcCEyRyiHp9Z9MTVMRYoSYwVj', '239431EB597A2C00B8356D7C3043E0FC8D04CEE8', 'MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQC31Edi9nwLNMwL       BibV+d3lypSXUqnA3S4jC3f75SyvyVmFu1EPS5H0lGfSg2rE8yriLdt25PKEseax       wOz7ARIkqo+pBVtyIaLfvrftUpRFUvxG/8AxQZ2rmeVm9RVRZtGzVdRuNXBwOnnV       /fOf0zRB168dE1K+jJa47wx39TB6f8AqDpm/bzfCnwMqUwFihxUoU5Od4QK1Y6cb       ZugB5VSpxOp2RDLXovj92Du9c/MwfipD/ZvObEfbPXcdpKQ5yBtzNmEjC4o5PrFK       LK2QMPw1JwCotj004TVxaoXSannAKG6ZtXahb1I4rx/DTZ3m12KqcDQQRn0wY5U9       5TqZSmEbAgMBAAECggEAbDleWaXRO2u24JqbjIfRGnLcbgYtiCzu95TN649tcoQw       3yl7PGz6HaqtQOM7nX2ndocW2HVLTdHKhkWVfuSPgOSlNRXBU3z3/Y+zitV+Ex4b       BdWVmaoOoDM3Ohj5lvXYaSMTsFpnurrCdbhPkH1wG39WSeDRavsr1O5vQcQo6USB       669iYR5LLhIRPELxQjSLPfDDKllu48rcS3j365inQPc2QTvbZ63LVKHAs1MQvPvA       dBohlxbvUMIB/5YlYnkgJwwVBONmBfbj6u+f/5Os2W5Z6A3L3+LuvhApNPlUTolo       1jvVpJkHAklowZ8q8AsOm36pfMnH5M+2pjGIRDoP+QKBgQDv8TwKQS/87MDDgmOt       BTzZKPZ2lGsfn+/3Ql7Z/ob6yfMrvOt6EhyG5tTa4TSpBdq9Ar2EphilaYszoKHv       PTBWL8xWA1Tfn0E/vc6j+Hxri6Fd76vJPP8U61KmH+s7/x/HpWEzVV2PYLkkk3k+       gA2P0RmkwvrcZ6bgOSjXNyvgBwKBgQDEIbKE+tr3NWeQx9CqYVNNL4pvzUG6NCrM       4x1v19okYpRowDF1PIzJ6JZURDoB/zBYe0UbzPO1gIRS/ZBK3JT+MpD4iF2IIv2z       /ilSjNv0XBVDIrnPpZcRNcvrr5ZbGgt56VSWvJNObfuuL9mY7QxvZ+dRE1K/5FW8       GUUo9ppJTQKBgQC2V1EVSey5/QGQkipFdlGDhkmmW6v84IJwGRQT/gV92LLfbjZV       EiZzrj/cBoEKYdSVBtwXTlI0gjyzA0OMl6eAF9rlEqsRNelUE15R5ahH4ljDFjwq       NhxzPVIVxPPzKDJyeg+f/yOWrh6E2T2ubYng3TrrpvEVRf/fLhEHXHoPSQKBgEXr       CVv0nYiRJjlrsgx7I5oZf415q5u4qyVdNYpAdciQx/1LvpXfnwT/gE/L/q3ljhOM       5kcR2jNy/DkloYG+fvLbjei7nDRC3RSEX7DI/ERxazcCsyC+FEuTVP3RG5WghETS       lFAkK5NDC/y9nCGzJ8KzVLgHXnF/8bxbedcvijLlAoGBANtrk99k2X1ZVxh3WLUM       i7+6d5dGTed6mYzD7qjC1yCgu6ATrLF/L1qgfVkdpEcwqOB5dZzZX3eAjvXytCtV       xR9s+OqECoujURHY1QlmD0wGuNKDkusmlAqqV1xDCZzsoCdtKgjk+Rytee9BVW6t       Kdi+4BE0vnA+MDgHdUflDtbF', '', 'PUB_KEY_ID_0111071954572026030800292321000401', '-----BEGIN PUBLIC KEY-----         MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwzm4THV3l2aUkyl/dQln         vzs07PMP6iopZmcWXDvY2hu/N5iAlRwF5iYsKQQw0gRDeRMWlui+HkS7B7udE5T3         quK6i2XablINSRMSNVLtSbBebfvdj2JzmYmV4Z5D2t9DQrsYs22nwQTrojA61lYr         rdpukp0YH3p0IDxzRDzGIkmWT4/DhZ7eL87rB7o9Kvt0tzmg/ybdEsp80tv0h57R         5PGpbPyS28vGyNnqTK/3IgDMkf2AyiQxNqahjAj97pS8WI2uc1lTb076eEmJA4l+         VKmSHVGDOfQE8jReTE/jvjpTP5SxoFyvG/sp+tcLbk8BGjpVZnc6ynzO3AyHbFDp         LQIDAQAB         -----END PUBLIC KEY-----', '', '', '', '', 10, 10, 'https://192.168.1.5:6699/api/v1/pay-order/callback/wechat', 'https://aioveu.com/api/payment/wechat/refund/notify', 0, '', '', 1, '', '', 0, 0, '2026-03-28 18:51:56', '2026-03-29 18:00:14', NULL, NULL);

SET FOREIGN_KEY_CHECKS = 1;
