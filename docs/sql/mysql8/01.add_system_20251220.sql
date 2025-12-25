

use aioveu_system;
-- 添加逻辑删除字段
ALTER TABLE `sys_user` ADD COLUMN is_deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标志：0-未删除，1-已删除';


-- 为现有数据设置默认值
UPDATE `sys_user` SET is_deleted = 0 WHERE is_deleted IS NULL;