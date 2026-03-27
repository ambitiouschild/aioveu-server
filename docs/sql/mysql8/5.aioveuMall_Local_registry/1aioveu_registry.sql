/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_tenant

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 16/03/2026 12:16:12
*/

SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for registry_tenant
-- 租户注册小程序基本信息表 (registry_tenant) - 存储租户基本信息
-- ----------------------------
DROP TABLE IF EXISTS `registry_tenant`;
CREATE TABLE `registry_tenant`  (
  `id` bigint NOT NULL AUTO_INCREMENT ,
  `tenant_id` bigint NOT NULL  COMMENT '租户ID',
  `tenant_code` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '租户唯一编码',
  `tenant_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '主体类型：1-企业，2-个体工商户，3-政府/媒体，4-其他组织，5-个人',
  `business_type` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '行业类别/小程序类目',
  `country_region` varchar(100)  DEFAULT '中国大陆' NOT NULL COMMENT '注册国家/地区',
  `tenant_registry_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '租户注册状态：0-未注册，1-已注册，2-已认证，3-已备案，4-已禁用',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) COMMENT '租户ID唯一' ,-- 确保唯一性
  UNIQUE KEY `uk_tenant_code` (`tenant_code`) COMMENT '租户唯一编码',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户ID索引',
  KEY `idx_tenant_code` (`tenant_code`) COMMENT '租户唯一编码索引',
  KEY `idx_tenant_registry_status` (`tenant_registry_status`) COMMENT '租户注册状态索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '租户注册小程序基本信息表'
    ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_tenant
-- ----------------------------

-- ----------------------------
-- Table structure for registry_app_account 小程序账号表 - 存储小程序账号信息
-- ----------------------------
DROP TABLE IF EXISTS `registry_app_account`;
CREATE TABLE `registry_app_account`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `app_id` bigint NOT NULL COMMENT '小程序AppID',
  `tenant_id` bigint NOT NULL COMMENT '所属租户ID',
  `app_secret` varchar(100) DEFAULT NULL COMMENT '小程序AppSecret',
  `original_id` varchar(50)  DEFAULT NULL COMMENT '原始ID',
  `account_name` varchar(200) DEFAULT NULL COMMENT '小程序名称',
  `account_type` tinyint(1) NOT NULL DEFAULT 0 COMMENT '账号类型：0-未注册，1-普通小程序，2-游戏小程序',
  `email` varchar(100) NOT NULL COMMENT '注册邮箱',
  `password` varchar(100)  COMMENT '登录密码（加密存储）',
  `email_verify_code` varchar(10) DEFAULT NULL COMMENT '邮箱验证码',
  `email_verified` tinyint(1) NOT NULL DEFAULT 0 COMMENT '邮箱是否已验证：0-未验证，1-已验证',
  `register_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '注册状态：0-未开始，1-邮箱注册中，2-信息登记中，3-主体认证中，4-注册完成',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uk_email` (`email`)  COMMENT '邮箱唯一',
  UNIQUE KEY `uk_app_id` (`app_id`) COMMENT '小程序AppID唯一',
  KEY `idx_app_id` (`app_id`) COMMENT '小程序AppID索引',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户索引',
  KEY `idx_account_name` (`account_name`) COMMENT '小程序名称索引',
  KEY `idx_email` (`email`) COMMENT '注册邮箱索引',
  KEY `idx_register_status` (`register_status`) COMMENT '注册状态索引'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '小程序账号表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_app_account
-- ----------------------------

-- ----------------------------
-- Table structure for registry_enterprise_qualification  企业资质表  存储企业/个体工商户资质信息
-- ----------------------------
DROP TABLE IF EXISTS `registry_enterprise_qualification`;
CREATE TABLE `registry_enterprise_qualification`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '资质ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '所属租户ID',
  `enterprise_name` varchar(200) NOT NULL COMMENT '企业名称（营业执照全称）',
  `business_license_no` varchar(50) NOT NULL COMMENT '营业执照注册号/统一社会信用代码',
  `business_license_url` varchar(500) DEFAULT NULL COMMENT '营业执照照片路径',
  `other_certificates` json DEFAULT NULL COMMENT '其他证明材料（JSON数组）',
  `verification_type` tinyint(1) DEFAULT NULL COMMENT '主体验证方式：1-小额打款，2-微信认证，3-电子营业执照',
  `verification_amount` decimal(10,2) DEFAULT NULL COMMENT '小额打款金额',
  `verification_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '验证状态：0-未验证，1-验证中，2-验证成功，3-验证失败',
  `verification_time` datetime DEFAULT NULL COMMENT '验证时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_tenant_id` (`tenant_id`) COMMENT '租户唯一',
  UNIQUE KEY `uk_license_no` (`business_license_no`) COMMENT '统一社会信用代码唯一',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户索引',
  KEY `idx_enterprise_name` (`enterprise_name`) COMMENT '营业执照索引',
  KEY `idx_business_license_no` (`business_license_no`) COMMENT '统一社会信用代码索引',
  KEY `idx_verification_status` (`verification_status`) COMMENT '验证状态索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '企业资质表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_enterprise_qualification
-- ----------------------------

-- ----------------------------
-- Table structure for registry_administrator_info 管理员信息表 存储小程序管理员信息
-- ----------------------------
DROP TABLE IF EXISTS `registry_administrator_info`;
CREATE TABLE `registry_administrator_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '管理员ID',
  `tenant_id` bigint NULL DEFAULT 0 COMMENT '所属租户ID',
  `real_name` varchar(50) NOT NULL COMMENT '管理员真实姓名',
  `id_card_no` varchar(18) NOT NULL COMMENT '身份证号码',
  `id_card_front_path` varchar(500) DEFAULT NULL COMMENT '身份证正面照片',
  `id_card_back_path` varchar(500) DEFAULT NULL COMMENT '身份证反面照片',
  `phone` varchar(20) NOT NULL COMMENT '手机号码',
  `phone_verify_code` varchar(10) DEFAULT NULL COMMENT '短信验证码',
  `phone_verified` tinyint(1) NOT NULL DEFAULT 0 COMMENT '手机是否已验证：0-未验证，1-已验证',
  `wechat_openid` varchar(100) DEFAULT NULL COMMENT '管理员微信OpenID',
  `wechat_unionid` varchar(100) DEFAULT NULL COMMENT '管理员微信UnionID',
  `wechat_qr_scanned` tinyint(1) NOT NULL DEFAULT 0 COMMENT '微信扫码是否已验证',
  `wechat_scanned_time` datetime DEFAULT NULL COMMENT '微信扫码验证时间',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE ,
  UNIQUE KEY `uk_id_card` (`id_card_no`) COMMENT '身份证号码唯一',
  UNIQUE KEY `uk_phone` (`phone`) COMMENT '手机号码唯一',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '所属租户ID索引',
  KEY `idx_real_name` (`real_name`) COMMENT '管理员真实姓名索引',
  KEY `idx_id_card_no` (`id_card_no`) COMMENT '身份证号码索引',
  KEY `idx_phone` (`phone`) COMMENT 'phone索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '管理员信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_administrator_info
-- ----------------------------

-- ----------------------------
-- Table structure for registry_certification 认证全流程记录
-- ----------------------------
DROP TABLE IF EXISTS `registry_certification`;
CREATE TABLE `registry_certification`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '认证ID ',
  `tenant_id` bigint(20) NOT NULL COMMENT '所属租户ID',
  `app_account_id` bigint(20) DEFAULT NULL COMMENT '小程序账号ID',
  `certification_type` tinyint(1) NOT NULL DEFAULT 1 COMMENT '认证类型：1-微信认证',
  `certification_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '认证状态：0-未开始，1-申请中，2-审核中，3-审核通过，4-审核失败，5-已过期',
  `apply_time` datetime DEFAULT NULL COMMENT '申请时间',
  `audit_time` datetime DEFAULT NULL COMMENT '审核时间',
  `expire_time` datetime DEFAULT NULL COMMENT '过期时间',
  `certification_fee` decimal(10,2) NOT NULL DEFAULT 300.00 COMMENT '认证费用',
  `payment_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '支付状态：0-未支付，1-支付中，2-支付成功，3-支付失败',
  `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
  `payment_order_no` varchar(100) DEFAULT NULL COMMENT '支付订单号',
  `rejection_reason` text COMMENT '审核驳回原因',
  `audit_remark` text COMMENT '审核备注',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) COMMENT '认证ID',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '所属租户ID索引',
  KEY `idx_cert_status` (`certification_status`) COMMENT '认证状态索引',
  KEY `idx_app_account_id` (`app_account_id`) COMMENT '小程序账号ID索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '认证记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_certification
-- ----------------------------


-- ----------------------------
-- Table structure for registry_certification_contact 认证联系人表
-- ----------------------------
DROP TABLE IF EXISTS `registry_certification_contact`;
CREATE TABLE `registry_certification_contact`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '联系人ID',
  `tenant_id` bigint(20) NOT NULL COMMENT '所属租户ID',
  `certification_id` bigint(20) NOT NULL COMMENT '认证记录ID',
  `contact_name` varchar(50) NOT NULL COMMENT '联系人姓名',
  `contact_id_card` varchar(18) NOT NULL COMMENT '联系人身份证号',
  `contact_phone` varchar(20) NOT NULL COMMENT '联系人手机号',
  `contact_phone_verify_code` varchar(10) DEFAULT NULL COMMENT '联系人短信验证码',
  `contact_landline` varchar(50) DEFAULT NULL COMMENT '联系人座机（含分机）',
  `contact_wechat_openid` varchar(100) DEFAULT NULL COMMENT '联系人微信OpenID',
  `contact_wechat_scanned` tinyint(1) NOT NULL DEFAULT 0 COMMENT '联系人微信扫码是否验证',
  `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) COMMENT '联系人ID',
  KEY `idx_tenant_id` (`tenant_id`) COMMENT '所属租户ID索引',
  KEY `idx_cert_id` (`certification_id`) COMMENT '认证记录ID索引',
  KEY `idx_contact_name` (`contact_name`) COMMENT '联系人姓名索引',
  KEY `idx_contact_id_card` (`contact_id_card`) COMMENT '联系人身份证号索引',
  KEY `idx_contact_phone` (`contact_phone`) COMMENT '联系人手机号索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '认证联系人表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_certification_contact
-- ----------------------------

-- ----------------------------
-- Table structure for registry_invoice_info  发票信息表  存储发票信息
-- ----------------------------
DROP TABLE IF EXISTS `registry_invoice_info`;
CREATE TABLE `registry_invoice_info`  (
      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '发票ID',
      `tenant_id` bigint(20) NOT NULL COMMENT '所属租户ID',
      `certification_id` bigint(20) NOT NULL COMMENT '认证记录ID',
      `invoice_type` tinyint(1) NOT NULL COMMENT '发票类型：1-电子发票，2-纸质发票',
      `invoice_title` varchar(200) NOT NULL COMMENT '发票抬头',
      `tax_identification_no` varchar(50) DEFAULT NULL COMMENT '纳税人识别号',
      `invoice_remark` varchar(500) DEFAULT NULL COMMENT '发票备注',
      `invoice_email` varchar(100) DEFAULT NULL COMMENT '电子发票接收邮箱',
      `receiver_name` varchar(50) DEFAULT NULL COMMENT '收件人姓名',
      `receiver_phone` varchar(20) DEFAULT NULL COMMENT '收件人电话',
      `receiver_address` varchar(500) DEFAULT NULL COMMENT '收件地址',
      `invoice_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '开票状态：0-未开票，1-开票中，2-已开票，3-已寄送',
      `invoice_time` datetime DEFAULT NULL COMMENT '开票时间',
      `invoice_url` varchar(500) DEFAULT NULL COMMENT '电子发票URL',
      `express_no` varchar(100) DEFAULT NULL COMMENT '快递单号',
      `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`) COMMENT '发票ID',
      KEY `idx_tenant_id` (`tenant_id`) COMMENT '所属租户ID索引',
      KEY `idx_cert_id` (`certification_id`) COMMENT '认证记录ID索引',
      KEY `idx_tax_identification_no` (`tax_identification_no`) COMMENT '纳税人识别号索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '发票信息表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_invoice_info
-- ----------------------------

-- ----------------------------
-- Table structure for registry_app_filing_record 小程序备案表  存储备案信息
-- ----------------------------
DROP TABLE IF EXISTS `registry_app_filing_record`;
CREATE TABLE `registry_app_filing_record`  (
       `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '备案ID',
       `tenant_id` bigint(20) NOT NULL COMMENT '所属租户ID',
       `app_account_id` bigint(20) NOT NULL COMMENT '小程序账号ID',
       `filing_type` tinyint(1) NOT NULL COMMENT '备案类型：1-首次备案，2-变更备案，3-注销备案',
       `filing_status` tinyint(1) NOT NULL DEFAULT 0 COMMENT '备案状态：0-未备案，1-备案中，2-备案通过，3-备案驳回，4-已注销',
       `filing_no` varchar(100) DEFAULT NULL COMMENT '备案编号',
       `filing_subject` varchar(200) DEFAULT NULL COMMENT '备案主体',
       `filing_app_name` varchar(100) DEFAULT NULL COMMENT '备案小程序名称',
       `filing_domain` varchar(500) DEFAULT NULL COMMENT '备案域名',
       `filing_ip` varchar(200) DEFAULT NULL COMMENT '备案IP',
       `filing_certificate_path` varchar(500) DEFAULT NULL COMMENT '备案证书路径',
       `apply_time` datetime DEFAULT NULL COMMENT '备案申请时间',
       `audit_time` datetime DEFAULT NULL COMMENT '备案审核时间',
       `rejection_reason` text COMMENT '备案驳回原因',
       `expire_time` datetime DEFAULT NULL COMMENT '备案到期时间',
       `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
       `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       PRIMARY KEY (`id`) COMMENT '备案ID',
       UNIQUE KEY `uk_filing_no` (`filing_no`) COMMENT '备案编号唯一',
       KEY `idx_tenant_id` (`tenant_id`) COMMENT '所属租户ID索引',
       KEY `idx_app_account` (`app_account_id`) COMMENT '小程序账号ID索引',
       KEY `idx_filing_status` (`filing_status`) COMMENT '备案状态索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '小程序备案记录表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_app_filing_record
-- ----------------------------


-- ----------------------------
-- Table structure for registry_operation_log  操作日志表 存储所有操作日志
-- ----------------------------
DROP TABLE IF EXISTS `registry_operation_log`;
CREATE TABLE `registry_operation_log`  (
           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '日志ID',
           `tenant_id` bigint(20) DEFAULT NULL COMMENT '租户ID',
           `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
           `operator_type` tinyint(1) NOT NULL COMMENT '操作人类型：1-系统，2-租户，3-管理员',
           `operation_type` varchar(50) NOT NULL COMMENT '操作类型：REGISTER, CERTIFY, FILE, UPDATE, DELETE等',
           `operation_target` varchar(50) NOT NULL COMMENT '操作目标：TENANT, APP, CERTIFICATION, FILING等',
           `target_id` bigint(20) DEFAULT NULL COMMENT '目标记录ID',
           `operation_content` json NOT NULL COMMENT '操作内容（JSON格式）',
           `operation_result` tinyint(1) NOT NULL COMMENT '操作结果：1-成功，0-失败',
           `error_message` text COMMENT '错误信息',
           `ip_address` varchar(50) DEFAULT NULL COMMENT '操作IP',
           `user_agent` varchar(500) DEFAULT NULL COMMENT '用户代理',
           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
           PRIMARY KEY (`id`) COMMENT '日志ID',
           KEY `idx_tenant_id` (`tenant_id`) COMMENT '租户ID索引',
           KEY `idx_operator` (`operator_type`, `operator_id`) COMMENT '操作人类型 操作人ID索引',
           KEY `idx_target` (`operation_target`, `target_id`) COMMENT '操作目标 目标记录ID索引 ',
           KEY `idx_time` (`create_time`) COMMENT '操作时间索引'
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    COMMENT = '操作日志表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of registry_operation_log
-- ----------------------------

