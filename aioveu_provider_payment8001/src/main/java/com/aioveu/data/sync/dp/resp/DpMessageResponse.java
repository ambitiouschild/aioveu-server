package com.aioveu.data.sync.dp.resp;

import lombok.Data;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/3/31 10:47
 */
@Data
public class DpMessageResponse {

    private DpBaseResponse baseResponse;

    private Integer total;

    private Integer unReadCount;

    private List<DpMessage> messageList;

    public boolean success() {
        return baseResponse != null && "00000".equals(baseResponse.getCode());
    }

}
