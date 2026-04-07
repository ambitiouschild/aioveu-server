

-- 不要使用
# 选择1：完全用 Spring Security 的默认结构（推荐）
#
# ✅ 最简单
#
# ✅ 最稳定
#
# ✅ 不需要维护两套ID
#
# ❌ 无法按物理存储顺序排序

# 将表改回Spring Security默认结构
#
# 添加create_time字段用于排序
#
# 所有查询都加上ORDER BY create_time DESC
#
#     这样既解决了框架兼容性问题，又能按时间顺序显示数据。
#
#     记住：数据库物理存储顺序不重要，查询时的排序才是关键！ 🎯

-- ----------------------------
-- Table structure for oauth2_registered_client （注册客户端表）
-- ----------------------------
-- DROP TABLE IF EXISTS `oauth2_registered_client`;
CREATE TABLE `oauth2_registered_client`  (
    -- 核心标识字段
                                             `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                             `uuid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端记录的唯一标识符，通常是UUID',
                                             `client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端标识符，OAuth2请求中使用的client_id',
                                             `client_id_issued_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '客户端ID的创建时间', -- 改为datetime，移除DEFAULT
                                             `client_secret` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户端密钥，已加密存储。公共客户端可为NULL',
                                             `client_secret_expires_at` timestamp NULL DEFAULT NULL COMMENT '客户端密钥的过期时间，NULL表示永不过期',
                                             `client_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端显示名称，用于用户界面显示',

    -- 安全配置字段
                                             `client_authentication_methods` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支持的客户端认证方法，JSON格式数组。如["client_secret_basic","client_secret_post"]',
                                             `authorization_grant_types` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '支持的授权类型，JSON格式数组。如["authorization_code","refresh_token","password","client_credentials"]',
                                             `redirect_uris` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '允许的重定向URI列表，JSON格式数组。必须与授权请求中的redirect_uri完全匹配',
                                             `post_logout_redirect_uris` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '登出后的重定向URI列表，JSON格式数组',
                                             `scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端可请求的范围列表，JSON格式数组。如["openid","profile","email"]',

    -- 设置字段
                                             `client_settings` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '客户端设置，JSON格式。包含requireAuthorizationConsent、requireProofKey等配置',
                                             `token_settings` varchar(2000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '令牌设置，JSON格式。包含accessTokenTimeToLive、refreshTokenTimeToLive等配置',

    -- 系统字段
                                             `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                             `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                             `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                             `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                             PRIMARY KEY (`id`) USING BTREE COMMENT '主键id',
                                             UNIQUE KEY `uk_client_id` (`client_id`) COMMENT '客户端ID的唯一约束，确保client_id不重复'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic
COMMENT = 'OAuth2注册客户端表，存储所有已注册的客户端应用信息';

-- ----------------------------
-- Records of oauth2_registered_client
-- ----------------------------

-- 修改INSERT语句，包含所有字段
INSERT INTO `oauth2_registered_client`
(`uuid`, `client_id`, `client_id_issued_at`, `client_secret`, `client_secret_expires_at`, `client_name`,
 `client_authentication_methods`, `authorization_grant_types`, `redirect_uris`, `post_logout_redirect_uris`,
 `scopes`, `client_settings`, `token_settings`)VALUES
('0d2eb3c4-bab5-4973-a64e-2c5c3898e249', 'mall-app', '2025-06-09 23:16:03', '{bcrypt}$2a$10$NEjYGOUglb2eGLanGJfCcu5snPVBPearoioNVKs9YRH3LMgdF41gK', NULL, '商城APP客户端', 'client_secret_basic', 'refresh_token,client_credentials,authorization_code,sms_code,wechat', 'http://127.0.0.1:8080/authorized', 'http://127.0.0.1:8080/logged-out', 'openid,profile', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",86400.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}'),
('7a552307-537d-41f2-98a4-6e8df31220fb', 'aioveu-admin', '2025-06-11 13:58:55', '{bcrypt}$2a$10$Yz7b3symTlVYQ8TPWfiyne5U0TSnbdywCNGTSymay7iXEfXxZdHY6', NULL, 'aioveu管理前端', 'client_secret_basic', 'refresh_token,client_credentials,password,authorization_code', 'http://127.0.0.1:8080/authorized', 'http://127.0.0.1:8080/logged-out', 'openid,profile', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",86400.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}'),
('94aa75c2-1c00-44d5-a1ff-edb18f8ad9c4', 'mall-admin', '2025-06-09 23:16:03', '{bcrypt}$2a$10$9klpwOFEl1zeWlATOYNFYeHoWuDsb160od86/cJrJG8Ik.7fQNOaC', NULL, '商城管理客户端', 'client_secret_basic', 'refresh_token,client_credentials,password,captcha,authorization_code', 'http://127.0.0.1:8080/authorized', 'http://127.0.0.1:8080/logged-out', 'openid,profile', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",86400.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}'),
('94aa75c2-1c00-44d5-a1ff-edb18f8ad9c5', 'client', '2025-06-09 23:16:03', '{bcrypt}$2a$10$T2Z9XX60zjBS0pwXZmqe1.uEf6OJiPEUhGKZg5b0SnZPwtvlxESJ2', NULL, '商城管理客户端', 'client_secret_basic', 'refresh_token,client_credentials,password,captcha,authorization_code', 'http://127.0.0.1:8080/authorized', 'http://127.0.0.1:8080/logged-out', 'openid,profile', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.client.require-proof-key\":false,\"settings.client.require-authorization-consent\":true}', '{\"@class\":\"java.util.Collections$UnmodifiableMap\",\"settings.token.reuse-refresh-tokens\":true,\"settings.token.id-token-signature-algorithm\":[\"org.springframework.security.oauth2.jose.jws.SignatureAlgorithm\",\"RS256\"],\"settings.token.access-token-time-to-live\":[\"java.time.Duration\",86400.000000000],\"settings.token.access-token-format\":{\"@class\":\"org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat\",\"value\":\"self-contained\"},\"settings.token.refresh-token-time-to-live\":[\"java.time.Duration\",3600.000000000],\"settings.token.authorization-code-time-to-live\":[\"java.time.Duration\",300.000000000],\"settings.token.device-code-time-to-live\":[\"java.time.Duration\",300.000000000]}');


-- ----------------------------
-- Table structure for oauth2_authorization  （授权信息表）
-- ----------------------------
-- DROP TABLE IF EXISTS `oauth2_authorization`;
CREATE TABLE `oauth2_authorization`  (
                                         `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                         `uuid` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL  COMMENT '授权记录的唯一标识符，通常是UUID',
                                         `registered_client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '关联的注册客户端ID，指向oauth2_registered_client表',
                                         `principal_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户主体名称，通常是用户名或用户标识',
                                         `authorization_grant_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '授权类型，如authorization_code、password、client_credentials、refresh_token等',
                                         `authorized_scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '授权的范围列表，以空格分隔的scope字符串',
                                         `attributes` blob NULL COMMENT '扩展属性，存储认证过程中的额外信息，序列化为JSON格式',
                                         `state` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT 'OAuth2授权码流程中的state参数，用于防止CSRF攻击',

    -- 授权码相关字段（用于Authorization Code流程）
                                         `authorization_code_value` blob NULL COMMENT '授权码的值，加密存储',
                                         `authorization_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '授权码颁发时间',
                                         `authorization_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '授权码过期时间（通常很短，如5分钟）',
                                         `authorization_code_metadata` blob NULL COMMENT '授权码的元数据',

    -- 访问令牌相关字段
                                         `access_token_value` blob NULL COMMENT '访问令牌的值，JWT或opaque token格式',
                                         `access_token_issued_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌颁发时间',
                                         `access_token_expires_at` timestamp NULL DEFAULT NULL COMMENT '访问令牌过期时间',
                                         `access_token_metadata` blob NULL COMMENT '访问令牌的元数据',
                                         `access_token_type` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问令牌类型，通常是Bearer',
                                         `access_token_scopes` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '访问令牌关联的scope',

    -- ID令牌相关字段（用于OpenID Connect）
                                         `oidc_id_token_value` blob NULL COMMENT 'OpenID Connect ID令牌的值',
                                         `oidc_id_token_issued_at` timestamp NULL DEFAULT NULL COMMENT 'ID令牌颁发时间',
                                         `oidc_id_token_expires_at` timestamp NULL DEFAULT NULL COMMENT 'ID令牌过期时间',
                                         `oidc_id_token_metadata` blob NULL COMMENT 'ID令牌的元数据',

    -- 刷新令牌相关字段
                                         `refresh_token_value` blob NULL COMMENT '刷新令牌的值',
                                         `refresh_token_issued_at` timestamp NULL DEFAULT NULL COMMENT '刷新令牌颁发时间',
                                         `refresh_token_expires_at` timestamp NULL DEFAULT NULL COMMENT '刷新令牌过期时间（通常较长，如30天）',
                                         `refresh_token_metadata` blob NULL COMMENT '刷新令牌的元数据',

    -- 设备授权相关字段（用于Device Code流程）
                                         `user_code_value` blob NULL COMMENT '设备授权流程中的用户码',
                                         `user_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '用户码颁发时间',
                                         `user_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '用户码过期时间',
                                         `user_code_metadata` blob NULL COMMENT '用户码的元数据',
                                         `device_code_value` blob NULL COMMENT '设备授权流程中的设备码',
                                         `device_code_issued_at` timestamp NULL DEFAULT NULL COMMENT '设备码颁发时间',
                                         `device_code_expires_at` timestamp NULL DEFAULT NULL COMMENT '设备码过期时间',
                                         `device_code_metadata` blob NULL COMMENT '设备码的元数据',

    -- 系统字段
                                         `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                         `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                         `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                                         PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic
    COMMENT = 'OAuth2授权信息表，存储所有的授权记录、令牌和状态信息';


-- ----------------------------
-- Table structure for oauth2_authorization_consent （授权同意表）
-- ----------------------------
-- DROP TABLE IF EXISTS `oauth2_authorization_consent`;
CREATE TABLE `oauth2_authorization_consent`  (
                                                 `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                                 `registered_client_id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '注册客户端ID，指向oauth2_registered_client表',
                                                 `principal_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户主体名称，用户的唯一标识',
                                                 `authorities` varchar(1000) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '用户对该客户端已同意的权限列表，JSON格式存储',

    -- 系统字段
                                                 `is_deleted` tinyint(1) NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除 1-已删除',
                                                 `remark` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
                                                 `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                 `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    -- 保留自增id，联合主键改为唯一索引
                                                 PRIMARY KEY (`id`) USING BTREE COMMENT '主键',
                                                 UNIQUE KEY `uk_client_principal` (`registered_client_id`, `principal_name`)  COMMENT '确保每个用户对每个客户端只有一条同意记录,联合唯一约束'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic
    COMMENT = 'OAuth2授权同意表，记录用户对每个客户端的授权同意情况';