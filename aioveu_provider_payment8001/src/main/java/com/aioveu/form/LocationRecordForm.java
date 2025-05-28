package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:43
 */
@Data
public class LocationRecordForm {

    private Long categoryId;

    private String categoryCode;

    private Boolean baidu;

    private String productId;

    private Double latitude;

    private String userId;

    private Double longitude;

}
