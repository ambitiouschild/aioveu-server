
use aioveu_ums;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for ums_address  会员收货地址表
-- ----------------------------
DROP TABLE IF EXISTS `ums_member_address`;
CREATE TABLE `ums_member_address`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '地址ID',
  `member_id` bigint NULL DEFAULT NULL COMMENT '会员ID',
  `consignee_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人姓名',
  `consignee_mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '收货人联系方式',
  `country` varchar(50) DEFAULT NULL COMMENT '国家',
  `province` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '城市',
  `district` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '区/县',
  `street` varchar(200) DEFAULT NULL COMMENT '街道',
  `detail_address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '详细地址',
  `postal_code` char(6) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '邮政编码',
  `defaulted` tinyint(1) DEFAULT '0' COMMENT '是否默认地址(0=否,1=是)',

  `address_tag` varchar(20) DEFAULT NULL COMMENT '地址标签(家,公司,学校等)',
  `longitude` decimal(10,7) DEFAULT NULL COMMENT '经度', -- 增加longitude、latitude字段：经纬度，用于地图定位
  `latitude` decimal(10,7) DEFAULT NULL COMMENT '纬度',
  `status` tinyint(1) DEFAULT '1' COMMENT '状态(0=已删除,1=正常)',  -- 增加status字段：状态（正常/已删除），替代物理删除


  `create_time` datetime NULL DEFAULT CURRENT_TIMESTAMP  COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT CURRENT_TIMESTAMP  ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE,

  INDEX `idx_member_id` (`member_id`) COMMENT '会员ID索引',
  INDEX `idx_is_default` (`defaulted`) COMMENT '默认地址索引',
  INDEX `idx_province_city` (`province`, `city`) COMMENT '省市区联合索引',
  UNIQUE KEY `uk_member_default` (`member_id`, `defaulted`) COMMENT '会员默认地址唯一约束'

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci
    ROW_FORMAT = DYNAMIC
    COMMENT '会员收货地址表';

-- ----------------------------
-- Records of ums_address
-- ----------------------------

-- ----------------------------
-- Table structure for ums_member
-- ----------------------------
DROP TABLE IF EXISTS `ums_member`;
CREATE TABLE `ums_member`  (

    -- 基本信息在前（昵称、手机、头像）
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '会员ID',
  `nick_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '昵称',
  `mobile` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '手机号',
  `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '头像URL',
  `gender` tinyint(1) NULL DEFAULT NULL COMMENT '性别(0=未知,1=男,2=女)',
  `birthday` date NULL DEFAULT NULL COMMENT '生日',

    -- 业务信息在中（积分、余额、状态）
  `point` int NULL DEFAULT 0 COMMENT '会员积分',
  `balance` bigint NULL DEFAULT 1000000000 COMMENT '账户余额(单位:分)',   -- 单位为分，使用bigint防止溢出
  `status` tinyint(1) NULL DEFAULT 1 COMMENT '状态(0=禁用,1=正常)',
  `deleted` tinyint(1) NULL DEFAULT 0 COMMENT '删除标志(0=未删除,1=已删除)',

    -- 微信信息在后（openid、session_key、地区信息）
  `openid` char(28) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信OpenID', -- char(28)，微信OpenID固定长度
  `session_key` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '微信会话密钥', -- varchar(32)，微信session_key标准长度

    -- 微信用户信息
  `country` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '国家',
  `province` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '省份',
  `city` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '城市',
  `language` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '语言',

    -- 时间字段
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',

  PRIMARY KEY (`id`) USING BTREE,

  UNIQUE KEY `uk_mobile` (`mobile`) USING BTREE COMMENT '手机号唯一索引',
  UNIQUE KEY `uk_openid` (`openid`) USING BTREE COMMENT 'OpenID唯一索引',
  INDEX `idx_status_deleted` (`status`, `deleted`) COMMENT '状态删除联合索引',  -- 状态+删除联合索引，用于快速查询有效用户
  INDEX `idx_create_time` (`create_time`) COMMENT '创建时间索引'  -- 创建时间索引，用于按时间范围查询

) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci
    ROW_FORMAT = DYNAMIC
    COMMENT '会员表';

-- ----------------------------
-- Records of ums_member 会员表
-- ----------------------------

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
