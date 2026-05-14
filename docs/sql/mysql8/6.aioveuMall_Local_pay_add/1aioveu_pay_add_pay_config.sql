





-- 执行这个SQL添加字段
use aioveu_pay;
ALTER TABLE `mq_send_record`
    ADD COLUMN exchange VARCHAR(100) NOT NULL DEFAULT '' COMMENT '交换机名称',
ADD COLUMN routing_key VARCHAR(200) NOT NULL DEFAULT '' COMMENT '路由键',
ADD INDEX idx_exchange (exchange),
ADD INDEX idx_routing_key (routing_key),
ADD INDEX idx_exchange_routing (exchange, routing_key);