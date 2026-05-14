





-- 执行这个SQL添加字段
use aioveu_pay;
ALTER TABLE `mq_send_record`
    -- RabbitMQ相关
ADD COLUMN cost_time BIGINT COMMENT '发送耗时（毫秒）',
ADD COLUMN broker_ack_time DATETIME COMMENT 'Broker确认时间',
ADD COLUMN network_cost_time BIGINT COMMENT '网络传输耗时',
ADD COLUMN broker_process_cost_time BIGINT COMMENT 'Broker处理耗时',

ADD INDEX idx_cost_time (cost_time);
