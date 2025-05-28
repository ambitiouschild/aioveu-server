package com.aioveu.vo;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/24 19:29
 */
@Data
public class VenueFieldVO {

    private Long fieldId;

    /**
     * 场地名称
     */
    private String name;

    private List<VenueFieldItemVO> timeList;

    private Long storeId;

    private Long venueId;

    private String venueName;

}
