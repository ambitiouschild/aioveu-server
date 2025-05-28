package com.aioveu.data.sync.qyd.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/31 15:51
 */
@Data
public class QydFieldPlan {

    private String venueName;

    private String fieldName;

    private String fieldDay;

    private String startTime;

    private String endTime;

    private BigDecimal amount;

}
