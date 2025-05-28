package com.aioveu.data.sync.dp.resp;

import lombok.Data;

import java.util.List;

/**
 * @description 美团点评 订单请求返回对象
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class DpOrderResponse {

    private Integer recordCount;

    private Integer startIndex;

    private Boolean isEnd;

    private Integer nextStartIndex;

    private List<DpOrder> list;

}
