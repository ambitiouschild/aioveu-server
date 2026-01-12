USE aioveu_pms;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================================
-- 1. 商品品牌表
-- ============================================================================
DROP TABLE IF EXISTS `pms_brand`;
CREATE TABLE `pms_brand` (
                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '品牌ID',
                             `name` varchar(64) NOT NULL COMMENT '品牌名称',
                             `logo_url` varchar(255) DEFAULT NULL COMMENT '品牌LOGO地址',
                             `first_letter` char(1) DEFAULT NULL COMMENT '品牌首字母（用于品牌筛选）',
                             `description` text COMMENT '品牌描述',
                             `sort` int NOT NULL DEFAULT 100 COMMENT '排序（数值越小越靠前）',
                             `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
                             `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                             `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                             `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_brand_name` (`name`) COMMENT '品牌名称唯一',
                             KEY `idx_sort` (`sort`) COMMENT '排序索引',
                             KEY `idx_status` (`status`) COMMENT '状态索引',
                             KEY `idx_first_letter` (`first_letter`) COMMENT '首字母索引'
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品品牌表';

-- ============================================================================
-- 2. 商品分类表
-- ============================================================================
DROP TABLE IF EXISTS `pms_category`;
CREATE TABLE `pms_category` (
                                `id` bigint NOT NULL AUTO_INCREMENT COMMENT '分类ID',
                                `parent_id` bigint NOT NULL DEFAULT 0 COMMENT '父分类ID（0表示根分类）',
                                `ancestors` varchar(255) DEFAULT NULL COMMENT '祖先节点ID路径（用逗号分隔，用于快速查询子分类）',
                                `name` varchar(64) NOT NULL COMMENT '分类名称',
                                `level` tinyint NOT NULL DEFAULT 1 COMMENT '层级：1-一级分类 2-二级分类 3-三级分类',
                                `icon_url` varchar(255) DEFAULT NULL COMMENT '分类图标地址',
                                `image_url` varchar(255) DEFAULT NULL COMMENT '分类图片地址（用于前台展示）',
                                `description` varchar(500) DEFAULT NULL COMMENT '分类描述',
                                `keywords` varchar(255) DEFAULT NULL COMMENT '分类关键词（SEO用）',
                                `sort` int NOT NULL DEFAULT 100 COMMENT '排序（数值越小越靠前）',
                                `show_status` tinyint NOT NULL DEFAULT 1 COMMENT '显示状态：0-隐藏 1-显示',
                                `nav_status` tinyint NOT NULL DEFAULT 1 COMMENT '导航栏显示状态：0-不显示 1-显示',
                                `product_count` int NOT NULL DEFAULT 0 COMMENT '商品数量',
                                `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                                `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                                `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                PRIMARY KEY (`id`),
                                UNIQUE KEY `uk_category_name_parent` (`name`, `parent_id`) COMMENT '同一父分类下名称唯一',
                                KEY `idx_parent_id` (`parent_id`) COMMENT '父分类索引',
                                KEY `idx_level` (`level`) COMMENT '层级索引',
                                KEY `idx_sort` (`sort`) COMMENT '排序索引',
                                KEY `idx_show_status` (`show_status`) COMMENT '显示状态索引',
                                KEY `idx_nav_status` (`nav_status`) COMMENT '导航状态索引',
                                KEY `idx_ancestors` (`ancestors`(191)) COMMENT '祖先路径索引',
                                CONSTRAINT `chk_category_level` CHECK (`level` between 1 and 3)
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品分类表';

-- ============================================================================
-- 3. 分类属性表（规格+属性定义）
-- ============================================================================
DROP TABLE IF EXISTS `pms_category_attribute`;
CREATE TABLE `pms_category_attribute` (
                                          `id` bigint NOT NULL AUTO_INCREMENT COMMENT '属性ID',
                                          `category_id` bigint NOT NULL COMMENT '分类ID',
                                          `name` varchar(64) NOT NULL COMMENT '属性名称',
                                          `type` tinyint NOT NULL COMMENT '类型：1-规格 2-属性',
                                          `input_type` tinyint NOT NULL DEFAULT 1 COMMENT '输入类型：1-手动输入 2-单选 3-多选 4-下拉选择',
                                          `value_list` varchar(1000) DEFAULT NULL COMMENT '可选值列表（JSON数组，用于选择型属性）',
                                          `required` tinyint NOT NULL DEFAULT 0 COMMENT '是否必填：0-否 1-是',
                                          `searchable` tinyint NOT NULL DEFAULT 0 COMMENT '是否可搜索：0-否 1-是',
                                          `filterable` tinyint NOT NULL DEFAULT 0 COMMENT '是否可筛选：0-否 1-是',
                                          `sort` int NOT NULL DEFAULT 100 COMMENT '排序（数值越小越靠前）',
                                          `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                                          `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                                          `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                          PRIMARY KEY (`id`),
                                          UNIQUE KEY `uk_category_attribute_name` (`category_id`, `name`) COMMENT '同一分类下属性名称唯一',
                                          KEY `idx_category_id` (`category_id`) COMMENT '分类索引',
                                          KEY `idx_type` (`type`) COMMENT '类型索引',
                                          KEY `idx_sort` (`sort`) COMMENT '排序索引',
                                          CONSTRAINT `fk_category_attribute_category` FOREIGN KEY (`category_id`) REFERENCES `pms_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类属性表（定义规格和属性）';

-- ============================================================================
-- 4. 分类-品牌关联表
-- ============================================================================
DROP TABLE IF EXISTS `pms_category_brand`;
CREATE TABLE `pms_category_brand` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                      `category_id` bigint NOT NULL COMMENT '分类ID',
                                      `brand_id` bigint NOT NULL COMMENT '品牌ID',
                                      `sort` int NOT NULL DEFAULT 100 COMMENT '排序（数值越小越靠前）',
                                      `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                                      `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                                      `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_category_brand` (`category_id`, `brand_id`) COMMENT '同一分类下品牌唯一',
                                      KEY `idx_category_id` (`category_id`) COMMENT '分类索引',
                                      KEY `idx_brand_id` (`brand_id`) COMMENT '品牌索引',
                                      KEY `idx_sort` (`sort`) COMMENT '排序索引',
                                      CONSTRAINT `fk_category_brand_category` FOREIGN KEY (`category_id`) REFERENCES `pms_category` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `fk_category_brand_brand` FOREIGN KEY (`brand_id`) REFERENCES `pms_brand` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='分类-品牌关联表（多对多关系）';

-- ============================================================================
-- 5. 商品SPU表（标准产品单元）
-- ============================================================================
DROP TABLE IF EXISTS `pms_spu`;
CREATE TABLE `pms_spu` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'SPU ID',
                           `spu_sn` varchar(64) NOT NULL COMMENT 'SPU编码（唯一）',
                           `name` varchar(128) NOT NULL COMMENT '商品名称',
                           `sub_title` varchar(255) DEFAULT NULL COMMENT '商品副标题',
                           `category_id` bigint NOT NULL COMMENT '分类ID',
                           `brand_id` bigint DEFAULT NULL COMMENT '品牌ID',
                           `main_pic_url` varchar(255) NOT NULL COMMENT '商品主图',
                           `album` json DEFAULT NULL COMMENT '商品相册（JSON数组）',
                           `video_url` varchar(255) DEFAULT NULL COMMENT '商品视频地址',
                           `description` text COMMENT '商品描述（富文本）',
                           `detail` longtext COMMENT '商品详情（HTML）',
                           `unit` varchar(16) DEFAULT '件' COMMENT '商品单位',
                           `weight` decimal(10,2) DEFAULT NULL COMMENT '商品重量（kg）',
                           `volume` decimal(10,2) DEFAULT NULL COMMENT '商品体积（m³）',
                           `origin_price` bigint NOT NULL COMMENT '原价（单位：分）',
                           `price` bigint NOT NULL COMMENT '销售价（单位：分）',
                           `cost_price` bigint DEFAULT NULL COMMENT '成本价（单位：分）',
                           `market_price` bigint DEFAULT NULL COMMENT '市场价（单位：分）',
                           `sales` int NOT NULL DEFAULT 0 COMMENT '销量',
                           `stock` int NOT NULL DEFAULT 0 COMMENT '总库存',
                           `lock_stock` int NOT NULL DEFAULT 0 COMMENT '锁定库存',
                           `warning_stock` int NOT NULL DEFAULT 0 COMMENT '库存预警值',
                           `comment_count` int NOT NULL DEFAULT 0 COMMENT '评价数量',
                           `positive_rating` decimal(3,2) NOT NULL DEFAULT 0.00 COMMENT '好评率',
                           `view_count` int NOT NULL DEFAULT 0 COMMENT '浏览数',
                           `collect_count` int NOT NULL DEFAULT 0 COMMENT '收藏数',
                           `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-下架 1-上架 2-待审核',
                           `verify_status` tinyint NOT NULL DEFAULT 0 COMMENT '审核状态：0-未审核 1-审核通过 2-审核不通过',
                           `publish_status` tinyint NOT NULL DEFAULT 0 COMMENT '发布状态：0-未发布 1-已发布',
                           `new_status` tinyint NOT NULL DEFAULT 0 COMMENT '新品状态：0-不是新品 1-新品',
                           `recommend_status` tinyint NOT NULL DEFAULT 0 COMMENT '推荐状态：0-不推荐 1-推荐',
                           `service_ids` varchar(255) DEFAULT NULL COMMENT '服务保障IDs（逗号分隔）',
                           `keywords` varchar(255) DEFAULT NULL COMMENT '商品关键词（SEO用）',
                           `note` varchar(500) DEFAULT NULL COMMENT '备注',
                           `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                           `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                           `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_spu_sn` (`spu_sn`) COMMENT 'SPU编码唯一',
                           KEY `idx_category_id` (`category_id`) COMMENT '分类索引',
                           KEY `idx_brand_id` (`brand_id`) COMMENT '品牌索引',
                           KEY `idx_name` (`name`) COMMENT '商品名称索引',
                           KEY `idx_status` (`status`) COMMENT '状态索引',
                           KEY `idx_verify_status` (`verify_status`) COMMENT '审核状态索引',
                           KEY `idx_price` (`price`) COMMENT '价格索引',
                           KEY `idx_sales` (`sales`) COMMENT '销量索引',
                           KEY `idx_new_status` (`new_status`) COMMENT '新品状态索引',
                           KEY `idx_recommend_status` (`recommend_status`) COMMENT '推荐状态索引',
                           KEY `idx_created_time` (`created_time`) COMMENT '创建时间索引',
                           CONSTRAINT `fk_spu_category` FOREIGN KEY (`category_id`) REFERENCES `pms_category` (`id`) ON DELETE RESTRICT ON UPDATE CASCADE,
                           CONSTRAINT `fk_spu_brand` FOREIGN KEY (`brand_id`) REFERENCES `pms_brand` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品SPU表（Standard Product Unit）';

-- ============================================================================
-- 6. 商品SKU表（库存量单位）
-- ============================================================================
DROP TABLE IF EXISTS `pms_sku`;
CREATE TABLE `pms_sku` (
                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'SKU ID',
                           `sku_sn` varchar(64) NOT NULL COMMENT 'SKU编码（唯一）',
                           `spu_id` bigint NOT NULL COMMENT 'SPU ID',
                           `name` varchar(255) NOT NULL COMMENT 'SKU名称',
                           `spec_attrs` json DEFAULT NULL COMMENT '规格属性（JSON格式，如{"颜色":"黑色","内存":"8G"}）',
                           `spec_ids` varchar(255) DEFAULT NULL COMMENT '规格ID组合（用下划线分隔，用于快速匹配）',
                           `price` bigint NOT NULL COMMENT '价格（单位：分）',
                           `cost_price` bigint DEFAULT NULL COMMENT '成本价（单位：分）',
                           `market_price` bigint DEFAULT NULL COMMENT '市场价（单位：分）',
                           `stock` int NOT NULL DEFAULT 0 COMMENT '库存数量',
                           `lock_stock` int NOT NULL DEFAULT 0 COMMENT '锁定库存',
                           `warning_stock` int NOT NULL DEFAULT 0 COMMENT '库存预警值',
                           `pic_url` varchar(255) NOT NULL COMMENT 'SKU图片',
                           `album` json DEFAULT NULL COMMENT 'SKU相册（JSON数组）',
                           `sales` int NOT NULL DEFAULT 0 COMMENT '销量',
                           `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
                           `default_sku` tinyint NOT NULL DEFAULT 0 COMMENT '是否默认SKU：0-否 1-是',
                           `low_stock` tinyint NOT NULL DEFAULT 0 COMMENT '低库存标识：0-正常 1-低库存',
                           `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                           `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                           `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                           `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                           PRIMARY KEY (`id`),
                           UNIQUE KEY `uk_sku_sn` (`sku_sn`) COMMENT 'SKU编码唯一',
                           UNIQUE KEY `uk_spu_spec_ids` (`spu_id`, `spec_ids`) COMMENT '同一SPU下规格组合唯一',
                           KEY `idx_spu_id` (`spu_id`) COMMENT 'SPU索引',
                           KEY `idx_sku_sn` (`sku_sn`) COMMENT 'SKU编码索引',
                           KEY `idx_price` (`price`) COMMENT '价格索引',
                           KEY `idx_stock` (`stock`) COMMENT '库存索引',
                           KEY `idx_status` (`status`) COMMENT '状态索引',
                           KEY `idx_default_sku` (`default_sku`) COMMENT '默认SKU索引',
                           CONSTRAINT `fk_sku_spu` FOREIGN KEY (`spu_id`) REFERENCES `pms_spu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品SKU表（Stock Keeping Unit）';

-- ============================================================================
-- 7. 商品属性值表
-- ============================================================================
DROP TABLE IF EXISTS `pms_spu_attribute_value`;
CREATE TABLE `pms_spu_attribute_value` (
                                           `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                           `spu_id` bigint NOT NULL COMMENT 'SPU ID',
                                           `category_attribute_id` bigint DEFAULT NULL COMMENT '分类属性ID',
                                           `attribute_name` varchar(64) NOT NULL COMMENT '属性名称',
                                           `attribute_value` varchar(255) NOT NULL COMMENT '属性值',
                                           `type` tinyint NOT NULL COMMENT '类型：1-规格 2-属性',
                                           `sort` int NOT NULL DEFAULT 100 COMMENT '排序',
                                           `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                                           `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                                           `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                           PRIMARY KEY (`id`),
                                           UNIQUE KEY `uk_spu_attribute_name` (`spu_id`, `attribute_name`, `attribute_value`) COMMENT '同一SPU下属性名称+值唯一',
                                           KEY `idx_spu_id` (`spu_id`) COMMENT 'SPU索引',
                                           KEY `idx_category_attribute_id` (`category_attribute_id`) COMMENT '分类属性索引',
                                           KEY `idx_type` (`type`) COMMENT '类型索引',
                                           CONSTRAINT `fk_spu_attribute_spu` FOREIGN KEY (`spu_id`) REFERENCES `pms_spu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                           CONSTRAINT `fk_spu_attribute_category_attribute` FOREIGN KEY (`category_attribute_id`) REFERENCES `pms_category_attribute` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品属性值表';

-- ============================================================================
-- 8. 商品规格值表（与SKU关联）
-- ============================================================================
DROP TABLE IF EXISTS `pms_sku_spec_value`;
CREATE TABLE `pms_sku_spec_value` (
                                      `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                      `sku_id` bigint NOT NULL COMMENT 'SKU ID',
                                      `spu_id` bigint NOT NULL COMMENT 'SPU ID',
                                      `category_attribute_id` bigint NOT NULL COMMENT '分类属性ID',
                                      `attribute_name` varchar(64) NOT NULL COMMENT '属性名称',
                                      `attribute_value` varchar(128) NOT NULL COMMENT '属性值',
                                      `pic_url` varchar(255) DEFAULT NULL COMMENT '规格图片',
                                      `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                                      `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                                      `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      PRIMARY KEY (`id`),
                                      UNIQUE KEY `uk_sku_attribute` (`sku_id`, `category_attribute_id`) COMMENT '同一SKU下同一属性唯一',
                                      KEY `idx_sku_id` (`sku_id`) COMMENT 'SKU索引',
                                      KEY `idx_spu_id` (`spu_id`) COMMENT 'SPU索引',
                                      KEY `idx_category_attribute_id` (`category_attribute_id`) COMMENT '分类属性索引',
                                      CONSTRAINT `fk_sku_spec_sku` FOREIGN KEY (`sku_id`) REFERENCES `pms_sku` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `fk_sku_spec_spu` FOREIGN KEY (`spu_id`) REFERENCES `pms_spu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
                                      CONSTRAINT `fk_sku_spec_category_attribute` FOREIGN KEY (`category_attribute_id`) REFERENCES `pms_category_attribute` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='SKU规格值表';

-- ============================================================================
-- 9. 库存变更记录表
-- ============================================================================
DROP TABLE IF EXISTS `pms_stock_log`;
CREATE TABLE `pms_stock_log` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `sku_id` bigint NOT NULL COMMENT 'SKU ID',
                                 `change_type` tinyint NOT NULL COMMENT '变更类型：1-入库 2-出库 3-锁定 4-解锁 5-盘点',
                                 `change_quantity` int NOT NULL COMMENT '变更数量（正数表示增加，负数表示减少）',
                                 `before_stock` int NOT NULL COMMENT '变更前库存',
                                 `after_stock` int NOT NULL COMMENT '变更后库存',
                                 `before_lock_stock` int DEFAULT NULL COMMENT '变更前锁定库存',
                                 `after_lock_stock` int DEFAULT NULL COMMENT '变更后锁定库存',
                                 `order_sn` varchar(64) DEFAULT NULL COMMENT '订单号',
                                 `remark` varchar(500) DEFAULT NULL COMMENT '备注',
                                 `created_by` varchar(50) DEFAULT NULL COMMENT '操作人',
                                 `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_sku_id` (`sku_id`) COMMENT 'SKU索引',
                                 KEY `idx_created_time` (`created_time`) COMMENT '操作时间索引',
                                 KEY `idx_order_sn` (`order_sn`) COMMENT '订单号索引',
                                 KEY `idx_change_type` (`change_type`) COMMENT '变更类型索引',
                                 CONSTRAINT `fk_stock_log_sku` FOREIGN KEY (`sku_id`) REFERENCES `pms_sku` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=100000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='库存变更记录表';

-- ============================================================================
-- 10. 商品相册表
-- ============================================================================
DROP TABLE IF EXISTS `pms_spu_album`;
CREATE TABLE `pms_spu_album` (
                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                 `spu_id` bigint NOT NULL COMMENT 'SPU ID',
                                 `pic_url` varchar(255) NOT NULL COMMENT '图片地址',
                                 `pic_desc` varchar(255) DEFAULT NULL COMMENT '图片描述',
                                 `sort` int NOT NULL DEFAULT 100 COMMENT '排序',
                                 `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                                 `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                 PRIMARY KEY (`id`),
                                 KEY `idx_spu_id` (`spu_id`) COMMENT 'SPU索引',
                                 KEY `idx_sort` (`sort`) COMMENT '排序索引',
                                 CONSTRAINT `fk_spu_album_spu` FOREIGN KEY (`spu_id`) REFERENCES `pms_spu` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=10000 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品相册表';

-- ============================================================================
-- 11. 商品服务保障表
-- ============================================================================
DROP TABLE IF EXISTS `pms_service`;
CREATE TABLE `pms_service` (
                               `id` bigint NOT NULL AUTO_INCREMENT COMMENT '服务ID',
                               `name` varchar(64) NOT NULL COMMENT '服务名称',
                               `icon` varchar(255) DEFAULT NULL COMMENT '服务图标',
                               `description` varchar(255) DEFAULT NULL COMMENT '服务描述',
                               `sort` int NOT NULL DEFAULT 100 COMMENT '排序',
                               `status` tinyint NOT NULL DEFAULT 1 COMMENT '状态：0-禁用 1-启用',
                               `created_by` varchar(50) DEFAULT NULL COMMENT '创建人',
                               `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               `updated_by` varchar(50) DEFAULT NULL COMMENT '更新人',
                               `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               PRIMARY KEY (`id`),
                               KEY `idx_sort` (`sort`) COMMENT '排序索引',
                               KEY `idx_status` (`status`) COMMENT '状态索引'
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='商品服务保障表';

-- ============================================================================
-- 插入模拟数据
-- ============================================================================

-- 1. 插入品牌数据
INSERT INTO `pms_brand` (`id`, `name`, `logo_url`, `first_letter`, `description`, `sort`, `status`) VALUES
                                                                                                        (1, '小米', 'https://oss.aioveu.com/default/xiaomi-logo.png', 'X', '为发烧而生，小米科技有限责任公司', 1, 1),
                                                                                                        (2, '华为', 'https://oss.aioveu.com/default/huawei-logo.png', 'H', '华为技术有限公司，全球领先的信息与通信技术解决方案供应商', 2, 1),
                                                                                                        (3, '苹果', 'https://oss.aioveu.com/default/apple-logo.png', 'P', 'Apple Inc. 美国高科技公司', 3, 1),
                                                                                                        (4, '华硕', 'https://oss.aioveu.com/default/asus-logo.png', 'H', '华硕电脑股份有限公司，台湾电脑硬件公司', 4, 1),
                                                                                                        (5, '联想', 'https://oss.aioveu.com/default/lenovo-logo.png', 'L', '联想集团有限公司，全球PC领导品牌', 5, 1),
                                                                                                        (6, '戴尔', 'https://oss.aioveu.com/default/dell-logo.png', 'D', '戴尔公司，全球知名电脑品牌', 6, 1),
                                                                                                        (7, '惠普', 'https://oss.aioveu.com/default/hp-logo.png', 'H', '惠普公司，全球领先的打印和个人系统产品及服务供应商', 7, 1),
                                                                                                        (8, '三星', 'https://oss.aioveu.com/default/samsung-logo.png', 'S', '三星集团，韩国跨国企业', 8, 1);

-- 2. 插入分类数据
INSERT INTO `pms_category` (`id`, `parent_id`, `ancestors`, `name`, `level`, `icon_url`, `sort`, `show_status`) VALUES
-- 一级分类
(1, 0, '0', '手机数码', 1, 'https://oss.aioveu.com/default/category-phone.png', 1, 1),
(2, 0, '0', '电脑办公', 1, 'https://oss.aioveu.com/default/category-computer.png', 2, 1),
(3, 0, '0', '家用电器', 1, 'https://oss.aioveu.com/default/category-appliance.png', 3, 1),
(4, 0, '0', '服装鞋帽', 1, 'https://oss.aioveu.com/default/category-clothes.png', 4, 1),

-- 二级分类：手机数码
(101, 1, '0,1', '手机通讯', 2, NULL, 1, 1),
(102, 1, '0,1', '手机配件', 2, NULL, 2, 1),
(103, 1, '0,1', '摄影摄像', 2, NULL, 3, 1),

-- 二级分类：电脑办公
(201, 2, '0,2', '笔记本电脑', 2, NULL, 1, 1),
(202, 2, '0,2', '台式电脑', 2, NULL, 2, 1),
(203, 2, '0,2', '电脑配件', 2, NULL, 3, 1),

-- 三级分类：手机通讯
(1001, 101, '0,1,101', '智能手机', 3, NULL, 1, 1),
(1002, 101, '0,1,101', '老人手机', 3, NULL, 2, 1),
(1003, 101, '0,1,101', '游戏手机', 3, NULL, 3, 1),

-- 三级分类：笔记本电脑
(2001, 201, '0,2,201', '轻薄本', 3, NULL, 1, 1),
(2002, 201, '0,2,201', '游戏本', 3, NULL, 2, 1),
(2003, 201, '0,2,201', '商务本', 3, NULL, 3, 1);

-- 3. 插入分类属性（规格+属性）
INSERT INTO `pms_category_attribute` (`category_id`, `name`, `type`, `input_type`, `value_list`, `required`, `searchable`, `filterable`, `sort`) VALUES
-- 智能手机规格
(1001, '颜色', 1, 2, '["黑色","白色","蓝色","红色","金色"]', 1, 1, 1, 1),
(1001, '内存', 1, 2, '["8GB","12GB","16GB","24GB"]', 1, 1, 1, 2),
(1001, '存储', 1, 2, '["128GB","256GB","512GB","1TB"]', 1, 1, 1, 3),
(1001, '网络', 1, 2, '["5G","4G"]', 1, 0, 1, 4),

-- 智能手机属性
(1001, '上市时间', 2, 1, NULL, 1, 1, 1, 5),
(1001, 'CPU型号', 2, 1, NULL, 1, 1, 0, 6),
(1001, '屏幕尺寸', 2, 1, NULL, 1, 1, 1, 7),
(1001, '操作系统', 2, 1, NULL, 1, 0, 0, 8),

-- 游戏本规格
(2002, '颜色', 1, 2, '["黑色","灰色","白色","蓝色","红色"]', 1, 1, 1, 1),
(2002, '内存', 1, 2, '["16GB","32GB","64GB"]', 1, 1, 1, 2),
(2002, '硬盘', 1, 2, '["512GB","1TB","2TB"]', 1, 1, 1, 3),
(2002, '显卡', 1, 2, '["RTX 3050","RTX 3060","RTX 3070","RTX 3080"]', 1, 1, 1, 4),

-- 游戏本属性
(2002, 'CPU型号', 2, 1, NULL, 1, 1, 1, 5),
(2002, '屏幕尺寸', 2, 1, NULL, 1, 1, 1, 6),
(2002, '屏幕刷新率', 2, 1, NULL, 1, 1, 1, 7),
(2002, '重量', 2, 1, NULL, 1, 0, 0, 8);

-- 4. 插入分类-品牌关联
INSERT INTO `pms_category_brand` (`category_id`, `brand_id`, `sort`) VALUES
-- 智能手机分类关联品牌
(1001, 1, 1),  -- 小米
(1001, 2, 2),  -- 华为
(1001, 3, 3),  -- 苹果
(1001, 8, 4),  -- 三星

-- 游戏本分类关联品牌
(2002, 4, 1),  -- 华硕
(2002, 5, 2),  -- 联想
(2002, 6, 3),  -- 戴尔
(2002, 7, 4);  -- 惠普

-- 5. 插入SPU数据
INSERT INTO `pms_spu` (`id`, `spu_sn`, `name`, `sub_title`, `category_id`, `brand_id`, `main_pic_url`, `origin_price`, `price`, `sales`, `stock`, `status`, `description`) VALUES
-- 智能手机SPU
(1, 'SPU2023001', '小米13 Pro', '徕卡专业光学镜头 | 第二代骁龙8', 1001, 1, 'https://oss.aioveu.com/default/xiaomi13-pro.jpg', 499900, 459900, 1500, 500, 1, '小米年度旗舰手机'),
(2, 'SPU2023002', '华为Mate 50', '超光变XMAGE影像 | 北斗卫星消息', 1001, 2, 'https://oss.aioveu.com/default/huawei-mate50.jpg', 599900, 549900, 2000, 300, 1, '华为旗舰手机'),
(3, 'SPU2023003', 'iPhone 14 Pro', '灵动岛 | 4800万像素主摄', 1001, 3, 'https://oss.aioveu.com/default/iphone14-pro.jpg', 899900, 799900, 5000, 1000, 1, '苹果最新旗舰'),

-- 游戏本SPU
(4, 'SPU2023004', '华硕天选3', '12代i7 | RTX 3060 | 144Hz电竞屏', 2002, 4, 'https://oss.aioveu.com/default/asus-tianxuan3.jpg', 899900, 829900, 800, 200, 1, '潮玩次元游戏本'),
(5, 'SPU2023005', '联想拯救者Y9000P', 'i9-12900H | RTX 3070 Ti | 2.5K屏', 2002, 5, 'https://oss.aioveu.com/default/lenovo-y9000p.jpg', 1199900, 1099900, 600, 150, 1, '专业电竞游戏本');

-- 6. 插入SKU数据
INSERT INTO `pms_sku` (`sku_sn`, `spu_id`, `name`, `spec_attrs`, `spec_ids`, `price`, `stock`, `pic_url`, `default_sku`) VALUES
-- 小米13 Pro SKUs
('SKU202300101', 1, '小米13 Pro 黑色 8+256G', '{"颜色":"黑色","内存":"8GB","存储":"256GB"}', '1_2_3', 459900, 200, 'https://oss.aioveu.com/default/xiaomi13-pro-black.jpg', 1),
('SKU202300102', 1, '小米13 Pro 白色 12+256G', '{"颜色":"白色","内存":"12GB","存储":"256GB"}', '4_5_3', 499900, 150, 'https://oss.aioveu.com/default/xiaomi13-pro-white.jpg', 0),
('SKU202300103', 1, '小米13 Pro 蓝色 12+512G', '{"颜色":"蓝色","内存":"12GB","存储":"512GB"}', '6_5_7', 549900, 150, 'https://oss.aioveu.com/default/xiaomi13-pro-blue.jpg', 0),

-- 华为Mate 50 SKUs
('SKU202300201', 2, '华为Mate 50 黑色 8+128G', '{"颜色":"黑色","内存":"8GB","存储":"128GB"}', '1_2_8', 549900, 100, 'https://oss.aioveu.com/default/huawei-mate50-black.jpg', 1),
('SKU202300202', 2, '华为Mate 50 银色 8+256G', '{"颜色":"银色","内存":"8GB","存储":"256GB"}', '9_2_3', 599900, 100, 'https://oss.aioveu.com/default/huawei-mate50-silver.jpg', 0),
('SKU202300203', 2, '华为Mate 50 紫色 12+256G', '{"颜色":"紫色","内存":"12GB","存储":"256GB"}', '10_5_3', 649900, 100, 'https://oss.aioveu.com/default/huawei-mate50-purple.jpg', 0),

-- 华硕天选3 SKUs
('SKU202300401', 4, '华硕天选3 日蚀灰 i7/16G/512G/RTX3060', '{"颜色":"日蚀灰","内存":"16GB","硬盘":"512GB","显卡":"RTX 3060"}', '11_12_13_14', 829900, 100, 'https://oss.aioveu.com/default/asus-tianxuan3-gray.jpg', 1),
('SKU202300402', 4, '华硕天选3 魔幻青 i7/16G/1TB/RTX3060', '{"颜色":"魔幻青","内存":"16GB","硬盘":"1TB","显卡":"RTX 3060"}', '15_12_16_14', 879900, 50, 'https://oss.aioveu.com/default/asus-tianxuan3-green.jpg', 0),
('SKU202300403', 4, '华硕天选3 日蚀灰 i7/32G/1TB/RTX3070', '{"颜色":"日蚀灰","内存":"32GB","硬盘":"1TB","显卡":"RTX 3070"}', '11_17_16_18', 979900, 50, 'https://oss.aioveu.com/default/asus-tianxuan3-gray-pro.jpg', 0);

-- 7. 插入商品属性值
INSERT INTO `pms_spu_attribute_value` (`spu_id`, `category_attribute_id`, `attribute_name`, `attribute_value`, `type`, `sort`) VALUES
-- 小米13 Pro属性
(1, 5, '上市时间', '2022年12月', 2, 1),
(1, 6, 'CPU型号', '骁龙8 Gen 2', 2, 2),
(1, 7, '屏幕尺寸', '6.73英寸', 2, 3),
(1, 8, '操作系统', 'MIUI 14', 2, 4),

-- 华为Mate 50属性
(2, 5, '上市时间', '2022年9月', 2, 1),
(2, 6, 'CPU型号', '骁龙8+ Gen 1', 2, 2),
(2, 7, '屏幕尺寸', '6.7英寸', 2, 3),
(2, 8, '操作系统', 'HarmonyOS 3.0', 2, 4),

-- 华硕天选3属性
(4, 13, 'CPU型号', 'i7-12700H', 2, 1),
(4, 14, '屏幕尺寸', '15.6英寸', 2, 2),
(4, 15, '屏幕刷新率', '144Hz', 2, 3),
(4, 16, '重量', '2.1kg', 2, 4);

-- 8. 插入SKU规格值
INSERT INTO `pms_sku_spec_value` (`sku_id`, `spu_id`, `category_attribute_id`, `attribute_name`, `attribute_value`) VALUES
                                                                                                                        (1, 1, 1, '颜色', '黑色'),
                                                                                                                        (1, 1, 2, '内存', '8GB'),
                                                                                                                        (1, 1, 3, '存储', '256GB'),

                                                                                                                        (2, 1, 1, '颜色', '白色'),
                                                                                                                        (2, 1, 2, '内存', '12GB'),
                                                                                                                        (2, 1, 3, '存储', '256GB'),

                                                                                                                        (3, 1, 1, '颜色', '蓝色'),
                                                                                                                        (3, 1, 2, '内存', '12GB'),
                                                                                                                        (3, 1, 3, '存储', '512GB');

-- 9. 插入服务保障
INSERT INTO `pms_service` (`name`, `icon`, `description`, `sort`) VALUES
                                                                      ('七天退换', 'https://oss.aioveu.com/default/service-return.png', '7天无理由退换货', 1),
                                                                      ('正品保障', 'https://oss.aioveu.com/default/service-genuine.png', '100%正品保障', 2),
                                                                      ('极速发货', 'https://oss.aioveu.com/default/service-delivery.png', '下单后24小时内发货', 3),
                                                                      ('售后无忧', 'https://oss.aioveu.com/default/service-after-sale.png', '专业售后服务团队', 4);

-- ============================================================================
-- 重新开启外键检查
-- ============================================================================
SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================================
-- 表结构说明
-- ============================================================================
-- 1. pms_brand: 品牌表
-- 2. pms_category: 分类表（三级分类结构）
-- 3. pms_category_attribute: 分类属性表（定义规格和属性模板）
-- 4. pms_category_brand: 分类-品牌关联表
-- 5. pms_spu: 商品SPU表（标准产品单元）
-- 6. pms_sku: 商品SKU表（库存量单位）
-- 7. pms_spu_attribute_value: 商品属性值表
-- 8. pms_sku_spec_value: SKU规格值表
-- 9. pms_stock_log: 库存变更记录表
-- 10. pms_spu_album: 商品相册表
-- 11. pms_service: 商品服务保障表