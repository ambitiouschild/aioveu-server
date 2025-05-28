package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.MessageOptionDao;
import com.aioveu.entity.GradeAge;
import com.aioveu.entity.MessageOption;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.MessageOptionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2025/4/25 17:34
 * @Describe：
 */
@Slf4j
@Service
public class MessageOptionServiceImpl extends ServiceImpl<MessageOptionDao, MessageOption> implements MessageOptionService {

    @Override
    public List<MessageOption> getListByStoreId(String storeId) {
        QueryWrapper<MessageOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MessageOption::getStoreId, storeId)
                .eq(MessageOption::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }

    @Override
    public List<MessageOption> getListByCode(String code) {
        QueryWrapper<MessageOption> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(MessageOption::getCode, code)
                .eq(MessageOption::getStatus, DataStatus.NORMAL.getCode());
        return list(queryWrapper);
    }
}
