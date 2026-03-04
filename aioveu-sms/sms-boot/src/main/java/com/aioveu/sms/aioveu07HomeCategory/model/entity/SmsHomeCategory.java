package com.aioveu.sms.aioveu07HomeCategory.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: SmsHomeCategory
 * @Description TODO  首页分类配置实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/4 12:09
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("sms_home_category")
public class SmsHomeCategory extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 关联商品分类ID（pms_category表）
     */
    private Long categoryId;
    /**
     * 首页显示的图标URL
     */
    private String homeIcon;
    /**
     * 首页显示名称
     */
    private String homeName;
    /**
     * 跳转路径
     */
    private String jumpPath;
    /**
     * 跳转类型：navigateTo, redirectTo, switchTab
     */
    private String jumpType;
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
