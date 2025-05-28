package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class TopicDetailVO {

    private Long id;

    private String name;

    private Long categoryId;

    private Date startTime;

    private Date endTime;

    private String introduce;

    private String qa;

    private BigDecimal price;

    private Integer styleType;

    private BigDecimal reward;

    /**
     * 分享的地推人的用户id
     */
    private String pushUserId;

    private List<CategoryBaseVO> categoryList;

    private List<String> imageList;

}
