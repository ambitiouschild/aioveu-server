package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.MessageReceiver;
import com.aioveu.form.MessageReceiverForm;
import com.aioveu.vo.MessageReceiverVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface MessageReceiverService extends IService<MessageReceiver> {
    /**
     * 根据消息类型获取获取门店所有配置
     * @param storeId
     * @param msgConfigId
     * @return
     */
    List<MessageReceiverVO> getVoList(Long storeId, Long msgConfigId);
    /**
     * 根据消息类型获取获取门店所有配置
     * @param storeId
     * @param msgConfigId
     * @return
     */
    List<MessageReceiver> getList(Long storeId, Long msgConfigId);

    /**
     * 更新状态，启用、停用
     * @param id
     * @param status
     * @return
     */
    Boolean updateStatus(Long id, Integer status);

    /**
     * 添加
     * @param form
     * @return
     */
    Boolean add(MessageReceiverForm form);


}
