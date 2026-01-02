package com.aioveu.system.aioveu09Notice.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.time.LocalDateTime;

/**
 * @ClassName: Notice
 * @Description TODO  通知公告实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:39
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("sys_notice")
public class Notice extends BaseEntity {


    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 通知标题
     */
    private String title;
    /**
     * 通知内容
     */
    private String content;
    /**
     * 通知类型
     */
    private Integer type;

    /**
     * 发布人
     */
    private Long publisherId;

    /**
     * 通知等级（L: 低, M: 中, H: 高）
     */
    private String level;

    /**
     * 目标类型（1: 全体, 2: 指定）
     */
    private Integer targetType;

    /**
     * 目标用户ID集合
     */
    private String targetUserIds;

    /**
     * 发布状态（0: 未发布, 1: 已发布, -1: 已撤回）
     */
    private Integer publishStatus;

    /**
     * 发布时间
     */
    private LocalDateTime publishTime;

    /**
     * 撤回时间
     */
    private LocalDateTime revokeTime;

    /**
     * 创建人ID
     */
    private Long createBy;

    /**
     * 更新人ID
     */
    private Long updateBy;

    /**
     * 逻辑删除标识(0-未删除 1-已删除)
     */
    private Integer isDeleted;
}
