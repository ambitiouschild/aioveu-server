





-- 执行这个SQL添加字段
use aioveu_pay;
ALTER TABLE `mq_send_record`
    -- RabbitMQ相关
ADD COLUMN  correlation_id VARCHAR(64) COMMENT 'RabbitMQ关联ID',
ADD COLUMN  message_type VARCHAR(50) COMMENT '消息类型';
