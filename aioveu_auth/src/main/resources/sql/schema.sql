DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`  (
     `client_id` varchar(48) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '客户端id',
     `resource_ids` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资源的id，多个用逗号分隔',
     `client_secret` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端的秘钥',
     `scope` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户端的权限，多个用逗号分隔',
     `authorized_grant_types` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权类型，五种，多个用逗号分隔',
     `web_server_redirect_uri` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '授权码模式的跳转uri',
     `authorities` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '权限，多个用逗号分隔',
     `access_token_validity` int(11) NULL DEFAULT NULL COMMENT 'access_token的过期时间，单位毫秒，覆盖掉硬编码',
     `refresh_token_validity` int(11) NULL DEFAULT NULL COMMENT 'refresh_token的过期时间，单位毫秒，覆盖掉硬编码',
     `additional_information` varchar(4096) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '扩展字段，JSON',
     `autoapprove` varchar(256) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '默认false，是否自动授权',
     PRIMARY KEY (`client_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Compact;

DROP TABLE IF EXISTS `aioveu_permission`;
CREATE TABLE `aioveu_permission`  (
   `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
   `name` VARCHAR(64) NOT NULL COMMENT '权限名称',
   `url` VARCHAR(128) NOT NULL COMMENT 'URL权限标识',
   `method` VARCHAR(8) NOT NULL DEFAULT 'POST' COMMENT '请求方式 GET POST',
   `need_token` tinyint(2) NOT NULL DEFAULT 1 COMMENT '是否需要认证 默认1 需要 0 不需要',
   `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 待审核 0 删除 2 审核成功',
   `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
   `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
   PRIMARY KEY (`id`) USING BTREE,
   INDEX `id`(`id`, `name`) USING BTREE
) ENGINE = INNODB AUTO_INCREMENT = 2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT = 'URL权限表';

DROP TABLE IF EXISTS `aioveu_menu_permission`;
CREATE TABLE `aioveu_menu_permission`  (
     `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
     `menu_code` varchar(32) COLLATE utf8mb4_general_ci NOT NULL COMMENT '菜单code',
     `permission_id` BIGINT(20) NOT NULL COMMENT '资源id',
     `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
     `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
     `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
     PRIMARY KEY (`id`) USING BTREE,
     UNIQUE KEY `menu_permission` (`menu_code`,`permission_id`)
) ENGINE = INNODB AUTO_INCREMENT = 2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT = '菜单权限表';

DROP TABLE IF EXISTS `aioveu_role_menu_permission`;
CREATE TABLE `aioveu_role_menu_permission`  (
      `id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
      `role_code` varchar(32) NOT NULL COMMENT '角色code',
      `menu_code` varchar(32) NOT NULL COMMENT '菜单code',
      `permission_id` BIGINT(20) NOT NULL COMMENT '资源id',
      `status` tinyint(4) NOT NULL DEFAULT 1 COMMENT '状态 默认1 正常 0 删除',
      `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
      `update_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
      PRIMARY KEY (`id`) USING BTREE,
      UNIQUE KEY `menu_permission` (`role_code`, `menu_code`,`permission_id`)
) ENGINE = INNODB AUTO_INCREMENT = 2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT = '角色菜单权限表';