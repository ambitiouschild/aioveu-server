package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.PushSystemMessageDao;
import com.aioveu.entity.PushSystemMessage;
import com.aioveu.exception.SportException;
import com.aioveu.service.PushSystemMessageService;
import com.aioveu.service.UserService;
import com.mysql.cj.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/7 0007 23:26
 */
@Slf4j
@Service
public class PushSystemMessageServiceImpl extends ServiceImpl<PushSystemMessageDao, PushSystemMessage>
        implements PushSystemMessageService {

    @Autowired
    private UserService userService;

    @Override
    public String newest() {
        QueryWrapper<PushSystemMessage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .orderByDesc(PushSystemMessage::getCreateDate)
                .eq(PushSystemMessage::getStatus,1)
                .last("limit 1");
        PushSystemMessage pm = getOne(queryWrapper);
        if (pm != null) {
            return pm.getMessage();
        }
        return null;
    }

    @Override
    public IPage<PushSystemMessage> userList(int page, int size, String username) {
        return getBaseMapper().userList(new Page<>(page, size), userService.getUserIdFromCache(username));
    }

    @Override
    public boolean read(Long id, String username) {
        String userId = userService.getUserIdFromCache(username);
        if (getBaseMapper().getMessageReadRecord(id, userId) == null) {
            return getBaseMapper().read(id, userId) > 0;
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean createOrUpdate(PushSystemMessage pushSystemMessage) {
        if (Objects.nonNull(pushSystemMessage)) {
            if (pushSystemMessage.getId() == null){
                return save(pushSystemMessage);
            }
            return updateById(pushSystemMessage);
        }else {
            throw new SportException("消息操作失败");
        }
    }

    @Override
    public IPage<PushSystemMessage> findMessage(Integer page,Integer size,String name,Long id) {
        QueryWrapper<PushSystemMessage> pushSystemMessageQueryWrapper = new QueryWrapper<>();
        pushSystemMessageQueryWrapper.eq("status",1);
        if (StringUtils.isNullOrEmpty(name) && Objects.isNull(id)){
            return getBaseMapper().selectPage(new Page<>(page, size),pushSystemMessageQueryWrapper);
        }
        if (Objects.nonNull(id)){
            return getBaseMapper().selectPage(new Page<>(page, size),pushSystemMessageQueryWrapper.eq("id",id));
        }
        pushSystemMessageQueryWrapper.lambda().like(PushSystemMessage::getName,name);
        return getBaseMapper().selectPage(new Page<>(page, size),pushSystemMessageQueryWrapper);
    }

    @Override
    public Boolean low(Long id) {
        if (Objects.nonNull(id)){
            PushSystemMessage pushSystemMessage = new PushSystemMessage();
            pushSystemMessage.setId(id);
            pushSystemMessage.setStatus(0);
            return updateById(pushSystemMessage);
        }
        throw new SportException("系统通知消息id为空");
    }
}
