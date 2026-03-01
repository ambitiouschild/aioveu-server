-- 文件信息表
CREATE TABLE sys_file (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
                          name VARCHAR(255) NOT NULL COMMENT '文件名',
                          url VARCHAR(500) NOT NULL COMMENT 'CDN地址（主）',
                          direct_url VARCHAR(500) COMMENT 'OSS直链地址（备）',
                          path VARCHAR(500) NOT NULL COMMENT '存储路径',
                          size BIGINT COMMENT '文件大小（字节）',
                          content_type VARCHAR(100) COMMENT '文件类型',
                          module VARCHAR(50) DEFAULT 'common' COMMENT '模块',

    -- CDN状态
                          cdn_status VARCHAR(20) DEFAULT 'pending' COMMENT 'CDN状态: pending/processing/completed/failed',
                          cdn_task_id VARCHAR(100) COMMENT 'CDN预热任务ID',
                          cdn_ready_time DATETIME COMMENT 'CDN就绪时间',

    -- 基础字段
                          tenant_id BIGINT DEFAULT 0 COMMENT '租户ID',
                          create_by BIGINT COMMENT '创建人',
                          create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                          update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

                          INDEX idx_path (path(100)),
                          INDEX idx_cdn_status (cdn_status),
                          INDEX idx_create_time (create_time)
) COMMENT='文件信息表';