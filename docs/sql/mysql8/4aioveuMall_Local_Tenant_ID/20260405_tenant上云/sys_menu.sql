/*
 Navicat Premium Data Transfer

 Source Server         : aioveu-tenant_Cloud_98_3309
 Source Server Type    : MySQL
 Source Server Version : 80029
 Source Host           : 139.224.209.98:3309
 Source Schema         : aioveu_tenant

 Target Server Type    : MySQL
 Target Server Version : 80029
 File Encoding         : 65001

 Date: 05/04/2026 14:06:11
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for sys_menu
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `parent_id` bigint NOT NULL COMMENT '父菜单ID',
  `tree_path` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '父节点ID路径',
  `name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单名称',
  `type` char(1) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '菜单类型（C-目录 M-菜单 B-按钮）',
  `route_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由名称（Vue Router 中用于命名路由）',
  `route_path` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由路径（Vue Router 中定义的 URL 路径）',
  `component` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '组件路径（组件页面完整路径，相对于 src/views/，缺省后缀 .vue）',
  `perm` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '【按钮】权限标识',
  `always_show` tinyint NULL DEFAULT 0 COMMENT '【目录】只有一个子路由是否始终显示（1-是 0-否）',
  `keep_alive` tinyint NULL DEFAULT 0 COMMENT '【菜单】是否开启页面缓存（1-是 0-否）',
  `visible` tinyint(1) NULL DEFAULT 1 COMMENT '显示状态（1-显示 0-隐藏）',
  `sort` int NULL DEFAULT 0 COMMENT '排序',
  `icon` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '菜单图标',
  `redirect` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '跳转路径',
  `create_time` datetime NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime NULL DEFAULT NULL COMMENT '更新时间',
  `params` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL COMMENT '路由参数',
  `scope` tinyint(1) NOT NULL DEFAULT 2 COMMENT '菜单范围(1=平台菜单 2=业务菜单)',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3068 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '系统菜单表' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES (1, 0, '0', '平台管理', 'C', '', '/platform', 'Layout', NULL, NULL, NULL, 1, 1, 'el-icon-Platform', '/platform/tenant', '2026-02-20 18:08:50', '2026-02-20 18:08:50', NULL, 1);
INSERT INTO `sys_menu` VALUES (2, 0, '0', '系统管理', 'C', '', '/system', 'Layout', NULL, NULL, NULL, 1, 31, 'system', '/system/user', '2026-02-20 18:08:51', '2026-03-13 15:40:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (3, 0, '0', '代码生成', 'C', '', '/codegen', 'Layout', NULL, NULL, NULL, 1, 33, 'code', '/codegen/index', '2026-02-20 18:08:51', '2026-03-13 15:40:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (5, 0, '0', '平台文档', 'C', '', '/doc', 'Layout', NULL, NULL, NULL, 1, 35, 'document', '', '2026-02-20 18:08:51', '2026-03-13 15:41:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (6, 0, '0', '接口文档', 'C', '', '/api', 'Layout', NULL, NULL, NULL, 1, 36, 'api', '', '2026-02-20 18:08:51', '2026-03-13 15:41:11', NULL, 2);
INSERT INTO `sys_menu` VALUES (7, 0, '0', '组件封装', 'C', '', '/component', 'Layout', NULL, NULL, NULL, 1, 37, 'menu', '', '2026-02-20 18:08:51', '2026-03-13 15:41:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (8, 0, '0', '功能演示', 'C', '', '/function', 'Layout', NULL, NULL, NULL, 1, 38, 'menu', '', '2026-02-20 18:08:51', '2026-03-13 15:41:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (9, 0, '0', '多级菜单', 'C', NULL, '/multi-level', 'Layout', NULL, 1, NULL, 1, 39, 'cascader', '', '2026-02-20 18:08:51', '2026-03-13 15:41:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (10, 0, '0', '路由参数', 'C', '', '/route-param', 'Layout', NULL, NULL, NULL, 1, 40, 'el-icon-ElementPlus', '', '2026-02-20 18:08:52', '2026-03-13 15:41:36', NULL, 2);
INSERT INTO `sys_menu` VALUES (110, 1, '0,1', '租户管理', 'M', 'Tenant', 'tenant', 'system/tenant/index', NULL, NULL, 1, 1, 1, 'el-icon-OfficeBuilding', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (120, 1, '0,1', '租户套餐', 'M', 'TenantPlan', 'tenant-plan', 'system/tenant/plan', NULL, NULL, 1, 1, 2, 'el-icon-CollectionTag', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (210, 2, '0,2', '用户管理', 'M', 'User', 'user', 'system/user/index', NULL, NULL, 1, 1, 1, 'el-icon-User', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (220, 2, '0,2', '角色管理', 'M', 'Role', 'role', 'system/role/index', NULL, NULL, 1, 1, 2, 'role', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (230, 1, '0,1', '菜单管理', 'M', 'SysMenu', 'menu', 'system/menu/index', NULL, NULL, 1, 1, 2, 'menu', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (240, 2, '0,2', '部门管理', 'M', 'Dept', 'dept', 'system/dept/index', NULL, NULL, 1, 1, 4, 'tree', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (250, 2, '0,2', '字典管理', 'M', 'Dict', 'dict', 'system/dict/index', NULL, NULL, 1, 1, 5, 'dict', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (251, 250, '0,2,250', '字典项', 'M', 'DictItem', 'dict-item', 'system/dict/dict-item', NULL, 0, 1, 0, 6, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (260, 2, '0,2', '系统日志', 'M', 'Log', 'log', 'system/log/index', NULL, 0, 1, 1, 7, 'document', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (270, 1, '0,1', '系统配置', 'M', 'Config', 'config', 'system/config/index', NULL, 0, 1, 1, 3, 'setting', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (280, 2, '0,2', '通知公告', 'M', 'Notice', 'notice', 'system/notice/index', NULL, NULL, NULL, 1, 9, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (310, 3, '0,3', '代码生成', 'M', 'Codegen', 'codegen', 'codegen/index', NULL, NULL, 1, 1, 1, 'code', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (501, 5, '0,5', '平台文档(外链)', 'M', NULL, 'https://juejin.cn/post/7228990409909108793', '', NULL, NULL, NULL, 1, 1, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (502, 5, '0,5', '后端文档', 'M', NULL, 'https://youlai.blog.csdn.net/article/details/145178880', '', NULL, NULL, NULL, 1, 2, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (503, 5, '0,5', '移动端文档', 'M', NULL, 'https://youlai.blog.csdn.net/article/details/143222890', '', NULL, NULL, NULL, 1, 3, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (504, 5, '0,5', '内部文档', 'M', NULL, 'internal-doc', 'demo/internal-doc', NULL, NULL, NULL, 1, 4, 'document', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (601, 6, '0,6', 'Apifox', 'M', 'Apifox', 'apifox', 'demo/api/apifox', NULL, NULL, 1, 1, 1, 'api', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (701, 7, '0,7', '富文本编辑器', 'M', 'WangEditor', 'wang-editor', 'demo/wang-editor', NULL, NULL, 1, 1, 2, '', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (702, 7, '0,7', '图片上传', 'M', 'Upload', 'upload', 'demo/upload', NULL, NULL, 1, 1, 3, '', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (703, 7, '0,7', '图标选择器', 'M', 'IconSelect', 'icon-select', 'demo/icon-select', NULL, NULL, 1, 1, 4, '', '', '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (704, 7, '0,7', '字典组件', 'M', 'DictDemo', 'dict-demo', 'demo/dictionary', NULL, NULL, 1, 1, 4, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (705, 7, '0,7', '增删改查', 'M', 'Curd', 'curd', 'demo/curd/index', NULL, NULL, 1, 1, 0, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (706, 7, '0,7', '列表选择器', 'M', 'TableSelect', 'table-select', 'demo/table-select/index', NULL, NULL, 1, 1, 1, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (707, 7, '0,7', '拖拽组件', 'M', 'Drag', 'drag', 'demo/drag', NULL, NULL, NULL, 1, 5, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (708, 7, '0,7', '滚动文本', 'M', 'TextScroll', 'text-scroll', 'demo/text-scroll', NULL, NULL, NULL, 1, 6, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (709, 7, '0,7', '自适应表格操作列', 'M', 'AutoOperationColumn', 'operation-column', 'demo/auto-operation-column', NULL, NULL, 1, 1, 1, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (801, 8, '0,8', 'Websocket', 'M', 'WebSocket', '/function/websocket', 'demo/websocket', NULL, NULL, 1, 1, 1, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (802, 8, '0,8', 'Icons', 'M', 'IconDemo', 'icon-demo', 'demo/icons', NULL, NULL, 1, 1, 2, 'el-icon-Notification', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (803, 8, '0,8', '字典实时同步', 'M', 'DictSync', 'dict-sync', 'demo/dict-sync', NULL, NULL, NULL, 1, 3, '', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (804, 8, '0,8', 'VxeTable', 'M', 'VxeTable', 'vxe-table', 'demo/vxe-table/index', NULL, NULL, 1, 1, 4, 'el-icon-MagicStick', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (805, 8, '0,8', 'CURD单文件', 'M', 'CurdSingle', 'curd-single', 'demo/curd-single', NULL, NULL, 1, 1, 5, 'el-icon-Reading', '', '2026-02-20 18:08:58', '2026-02-20 18:08:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (910, 9, '0,9', '菜单一级', 'C', NULL, 'multi-level1', 'Layout', NULL, 1, NULL, 1, 1, '', '', '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (911, 910, '0,9,910', '菜单二级', 'C', NULL, 'multi-level2', 'Layout', NULL, 0, NULL, 1, 1, '', NULL, '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (912, 911, '0,9,910,911', '菜单三级-1', 'M', NULL, 'multi-level3-1', 'demo/multi-level/children/children/level3-1', NULL, 0, 1, 1, 1, '', '', '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (913, 911, '0,9,910,911', '菜单三级-2', 'M', NULL, 'multi-level3-2', 'demo/multi-level/children/children/level3-2', NULL, 0, 1, 1, 2, '', '', '2026-02-20 18:08:59', '2026-02-20 18:08:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (1001, 10, '0,10', '参数(type=1)', 'M', 'RouteParamType1', 'route-param-type1', 'demo/route-param', NULL, 0, 1, 1, 1, 'el-icon-Star', NULL, '2026-02-20 18:08:59', '2026-02-20 18:08:59', '{\"type\": \"1\"}', 1);
INSERT INTO `sys_menu` VALUES (1002, 10, '0,10', '参数(type=2)', 'M', 'RouteParamType2', 'route-param-type2', 'demo/route-param', NULL, 0, 1, 1, 2, 'el-icon-StarFilled', NULL, '2026-02-20 18:08:59', '2026-02-20 18:08:59', '{\"type\": \"2\"}', 1);
INSERT INTO `sys_menu` VALUES (1101, 110, '0,1,110', '租户查询', 'B', NULL, '', NULL, 'sys:tenant:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1102, 110, '0,1,110', '租户新增', 'B', NULL, '', NULL, 'sys:tenant:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1103, 110, '0,1,110', '租户编辑', 'B', NULL, '', NULL, 'sys:tenant:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1104, 110, '0,1,110', '租户删除', 'B', NULL, '', NULL, 'sys:tenant:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1105, 110, '0,1,110', '租户启用/禁用', 'B', NULL, '', NULL, 'sys:tenant:change-status', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1106, 110, '0,1,110', '租户切换', 'B', NULL, '', NULL, 'sys:tenant:switch', NULL, NULL, 1, 6, '', NULL, '2026-02-20 18:08:52', '2026-02-20 18:08:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (1107, 110, '0,1,110', '设置套餐', 'B', NULL, '', NULL, 'sys:tenant:plan-assign', NULL, NULL, 1, 7, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1201, 120, '0,1,120', '套餐查询', 'B', NULL, '', NULL, 'sys:tenant-plan:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1202, 120, '0,1,120', '套餐新增', 'B', NULL, '', NULL, 'sys:tenant-plan:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1203, 120, '0,1,120', '套餐编辑', 'B', NULL, '', NULL, 'sys:tenant-plan:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1204, 120, '0,1,120', '套餐删除', 'B', NULL, '', NULL, 'sys:tenant-plan:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (1205, 120, '0,1,120', '套餐菜单配置', 'B', NULL, '', NULL, 'sys:tenant-plan:assign', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (2101, 210, '0,2,210', '用户查询', 'B', NULL, '', NULL, 'sys:user:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2102, 210, '0,2,210', '用户新增', 'B', NULL, '', NULL, 'sys:user:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:53', '2026-02-20 18:08:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2103, 210, '0,2,210', '用户编辑', 'B', NULL, '', NULL, 'sys:user:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2104, 210, '0,2,210', '用户删除', 'B', NULL, '', NULL, 'sys:user:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2105, 210, '0,2,210', '重置密码', 'B', NULL, '', NULL, 'sys:user:reset-password', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2106, 210, '0,2,210', '用户导入', 'B', NULL, '', NULL, 'sys:user:import', NULL, NULL, 1, 6, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2107, 210, '0,2,210', '用户导出', 'B', NULL, '', NULL, 'sys:user:export', NULL, NULL, 1, 7, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2201, 220, '0,2,220', '角色查询', 'B', NULL, '', NULL, 'sys:role:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2202, 220, '0,2,220', '角色新增', 'B', NULL, '', NULL, 'sys:role:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2203, 220, '0,2,220', '角色编辑', 'B', NULL, '', NULL, 'sys:role:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2204, 220, '0,2,220', '角色删除', 'B', NULL, '', NULL, 'sys:role:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2205, 220, '0,2,220', '角色分配权限', 'B', NULL, '', NULL, 'sys:role:assign', NULL, NULL, 1, 5, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2301, 230, '0,1,230', '菜单查询', 'B', NULL, '', NULL, 'sys:menu:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (2302, 230, '0,1,230', '菜单新增', 'B', NULL, '', NULL, 'sys:menu:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (2303, 230, '0,1,230', '菜单编辑', 'B', NULL, '', NULL, 'sys:menu:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:54', '2026-02-20 18:08:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (2304, 230, '0,1,230', '菜单删除', 'B', NULL, '', NULL, 'sys:menu:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 1);
INSERT INTO `sys_menu` VALUES (2401, 240, '0,2,240', '部门查询', 'B', NULL, '', NULL, 'sys:dept:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2402, 240, '0,2,240', '部门新增', 'B', NULL, '', NULL, 'sys:dept:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2403, 240, '0,2,240', '部门编辑', 'B', NULL, '', NULL, 'sys:dept:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2404, 240, '0,2,240', '部门删除', 'B', NULL, '', NULL, 'sys:dept:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2501, 250, '0,2,250', '字典查询', 'B', NULL, '', NULL, 'sys:dict:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2502, 250, '0,2,250', '字典新增', 'B', NULL, '', NULL, 'sys:dict:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2503, 250, '0,2,250', '字典编辑', 'B', NULL, '', NULL, 'sys:dict:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2504, 250, '0,2,250', '字典删除', 'B', NULL, '', NULL, 'sys:dict:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2511, 251, '0,2,250,251', '字典项查询', 'B', NULL, '', NULL, 'sys:dict-item:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:55', '2026-02-20 18:08:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2512, 251, '0,2,250,251', '字典项新增', 'B', NULL, '', NULL, 'sys:dict-item:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2513, 251, '0,2,250,251', '字典项编辑', 'B', NULL, '', NULL, 'sys:dict-item:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2514, 251, '0,2,250,251', '字典项删除', 'B', NULL, '', NULL, 'sys:dict-item:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2701, 270, '0,1,270', '系统配置查询', 'B', NULL, '', NULL, 'sys:config:list', 0, 1, 1, 1, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2702, 270, '0,1,270', '系统配置新增', 'B', NULL, '', NULL, 'sys:config:create', 0, 1, 1, 2, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2703, 270, '0,1,270', '系统配置修改', 'B', NULL, '', NULL, 'sys:config:update', 0, 1, 1, 3, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2704, 270, '0,1,270', '系统配置删除', 'B', NULL, '', NULL, 'sys:config:delete', 0, 1, 1, 4, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2705, 270, '0,1,270', '系统配置刷新', 'B', NULL, '', NULL, 'sys:config:refresh', 0, 1, 1, 5, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2801, 280, '0,2,280', '通知查询', 'B', NULL, '', NULL, 'sys:notice:list', NULL, NULL, 1, 1, '', NULL, '2026-02-20 18:08:56', '2026-02-20 18:08:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2802, 280, '0,2,280', '通知新增', 'B', NULL, '', NULL, 'sys:notice:create', NULL, NULL, 1, 2, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2803, 280, '0,2,280', '通知编辑', 'B', NULL, '', NULL, 'sys:notice:update', NULL, NULL, 1, 3, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2804, 280, '0,2,280', '通知删除', 'B', NULL, '', NULL, 'sys:notice:delete', NULL, NULL, 1, 4, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2805, 280, '0,2,280', '通知发布', 'B', NULL, '', NULL, 'sys:notice:publish', 0, 1, 1, 5, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2806, 280, '0,2,280', '通知撤回', 'B', NULL, '', NULL, 'sys:notice:revoke', 0, 1, 1, 6, '', NULL, '2026-02-20 18:08:57', '2026-02-20 18:08:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2807, 0, '0', '订单管理', 'C', NULL, '/oms', 'Layout', NULL, 1, 1, 1, 21, 'file', NULL, '2026-03-13 15:40:27', '2026-03-30 18:44:30', NULL, 2);
INSERT INTO `sys_menu` VALUES (2808, 0, '0', '商品管理', 'C', NULL, '/pms', 'Layout', NULL, 1, 1, 1, 22, 'file', NULL, '2026-03-13 16:33:59', '2026-03-30 18:44:35', NULL, 2);
INSERT INTO `sys_menu` VALUES (2809, 0, '0', '营销管理', 'C', NULL, '/sms', 'Layout', NULL, 1, 1, 1, 23, 'file', NULL, '2026-03-13 16:34:20', '2026-03-30 18:44:40', NULL, 2);
INSERT INTO `sys_menu` VALUES (2810, 0, '0', '会员管理', 'C', NULL, '/ums', 'Layout', NULL, 1, 1, 1, 24, 'user', NULL, '2026-03-13 16:34:59', '2026-03-30 18:44:48', NULL, 2);
INSERT INTO `sys_menu` VALUES (2811, 0, '0', '支付管理', 'C', NULL, '/pay', 'Layout', NULL, 1, 1, 1, 25, 'file', NULL, '2026-03-13 16:35:25', '2026-03-30 18:44:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2812, 0, '0', '退款管理', 'C', NULL, '/refund', 'Layout', NULL, 1, 1, 1, 26, 'file', NULL, '2026-03-13 16:35:47', '2026-03-30 18:44:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2813, 2807, '0,2807', '订单详情', 'M', 'OmsOrder', 'oms-order', 'aioveuMallOmsOrder/oms-order/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 16:41:04', '2026-03-13 16:41:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2814, 2813, '0,2807,2813', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:41:28', '2026-03-13 16:41:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2816, 2813, '0,2807,2813', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:42:17', '2026-03-13 16:42:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2817, 2813, '0,2807,2813', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:42:31', '2026-03-13 16:42:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2818, 2813, '0,2807,2813', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrder:oms-order:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:42:47', '2026-03-13 16:42:47', NULL, 2);
INSERT INTO `sys_menu` VALUES (2819, 2807, '0,2807', '订单商品信息', 'M', 'OmsOrderItem', 'oms-order-item', 'aioveuMallOmsOrderItem/oms-order-item/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 16:43:21', '2026-03-13 16:43:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2820, 2819, '0,2807,2819', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:43:35', '2026-03-13 16:43:35', NULL, 2);
INSERT INTO `sys_menu` VALUES (2821, 2819, '0,2807,2819', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:43:50', '2026-03-13 16:43:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2822, 2819, '0,2807,2819', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:44:06', '2026-03-13 16:44:06', NULL, 2);
INSERT INTO `sys_menu` VALUES (2823, 2819, '0,2807,2819', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderItem:oms-order-item:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:44:21', '2026-03-13 16:44:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2824, 2807, '0,2807', '订单物流记录', 'M', 'OmsOrderDelivery', 'oms-order-delivery', 'aioveuMallOmsOrderDelivery/oms-order-delivery/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 16:44:51', '2026-03-13 16:44:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2825, 2824, '0,2807,2824', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:45:36', '2026-03-13 16:45:36', NULL, 2);
INSERT INTO `sys_menu` VALUES (2826, 2824, '0,2807,2824', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:45:51', '2026-03-13 16:45:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2827, 2824, '0,2807,2824', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:46:05', '2026-03-13 16:46:05', NULL, 2);
INSERT INTO `sys_menu` VALUES (2828, 2824, '0,2807,2824', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderDelivery:oms-order-delivery:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:46:19', '2026-03-13 16:46:19', NULL, 2);
INSERT INTO `sys_menu` VALUES (2829, 2807, '0,2807', '订单操作历史记录', 'M', 'OmsOrderLog', 'oms-order-log', 'aioveuMallOmsOrderLog/oms-order-log/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 16:46:56', '2026-03-13 16:46:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2830, 2829, '0,2807,2829', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:47:10', '2026-03-13 16:47:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2831, 2829, '0,2807,2829', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:47:24', '2026-03-13 16:47:24', NULL, 2);
INSERT INTO `sys_menu` VALUES (2832, 2829, '0,2807,2829', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:47:38', '2026-03-13 16:47:38', NULL, 2);
INSERT INTO `sys_menu` VALUES (2833, 2829, '0,2807,2829', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderLog:oms-order-log:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:47:52', '2026-03-13 16:47:52', NULL, 2);
INSERT INTO `sys_menu` VALUES (2834, 2807, '0,2807', '支付信息', 'M', 'OmsOrderPay', 'oms-order-pay', 'aioveuMallOmsOrderPay/oms-order-pay/index', NULL, 1, 1, 1, 5, 'menu', NULL, '2026-03-13 16:48:39', '2026-03-13 16:48:39', NULL, 2);
INSERT INTO `sys_menu` VALUES (2835, 2834, '0,2807,2834', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:48:55', '2026-03-13 16:48:55', NULL, 2);
INSERT INTO `sys_menu` VALUES (2836, 2834, '0,2807,2834', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:49:10', '2026-03-13 16:49:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2837, 2834, '0,2807,2834', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:49:23', '2026-03-13 16:49:23', NULL, 2);
INSERT INTO `sys_menu` VALUES (2838, 2834, '0,2807,2834', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderPay:oms-order-pay:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:49:35', '2026-03-13 16:49:35', NULL, 2);
INSERT INTO `sys_menu` VALUES (2839, 2807, '0,2807', '订单配置信息', 'M', 'OmsOrderSetting', 'oms-order-setting', 'aioveuMallOmsOrderSetting/oms-order-setting/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 16:50:05', '2026-03-13 16:50:05', NULL, 2);
INSERT INTO `sys_menu` VALUES (2840, 2839, '0,2807,2839', '查询', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:50:20', '2026-03-13 16:50:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2841, 2839, '0,2807,2839', '新增', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:50:33', '2026-03-13 16:50:33', NULL, 2);
INSERT INTO `sys_menu` VALUES (2842, 2839, '0,2807,2839', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:50:46', '2026-03-13 16:50:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2843, 2839, '0,2807,2839', '删除', 'B', NULL, NULL, NULL, 'aioveuMallOmsOrderSetting:oms-order-setting:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:50:59', '2026-03-13 16:50:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2844, 2808, '0,2808', '商品上架', 'M', 'GoodsDetail', 'pms-detail', 'aioveuMallPmsGoods/pms-detail/index', NULL, 1, 1, 1, 0, 'menu', NULL, '2026-03-13 16:51:54', '2026-03-13 16:51:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2845, 2808, '0,2808', '商品品牌', 'M', 'PmsBrand', 'pms-brand', 'aioveuMallPmsBrand/pms-brand/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 16:52:24', '2026-03-13 16:54:26', NULL, 2);
INSERT INTO `sys_menu` VALUES (2846, 2845, '0,2808,2845', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:54:41', '2026-03-13 16:54:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2847, 2845, '0,2808,2845', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:55:01', '2026-03-13 16:55:01', NULL, 2);
INSERT INTO `sys_menu` VALUES (2848, 2845, '0,2808,2845', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:55:19', '2026-03-13 16:55:19', NULL, 2);
INSERT INTO `sys_menu` VALUES (2849, 2845, '0,2808,2845', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsBrand:pms-brand:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:55:31', '2026-03-13 16:55:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2850, 2808, '0,2808', '商品分类', 'M', 'PmsCategory', 'pms-category', 'aioveuMallPmsCategory/pms-category/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 16:56:03', '2026-03-13 16:56:03', NULL, 2);
INSERT INTO `sys_menu` VALUES (2851, 2850, '0,2808,2850', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:56:17', '2026-03-13 16:56:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2852, 2850, '0,2808,2850', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:56:43', '2026-03-13 16:56:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2853, 2850, '0,2808,2850', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:56:54', '2026-03-13 16:56:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2854, 2850, '0,2808,2850', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategory:pms-category:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:57:07', '2026-03-13 16:57:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2855, 2808, '0,2808', '商品分类树级', 'M', 'PmsCategoryTree', 'pms-categoryTree', 'aioveuMallPmsCategoryTree/pms-categoryTree/categoryTree', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 16:57:37', '2026-03-13 16:57:37', NULL, 2);
INSERT INTO `sys_menu` VALUES (2856, 2808, '0,2808', '商品分类类型（规格，属性）', 'M', 'PmsCategoryAttribute', 'pms-category-attribute', 'aioveuMallPmsCategoryAttribute/pms-category-attribute/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 16:58:09', '2026-03-13 16:58:09', NULL, 2);
INSERT INTO `sys_menu` VALUES (2857, 2856, '0,2808,2856', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 16:58:27', '2026-03-13 16:58:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2858, 2856, '0,2808,2856', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 16:58:40', '2026-03-13 16:58:40', NULL, 2);
INSERT INTO `sys_menu` VALUES (2859, 2856, '0,2808,2856', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 16:58:52', '2026-03-13 16:58:52', NULL, 2);
INSERT INTO `sys_menu` VALUES (2860, 2856, '0,2808,2856', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsCategoryAttribute:pms-category-attribute:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 16:59:03', '2026-03-13 16:59:03', NULL, 2);
INSERT INTO `sys_menu` VALUES (2861, 2808, '0,2808', '商品库存', 'M', 'PmsSku', 'pms-sku', 'aioveuMallPmsSku/pms-sku/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 17:00:01', '2026-03-13 17:00:01', NULL, 2);
INSERT INTO `sys_menu` VALUES (2862, 2861, '0,2808,2861', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:00:18', '2026-03-13 17:00:18', NULL, 2);
INSERT INTO `sys_menu` VALUES (2863, 2861, '0,2808,2861', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:00:32', '2026-03-13 17:00:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2864, 2861, '0,2808,2861', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:00:46', '2026-03-13 17:00:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2865, 2861, '0,2808,2861', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsSku:pms-sku:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:01:03', '2026-03-13 17:01:03', NULL, 2);
INSERT INTO `sys_menu` VALUES (2866, 2808, '0,2808', '商品', 'M', 'PmsSpu', 'pms-spu', 'aioveuMallPmsSpu/pms-spu/index', NULL, 1, 1, 1, 7, 'menu', NULL, '2026-03-13 17:07:23', '2026-03-13 17:07:23', NULL, 2);
INSERT INTO `sys_menu` VALUES (2867, 2866, '0,2808,2866', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:07:37', '2026-03-13 17:07:37', NULL, 2);
INSERT INTO `sys_menu` VALUES (2868, 2866, '0,2808,2866', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:07:56', '2026-03-13 17:07:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2869, 2866, '0,2808,2866', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:08:10', '2026-03-13 17:08:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2870, 2866, '0,2808,2866', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpu:pms-spu:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:08:22', '2026-03-13 17:08:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (2871, 2808, '0,2808', '商品类型（属性/规格）', 'M', 'PmsSpuAttribute', 'pms-spu-attribute', 'aioveuMallPmsSpuAttribute/pms-spu-attribute/index', NULL, 1, 1, 1, 8, 'menu', NULL, '2026-03-13 17:09:00', '2026-03-13 17:09:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (2872, 2871, '0,2808,2871', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:09:42', '2026-03-13 17:09:42', NULL, 2);
INSERT INTO `sys_menu` VALUES (2873, 2871, '0,2808,2871', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:10:13', '2026-03-13 17:10:13', NULL, 2);
INSERT INTO `sys_menu` VALUES (2874, 2871, '0,2808,2871', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:10:28', '2026-03-13 17:10:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2875, 2871, '0,2808,2871', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPmsSpuAttribute:pms-spu-attribute:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:10:41', '2026-03-13 17:10:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2876, 2809, '0,2809', '广告', 'M', 'SmsAdvert', 'sms-advert', 'aioveuMallSmsAdvert/sms-advert/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:11:29', '2026-03-13 17:11:29', NULL, 2);
INSERT INTO `sys_menu` VALUES (2877, 2876, '0,2809,2876', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:11:45', '2026-03-13 17:11:45', NULL, 2);
INSERT INTO `sys_menu` VALUES (2878, 2876, '0,2809,2876', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:11:59', '2026-03-13 17:11:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2879, 2876, '0,2809,2876', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:12:11', '2026-03-13 17:12:11', NULL, 2);
INSERT INTO `sys_menu` VALUES (2880, 2876, '0,2809,2876', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsAdvert:sms-advert:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:12:24', '2026-03-13 17:12:24', NULL, 2);
INSERT INTO `sys_menu` VALUES (2881, 2809, '0,2809', '优惠券', 'M', 'SmsCoupon', 'sms-coupon', 'aioveuMallSmsCoupon/sms-coupon/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:12:50', '2026-03-13 17:12:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2882, 2881, '0,2809,2881', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:13:04', '2026-03-13 17:13:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2883, 2881, '0,2809,2881', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:13:18', '2026-03-13 17:13:18', NULL, 2);
INSERT INTO `sys_menu` VALUES (2884, 2881, '0,2809,2881', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:13:32', '2026-03-13 17:13:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2885, 2881, '0,2809,2881', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCoupon:sms-coupon:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:13:44', '2026-03-13 17:13:44', NULL, 2);
INSERT INTO `sys_menu` VALUES (2886, 2809, '0,2809', '优惠券领取/使用记录', 'M', 'SmsCouponHistory', 'sms-coupon-history', 'aioveuMallSmsCouponHistory/sms-coupon-history/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 17:14:10', '2026-03-13 17:14:10', NULL, 2);
INSERT INTO `sys_menu` VALUES (2887, 2886, '0,2809,2886', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:14:21', '2026-03-13 17:14:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2888, 2886, '0,2809,2886', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:14:36', '2026-03-13 17:14:36', NULL, 2);
INSERT INTO `sys_menu` VALUES (2889, 2886, '0,2809,2886', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:14:48', '2026-03-13 17:14:48', NULL, 2);
INSERT INTO `sys_menu` VALUES (2890, 2886, '0,2809,2886', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponHistory:sms-coupon-history:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:15:00', '2026-03-13 17:15:00', NULL, 2);
INSERT INTO `sys_menu` VALUES (2891, 2809, '0,2809', '优惠券适用的具体商品', 'M', 'SmsCouponSpu', 'sms-coupon-spu', 'aioveuMallSmsCouponSpu/sms-coupon-spu/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 17:15:31', '2026-03-13 17:15:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2892, 2891, '0,2809,2891', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:15:51', '2026-03-13 17:15:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2893, 2891, '0,2809,2891', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:16:07', '2026-03-13 17:16:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2894, 2891, '0,2809,2891', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:16:17', '2026-03-13 17:16:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2895, 2891, '0,2809,2891', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpu:sms-coupon-spu:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:16:28', '2026-03-13 17:16:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2896, 2809, '0,2809', '优惠券适用的具体分类', 'M', 'SmsCouponSpuCategory', 'sms-coupon-spu-category', 'aioveuMallSmsCouponSpuCategory/sms-coupon-spu-category/index', NULL, 1, 1, 1, 5, 'menu', NULL, '2026-03-13 17:16:56', '2026-03-13 17:16:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2897, 2896, '0,2809,2896', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:17:08', '2026-03-13 17:17:08', NULL, 2);
INSERT INTO `sys_menu` VALUES (2898, 2896, '0,2809,2896', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:17:22', '2026-03-13 17:17:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (2899, 2896, '0,2809,2896', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:17:34', '2026-03-13 17:17:34', NULL, 2);
INSERT INTO `sys_menu` VALUES (2900, 2896, '0,2809,2896', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsCouponSpuCategory:sms-coupon-spu-category:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:17:46', '2026-03-13 17:17:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2901, 2809, '0,2809', '首页分类配置', 'M', 'SmsHomeCategory', 'sms-home-category', 'aioveuMallSmsHomeCategory/sms-home-category/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 17:18:14', '2026-03-13 17:18:14', NULL, 2);
INSERT INTO `sys_menu` VALUES (2902, 2901, '0,2809,2901', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:18:30', '2026-03-13 17:18:30', NULL, 2);
INSERT INTO `sys_menu` VALUES (2903, 2901, '0,2809,2901', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:18:41', '2026-03-13 17:18:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2904, 2901, '0,2809,2901', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:18:52', '2026-03-13 17:18:52', NULL, 2);
INSERT INTO `sys_menu` VALUES (2905, 2901, '0,2809,2901', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeCategory:sms-home-category:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:19:07', '2026-03-13 17:19:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2906, 2809, '0,2809', '首页广告配置', 'M', 'SmsHomeAdvert', 'sms-home-advert', 'aioveuMallSmsHomeAdvert/sms-home-advert/index', NULL, 1, 1, 1, 7, 'menu', NULL, '2026-03-13 17:19:34', '2026-03-13 17:19:34', NULL, 2);
INSERT INTO `sys_menu` VALUES (2907, 2906, '0,2809,2906', '查询', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:19:46', '2026-03-13 17:19:46', NULL, 2);
INSERT INTO `sys_menu` VALUES (2908, 2906, '0,2809,2906', '新增', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:19:59', '2026-03-13 17:19:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2909, 2906, '0,2809,2906', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:20:12', '2026-03-13 17:20:12', NULL, 2);
INSERT INTO `sys_menu` VALUES (2910, 2906, '0,2809,2906', '删除', 'B', NULL, NULL, NULL, 'aioveuMallSmsHomeAdvert:sms-home-advert:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:20:23', '2026-03-13 17:20:23', NULL, 2);
INSERT INTO `sys_menu` VALUES (2911, 2810, '0,2810', '会员', 'M', 'UmsMember', 'ums-member', 'aioveuMallUmsMember/ums-member/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:23:31', '2026-03-13 17:23:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2912, 2911, '0,2810,2911', '查询', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:23:54', '2026-03-13 17:23:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2913, 2911, '0,2810,2911', '新增', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:24:07', '2026-03-13 17:24:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2914, 2911, '0,2810,2911', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:24:20', '2026-03-13 17:24:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2915, 2911, '0,2810,2911', '删除', 'B', NULL, NULL, NULL, 'aioveuMallUmsMember:ums-member:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:24:37', '2026-03-13 17:24:37', NULL, 2);
INSERT INTO `sys_menu` VALUES (2916, 2810, '0,2810', '会员收货地址', 'M', 'UmsMemberAddress', 'ums-member-address', 'aioveuMallUmsMemberAddress/ums-member-address/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:25:01', '2026-03-13 17:25:01', NULL, 2);
INSERT INTO `sys_menu` VALUES (2917, 2916, '0,2810,2916', '查询', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:25:15', '2026-03-13 17:25:15', NULL, 2);
INSERT INTO `sys_menu` VALUES (2918, 2916, '0,2810,2916', '新增', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:25:29', '2026-03-13 17:25:29', NULL, 2);
INSERT INTO `sys_menu` VALUES (2919, 2916, '0,2810,2916', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:25:43', '2026-03-13 17:25:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2920, 2916, '0,2810,2916', '删除', 'B', NULL, NULL, NULL, 'aioveuMallUmsMemberAddress:ums-member-address:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:25:57', '2026-03-13 17:25:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2921, 2811, '0,2811', '支付订单', 'M', 'PayOrder', 'pay-order', 'aioveuMallPayOrder/pay-order/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:26:43', '2026-03-13 17:26:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2922, 2921, '0,2811,2921', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:26:59', '2026-03-13 17:26:59', NULL, 2);
INSERT INTO `sys_menu` VALUES (2923, 2921, '0,2811,2921', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:27:14', '2026-03-13 17:27:14', NULL, 2);
INSERT INTO `sys_menu` VALUES (2924, 2921, '0,2811,2921', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:27:27', '2026-03-13 17:27:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2925, 2921, '0,2811,2921', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayOrder:pay-order:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:27:41', '2026-03-13 17:27:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2926, 2811, '0,2811', '退款记录', 'M', 'PayRefundRecord', 'pay-refund-record', 'aioveuMallPayRefundRecord/pay-refund-record/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:28:06', '2026-03-13 17:28:06', NULL, 2);
INSERT INTO `sys_menu` VALUES (2927, 2926, '0,2811,2926', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:28:19', '2026-03-13 17:28:19', NULL, 2);
INSERT INTO `sys_menu` VALUES (2928, 2926, '0,2811,2926', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:28:34', '2026-03-13 17:28:34', NULL, 2);
INSERT INTO `sys_menu` VALUES (2929, 2926, '0,2811,2926', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:28:45', '2026-03-13 17:28:45', NULL, 2);
INSERT INTO `sys_menu` VALUES (2930, 2926, '0,2811,2926', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayRefundRecord:pay-refund-record:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:28:57', '2026-03-13 17:28:57', NULL, 2);
INSERT INTO `sys_menu` VALUES (2931, 2811, '0,2811', '支付渠道配置', 'M', 'PayChannelConfig', 'pay-channel-config', 'aioveuMallPayChannelConfig/pay-channel-config/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 17:31:04', '2026-03-13 17:31:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2932, 2931, '0,2811,2931', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:31:16', '2026-03-13 17:31:16', NULL, 2);
INSERT INTO `sys_menu` VALUES (2933, 2931, '0,2811,2931', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:31:27', '2026-03-13 17:31:27', NULL, 2);
INSERT INTO `sys_menu` VALUES (2934, 2931, '0,2811,2931', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:31:40', '2026-03-13 17:31:40', NULL, 2);
INSERT INTO `sys_menu` VALUES (2935, 2931, '0,2811,2931', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayChannelConfig:pay-channel-config:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:31:51', '2026-03-13 17:31:51', NULL, 2);
INSERT INTO `sys_menu` VALUES (2936, 2811, '0,2811', '支付对账', 'M', 'PayReconciliation', 'pay-reconciliation', 'aioveuMallPayReconciliation/pay-reconciliation/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 17:32:16', '2026-03-13 17:32:16', NULL, 2);
INSERT INTO `sys_menu` VALUES (2937, 2936, '0,2811,2936', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:32:28', '2026-03-13 17:32:28', NULL, 2);
INSERT INTO `sys_menu` VALUES (2938, 2936, '0,2811,2936', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:32:42', '2026-03-13 17:32:42', NULL, 2);
INSERT INTO `sys_menu` VALUES (2939, 2936, '0,2811,2936', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:32:53', '2026-03-13 17:32:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2940, 2936, '0,2811,2936', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayReconciliation:pay-reconciliation:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:33:05', '2026-03-13 17:33:05', NULL, 2);
INSERT INTO `sys_menu` VALUES (2941, 2812, '0,2812', '订单退款申请', 'M', 'RefundOrder', 'refund-order', 'aioveuMallRefundOrder/refund-order/index', NULL, 1, 1, 1, 1, 'menu', NULL, '2026-03-13 17:33:40', '2026-03-13 17:33:40', NULL, 2);
INSERT INTO `sys_menu` VALUES (2942, 2941, '0,2812,2941', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:33:53', '2026-03-13 17:33:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2943, 2941, '0,2812,2941', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:34:04', '2026-03-13 17:34:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2944, 2941, '0,2812,2941', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:34:18', '2026-03-13 17:34:18', NULL, 2);
INSERT INTO `sys_menu` VALUES (2945, 2941, '0,2812,2941', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundOrder:refund-order:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:34:32', '2026-03-13 17:34:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2946, 2812, '0,2812', '退款商品明细', 'M', 'RefundItem', 'refund-item', 'aioveuMallRefundItem/refund-item/index', NULL, 1, 1, 1, 2, 'menu', NULL, '2026-03-13 17:34:58', '2026-03-13 17:34:58', NULL, 2);
INSERT INTO `sys_menu` VALUES (2947, 2946, '0,2812,2946', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:35:09', '2026-03-13 17:35:09', NULL, 2);
INSERT INTO `sys_menu` VALUES (2948, 2946, '0,2812,2946', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:35:20', '2026-03-13 17:35:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2949, 2946, '0,2812,2946', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:35:31', '2026-03-13 17:35:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2950, 2946, '0,2812,2946', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundItem:refund-item:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:35:45', '2026-03-13 17:35:45', NULL, 2);
INSERT INTO `sys_menu` VALUES (2951, 2812, '0,2812', '退款物流信息（用于退货）', 'M', 'RefundDelivery', 'refund-delivery', 'aioveuMallRefundDelivery/refund-delivery/index', NULL, 1, 1, 1, 3, 'menu', NULL, '2026-03-13 17:36:08', '2026-03-13 17:36:08', NULL, 2);
INSERT INTO `sys_menu` VALUES (2952, 2951, '0,2812,2951', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:36:20', '2026-03-13 17:36:20', NULL, 2);
INSERT INTO `sys_menu` VALUES (2953, 2951, '0,2812,2951', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:36:31', '2026-03-13 17:36:31', NULL, 2);
INSERT INTO `sys_menu` VALUES (2954, 2951, '0,2812,2951', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:36:43', '2026-03-13 17:36:43', NULL, 2);
INSERT INTO `sys_menu` VALUES (2955, 2951, '0,2812,2951', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundDelivery:refund-delivery:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:36:54', '2026-03-13 17:36:54', NULL, 2);
INSERT INTO `sys_menu` VALUES (2956, 2812, '0,2812', '退款操作记录（用于审计）', 'M', 'RefundOperationLog', 'refund-operation-log', 'aioveuMallRefundOperationLog/refund-operation-log/index', NULL, 1, 1, 1, 4, 'menu', NULL, '2026-03-13 17:40:53', '2026-03-13 17:40:53', NULL, 2);
INSERT INTO `sys_menu` VALUES (2957, 2956, '0,2812,2956', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:41:39', '2026-03-13 17:41:39', NULL, 2);
INSERT INTO `sys_menu` VALUES (2958, 2956, '0,2812,2956', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:41:50', '2026-03-13 17:41:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2959, 2956, '0,2812,2956', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:42:08', '2026-03-13 17:42:08', NULL, 2);
INSERT INTO `sys_menu` VALUES (2960, 2956, '0,2812,2956', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundOperationLog:refund-operation-log:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:42:21', '2026-03-13 17:42:21', NULL, 2);
INSERT INTO `sys_menu` VALUES (2961, 2812, '0,2812', '退款凭证图片', 'M', 'RefundProof', 'refund-proof', 'aioveuMallRefundProof/refund-proof/index', NULL, 1, 1, 1, 5, 'menu', NULL, '2026-03-13 17:42:50', '2026-03-13 17:42:50', NULL, 2);
INSERT INTO `sys_menu` VALUES (2962, 2961, '0,2812,2961', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:43:04', '2026-03-13 17:43:04', NULL, 2);
INSERT INTO `sys_menu` VALUES (2963, 2961, '0,2812,2961', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:43:17', '2026-03-13 17:43:17', NULL, 2);
INSERT INTO `sys_menu` VALUES (2964, 2961, '0,2812,2961', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:43:32', '2026-03-13 17:43:32', NULL, 2);
INSERT INTO `sys_menu` VALUES (2965, 2961, '0,2812,2961', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundProof:refund-proof:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:43:44', '2026-03-13 17:43:44', NULL, 2);
INSERT INTO `sys_menu` VALUES (2966, 2812, '0,2812', '退款支付记录', 'M', 'RefundPayment', 'refund-payment', 'aioveuMallRefundPayment/refund-payment/index', NULL, 1, 1, 1, 6, 'menu', NULL, '2026-03-13 17:44:11', '2026-03-13 17:44:11', NULL, 2);
INSERT INTO `sys_menu` VALUES (2967, 2966, '0,2812,2966', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:44:22', '2026-03-13 17:44:22', NULL, 2);
INSERT INTO `sys_menu` VALUES (2968, 2966, '0,2812,2966', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:44:35', '2026-03-13 17:44:35', NULL, 2);
INSERT INTO `sys_menu` VALUES (2969, 2966, '0,2812,2966', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:44:49', '2026-03-13 17:44:49', NULL, 2);
INSERT INTO `sys_menu` VALUES (2970, 2966, '0,2812,2966', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundPayment:refund-payment:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:45:00', '2026-03-13 17:45:00', NULL, 2);
INSERT INTO `sys_menu` VALUES (2971, 2812, '0,2812', '退款原因分类', 'M', 'RefundReason', 'refund-reason', 'aioveuMallRefundReason/refund-reason/index', NULL, 1, 1, 1, 7, 'menu', NULL, '2026-03-13 17:45:24', '2026-03-13 17:45:24', NULL, 2);
INSERT INTO `sys_menu` VALUES (2972, 2971, '0,2812,2971', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:query', 0, 1, 1, 1, NULL, NULL, '2026-03-13 17:45:41', '2026-03-13 17:45:41', NULL, 2);
INSERT INTO `sys_menu` VALUES (2973, 2971, '0,2812,2971', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:add', 0, 1, 1, 2, NULL, NULL, '2026-03-13 17:45:56', '2026-03-13 17:45:56', NULL, 2);
INSERT INTO `sys_menu` VALUES (2974, 2971, '0,2812,2971', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:edit', 0, 1, 1, 3, NULL, NULL, '2026-03-13 17:46:07', '2026-03-13 17:46:07', NULL, 2);
INSERT INTO `sys_menu` VALUES (2975, 2971, '0,2812,2971', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRefundReason:refund-reason:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-13 17:46:19', '2026-03-13 17:46:19', NULL, 2);
INSERT INTO `sys_menu` VALUES (2976, 1, '0,1', '前端小程序管理', 'M', 'aioveuMallTenantOauthClientWxApp', 'oauth-client-wx-app', 'aioveuMallTenantOauthClientWxApp/oauth-client-wx-app/index', NULL, 1, 1, 1, 5, 'monitor', NULL, '2026-03-23 18:09:45', '2026-03-23 18:09:45', NULL, 1);
INSERT INTO `sys_menu` VALUES (2977, 1, '0,1', '租户小程序管理', 'M', 'aioveuMallTenantWxApp', 'tenant-wx-app', 'aioveuMallTenantWxApp/tenant-wx-app/index', NULL, 1, 1, 1, 6, 'github', NULL, '2026-03-23 18:10:21', '2026-03-23 18:10:21', NULL, 1);
INSERT INTO `sys_menu` VALUES (2978, 1, '0,1', 'OAuth2授权信息', 'M', 'aioveuMallAuthOauth2Authorization', 'oauth2-authorization', 'aioveuMallAuthOauth2Authorization/oauth2-authorization/index', NULL, 1, 1, 1, 7, 'file', NULL, '2026-03-23 18:10:46', '2026-03-23 18:10:46', NULL, 1);
INSERT INTO `sys_menu` VALUES (2979, 1, '0,1', 'OAuth2授权同意', 'M', 'aioveuMallAuthOauth2AuthorizationConsent', 'oauth2-authorization-consent', 'aioveuMallAuthOauth2AuthorizationConsent/oauth2-authorization-consent/index', NULL, 1, 1, 1, 8, 'file', NULL, '2026-03-23 18:11:12', '2026-03-23 18:11:12', NULL, 1);
INSERT INTO `sys_menu` VALUES (2980, 1, '0,1', 'OAuth2注册客户端', 'M', 'aioveuMallAuthOauth2RegisteredClient', 'oauth2-registered-client', 'aioveuMallAuthOauth2RegisteredClient/oauth2-registered-client/index', NULL, 1, 1, 1, 9, 'file', NULL, '2026-03-23 18:11:38', '2026-03-23 18:11:38', NULL, 1);
INSERT INTO `sys_menu` VALUES (2981, 2976, '0,1,2976', '查询', 'B', NULL, NULL, NULL, 'aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:list', 0, 1, 1, 1, NULL, NULL, '2026-03-23 18:11:59', '2026-03-23 18:11:59', NULL, 1);
INSERT INTO `sys_menu` VALUES (2982, 2976, '0,1,2976', '新增', 'B', NULL, NULL, NULL, 'aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:create', 0, 1, 1, 2, NULL, NULL, '2026-03-23 18:12:18', '2026-03-23 18:12:18', NULL, 1);
INSERT INTO `sys_menu` VALUES (2983, 2976, '0,1,2976', '修改', 'B', NULL, NULL, NULL, 'aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:update', 0, 1, 1, 3, NULL, NULL, '2026-03-23 18:12:29', '2026-03-23 18:12:29', NULL, 1);
INSERT INTO `sys_menu` VALUES (2984, 2976, '0,1,2976', '删除', 'B', NULL, NULL, NULL, 'aioveuMallTenantOauthClientWxApp:oauth-client-wx-app:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-23 18:12:42', '2026-03-23 18:12:42', NULL, 1);
INSERT INTO `sys_menu` VALUES (2985, 2977, '0,1,2977', '查询', 'B', NULL, NULL, NULL, 'aioveuMallTenantWxApp:tenant-wx-app:list', 0, 1, 1, 1, NULL, NULL, '2026-03-23 18:12:58', '2026-03-23 18:12:58', NULL, 1);
INSERT INTO `sys_menu` VALUES (2986, 2977, '0,1,2977', '新增', 'B', NULL, NULL, NULL, 'aioveuMallTenantWxApp:tenant-wx-app:create', 0, 1, 1, 2, NULL, NULL, '2026-03-23 18:13:08', '2026-03-23 18:13:08', NULL, 1);
INSERT INTO `sys_menu` VALUES (2987, 2977, '0,1,2977', '修改', 'B', NULL, NULL, NULL, 'aioveuMallTenantWxApp:tenant-wx-app:update', 0, 1, 1, 3, NULL, NULL, '2026-03-23 18:13:18', '2026-03-23 18:13:18', NULL, 1);
INSERT INTO `sys_menu` VALUES (2988, 2977, '0,1,2977', '删除', 'B', NULL, NULL, NULL, 'aioveuMallTenantWxApp:tenant-wx-app:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-23 18:13:31', '2026-03-23 18:13:31', NULL, 1);
INSERT INTO `sys_menu` VALUES (2989, 2978, '0,1,2978', '查询', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2Authorization:oauth2-authorization:list', 0, 1, 1, 1, NULL, NULL, '2026-03-23 18:13:44', '2026-03-23 18:13:44', NULL, 1);
INSERT INTO `sys_menu` VALUES (2990, 2978, '0,1,2978', '新增', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2Authorization:oauth2-authorization:create', 0, 1, 1, 2, NULL, NULL, '2026-03-23 18:13:56', '2026-03-23 18:13:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (2991, 2978, '0,1,2978', '修改', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2Authorization:oauth2-authorization:update', 0, 1, 1, 3, NULL, NULL, '2026-03-23 18:14:07', '2026-03-23 18:14:07', NULL, 1);
INSERT INTO `sys_menu` VALUES (2992, 2978, '0,1,2978', '删除', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2Authorization:oauth2-authorization:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-23 18:14:17', '2026-03-23 18:14:17', NULL, 1);
INSERT INTO `sys_menu` VALUES (2993, 2979, '0,1,2979', '查询', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:list', 0, 1, 1, 1, NULL, NULL, '2026-03-23 18:14:30', '2026-03-23 18:14:30', NULL, 1);
INSERT INTO `sys_menu` VALUES (2994, 2979, '0,1,2979', '新增', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:create', 0, 1, 1, 2, NULL, NULL, '2026-03-23 18:14:43', '2026-03-23 18:14:43', NULL, 1);
INSERT INTO `sys_menu` VALUES (2995, 2979, '0,1,2979', '修改', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:update', 0, 1, 1, 3, NULL, NULL, '2026-03-23 18:14:57', '2026-03-23 18:14:57', NULL, 1);
INSERT INTO `sys_menu` VALUES (2996, 2979, '0,1,2979', '删除', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2AuthorizationConsent:oauth2-authorization-consent:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-23 18:15:08', '2026-03-23 18:15:08', NULL, 1);
INSERT INTO `sys_menu` VALUES (2997, 2980, '0,1,2980', '查询', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:list', 0, 1, 1, 1, NULL, NULL, '2026-03-23 18:15:18', '2026-03-23 18:15:18', NULL, 1);
INSERT INTO `sys_menu` VALUES (2998, 2980, '0,1,2980', '新增', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:create', 0, 1, 1, 2, NULL, NULL, '2026-03-23 18:15:30', '2026-03-23 18:15:30', NULL, 1);
INSERT INTO `sys_menu` VALUES (2999, 2980, '0,1,2980', '修改', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:update', 0, 1, 1, 3, NULL, NULL, '2026-03-23 18:15:41', '2026-03-23 18:15:41', NULL, 1);
INSERT INTO `sys_menu` VALUES (3000, 2980, '0,1,2980', '删除', 'B', NULL, NULL, NULL, 'aioveuMallAuthOauth2RegisteredClient:oauth2-registered-client:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-23 18:15:52', '2026-03-23 18:15:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (3001, 0, '0', '小程序注册管理', 'C', NULL, '/registry', 'Layout', NULL, 0, 1, 1, 2, 'el-icon-HotWater', NULL, '2026-03-30 18:45:35', '2026-03-30 18:45:35', NULL, 1);
INSERT INTO `sys_menu` VALUES (3002, 0, '0', '支付配置', 'C', NULL, '/payConfig', 'Layout', NULL, 1, 1, 1, 3, 'el-icon-ColdDrink', NULL, '2026-03-30 18:45:54', '2026-03-30 18:45:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (3003, 3001, '0,3001', '注册租户基本信息', 'M', 'aioveuMallRegistryTenant', 'registry-tenant', 'aioveuMallRegistryTenant/registry-tenant/index', NULL, 1, 1, 1, 1, 'el-icon-GobletFull', NULL, '2026-03-30 18:46:25', '2026-03-30 18:46:25', NULL, 1);
INSERT INTO `sys_menu` VALUES (3004, 3001, '0,3001', '小程序账号', 'M', 'aioveuMallRegistryAppAccount', 'registry-app-account', 'aioveuMallRegistryAppAccount/registry-app-account/index', NULL, 1, 1, 1, 2, 'el-icon-Food', NULL, '2026-03-30 18:46:49', '2026-03-30 18:46:49', NULL, 1);
INSERT INTO `sys_menu` VALUES (3005, 3001, '0,3001', '企业资质', 'M', 'aioveuMallRegistryEnterpriseQualification', 'registry-enterprise-qualification', 'aioveuMallRegistryEnterpriseQualification/registry-enterprise-qualification/index', NULL, 1, 1, 1, 3, 'el-icon-Football', NULL, '2026-03-30 18:47:28', '2026-03-30 18:47:28', NULL, 1);
INSERT INTO `sys_menu` VALUES (3006, 3001, '0,3001', '管理员信息', 'M', 'aioveuMallRegistryAdministratorInfo', 'registry-administrator-info', 'aioveuMallRegistryAdministratorInfo/registry-administrator-info/index', NULL, 1, 1, 1, 4, 'el-icon-ForkSpoon', NULL, '2026-03-30 18:47:50', '2026-03-30 18:47:50', NULL, 1);
INSERT INTO `sys_menu` VALUES (3007, 3001, '0,3001', '认证记录', 'M', 'aioveuMallRegistryCertification', 'registry-certification', 'aioveuMallRegistryCertification/registry-certification/index', NULL, 1, 1, 1, 5, 'el-icon-Fries', NULL, '2026-03-30 18:48:14', '2026-03-30 18:48:14', NULL, 1);
INSERT INTO `sys_menu` VALUES (3008, 3001, '0,3001', '认证联系人', 'M', 'aioveuMallRegistryCertificationContact', 'registry-certification-contact', 'aioveuMallRegistryCertificationContact/registry-certification-contact/index', NULL, 1, 1, 1, 6, 'el-icon-GoldMedal', NULL, '2026-03-30 18:48:41', '2026-03-30 18:48:41', NULL, 1);
INSERT INTO `sys_menu` VALUES (3009, 3001, '0,3001', '发票信息', 'M', 'aioveuMallRegistryInvoiceInfo', 'registry-invoice-info', 'aioveuMallRegistryInvoiceInfo/registry-invoice-info/index', NULL, 1, 1, 1, 7, 'el-icon-GoodsFilled', NULL, '2026-03-30 18:49:04', '2026-03-30 18:49:04', NULL, 1);
INSERT INTO `sys_menu` VALUES (3010, 3001, '0,3001', '小程序备案记录', 'M', 'aioveuMallRegistryAppFilingRecord', 'registry-app-filing-record', 'aioveuMallRegistryAppFilingRecord/registry-app-filing-record/index', NULL, 1, 1, 1, 8, 'el-icon-Grape', NULL, '2026-03-30 18:49:28', '2026-03-30 18:49:28', NULL, 1);
INSERT INTO `sys_menu` VALUES (3011, 3001, '0,3001', '操作日志', 'M', 'aioveuMallRegistryOperationLog', 'registry-operation-log', 'aioveuMallRegistryOperationLog/registry-operation-log/index', NULL, 1, 1, 1, 9, 'el-icon-MilkTea', NULL, '2026-03-30 18:49:50', '2026-03-30 18:49:50', NULL, 1);
INSERT INTO `sys_menu` VALUES (3012, 3002, '0,3002', '支付配置主表', 'M', 'aioveuMallPayConfig', 'pay-config', 'aioveuMallPayConfig/pay-config/index', NULL, 1, 1, 1, 1, 'el-icon-Headset', NULL, '2026-03-30 18:50:41', '2026-03-30 18:50:41', NULL, 1);
INSERT INTO `sys_menu` VALUES (3013, 3002, '0,3002', '微信支付配置', 'M', 'aioveuMallPayConfigWechat', 'pay-config-wechat', 'aioveuMallPayConfigWechat/pay-config-wechat/index', NULL, 1, 1, 1, 2, 'el-icon-Umbrella', NULL, '2026-03-30 18:51:09', '2026-03-30 18:51:09', NULL, 1);
INSERT INTO `sys_menu` VALUES (3014, 3002, '0,3002', '模拟支付配置', 'M', 'aioveuMallPayConfigDummy', 'pay-config-dummy', 'aioveuMallPayConfigDummy/pay-config-dummy/index', NULL, 1, 1, 1, 3, 'el-icon-Trophy', NULL, '2026-03-30 18:51:36', '2026-03-30 18:51:36', NULL, 1);
INSERT INTO `sys_menu` VALUES (3015, 3002, '0,3002', '支付宝支付配置', 'M', 'aioveuMallPayConfigAlipay', 'pay-config-alipay', 'aioveuMallPayConfigAlipay/pay-config-alipay/index', NULL, 1, 1, 1, 4, 'el-icon-VideoCamera', NULL, '2026-03-30 18:51:59', '2026-03-30 18:51:59', NULL, 1);
INSERT INTO `sys_menu` VALUES (3016, 3003, '0,3001,3003', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryTenant:registry-tenant:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 18:53:25', '2026-03-30 18:53:25', NULL, 1);
INSERT INTO `sys_menu` VALUES (3017, 3003, '0,3001,3003', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryTenant:registry-tenant:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 18:53:37', '2026-03-30 18:53:37', NULL, 1);
INSERT INTO `sys_menu` VALUES (3018, 3003, '0,3001,3003', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRegistryTenant:registry-tenant:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 18:53:45', '2026-03-30 18:53:45', NULL, 1);
INSERT INTO `sys_menu` VALUES (3019, 3003, '0,3001,3003', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryTenant:registry-tenant:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 18:53:54', '2026-03-30 18:53:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (3020, 3004, '0,3001,3004', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppAccount:registry-app-account:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 18:54:08', '2026-03-30 18:54:08', NULL, 1);
INSERT INTO `sys_menu` VALUES (3021, 3004, '0,3001,3004', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppAccount:registry-app-account:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 18:54:17', '2026-03-30 18:54:17', NULL, 1);
INSERT INTO `sys_menu` VALUES (3022, 3004, '0,3001,3004', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppAccount:registry-app-account:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 18:54:25', '2026-03-30 18:54:25', NULL, 1);
INSERT INTO `sys_menu` VALUES (3023, 3004, '0,3001,3004', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppAccount:registry-app-account:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 18:54:39', '2026-03-30 18:54:39', NULL, 1);
INSERT INTO `sys_menu` VALUES (3024, 3005, '0,3001,3005', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 18:54:52', '2026-03-30 18:54:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (3025, 3005, '0,3001,3005', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 18:55:03', '2026-03-30 18:55:03', NULL, 1);
INSERT INTO `sys_menu` VALUES (3026, 3005, '0,3001,3005', '编辑', 'B', NULL, NULL, NULL, 'aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 18:55:14', '2026-03-30 18:55:14', NULL, 1);
INSERT INTO `sys_menu` VALUES (3027, 3005, '0,3001,3005', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryEnterpriseQualification:registry-enterprise-qualification:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 18:55:23', '2026-03-30 18:55:23', NULL, 1);
INSERT INTO `sys_menu` VALUES (3028, 3006, '0,3001,3006', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAdministratorInfo:registry-administrator-info:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 18:55:37', '2026-03-30 18:55:37', NULL, 1);
INSERT INTO `sys_menu` VALUES (3029, 3006, '0,3001,3006', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAdministratorInfo:registry-administrator-info:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 18:55:51', '2026-03-30 18:55:51', NULL, 1);
INSERT INTO `sys_menu` VALUES (3030, 3006, '0,3001,3006', '更新', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAdministratorInfo:registry-administrator-info:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 18:56:00', '2026-03-30 18:56:00', NULL, 1);
INSERT INTO `sys_menu` VALUES (3031, 3006, '0,3001,3006', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAdministratorInfo:registry-administrator-info:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 18:56:09', '2026-03-30 18:56:09', NULL, 1);
INSERT INTO `sys_menu` VALUES (3032, 3007, '0,3001,3007', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertification:registry-certification:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 18:56:25', '2026-03-30 18:56:25', NULL, 1);
INSERT INTO `sys_menu` VALUES (3033, 3007, '0,3001,3007', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertification:registry-certification:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 18:56:37', '2026-03-30 18:56:37', NULL, 1);
INSERT INTO `sys_menu` VALUES (3034, 3007, '0,3001,3007', '更新', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertification:registry-certification:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 18:56:47', '2026-03-30 18:56:47', NULL, 1);
INSERT INTO `sys_menu` VALUES (3035, 3007, '0,3001,3007', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertification:registry-certification:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 18:56:56', '2026-03-30 18:56:56', NULL, 1);
INSERT INTO `sys_menu` VALUES (3036, 3008, '0,3001,3008', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertificationContact:registry-certification-contact:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 18:57:10', '2026-03-30 18:57:10', NULL, 1);
INSERT INTO `sys_menu` VALUES (3037, 3008, '0,3001,3008', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertificationContact:registry-certification-contact:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 18:57:21', '2026-03-30 18:57:21', NULL, 1);
INSERT INTO `sys_menu` VALUES (3038, 3008, '0,3001,3008', '更新', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertificationContact:registry-certification-contact:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 18:57:33', '2026-03-30 18:57:33', NULL, 1);
INSERT INTO `sys_menu` VALUES (3039, 3008, '0,3001,3008', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryCertificationContact:registry-certification-contact:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 18:57:44', '2026-03-30 18:57:44', NULL, 1);
INSERT INTO `sys_menu` VALUES (3040, 3009, '0,3001,3009', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryInvoiceInfo:registry-invoice-info:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 19:04:52', '2026-03-30 19:04:52', NULL, 1);
INSERT INTO `sys_menu` VALUES (3041, 3009, '0,3001,3009', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryInvoiceInfo:registry-invoice-info:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 19:05:03', '2026-03-30 19:05:03', NULL, 1);
INSERT INTO `sys_menu` VALUES (3042, 3009, '0,3001,3009', '更新', 'B', NULL, NULL, NULL, 'aioveuMallRegistryInvoiceInfo:registry-invoice-info:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 19:05:11', '2026-03-30 19:05:11', NULL, 1);
INSERT INTO `sys_menu` VALUES (3043, 3009, '0,3001,3009', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryInvoiceInfo:registry-invoice-info:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 19:05:20', '2026-03-30 19:05:20', NULL, 1);
INSERT INTO `sys_menu` VALUES (3044, 3010, '0,3001,3010', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppFilingRecord:registry-app-filing-record:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 19:05:33', '2026-03-30 19:05:33', NULL, 1);
INSERT INTO `sys_menu` VALUES (3045, 3010, '0,3001,3010', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppFilingRecord:registry-app-filing-record:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 19:05:43', '2026-03-30 19:05:43', NULL, 1);
INSERT INTO `sys_menu` VALUES (3046, 3010, '0,3001,3010', '更新', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppFilingRecord:registry-app-filing-record:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 19:05:54', '2026-03-30 19:05:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (3047, 3010, '0,3001,3010', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryAppFilingRecord:registry-app-filing-record:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 19:06:04', '2026-03-30 19:06:04', NULL, 1);
INSERT INTO `sys_menu` VALUES (3048, 3011, '0,3001,3011', '查询', 'B', NULL, NULL, NULL, 'aioveuMallRegistryOperationLog:registry-operation-log:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 19:06:15', '2026-03-30 19:06:15', NULL, 1);
INSERT INTO `sys_menu` VALUES (3049, 3011, '0,3001,3011', '新增', 'B', NULL, NULL, NULL, 'aioveuMallRegistryOperationLog:registry-operation-log:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 19:06:24', '2026-03-30 19:06:24', NULL, 1);
INSERT INTO `sys_menu` VALUES (3050, 3011, '0,3001,3011', '更新', 'B', NULL, NULL, NULL, 'aioveuMallRegistryOperationLog:registry-operation-log:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 19:06:35', '2026-03-30 19:06:35', NULL, 1);
INSERT INTO `sys_menu` VALUES (3051, 3011, '0,3001,3011', '删除', 'B', NULL, NULL, NULL, 'aioveuMallRegistryOperationLog:registry-operation-log:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 19:06:44', '2026-03-30 19:06:44', NULL, 1);
INSERT INTO `sys_menu` VALUES (3052, 3012, '0,3002,3012', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayConfig:pay-config:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 19:06:59', '2026-03-30 19:06:59', NULL, 1);
INSERT INTO `sys_menu` VALUES (3053, 3012, '0,3002,3012', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayConfig:pay-config:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 19:07:09', '2026-03-30 19:07:09', NULL, 1);
INSERT INTO `sys_menu` VALUES (3054, 3012, '0,3002,3012', '更新', 'B', NULL, NULL, NULL, 'aioveuMallPayConfig:pay-config:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 19:07:17', '2026-03-30 19:07:17', NULL, 1);
INSERT INTO `sys_menu` VALUES (3055, 3012, '0,3002,3012', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayConfig:pay-config:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 19:07:25', '2026-03-30 19:07:25', NULL, 1);
INSERT INTO `sys_menu` VALUES (3056, 3013, '0,3002,3013', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigWechat:pay-config-wechat:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 19:07:34', '2026-03-30 19:07:34', NULL, 1);
INSERT INTO `sys_menu` VALUES (3057, 3013, '0,3002,3013', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigWechat:pay-config-wechat:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 19:07:44', '2026-03-30 19:07:44', NULL, 1);
INSERT INTO `sys_menu` VALUES (3058, 3013, '0,3002,3013', '更新', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigWechat:pay-config-wechat:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 19:07:54', '2026-03-30 19:07:54', NULL, 1);
INSERT INTO `sys_menu` VALUES (3059, 3013, '0,3002,3013', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigWechat:pay-config-wechat:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 19:08:02', '2026-03-30 19:08:02', NULL, 1);
INSERT INTO `sys_menu` VALUES (3060, 3014, '0,3002,3014', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigDummy:pay-config-dummy:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 19:08:13', '2026-03-30 19:08:13', NULL, 1);
INSERT INTO `sys_menu` VALUES (3061, 3014, '0,3002,3014', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigDummy:pay-config-dummy:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 19:08:24', '2026-03-30 19:08:24', NULL, 1);
INSERT INTO `sys_menu` VALUES (3062, 3014, '0,3002,3014', '更新', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigDummy:pay-config-dummy:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 19:08:32', '2026-03-30 19:08:32', NULL, 1);
INSERT INTO `sys_menu` VALUES (3063, 3014, '0,3002,3014', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigDummy:pay-config-dummy:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 19:08:41', '2026-03-30 19:08:41', NULL, 1);
INSERT INTO `sys_menu` VALUES (3064, 3015, '0,3002,3015', '查询', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigAlipay:pay-config-alipay:list', 0, 1, 1, 1, NULL, NULL, '2026-03-30 19:08:53', '2026-03-30 19:08:53', NULL, 1);
INSERT INTO `sys_menu` VALUES (3065, 3015, '0,3002,3015', '新增', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigAlipay:pay-config-alipay:create', 0, 1, 1, 2, NULL, NULL, '2026-03-30 19:09:03', '2026-03-30 19:09:03', NULL, 1);
INSERT INTO `sys_menu` VALUES (3066, 3015, '0,3002,3015', '更新', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigAlipay:pay-config-alipay:update', 0, 1, 1, 3, NULL, NULL, '2026-03-30 19:09:12', '2026-03-30 19:09:12', NULL, 1);
INSERT INTO `sys_menu` VALUES (3067, 3015, '0,3002,3015', '删除', 'B', NULL, NULL, NULL, 'aioveuMallPayConfigAlipay:pay-config-alipay:delete', 0, 1, 1, 4, NULL, NULL, '2026-03-30 19:09:22', '2026-03-30 19:09:22', NULL, 1);

SET FOREIGN_KEY_CHECKS = 1;
