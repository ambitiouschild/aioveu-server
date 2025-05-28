package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.MessageConfig;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface MessageConfigService extends IService<MessageConfig> {

    /**
     * 根据消息类型获取获取门店所有配置
     * @param storeId
     * @param msgCode
     * @return
     */
    List<MessageConfig> getList(Long storeId, String msgCode);
    /**
     * 根据消息类型获取店铺的消息配置
     * @param storeId
     * @param msgCode
     * @return
     */
    List<MessageConfig> getMessageConfigByStoreAndMsgCode(Long storeId, String msgCode);

    /**
     * 获取小程序订阅消息模板id
     * @param storeId
     * @param msgCodes
     * @return
     */
    List<String> getMiniAppTemplateIdByMsgCode(Long storeId, String msgCodes);

    /**
     * 更新状态，启用、停用
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(String id, Integer status);



}
