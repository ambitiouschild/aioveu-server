package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/10/3 0003 23:52
 */
@Data
public class TopicCheckDetailVO {

    private String storeName;

    private String address;

    private List<TopicCheckItemVO> exerciseList;

    private String storeLogo;


}
