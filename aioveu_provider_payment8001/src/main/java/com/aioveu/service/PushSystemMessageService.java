package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.PushSystemMessage;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:25
 */
public interface PushSystemMessageService extends IService<PushSystemMessage> {

    /**
     * 最新的消息
     * @return
     */
    String newest();

    /**
     * 查询消息内容
     * @param page
     * @param size
     * @param username
     * @return
     */
    IPage<PushSystemMessage> userList(int page, int size, String username);

    /**
     * 已读
     * @param id
     * @param username
     * @return
     */
    boolean read(Long id, String username);

    /**
     * 创建或者更改消息通知
     * @param pushSystemMessage
     * @return
     */
    Boolean createOrUpdate(PushSystemMessage pushSystemMessage);

    /**
     * 查询系统通知消息
     * @param name
     * @return
     */
    IPage<PushSystemMessage> findMessage(Integer page,Integer size,String name,Long id);

    /**
     * 下架消息
     * @param id
     * @return
     */
    Boolean low(Long id);
}
