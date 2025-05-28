package com.aioveu.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aioveu.vo.PriceRule;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TableName("sport_field_plan_template")
@Data
public class FieldPlanTemplate extends StringIdNameEntity {

    private String fieldIds;

    private Long venueId;

    private Long companyId;

    private Long storeId;

    private Date startDay;

    private Date endDay;

    private Time startTime;

    private Time endTime;

    /**
     * 时间类型 默认0 自由时间 1每星期 2每月 3每天
     */
    private Integer timeType;

    private String dateList;

    private Boolean skipHoliday;

    private String remark;

    /**
     * 价格单位，60 按每小时，30 按每半小时
     */
    private Integer priceTimeUnit;

    private String priceRule;
    private String lockRule;

    @TableField(exist = false)
    private List<PriceRule> priceRules;

    @TableField(exist = false)
    private List<PriceRule> lockRules;

    public List<PriceRule> getPriceRules() {
        if (priceRules == null) {
            priceRules = new ArrayList<>();
            if (!StringUtils.isEmpty(priceRule)) {
                priceRules = JSONArray.parseArray(priceRule, PriceRule.class);
            }
        }
        return priceRules;
    }

    public List<PriceRule> getLockRules() {
        if (lockRules == null) {
            lockRules = new ArrayList<>();
            if (!StringUtils.isEmpty(lockRule)) {
                lockRules = JSONArray.parseArray(lockRule, PriceRule.class);
            }
        }
        return lockRules;
    }
}
