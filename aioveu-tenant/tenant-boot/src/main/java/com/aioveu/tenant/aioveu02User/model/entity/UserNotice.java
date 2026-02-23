package com.aioveu.tenant.aioveu02User.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName: UserNotice
 * @Description TODO 用户通知公告实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:26
 * @Version 1.0
 **/
@Data
@TableName("sys_user_notice")
public class UserNotice extends BaseEntityWithTenantId {

    /**
     * 公共通知id
     */
    private Long noticeId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 读取状态，0未读，1已读
     */
    private Integer isRead;
    /**
     * 用户阅读时间
     */
    private LocalDateTime readTime;

    /**
     * 逻辑删除标识(0-未删除 1-已删除)
     */
    @TableLogic(value = "0", delval = "1")
    private Integer isDeleted;
}
