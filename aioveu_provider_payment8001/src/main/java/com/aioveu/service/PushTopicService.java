package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.PushTopic;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface PushTopicService extends IService<PushTopic> {

    /**
     * 获取用户的推送主题
     * @param userId
     * @return
     */
    Long getByUserId(String userId);

    /**
     * 为推广人员分配活动主题
     * @param pushTopic   推广人员
     * @return
     */
    boolean create(PushTopic pushTopic);
}
