package com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: ManagerMenuHomeBanner
 * @Description TODO 管理端app首页滚播栏实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:31
 * @Version 1.0
 **/


@Getter
@Setter
@TableName("manager_menu_home_banner")
public class ManagerMenuHomeBanner extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 滚播栏标题
     */
    private String title;
    /**
     * 滚播栏图片地址
     */
    private String imageUrl;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    /**
     * 状态(1:开启；0:关闭)
     */
    private Integer status;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 跳转链接
     */
    private String redirectUrl;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除：0-正常 1-删除
     */
    private Integer deleted;
    /**
     * 版本号（用于乐观锁）
     */
    private Integer version;
    /**
     * 租户ID
     */
    private Long tenantId;
}
