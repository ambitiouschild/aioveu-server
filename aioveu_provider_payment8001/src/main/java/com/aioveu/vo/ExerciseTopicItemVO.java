package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/7 0007 10:46
 */
@Data
public class ExerciseTopicItemVO {

    private Long id;

    private String name;

    private BigDecimal price;

    private BigDecimal originalPrice;

    private String storeName;

    private String description;

    private String storeLogo;

    private String address;

    private String orderDetailId;

    private Integer distance;

    private List<String> imageList;

    private String rewardProduct;

    private Date endTime;
}