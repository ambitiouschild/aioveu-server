-- 场馆同步别名
drop table if exists `sport_venue_sync_alias`;
CREATE TABLE IF NOT EXISTS `sport_venue_sync_alias`(
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
    `company_id` INT not null COMMENT '公司id',
    `store_id` int not null COMMENT '门店id',
    `venue_name` varchar(32) not null comment '场馆名称',
    `venue_id` int not null COMMENT '场馆id',
    `alias_name` varchar(32) not null comment '第三方平台场馆名称',
    `account_config_id` VARCHAR(32) not null COMMENT '账户id',
    `platform_code` VARCHAR(32) not null COMMENT '平台编号',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '场馆同步别名表';

-- 场馆场地同步别名
drop table if exists `sport_venue_field_sync_alias`;
CREATE TABLE IF NOT EXISTS `sport_venue_field_sync_alias`(
   `id` int NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
   `company_id` INT not null COMMENT '公司id',
   `store_id` int not null COMMENT '门店id',
   `venue_id` int not null COMMENT '场馆id',
   `venue_alias_name` varchar(32) not null comment '第三方平台场馆名称',
   `field_id` int not null COMMENT '场地id',
   `field_name` varchar(32) not null comment '场地名称',
   `alias_name` varchar(32) not null comment '第三方平台场地名称',
   `account_config_id` VARCHAR(32) not null COMMENT '账户id',
   `platform_code` VARCHAR(32) not null COMMENT '平台编号',
   `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
   `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '场馆场地同步别名表';

DROP TABLE if exists `aioveu`. `aioveu_audit`;
CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_audit` (
                                                        `id` INT(10) NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
    `audit_title` VARCHAR(200) NOT NULL COMMENT '审核标题' COLLATE 'utf8mb4_general_ci',
    `audit_type` TINYINT(3) NOT NULL COMMENT '审核类型 1 班级编辑 2 取消课程',
    `json_val` TEXT NULL DEFAULT NULL COMMENT 'JSON字符串数据' COLLATE 'utf8mb4_general_ci',
    `store_id` INT(10) NULL DEFAULT NULL COMMENT '店铺ID',
    `audit_service` VARCHAR(200) NULL DEFAULT NULL COMMENT '审核实现类',
    `audit_remark` VARCHAR(200) NULL DEFAULT NULL COMMENT '审核备注' COLLATE 'utf8mb4_general_ci',
    `audit_user_id` VARCHAR(32) NULL DEFAULT NULL COMMENT '审核人' COLLATE 'utf8mb4_general_ci',
    `audit_date` DATETIME NULL DEFAULT NULL COMMENT '审核时间',
    `audit_status` TINYINT(3) NOT NULL DEFAULT '1' COMMENT '状态 默认1 提交 2 通过 3 拒接',
    `status` TINYINT(3) NOT NULL DEFAULT '1' COMMENT '0 删除 1 正常',
    `create_user_id` VARCHAR(32) NOT NULL COMMENT '创建人' COLLATE 'utf8mb4_general_ci',
    `create_date` TIMESTAMP NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT 'CURRENT_TIMESTAMP' ON UPDATE (CURRENT_TIMESTAMP) COMMENT '更新时间',
    PRIMARY KEY (`id`) USING BTREE
    ) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='审批表';

