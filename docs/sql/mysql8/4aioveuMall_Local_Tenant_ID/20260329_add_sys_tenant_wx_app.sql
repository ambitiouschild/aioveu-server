-- ============================================

USE aioveu_tenant;

-- 添加 app_secret 字段
ALTER TABLE `sys_tenant_wx_app`
    ADD COLUMN `app_secret` VARCHAR(200) NULL COMMENT '小程序/公众号密钥' AFTER `wx_appid`;

-- 添加其他可能需要的字段
ALTER TABLE `sys_tenant_wx_app`
    ADD COLUMN `app_name` VARCHAR(100) NULL COMMENT '应用名称' AFTER `app_secret`,
    ADD COLUMN `app_type` VARCHAR(20) DEFAULT 'MINI_PROGRAM' COMMENT '应用类型: MINI_PROGRAM-小程序, OFFICIAL_ACCOUNT-公众号' AFTER `app_name`,
    ADD COLUMN `status` TINYINT DEFAULT 1 COMMENT '状态: 0-禁用, 1-启用' AFTER `app_type`;

-- 添加索引
ALTER TABLE `sys_tenant_wx_app`
    ADD INDEX `idx_tenant_app_type` (`tenant_id`, `app_type`),
    ADD UNIQUE INDEX `uk_wx_appid` (`wx_appid`);