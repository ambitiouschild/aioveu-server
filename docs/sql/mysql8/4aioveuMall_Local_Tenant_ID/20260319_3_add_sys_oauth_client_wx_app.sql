-- ============================================

/*
===========================
 sys_oauth_client_wx_app

✅ 这是你问题的核心答案

✅ client_id ↔ wx_appid 一对一

✅ 但 tenant 不直接碰 wx_appid

✅ wx_appid 永远不直接进 tenant

✅ tenant 只通过中间表关联小程序
===========================
*/
-- sys_oauth_client（OAuth2 客户端） 已有

USE aioveu_tenant;

-- OAuth2客户端与微信小程序映射表
CREATE TABLE sys_oauth_client_wx_app (
     `id` BIGINT NOT NULL AUTO_INCREMENT,
     `client_id` VARCHAR(255)  NOT NULL COMMENT 'OAuth2 client客户端 ID',
     `wx_appid` VARCHAR(255)  NOT NULL COMMENT '微信小程序appid',
     `wx_appname` VARCHAR(255) NOT NULL COMMENT '微信小程序名称',
     `registered_email` VARCHAR(255) NOT NULL COMMENT '微信小程序注册邮箱',
     `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
     `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE,
     UNIQUE KEY uk_client_id (client_id),
     UNIQUE KEY uk_wx_appid (wx_appid)
)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci
    COMMENT = 'OAuth2客户端与微信小程序映射表' ROW_FORMAT = DYNAMIC;

-- 客户端×小程序表

CREATE TABLE sys_tenant_wx_app (
                                         `id` BIGINT NOT NULL AUTO_INCREMENT,
                                         `tenant_id` BIGINT  NOT NULL COMMENT '租户ID',
                                         `wx_appid` VARCHAR(255) NOT NULL COMMENT '微信小程序ID',
                                         `wx_appname` VARCHAR(255) NOT NULL COMMENT '微信小程序名称',
                                         `registered_email` VARCHAR(255) NOT NULL COMMENT '微信小程序注册邮箱',
                                         `is_default` TINYINT DEFAULT 0 COMMENT '是否为默认小程序',
                                         `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                          PRIMARY KEY (`id`) USING BTREE,
                                          UNIQUE KEY uk_tenant_wx (tenant_id, wx_appid),
                                          KEY idx_wx_appid (wx_appid)
)ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci
    COMMENT = '租户与微信小程序关联表' ROW_FORMAT = DYNAMIC;

-- -----