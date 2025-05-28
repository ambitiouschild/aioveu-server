package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.aioveu.vo.PriceRule;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/6 0006 22:57
 */
@Data
public class FieldPlanTemplateForm {

    private String id;

    private String name;

    private String fieldIds;

    private Long venueId;

    private Long gradeId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDay;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date endDay;

    private String startTime;

    private String endTime;

    private String remark;

    /**
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    private Integer timeType;

    private Boolean skipHoliday;

    /**
     * 时间列表
     */
    private List<String> dateList;

    private List<Long> fieldIdList;

    /**
     * 模板状态  1正常 2下架
     */
    private Integer status;

    /**
     * 价格单位，60 按每小时，30 按每半小时
     */
    private Integer priceTimeUnit;

    private List<PriceRule> priceRules;

    private List<PriceRule> lockRules;
}
