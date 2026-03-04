/*
 Navicat Premium Data Transfer

 Source Server         : www.aioveu.com
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : www.aioveu.com:3306
 Source Schema         : aioveu_sms

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 03/04/2026 11:39:37
*/
# use aioveu_sms;

-- ----------------------------
-- Table structure for sms_home_category
-- ----------------------------
DROP TABLE IF EXISTS `sms_home_category`;
CREATE TABLE `sms_home_category`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category_id` BIGINT(20) NOT NULL COMMENT '关联商品分类ID（pms_category表）',
  `home_icon` VARCHAR(255) COMMENT '首页显示的图标URL',
  `home_name` varchar(100) COMMENT '首页显示名称',
  `jump_path` VARCHAR(255) DEFAULT '/pages/category/category' COMMENT '跳转路径',
  `jump_type` VARCHAR(20) DEFAULT 'navigateTo' COMMENT '跳转类型：navigateTo, redirectTo, switchTab',
  `sort` int(0) NULL DEFAULT NULL COMMENT '排序',
  `status` tinyint(1) NOT NULL COMMENT '状态：0-隐藏，1-显示',
  `remark` varchar(255)  COMMENT '备注',
    -- 时间戳
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `deleted` tinyint(1) NOT NULL DEFAULT '0' COMMENT '逻辑删除：0-正常 1-删除',
  `version` int(11) NOT NULL DEFAULT '0' COMMENT '版本号（用于乐观锁）',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '首页分类配置表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of sms_home_category
-- ----------------------------
-- 示例数据（包含跳转路径）
INSERT INTO sms_home_category (category_id, home_icon, home_name, jump_path, jump_type, sort, status) VALUES
(1, 'https://cdn.aioveu.com/icon/claude.png', 'ClaudeCode', '/pages/chat/claude', 'navigateTo', 5, 1),
(2, 'https://cdn.aioveu.com/icon/chatgpt.png', 'ChatGPT', '/pages/chat/chatgpt', 'navigateTo', 4, 1),
(3, 'https://cdn.aioveu.com/icon/gemini.png', 'Gemini', '/pages/chat/gemini', 'navigateTo', 3, 1),
(4, 'https://cdn.aioveu.com/icon/x.png', 'xAI', '/pages/chat/xai', 'navigateTo', 2, 1),
(5, 'https://cdn.aioveu.com/icon/deepseek.png', 'DeepSeek', '/pages/chat/deepseek', 'navigateTo', 1, 1);



