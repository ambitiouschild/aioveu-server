USE aioveu_refund;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;


/*问题分析：
表太多：16张表，实际只需要3-5张核心表就够了
字段太多：退款主表有30+个字段，太臃肿
功能过度：风控、统计、快照、有效期等全部集成，初期没必要
维护困难：表关系复杂，开发和维护成本高
性能考虑：JOIN太多会影响查询性能*/

-- ============================================================================
-- 1. 订单退款申请表（主表）
-- ============================================================================
DROP TABLE IF EXISTS `refund_order`;
CREATE TABLE `refund_order` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                `order_id` bigint(20) NOT NULL COMMENT '订单ID',
                                `order_sn` varchar(64) NOT NULL COMMENT '订单编号',
                                `refund_sn` varchar(64) NOT NULL COMMENT '退款单号',
                                `user_id` bigint(20) NOT NULL COMMENT '用户ID',

                                -- 基础信息
                                `refund_type` tinyint(1) NOT NULL COMMENT '退款类型：1-仅退款 2-退货退款 3-换货',
                                `refund_reason` varchar(200) NOT NULL COMMENT '退款原因',
                                `description` varchar(500) DEFAULT NULL COMMENT '补充说明',
                                `proof_images` json COMMENT '退款凭证图片（JSON数组）',

                                -- 金额信息
                                `refund_amount` decimal(10,2) NOT NULL COMMENT '申请退款金额（分）',
                                `actual_refund_amount` decimal(10,2) DEFAULT NULL COMMENT '实际退款金额（分）',

                                -- 状态信息
                                `status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '退款状态：0-待处理 1-审核中 2-审核通过 3-审核拒绝 4-退款中 5-退款成功 6-退款失败',
                                `handle_note` varchar(500) DEFAULT NULL COMMENT '处理备注',
                                `handle_by` varchar(50) DEFAULT NULL COMMENT '处理人',
                                `handle_time` datetime DEFAULT NULL COMMENT '处理时间',

                                -- 时间戳
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-正常 1-删除',
                                `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号（用于乐观锁）',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_refund_sn` (`refund_sn`) COMMENT '退款单号唯一',
                                KEY `idx_order_id` (`order_id`) COMMENT '订单ID索引',
                                KEY `idx_user_id` (`user_id`) COMMENT '用户ID索引',
                                KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引',
                                KEY `idx_status` (`status`) COMMENT '退款状态索引',
                                    KEY `idx_user_status` (`user_id`, `status`) COMMENT '用户+退款状态复合索引',
                                    KEY `idx_order_status` (`order_id`,`status`) COMMENT '订单+退款状态复合索引',
                                    KEY `idx_time_status` (`create_time`,`status`) COMMENT '创建时间+退款状态复合索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='订单退款申请表';

-- ============================================================================
-- 2. 退款商品明细表
-- ============================================================================
DROP TABLE IF EXISTS `refund_item`;
CREATE TABLE `refund_item` (
                               `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                               `refund_id` bigint(20) NOT NULL COMMENT '退款申请ID',
                               `refund_type` tinyint(1) DEFAULT NULL COMMENT '退款类型（冗余字段，与主表一致）',
                                `order_item_id` bigint(20) NOT NULL COMMENT '订单项ID',
                               `spu_id` bigint(20) NOT NULL COMMENT '商品ID',
                               `spu_name` varchar(200) NOT NULL COMMENT '商品名称',
                               `sku_id` bigint(20) NOT NULL COMMENT 'SKU ID',
                               `sku_name` varchar(200) DEFAULT NULL COMMENT 'SKU名称',
                               `pic_url` varchar(500) DEFAULT NULL COMMENT '商品图片',
                               `price` decimal(10,2) NOT NULL COMMENT '商品单价（分）',
                               `quantity` int(11) NOT NULL DEFAULT '1' COMMENT '退款数量',
                               `refund_amount` decimal(10,2) NOT NULL COMMENT '退款金额（分）',
                               `refund_reason` varchar(200) DEFAULT NULL COMMENT '该商品的退款原因',
                               `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                               PRIMARY KEY (`id`),
                               KEY `idx_refund_id` (`refund_id`) COMMENT '退款申请ID索引',
                               KEY `idx_order_item_id` (`order_item_id`) COMMENT '订单项ID索引',
                               KEY `idx_sku_id` (`sku_id`) COMMENT 'SKU ID索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款商品明细表';

-- ============================================================================
-- 3. 退款物流信息表（用于退货）
-- ============================================================================
DROP TABLE IF EXISTS `refund_delivery`;
CREATE TABLE `refund_delivery` (
                                   `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `refund_id` bigint(20) NOT NULL COMMENT '退款申请ID',
                                   `delivery_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '物流类型：1-买家发货 2-卖家发货 3-换货发货',
                                   `delivery_company` varchar(100) NOT NULL COMMENT '物流公司',
                                   `delivery_sn` varchar(100) NOT NULL COMMENT '物流单号',
                                   `receiver_name` varchar(100) NOT NULL COMMENT '收货人姓名',
                                   `receiver_phone` varchar(20) NOT NULL COMMENT '收货人电话',
                                   `receiver_address` varchar(500) NOT NULL COMMENT '收货地址',
                                   `delivery_time` datetime DEFAULT NULL COMMENT '发货时间',
                                   `receive_time` datetime DEFAULT NULL COMMENT '收货时间',
                                   `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                   `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_refund_id_type` (`refund_id`, `delivery_type`) COMMENT '退款申请ID 和 物流类型唯一',
                                   KEY `idx_delivery_sn` (`delivery_sn`) COMMENT '物流单号索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款物流信息表（用于退货）';

-- ============================================================================
-- 4. 退款操作记录表（用于审计）
-- ============================================================================
DROP TABLE IF EXISTS `refund_operation_log`;
CREATE TABLE `refund_operation_log` (
                                        `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                        `refund_id` bigint(20) NOT NULL COMMENT '退款申请ID',
                                        `operation_type` tinyint(2) NOT NULL COMMENT '操作类型：1-用户申请 2-客服审核 3-财务审核 4-商家处理 5-用户操作 6-系统自动',
                                        `operation_content` varchar(500) NOT NULL COMMENT '操作内容',
                                        `operator_id` bigint(20) DEFAULT NULL COMMENT '操作人ID',
                                        `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
                                        `operator_type` tinyint(1) DEFAULT '1' COMMENT '操作人类型：1-用户 2-客服 3-商家 4-系统',
                                        `before_status` tinyint(2) DEFAULT NULL COMMENT '操作前状态',
                                        `after_status` tinyint(2) DEFAULT NULL COMMENT '操作后状态',
                                        `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                        `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        PRIMARY KEY (`id`),
                                        KEY `idx_refund_id` (`refund_id`) COMMENT '退款申请ID索引',
                                        KEY `idx_create_time` (`create_time`) COMMENT '创建时间索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款操作记录表（用于审计）';

-- ============================================================================
-- 5. 退款凭证图片表
-- ============================================================================
DROP TABLE IF EXISTS `refund_proof`;
CREATE TABLE `refund_proof` (
                                `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                `refund_id` bigint(20) NOT NULL COMMENT '退款申请ID',
                                `proof_type` tinyint(1) NOT NULL DEFAULT '1' COMMENT '凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他',
                                `image_url` varchar(500) NOT NULL COMMENT '图片URL',
                                `image_desc` varchar(200) DEFAULT NULL COMMENT '图片描述',
                                `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除',
                                PRIMARY KEY (`id`),
                                KEY `idx_refund_id` (`refund_id`) COMMENT '退款申请ID索引'

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款凭证图片表';

-- ============================================================================
-- 6. 退款支付记录表
-- ============================================================================
DROP TABLE IF EXISTS `refund_payment`;
CREATE TABLE `refund_payment` (
                           `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                           `refund_id` bigint(20) NOT NULL COMMENT '退款申请ID',
                           `payment_sn` varchar(64) NOT NULL COMMENT '退款支付单号',
                           `payment_type` tinyint(1) NOT NULL COMMENT '支付类型：1-微信 2-支付宝 3-银行卡 4-余额',
                           `payment_amount` decimal(10,2) NOT NULL COMMENT '支付金额（分）',
                           `payment_status` tinyint(1) NOT NULL DEFAULT '0' COMMENT '支付状态：0-待支付 1-支付中 2-支付成功 3-支付失败',
                           `payment_time` datetime DEFAULT NULL COMMENT '支付时间',
                           `payment_channel` varchar(50) DEFAULT NULL COMMENT '支付渠道',
                           `payment_trade_no` varchar(100) DEFAULT NULL COMMENT '支付交易号',
                           `payment_fee` decimal(10,2) DEFAULT '0.00' COMMENT '支付手续费（分）',
                           `remark` varchar(200) DEFAULT NULL COMMENT '备注',
                           `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_payment_sn` (`payment_sn`) COMMENT '退款支付单号唯一',
                           KEY `idx_refund_id` (`refund_id`) COMMENT '退款申请ID索引',
                           KEY `idx_payment_status` (`payment_status`) COMMENT '支付状态索引'

) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款支付记录表';

-- ============================================================================
-- 7. 退款原因分类表
-- ============================================================================
DROP TABLE IF EXISTS `refund_reason`;
CREATE TABLE `refund_reason` (
                                 `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `reason_type` tinyint(1) NOT NULL COMMENT '原因类型：1-仅退款原因 2-退货退款原因 3-换货原因',
                                 `reason_content` varchar(100) NOT NULL COMMENT '原因内容',
                                 `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序',
                                 `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_reason_type` (`reason_type`) COMMENT '原因类型索引'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='退款原因分类表';

-- 插入默认的退款原因
INSERT INTO `refund_reason` (`reason_type`, `reason_content`, `sort`, `status`) VALUES
                            (1, '拍错/不想要了', 1, 1),
                            (1, '商品信息描述不符', 2, 1),
                            (1, '质量问题', 3, 1),
                            (1, '快递/物流问题', 4, 1),
                            (1, '未按时发货', 5, 1),
                            (1, '其他原因', 6, 1),
                            (2, '商品信息描述不符', 1, 1),
                            (2, '质量问题', 2, 1),
                            (2, '收到商品破损', 3, 1),
                            (2, '商品错发/漏发', 4, 1),
                            (2, '其他原因', 5, 1),
                            (3, '商品信息描述不符', 1, 1),
                            (3, '质量问题', 2, 1),
                            (3, '收到商品破损', 3, 1),
                            (3, '商品错发/漏发', 4, 1),
                            (3, '尺码不合适', 5, 1);

-- ============================================================================
-- 8. 退款订单快照表
-- ============================================================================


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