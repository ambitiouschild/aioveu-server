package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 服务项目列表
 * @author: 雒世松
 * @date: 2025/11/23 17:06
 */
@Data
public class BaseServiceItemVO {

    private Long id;

    private String image;

    private String name;

    private String address;

    /**
     * 最低价格
     */
    private BigDecimal lowerPrice;

    /**
     * 距离
     */
    private Double distance;

    private Integer status;

    private String date;

    private Integer limitNumber;

    /**
     * 报名人数
     */
    private Integer enrollNumber;
    private Long storeProductCategoryId;
    private String storeProductCategoryName;

    /**
     * 原价
     */
    private BigDecimal originalPrice;

    /**
     * 开始时间
     */
    private Date startTime;


    /**
     * 结束时间
     */
    private Date endTime;
}
