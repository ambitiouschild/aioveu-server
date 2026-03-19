-- ============================================

/*
===========================
appid 是“租户”和“小程序”之间的桥梁
===========================

✅ NOT NULL：

一个租户必须绑定一个 appid（你现在这个模型下）

✅ 唯一索引：

防止一个 appid 被多个租户误用（非常关键）

为什么不叫 appid，而是 wx_appid

SELECT id
FROM sys_tenant
WHERE wx_appid = 'wx1234567890abcdef';

✅ 登录时序（你这套表能完美支撑）

*/


USE aioveu_tenant;

-- 用户表
ALTER TABLE sys_tenant ADD COLUMN `wx_appid` VARCHAR(32) NOT NULL COMMENT '微信小程序appid';
ALTER TABLE sys_tenant ADD INDEX `uk_wx_appid`  (`wx_appid` ASC);

