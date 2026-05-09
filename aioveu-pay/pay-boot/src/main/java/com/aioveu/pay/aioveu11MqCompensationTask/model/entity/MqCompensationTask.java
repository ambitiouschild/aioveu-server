package com.aioveu.pay.aioveu11MqCompensationTask.model.entity;


import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: MqCompensationTask
 * @Description TODO MQ补偿任务实体对象
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:47
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("mq_compensation_task")
public class MqCompensationTask extends BaseEntityWithTenantId {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID，0表示平台默认
     */
    private Long tenantId;
    /**
     * 任务类型:send_retry
     */
    private String taskType;
    /**
     * 业务ID
     */
    private String bizId;
    /**
     * 业务数据
     */
    private String bizData;
    /**
     * 状态:0-待处理,1-处理中,2-成功,3-失败
     */
    private Integer status;
    /**
     * 重试次数
     */
    private Integer retryCount;
    /**
     * 下次执行时间
     */
    private LocalDateTime nextExecuteTime;
    /**
     * 执行结果
     */
    private String executeResult;
    /**
     * 错误信息
     */
    private String errorMsg;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
