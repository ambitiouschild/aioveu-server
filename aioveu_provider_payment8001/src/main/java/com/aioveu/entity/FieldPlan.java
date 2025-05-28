package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * <p>
 * 场地计划表
 * </p>
 *
 * @author zyk
 * @since 2022-12-07
 */
@Data
@TableName("sport_field_plan")
public class FieldPlan extends IdEntity {

    private Long fieldId;

    private Long venueId;

    private Long companyId;

    private Long storeId;

    private Date fieldDay;

    private Time startTime;

    private Time endTime;

    private BigDecimal price;

    private BigDecimal vipPrice;
    /**
     * 教练价格
     */
    private BigDecimal coachPrice;

    private String remark;

    private String lockRemark;

    /**
     * 锁场平台
     * 平台编码对应sport_category分类表code值
     * {@link com.aioveu.enums.FieldPlanLockChannels}
     */
    private String lockChannel;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String gradeIds;

    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private Long fieldPlanLockId;

}
