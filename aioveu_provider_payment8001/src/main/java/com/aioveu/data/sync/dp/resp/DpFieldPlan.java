package com.aioveu.data.sync.dp.resp;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/31 15:50
 */
@Data
public class DpFieldPlan {

    private String venueName;

    private String fieldName;

    private String fieldDay;

    private String startTime;

    private String endTime;

    private BigDecimal amount;

    private String remark;

    private String lockRemark;

    private String lockChannel;

    private String lockChannelName;

}
