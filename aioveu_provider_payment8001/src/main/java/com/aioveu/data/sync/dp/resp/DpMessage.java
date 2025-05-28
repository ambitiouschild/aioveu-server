package com.aioveu.data.sync.dp.resp;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/31 10:50
 */
@Data
public class DpMessage {

    private String content;

    private Integer type;

    private String title;

    private Integer configTypeId;

    private String configTypeName;

    /**
     * 消息时间
     */
    private Long gmtCreated;

    private Integer readStatus;

    private Integer msgGrade;


}
