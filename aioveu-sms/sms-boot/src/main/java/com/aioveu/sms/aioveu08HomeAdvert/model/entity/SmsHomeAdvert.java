package com.aioveu.sms.aioveu08HomeAdvert.model.entity;


import com.aioveu.common.base.BaseEntity;
import com.aioveu.common.base.BaseEntityWithTenantId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @ClassName: SmsHomeAdvert
 * @Description TODO 首页广告配置（增加跳转路径）实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:32
 * @Version 1.0
 **/
@Data
@TableName("sms_home_advert")
public class SmsHomeAdvert extends BaseEntityWithTenantId {

    private static final long serialVersionUID = 1L;

    /**
     * 关联广告ID（sms_advert表）
     */
    private Long advertId;
    /**
     * 广告显示的图标URL
     */
    private String homeAdvertIcon;
    /**
     * 广告显示名称
     */
    private String homeAdvertName;
    /**
     * 高度（rpx/upx）
     */
    private Integer height;
    /**
     * 图片模式
     */
    private String imageMode;
    /**
     * 跳转路径
     */
    private String jumpPath;
    /**
     * 跳转类型：navigateTo, redirectTo, switchTab
     */
    private String jumpType;
    /**
     * 跳转参数（JSON格式）
     */
    private String jumpParams;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态：0-隐藏，1-显示
     */
    private Integer status;
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
}
