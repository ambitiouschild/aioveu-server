package com.aioveu.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/5/5 0005 23:16
 */
@Data
public class FieldPlanDTO implements Serializable {

    private Long id;

    private Long fieldId;

    private String fieldName;

    private Long venueId;

    private String venueName;

    private Date fieldDay;

    private Time startTime;

    private Time endTime;

    private BigDecimal price;

    private BigDecimal vipPrice;
    /**
     * 教练价格
     */
    private BigDecimal coachPrice;

    private Integer status;

    private Long storeId;

    private Long companyId;

    private String name;

    private String remark;

    /**
     * 锁场平台
     * 默认QS是趣数系统
     * 平台编码对应sport_category分类表code值
     * {@link com.aioveu.enums.FieldPlanLockChannels}
     */
    private String lockChannel;
}
