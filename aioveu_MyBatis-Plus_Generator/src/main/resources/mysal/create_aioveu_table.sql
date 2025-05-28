-- 创建 aioveu_company 公司数据表
drop table if exists `aioveu`. `aioveu_company`;
CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_company` (
  `id` int NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
  `name` varchar(32) NOT NULL COMMENT '公司名称',
  `address` varchar(128)  NOT NULL COMMENT '公司地址',
  `province_id` int NOT NULL COMMENT '省份Id',
  `city_id` int NOT NULL COMMENT '城市Id',
  `region_id` int NOT NULL COMMENT '区域Id',
  `tel` varchar(20) NOT NULL COMMENT '公司电话',
  `business_license` varchar(256) DEFAULT NULL COMMENT '营业执照',
  `mp_app_id` varchar(32) NOT NULL COMMENT '微信服务号appId',
  `mini_app_id` varchar(32) NOT NULL COMMENT '微信小程序appId',
  `mini_app_pay_id` varchar(32) NOT NULL COMMENT '微信小程序支付appId',
  `brand_logo` varchar(256) NOT NULL DEFAULT '' COMMENT '品牌logo',
  `balance` decimal(8,2) DEFAULT '0.00' COMMENT '余额',
  `brand_name` varchar(32) NOT NULL DEFAULT '' COMMENT '品牌名称',
  `legal_name` varchar(32) DEFAULT NULL COMMENT '法人',
  `cancel_booking_days` tinyint DEFAULT '1' COMMENT '订场可取消天数，默认1天',
  `before_booking_minutes` tinyint DEFAULT '10' COMMENT '多少分钟前可以订场，默认10分钟',
  `vip_agreement_template` varchar(200) DEFAULT NULL COMMENT ' VIP协议模版',
  `grade_agreement_template` varchar(200) DEFAULT NULL COMMENT '班级合同模版',
  `invoice_contents` varchar(2000) DEFAULT NULL COMMENT '开票内容',
  `cancel_grade_minutes` smallint DEFAULT '60' COMMENT '多少分钟前可以取消班级，默认60分钟',
  `field_book_nums` tinyint NOT NULL DEFAULT '4' COMMENT '单次订场数，默认4',
  `status` tinyint NOT NULL DEFAULT '1' COMMENT '状态 默认1 待审核 0 删除 2 审核成功',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=130 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公司表';

CREATE TABLE IF NOT EXISTS `aioveu_store` (
   `id` int NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
   `name` varchar(32) NOT NULL COMMENT '名称',
   `company_id` int NOT NULL COMMENT '公司Id',
   `category_code` varchar(32) COMMENT '分类编号',
   `logo` varchar(128) comment '店铺logo',
   `topic_logo` varchar(128) comment '主题logo',
   `introduce` varchar(256) not null comment '店铺介绍',
   `tags` VARCHAR(32) COMMENT '标签',
   `recommend_order` tinyint(8) not null default 0 comment '推荐顺序',
   `address` varchar(128) COMMENT '场馆地址',
   `longitude` double DEFAULT NULL COMMENT '经度',
   `latitude` double DEFAULT NULL COMMENT '纬度',
   `tel` varchar(20) COMMENT '店铺电话',
   `balance` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '可提现余额',
   `business_area_id` int DEFAULT NULL COMMENT '商圈Id',
   `region_id` int DEFAULT NULL COMMENT '区域Id',
   `city_id` int DEFAULT NULL COMMENT '城市Id',
   `province_id` int NOT NULL COMMENT '省份Id',
   `app_id` varchar(32) comment '微信小程序appId',
   `path` varchar(32) comment '店铺路径',
   `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
   `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='店铺表';

CREATE TABLE IF NOT EXISTS `aioveu_agreement` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
    `company_id` int NOT NULL COMMENT '公司Id',
    `store_id` int COMMENT '店铺Id',
    `name` varchar(32) NOT NULL COMMENT '名称',
    `url` varchar(128) NOT NULL COMMENT '合同地址',
    `agreement_type` tinyint(4) not null default 0 comment '合同类型 0 公司合同',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='合同表';


CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_category`(
     `id` int NOT NULL AUTO_INCREMENT COMMENT '类别Id',
     `parent_id` int COMMENT '父类别Id',
     `name` VARCHAR(32) not null COMMENT '名称',
     `code` varchar(32) not null comment '编码',
     `cover` varchar(128) comment '背景图',
     `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '分类表';

# 位置体系
drop table if exists `aioveu`. `aioveu_province`;
CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_province` (
      `id` int NOT NULL AUTO_INCREMENT COMMENT '省份Id',
      `name` varchar(32) NOT NULL COMMENT '省份名称',
      `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
      `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='省份表';

drop table if exists `aioveu`. `aioveu_city`;
CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_city` (
      `id` int NOT NULL AUTO_INCREMENT COMMENT '城市Id',
      `name` varchar(32) NOT NULL COMMENT '城市名称',
      `province_id` int NOT NULL COMMENT '省份Id',
      `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
      `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`),
      UNIQUE KEY `name` (`name`,`province_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='城市表';

drop table if exists `aioveu`. `aioveu_region`;
CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_region` (
        `id` int NOT NULL AUTO_INCREMENT COMMENT '区Id',
        `name` varchar(32) NOT NULL COMMENT '区名称',
        `city_id` int NOT NULL COMMENT '城市Id',
        `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
        `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
        `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
        `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
        PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='区域表';

drop table if exists `aioveu`. `aioveu_business_area`;
CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_business_area` (
       `id` int NOT NULL AUTO_INCREMENT COMMENT '商圈Id',
       `name` varchar(32) NOT NULL COMMENT '商圈名称',
       `region_id` int NOT NULL COMMENT '区域Id',
       `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
       `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
       `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
       `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
       PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='商圈表';

CREATE TABLE `aioveu_user_view_position` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(32) COMMENT '用户id',
    `category_id` int comment '分类id',
    `longitude` DOUBLE NOT NULL COMMENT '经度',
    `latitude` DOUBLE NOT NULL COMMENT '纬度',
    `province` VARCHAR(32) COMMENT '省名',
    `city` VARCHAR(32) COMMENT '城市名',
    `district` VARCHAR(32) COMMENT '区县名',
    `town` VARCHAR(32) COMMENT '乡镇名',
    `street` VARCHAR(32) COMMENT '街道名（行政区划中的街道层级）',
    `address` VARCHAR(128) NOT NULL COMMENT '地址',
    `business` VARCHAR(64) COMMENT '用户所在的商圈',
    `adcode` VARCHAR(16) COMMENT '行政区划代码',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户浏览位置表';

drop table if exists `aioveu_exercise`;
CREATE TABLE IF NOT EXISTS `aioveu_exercise` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `category_id` int NOT NULL COMMENT '项目类型id',
  `store_product_category_id` int NOT NULL COMMENT '店铺产品分类id',
  `name` VARCHAR(32) NOT NULL COMMENT '名称',
  `image` VARCHAR(128) COMMENT '图片',
  `video` VARCHAR(128) COMMENT '视频地址',
  `original_price` DECIMAL(8, 2) not null COMMENT '原价',
  `price` DECIMAL(8, 2) not null COMMENT '价格',
  `vip_price` DECIMAL(8, 2) COMMENT '会员价格',
  `description` VARCHAR(512) COMMENT '介绍',
  `process` VARCHAR(512) COMMENT '流程',
  `suitable_people` VARCHAR(32) COMMENT '适合人群',
  `requirement` VARCHAR(256) COMMENT '要求',
  `tags` VARCHAR(32) COMMENT '标签',
  `qa` VARCHAR(512) COMMENT 'qa',
  `careful` VARCHAR(512) COMMENT '注意事项',
  `store_id` INT NOT NULL COMMENT '场馆id',
  `start_time` DATETIME NOT NULL COMMENT '活动开始时间',
  `end_time` DATETIME NOT NULL COMMENT '活动结束时间',
  `exercise_start_time` DATETIME COMMENT '活动实际开始时间',
  `exercise_end_time` DATETIME COMMENT '活动实际结束时间',
  `service_open_id` varchar(32) comment '服务公众号open_id',
  `remark` VARCHAR(128) COMMENT '备注',
  `limit_number` INT DEFAULT 0 NOT NULL COMMENT '限制人数',
  `enroll_number` INT DEFAULT 0 NOT NULL COMMENT '报名人数',
  `single_number` INT DEFAULT 0 NOT NULL COMMENT '单个用户限制人数',
  `coupon_receive_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '优惠券领取类型 默认0 1支付成功领取 2核销领取',
  `need_address` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '是否需要地址 默认0不需要 1需要',
  `need_location` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '是否需要定位 默认0不需要 1需要',
  `hot` tinyint(2) not null default 0 comment '是否热门 默认0 不热门 1热门',
  `send_balance` DECIMAL(8, 2) COMMENT '赠送余额',
  `agreement_template` VARCHAR(128) COMMENT '合同模板地址',
  `top` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '是否置顶 默认0不置顶 1置顶',
  `status` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '状态',
  `create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = INNODB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '活动表';

CREATE TABLE `aioveu_exercise_image` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `exercise_id` INT NOT NULL COMMENT '活动id',
    `width` INT(2) COMMENT '图片宽',
    `height` INT(2) COMMENT '图片高',
    `url` VARCHAR(128) COMMENT '图片地址',
    `image_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '图片类型 默认0 活动图片 1活动详情介绍图',
    `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '活动图片表';

CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_agreement` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
    `company_id` int NOT NULL COMMENT '公司id',
    `store_id` int NOT NULL COMMENT '店铺id',
    `name` varchar(32) NOT NULL COMMENT '名称',
    `product_id` VARCHAR(32) COMMENT '产品Id',
    `url` varchar(256) not NULL COMMENT '合同地址',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=74 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='合同表';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_coupon_template` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
   `available` boolean NOT NULL DEFAULT false COMMENT '是否是可用状态; true: 可用, false: 不可用',
   `expired` boolean NOT NULL DEFAULT false COMMENT '是否过期; true: 是, false: 否',
   `name` varchar(32) NOT NULL DEFAULT '' COMMENT '优惠券名称',
   `logo` varchar(256) NOT NULL DEFAULT '' COMMENT '优惠券logo',
   `intro` varchar(256) NOT NULL DEFAULT '' COMMENT '优惠券描述',
   `active_price` DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '激活价格',
   `company_id` int NOT NULL default 0 COMMENT '公司id',
   `store_id` int NOT NULL default 0 COMMENT '店铺id',
   `category` varchar(32) NOT NULL DEFAULT '' COMMENT '优惠券分类',
   `product_line` int(11) NOT NULL DEFAULT '0' COMMENT '产品线',
   `coupon_count` int(11) NOT NULL DEFAULT '0' COMMENT '总数',
   `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '创建用户',
   `template_key` varchar(128) NOT NULL DEFAULT '' COMMENT '优惠券模板的编码',
   `target` int(11) NOT NULL DEFAULT '0' COMMENT '目标用户',
   `rule` varchar(1024) NOT NULL DEFAULT '' COMMENT '优惠券规则: TemplateRule 的 json 表示',
   `status` tinyint(4) NOT NULL DEFAULT 2 COMMENT '状态 默认2 1正常 0删除 2待审核 3禁用',
   `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`),
   KEY `idx_category` (`category`),
   KEY `idx_user_id` (`user_id`),
   UNIQUE KEY `name` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='优惠券模板表';

drop table if exists `aioveu`.`aioveu_user_coupon`;
CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_user_coupon` (
      `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
      `template_id` int(11) NOT NULL DEFAULT '0' COMMENT '关联优惠券模板的主键',
      `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '领取用户',
      `coupon_code` varchar(64) NOT NULL DEFAULT '' COMMENT '优惠券码',
      `order_id` VARCHAR(32) COMMENT '订单id',
      `active_price` DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '激活价格',
      `amount` DECIMAL(8,2) NOT NULL DEFAULT 0.00 COMMENT '优惠券金额',
      `rule` varchar(1024) NOT NULL DEFAULT '' COMMENT '优惠券规则: TemplateRule 的 json 表示',
      `status` tinyint(4) NOT NULL DEFAULT 2 COMMENT '状态 默认1 正常 0 删除 2待激活',
      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`),
      KEY `idx_template_id` (`template_id`),
      KEY `idx_user_id` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户优惠券';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_form_enroll` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     `name` varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
     `description` varchar(128) NOT NULL DEFAULT '' COMMENT '描述',
     `service_open_id` varchar(32) comment '服务公众号open_id',
     `start_time` DATETIME NOT NULL COMMENT '开始时间',
     `end_time` DATETIME NOT NULL COMMENT '结束时间',
     `success_msg` varchar(128) NOT NULL DEFAULT '' COMMENT '报名成功提示语',
     `limit_number` INT DEFAULT 0 NOT NULL COMMENT '限制人数',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='表单报名';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_enroll_question` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     `form_enroll_id` int(11) NOT NULL COMMENT '报名表单id',
     `name` varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
     `question_type` tinyint(4) NOT NULL DEFAULT 0 COMMENT '题目类型 默认0 输入 1单选 2多选',
     `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报名问题';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_question_select` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     `enroll_question_id` int(11) NOT NULL COMMENT '问题id',
     `name` varchar(32) NOT NULL DEFAULT '' COMMENT '名称',
     `code` varchar(32) NOT NULL DEFAULT '' COMMENT '编号',
     `limit_number` INT DEFAULT 0 NOT NULL COMMENT '限制人数',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='问题选项';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_question_answer` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     `form_enroll_id` int(11) NOT NULL COMMENT '报名表单id',
     `enroll_question_id` int(11) NOT NULL COMMENT '问题id',
     `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '用户id',
     `select_ids` varchar(128) COMMENT '选项ids',
     `answer` varchar(128) NOT NULL COMMENT '答案',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='问题答案';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_order`(
    `id` VARCHAR(32) NOT NULL ,
    `name` VARCHAR(256) COMMENT '订单名称',
    `category_id` int not null comment '服务项目类型id',
    `amount` DECIMAL(8, 2) NOT NULL COMMENT '订单总金额',
    `coupon_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '订单优惠金额',
    `consume_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '消费金额',
    `actual_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '实际消费金额',
    `refund_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '实际退款金额',
    `can_refund_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '可退款金额',
    `pay_type` VARCHAR(8) COMMENT '支付方式 wx微信 aliPay支付宝',
    `prepay_id` VARCHAR(64) COMMENT '微信支付预支付回话标识',
    `nonce_str` VARCHAR(64) COMMENT '微信支付随机字符串',
    `app_id` VARCHAR(64) COMMENT '微信appId',
    `user_id` VARCHAR(32) NOT NULL COMMENT '用户Id',
    `address_id` int comment '地址id',
    `product_id` VARCHAR(32) COMMENT '产品Id',
    `start_time` DATETIME COMMENT '开始时间',
    `end_time` DATETIME COMMENT '结束时间',
    `address` VARCHAR(128) COMMENT '地址',
    `company_id` int not null comment '公司id',
    `store_id` int not null comment '店铺id',
    `pay_finish_time` DATETIME COMMENT '订单支付完成时间',
    `active_time` DATETIME COMMENT '订单激活时间',
    `user_coupon_id` int comment '用户优惠券',
    `share_user_id` VARCHAR(32) COMMENT '分享人Id',
    `sale_user_id` VARCHAR(32) COMMENT '销售Id',
    `agreement_url` VARCHAR(128) COMMENT '合同地址',
    `count` INT NOT NULL DEFAULT 0 COMMENT '数量',
    `consume_code` varchar(16) DEFAULT NULL COMMENT '消费码',
    `user_vip_card_id` INT NULL DEFAULT NULL COMMENT '用户会员卡ID',
    `remark` VARCHAR(128) COMMENT '备注',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '订单表';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_order_detail`(
      `id` VARCHAR(32) NOT NULL ,
      `order_id` VARCHAR(32) NOT NULL COMMENT '订单id',
      `category_id` int not null comment '服务项目类型id',
      `product_id` VARCHAR(32) NOT NULL COMMENT '商品id, 对应活动或者培训课程',
      `product_name` VARCHAR(32) NOT NULL COMMENT '商品名称',
      `product_price` DECIMAL(8, 2) COMMENT '商品价格',
      `amount` DECIMAL(8, 2) NOT NULL COMMENT '总金额',
      `coupon_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '优惠金额',
      `consume_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '消费金额',
      `actual_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '实际消费金额',
      `refund_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '实际退款金额',
      `can_refund_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '可退款金额',
      `pay_type` VARCHAR(8) COMMENT '支付方式 wx微信 aliPay支付宝',
      `product_quantity` INT NOT NULL DEFAULT 1 COMMENT '商品数量',
      `product_image` VARCHAR(512) COMMENT '商品图片',
      `start_time` DATETIME COMMENT '开始时间',
      `end_time` DATETIME COMMENT '结束时间',
      `active_time` DATETIME COMMENT '激活时间',
      `user_id` VARCHAR(32) NOT NULL COMMENT '用户Id',
      `phone` VARCHAR(16) NOT NULL COMMENT '手机号码',
      `consume_code` varchar(16) DEFAULT NULL COMMENT '消费码',
      `store_id` int not null comment '店铺id',
      `company_id` int not null comment '公司id',
      `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '订单详情表';

CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_order_fail` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `order_id` VARCHAR(32) NOT NULL COMMENT '订单id',
    `transaction_id` VARCHAR(32) COMMENT '微信支付订单号',
    `error_code` VARCHAR(32) COMMENT '错误码',
    `error_msg` VARCHAR(128) COMMENT '错误信息',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '订单支付失败表';

drop table if exists `aioveu`.`aioveu_company_store_user`;
CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_company_store_user` (
   `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
   `user_id` varchar(32) NOT NULL DEFAULT '' COMMENT '用户id',
   `company_id` int not null comment '公司id',
   `store_id` int not null comment '店铺id',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  COMMENT '公司店铺用户表';

CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_exercise_coupon` (
    `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `exercise_id` int not null comment '活动id',
    `coupon_template_id` int not null comment '优惠券模板id',
    `category_id` int not null comment '项目类型id',
    `coupon_number` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '优惠券数量',
    `coupon_expire_time` DATETIME COMMENT '优惠券过期时间',
    `coupon_expire_day` TINYINT(4) COMMENT '优惠券过期天数',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci  COMMENT '活动优惠券表';

DROP TABLE IF EXISTS `aioveu_user_info`;
create table `aioveu_user_info` (
   `id` int NOT NULL AUTO_INCREMENT,
   `name` VARCHAR(32) COMMENT '姓名',
   `company_id` int not null COMMENT '公司id',
   `phone` VARCHAR(12) NOT NULL COMMENT '电话',
   `channel_category` varchar(32) not null comment '渠道分类',
   `channel_name` varchar(32) not null comment '渠道名称',
   `child_name` varchar(32) not null comment '孩子名称',
   `child_gender` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '孩子性别 默认0 未知 1 男性 2 女性',
   `child_age` tinyint(6) comment '孩子年龄',
   `child_interest_category` varchar(32) comment '孩子兴趣品类',
   `child_aioveu_base_remark` varchar(32) comment '孩子运动基础备注',
   `age` tinyint(6) comment '年龄',
   `province_id` int COMMENT '省份id',
   `province_name` VARCHAR(32) COMMENT '省份名称',
   `city_id` int COMMENT '城市id',
   `city_name` VARCHAR(32) COMMENT '城市名称',
   `region_id` int COMMENT '区id',
   `region_name` VARCHAR(32) COMMENT '区名称',
   `business_area_id` int COMMENT '商圈id',
   `business_area_name` VARCHAR(32) COMMENT '商圈名称',
   `address` VARCHAR(128) COMMENT '地址',
   `last_call` DATETIME COMMENT '最后联系时间',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除 2已领取',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`),
   unique key (`phone`, `company_id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户信息表';

DROP TABLE IF EXISTS `aioveu_water_pool_sale_group`;
create table `aioveu_water_pool_sale_group` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) COMMENT '名称',
    `company_id` int not null comment '公司id',
    `store_id` int comment '店铺id',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '公海销售组表';

DROP TABLE IF EXISTS `aioveu_water_pool_sale_group_user`;
create table `aioveu_water_pool_sale_group_user` (
    `id` int NOT NULL AUTO_INCREMENT,
    `sale_group_id` int not null comment '组id',
    `user_id` VARCHAR(32) COMMENT '销售用户id',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '公海销售组用户关联表';


DROP TABLE IF EXISTS `aioveu_user_info_public`;
create table `aioveu_user_info_public` (
   `id` int NOT NULL AUTO_INCREMENT,
   `name` VARCHAR(32) COMMENT '姓名',
   `phone` VARCHAR(12) NOT NULL COMMENT '电话',
   `channel_category` varchar(32) not null comment '渠道分类',
   `channel_name` varchar(32) not null comment '渠道名称',
   `child_name` varchar(32) not null comment '孩子名称',
   `child_gender` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '孩子性别 默认0 未知 1 男性 2 女性',
   `child_age` tinyint(6) comment '孩子年龄',
   `child_interest_category` varchar(32) comment '孩子兴趣品类',
   `child_aioveu_base_remark` varchar(32) comment '孩子运动基础备注',
   `age` tinyint(6) comment '年龄',
   `province_id` int COMMENT '省份id',
   `province_name` VARCHAR(32) COMMENT '省份名称',
   `city_id` int COMMENT '城市id',
   `city_name` VARCHAR(32) COMMENT '城市名称',
   `region_id` int COMMENT '区id',
   `region_name` VARCHAR(32) COMMENT '区名称',
   `business_area_id` int COMMENT '商圈id',
   `business_area_name` VARCHAR(32) COMMENT '商圈名称',
   `address` VARCHAR(128) COMMENT '地址',
   `last_call` DATETIME COMMENT '最后联系时间',
   `company_id` int COMMENT '公司id',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除 2已领取',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`),
   unique key (`phone`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户信息公共表';

DROP TABLE IF EXISTS `aioveu_business_user_info`;
create table `aioveu_business_user_info` (
      `id` int NOT NULL AUTO_INCREMENT,
      `store_id` int not null comment '店铺id',
      `order_detail_id` VARCHAR(32) NOT NULL COMMENT '订单详情id',
      `exercise_id` int not null comment '活动id',
      `user_info_public_id` int not null comment '公共用户信息id',
      `phone` VARCHAR(12) NOT NULL COMMENT '电话',
      `source` VARCHAR(32) NOT NULL COMMENT '来源',
      `address` VARCHAR(128) COMMENT '地址',
      `push_user_id` VARCHAR(32) COMMENT '地推用户id',
      `new_user` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 新课 0老客',
      `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 待领取 0 删除 2已领取 8 电话失效待审核 10 审核通过(已退款)',
      `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
      `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
      PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '商户用户信息关联表';

DROP TABLE IF EXISTS `aioveu_user_info_public`;
create table `aioveu_user_info_public` (
   `id` int NOT NULL AUTO_INCREMENT,
   `name` VARCHAR(32) COMMENT '姓名',
   `phone` VARCHAR(12) NOT NULL COMMENT '电话',
   `channel_category` varchar(32) not null comment '渠道分类',
   `channel_name` varchar(32) not null comment '渠道名称',
   `child_name` varchar(32) not null comment '孩子名称',
   `child_gender` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '孩子性别 默认0 未知 1 男性 2 女性',
   `child_age` tinyint(6) comment '孩子年龄',
   `child_interest_category` varchar(32) comment '孩子兴趣品类',
   `child_aioveu_base_remark` varchar(32) comment '孩子运动基础备注',
   `age` tinyint(6) comment '年龄',
   `province_id` int COMMENT '省份id',
   `province_name` VARCHAR(32) COMMENT '省份名称',
   `city_id` int COMMENT '城市id',
   `city_name` VARCHAR(32) COMMENT '城市名称',
   `region_id` int COMMENT '区id',
   `region_name` VARCHAR(32) COMMENT '区名称',
   `business_area_id` int COMMENT '商圈id',
   `business_area_name` VARCHAR(32) COMMENT '商圈名称',
   `address` VARCHAR(128) COMMENT '地址',
   `last_call` DATETIME COMMENT '最后联系时间',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除 2已领取',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户信息公共表';

DROP TABLE IF EXISTS `aioveu_business_user_info`;
create table `aioveu_business_user_info` (
      `id` int NOT NULL AUTO_INCREMENT,
      `store_id` int not null comment '店铺id',
      `order_id` VARCHAR(32) NOT NULL COMMENT '订单id',
      `user_info_public_id` int not null comment '公共用户信息id',
      `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 待领取 0 删除 2已领取',
      `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
      `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
      PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '商户用户信息关联表';

DROP TABLE IF EXISTS `aioveu_user_call`;
create table `aioveu_user_call` (
   `id` int NOT NULL AUTO_INCREMENT,
   `user_info_id` int not null COMMENT '用户信息id',
   `user_id` VARCHAR(32) COMMENT '联系人id',
   `call_status` tinyint(2) not null default 0 comment '拨打状态 默认0 未拨打 1拨打 2微信',
   `intention` tinyint(4) default 1 comment '跟进状态 默认1 新客户未联系 2 未接通 3无明确意向 4有意向',
   `introduce` varchar(512) comment '跟进说明',
   `next_contact_time` DATETIME COMMENT '下次联系时间',
   `price_sensitive` tinyint(2) comment '价格是否敏感 0不敏感 1敏感',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 新客户未联系 0 删除 ',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户拨打记录表';

DROP TABLE IF EXISTS `aioveu_user_receive_call`;
create table `aioveu_user_receive_call` (
   `id` int NOT NULL AUTO_INCREMENT,
   `user_info_id` int not null COMMENT '用户信息id',
   `user_id` VARCHAR(32) COMMENT '联系人id',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 新客户未联系 0 删除 ',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户信息领取表';

DROP TABLE IF EXISTS `aioveu_user_info_tag`;
create table `aioveu_user_info_tag` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) COMMENT '名称',
    `user_info_id` int not null COMMENT '用户信息id',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户标签表';

DROP TABLE IF EXISTS `aioveu_user_call_pool`;
create table `aioveu_user_call_pool` (
   `id` int NOT NULL AUTO_INCREMENT,
   `user_id` VARCHAR(32) COMMENT '用户id',
   `pool_size` int not null default 200 comment '电话池大小',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '用户拨打池表';

CREATE TABLE `aioveu_user_view_position` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(32) COMMENT '用户id',
    `category_id` int comment '分类id',
    `product_id` VARCHAR(32) COMMENT '产品id',
    `longitude` DOUBLE NOT NULL COMMENT '经度',
    `latitude` DOUBLE NOT NULL COMMENT '纬度',
    `province` VARCHAR(32) COMMENT '省名',
    `city` VARCHAR(32) COMMENT '城市名',
    `district` VARCHAR(32) COMMENT '区县名',
    `town` VARCHAR(32) COMMENT '乡镇名',
    `street` VARCHAR(32) COMMENT '街道名（行政区划中的街道层级）',
    `address` VARCHAR(128) COMMENT '地址',
    `business` VARCHAR(64) COMMENT '用户所在的商圈',
    `adcode` VARCHAR(16) COMMENT '行政区划代码',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '用户浏览位置表';

CREATE TABLE `aioveu_coupon_verify` (
    `id` int NOT NULL AUTO_INCREMENT,
    `user_id` VARCHAR(32) COMMENT '核销人id',
    `user_coupon_id` int comment '用户优惠券id',
    `store_id` int COMMENT '店铺Id',
    `remark` VARCHAR(128) COMMENT '备注',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '优惠券核销表';

drop table if exists `aioveu_group_luck`;
CREATE TABLE `aioveu_group_luck` (
   `id` int NOT NULL AUTO_INCREMENT,
   `name` VARCHAR(32) not null COMMENT '名称',
   `group_date` DATETIME not null COMMENT '组日期',
   `uuid` VARCHAR(32) COMMENT '唯一id',
   `username` VARCHAR(32) COMMENT '姓名',
   `group_code` VARCHAR(32) not null COMMENT '编号',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '组抽奖表(临时表)';

drop table if exists `aioveu_share_config`;
CREATE TABLE `aioveu_share_config` (
    `id` int NOT NULL AUTO_INCREMENT,
    `category_id` int comment '分类id',
    `company_id` int comment '公司id',
    `store_id` int comment '店铺id',
    `product_id` VARCHAR(32) COMMENT '产品id',
    `reward_condition` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '应用条件 默认1 应用 0 不应用',
    `reward_ratio` double COMMENT '奖励比例 根据商品价格计算',
    `reward_amount` DECIMAL(8, 2) NOT NULL DEFAULT 0.00 COMMENT '奖励金额',
    `share_type` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '分享类型 默认0 商品 0 海报',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '分享配置表';

CREATE TABLE `aioveu_user_balance_change` (
     `id` int NOT NULL AUTO_INCREMENT,
     `user_id` varchar(32) not null COMMENT '用户id',
     `name` varchar(32) NOT NULL COMMENT '变动说明',
     `description` varchar(128) COMMENT '描述',
     `order_id` varchar(32) COMMENT '订单id',
     `account_type` tinyint NOT NULL default 0 COMMENT '账户类型 默认0 普通账户 1推广账户',
     `change_type` tinyint NOT NULL default 0 COMMENT '变动类型 0 收入 1提现',
     `amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
     `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     `user_vip_card_id` INT NULL DEFAULT NULL COMMENT '用户会员卡ID',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户余额变动表';

drop table if exists `aioveu_poster`;
CREATE TABLE `aioveu_poster` (
      `id` int NOT NULL AUTO_INCREMENT,
      `name` varchar(32) NOT NULL COMMENT '名称',
      `category_id` int comment '分类id',
      `store_id` int comment '店铺id',
      `product_id` VARCHAR(32) COMMENT '产品id',
      `reward_category_id` int comment '奖励分类id',
      `reward_product_id` VARCHAR(32) COMMENT '奖励产品id',
      `reward_company_id` int comment '奖励公司id',
      `url` VARCHAR(128) not null COMMENT '海报地址',
      `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
      `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
      `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
      PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '海报表';


drop table if exists `aioveu_user_address`;
CREATE TABLE `aioveu_user_address` (
    `id` int NOT NULL AUTO_INCREMENT,
    `province_id` int not null COMMENT '省份id',
    `city_id` int not null COMMENT '城市id',
    `region_id` int not null COMMENT '区id',
    `address` varchar(32) NOT NULL COMMENT '详细地址',
    `user_id` VARCHAR(32) not null COMMENT '用户id',
    `username` varchar(32) not null comment '用户名',
    `phone` varchar(11) not null comment '手机号码',
    `default_address` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '默认1 0非默认',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '用户地址表';

CREATE TABLE `aioveu_store_image` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `store_id` INT NOT NULL COMMENT '店铺id',
    `width` INT(2) COMMENT '图片宽',
    `height` INT(2) COMMENT '图片高',
    `url` VARCHAR(128) COMMENT '图片地址',
    `image_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '图片类型 默认0 店铺图片',
    `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '店铺图片表';

CREATE TABLE `aioveu_store_coach` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL COMMENT '名称',
     `store_id` INT NOT NULL COMMENT '店铺id',
     `user_type` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '用户类型 默认1 教练 4 销售',
     `expertise` varchar(32) DEFAULT NULL COMMENT '专长',
     `introduce` varchar(256) not null comment '介绍',
     `url` VARCHAR(128) not null COMMENT '头像地址',
     `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '店铺教练表';

CREATE TABLE `aioveu_user_open_id` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `user_id` VARCHAR(32) not null COMMENT '用户id',
     `open_id` varchar(32) comment '微信openId',
     `union_id` varchar(32) comment '微信unionId',
     `app_id` varchar(32) comment '微信小程序appId',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`),
     unique (`open_id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '用户openId表';

CREATE TABLE `aioveu_mini_app_store` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `store_id` INT NOT NULL COMMENT '店铺id',
      `app_id` varchar(32) comment '微信小程序appId',
      `default_store` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '默认店铺 默认0非默认 1默认',
      `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
      `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
      `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
      PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '小程序店铺表';

DROP TABLE IF EXISTS `aioveu_order_refund`;
CREATE TABLE `aioveu_order_refund` (
  `id` VARCHAR(32) NOT NULL ,
  `order_id` VARCHAR(32) NOT NULL COMMENT '订单id',
  `order_detail_id` VARCHAR(32) COMMENT '订单详情id',
  `refund_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '退款金额',
  `refund_finish` DATETIME COMMENT '退款成功时间',
  `status` TINYINT(3) NOT NULL DEFAULT 0 COMMENT '默认0 成功 1失败',
  `error_code` VARCHAR(32) COMMENT '错误码',
  `error_msg` VARCHAR(128) COMMENT '错误信息',
  `remark` VARCHAR(32) COMMENT '备注',
  `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '订单退款表';

CREATE TABLE `aioveu_coach_tag` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL COMMENT '名称',
    `coach_id` INT NOT NULL COMMENT '教练id',
    `icon` VARCHAR(128) COMMENT '标签地址',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '教练标签表';

CREATE TABLE `aioveu_grade_template` (
  `id` VARCHAR(32) NOT NULL,
  `name` varchar(32) NOT NULL COMMENT '名称',
  `store_id` INT NOT NULL COMMENT '店铺id',
  `limit_number` INT DEFAULT 0 NOT NULL COMMENT '限制人数',
  `exceed` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '是否超额 默认不可',
  `min_number` INT DEFAULT 0 NOT NULL COMMENT '最少人数',
  `class_hour` INT DEFAULT 1 COMMENT '课时',
  `grade_classroom_id` INT NOT NULL COMMENT '课程教室id',
  `grade_level_id` INT NOT NULL COMMENT '课程等级id',
  `grade_age_id` INT NOT NULL COMMENT '课程年龄段id',
  `start_day` DATE NOT NULL COMMENT '开始日期',
  `end_day` DATE NOT NULL COMMENT '结束日期',
  `start_time` TIME NOT NULL COMMENT '开始时间',
  `end_time` TIME NOT NULL COMMENT '结束时间',
  `remark` VARCHAR(32) COMMENT '备注',
  `time_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '时间类型 默认0 自由时间 1每星期 2每月 3每天',
  `date_list` VARCHAR(512) COMMENT '选取时间 自由时间 每星期和每月有值',
  `skip_holiday` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '是否跳过节假日 默认0不跳过 1跳过',
  `coupon_template_ids` varchar(128) not null comment '优惠券模板ids 逗号分割',
  `coach_ids` varchar(128) not null comment '教练ids 逗号分割',
  `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态 1正常 2下架',
  `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '班级模板表';

CREATE TABLE `aioveu_grade` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `name` varchar(32) NOT NULL COMMENT '名称',
   `grade_template_id` VARCHAR(32) NOT NULL COMMENT '班级模板id',
   `store_id` INT NOT NULL COMMENT '店铺id',
   `start_time` DATETIME NOT NULL COMMENT '开始时间',
   `end_time` DATETIME NOT NULL COMMENT '结束时间',
   `grade_classroom_id` INT NOT NULL COMMENT '课程教室id',
   `limit_number` INT DEFAULT 0 NOT NULL COMMENT '限制人数',
   `min_number` INT DEFAULT 0 NOT NULL COMMENT '最少人数',
   `exceed` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '是否超额 默认不可',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `remark` VARCHAR(32) COMMENT '备注',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '班级表';

CREATE TABLE `aioveu_grade_coupon_template` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `grade_id` INT NOT NULL COMMENT '班级id',
  `coupon_template_id` INT NOT NULL COMMENT '优惠券模板id',
  `class_hour` INT DEFAULT 1 NOT NULL COMMENT '课时',
  `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
  `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '班级优惠券模板表';

CREATE TABLE `aioveu_grade_coach` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `grade_id` INT NOT NULL COMMENT '班级id',
   `coach_id` INT NOT NULL COMMENT '教练id',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '班级教练表';

drop table if exists `aioveu_grade_fixed_user`;
CREATE TABLE `aioveu_grade_fixed_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `grade_template_id` VARCHAR(32) NOT NULL COMMENT '班级模板id',
  `user_id` VARCHAR(32) not null COMMENT '用户id',
  `username` VARCHAR(32) not null COMMENT '客户名称',
  `child_name` VARCHAR(32) not null COMMENT '孩子名称',
  `child_age` tinyint(8) not null COMMENT '孩子年龄',
  `phone` VARCHAR(32) not null COMMENT '联系方式',
  `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
  `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '课程固定用户表';

drop table if exists `aioveu_grade_enroll_user`;
CREATE TABLE `aioveu_grade_enroll_user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `grade_id` INT NOT NULL COMMENT '班级id',
  `user_id` VARCHAR(32) not null COMMENT '用户id',
  `username` VARCHAR(32) not null COMMENT '客户名称',
  `child_name` VARCHAR(32) not null COMMENT '孩子名称',
  `child_age` tinyint(8) not null COMMENT '孩子年龄',
  `phone` VARCHAR(32) not null COMMENT '联系方式',
  `appointment_type` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '约课类型 默认 0约课 1体验课',
  `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态1 已约课 0取消 2已签到 3已完成',
  `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '班级报名用户表';

drop table if exists `aioveu_grade_sign_evaluate`;
CREATE TABLE `aioveu_grade_sign_evaluate` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `grade_enroll_user_id` INT NOT NULL COMMENT '班级用户报名id',
   `grade_id` INT NOT NULL COMMENT '班级id',
   `user_id` VARCHAR(32) not null COMMENT '用户id',
   `sign_user_id` VARCHAR(32) COMMENT '签到用户id',
   `evaluate_user_id` VARCHAR(32) COMMENT '课评用户id',
   `evaluate` varchar(512) comment '课评',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '班级用户签到课评表';

drop table if exists `aioveu_grade_file`;
CREATE TABLE `aioveu_grade_file` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `name` varchar(32) NOT NULL COMMENT '课件名称',
   `grade_id` INT NOT NULL COMMENT '班级id',
   `file_path` varchar(512) not null comment '课件路径',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '课程课件表';

drop table if exists `aioveu_grade_user_coupon`;
CREATE TABLE `aioveu_grade_user_coupon` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `user_id` VARCHAR(32) not null COMMENT '用户id',
   `grade_id` INT NOT NULL COMMENT '班级id',
   `user_coupon_id` INT NOT NULL COMMENT '用户优惠券id',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '约课用户优惠券表';

CREATE TABLE `aioveu_grade_level` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `name` varchar(32) NOT NULL COMMENT '名称',
   `store_id` INT NOT NULL COMMENT '店铺id',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '课程等级表';

CREATE TABLE `aioveu_grade_age` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL COMMENT '名称',
     `store_id` INT NOT NULL COMMENT '店铺id',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '课程年龄段表';

CREATE TABLE `aioveu_grade_classroom` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `name` varchar(32) NOT NULL COMMENT '名称',
   `store_id` INT NOT NULL COMMENT '店铺id',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '课程教室表';

CREATE TABLE `aioveu_special_day` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL COMMENT '名称',
     `detail_day` DATETIME NOT NULL COMMENT '日期',
     `special_type` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '特殊类型 默认1 节假日 0调休工作日',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '特殊日期表';

drop table if exists `aioveu_user_coach`;
CREATE TABLE IF NOT EXISTS `aioveu`.`aioveu_user_coach` (
     `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '自增主键',
     `coach_id` int(11) NOT NULL COMMENT '教练id',
     `user_id` varchar(32) NOT NULL COMMENT '用户id',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户教练关系表';

CREATE TABLE `aioveu_recharge_option` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL COMMENT '名称',
     `money` double NOT NULL COMMENT '金额',
     `give` double NOT NULL default 0.00 COMMENT '赠送金额',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '充值选项表';

drop table if exists `aioveu_recharge_order`;
CREATE TABLE IF NOT EXISTS `aioveu_recharge_order`(
  `id` VARCHAR(32) NOT NULL ,
  `name` VARCHAR(256) COMMENT '订单名称',
  `amount` DECIMAL(8, 2) NOT NULL COMMENT '订单金额',
  `actual_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '实际消费金额',
  `give_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '赠送金额',
  `total_amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '总金额',
  `pay_type` VARCHAR(8) COMMENT '支付方式 wx微信 aliPay支付宝',
  `prepay_id` VARCHAR(64) COMMENT '微信支付预支付回话标识',
  `nonce_str` VARCHAR(64) COMMENT '微信支付随机字符串',
  `app_id` VARCHAR(64) COMMENT '微信appId',
  `wx_pay_id` VARCHAR(64) COMMENT '微信支付Id',
  `user_id` VARCHAR(32) NOT NULL COMMENT '用户Id',
  `pay_finish_time` DATETIME COMMENT '订单支付完成时间',
  `remark` VARCHAR(128) COMMENT '备注',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '充值订单表';

drop table if exists `aioveu_grade_cancel_record`;
CREATE TABLE `aioveu_grade_cancel_record` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `grade_id` INT NOT NULL COMMENT '班级id',
   `user_id` VARCHAR(32) not null COMMENT '用户id',
   `explain_reason` VARCHAR(128) not null COMMENT '取消说明',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '班级取消记录表';

drop table if exists `aioveu_role_mini_app_menu`;
CREATE TABLE `aioveu_role_mini_app_menu` (
    `id` int NOT NULL AUTO_INCREMENT COMMENT '自增主键',
    `name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '名称',
    `code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单code',
    `bg_color` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '背景颜色',
    `role_code` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '角色code',
    `icon` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '图标',
    `priority` int NOT NULL DEFAULT '0' COMMENT '优先级',
    `path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL DEFAULT '' COMMENT '路径',
    `status` tinyint NOT NULL DEFAULT '1' COMMENT '默认1 正常 0 删除',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='小程序角色菜单表';

CREATE TABLE `aioveu_user_mp_subscribe` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `open_id` varchar(32) not null comment '公众号openId',
  `to_user` varchar(32) not null comment '微信号',
  `event` varchar(32) not null comment '事件',
  `event_key` varchar(64) comment '事件KEY值，qrscene_为前缀，后面为二维码的参数值',
  `ticket` varchar(128) comment '二维码的ticket，可用来换取二维码图片',
  `msg_type` varchar(32) not null comment '消息类型',
  `union_id` varchar(32) comment 'unionId',
  `subscribe_scene` varchar(32) comment '关注场景',
  `qr_scene` varchar(32) comment '二维码场景',
  `qr_scene_str` varchar(512) comment '二维码内容',
  `app_id` varchar(32) comment '微信公众号appId',
  `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
  `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '用户公众号关注表';

CREATE TABLE IF NOT EXISTS `aioveu_third_category`(
  `id` int NOT NULL AUTO_INCREMENT COMMENT '类别Id',
  `parent_id` int COMMENT '父类别Id',
  `name` VARCHAR(32) not null COMMENT '名称',
  `code` varchar(32) comment '编码',
  `company_id` int NOT NULL COMMENT '公司Id',
  `store_id` int COMMENT '店铺Id',
  `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
  `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
  `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
  `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
  PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '第三方分类表';

CREATE TABLE IF NOT EXISTS `aioveu_store_product_category`(
    `id` int NOT NULL AUTO_INCREMENT COMMENT '类别Id',
    `store_id` int not null COMMENT '店铺Id',
    `category_id` int not null COMMENT '分类Id',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '店铺产品分类表';

drop table if exists `aioveu_topic`;
CREATE TABLE `aioveu_topic` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `name` varchar(32) NOT NULL COMMENT '名称',
   `category_id` INT NOT NULL COMMENT '分类id',
   `cover` varchar(128) comment '封面图',
   `price` DECIMAL(8, 2) not null COMMENT '价格',
   `style_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '样式类型 默认0',
   `introduce` VARCHAR(512) not null COMMENT '介绍',
   `qa` VARCHAR(512) not null COMMENT 'qa',
   `color` varchar(32) not null comment '图标颜色 rgb 逗号分隔',
   `start_time` DATETIME NOT NULL COMMENT '开始时间',
   `end_time` DATETIME NOT NULL COMMENT '结束时间',
   `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
   `reward` DECIMAL(8, 2) default 0 not null COMMENT '奖励金额',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '专题表';

CREATE TABLE IF NOT EXISTS `aioveu_topic_category`(
     `id` int NOT NULL AUTO_INCREMENT COMMENT '类别Id',
     `name` VARCHAR(32) not null COMMENT '名称',
     `topic_id` INT NOT NULL COMMENT '专题id',
     `code` varchar(32) not null comment '编码',
     `icon` varchar(128) comment '图标',
     `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE=utf8mb4_general_ci COMMENT '专题分类表';

CREATE TABLE `aioveu_topic_exercise` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `topic_id` INT NOT NULL COMMENT '专题id',
    `store_id` INT NOT NULL COMMENT '店铺id',
    `exercise_id` INT NOT NULL COMMENT '活动id',
    `category_id` INT COMMENT '专题子分类id',
    `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '专题活动表';

CREATE TABLE `aioveu_topic_image` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `topic_id` INT NOT NULL COMMENT '主题id',
    `width` INT(2) COMMENT '图片宽',
    `height` INT(2) COMMENT '图片高',
    `url` VARCHAR(128) COMMENT '图片地址',
    `priority` int NOT NULL DEFAULT 0 COMMENT '优先级',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '主题图片表';

CREATE TABLE `aioveu_coupon_change_record` (
   `id` int NOT NULL AUTO_INCREMENT,
   `user_id` VARCHAR(32) COMMENT '用户id',
   `user_coupon_id` int comment '用户优惠券id',
   `grade_id` INT COMMENT '班级id',
   `remark` VARCHAR(128) COMMENT '备注',
   `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '优惠券变动记录表';

CREATE TABLE `aioveu_exercise_push_reward_config` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `name` VARCHAR(32) not null COMMENT '名称',
     `exercise_id` INT COMMENT '活动id',
     `topic_id` INT NOT NULL COMMENT '主题id',
     `coupon_template_id` INT COMMENT '优惠券模板id',
     `reward_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '奖励类型 默认0 预约 1到店',
     `reward_product` VARCHAR(128) COMMENT '奖品商品',
     `remark` VARCHAR(128) COMMENT '备注',
     `reward` DECIMAL(8, 2) not null COMMENT '奖励金额',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '活动地推奖励配置表';

drop table if exists `aioveu_extension_position`;
CREATE TABLE `aioveu_extension_position`
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `theme_id`     int not null COMMENT '主题id',
    `extension_id` varchar(32) not null COMMENT '推广者Id',
    `run_step`     int COMMENT '运动步数',
    `longitude`    double not null COMMENT '经度',
    `latitude`     double not null COMMENT '纬度',
    `b_longitude`    double COMMENT '百度经度',
    `b_latitude`     double COMMENT '百度纬度',
    `province` VARCHAR(32) COMMENT '省名',
    `city` VARCHAR(32) COMMENT '城市名',
    `district` VARCHAR(32) COMMENT '区县名',
    `town` VARCHAR(32) COMMENT '乡镇名',
    `street` VARCHAR(32) COMMENT '街道名（行政区划中的街道层级）',
    `address` VARCHAR(128) COMMENT '地址',
    `business` VARCHAR(64) COMMENT '用户所在的商圈',
    `adcode` VARCHAR(16) COMMENT '行政区划代码',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '地推位置表';


CREATE TABLE `aioveu_extension_share`
(
    `id`           int NOT NULL AUTO_INCREMENT,
    `theme_id`     int(0) NULL DEFAULT NULL COMMENT '主题id',
    `share_id`     varchar(32) COMMENT '被分享人id',
    `extension_id` varchar(32) not null COMMENT '推广者id',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '地推主题分享表';


CREATE TABLE `aioveu_user_recommend_record`
(
    `id` int NOT NULL AUTO_INCREMENT,
    `recommend_user_id` varchar(32) not null COMMENT '推荐人用户Id',
    `user_id` varchar(32) not null COMMENT '用户id',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB AUTO_INCREMENT = 10 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '用户推荐记录表';

CREATE TABLE `aioveu_push_system_message` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) not null COMMENT '名称',
    `message` varchar(512) not null COMMENT '内容',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '地推系统消息表';

CREATE TABLE `aioveu_push_system_message_record` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `push_system_message_id` INT NOT NULL COMMENT '地推系统消息id',
    `user_id` varchar(32) not null COMMENT '用户id',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '地推系统消息记录表';

CREATE TABLE `aioveu_exercise_show_record` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `exercise_id` INT NOT NULL COMMENT '活动id',
    `topic_id` INT NOT NULL COMMENT '专题id',
    `user_id` varchar(32) COMMENT '用户id',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '活动展示记录表';

CREATE TABLE `aioveu_push_topic` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `topic_id` INT NOT NULL COMMENT '专题id',
   `user_id` VARCHAR(32) COMMENT '地推人员id',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '地推主题表';

CREATE TABLE `aioveu_user_payment_info` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(32) not null COMMENT '名称',
    `account` VARCHAR(32) not null COMMENT '账户',
    `full_name` VARCHAR(32) not null COMMENT '姓名',
    `user_id` VARCHAR(32) COMMENT '地推人员id',
    `bank` VARCHAR(32) COMMENT '银行',
    `remark` VARCHAR(128) COMMENT '备注',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '地推付款信息表';

drop table if exists `aioveu_cash_order`;
CREATE TABLE IF NOT EXISTS `aioveu_cash_order`(
     `id` VARCHAR(32) NOT NULL ,
     `name` VARCHAR(256) COMMENT '名称',
     `amount` DECIMAL(8, 2) NOT NULL COMMENT '金额',
     `pay_type` VARCHAR(8) COMMENT '提现方式 wx微信 aliPay支付宝 bank银行卡',
     `app_id` VARCHAR(64) COMMENT '微信appId',
     `user_id` VARCHAR(32) NOT NULL COMMENT '用户Id',
     `remark` VARCHAR(128) COMMENT '备注',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 待审核 0 删除 4成功',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '提现订单表';

CREATE TABLE `aioveu_user_extension_account` (
     `id` int NOT NULL AUTO_INCREMENT,
     `user_id` varchar(32) not null COMMENT '用户id',
     `name` varchar(32) NOT NULL COMMENT '名称',
     `balance` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
     `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='用户推广账户表';

CREATE TABLE `aioveu_store_venue` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL COMMENT '名称',
     `store_id` INT NOT NULL COMMENT '店铺id',
     `company_id` INT NOT NULL COMMENT '公司id',
     `logo` varchar(128) comment '场馆logo',
     `category_code` varchar(32) not null COMMENT '分类编号',
     `tags` varchar(32) COMMENT '标签',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '店铺场馆表';

CREATE TABLE `aioveu_venue_field` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL COMMENT '名称',
     `store_id` INT NOT NULL COMMENT '店铺id',
     `venue_id` INT NOT NULL COMMENT '场馆id',
     `start_time` TIME NOT NULL default '08:00' COMMENT '开始时间',
     `end_time` TIME NOT NULL default '18:00' COMMENT '结束时间',
     `price` DECIMAL(8, 2) COMMENT '普通价格',
     `vip_price` DECIMAL(8, 2) COMMENT '会员价格',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '场馆场地表';

CREATE TABLE `aioveu_field_plan` (
     `id` INT NOT NULL AUTO_INCREMENT,
     `field_id` INT NOT NULL COMMENT '场地id',
     `venue_id` INT NOT NULL COMMENT '场馆id',
     `field_day` DATE NOT NULL COMMENT '场地日期',
     `start_time` TIME NOT NULL COMMENT '开始时间',
     `end_time` TIME NOT NULL COMMENT '结束时间',
     `price` DECIMAL(8, 2) COMMENT '普通价格',
     `vip_price` DECIMAL(8, 2) COMMENT '会员价格',
     `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态 默认 1空闲 4占用 6预订',
     `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
     `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
     PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '场地计划表';

CREATE TABLE `aioveu_field_plan_user` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `field_plan_id` INT NOT NULL COMMENT '场地计划id',
    `user_id` varchar(32) not null COMMENT '用户id',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '场地计划用户表';

drop table if exists `aioveu`. `aioveu_field_plan_template`;
CREATE TABLE `aioveu_field_plan_template` (
	`id` VARCHAR(32) NOT NULL COMMENT '主键' COLLATE 'utf8mb4_general_ci',
	`name` VARCHAR(32) NOT NULL COMMENT '名称' COLLATE 'utf8mb4_general_ci',
	`field_ids` VARCHAR(100) NOT NULL DEFAULT '' COMMENT '场地id' COLLATE 'utf8mb4_general_ci',
	`venue_id` INT(10) NOT NULL COMMENT '场馆id',
	`start_day` DATE NOT NULL COMMENT '开始日期',
	`end_day` DATE NOT NULL COMMENT '结束日期',
	`start_time` TIME NOT NULL COMMENT '开始时间',
	`end_time` TIME NOT NULL COMMENT '结束时间',
	`price_rule` TEXT NOT NULL COMMENT '价格规则' COLLATE 'utf8mb4_general_ci',
	`time_type` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '时间类型 默认0 自由时间 1每星期 2每月 3每天',
	`date_list` VARCHAR(128) NULL DEFAULT NULL COMMENT '选取时间 自由时间 每星期和每月有值' COLLATE 'utf8mb4_general_ci',
	`skip_holiday` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '是否跳过节假日 默认0不跳过 1跳过',
	`remark` VARCHAR(250) NULL DEFAULT NULL COLLATE 'utf8mb4_general_ci',
	`status` TINYINT(3) NOT NULL DEFAULT '2' COMMENT '状态',
	`create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='场地计划模板表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
;

CREATE TABLE `aioveu_vip_card` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(32) NOT NULL COMMENT '名称' COLLATE 'utf8mb4_general_ci',
	`price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '会员价格',
	`discount` DOUBLE NULL DEFAULT NULL COMMENT '折扣',
	`company_id` INT(10) NOT NULL COMMENT '公司Id',
	`store_id` INT(10) NULL DEFAULT NULL COMMENT '店铺Id',
	`category_code` VARCHAR(32) NOT NULL COMMENT '会员卡分类编号' COLLATE 'utf8mb4_general_ci',
	`product_category_code` VARCHAR(32) NULL DEFAULT NULL COMMENT '产品分类编号' COLLATE 'utf8mb4_general_ci',
	`fixed_time` DATETIME NULL DEFAULT NULL COMMENT '会员卡固定有效期',
	`receive_day` INT(10) NULL DEFAULT NULL COMMENT '领取有效期天数 从领取之日累加',
	`status` TINYINT(3) NOT NULL DEFAULT '1' COMMENT '状态',
	`create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	`original_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '原价',
	`selling_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '现价',
	`stored_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '储值金额',
	`gift_price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '赠送金额',
	`description` VARCHAR(520) NULL DEFAULT NULL COMMENT '描述' COLLATE 'utf8mb4_general_ci',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='会员卡表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5
;

CREATE TABLE `aioveu_user_vip_card` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`name` VARCHAR(32) NOT NULL COMMENT '名称' COLLATE 'utf8mb4_general_ci',
	`vip_code` VARCHAR(32) NOT NULL COMMENT '卡号' COLLATE 'utf8mb4_general_ci',
	`user_id` VARCHAR(32) NOT NULL COMMENT '用户id' COLLATE 'utf8mb4_general_ci',
	`price` DECIMAL(8,2) NULL DEFAULT NULL COMMENT '会员价格',
	`discount` DOUBLE NULL DEFAULT NULL COMMENT '折扣',
	`vip_card_id` INT(10) NULL DEFAULT NULL COMMENT '会员卡Id',
	`company_id` INT(10) NOT NULL COMMENT '公司Id',
	`store_id` INT(10) NULL DEFAULT NULL COMMENT '店铺Id',
	`category_code` VARCHAR(32) NOT NULL COMMENT '分类编号' COLLATE 'utf8mb4_general_ci',
	`product_category_id` INT(10) NULL DEFAULT NULL,
	`product_category_code` VARCHAR(32) NULL DEFAULT NULL COMMENT '产品分类' COLLATE 'utf8mb4_general_ci',
	`valid_time` DATETIME NULL DEFAULT NULL COMMENT '有效时间',
	`balance` DECIMAL(8,2) NOT NULL DEFAULT '0.00' COMMENT '余额',
	`status` TINYINT(3) NOT NULL DEFAULT '1' COMMENT '状态 默认0 删除 1正常 2未激活 6停用 ',
	`create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE,
	UNIQUE INDEX `vip_code` (`vip_code`) USING BTREE
)
COMMENT='用户会员卡表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=5
;

CREATE TABLE `aioveu_exercise_vip_card` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `exercise_id` INT NOT NULL COMMENT '活动id',
      `vip_card_id` int NOT NULL COMMENT '会员卡Id',
      `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
      `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
      `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
      PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '活动会员卡表';

DROP TABLE if exists `aioveu`. `aioveu_grade_cancel_options`;
CREATE TABLE IF NOT EXISTS `aioveu`. `aioveu_grade_cancel_options` (
   `id` int NOT NULL AUTO_INCREMENT COMMENT 'Id主键',
   `name` VARCHAR(200) NOT NULL COMMENT '取消选项',
    `coupon_extend_days` tinyint(4) NOT NULL DEFAULT 0 COMMENT '优惠券延长天数',
    `company_id` INT(10) NULL DEFAULT NULL COMMENT '公司id',
    `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 在用 0禁用',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='课程取消选项表';

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

drop table if exists `aioveu_field_book_user`;
CREATE TABLE `aioveu_field_book_user` (
	`id` INT(10) NOT NULL AUTO_INCREMENT,
	`field_plan_id` INT(10) NOT NULL COMMENT '场地计划id',
	`order_id` VARCHAR(32) NOT NULL COMMENT '订单id' COLLATE 'utf8mb4_general_ci',
	`user_id` VARCHAR(32) NOT NULL COMMENT '用户id' COLLATE 'utf8mb4_general_ci',
	`username` VARCHAR(32) NOT NULL COMMENT '客户名称' COLLATE 'utf8mb4_general_ci',
	`gender` TINYINT(3) NOT NULL DEFAULT '0' COMMENT '孩子性别 默认0 未知 1 男性 2 女性',
	`phone` VARCHAR(32) NOT NULL COMMENT '联系方式' COLLATE 'utf8mb4_general_ci',
	`status` TINYINT(3) NOT NULL DEFAULT '1' COMMENT '状态1',
	`create_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`update_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='场地预订用户表'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=103;


CREATE TABLE `aioveu_exercise_count_template` (
    `id` VARCHAR(32) NOT NULL,
    `name` varchar(32) NOT NULL COMMENT '名称',
    `company_id` INT(10) NULL DEFAULT NULL COMMENT '公司id',
    `store_id` INT NOT NULL COMMENT '店铺id',
    `image` varchar(128) DEFAULT NULL COMMENT '图片',
    `original_price` decimal(8,2) NOT NULL COMMENT '原价',
    `price` decimal(8,2) NOT NULL COMMENT '价格',
    `vip_discount` double DEFAULT 1.0 COMMENT '会员折扣',
    `hour_price` decimal(8,2) default 0 COMMENT '小时价格',
    `description` VARCHAR(512) COMMENT '介绍',
    `process` VARCHAR(512) COMMENT '流程',
    `requirement` VARCHAR(256) COMMENT '要求',
    `start_day` DATE NOT NULL COMMENT '开始日期',
    `end_day` DATE NOT NULL COMMENT '结束日期',
    `exercise_start_time` TIME NOT NULL COMMENT '活动实际开始时间',
    `exercise_end_time` TIME NOT NULL COMMENT '活动实际结束时间',
    `limit_number` INT DEFAULT 0 NOT NULL COMMENT '限制人数',
    `enroll_number` INT DEFAULT 0 NOT NULL COMMENT '报名人数',
    `venue_id` INT NOT NULL COMMENT '场馆id',
    `venue_field_ids` varchar(128) NOT NULL COMMENT '场地id',
    `venue_field_names` varchar(128) not NULL COMMENT '场地名称',
    `remark` VARCHAR(32) COMMENT '备注',
    `create_user_id` VARCHAR(32) NOT NULL COMMENT '创建人',
    `create_username` VARCHAR(32) NOT NULL COMMENT '创建人名称',
    `time_type` TINYINT(4) NOT NULL DEFAULT 0 COMMENT '时间类型 默认0 自由时间 1每星期 2每月 3每天',
    `date_list` VARCHAR(512) COMMENT '选取时间 自由时间 每星期和每月有值',
    `skip_holiday` TINYINT(2) NOT NULL DEFAULT 0 COMMENT '是否跳过节假日 默认0不跳过 1跳过',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态 1正常 2下架',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '活动按次模板表';

CREATE TABLE `aioveu_exercise_count_template_id` (
   `id` INT NOT NULL AUTO_INCREMENT,
   `exercise_id` INT NOT NULL COMMENT '活动id',
   `exercise_count_template_id` VARCHAR(32) NOT NULL COMMENT '活动按次模板Id',
   `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
   `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
   `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
   PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '活动按次模板Id关联表';

CREATE TABLE `aioveu_company_balance_change` (
     `id` int NOT NULL AUTO_INCREMENT,
     `company_id` INT(10) NOT NULL COMMENT '公司id',
     `name` varchar(32) NOT NULL COMMENT '变动说明',
     `description` varchar(128) COMMENT '描述',
     `order_id` varchar(32) COMMENT '订单id',
     `change_type` tinyint NOT NULL default 0 COMMENT '变动类型 0 支出 1 充值',
     `amount` decimal(8,2) NOT NULL DEFAULT '0.00' COMMENT '金额',
     `balance` decimal(8,2) DEFAULT '0.00' COMMENT '余额',
     `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='公司余额变动表';

CREATE TABLE `aioveu_message` (
    `id` int NOT NULL AUTO_INCREMENT,
    `company_id` INT(10) NOT NULL COMMENT '公司id',
    `store_id` INT NOT NULL COMMENT '店铺id',
    `name` varchar(32) NOT NULL COMMENT '名称',
    `content` varchar(512) COMMENT '内容',
    `msg_type` tinyint NOT NULL default 0 COMMENT '消息类型 0 系统消息',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 未读 0 删除 2 已读',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息表';

CREATE TABLE `aioveu_report_form` (
     `id` int NOT NULL AUTO_INCREMENT,
     `company_id` INT(10) NOT NULL COMMENT '公司id',
     `store_id` INT NOT NULL COMMENT '店铺id',
     `content` varchar(512) COMMENT '内容',
     `count` INT NOT NULL DEFAULT 0 COMMENT '数量',
     `amount` DECIMAL(8, 2) DEFAULT 0 COMMENT '金额',
     `create_user_id` VARCHAR(32) NOT NULL COMMENT '创建人Id',
     `create_username` VARCHAR(32) NOT NULL COMMENT '创建人',
     `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 未读 0 删除 2 已读',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='报单表';

CREATE TABLE `aioveu_notice_option` (
    `id` int NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL COMMENT '名称',
    `code` varchar(32) NOT NULL COMMENT '编号',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='通知选项';

CREATE TABLE `aioveu_message_option` (
     `id` int NOT NULL AUTO_INCREMENT,
     `name` varchar(32) NOT NULL COMMENT '名称',
     `code` varchar(32) NOT NULL COMMENT '编号',
     `company_id` INT(10) NOT NULL COMMENT '公司id',
     `store_id` INT NOT NULL COMMENT '店铺id',
     `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '状态',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息选项';

CREATE TABLE `aioveu_message_config` (
    `id` int NOT NULL AUTO_INCREMENT,
    `company_id` INT(10) NOT NULL COMMENT '公司id',
    `store_id` INT NOT NULL COMMENT '店铺id',
    `notice_code` varchar(32) NOT NULL COMMENT '通知编号',
    `msg_code` varchar(32) NOT NULL COMMENT '消息编号',
    `template_id` varchar(32) COMMENT '消息模板id',
    `app_id` varchar(32) COMMENT '微信AppId',
    `config` json comment '配置',
    `receive_user` TEXT COMMENT '消息接收人',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='消息配置';

CREATE TABLE `aioveu_sys_config` (
      `id` int NOT NULL AUTO_INCREMENT,
      `name` varchar(32) NOT NULL COMMENT '字典名称',
      `code` varchar(32) NOT NULL COMMENT '字典编码',
      `category_code` varchar(32) COMMENT '分类编号',
      `role_code` varchar(32) COMMENT '角色编号',
      `value` TEXT NOT NULL comment '字典值',
      `default_value` TEXT comment '默认值',
      `field_type` varchar(64) DEFAULT NULL COMMENT '值类型',
      `config_desc` varchar(512) DEFAULT NULL COMMENT '描述',
      `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`),
      UNIQUE KEY `unique_code` (`code`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='系统配置表';

CREATE TABLE `aioveu_store_config` (
      `id` int NOT NULL AUTO_INCREMENT,
      `name` varchar(32) NOT NULL COMMENT '字典名称',
      `code` varchar(32) NOT NULL COMMENT '字典编码',
      `category_code` varchar(32) COMMENT '分类编号',
      `value` TEXT NOT NULL comment '字典值',
      `default_value` TEXT comment '默认值',
      `company_id` INT DEFAULT NULL COMMENT '公司id',
      `store_id` int DEFAULT NULL COMMENT '门店id',
      `field_type` varchar(64) DEFAULT NULL COMMENT '值类型',
      `config_desc` varchar(512) DEFAULT NULL COMMENT '描述',
      `remark` varchar(128) DEFAULT NULL COMMENT '备注',
      `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='店铺配置表';

CREATE TABLE `aioveu_chat_history` (
    `id` VARCHAR(64) NOT NULL comment '会话id',
    `user_id` varchar(32) NOT NULL comment '用户id',
    `messages` json NOT NULL comment '内容',
    `status` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '默认1 未读 0 删除 2 已读',
    `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='聊天记录表';

CREATE TABLE `aioveu_user_wechat_id` (
      `id` INT NOT NULL AUTO_INCREMENT,
      `user_id` VARCHAR(32) not null COMMENT '用户id',
      `robot_id` VARCHAR(32) COMMENT '微信机器人id',
      `company_id` INT(10) NOT NULL COMMENT '公司id',
      `wx_id` varchar(32) not null comment '微信Id',
      `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
      `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
      `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
      PRIMARY KEY (`id`),
      unique (`wx_id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '用户微信Id表';

CREATE TABLE `aioveu_operate_log` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` varchar(32) NOT NULL COMMENT '操作名称',
    `user_id` VARCHAR(32) not null COMMENT '用户id',
    `username` varchar(32) NOT NULL COMMENT '用户名称',
    `detail` varchar(128) NOT NULL COMMENT '操作详情',
    `role_code` varchar(128) NOT NULL COMMENT '角色code',
    `category_code` varchar(32) NOT NULL COMMENT '分类编号',
    `company_id` INT DEFAULT NULL COMMENT '公司id',
    `store_id` int DEFAULT NULL COMMENT '门店id',
    `product_id` varchar(32) NOT NULL COMMENT '产品id',
    `operate_time` DATETIME NOT NULL COMMENT '操作时间',
    `operate_type` TINYINT(2) NOT NULL DEFAULT 1 COMMENT '操作类型 增 0 删 1 改 2 查 3',
    `status` TINYINT(4) NOT NULL DEFAULT 1 COMMENT '状态',
    `create_date` TIMESTAMP NOT NULL DEFAULT current_timestamp COMMENT '创建时间',
    `update_date` TIMESTAMP NOT NULL DEFAULT current_timestamp ON UPDATE current_timestamp COMMENT '更新时间',
    PRIMARY KEY (`id`)
)ENGINE = InnoDB DEFAULT CHARSET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT '操作日志表';