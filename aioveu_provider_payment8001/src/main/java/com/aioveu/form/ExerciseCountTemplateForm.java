package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class ExerciseCountTemplateForm {

    private String id;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "店铺不能为空")
    private Long storeId;

    private String image;

    private BigDecimal originalPrice;

    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @NotNull(message = "小时价格不能为空")
    private BigDecimal hourPrice;

    /**
     * 会员折扣
     */
    private Double vipDiscount;

    private String description;

    private String process;

    private String requirement;

    /**
     * 限制人数
     */
    private Integer limitNumber;

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

    /**
     * 模板状态  1正常 2下架
     */
    private Integer status;

    /**
     * 场馆id
     */
    private Long venueId;

    /**
     * 场馆名称 用户审批展示用的
     */
    private String venueName;

    /**
     * 场馆场地id集合
     */
    private String venueFieldIds;

    /**
     * 场地名称集合
     */
    private String venueFieldNames;

    private String createUsername;

}
