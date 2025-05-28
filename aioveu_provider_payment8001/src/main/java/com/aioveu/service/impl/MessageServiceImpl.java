package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.MessageDao;
import com.aioveu.entity.Message;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.MessageType;
import com.aioveu.exception.SportException;
import com.aioveu.service.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class MessageServiceImpl extends ServiceImpl<MessageDao, Message> implements MessageService {

    @Override
    public boolean create(Message message) {
        if (StringUtils.isEmpty(message.getName())) {
            throw new SportException("名称不能为空");
        }
        return save(message);
    }

    @Override
    public IPage<Message> getList(int page, int size, Long storeId, Integer msgType) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Message::getStoreId, storeId)
                .eq(Message::getMsgType, msgType)
                .orderByDesc(Message::getCreateDate);
        return page(new Page<>(page, size), queryWrapper);
    }

    @Override
    public boolean read(Long id) {
        Message message = new Message();
        message.setId(id);
        message.setStatus(2);
        return updateById(message);
    }

    @Override
    public int getStoreUnreadMessageNumber(Long storeId, Integer msgType) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Message::getStoreId, storeId)
                .eq(Message::getMsgType, msgType)
                .eq(Message::getStatus, DataStatus.NORMAL.getCode());
        return count(queryWrapper);
    }

    @Override
    public Map<String, Integer> getMenuUnreadNumber(Long storeId, List<String> menuCodeList) {
        Map<String, Integer> numMap = new HashMap<>();
        for (String menuCode : menuCodeList) {
            if ("operate-message".equals(menuCode)) {
                numMap.put(menuCode, getStoreUnreadMessageNumber(storeId, MessageType.OperateMessage.getCode()));
            } else if ("message".equals(menuCode)) {
                numMap.put(menuCode, getStoreUnreadMessageNumber(storeId, MessageType.SystemMessage.getCode()));
            }
        }
        return numMap;
    }

    @Override
    public boolean readAll(Long storeId, Integer msgType) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(Message::getStoreId, storeId)
                .eq(Message::getMsgType, msgType)
                .eq(Message::getStatus, DataStatus.NORMAL.getCode());
        List<Message> msgList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(msgList)) {
            List<Message> upList = new ArrayList<>(msgList.size());
            for (Message item : msgList) {
                Message up = new Message();
                up.setId(item.getId());
                up.setStatus(2);
                upList.add(up);
            }
            return updateBatchById(upList);
        }
        return false;
    }
}
