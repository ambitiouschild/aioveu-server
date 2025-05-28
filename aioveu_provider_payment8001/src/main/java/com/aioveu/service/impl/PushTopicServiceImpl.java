package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.PushTopicDao;
import com.aioveu.entity.PushTopic;
import com.aioveu.exception.SportException;
import com.aioveu.service.PushTopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class PushTopicServiceImpl extends ServiceImpl<PushTopicDao, PushTopic> implements PushTopicService {

    @Override
    public Long getByUserId(String userId) {
        QueryWrapper<PushTopic> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(PushTopic::getUserId, userId);
        PushTopic pushTopic = getOne(queryWrapper);
        if (pushTopic != null) {
            return pushTopic.getTopicId();
        }
        return null;
    }

    @Override
    public boolean create(PushTopic pushTopic) {
        if (pushTopic.getTopicId() != null && pushTopic.getUserId() != null){
            PushTopic pushT = getOne(new QueryWrapper<PushTopic>().lambda().eq(PushTopic::getUserId, pushTopic.getUserId()));
            if (Objects.isNull(pushT)){
                pushTopic.setTopicId(pushTopic.getTopicId());
                pushTopic.setUserId(pushTopic.getUserId());
                save(pushTopic);
            }else {
                pushT.setTopicId(pushTopic.getTopicId());
                pushT.setUpdateDate(new Date());
                getBaseMapper().updateById(pushT);
            }
            return true;
        }else {
            throw new SportException("主题id或者推广人员id为空");
        }
    }
}
