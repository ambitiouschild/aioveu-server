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
-- Table structure for pay_config
-- 支付配置主表 (pay_config) - - 统一管理所有支付配置的基础信息
-- ----------------------------
DROP TABLE IF EXISTS `pay_config`;
CREATE TABLE `pay_config`  (
       `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
       `tenant_id` bigint NOT NULL COMMENT '租户ID',
       `config_code` varchar(100) NOT NULL COMMENT '配置编码（唯一标识）',
       `config_name` varchar(100) NOT NULL COMMENT '配置名称',
       `platform_type` varchar(20) NOT NULL COMMENT '支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付',
       `pay_type` varchar(20) NOT NULL COMMENT '支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付',
       `enabled` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否启用: 0-禁用, 1-启用',
       `is_default` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否默认配置: 0-否, 1-是',
       `sort` int NOT NULL DEFAULT 0 COMMENT '排序',
       `remark` varchar(500) DEFAULT NULL COMMENT '备注',

    -- 系统字段
      `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
      `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
       PRIMARY KEY (`id`) USING BTREE,
       UNIQUE KEY `uk_tenant_config_code` (`tenant_id`, `config_code`) COMMENT '租户+配置编码唯一索引',
       KEY `idx_tenant_platform` (`tenant_id`, `platform_type`) COMMENT '租户+平台类型索引',
       KEY `idx_tenant_pay_type` (`tenant_id`, `pay_type`) COMMENT '租户+支付类型索引',
       KEY `idx_enabled` (`enabled`) COMMENT '启用状态索引',
       KEY `idx_is_default` (`is_default`) COMMENT '默认配置索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '支付配置主表'
  ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for pay_config_wechat
-- 微信支付配置表 (pay_config_wechat) - 存储微信支付相关配置
-- ----------------------------
DROP TABLE IF EXISTS `pay_config_wechat`;
CREATE TABLE `pay_config_wechat`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` bigint(20) NOT NULL COMMENT '支付配置主表ID',
  `tenant_id` bigint NOT NULL  COMMENT '租户ID',

   --  基础认证信息
  `app_id` varchar(100) NOT NULL COMMENT '微信应用ID(公众号/小程序/APP等)',
  `mch_id` varchar(100) NOT NULL COMMENT '微信商户号(10位数字)',
  `mch_key` varchar(128) DEFAULT NULL COMMENT '商户API密钥V2',

   --  APIv3密钥信息
  `api_v3_key` varchar(128) NOT NULL COMMENT 'APIv3密钥(32位字符)',
  `mch_serial_no` varchar(200) NOT NULL COMMENT '商户证书序列号',

   --  商户私钥信息
  `private_key` text NOT NULL COMMENT '商户私钥内容(PKCS#8格式)',
  `private_key_path` varchar(500) DEFAULT NULL COMMENT '商户私钥文件路径',

    --  微信支付公钥信息
  `wechatpay_public_key_id` varchar(200) DEFAULT NULL COMMENT '微信支付公钥ID',
  `wechatpay_public_key` text DEFAULT NULL COMMENT '微信支付公钥内容',
  `wechatpay_public_key_path` varchar(500) DEFAULT NULL COMMENT '微信支付公钥文件路径',

   -- 平台证书信息
  `platform_cert_serial_no` varchar(200) DEFAULT NULL COMMENT '平台证书序列号',
  `platform_cert_path` varchar(500) DEFAULT NULL COMMENT '平台证书文件路径',

    -- 网络连接配置
  `api_domain` varchar(200) NOT NULL DEFAULT 'https://api.mch.weixin.qq.com' COMMENT '网关地址',
  `connect_timeout` int(11) NOT NULL DEFAULT 10 COMMENT '连接超时时间(秒)',
  `read_timeout` int(11) NOT NULL DEFAULT 10 COMMENT '读取超时时间(秒)',

    -- 回调地址配置
  `notify_url` varchar(500) DEFAULT NULL COMMENT '支付异步通知地址',
  `refund_notify_url` varchar(500) DEFAULT NULL COMMENT '退款异步通知地址',

    -- 高级配置
  `sandbox` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否沙箱环境: 0-否, 1-是',
  `sign_type` varchar(20) NOT NULL DEFAULT 'RSA' COMMENT '签名类型: RSA, HMAC-SHA256',
  `cert_store_type` varchar(20) NOT NULL DEFAULT 'STRING' COMMENT '证书存储方式: FILE-文件, STRING-字符串, CLOUD-云存储',
  `auto_download_cert` tinyint(1) NOT NULL DEFAULT 1 COMMENT '是否自动下载平台证书: 0-否, 1-是',

    -- 子商户配置
  `sub_app_id` varchar(100) DEFAULT NULL COMMENT '子商户应用ID',
  `sub_mch_id` varchar(50) DEFAULT NULL COMMENT '子商户号',
  `profit_sharing` tinyint(1) DEFAULT NULL DEFAULT 0 COMMENT '是否支持分账: 0-否, 1-是',

    -- 系统字段
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_config_id` (`config_id`) COMMENT '支付配置ID唯一',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户ID索引',
  KEY `idx_app_id` (`app_id`) COMMENT '应用ID索引',
  KEY `idx_mch_id` (`mch_id`) COMMENT '商户号索引',
  KEY `idx_sandbox` (`sandbox`) COMMENT '沙箱环境索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '微信支付配置表'
    ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_config_wechat
-- ----------------------------

-- ----------------------------
-- Table structure for pay_config_alipay 支付宝支付配置表 - 存储支付宝支付相关配置
-- ----------------------------
DROP TABLE IF EXISTS `pay_config_alipay`;
CREATE TABLE `pay_config_alipay`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` bigint(20) NOT NULL COMMENT '支付配置主表ID',
  `tenant_id` bigint NOT NULL COMMENT '所属租户ID',

    --  基础认证信息
  `app_id` varchar(100) NOT NULL COMMENT '阿里应用ID',
  `merchant_private_key` text NOT NULL COMMENT '应用私钥',
  `alipay_public_key` text DEFAULT NULL COMMENT '支付宝公钥',
  `alipay_root_cert` text DEFAULT NULL COMMENT '支付宝根证书',
  `app_cert_public_key` text DEFAULT NULL COMMENT '应用公钥证书',

    -- 回调地址
  `notify_url` varchar(500) DEFAULT NULL COMMENT '异步通知地址',
  `return_url` varchar(500) DEFAULT NULL COMMENT '同步通知地址',
  `encrypt_key` varchar(100) DEFAULT NULL COMMENT 'AES加密密钥',

    -- 签名配置
  `sign_type` varchar(20) NOT NULL DEFAULT 'RSA2' COMMENT '签名类型: RSA/RSA2',
  `charset` varchar(20) NOT NULL DEFAULT 'UTF-8' COMMENT '字符编码',
  `format` varchar(20) NOT NULL DEFAULT 'JSON' COMMENT '数据格式',
  `gateway_url` varchar(200) NOT NULL DEFAULT 'https://openapi.alipay.com/gateway.do' COMMENT '网关地址',
  `sandbox_gateway_url` varchar(200) NOT NULL DEFAULT 'https://openapi.alipaydev.com/gateway.do' COMMENT '沙箱网关地址',

    -- 环境配置
  `sandbox` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否沙箱环境: 0-否, 1-是',
  `encrypt_type` varchar(20) NOT NULL DEFAULT 'AES' COMMENT '加密方式: AES',

    -- 网络配置
  `connect_timeout` int(11) NOT NULL DEFAULT 10 COMMENT '连接超时时间(秒)',
  `read_timeout` int(11) NOT NULL DEFAULT 10 COMMENT '读取超时时间(秒)',
  `proxy_host` varchar(100) DEFAULT NULL COMMENT '代理主机',
  `proxy_port` int(11) DEFAULT NULL COMMENT '代理端口',

    -- 系统字段
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_config_id` (`config_id`) COMMENT '支付配置ID唯一索引',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户索引',
  KEY `idx_app_id` (`app_id`) COMMENT '应用ID索引',
  KEY `idx_sandbox` (`sandbox`) COMMENT '沙箱环境索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '支付宝支付配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_config_alipay
-- ----------------------------

-- ----------------------------
-- Table structure for pay_config_dummy  存储模拟支付相关配置
-- ----------------------------
DROP TABLE IF EXISTS `pay_config_dummy`;
CREATE TABLE `pay_config_dummy`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `config_id` bigint(20) NOT NULL COMMENT '支付配置主表ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '所属租户ID',

    -- 模拟行为配置
  `success_rate` int(11) NOT NULL DEFAULT 100 COMMENT '支付成功率(0-100)',
  `mock_delay` int(11) NOT NULL DEFAULT 0 COMMENT '模拟延迟(毫秒)',
  `callback_url` varchar(500) DEFAULT NULL COMMENT '模拟回调地址',
  `callback_delay` int(11) NOT NULL DEFAULT 0 COMMENT '回调延迟(毫秒)',
  `success_response` text DEFAULT NULL COMMENT '成功响应模板',
  `fail_response` text DEFAULT NULL COMMENT '失败响应模板',

    -- 错误模拟配置
  `simulate_error` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否模拟异常: 0-否, 1-是',
  `error_code` varchar(50) DEFAULT NULL COMMENT '模拟错误码',
  `error_msg` varchar(200) DEFAULT NULL COMMENT '模拟错误信息',

    -- 退款配置
  `auto_refund` tinyint(1) NOT NULL DEFAULT 0 COMMENT '是否自动退款: 0-否, 1-是',
  `refund_delay` int(11) NOT NULL DEFAULT 0 COMMENT '退款延迟(毫秒)',

    -- 系统字段
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `create_by` bigint DEFAULT NULL COMMENT '创建人ID',
  `update_by` bigint DEFAULT NULL COMMENT '更新人ID',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_id` (`config_id`) COMMENT '支付配置ID唯一索引',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户ID索引',
  KEY `idx_simulate_error` (`simulate_error`) COMMENT '模拟错误索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '模拟支付配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of pay_config_dummy
-- ----------------------------

