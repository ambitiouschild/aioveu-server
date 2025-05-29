package com.aioveu.gateway.service.impl;

import com.aioveu.auth.common.model.SysConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {

    @Autowired
    private RedisTemplate redisTemplate;

    public List<String> getWhiteList() {
        List<String> urlList = redisTemplate.opsForList().range(SysConstant.URL_WHITELIST, 0, -1);
        if (urlList == null) {
            return new ArrayList<>();
        }
        return urlList;
    }


}
