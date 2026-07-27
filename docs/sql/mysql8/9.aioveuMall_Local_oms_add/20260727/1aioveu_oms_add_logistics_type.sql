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
    ADD COLUMN `logistics_type` TINYINT NOT NULL DEFAULT 1 COMMENT '物流类型：1=物流配送，2=无需物流(虚拟商品)，3=自提，4=同城配送'
        AFTER `payment_method`;