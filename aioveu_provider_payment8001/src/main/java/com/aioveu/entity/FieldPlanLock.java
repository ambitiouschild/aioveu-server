package com.aioveu.entity;

import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.aioveu.vo.PriceRule;
import lombok.Data;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@TableName("sport_field_plan_lock")
@Data
public class FieldPlanLock extends IdNameEntity {

    private Long venueId;

    private Date expiryDate;

    private String lockRule;

    @TableField(exist = false)
    private String venueName;

    @TableField(exist = false)
    private List<PriceRule> lockRules;

    public List<PriceRule> getLockRules() {
        if (lockRules == null) {
            lockRules = new ArrayList<>();
            if (!StringUtils.isEmpty(lockRule)) {
                lockRules = JSONArray.parseArray(lockRule, PriceRule.class);
                for (PriceRule rule : lockRules) {
                    rule.setId(this.getId());
                    rule.setExpiryDate(DateFormatUtils.format(this.getExpiryDate(), "yyyy-MM-dd"));
                    rule.setRemark(this.getName());
                }
            }
        }
        return lockRules;
    }

    @TableField(exist = false)
    private String expiryDay;

    public String getExpiryDay() {
        if (expiryDay == null) {
            expiryDay = DateFormatUtils.format(this.getExpiryDate(), "yyyy-MM-dd");
        }
        return expiryDay;
    }
}
