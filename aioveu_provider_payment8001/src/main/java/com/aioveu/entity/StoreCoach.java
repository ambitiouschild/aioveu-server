package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @description 商铺教练
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_store_coach")
@Data
public class StoreCoach extends IdNameEntity{

    private Long companyId;

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    @NotEmpty(message = "介绍不能为空")
    private String introduce;

    @NotEmpty(message = "头像不能为空")
    private String url;

    private Integer priority;

    /**
     * 擅长
     */
    private String expertise;

    /**
     * 用户类型 默认1 教练 4 销售
     */
    private Integer userType;

    @TableField(exist = false)
    private List<CoachCertificate> certificate;

}
