package com.aioveu.vo;

import lombok.Data;

@Data
public class FieldOrderDetailVO {

    private String id;

    private Integer status;

    private String phone;

    private String consumeCode;

    private Long fieldPlanId;

    private String fieldDay;

    private String startTime;

    private String endTime;

    private String venueName;

    private String fieldName;

    private String qrCode;
}
