/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_oauth2_server

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 10/06/2026 2339:12
*/


#废弃

use aioveu_oauth2_server;

ALTER TABLE `oauth2_registered_client_biz`
    ADD COLUMN `client_id_view` VARCHAR(100) NULL COMMENT '对外展示客户端ID';