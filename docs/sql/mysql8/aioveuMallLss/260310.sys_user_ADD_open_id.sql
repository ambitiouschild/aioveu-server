-- 添加openid字段，建议用下划线命名：open_id

USE aioveu_tenant;

ALTER TABLE sys_user
    ADD COLUMN open_id VARCHAR(64)
        DEFAULT NULL
    COMMENT '微信openid，用于微信登录';

-- 添加索引
ALTER TABLE sys_user ADD INDEX idx_openid (open_id);