USE aioveu_pay;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


/*
        这个数据库设计支持：
        多支付渠道
        完整的支付退款流程
        账户余额管理
        自动对账功能
        异步通知重试
        完整的审计日志
    */

-- ============================================================================
-- 1. 支付订单表
-- ============================================================================
DROP TABLE IF EXISTS `pay_order`;
CREATE TABLE `pay_order` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `payment_no` varchar(32) NOT NULL COMMENT '支付单号，唯一，格式：PAYyyyyMMddHHmmss+6位随机',
                             `order_no` varchar(32) NOT NULL COMMENT '业务订单号（如退款单号、订单号）',
                             `biz_type` varchar(20) NOT NULL COMMENT '业务类型：REFUND-退款 ORDER-订单 RECHARGE-充值',
                             `user_id` bigint(20) NOT NULL COMMENT '用户ID',

    -- 基础信息
                             `payment_amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '支付/退款金额',
                             `payment_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败 4-已关闭 5-已退款',
                             `payment_channel` varchar(20) NOT NULL COMMENT '支付渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额',
                             `payment_method` varchar(20) DEFAULT NULL COMMENT '支付方式：APP-APP支付 H5-H5支付 JSAPI-小程序/公众号 NATIVE-扫码支付',
                             `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
                             `payment_expire_time` datetime DEFAULT NULL COMMENT '支付过期时间',
                             `third_payment_no` varchar(128) DEFAULT NULL COMMENT '第三方支付单号',
                             `third_transaction_no` varchar(128) DEFAULT NULL COMMENT '第三方交易流水号',

    -- 金额信息
                             `attach_data` json DEFAULT NULL COMMENT '附加数据，JSON格式',
                             `notify_url` varchar(500) DEFAULT NULL COMMENT '异步通知地址',
                             `return_url` varchar(500) DEFAULT NULL COMMENT '同步返回地址',
                             `client_ip` varchar(50) DEFAULT NULL COMMENT '客户端IP',
                             `device_info` varchar(200) DEFAULT NULL COMMENT '设备信息',
                             `subject` varchar(200) DEFAULT NULL COMMENT '订单标题',
                             `body` varchar(500) DEFAULT NULL COMMENT '订单描述',
                             `currency` varchar(3) DEFAULT 'CNY' COMMENT '币种，默认人民币',
                             `error_code` varchar(50) DEFAULT NULL COMMENT '错误代码',
                             `error_message` varchar(500) DEFAULT NULL COMMENT '错误信息',

    -- 状态信息
                             `notify_status` tinyint(4) DEFAULT '0' COMMENT '通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败',
                             `notify_count` int(11) DEFAULT '0' COMMENT '通知次数',
                             `last_notify_time` datetime DEFAULT NULL COMMENT '最后通知时间',
                             `next_notify_time` datetime DEFAULT NULL COMMENT '下次通知时间',
                             `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',

    -- 时间戳
                             `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
                             `update_by` varchar(64) DEFAULT '' COMMENT '更新人',

                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号（用于乐观锁）',

    -- PRIMARY KEY (id) USING BTREE 是 MySQL 中定义主键索引时使用的语法，
    -- 表示该主键使用 B+Tree 数据结构来存储和管理索引数据。

                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `uk_payment_no` (`payment_no`) USING BTREE COMMENT '支付单号唯一',
    -- uk_order_no_biz_type: 防止同一业务单重复支付
                            UNIQUE KEY `uk_order_no_biz_type` (`order_no`, `biz_type`, `payment_channel`) USING BTREE COMMENT '业务订单号，业务类型，支付渠道唯一',
                            KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
                            KEY `idx_payment_status` (`payment_status`) USING BTREE COMMENT '支付状态索引', -- idx_payment_status: 状态查询优化
                            KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引',
                            KEY `idx_payment_channel` (`payment_channel`) USING BTREE COMMENT '支付渠道索引', -- idx_payment_channel: 渠道查询优化
                            KEY `idx_payment_time` (`payment_time`) USING BTREE COMMENT '支付时间索引', -- idx_payment_time: 时间范围查询
                            KEY `idx_third_payment_no` (`third_payment_no`) USING BTREE COMMENT '第三方支付单号索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付订单主表';
-- ROW_FORMAT=DYNAMIC;
-- 当表中包含 VARCHAR、TEXT 或 BLOB 等变长字段时，通常会使用 DYNAMIC 行格式。这种格式允许每行记录的大小动态变化，从而更有效地利用存储空间。
-- 在 DYNAMIC 格式下，变长字段的数据会被存储在行的末尾，而行的开头部分则包含一个指向这些数据的指针。这种方式可以减少存储空间的浪费，但可能会略微增加读取时的开销。
-- ============================================================================
-- 2.  退款记录表
-- ============================================================================
DROP TABLE IF EXISTS `pay_refund_record`;
CREATE TABLE `pay_refund_record` (
                             `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `refund_no` varchar(32) NOT NULL COMMENT '退款单号，格式：RFyyyyMMddHHmmss+6位随机',
                             `payment_no` varchar(32) NOT NULL COMMENT '原支付单号',
                             `order_no` varchar(32) NOT NULL COMMENT '业务订单号',
                             `biz_type` varchar(20) NOT NULL COMMENT '业务类型：REFUND-退款 ORDER-订单退款',
                             `refund_amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '退款金额',
                             `refund_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '退款状态：0-待退款 1-退款中 2-退款成功 3-退款失败 4-已关闭',
                             `refund_channel` varchar(20) NOT NULL COMMENT '退款渠道：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联 BALANCE-余额',
                             `refund_reason` varchar(500) DEFAULT NULL COMMENT '退款原因',
                             `apply_time` datetime NOT NULL COMMENT '申请时间',
                             `refund_time` datetime DEFAULT NULL COMMENT '退款完成时间',
                             `third_refund_no` varchar(128) DEFAULT NULL COMMENT '第三方退款单号',
                             `third_transaction_no` varchar(128) DEFAULT NULL COMMENT '第三方退款流水号',
                             `currency` varchar(3) DEFAULT 'CNY' COMMENT '币种',
                             `error_code` varchar(50) DEFAULT NULL COMMENT '错误代码',
                             `error_message` varchar(500) DEFAULT NULL COMMENT '错误信息',
                             `notify_status` tinyint(4) DEFAULT '0' COMMENT '通知状态：0-未通知 1-通知中 2-通知成功 3-通知失败',
                             `notify_count` int(11) DEFAULT '0' COMMENT '通知次数',
                             `last_notify_time` datetime DEFAULT NULL COMMENT '最后通知时间',
                             `next_notify_time` datetime DEFAULT NULL COMMENT '下次通知时间',
                             `callback_data` text COMMENT '回调数据',
                             `callback_time` datetime DEFAULT NULL COMMENT '回调时间',
                             `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                             `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
                             `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                             PRIMARY KEY (`id`) USING BTREE,
                             UNIQUE KEY `uk_refund_no` (`refund_no`) USING BTREE COMMENT '退款单号唯一',
                             UNIQUE KEY `uk_payment_refund` (`payment_no`, `refund_channel`) USING BTREE COMMENT '防止同一支付单重复退款',
                             KEY `idx_order_no` (`order_no`) USING BTREE COMMENT '业务订单号索引',
                             KEY `idx_payment_no` (`payment_no`) USING BTREE COMMENT '原支付单号索引',
                             KEY `idx_refund_status` (`refund_status`) USING BTREE COMMENT '退款状态索引',
                             KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引',
                             KEY `idx_refund_time` (`refund_time`) USING BTREE COMMENT '退款完成时间索引',
                             KEY `idx_third_refund_no` (`third_refund_no`) USING BTREE COMMENT '第三方退款流水号索引',
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款记录表';

-- ============================================================================
-- 3. 支付渠道配置表
-- ============================================================================
DROP TABLE IF EXISTS `pay_channel_config`;
CREATE TABLE `pay_channel_config` (
                                  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `channel_code` varchar(20) NOT NULL COMMENT '渠道编码：ALIPAY-支付宝 WECHAT-微信 UNIONPAY-银联',
                                  `channel_name` varchar(50) NOT NULL COMMENT '渠道名称',
                                  `channel_type` varchar(20) NOT NULL COMMENT '渠道类型：ONLINE-线上 OFFLINE-线下',
                                  `config_key` varchar(50) NOT NULL COMMENT '配置键',
                                  `config_value` text NOT NULL COMMENT '配置值',
                                  `config_type` varchar(20) DEFAULT NULL COMMENT '配置类型：CERT-证书 KEY-密钥 URL-地址',
                                  `config_desc` varchar(200) DEFAULT NULL COMMENT '配置描述',
                                  `is_enabled` tinyint(1) NOT NULL DEFAULT '1' COMMENT '是否启用：0-禁用 1-启用',
                                  `is_default` tinyint(1) DEFAULT '0' COMMENT '是否默认：0-否 1-是',
                                  `priority` int(11) DEFAULT '0' COMMENT '优先级，数值越大优先级越高',
                                  `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                                  `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
                                  `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                   PRIMARY KEY (`id`) USING BTREE,
                                   UNIQUE KEY `uk_channel_config` (`channel_code`, `config_key`) USING BTREE COMMENT '渠道编码 和 配置键唯一',
                                   KEY `idx_channel_code` (`channel_code`) USING BTREE COMMENT '渠道编码索引',
                                   KEY `idx_is_enabled` (`is_enabled`) USING BTREE COMMENT '逻辑删除索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付渠道配置表';

-- 初始化支付宝配置
INSERT INTO `payment_channel_config`
(`channel_code`, `channel_name`, `channel_type`, `config_key`, `config_value`, `config_type`, `config_desc`, `is_enabled`, `is_default`, `priority`) VALUES
('ALIPAY', '支付宝', 'ONLINE', 'app_id', 'your_app_id', 'STRING', '应用ID', 1, 1, 100),
('ALIPAY', '支付宝', 'ONLINE', 'private_key', 'your_private_key', 'SECRET', '应用私钥', 1, 1, 100),
('ALIPAY', '支付宝', 'ONLINE', 'alipay_public_key', 'your_public_key', 'SECRET', '支付宝公钥', 1, 1, 100),
('ALIPAY', '支付宝', 'ONLINE', 'gateway_url', 'https://openapi.alipay.com/gateway.do', 'URL', '网关地址', 1, 1, 100),
('WECHAT', '微信支付', 'ONLINE', 'app_id', 'your_app_id', 'STRING', '应用ID', 1, 0, 90),
('WECHAT', '微信支付', 'ONLINE', 'mch_id', 'your_mch_id', 'STRING', '商户号', 1, 0, 90);
-- ============================================================================
-- 4. 支付对账表
-- ============================================================================
DROP TABLE IF EXISTS `pay_reconciliation`;
CREATE TABLE `pay_reconciliation` (
                                      `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                      `reconciliation_no` varchar(32) NOT NULL COMMENT '对账单号',
                                      `channel_code` varchar(20) NOT NULL COMMENT '渠道编码',
                                      `bill_date` date NOT NULL COMMENT '对账日期',
                                      `bill_type` varchar(20) NOT NULL COMMENT '账单类型：PAYMENT-支付 REFUND-退款 ALL-全部',
                                      `total_count` int(11) DEFAULT '0' COMMENT '总笔数',
                                      `total_amount` decimal(15,2) DEFAULT '0.00' COMMENT '总金额',
                                      `success_count` int(11) DEFAULT '0' COMMENT '成功笔数',
                                      `success_amount` decimal(15,2) DEFAULT '0.00' COMMENT '成功金额',
                                      `failure_count` int(11) DEFAULT '0' COMMENT '失败笔数',
                                      `failure_amount` decimal(15,2) DEFAULT '0.00' COMMENT '失败金额',
                                      `difference_count` int(11) DEFAULT '0' COMMENT '差异笔数',
                                      `reconcile_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '对账状态：0-未对账 1-对账中 2-对账完成 3-对账异常',
                                      `download_status` tinyint(4) DEFAULT '0' COMMENT '下载状态：0-未下载 1-下载中 2-下载完成 3-下载失败',
                                      `download_time` datetime DEFAULT NULL COMMENT '下载时间',
                                      `reconcile_time` datetime DEFAULT NULL COMMENT '对账时间',
                                      `bill_file_url` varchar(500) DEFAULT NULL COMMENT '账单文件URL',
                                      `error_message` varchar(1000) DEFAULT NULL COMMENT '错误信息',
                                      `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                                      `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
                                      `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
                                      `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                      PRIMARY KEY (`id`) USING BTREE,
                                      UNIQUE KEY `uk_bill_date_channel` (`bill_date`, `channel_code`, `bill_type`) USING BTREE COMMENT '对账日期 和 渠道编码 和 账单类型唯一',
                                      KEY `idx_reconcile_status` (`reconcile_status`) USING BTREE COMMENT '对账状态索引',
                                      KEY `idx_bill_date` (`bill_date`) USING BTREE COMMENT '对账日期索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付对账表';

-- ============================================================================
-- 5. 对账明细表
-- ============================================================================
DROP TABLE IF EXISTS `pay_reconciliation_detail`;
CREATE TABLE `pay_reconciliation_detail` (
                                         `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         `reconciliation_id` bigint(20) NOT NULL COMMENT '对账单ID',
                                         `channel_code` varchar(20) NOT NULL COMMENT '渠道编码',
                                         `bill_date` date NOT NULL COMMENT '对账日期',
                                         `third_transaction_no` varchar(128) NOT NULL COMMENT '第三方交易流水号',
                                         `third_order_no` varchar(128) DEFAULT NULL COMMENT '第三方订单号',
                                         `payment_no` varchar(32) DEFAULT NULL COMMENT '系统支付单号',
                                         `order_no` varchar(32) DEFAULT NULL COMMENT '业务订单号',
                                         `trade_type` varchar(20) NOT NULL COMMENT '交易类型：PAYMENT-支付 REFUND-退款',
                                         `trade_time` datetime NOT NULL COMMENT '交易时间',
                                         `trade_amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '交易金额',
                                         `trade_status` varchar(20) NOT NULL COMMENT '交易状态：SUCCESS-成功 FAIL-失败 PROCESSING-处理中',
                                         `reconcile_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '对账状态：0-未对账 1-成功 2-失败 3-系统多 4-渠道多',
                                         `difference_reason` varchar(200) DEFAULT NULL COMMENT '差异原因',
                                         `reconcile_result` varchar(20) DEFAULT NULL COMMENT '对账结果：MATCH-匹配 SYS_MORE-系统多 CHANNEL_MORE-渠道多',
                                         `reconcile_time` datetime DEFAULT NULL COMMENT '对账时间',
                                         `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                         `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                         PRIMARY KEY (`id`) USING BTREE,
                                         UNIQUE KEY `uk_channel_transaction` (`channel_code`, `third_transaction_no`, `bill_date`) USING BTREE COMMENT '渠道编码 和 第三方交易流水号 和 对账日期唯一',
                                         KEY `idx_reconciliation_id` (`reconciliation_id`) USING BTREE COMMENT '对账单ID索引',
                                         KEY `idx_payment_no` (`payment_no`) USING BTREE COMMENT '系统支付单号索引',
                                         KEY `idx_order_no` (`order_no`) USING BTREE COMMENT '业务订单号索引',
                                         KEY `idx_trade_time` (`trade_time`) USING BTREE COMMENT '交易时间索引',
                                         KEY `idx_reconcile_status` (`reconcile_status`) USING BTREE COMMENT '对账状态索引',
                                         KEY `idx_bill_date` (`bill_date`) USING BTREE COMMENT '对账日期索引',
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='对账明细表';

-- ============================================================================
-- 6. 支付流水表
-- ============================================================================
DROP TABLE IF EXISTS `pay_flow`;
CREATE TABLE `pay_flow` (
                            `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                            `flow_no` varchar(32) NOT NULL COMMENT '流水号',
                            `payment_no` varchar(32) NOT NULL COMMENT '支付单号',
                            `refund_no` varchar(32) DEFAULT NULL COMMENT '退款单号',
                            `order_no` varchar(32) NOT NULL COMMENT '业务订单号',
                            `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                            `flow_type` varchar(20) NOT NULL COMMENT '流水类型：PAYMENT-支付 REFUND-退款 SETTLEMENT-结算 ADJUST-调账',
                            `flow_direction` varchar(10) NOT NULL COMMENT '资金方向：IN-入金 OUT-出金',
                            `amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '流水金额',
                            `balance_before` decimal(15,2) DEFAULT '0.00' COMMENT '交易前余额',
                            `balance_after` decimal(15,2) DEFAULT '0.00' COMMENT '交易后余额',
                            `channel_code` varchar(20) NOT NULL COMMENT '渠道编码',
                            `third_flow_no` varchar(128) DEFAULT NULL COMMENT '第三方流水号',
                            `flow_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '流水状态：0-处理中 1-成功 2-失败',
                            `trade_time` datetime NOT NULL COMMENT '交易时间',
                            `complete_time` datetime DEFAULT NULL COMMENT '完成时间',
                            `error_code` varchar(50) DEFAULT NULL COMMENT '错误代码',
                            `error_message` varchar(500) DEFAULT NULL COMMENT '错误信息',
                            `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                            `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                            `create_by` varchar(64) DEFAULT '' COMMENT '创建人',
                            `update_by` varchar(64) DEFAULT '' COMMENT '更新人',
                            `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                            PRIMARY KEY (`id`) USING BTREE,
                            UNIQUE KEY `uk_flow_no` (`flow_no`) USING BTREE COMMENT '流水号唯一',
                            UNIQUE KEY `uk_third_flow` (`channel_code`, `third_flow_no`, `flow_type`) USING BTREE COMMENT '渠道编码 和 第三方流水号 和 流水类型唯一',
                            KEY `idx_payment_no` (`payment_no`) USING BTREE COMMENT '支付单号索引',
                            KEY `idx_refund_no` (`refund_no`) USING BTREE COMMENT '退款单号索引',
                            KEY `idx_order_no` (`order_no`) USING BTREE COMMENT '业务订单号索引',
                            KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
                            KEY `idx_trade_time` (`trade_time`) USING BTREE COMMENT '交易时间索引',
                            KEY `idx_flow_type` (`flow_type`) USING BTREE COMMENT '流水类型索引',
                            KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引',

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付流水表';

-- ============================================================================
-- 7. 支付通知表
-- ============================================================================
DROP TABLE IF EXISTS `pay_notify`;
CREATE TABLE `pay_notify` (
                              `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                              `notify_no` varchar(32) NOT NULL COMMENT '通知编号',
                              `payment_no` varchar(32) DEFAULT NULL COMMENT '支付单号',
                              `refund_no` varchar(32) DEFAULT NULL COMMENT '退款单号',
                              `notify_type` varchar(20) NOT NULL COMMENT '通知类型：PAYMENT-支付 REFUND-退款',
                              `notify_url` varchar(500) NOT NULL COMMENT '通知地址',
                              `request_data` text COMMENT '请求数据',
                              `response_data` text COMMENT '响应数据',
                              `notify_status` tinyint(4) NOT NULL DEFAULT '0' COMMENT '通知状态：0-待通知 1-通知中 2-通知成功 3-通知失败',
                              `notify_count` int(11) NOT NULL DEFAULT '0' COMMENT '通知次数',
                              `max_notify_count` int(11) NOT NULL DEFAULT '5' COMMENT '最大通知次数',
                              `next_notify_time` datetime NOT NULL COMMENT '下次通知时间',
                              `last_notify_time` datetime DEFAULT NULL COMMENT '最后通知时间',
                              `success_time` datetime DEFAULT NULL COMMENT '成功时间',
                              `error_message` varchar(1000) DEFAULT NULL COMMENT '错误信息',
                              `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                              `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE KEY `uk_notify_no` (`notify_no`) USING BTREE COMMENT '通知编号唯一',
                              KEY `idx_payment_no` (`payment_no`) USING BTREE COMMENT '支付单号索引',
                              KEY `idx_refund_no` (`refund_no`) USING BTREE COMMENT '退款单号索引',
                              KEY `idx_notify_status` (`notify_status`) USING BTREE COMMENT '通知状态索引',
                              KEY `idx_next_notify_time` (`next_notify_time`) USING BTREE COMMENT '下次通知时间索引',
                              KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引',

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付通知表';


-- ============================================================================
-- 8. 支付账户表
-- ============================================================================
DROP TABLE IF EXISTS `pay_account`;
CREATE TABLE `pay_account` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                               `user_id` bigint(20) NOT NULL COMMENT '用户ID',
                               `account_type` varchar(20) NOT NULL COMMENT '账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户',
                               `balance` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '账户余额',
                               `frozen_balance` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '冻结余额',
                               `available_balance` decimal(15,2) GENERATED ALWAYS AS (`balance` - `frozen_balance`) VIRTUAL COMMENT '可用余额',
                               `total_income` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '总收入',
                               `total_expend` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '总支出',
                               `currency` varchar(3) DEFAULT 'CNY' COMMENT '币种',
                               `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT '账户状态：0-冻结 1-正常 2-注销',
                               `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号，用于乐观锁',
                               `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                              PRIMARY KEY (`id`) USING BTREE,
                              UNIQUE KEY `uk_account_no` (`account_no`) USING BTREE COMMENT '账户编号唯一',
                              UNIQUE KEY `uk_user_account_type` (`user_id`, `account_type`) USING BTREE COMMENT '用户ID 和 账户类型 唯一',
                              KEY `idx_user_id` (`user_id`) USING BTREE COMMENT '用户ID索引',
                              KEY `idx_status` (`status`) USING BTREE COMMENT '账户状态索引',

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='支付账户表';


-- ============================================================================
-- 8. 账户流水表
-- ============================================================================
DROP TABLE IF EXISTS `pay_account_flow`;
CREATE TABLE `pay_account_flow` (
                                    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                    `flow_no` varchar(32) NOT NULL COMMENT '流水号',
                                    `account_no` varchar(32) NOT NULL COMMENT '账户编号',
                                    `biz_no` varchar(32) NOT NULL COMMENT '业务单号',
                                    `biz_type` varchar(20) NOT NULL COMMENT '业务类型：PAYMENT-支付 REFUND-退款 RECHARGE-充值 WITHDRAW-提现',
                                    `flow_type` varchar(20) NOT NULL COMMENT '流水类型：INCOME-收入 EXPEND-支出 FREEZE-冻结 UNFREEZE-解冻',
                                    `amount` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '变动金额',
                                    `balance_before` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '变动前余额',
                                    `balance_after` decimal(15,2) NOT NULL DEFAULT '0.00' COMMENT '变动后余额',
                                    `frozen_before` decimal(15,2) DEFAULT '0.00' COMMENT '变动前冻结',
                                    `frozen_after` decimal(15,2) DEFAULT '0.00' COMMENT '变动后冻结',
                                    `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                    `is_deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-未删除 1-已删除',
                                    `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                               PRIMARY KEY (`id`) USING BTREE,
                               UNIQUE KEY `uk_flow_no` (`flow_no`) USING BTREE COMMENT '流水号唯一',
                               KEY `idx_account_no` (`account_no`) USING BTREE COMMENT '账户编号索引',
                               KEY `idx_biz_no` (`biz_no`) USING BTREE COMMENT '业务单号索引',
                               KEY `idx_create_time` (`create_time`) USING BTREE COMMENT '创建时间索引',

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='账户流水表';

-- ============================================================================
-- 9. Table structure for undo_log
-- ============================================================================

DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
                             `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
                             `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
                             `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
                             `rollback_info` longblob NOT NULL COMMENT 'rollback info',
                             `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
                             `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
                             `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
                             UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '回滚日志表 AT transaction mode undo table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;