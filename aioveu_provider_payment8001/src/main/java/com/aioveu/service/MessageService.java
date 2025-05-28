package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Message;

import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface MessageService extends IService<Message> {

    /**
     * 创建
     * @param message
     * @return
     */
    boolean create(Message message);


    /**
     * 获取消息列表
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<Message> getList(int page, int size, Long storeId, Integer msgType);

    /**
     * 已读
     * @param id
     * @return
     */
    boolean read(Long id);

    /**
     * 获取未读消息
     * @param storeId
     * @param msgType
     * @return
     */
    int getStoreUnreadMessageNumber(Long storeId, Integer msgType);

    /**
     * 获取指定菜单的未读消息数量
     * @param storeId
     * @param menuCodeList
     * @return
     */
    Map<String, Integer> getMenuUnreadNumber(Long storeId, List<String> menuCodeList);

    /**
     * 已读所有未读消息
     * @param storeId
     * @param msgType
     * @return
     */
    boolean readAll(Long storeId, Integer msgType);


}
