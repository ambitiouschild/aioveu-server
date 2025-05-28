package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserReceiveCallDao;
import com.aioveu.entity.UserReceiveCall;
import com.aioveu.exception.SportException;
import com.aioveu.service.UserCallPoolService;
import com.aioveu.service.UserInfoService;
import com.aioveu.service.UserReceiveCallService;
import com.aioveu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserReceiveCallServiceImpl extends ServiceImpl<UserReceiveCallDao, UserReceiveCall> implements UserReceiveCallService {

    @Autowired
    private UserCallPoolService userCallPoolService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserReceiveCallService userReceiveCallService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteByIds(List<Long> idList, String userId, String nickname) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        List<UserReceiveCall> userReceiveCallList = userReceiveCallService.listByIds(idList);
        List<Long> userInfoList = userReceiveCallList.stream().map(UserReceiveCall::getUserInfoId).collect(Collectors.toList());
        userInfoService.batchRemove(userInfoList, userId, nickname);
        return justDeleteAndUpdateCount(idList, userId);
    }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean justDeleteAndUpdateCount(List<Long> idList, String userId) {
        if (removeByIds(idList) && userCallPoolService.addCount(userId, idList.size())) {
            return true;
        }
        throw new SportException("操作失败!");
    }

    @Override
    public boolean autoRemove30DayUnContact() {
        Date date = DateUtils.addDays(new Date(), 30);
        QueryWrapper<UserReceiveCall> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().ge(UserReceiveCall::getUpdateDate, date);
        List<UserReceiveCall> userReceiveCallList = list(queryWrapper);
        if (CollectionUtils.isEmpty(userReceiveCallList)) {
            log.info("没有超过30天未联系的用户");
            return true;
        }
        log.info("超过30天未联系的用户条数:" + userReceiveCallList.size());
        Map<String, List<UserReceiveCall>> userReceiveCallMap = userReceiveCallList.stream()
                .collect(Collectors.groupingBy(UserReceiveCall::getUserId));

        for (Map.Entry<String, List<UserReceiveCall>> entity : userReceiveCallMap.entrySet()) {
            justDeleteAndUpdateCount(entity.getValue().stream().map(UserReceiveCall::getId).collect(Collectors.toList()), entity.getKey());
        }
        return true;
    }

    @Override
    public UserReceiveCall getById(Long id) {
        QueryWrapper<UserReceiveCall> wrapper = new QueryWrapper<>();
        wrapper.lambda().eq(UserReceiveCall::getId,id);
        return this.getOne(wrapper);
    }

}
