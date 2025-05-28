package com.aioveu.vo;

import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/10 0010 21:05
 */
@Data
public class MessageReceiverVO {

    private Long id;
    /**
     * 消息Id
     */
    private Long msgConfigId;
    /**
     * 公司Id
     */
    private Long companyId;
    /**
     * 店铺Id
     */
    private Long storeId;
    /**
     * 昵称
     */
    private String name;
    /**
     * 电话
     */
    private String phone;

}
