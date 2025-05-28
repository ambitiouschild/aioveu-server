package com.aioveu.data.sync.dp.resp;

import lombok.Data;

import java.util.List;

/**
 * @description 美团点评 场馆信息
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class DpVenueResponse {

    private Integer code;

    private String msg;

    private Boolean success;

    private List<DpVenue> data;


}
