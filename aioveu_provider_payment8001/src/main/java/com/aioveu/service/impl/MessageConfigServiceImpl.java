package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.MessageConfigDao;
import com.aioveu.entity.MessageConfig;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.MessageConfigService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class MessageConfigServiceImpl extends ServiceImpl<MessageConfigDao, MessageConfig> implements MessageConfigService {

    @Override
    public List<MessageConfig> getList(Long storeId, String msgCode) {
        LambdaQueryWrapper<MessageConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MessageConfig::getStoreId, storeId)
                .eq(MessageConfig::getMsgCode, msgCode)
                .orderByAsc(MessageConfig::getNoticeCode);
        return list(queryWrapper);
    }

    @Override
    public List<MessageConfig> getMessageConfigByStoreAndMsgCode(Long storeId, String msgCode) {
        LambdaQueryWrapper<MessageConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MessageConfig::getStoreId, storeId)
                .eq(MessageConfig::getMsgCode, msgCode)
                .eq(MessageConfig::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public List<String> getMiniAppTemplateIdByMsgCode(Long storeId, String msgCodes) {
        String[] msgCodeArray = msgCodes.split(",");
        LambdaQueryWrapper<MessageConfig> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(MessageConfig::getStoreId, storeId)
                .eq(MessageConfig::getNoticeCode, "wechat_mini_app")
                .eq(MessageConfig::getStatus, DataStatus.NORMAL.getCode())
                .in(MessageConfig::getMsgCode, Arrays.asList(msgCodeArray));
        List<MessageConfig> messageConfigs = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(messageConfigs)) {
            List<String> templateList = new ArrayList<>();
            for (MessageConfig messageConfig : messageConfigs) {
                Map<String, Object> config = messageConfig.getConfig();
                templateList.add(config.get("templateId") + "");
            }
            return templateList;
        }
        return Collections.emptyList();
    }

    @Override
    public Boolean updateStatus(String id, Integer status) {
        LambdaUpdateWrapper<MessageConfig> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(MessageConfig::getStatus, status)
                .eq(MessageConfig::getId, id);
        return update(wrapper);
    }
}
