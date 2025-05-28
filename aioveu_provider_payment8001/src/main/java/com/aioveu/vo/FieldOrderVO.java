package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class FieldOrderVO {

    private String id;

    private String storeId;

    private String storeName;

    private String storeAddress;

    private String address;

    private Date createDate;

    private Integer status;

    private String venueName;

    private Map<String, Map<String, List<FieldOrderDetailVO>>> fieldOrderDetailMap;
}
