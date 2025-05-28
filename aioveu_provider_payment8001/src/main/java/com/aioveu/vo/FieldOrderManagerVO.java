package com.aioveu.vo;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class FieldOrderManagerVO {

    private String id;

    private String name;

    private Date createDate;

    private Integer status;

    private String bookingUser;

    private String venueName;

    /**
     * 退款时间
     */
    private Date refundTime;

    private Integer refundStatus;

    private Map<String, Map<String, List<FieldOrderDetailVO>>> fieldOrderDetailMap;
}
