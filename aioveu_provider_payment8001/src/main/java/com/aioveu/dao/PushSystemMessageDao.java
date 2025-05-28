package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.PushSystemMessage;
import com.aioveu.entity.PushSystemMessageRecord;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface PushSystemMessageDao extends BaseMapper<PushSystemMessage> {


    /**
     * 查询用户消息记录
     * @param page
     * @param userId
     * @return
     */
    IPage<PushSystemMessage> userList(IPage<PushSystemMessage> page, String userId);

    /**
     * 已读
     * @param pushSystemMessageId
     * @param userId
     * @return
     */
    int read(Long pushSystemMessageId, String userId);

    /**
     * 查询已读记录
     * @param pushSystemMessageId
     * @param userId
     * @return
     */
    PushSystemMessageRecord getMessageReadRecord(Long pushSystemMessageId, String userId);

}
