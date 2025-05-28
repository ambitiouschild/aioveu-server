package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/23 17:06
 */
@Data
public class StoreCoachVO {

    private String userId;

    private Long id;

    private String name;

    private String introduce;

    private String url;

    private List<CoachTagVO> tagList;
}
