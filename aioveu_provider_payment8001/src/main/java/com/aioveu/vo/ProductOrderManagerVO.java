package com.aioveu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class ProductOrderManagerVO {

    private String id;

    private String name;

    private String categoryName;

    private BigDecimal amount;

    private Long categoryId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date payFinishTime;

    private String remark;

    private String codeUrl;

    private String customerName;

    private String customerTel;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date createDate;

    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date startDay;

    private String companyName;
}
