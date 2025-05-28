package com.aioveu.dto;

import lombok.Data;

@Data
public class CancelGradeMessageDTO {
    private int page = 1;
    private int size = 10;
    /**
     * 商店Id
     */
    private int storeId;
    private String start;
    private String end;
}
