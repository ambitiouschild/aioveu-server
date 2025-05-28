package com.aioveu.vo;

import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/3 0003 23:52
 */
@Data
public class TopicStoreItemVO extends IdNameVO {

    private String cover;

    /**
     * 商家数
     */
    private int storeNumber;

    /**
     * 推广人数
     */
    private int pushNumber;

    private Date endTime;
}
