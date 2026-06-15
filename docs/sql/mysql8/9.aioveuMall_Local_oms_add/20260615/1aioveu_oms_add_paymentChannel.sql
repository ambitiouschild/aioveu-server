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

use aioveu_oms;

-- 1. 先删掉那个全是 0 的字段（如果是测试环境）
# ALTER TABLE `oms_order` DROP COLUMN `payment_channel`;

-- 2. 重新加字段，允许为 NULL
ALTER TABLE `oms_order`
    ADD COLUMN `payment_channel` TINYINT NULL COMMENT '支付渠道：1-支付宝 2-微信支付 3-银联 4-余额 5-模拟支付 6-未知',
    ADD INDEX `idx_payment_channel` (`payment_channel`);