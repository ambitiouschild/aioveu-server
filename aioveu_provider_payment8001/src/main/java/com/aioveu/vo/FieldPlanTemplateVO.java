package com.aioveu.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class FieldPlanTemplateVO {

    private String id;

    private String name;

    private Long fieldId;

    private String fieldNames;

    private Long venueId;

    private String venueName;

    private Date startDay;

    private Date endDay;

    private Time startTime;

    private Time endTime;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private BigDecimal weekendPrice;

    private BigDecimal holidayPrice;

    /**
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    private Integer timeType;

    private String dateList;

    private Boolean skipHoliday;

    private String timeRule;

    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<PriceRule> priceRule;

    private Integer status;

}
