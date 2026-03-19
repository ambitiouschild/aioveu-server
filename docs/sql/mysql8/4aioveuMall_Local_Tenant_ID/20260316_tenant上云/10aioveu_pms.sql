/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-boot_Local_3308
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : localhost:3308
 Source Schema         : aioveu_pms

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 16/03/2026 12:18:14
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for pms_brand
-- ----------------------------
DROP TABLE IF EXISTS `pms_brand`;
CREATE TABLE `pms_brand`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '品牌名称',
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT 'LOGO图片',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 35 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品品牌表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pms_brand
-- ----------------------------
INSERT INTO `pms_brand` VALUES (1, '可我不敌可爱', 'http://a.aioveu.com:9000/default/5409e3deb5a14b8fa8cb4275dee0e25d.png', 1, '2021-07-11 19:56:58', '2021-07-11 20:02:54', 1);
INSERT INTO `pms_brand` VALUES (10, '小米', 'http://a.aioveu.com:9000/default/6a5a606fc60742919149a7861bf26cd5.jpg', 2, '2022-03-05 16:12:16', '2022-03-05 16:12:16', 1);
INSERT INTO `pms_brand` VALUES (11, '华硕', 'http://a.aioveu.com:9000/default/f18083f95e104a0bae3c587dee3bb2ed.png', 3, '2022-03-05 16:12:16', '2022-03-05 16:12:16', 1);
INSERT INTO `pms_brand` VALUES (20, '华为', 'https://oss.aioveu.com/default/ff61bd639b23491d8f2aa85d09fcf788.jpg', 1, '2022-05-06 23:08:33', '2022-05-06 23:08:33', 1);
INSERT INTO `pms_brand` VALUES (33, '惠普', 'https://oss.aioveu.com/default/4cf579add9544c6eaafb41ce1131559e.gif', 1, '2022-07-07 00:12:16', '2022-07-07 00:12:16', 1);
INSERT INTO `pms_brand` VALUES (34, '苹果', '1111', 2, '2026-03-08 12:16:38', '2026-03-08 12:16:38', 1);

-- ----------------------------
-- Table structure for pms_category
-- ----------------------------
DROP TABLE IF EXISTS `pms_category`;
CREATE TABLE `pms_category`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品分类名称',
  `parent_id` bigint NOT NULL COMMENT '父级ID',
  `level` int NULL DEFAULT NULL COMMENT '层级',
  `icon_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '图标地址',
  `sort` int NULL DEFAULT NULL COMMENT '排序',
  `visible` tinyint(1) NULL DEFAULT 1 COMMENT '显示状态:( 0:隐藏 1:显示)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 195 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品分类表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pms_category
-- ----------------------------
INSERT INTO `pms_category` VALUES (3, '手机配件', 0, 1, 'http://localhost:9002/aioveu/20260116/668ad2719e1649cd8f660f006f266be6.png', 2, 0, NULL, '2026-03-08 12:07:38', 1);
INSERT INTO `pms_category` VALUES (4, '智能手机', 3, 2, 'http://localhost:9002/aioveu/20260116/1d5e472cad4a4ed49fa1627ae2e8b016.png', 1, 1, NULL, '2026-01-16 20:29:44', 1);
INSERT INTO `pms_category` VALUES (5, '5g手机', 4, 3, 'http://localhost:9002/aioveu/20260116/1bcb82c80a7e4cfc9d9e38f030c5d75e.png', 1, 1, NULL, '2026-01-16 20:29:57', 1);
INSERT INTO `pms_category` VALUES (6, '电脑配件', 0, 1, 'http://localhost:9002/aioveu/20260116/7ea3d5b475214a418f02692559a8b79c.png', 1, 0, '2022-02-25 11:22:44', '2026-03-08 12:07:31', 1);
INSERT INTO `pms_category` VALUES (97, '笔记本电脑', 6, 2, 'http://localhost:9002/aioveu/20260116/237d5200f38141208edbb034839a8c0c.png', 100, 1, '2022-07-08 00:10:27', '2026-01-16 20:30:10', 1);
INSERT INTO `pms_category` VALUES (99, '三星轻薄本', 97, 3, 'http://localhost:9002/aioveu/20260116/410340de0877421bb924ee601b8ccb00.png', 100, 1, '2022-07-08 00:14:03', '2026-01-16 20:34:04', 1);
INSERT INTO `pms_category` VALUES (100, '全能本', 97, 3, 'http://localhost:9002/aioveu/20260116/a9f693b69f1148f1b90b81e5fffb70c0.png', 100, 1, '2022-07-08 00:14:10', '2026-01-16 20:34:14', 1);
INSERT INTO `pms_category` VALUES (101, '游戏本', 97, 3, 'http://localhost:9002/aioveu/20260116/d34e2b148d7b4d3f934507c77a48eb60.png', 100, 1, '2022-07-08 00:14:18', '2026-01-16 20:34:19', 1);
INSERT INTO `pms_category` VALUES (102, '母婴玩具', 0, 1, '', 6, 0, '2026-01-17 12:27:16', '2026-03-08 12:07:50', 1);
INSERT INTO `pms_category` VALUES (103, '美妆个护', 0, 1, '', 4, 0, '2026-01-17 12:30:32', '2026-03-08 12:07:43', 1);
INSERT INTO `pms_category` VALUES (104, '艾欧尼亚', 102, 2, '', 100, 1, '2026-01-17 14:46:30', '2026-01-17 14:47:09', 1);
INSERT INTO `pms_category` VALUES (105, '黑色玫瑰', 102, 2, '', 100, 1, '2026-01-17 14:46:38', '2026-01-17 14:47:22', 1);
INSERT INTO `pms_category` VALUES (106, '阿狸', 104, 3, 'http://localhost:9002/aioveu/20260117/10f66a0b83f941a588b1efbfe0607af5.png', 100, 1, '2026-01-17 14:48:34', '2026-01-17 14:53:04', 1);
INSERT INTO `pms_category` VALUES (107, '阿卡丽', 104, 3, 'http://localhost:9002/aioveu/20260117/351da1d21e9e449fa820303b807f3f99.png', 100, 1, '2026-01-17 14:48:41', '2026-01-17 14:54:30', 1);
INSERT INTO `pms_category` VALUES (108, '艾瑞莉娅', 104, 3, 'http://localhost:9002/aioveu/20260117/17a7d7d990e04565a1c2206197a42649.png', 100, 1, '2026-01-17 14:54:24', '2026-01-17 14:54:24', 1);
INSERT INTO `pms_category` VALUES (109, '永恩', 104, 3, 'http://localhost:9002/aioveu/20260117/b0d67675e54f4ffbb1a654d6cedc1624.png', 100, 1, '2026-01-17 14:54:46', '2026-01-17 14:54:46', 1);
INSERT INTO `pms_category` VALUES (110, '乐芙兰‌', 105, 3, 'http://localhost:9002/aioveu/20260117/df913b045fdd4696a43165e56a86f1df.png', 100, 1, '2026-01-17 14:55:04', '2026-01-17 14:55:04', 1);
INSERT INTO `pms_category` VALUES (111, '‌卡西奥佩娅‌', 105, 3, 'http://localhost:9002/aioveu/20260117/620d9512d1264bc1a71d4edd7d8212ad.png', 100, 1, '2026-01-17 14:55:20', '2026-01-17 14:55:20', 1);
INSERT INTO `pms_category` VALUES (112, '弗拉基米尔‌', 105, 3, 'http://localhost:9002/aioveu/20260117/194645453a034b0b9216c548cf080793.png', 100, 1, '2026-01-17 14:56:18', '2026-01-17 14:56:18', 1);
INSERT INTO `pms_category` VALUES (113, '食品饮料', 0, 1, 'http://localhost:9002/aioveu/20260117/596ff14880e540ae98df2dfbe52d2be5.png', 3, 0, '2026-01-17 14:57:18', '2026-03-08 12:11:12', 1);
INSERT INTO `pms_category` VALUES (114, '运动户外', 0, 1, 'http://localhost:9002/aioveu/20260117/33d90c49ed084f24b8eff3821f6e4b85.png', 5, 0, '2026-01-17 14:57:28', '2026-03-08 12:11:26', 1);
INSERT INTO `pms_category` VALUES (115, '图书文娱', 0, 1, 'http://localhost:9002/aioveu/20260117/e3d559ae8fb84f368200d29b7d2c9f88.png', 7, 0, '2026-01-17 14:57:47', '2026-03-08 12:07:55', 1);
INSERT INTO `pms_category` VALUES (116, '海岛', 115, 2, 'http://localhost:9002/aioveu/20260117/963609874ae5405d9690dc3ed81a046a.png', 100, 1, '2026-01-17 14:58:14', '2026-01-17 14:58:14', 1);
INSERT INTO `pms_category` VALUES (117, '沙漠', 115, 2, 'http://localhost:9002/aioveu/20260117/ab88ca794f58475ba486037f3d48ee19.png', 100, 1, '2026-01-17 14:58:37', '2026-01-17 14:58:37', 1);
INSERT INTO `pms_category` VALUES (118, '珠宝钟表', 0, 1, 'http://localhost:9002/aioveu/20260117/184d03e9e0ce4692a35e12a2c04a781c.png', 9, 0, '2026-01-17 14:59:09', '2026-03-08 12:08:05', 1);
INSERT INTO `pms_category` VALUES (119, '汽车用品', 0, 1, 'http://localhost:9002/aioveu/20260117/d2c47d78aa014f8ba7736ca612fb0eaf.png', 8, 0, '2026-01-17 14:59:52', '2026-03-08 12:08:02', 1);
INSERT INTO `pms_category` VALUES (120, '医药保健', 0, 1, 'http://localhost:9002/aioveu/20260117/f9dab7dc95414ed9aab301e7a58202ff.png', 10, 0, '2026-01-17 15:00:02', '2026-03-08 12:08:10', 1);
INSERT INTO `pms_category` VALUES (121, '上单坦克', 103, 2, 'http://localhost:9002/aioveu/20260117/ea92a54e953643559fe287b3c56797ad.png', 100, 1, '2026-01-17 15:00:25', '2026-01-17 15:02:00', 1);
INSERT INTO `pms_category` VALUES (122, '打野刺客', 103, 2, 'http://localhost:9002/aioveu/20260117/70076b40dddc4d33b598008383b977fd.png', 100, 1, '2026-01-17 15:00:32', '2026-01-17 15:04:02', 1);
INSERT INTO `pms_category` VALUES (123, '中单', 103, 2, 'http://localhost:9002/aioveu/20260117/8064795802834f978e411f7775dfbf38.png', 100, 1, '2026-01-17 15:00:40', '2026-01-17 15:00:40', 1);
INSERT INTO `pms_category` VALUES (124, '射手', 103, 2, 'http://localhost:9002/aioveu/20260117/f98a8d6403574fa094eb07321afba0cf.png', 100, 1, '2026-01-17 15:00:48', '2026-01-17 15:00:48', 1);
INSERT INTO `pms_category` VALUES (125, '辅助', 103, 2, 'http://localhost:9002/aioveu/20260117/1ca55ec5f1d84ef38cfd18bb17aeda92.png', 100, 1, '2026-01-17 15:00:56', '2026-01-17 15:00:56', 1);
INSERT INTO `pms_category` VALUES (126, '上单战士', 103, 2, 'http://localhost:9002/aioveu/20260117/f508905586e14856838876e70ff09892.png', 100, 1, '2026-01-17 15:02:17', '2026-01-17 15:02:17', 1);
INSERT INTO `pms_category` VALUES (127, '廉颇', 121, 3, 'http://localhost:9002/aioveu/20260117/982822e44c9643edae77ec90045dfc37.png', 100, 1, '2026-01-17 15:02:29', '2026-01-17 15:02:29', 1);
INSERT INTO `pms_category` VALUES (128, '亚瑟', 121, 3, 'http://localhost:9002/aioveu/20260117/1e0efde1df534503949a3f3e640cbb4e.png', 100, 1, '2026-01-17 15:02:42', '2026-01-17 15:02:42', 1);
INSERT INTO `pms_category` VALUES (129, '程咬金', 121, 3, 'http://localhost:9002/aioveu/20260117/4aa14bc8f8f04a6cb7bdeb8f0c7aa021.png', 100, 1, '2026-01-17 15:02:52', '2026-01-17 15:02:52', 1);
INSERT INTO `pms_category` VALUES (130, '花木兰', 126, 3, 'http://localhost:9002/aioveu/20260117/3410b9f045f94853b1aa5d72c3de6a87.png', 100, 1, '2026-01-17 15:03:17', '2026-01-17 15:03:17', 1);
INSERT INTO `pms_category` VALUES (131, '钟无艳', 126, 3, 'http://localhost:9002/aioveu/20260117/f928bb17a6be4c049aa5104b246f390f.png', 100, 1, '2026-01-17 15:03:32', '2026-01-17 15:03:32', 1);
INSERT INTO `pms_category` VALUES (132, '吕布', 126, 3, 'http://localhost:9002/aioveu/20260117/83c37a9fa5f244f5a621024651604010.png', 100, 1, '2026-01-17 15:03:50', '2026-01-17 15:03:50', 1);
INSERT INTO `pms_category` VALUES (133, '兰陵王', 122, 3, 'http://localhost:9002/aioveu/20260117/b8927ab3b7384ad8b12b8d2df9a346de.png', 100, 1, '2026-01-17 15:06:12', '2026-01-17 15:06:12', 1);
INSERT INTO `pms_category` VALUES (134, '李白', 122, 3, 'http://localhost:9002/aioveu/20260117/0857f3b433a9478d8cb81cd22a22f453.png', 100, 1, '2026-01-17 15:06:27', '2026-01-17 15:06:27', 1);
INSERT INTO `pms_category` VALUES (135, '孙悟空', 122, 3, 'http://localhost:9002/aioveu/20260117/94c01577516f49f195342c18371248b3.png', 100, 1, '2026-01-17 15:06:38', '2026-01-17 15:06:38', 1);
INSERT INTO `pms_category` VALUES (136, '水晶之痕', 102, 2, 'http://localhost:9002/aioveu/20260126/40412a0eb4ab4d098cd916950bc4bb27.JPG', 1, 1, '2026-01-26 14:53:14', '2026-01-26 14:53:14', 1);
INSERT INTO `pms_category` VALUES (137, '祖安', 102, 2, 'http://localhost:9002/aioveu/20260126/6d2805404d924a799ae5977ed2125231.JPG', 1, 1, '2026-01-26 14:55:17', '2026-01-26 14:55:17', 1);
INSERT INTO `pms_category` VALUES (138, '艾克', 137, 3, 'http://localhost:9002/aioveu/20260126/d3e5890776254729ac57a40c19fb6216.png', 1, 1, '2026-01-26 14:55:31', '2026-01-26 14:55:31', 1);
INSERT INTO `pms_category` VALUES (139, '宠物用品', 0, 1, '', 11, 0, '2026-01-28 17:01:01', '2026-03-08 12:08:14', 1);
INSERT INTO `pms_category` VALUES (140, '办公设备', 0, 1, '', 12, 0, '2026-01-28 17:01:08', '2026-03-08 12:08:18', 1);
INSERT INTO `pms_category` VALUES (141, '工业用品', 0, 1, '', 13, 0, '2026-01-28 17:01:18', '2026-03-08 12:08:22', 1);
INSERT INTO `pms_category` VALUES (142, '乐器', 0, 1, '', 14, 0, '2026-01-28 17:01:25', '2026-03-08 12:08:26', 1);
INSERT INTO `pms_category` VALUES (143, '鲜花绿植', 0, 1, '', 15, 0, '2026-01-28 17:01:32', '2026-03-08 12:08:30', 1);
INSERT INTO `pms_category` VALUES (144, '农资农具', 0, 1, '', 16, 0, '2026-01-28 17:01:44', '2026-03-08 12:08:36', 1);
INSERT INTO `pms_category` VALUES (145, '二手闲置', 0, 1, '', 17, 0, '2026-01-28 17:01:51', '2026-03-08 12:08:40', 1);
INSERT INTO `pms_category` VALUES (146, '本地生活', 0, 1, '', 18, 0, '2026-01-28 17:01:58', '2026-03-08 12:08:44', 1);
INSERT INTO `pms_category` VALUES (147, '虚拟产品', 0, 1, '', 19, 0, '2026-01-28 17:02:04', '2026-03-08 12:08:48', 1);
INSERT INTO `pms_category` VALUES (148, '成人用品', 0, 1, '', 20, 0, '2026-01-28 17:02:11', '2026-03-08 12:08:53', 1);
INSERT INTO `pms_category` VALUES (149, '艺术品收藏', 0, 1, '', 21, 0, '2026-01-28 17:02:18', '2026-03-08 12:08:59', 1);
INSERT INTO `pms_category` VALUES (150, '企业采购', 0, 1, '', 22, 0, '2026-01-28 17:02:24', '2026-03-08 12:09:04', 1);
INSERT INTO `pms_category` VALUES (151, '食品生鲜', 0, 1, '', 27, 0, '2026-01-28 17:04:21', '2026-03-08 12:09:23', 1);
INSERT INTO `pms_category` VALUES (152, '数码家电', 0, 1, '', 24, 1, '2026-01-28 17:04:28', '2026-01-28 17:04:28', 1);
INSERT INTO `pms_category` VALUES (153, '服装服饰', 0, 1, '', 25, 0, '2026-01-28 17:04:35', '2026-03-08 12:09:13', 1);
INSERT INTO `pms_category` VALUES (154, '家居生活', 0, 1, '', 26, 0, '2026-01-28 17:04:45', '2026-03-08 12:09:17', 1);
INSERT INTO `pms_category` VALUES (156, '乳品烘焙', 151, 2, 'http://localhost:9002/aioveu/20260128/3c7bdbf980814de6a8ab83290c1aa7ac.png', 2, 1, '2026-01-28 20:52:53', '2026-01-28 21:05:01', 1);
INSERT INTO `pms_category` VALUES (157, '新鲜水果', 151, 2, 'http://localhost:9002/aioveu/20260128/f99ed8a540b242e88064992b35452579.png', 1, 1, '2026-01-28 20:53:41', '2026-01-28 21:02:07', 1);
INSERT INTO `pms_category` VALUES (158, '海鲜水产', 151, 2, 'http://localhost:9002/aioveu/20260128/cb4a99d26c654ae08caf043519fb1d7e.png', 3, 1, '2026-01-28 21:05:19', '2026-01-28 21:05:19', 1);
INSERT INTO `pms_category` VALUES (159, '热带水果', 157, 3, 'https://minio.aioveu.com/aioveu/20260204/7948fba0e811418f9493741df1d58f21.png', 1, 1, '2026-01-28 21:07:14', '2026-02-04 18:32:33', 1);
INSERT INTO `pms_category` VALUES (160, '浆果类', 157, 3, 'https://minio.aioveu.com/aioveu/20260204/c4be50afd9e94bc9831e21bfd2c8853f.png', 2, 1, '2026-01-28 21:07:37', '2026-02-04 18:31:48', 1);
INSERT INTO `pms_category` VALUES (161, '鲜奶酸奶', 156, 3, 'https://minio.aioveu.com/aioveu/20260204/d9077d7368924a7291aedc58dfb972f1.png', 1, 1, '2026-01-28 21:08:11', '2026-02-04 18:32:57', 1);
INSERT INTO `pms_category` VALUES (162, '面包蛋糕', 156, 3, 'https://minio.aioveu.com/aioveu/20260204/e8e47dbea909455e8ce8dd74c1a2837e.png', 2, 1, '2026-01-28 21:08:20', '2026-02-04 18:32:50', 1);
INSERT INTO `pms_category` VALUES (163, '鲜活水产', 158, 3, 'https://minio.aioveu.com/aioveu/20260204/c2b26a3781cb48ac9aaa1af7d38bae68.png', 1, 1, '2026-01-28 21:08:35', '2026-02-04 18:30:32', 1);
INSERT INTO `pms_category` VALUES (164, '冷冻海鲜', 158, 3, 'https://minio.aioveu.com/aioveu/20260204/f05cef9f4140493f89adb57e7a6613b0.png', 2, 1, '2026-01-28 21:08:49', '2026-02-04 18:30:20', 1);
INSERT INTO `pms_category` VALUES (165, '家居家纺', 154, 2, 'http://localhost:9002/aioveu/20260128/fcd0d707c562479a9719f049f09fe288.png', 1, 1, '2026-01-28 21:24:10', '2026-01-28 21:24:10', 1);
INSERT INTO `pms_category` VALUES (166, '厨具水具', 154, 2, 'http://localhost:9002/aioveu/20260128/1812df8c7e9a40e4a058fef6f7694856.jpg', 2, 1, '2026-01-28 21:24:23', '2026-01-28 21:24:23', 1);
INSERT INTO `pms_category` VALUES (167, '家装建材', 154, 2, 'http://localhost:9002/aioveu/20260128/7fa5604d0bf945f09cc734e3ea1426ac.JPG', 3, 1, '2026-01-28 21:46:38', '2026-01-28 21:46:38', 1);
INSERT INTO `pms_category` VALUES (168, '四件套', 165, 3, 'http://localhost:9002/aioveu/20260128/c492188bb1f54451a14d44a82f1e2aea.JPG', 1, 1, '2026-01-28 21:46:49', '2026-01-28 21:46:49', 1);
INSERT INTO `pms_category` VALUES (169, '夏凉被', 165, 3, 'http://localhost:9002/aioveu/20260128/75adea6e3eae4fdd84eff0c39c5c5969.png', 2, 1, '2026-01-28 21:46:59', '2026-01-28 21:46:59', 1);
INSERT INTO `pms_category` VALUES (170, '保温杯', 166, 3, 'http://localhost:9002/aioveu/20260130/2d19605871f44984a438cecb1724cf2b.png', 1, 1, '2026-01-30 11:44:11', '2026-01-30 11:44:11', 1);
INSERT INTO `pms_category` VALUES (171, '炒锅', 166, 3, 'http://localhost:9002/aioveu/20260130/7b9ef872670c4590ad289d7e55106105.png', 2, 1, '2026-01-30 11:44:28', '2026-01-30 11:44:28', 1);
INSERT INTO `pms_category` VALUES (172, '照明灯具', 167, 3, 'http://localhost:9002/aioveu/20260130/ae19091bae2c4c4e9ee8714303ddafd0.png', 1, 1, '2026-01-30 11:44:38', '2026-01-30 11:44:38', 1);
INSERT INTO `pms_category` VALUES (173, '五金工具', 167, 3, 'http://localhost:9002/aioveu/20260130/4719d080f75048dd9315fc7509773fe1.png', 1, 1, '2026-01-30 11:44:47', '2026-01-30 11:44:47', 1);
INSERT INTO `pms_category` VALUES (174, '女装', 153, 2, 'http://localhost:9002/aioveu/20260130/b30cd351b8854d1d8bafa8b7a5a6df08.png', 1, 1, '2026-01-30 11:46:22', '2026-01-30 11:46:22', 1);
INSERT INTO `pms_category` VALUES (175, '男装', 153, 2, 'http://localhost:9002/aioveu/20260130/4cc4479b7c06497e9c333baa124c5cfb.png', 2, 1, '2026-01-30 11:46:34', '2026-01-30 11:46:34', 1);
INSERT INTO `pms_category` VALUES (176, '鞋靴箱包', 153, 2, 'http://localhost:9002/aioveu/20260130/52edae95866349dfa6ec0177b83f80b9.png', 3, 1, '2026-01-30 11:46:44', '2026-01-30 11:46:44', 1);
INSERT INTO `pms_category` VALUES (177, '连衣裙', 174, 3, 'http://localhost:9002/aioveu/20260130/0726bfaeccee4ff099ff2f6f09cecf8a.png', 1, 1, '2026-01-30 11:46:56', '2026-01-30 11:46:56', 1);
INSERT INTO `pms_category` VALUES (178, '针织衫', 174, 3, 'http://localhost:9002/aioveu/20260130/4ff471edccb844ba997fcef83c7c3805.png', 2, 1, '2026-01-30 11:47:04', '2026-01-30 11:47:04', 1);
INSERT INTO `pms_category` VALUES (179, '衬衫', 175, 3, 'http://localhost:9002/aioveu/20260130/60a9088aa54648e6b39efbb5f15f9b1c.png', 1, 1, '2026-01-30 11:47:13', '2026-01-30 11:47:13', 1);
INSERT INTO `pms_category` VALUES (180, '休闲裤', 175, 3, 'http://localhost:9002/aioveu/20260130/8442c2be325c4a208bbc93c6802e4783.png', 2, 1, '2026-01-30 11:47:21', '2026-01-30 11:47:21', 1);
INSERT INTO `pms_category` VALUES (181, '运动鞋', 176, 3, 'http://localhost:9002/aioveu/20260130/812ce5776aec457b88082b93b7004d07.png', 1, 1, '2026-01-30 11:47:30', '2026-01-30 11:47:30', 1);
INSERT INTO `pms_category` VALUES (182, '双肩包', 176, 3, 'http://localhost:9002/aioveu/20260130/7dd145ec6864445189aef70f2ed1657e.png', 2, 1, '2026-01-30 11:47:38', '2026-01-30 11:47:38', 1);
INSERT INTO `pms_category` VALUES (183, '手机通讯', 152, 2, 'http://localhost:9002/aioveu/20260130/14e474dbbcaf424eb41e3951f7d0c3cb.png', 1, 1, '2026-01-30 11:54:57', '2026-01-30 11:54:57', 1);
INSERT INTO `pms_category` VALUES (184, '厨房电器', 152, 2, 'http://localhost:9002/aioveu/20260130/993c445cffe44e8a838a9a5d7d34d914.png', 2, 1, '2026-01-30 11:55:09', '2026-01-30 11:55:09', 1);
INSERT INTO `pms_category` VALUES (185, '电脑办公', 152, 2, 'http://localhost:9002/aioveu/20260130/03430231e1b14110a576cc965c626c2f.png', 3, 1, '2026-01-30 11:55:19', '2026-01-30 11:55:19', 1);
INSERT INTO `pms_category` VALUES (186, '智能手机', 183, 3, 'https://cdn.aioveu.com/aioveu/1001/image/20260308/625fcfd407cd41f5bf2c414d8de68bd8.png', 1, 1, '2026-01-30 11:55:32', '2026-03-08 12:12:24', 1);
INSERT INTO `pms_category` VALUES (187, '5G手机', 183, 3, 'https://cdn.aioveu.com/aioveu/1001/image/20260308/478e80454ce048159de770b514263c34.png', 2, 1, '2026-01-30 11:55:45', '2026-03-08 12:12:58', 1);
INSERT INTO `pms_category` VALUES (188, '电饭煲', 184, 3, 'http://localhost:9002/aioveu/20260130/f800bd83832843db85c5e876c246facc.png', 1, 1, '2026-01-30 11:55:54', '2026-01-30 11:55:54', 1);
INSERT INTO `pms_category` VALUES (189, '破壁机', 184, 3, 'http://localhost:9002/aioveu/20260130/672e43611af04e7fafb0b79caee44924.png', 2, 1, '2026-01-30 11:56:04', '2026-01-30 11:56:04', 1);
INSERT INTO `pms_category` VALUES (190, '笔记本电脑', 185, 3, 'http://localhost:9002/aioveu/20260130/1687c0d511ff48d5aa8e6ae6d7eab73f.png', 1, 1, '2026-01-30 11:56:17', '2026-01-30 11:56:17', 1);
INSERT INTO `pms_category` VALUES (191, '办公外设', 185, 3, 'http://localhost:9002/aioveu/20260130/d6efb1369e9e45669e5db21e6cdaf7fb.png', 2, 1, '2026-01-30 11:56:28', '2026-01-30 11:56:28', 1);
INSERT INTO `pms_category` VALUES (192, '老人手机', 183, 3, 'https://cdn.aioveu.com/aioveu/1001/image/20260308/1ab62d24b7774e67ab0a2e5e40e0fce3.png', 3, 1, '2026-03-08 12:13:18', '2026-03-08 12:13:18', 1);
INSERT INTO `pms_category` VALUES (193, '拍照手机', 183, 3, 'https://cdn.aioveu.com/aioveu/1001/image/20260308/04c665e3398145739b3e754dca390b43.png', 4, 1, '2026-03-08 12:13:34', '2026-03-08 12:13:34', 1);
INSERT INTO `pms_category` VALUES (194, '游戏手机', 183, 3, 'https://cdn.aioveu.com/aioveu/1001/image/20260308/2e08c7be6a404c5ba19d87106900afc3.png', 5, 1, '2026-03-08 12:13:53', '2026-03-08 12:13:53', 1);

-- ----------------------------
-- Table structure for pms_category_attribute
-- ----------------------------
DROP TABLE IF EXISTS `pms_category_attribute`;
CREATE TABLE `pms_category_attribute`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `category_id` bigint NOT NULL COMMENT '分类ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性名称',
  `type` tinyint NOT NULL COMMENT '类型(1:规格;2:属性;)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pms_attr_pms_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 184 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pms_category_attribute
-- ----------------------------
INSERT INTO `pms_category_attribute` VALUES (34, 5, '颜色', 1, '2021-07-11 17:57:06', '2022-07-01 00:08:19', 1);
INSERT INTO `pms_category_attribute` VALUES (35, 5, '规格', 1, '2021-07-11 18:00:06', '2022-07-01 00:08:19', 1);
INSERT INTO `pms_category_attribute` VALUES (36, 5, '上市时间', 2, '2021-07-11 18:00:08', '2022-06-01 17:41:05', 1);
INSERT INTO `pms_category_attribute` VALUES (183, 106, '颜色', 1, '2026-01-17 16:05:35', '2026-01-17 16:05:35', 1);

-- ----------------------------
-- Table structure for pms_category_brand
-- ----------------------------
DROP TABLE IF EXISTS `pms_category_brand`;
CREATE TABLE `pms_category_brand`  (
  `category_id` bigint NOT NULL,
  `brand_id` bigint NOT NULL,
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`category_id`) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pms_category_brand
-- ----------------------------

-- ----------------------------
-- Table structure for pms_sku
-- ----------------------------
DROP TABLE IF EXISTS `pms_sku`;
CREATE TABLE `pms_sku`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `sku_sn` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品编码',
  `spu_id` bigint NOT NULL COMMENT 'SPU ID',
  `name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品名称',
  `spec_ids` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品规格值，以英文逗号(,)分割',
  `price` bigint NULL DEFAULT NULL COMMENT '商品价格(单位：分)',
  `stock` int UNSIGNED NULL DEFAULT NULL COMMENT '库存数量',
  `locked_stock` int NULL DEFAULT NULL COMMENT '库存锁定数量',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品图片地址',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pms_sku_pms_spu`(`spu_id` ASC) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 834 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品库存表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pms_sku
-- ----------------------------
INSERT INTO `pms_sku` VALUES (1, 'sn001', 1, '黑 6+128g', '1_3', 399900, 990, 150, 'https://www.aioveu.com/files/default/c25b39470474494485633c49101a0f5d.png', '2021-08-08 00:43:26', '2022-07-03 14:16:16', 1);
INSERT INTO `pms_sku` VALUES (2, 'sn002', 1, '黑 8+256g', '1_4', 499900, 999, 0, 'https://www.aioveu.com/files/default/c25b39470474494485633c49101a0f5d.png', '2021-08-08 00:43:26', '2022-07-03 14:16:16', 1);
INSERT INTO `pms_sku` VALUES (3, 'sn003', 1, '蓝 6+128g', '216_3', 399900, 999, 0, 'https://www.aioveu.com/files/default/835d73a337964b9b97e5c7c90acc8cb2.png', '2022-03-05 09:25:53', '2022-07-03 14:16:16', 1);
INSERT INTO `pms_sku` VALUES (4, 'sn004', 1, '蓝 8+256g', '216_4', 499900, 999, 0, 'https://www.aioveu.com/files/default/835d73a337964b9b97e5c7c90acc8cb2.png', '2022-03-05 09:25:53', '2022-07-03 14:16:16', 1);
INSERT INTO `pms_sku` VALUES (5, '10000001', 2, '魔幻青 RTX3060/i7-12700H/165Hz 2.5K屏', '256_258', 1025000, 998, 0, 'http://a.aioveu.com:9000/default/8815c9a46fcc4b1ea952623406750da5.jpg', '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_sku` VALUES (6, '10000002', 2, '魔幻青 RTX3050tTi/12代i5/144Hz高色域屏', '256_259', 925000, 999, 0, 'http://a.aioveu.com:9000/default/8815c9a46fcc4b1ea952623406750da5.jpg', '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_sku` VALUES (7, '10000003', 2, '日蚀灰 RTX3060/i7-12700H/165Hz 2.5K屏', '257_258', 1025000, 999, 0, 'http://a.aioveu.com:9000/default/3210cd1ffb6c4346b743a10855d3cb37.jpg', '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_sku` VALUES (8, '10000004', 2, '日蚀灰 RTX3050tTi/12代i5/144Hz高色域屏', '257_259', 925000, 999, 0, 'http://a.aioveu.com:9000/default/3210cd1ffb6c4346b743a10855d3cb37.jpg', '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_sku` VALUES (9, '111', 3, '16g-512g-【2022款】锐龙六核R5-6600U/核芯显卡/100%sRGB高色域', '841_843_845', 589900, 992, 1, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/78b8efecb753426f81e5dcfcd175495f.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (10, '112', 3, '16g-512g-【2022款】锐龙八核R7-6800U/核芯显卡/100%sRGB高色域', '841_843_846', 629900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/93cbc9dc6fe144f5a59793a6248479a0.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (11, '113', 3, '16g-1t-【2022款】锐龙六核R5-6600U/核芯显卡/100%sRGB高色域', '841_844_845', 639900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/78b8efecb753426f81e5dcfcd175495f.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (12, '114', 3, '16g-1t-【2022款】锐龙八核R7-6800U/核芯显卡/100%sRGB高色域', '841_844_846', 639900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/93cbc9dc6fe144f5a59793a6248479a0.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (13, '115', 3, '32g-512g-【2022款】锐龙六核R5-6600U/核芯显卡/100%sRGB高色域', '842_843_845', 589900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/78b8efecb753426f81e5dcfcd175495f.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (14, '116', 3, '32g-512g-【2022款】锐龙八核R7-6800U/核芯显卡/100%sRGB高色域', '842_843_846', 629900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/93cbc9dc6fe144f5a59793a6248479a0.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (15, '117', 3, '32g-1t-【2022款】锐龙六核R5-6600U/核芯显卡/100%sRGB高色域', '842_844_845', 639900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/78b8efecb753426f81e5dcfcd175495f.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (16, '118', 3, '32g-1t-【2022款】锐龙八核R7-6800U/核芯显卡/100%sRGB高色域', '842_844_846', 639900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/93cbc9dc6fe144f5a59793a6248479a0.jpg', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_sku` VALUES (17, 'sn001', 4, '黑 6+128g', '1_3', 399900, 999, 0, 'https://oss.aioveu.com/aioveu-boot/2023/06/08/6b83dd33eaa248ed8e11cff0003287ee.jpg', '2021-08-08 00:43:26', '2022-07-03 14:16:16', 1);
INSERT INTO `pms_sku` VALUES (765, 'SKU_JLKL_1', 306, '', 'null', 1100, 11, 0, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_sku` VALUES (766, 'SKU_JLKN_2', 306, '', 'null', 1100, 11, 0, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_sku` VALUES (767, 'SKU_JLKL_2', 306, '', 'null', 1100, 11, 0, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_sku` VALUES (768, 'SKU_JLKN_4', 306, '', 'null', 1100, 11, 0, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_sku` VALUES (769, 'SKU_JMJ8_1', 307, '', 'null', 1100, 11, 0, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_sku` VALUES (770, 'SKU_JMJC_2', 307, '', 'null', 1100, 11, 0, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_sku` VALUES (771, 'SKU_JMJ8_2', 307, '', 'null', 1100, 11, 0, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_sku` VALUES (772, 'SKU_JMJC_4', 307, '', 'null', 1100, 11, 0, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_sku` VALUES (777, 'SKU_JN4O_1', 309, '', 'temp_1768734950439_ov0nmkjrc_temp_1768734964360_r9s5uyegl', 1100, 11, 0, '', '2026-01-18 19:16:17', '2026-01-18 19:16:17', 1);
INSERT INTO `pms_sku` VALUES (778, 'SKU_JN4R_2', 309, '', 'temp_1768734950439_ov0nmkjrc_temp_1768734967183_xr4q6tb0u', 1100, 11, 0, '', '2026-01-18 19:16:17', '2026-01-18 19:16:17', 1);
INSERT INTO `pms_sku` VALUES (779, 'SKU_JN4O_2', 309, '', 'temp_1768734953599_r6g1ix4i7_temp_1768734964360_r9s5uyegl', 1100, 11, 0, '', '2026-01-18 19:16:17', '2026-01-18 19:16:17', 1);
INSERT INTO `pms_sku` VALUES (780, 'SKU_JN4R_4', 309, '', 'temp_1768734953599_r6g1ix4i7_temp_1768734967183_xr4q6tb0u', 1100, 11, 0, '', '2026-01-18 19:16:17', '2026-01-18 19:16:17', 1);
INSERT INTO `pms_sku` VALUES (781, 'SKU_JNGW_1', 310, '黑-6+128g-2021-07-17-2021-07-17-黑-6+128g-8+256g-8+256g-黑-6+128g-2021-07-17-2021-07-17-6+128g-6+128g-8+256g-6+128g-黑', 't_e_m_p___1_7_6_8_7_3_5_5_1_8_6_7_2___3_b_t_o_z_4_v_n_4_|_t_e_m_p___1_7_6_8_7_3_5_5_3_3_4_3_9___j_z_d_n_b_m_g_1_y', 1100, 11, 0, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_sku` VALUES (782, 'SKU_JNGY_2', 310, '黑-6+128g-2021-07-17-2021-07-17-黑-6+128g-8+256g-8+256g-黑-6+128g-2021-07-17-2021-07-17-6+128g-黑-8+256g', 't_e_m_p___1_7_6_8_7_3_5_5_1_8_6_7_2___3_b_t_o_z_4_v_n_4_|_t_e_m_p___1_7_6_8_7_3_5_5_3_6_1_2_7___q_u_6_m_f_a_m_4_t', 1100, 11, 0, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_sku` VALUES (783, 'SKU_JNGW_2', 310, '黑-6+128g-2021-07-17-2021-07-17-黑-黑-黑-6+128g-2021-07-17-2021-07-17-6+128g-6+128g-8+256g-6+128g-黑', 't_e_m_p___1_7_6_8_7_3_5_5_2_2_1_2_7___x_x_z_m_f_6_1_d_i_|_t_e_m_p___1_7_6_8_7_3_5_5_3_3_4_3_9___j_z_d_n_b_m_g_1_y', 1100, 11, 0, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_sku` VALUES (784, 'SKU_JNGY_4', 310, '黑-6+128g-2021-07-17-2021-07-17-黑-黑-黑-6+128g-2021-07-17-2021-07-17-6+128g-黑-8+256g', 't_e_m_p___1_7_6_8_7_3_5_5_2_2_1_2_7___x_x_z_m_f_6_1_d_i_|_t_e_m_p___1_7_6_8_7_3_5_5_3_6_1_2_7___q_u_6_m_f_a_m_4_t', 1100, 11, 0, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_sku` VALUES (789, 'SKU_UTEP_1', 312, '黑色-64', '906_908', 1100, 1111, NULL, 'http://localhost:9002/aioveu/20260126/3f582fcc379b442c88d4b41c53d8003e.JPG', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_sku` VALUES (790, 'SKU_UTET_2', 312, '黑色-128', '906_909', 1100, 1111, NULL, 'http://localhost:9002/aioveu/20260126/3f582fcc379b442c88d4b41c53d8003e.JPG', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_sku` VALUES (791, 'SKU_UTEX_3', 312, '黑色-256', '906_910', 1100, 1111, NULL, 'http://localhost:9002/aioveu/20260126/3f582fcc379b442c88d4b41c53d8003e.JPG', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_sku` VALUES (792, 'SKU_UTEP_2', 312, '白色-64', '907_908', 1100, 1111, NULL, 'http://localhost:9002/aioveu/20260126/d0e6d9e95c134a3a821087986e9f5385.png', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_sku` VALUES (793, 'SKU_UTET_4', 312, '白色-128', '907_909', 1100, 1111, NULL, '', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_sku` VALUES (794, 'SKU_UTEX_6', 312, '白色-256', '907_910', 1100, 1111, 7, '', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_sku` VALUES (795, 'SKU_V9AJ_1', 311, '红色-128', '900_902', 22200, 109, NULL, 'http://localhost:9002/aioveu/20260126/73ec8373454e471aae7436eae0fcc6fd.JPG', '2026-01-26 22:22:06', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_sku` VALUES (796, 'SKU_V9AJ_2', 311, '红色-256', '900_903', 11100, 111, NULL, 'http://localhost:9002/aioveu/20260126/a5e79ed85dc34be081f1e8769415e2fc.jpg', '2026-01-26 22:22:06', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_sku` VALUES (797, 'SKU_V9AJ_3', 311, '黑色-128', '901_902', 11100, 111, NULL, 'http://localhost:9002/aioveu/20260127/57962dd8374048febc7d3552c2c59a21.png', '2026-01-26 22:22:06', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_sku` VALUES (798, 'SKU_V9AJ_4', 311, '黑色-256', '901_903', 11100, 111, NULL, 'http://localhost:9002/aioveu/20260127/36f76ed241aa49d991a14f7d7d779aaa.jpg', '2026-01-26 22:22:06', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_sku` VALUES (799, 'SKU_WBCC_1', 304, '22', '911', 0, 0, NULL, 'http://localhost:9002/aioveu/20260127/6238c43aec9748e1874592fba8c65f73.jpg', '2026-01-27 16:07:15', '2026-01-27 18:21:36', 1);
INSERT INTO `pms_sku` VALUES (800, 'SKU_WC4T_1', 305, '黑色-11', '870_872', 0, 0, NULL, 'http://localhost:9002/aioveu/20260127/120e1d3f7edf47f894654c1708cec88a.JPG', '2026-01-27 16:29:30', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_sku` VALUES (801, 'SKU_WC4T_2', 305, '黑色-22', '870_873', 0, 0, NULL, 'http://localhost:9002/aioveu/20260127/43c5b86d82794066bea2ed4756fe76d3.JPG', '2026-01-27 16:29:30', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_sku` VALUES (802, 'SKU_WC4T_3', 305, '白色-11', '871_872', 0, 0, NULL, '', '2026-01-27 16:29:30', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_sku` VALUES (803, 'SKU_WC4T_4', 305, '白色-22', '871_873', 0, 0, NULL, '', '2026-01-27 16:29:30', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_sku` VALUES (804, 'SKU_WGPM_1', 308, '白色-128', '885_887', 0, 0, NULL, 'http://localhost:9002/aioveu/20260127/2fe4ccd33793400d80c873b72760c9aa.png', '2026-01-27 18:37:24', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_sku` VALUES (805, 'SKU_WGPM_2', 308, '白色-256', '885_888', 0, 0, NULL, 'http://localhost:9002/aioveu/20260127/2eba26152f9d43ef94c0438eda0b3920.png', '2026-01-27 18:37:24', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_sku` VALUES (806, 'SKU_WGPM_3', 308, '黑色-128', '886_887', 0, 0, NULL, '', '2026-01-27 18:37:24', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_sku` VALUES (807, 'SKU_WGPM_4', 308, '黑色-256', '886_888', 0, 0, NULL, '', '2026-01-27 18:37:24', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_sku` VALUES (808, 'SKU_0OKE_1', 313, '4-5斤', '913', 12800, 10000, 486, 'https://minio.aioveu.com/aioveu/20260204/adc5bb3e54364cabae457fd4f55c16a4.png', '2026-01-30 17:29:35', '2026-02-04 18:33:52', 1);
INSERT INTO `pms_sku` VALUES (809, 'SKU_0OKI_2', 313, '6-10斤', '914', 25600, 10000, NULL, 'https://minio.aioveu.com/aioveu/20260204/b6e84066fc924ac4ac4b392b74068707.png', '2026-01-30 17:29:35', '2026-02-04 18:33:52', 1);
INSERT INTO `pms_sku` VALUES (822, 'SKU_H8ZQ_1', 314, '金色-128GB', '916_920', 10, 100, 57, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/e9dfcc6988f3409d9c1907a62745dfab.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (823, 'SKU_H8ZQ_2', 314, '金色-256GB', '916_921', 10, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/48ffabe556914d06a472d4ff4b3282c5.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (824, 'SKU_H8ZQ_3', 314, '金色-512GB', '916_922', 300000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/d4050523b1a049aa85a6c2aa68994ce8.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (825, 'SKU_H8ZQ_4', 314, '银色-128GB', '917_920', 220000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/94a0f2d51d7b42a5a3d57a7931d0e3bb.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (826, 'SKU_H8ZQ_5', 314, '银色-256GB', '917_921', 250000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/02f392d45ab4440483f75e084a9de799.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (827, 'SKU_H8ZQ_6', 314, '银色-512GB', '917_922', 300000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/a431946932684eeea20769411aa8a856.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (828, 'SKU_H8ZQ_7', 314, '深空黑色-128GB', '918_920', 220000, 100, 4, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/c909b9ab4e4a4853a1b0631710f89a21.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (829, 'SKU_H8ZQ_8', 314, '深空黑色-256GB', '918_921', 250000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/9795d808e68b48fe81b288499e8c4adf.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (830, 'SKU_H8ZQ_9', 314, '深空黑色-512GB', '918_922', 300000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/5ae74fd748e6436dbbd9e1b50e015155.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (831, 'SKU_H8ZQ_10', 314, '暗紫色-128GB', '919_920', 220000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/6c7db88940d7419e881e734bda81f142.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (832, 'SKU_H8ZQ_11', 314, '暗紫色-256GB', '919_921', 250000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/0d86d4ef8634409cad3bd6832c712e5d.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_sku` VALUES (833, 'SKU_H8ZQ_12', 314, '暗紫色-512GB', '919_922', 300000, 100, NULL, 'https://cdn.aioveu.com/aioveu/1001/image/20260310/ca9640ef19404182890d332e8334fad2.png', '2026-03-08 12:26:39', '2026-03-10 18:54:31', 1);

-- ----------------------------
-- Table structure for pms_spu
-- ----------------------------
DROP TABLE IF EXISTS `pms_spu`;
CREATE TABLE `pms_spu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '商品名称',
  `category_id` bigint NOT NULL COMMENT '商品类型ID',
  `brand_id` bigint NULL DEFAULT NULL COMMENT '商品品牌ID',
  `origin_price` bigint NOT NULL COMMENT '原价【起】',
  `price` bigint NOT NULL COMMENT '现价【起】',
  `sales` int NULL DEFAULT 0 COMMENT '销量',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品主图',
  `album` json NULL COMMENT '商品图册',
  `unit` varchar(16) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '单位',
  `description` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '商品简介',
  `detail` text CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL COMMENT '商品详情',
  `status` tinyint NULL DEFAULT 1 COMMENT '商品状态(0:下架 1:上架)',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pms_spu_pms_brand`(`brand_id` ASC) USING BTREE,
  INDEX `fk_pms_spu_pms_category`(`category_id` ASC) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 315 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pms_spu
-- ----------------------------
INSERT INTO `pms_spu` VALUES (1, 'Galaxy Z Fold5', 5, 10, 599900, 599900, 1, 'https://shop-image.samsung.com.cn/productv5/img/2023/07/26/64c0e30fe4b08db29258c50c.png', '[\"http://localhost:9002/aioveu/20260115/9536518906434a0abd9ffcdf308e77a1.JPG\", \"http://localhost:9002/aioveu/20260115/91c929bfb0f147ab8419abda5a124124.jpg\"]', '台', '好快,好稳,\n好一次强上加强。\n高通全新一代芯片赋能，速度大幅提升。\n三大专业主摄影像加持，能力全面进化。\n大师级设计理念新诠释，质感简而不凡。\n斩获十五项纪录旗舰屏，感官万般出众。', '<p><img src=\"https://shop-image.samsung.com.cn/productv5/img/2023/11/03/65449d3ee4b08db20823bcbe.jpg\" alt=\"\" data-href=\"\" style=\"width: 449.00px;height: 449.00px;\"/></p>', 1, NULL, '2026-01-15 21:22:09', 1);
INSERT INTO `pms_spu` VALUES (2, '华硕天选3', 101, 11, 1145000, 929900, 0, 'http://localhost:9002/aioveu/20260115/8887d0340553460384db39821eb497d7.png', '[\"https://www.aioveu.com/files/default/3edd01c723ff456384cea9bd3c9b19e7.jpg\", \"http://localhost:9002/aioveu/20260115/49de1ec011234185850f12d527a4581e.jpg\", \"https://www.aioveu.com/files/default/97458ae9ea734bc498724660abb1c6cd.jpg\", \"https://www.aioveu.com/files/default/501b0e6dcb3f4d69b7e40e90b3d3ac32.jpg\"]', NULL, '中国台湾华硕电脑股份有限公司 [1]  是当前全球第一大主板生产商、全球第三大显卡生产商，同时也是全球领先的3C解决方案提供商之一，致力于为个人和企业用户提供最具创新价值的产品及应用方案。华硕的产品线完整覆盖至笔记本电脑、主板、显卡、服务器、光存储、有线/无线网络通讯产品、LCD、掌上电脑、智能手机等全线3C产品。其中显卡和主板以及笔记本电脑三大产品已经成为华硕的主要竞争实力。', '<p><img src=\"http://a.aioveu.com:9000/default/5e4fb81b04244a74aacaabb4685101e2.png\" alt=\"\" data-href=\"\" style=\"\"/><img src=\"http://a.aioveu.com:9000/default/0744c5b6d77b47b294eb111ee992c62b.png\" alt=\"\" data-href=\"\" style=\"\"/></p>', 1, '2022-03-11 14:39:21', '2026-01-15 20:59:39', 1);
INSERT INTO `pms_spu` VALUES (3, '惠普战X ', 99, 33, 639900, 629900, 0, 'http://localhost:9002/aioveu/20260115/db78ac09957c4442969d58f2b38636b5.JPG', '[\"http://localhost:9002/aioveu/20260115/f775a513730444d8846cfeb4b6254cb2.png\", \"http://localhost:9002/aioveu/20260115/a779e9c4eb9e464aa8d604279820113a.jpg\"]', NULL, '【2022新款】HP/惠普战X 16英寸锐龙新款6000系列R5六核/R7八核高性能学生家用轻薄办公商用笔记本电脑\n六核/八核处理器，高性能集成显卡', '<p><img src=\"https://oss.aioveu.com/default/d645a6f642794e2183cc44d340613b9d.jpg\" alt=\"\" data-href=\"\" width=\"\" height=\"\" style=\"\"/></p>', 1, '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu` VALUES (4, '小米13', 5, 10, 599900, 599900, 1, 'http://localhost:9002/aioveu/20260115/0bad98c955aa452dac806353c0a19109.png', '[\"https://oss.aioveu.com/aioveu-boot/2023/06/08/6b83dd33eaa248ed8e11cff0003287ee.jpg\"]', '台', '好快,好稳,\n好一次强上加强。\n高通全新一代芯片赋能，速度大幅提升。\n三大专业主摄影像加持，能力全面进化。\n大师级设计理念新诠释，质感简而不凡。\n斩获十五项纪录旗舰屏，感官万般出众。', '<p><img src=\"http://a.aioveu.com:9000/default/1a69357664c24962ac23953905c3c38f.png\" alt=\"\" data-href=\"\" style=\"width: 449.00px;height: 449.00px;\"/></p>', 1, NULL, '2026-01-15 20:40:36', 1);
INSERT INTO `pms_spu` VALUES (288, '我爱你', 1, 1, 111, 11, 0, 'http://localhost:9002/aioveu/20260115/61773013a47a4f2d8a03d61263258631.png', NULL, NULL, NULL, NULL, 1, '2026-01-15 19:54:44', '2026-01-15 21:04:11', 1);
INSERT INTO `pms_spu` VALUES (289, '三星外星人', 99, 20, 11, 11, 0, 'http://localhost:9002/aioveu/20260117/6fcec28a1658434a8f7c322c400f3c0b.png', '[]', NULL, '三星外星人', '<p>三星外星人</p>', 1, '2026-01-17 19:58:19', '2026-01-17 19:58:19', 1);
INSERT INTO `pms_spu` VALUES (290, '廉颇饭否', 127, 10, 111, 11, 0, 'http://localhost:9002/aioveu/20260117/5d55cb2221dc4fc59040d2116199948a.png', '[]', NULL, '廉颇饭否', '<p>廉颇饭否</p>', 1, '2026-01-17 20:03:48', '2026-01-17 20:03:48', 1);
INSERT INTO `pms_spu` VALUES (304, '阿卡丽最美', 107, 1, 11100, 8800, 333, 'http://localhost:9002/aioveu/20260117/600d6cd4fff1409cb248166ff15382b9.png', '[\"http://localhost:9002/aioveu/20260117/b57f63a2d5424cca825adeac57464e05.png\", \"http://localhost:9002/aioveu/20260117/f41108bcf1a743fab3d93c9b61cacc7a.png\"]', NULL, '阿卡丽最美', '<p>阿卡丽最美</p>', 1, '2026-01-17 22:39:56', '2026-01-27 18:21:36', 1);
INSERT INTO `pms_spu` VALUES (305, '阿卡丽女王', 107, 10, 1100, 1100, 0, 'http://localhost:9002/aioveu/20260118/806c2e1ca5e2470b85f05ca2126edf19.JPG', '[\"http://localhost:9002/aioveu/20260118/457484ebca444af185bcadeecf2c8a9b.png\", \"http://localhost:9002/aioveu/20260118/4c7730fb9c274f748eeb9699a4e64e9e.png\"]', NULL, '阿卡丽女王', '<p>阿卡丽女王11</p>', 1, '2026-01-18 18:01:29', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_spu` VALUES (306, '蛇女', 111, 1, 111, 11, 0, 'http://localhost:9002/aioveu/20260118/747a330e1d8c406382bb8ff1262ba056.jpg', '[\"http://localhost:9002/aioveu/20260118/e4f102c8daec48f7aef4dd6939448970.png\"]', NULL, '蛇女', '<p>蛇女</p>', 1, '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_spu` VALUES (307, '乐芙兰开心', 110, 10, 11, 11, 0, 'http://localhost:9002/aioveu/20260118/021fce8e6b6e4d598cc52e37b29e93f3.jpg', '[\"http://localhost:9002/aioveu/20260118/466e0488675c4f62b6af10fbb13789d1.png\"]', NULL, '乐芙兰开心', '<p>乐芙兰开心</p>', 1, '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_spu` VALUES (308, '李白诗仙', 134, 10, 99900, 99900, 0, 'http://localhost:9002/aioveu/20260118/39f04902feca42ebbe5a30860da6fb63.JPG', '[]', NULL, '李白诗仙', '<p>李白诗仙</p>', 1, '2026-01-18 19:08:02', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_spu` VALUES (309, '兰陵王刺客', 133, 10, 11, 11, 0, 'http://localhost:9002/aioveu/20260118/e2e1437b456d483d94b654b31d499794.jpg', '[\"http://localhost:9002/aioveu/20260118/eb2d1ed8f65f430581bf327c8e80f117.png\"]', NULL, '兰陵王刺客', '<p>兰陵王刺客</p>', 1, '2026-01-18 19:16:16', '2026-01-18 19:16:16', 1);
INSERT INTO `pms_spu` VALUES (310, '美猴王大脑', 135, 10, 11, 11, 0, 'http://localhost:9002/aioveu/20260118/0cfef37d51bb4bed96738480b0b7dc3d.png', '[\"http://localhost:9002/aioveu/20260118/9a8d29941c0c4540b215fe3bc279e33c.jpg\"]', NULL, '美猴王大脑', '<p>美猴王大脑</p>', 1, '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_spu` VALUES (311, '程咬金大斧', 129, 10, 122300, 11100, 0, 'http://localhost:9002/aioveu/20260118/e0d8bf0ed992492f9427ea78419f34e4.png', '[\"http://localhost:9002/aioveu/20260118/cfa650eedc224fc7992f291add63458a.png\", \"http://localhost:9002/aioveu/20260118/c1f811caf9cf4524aac50b329a7a3f67.png\"]', NULL, '程咬金大斧', '<p>程咬金大斧11133</p>', 1, '2026-01-18 19:32:56', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_spu` VALUES (312, '艾克1号', 138, 10, 0, 0, 0, 'http://localhost:9002/aioveu/20260126/13ab65c95bb94ad6b534e79c8b083087.JPG', '[\"http://localhost:9002/aioveu/20260126/2457d84cc2474522a707433adafae059.jpg\", \"http://localhost:9002/aioveu/20260126/02f21b4c10c74ba696bf119f2b829906.png\"]', NULL, '艾克1号', '<p>艾克1号</p>', 1, '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu` VALUES (313, '泰国金枕头榴莲', 159, 10, 12800, 12800, 0, 'https://minio.aioveu.com/aioveu/20260204/8c576768795e462ba14e9cb800172153.png', '[\"https://minio.aioveu.com/aioveu/20260204/74764f3cfec24b74aba8a4b1c46cd131.png\", \"https://minio.aioveu.com/aioveu/20260204/a24f632a08c246a3a0faa895d798b595.png\", \"https://minio.aioveu.com/aioveu/20260204/d7bf3ee39ff649de92c853bd509e79d1.png\"]', NULL, '泰国金枕头榴莲', '<p><span style=\"color: rgb(51, 51, 51); background-color: rgb(255, 255, 255); font-size: 16px; font-family: &quot;Open Sans&quot;, &quot;Clear Sans&quot;, &quot;Helvetica Neue&quot;, Helvetica, Arial, &quot;Segoe UI Emoji&quot;, sans-serif;\">泰国金枕头榴莲</span></p>', 1, '2026-01-30 17:29:35', '2026-02-04 18:33:52', 1);
INSERT INTO `pms_spu` VALUES (314, 'Apple/苹果iPhone14ProMax手机14国行正品苹果14Pro双卡5G全网通', 186, 34, 235000, 220000, 0, 'https://cdn.aioveu.com/aioveu/1001/image/20260308/930d6311fa7f4437ba7c42ef874b1b27.png', '[\"https://cdn.aioveu.com/aioveu/1001/image/20260308/4d05276c25fb4f3fb8d8f27b6c033b0c.png\", \"https://cdn.aioveu.com/aioveu/1001/image/20260308/c5a9fb287ce0439393eabe7d589d29e7.png\", \"https://cdn.aioveu.com/aioveu/1001/image/20260308/2c9a2b36ebd7434aabd2d496b002ec37.png\"]', NULL, 'Apple/苹果iPhone14ProMax手机14国行正品苹果14Pro双卡5G全网通', '<p><span style=\"color: rgba(9, 9, 10, 0.92); background-color: rgb(255, 255, 255); font-size: 20px; font-family: &quot;PingFang SC&quot;;\">Apple/苹果iPhone14ProMax手机14国行正品苹果14Pro双卡5G全网通</span></p>', 1, '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);

-- ----------------------------
-- Table structure for pms_spu_attribute
-- ----------------------------
DROP TABLE IF EXISTS `pms_spu_attribute`;
CREATE TABLE `pms_spu_attribute`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  `spu_id` bigint NOT NULL COMMENT '产品ID',
  `attribute_id` bigint NULL DEFAULT NULL COMMENT '属性ID',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性名称',
  `value` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '属性值',
  `type` tinyint NOT NULL COMMENT '类型(1:规格;2:属性;)',
  `pic_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '规格图片',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `tenant_id` bigint NOT NULL COMMENT '租户ID',
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `fk_pms_spu_attribute_pms_attr`(`name` ASC) USING BTREE,
  INDEX `fk_pms_spu_attribute_pms_spu`(`spu_id` ASC) USING BTREE,
  INDEX `idx_tenant`(`tenant_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 923 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '商品属性/规格表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of pms_spu_attribute
-- ----------------------------
INSERT INTO `pms_spu_attribute` VALUES (1, 1, 34, '颜色', '黑', 1, 'https://www.aioveu.com/files/default/c25b39470474494485633c49101a0f5d.png', NULL, '2022-07-03 14:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (3, 1, 35, '规格', '6+128g', 1, NULL, NULL, '2022-07-03 14:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (4, 1, 35, '规格', '8+256g', 1, NULL, NULL, '2022-07-03 14:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (5, 1, 36, '上市时间', '2021-07-17', 2, NULL, NULL, '2022-07-03 14:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (216, 1, NULL, '颜色', '蓝', 1, 'https://www.aioveu.com/files/default/835d73a337964b9b97e5c7c90acc8cb2.png', '2022-03-05 09:25:53', '2022-07-03 14:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (251, 2, NULL, '上市时间', '2022/3/11', 2, NULL, '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (252, 2, NULL, '商品名称', '华硕天选3', 2, NULL, '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (253, 2, NULL, '商品编号', '100032610338', 2, NULL, '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (254, 2, NULL, '商品毛重', '4.05kg', 2, NULL, '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (255, 2, NULL, '系统', 'windows11', 2, NULL, '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (256, 2, NULL, '颜色', '魔幻青', 1, 'http://a.aioveu.com:9000/default/8815c9a46fcc4b1ea952623406750da5.jpg', '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (257, 2, NULL, '颜色', '日蚀灰', 1, 'http://a.aioveu.com:9000/default/3210cd1ffb6c4346b743a10855d3cb37.jpg', '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (258, 2, NULL, '规格', 'RTX3060/i7-12700H/165Hz 2.5K屏', 1, NULL, '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (259, 2, NULL, '规格', 'RTX3050tTi/12代i5/144Hz高色域屏', 1, NULL, '2022-03-11 14:39:21', '2022-07-08 00:29:56', 1);
INSERT INTO `pms_spu_attribute` VALUES (838, 3, NULL, '内存', '16g 32g', 2, NULL, '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (839, 3, NULL, '重量', '1.5kg(含)-2kg(不含)', 2, NULL, '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (840, 3, NULL, '显卡类型', '核芯显卡', 2, NULL, '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (841, 3, NULL, '内存容量', '16g', 1, '', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (842, 3, NULL, '内存容量', '32g', 1, '', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (843, 3, NULL, '硬盘容量', '512g', 1, '', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (844, 3, NULL, '硬盘容量', '1t', 1, '', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (845, 3, NULL, '套餐类型', '【2022款】锐龙六核R5-6600U/核芯显卡/100%sRGB高色域', 1, '', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (846, 3, NULL, '套餐类型', '【2022款】锐龙八核R7-6800U/核芯显卡/100%sRGB高色域', 1, '', '2022-07-07 00:22:13', '2026-01-30 17:07:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (866, 304, NULL, '材质', '纯棉22', 2, NULL, '2026-01-17 22:39:56', '2026-01-27 18:21:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (867, 304, NULL, '适用季节', '春秋11', 2, NULL, '2026-01-17 22:39:56', '2026-01-27 18:21:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (868, 305, NULL, '阿卡丽女王', '11', 2, NULL, '2026-01-18 18:01:29', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_spu_attribute` VALUES (869, 305, NULL, '阿卡丽女王2', '22', 2, NULL, '2026-01-18 18:01:29', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_spu_attribute` VALUES (870, 305, NULL, '阿卡丽女王', '黑色', 1, '', '2026-01-18 18:01:29', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_spu_attribute` VALUES (871, 305, NULL, '阿卡丽女王', '白色', 1, '', '2026-01-18 18:01:29', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_spu_attribute` VALUES (872, 305, NULL, '内村', '11', 1, '', '2026-01-18 18:01:29', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_spu_attribute` VALUES (873, 305, NULL, '内村', '22', 1, '', '2026-01-18 18:01:29', '2026-01-27 16:35:25', 1);
INSERT INTO `pms_spu_attribute` VALUES (874, 306, NULL, '蛇女动物', '蛇', 2, NULL, '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_spu_attribute` VALUES (875, 306, NULL, '蛇女颜色', '红色', 1, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_spu_attribute` VALUES (876, 306, NULL, '蛇女颜色', '白色', 1, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_spu_attribute` VALUES (877, 306, NULL, '蛇女内存', '11', 1, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_spu_attribute` VALUES (878, 306, NULL, '蛇女内存', '22', 1, '', '2026-01-18 18:32:38', '2026-01-18 18:32:38', 1);
INSERT INTO `pms_spu_attribute` VALUES (879, 307, NULL, '乐芙兰开心材质', '大美女', 2, NULL, '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (880, 307, NULL, '乐芙兰开心颜色', '黑色', 1, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (881, 307, NULL, '乐芙兰开心颜色', '白色', 1, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (882, 307, NULL, '乐芙兰开心内存', '126', 1, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (883, 307, NULL, '乐芙兰开心内存', '256', 1, '', '2026-01-18 18:59:36', '2026-01-18 18:59:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (884, 308, NULL, '李白诗仙', '李白诗仙999', 2, NULL, '2026-01-18 19:08:02', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_spu_attribute` VALUES (885, 308, NULL, '李白诗仙1', '白色', 1, '', '2026-01-18 19:08:02', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_spu_attribute` VALUES (886, 308, NULL, '李白诗仙1', '黑色', 1, '', '2026-01-18 19:08:02', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_spu_attribute` VALUES (887, 308, NULL, '李白诗仙2', '128', 1, '', '2026-01-18 19:08:02', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_spu_attribute` VALUES (888, 308, NULL, '李白诗仙2', '256', 1, '', '2026-01-18 19:08:02', '2026-01-27 18:43:07', 1);
INSERT INTO `pms_spu_attribute` VALUES (889, 309, NULL, '兰陵王刺客', '春天', 2, NULL, '2026-01-18 19:16:16', '2026-01-18 19:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (890, 309, NULL, '兰陵王刺客颜色', '白色', 1, '', '2026-01-18 19:16:16', '2026-01-18 19:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (891, 309, NULL, '兰陵王刺客颜色', '黑色', 1, '', '2026-01-18 19:16:16', '2026-01-18 19:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (892, 309, NULL, '兰陵王刺客内存', '128', 1, '', '2026-01-18 19:16:16', '2026-01-18 19:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (893, 309, NULL, '兰陵王刺客内存', '256', 1, '', '2026-01-18 19:16:16', '2026-01-18 19:16:16', 1);
INSERT INTO `pms_spu_attribute` VALUES (894, 310, NULL, '美猴王大脑', '秋季', 2, NULL, '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_spu_attribute` VALUES (895, 310, NULL, '美猴王大脑颜色', '秋季', 1, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_spu_attribute` VALUES (896, 310, NULL, '美猴王大脑颜色', '白色', 1, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_spu_attribute` VALUES (897, 310, NULL, '美猴王大脑内存', '128', 1, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_spu_attribute` VALUES (898, 310, NULL, '美猴王大脑内存', '256', 1, '', '2026-01-18 19:25:44', '2026-01-18 19:25:44', 1);
INSERT INTO `pms_spu_attribute` VALUES (899, 311, NULL, '程咬金大斧', '冬季111334455', 2, NULL, '2026-01-18 19:32:56', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (900, 311, NULL, '程咬金大斧颜色', '红色', 1, '', '2026-01-18 19:32:56', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (901, 311, NULL, '程咬金大斧颜色', '黑色', 1, '', '2026-01-18 19:32:56', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (902, 311, NULL, '程咬金大斧内存', '128', 1, '', '2026-01-18 19:32:56', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (903, 311, NULL, '程咬金大斧内存', '256', 1, '', '2026-01-18 19:32:56', '2026-01-27 15:54:10', 1);
INSERT INTO `pms_spu_attribute` VALUES (904, 312, NULL, '艾克1号', '春天', 2, NULL, '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu_attribute` VALUES (905, 312, NULL, '艾克1号材质', '爱', 2, NULL, '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu_attribute` VALUES (906, 312, NULL, '颜色', '黑色', 1, 'http://localhost:9002/aioveu/20260126/3f582fcc379b442c88d4b41c53d8003e.JPG', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu_attribute` VALUES (907, 312, NULL, '颜色', '白色', 1, '', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu_attribute` VALUES (908, 312, NULL, '内存', '64', 1, '', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu_attribute` VALUES (909, 312, NULL, '内存', '128', 1, '', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu_attribute` VALUES (910, 312, NULL, '内存', '256', 1, '', '2026-01-26 14:57:47', '2026-01-26 23:01:58', 1);
INSERT INTO `pms_spu_attribute` VALUES (911, 304, NULL, '11', '22', 1, '', '2026-01-27 16:07:15', '2026-01-27 18:21:36', 1);
INSERT INTO `pms_spu_attribute` VALUES (912, 313, NULL, '爆款', '进口', 2, NULL, '2026-01-30 17:29:35', '2026-02-04 18:33:52', 1);
INSERT INTO `pms_spu_attribute` VALUES (913, 313, NULL, '重量', '4-5斤', 1, '', '2026-01-30 17:29:35', '2026-02-04 18:33:52', 1);
INSERT INTO `pms_spu_attribute` VALUES (914, 313, NULL, '重量', '6-10斤', 1, '', '2026-01-30 17:29:35', '2026-02-04 18:33:52', 1);
INSERT INTO `pms_spu_attribute` VALUES (915, 314, NULL, '网络类型', '5G全网通', 2, NULL, '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_spu_attribute` VALUES (916, 314, NULL, '机身颜色', '金色', 1, '', '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_spu_attribute` VALUES (917, 314, NULL, '机身颜色', '银色', 1, '', '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_spu_attribute` VALUES (918, 314, NULL, '机身颜色', '深空黑色', 1, '', '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_spu_attribute` VALUES (919, 314, NULL, '机身颜色', '暗紫色', 1, '', '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_spu_attribute` VALUES (920, 314, NULL, '存储容量', '128GB', 1, '', '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_spu_attribute` VALUES (921, 314, NULL, '存储容量', '256GB', 1, '', '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);
INSERT INTO `pms_spu_attribute` VALUES (922, 314, NULL, '存储容量', '512GB', 1, '', '2026-03-08 12:22:31', '2026-03-10 18:54:31', 1);

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT 'branch transaction id',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'global transaction id',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'undo_log context,such as serialization',
  `rollback_info` longblob NOT NULL COMMENT 'rollback info',
  `log_status` int NOT NULL COMMENT '0:normal status,1:defense status',
  `log_created` datetime(6) NOT NULL COMMENT 'create datetime',
  `log_modified` datetime(6) NOT NULL COMMENT 'modify datetime',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;
