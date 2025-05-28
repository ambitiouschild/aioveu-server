package com.aioveu.dto;

import lombok.Data;

import java.util.Map;

/**
 * @description 统一通知参数
 * @author: 雒世松
 * @date: 2025/2/13 0021 0:26
 */
@Data
public class BaseNoticeDTO {

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 消息选项code
     */
    private String msgOptionCode;

    /**
     * 消息参数
     */
    private Map<String, Object> msgParam;

}
