/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_oms

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 16/03/2026 12:16:12
*/

SET NAMES utf8mb4;
# SET FOREIGN_KEY_CHECKS = 0;

use aioveu_tenant;
ALTER TABLE `sys_oauth_client_wx_app`
    ADD COLUMN `enabled` TINYINT NOT NULL DEFAULT 1 COMMENT '是否启用(1-启用;0-禁用)';
