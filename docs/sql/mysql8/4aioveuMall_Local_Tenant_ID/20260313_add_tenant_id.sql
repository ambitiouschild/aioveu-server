-- ============================================
-- 多租户字段添加脚本
-- 执行前务必：1.备份数据库 2.在业务低峰期执行
-- ============================================

-- 注意：如果表中已有数据，DEFAULT 0会导致所有数据被归到租户0
-- 需要根据业务情况处理历史数据

/*
===========================
UMS 用户管理系统
===========================
*/
USE aioveu_ums;

-- 用户表
ALTER TABLE ums_member ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE ums_member SET tenant_id = 1;  -- 将历史数据分配给默认租户
ALTER TABLE ums_member ADD INDEX idx_tenant (tenant_id);

-- 用户地址表
ALTER TABLE ums_member_address ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE ums_member_address SET tenant_id = 1;  -- 将历史数据分配给默认租户
ALTER TABLE ums_member_address ADD INDEX idx_tenant (tenant_id);

/*
===========================
SMS 营销管理系统
===========================
*/
USE aioveu_sms;

-- 广告表
ALTER TABLE sms_advert ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE sms_advert SET tenant_id = 1;
ALTER TABLE sms_advert ADD INDEX idx_tenant (tenant_id);

-- 优惠券表
ALTER TABLE sms_coupon ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE sms_coupon SET tenant_id = 1;
ALTER TABLE sms_coupon ADD INDEX idx_tenant (tenant_id);

-- 优惠券历史表
ALTER TABLE sms_coupon_history ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE sms_coupon_history SET tenant_id = 1;
ALTER TABLE sms_coupon_history ADD INDEX idx_tenant (tenant_id);

-- 优惠券商品关联表
ALTER TABLE sms_coupon_spu ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE sms_coupon_spu SET tenant_id = 1;
ALTER TABLE sms_coupon_spu ADD INDEX idx_tenant (tenant_id);

-- 优惠券商品分类表
ALTER TABLE sms_coupon_spu_category ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE sms_coupon_spu_category SET tenant_id = 1;
ALTER TABLE sms_coupon_spu_category ADD INDEX idx_tenant (tenant_id);

-- 首页广告表
ALTER TABLE sms_home_advert ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE sms_home_advert SET tenant_id = 1;
ALTER TABLE sms_home_advert ADD INDEX idx_tenant (tenant_id);

-- 首页分类表
ALTER TABLE sms_home_category ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE sms_home_category SET tenant_id = 1;
ALTER TABLE sms_home_category ADD INDEX idx_tenant (tenant_id);

/*
===========================
OMS 订单管理系统
===========================
*/
USE aioveu_oms;

-- 订单主表
ALTER TABLE oms_order ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE oms_order SET tenant_id = 1;
ALTER TABLE oms_order ADD INDEX idx_tenant (tenant_id);

-- 订单发货表
ALTER TABLE oms_order_delivery ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE oms_order_delivery SET tenant_id = 1;
ALTER TABLE oms_order_delivery ADD INDEX idx_tenant (tenant_id);

-- 订单明细表
ALTER TABLE oms_order_item ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE oms_order_item SET tenant_id = 1;
ALTER TABLE oms_order_item ADD INDEX idx_tenant (tenant_id);

-- 订单日志表
ALTER TABLE oms_order_log ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE oms_order_log SET tenant_id = 1;
ALTER TABLE oms_order_log ADD INDEX idx_tenant (tenant_id);

-- 订单支付表
ALTER TABLE oms_order_pay ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE oms_order_pay SET tenant_id = 1;
ALTER TABLE oms_order_pay ADD INDEX idx_tenant (tenant_id);

-- 订单设置表
ALTER TABLE oms_order_setting ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE oms_order_setting SET tenant_id = 1;
ALTER TABLE oms_order_setting ADD INDEX idx_tenant (tenant_id);

/*
===========================
PMS 产品管理系统
===========================
*/
USE aioveu_pms;

-- 品牌表
ALTER TABLE pms_brand ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pms_brand SET tenant_id = 1;
ALTER TABLE pms_brand ADD INDEX idx_tenant (tenant_id);

-- 分类表
ALTER TABLE pms_category ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pms_category SET tenant_id = 1;
ALTER TABLE pms_category ADD INDEX idx_tenant (tenant_id);

-- 分类属性表
ALTER TABLE pms_category_attribute ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pms_category_attribute SET tenant_id = 1;
ALTER TABLE pms_category_attribute ADD INDEX idx_tenant (tenant_id);

-- 分类品牌关联表
ALTER TABLE pms_category_brand ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pms_category_brand SET tenant_id = 1;
ALTER TABLE pms_category_brand ADD INDEX idx_tenant (tenant_id);

-- SKU表
ALTER TABLE pms_sku ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pms_sku SET tenant_id = 1;
ALTER TABLE pms_sku ADD INDEX idx_tenant (tenant_id);

-- SPU表
ALTER TABLE pms_spu ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pms_spu SET tenant_id = 1;
ALTER TABLE pms_spu ADD INDEX idx_tenant (tenant_id);

-- SPU属性表
ALTER TABLE pms_spu_attribute ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pms_spu_attribute SET tenant_id = 1;
ALTER TABLE pms_spu_attribute ADD INDEX idx_tenant (tenant_id);

/*
===========================
PAY 支付系统
===========================
*/
USE aioveu_pay;

-- 账户表
ALTER TABLE pay_account ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_account SET tenant_id = 1;
ALTER TABLE pay_account ADD INDEX idx_tenant (tenant_id);

-- 账户流水表
ALTER TABLE pay_account_flow ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_account_flow SET tenant_id = 1;
ALTER TABLE pay_account_flow ADD INDEX idx_tenant (tenant_id);

-- 支付渠道配置表
ALTER TABLE pay_channel_config ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_channel_config SET tenant_id = 1;
ALTER TABLE pay_channel_config ADD INDEX idx_tenant (tenant_id);

-- 支付流水表
ALTER TABLE pay_flow ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_flow SET tenant_id = 1;
ALTER TABLE pay_flow ADD INDEX idx_tenant (tenant_id);

-- 支付通知表
ALTER TABLE pay_notify ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_notify SET tenant_id = 1;
ALTER TABLE pay_notify ADD INDEX idx_tenant (tenant_id);

-- 支付订单表
ALTER TABLE pay_order ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_order SET tenant_id = 1;
ALTER TABLE pay_order ADD INDEX idx_tenant (tenant_id);

-- 对账表
ALTER TABLE pay_reconciliation ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_reconciliation SET tenant_id = 1;
ALTER TABLE pay_reconciliation ADD INDEX idx_tenant (tenant_id);

-- 对账明细表
ALTER TABLE pay_reconciliation_detail ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_reconciliation_detail SET tenant_id = 1;
ALTER TABLE pay_reconciliation_detail ADD INDEX idx_tenant (tenant_id);

-- 退款记录表（注意：此表可能在aioveu_pay或aioveu_refund）
ALTER TABLE pay_refund_record ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE pay_refund_record SET tenant_id = 1;
ALTER TABLE pay_refund_record ADD INDEX idx_tenant (tenant_id);

/*
===========================
REFUND 退款系统
===========================
*/
USE aioveu_refund;

-- 退款发货表
ALTER TABLE refund_delivery ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE refund_delivery SET tenant_id = 1;
ALTER TABLE refund_delivery ADD INDEX idx_tenant (tenant_id);

-- 退款商品表
ALTER TABLE refund_item ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE refund_item SET tenant_id = 1;
ALTER TABLE refund_item ADD INDEX idx_tenant (tenant_id);

-- 退款操作日志表
ALTER TABLE refund_operation_log ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE refund_operation_log SET tenant_id = 1;
ALTER TABLE refund_operation_log ADD INDEX idx_tenant (tenant_id);

-- 退款订单表
ALTER TABLE refund_order ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE refund_order SET tenant_id = 1;
ALTER TABLE refund_order ADD INDEX idx_tenant (tenant_id);

-- 退款支付表
ALTER TABLE refund_payment ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE refund_payment SET tenant_id = 1;
ALTER TABLE refund_payment ADD INDEX idx_tenant (tenant_id);

-- 退款凭证表（去掉重复的）
ALTER TABLE refund_proof ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE refund_proof SET tenant_id = 1;
ALTER TABLE refund_proof ADD INDEX idx_tenant (tenant_id);

-- 退款原因表
ALTER TABLE refund_reason ADD COLUMN tenant_id BIGINT NOT NULL COMMENT '租户ID';
UPDATE refund_reason SET tenant_id = 1;
ALTER TABLE refund_reason ADD INDEX idx_tenant (tenant_id);