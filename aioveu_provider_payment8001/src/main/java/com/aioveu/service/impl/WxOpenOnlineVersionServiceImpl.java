package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.WxOpenOnlineVersionDao;
import com.aioveu.entity.WxOpenOnlineVersion;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.WxOpenOnlineVersionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Author： yao
 * @Date： 2025/2/14 15:34
 * @Describe：
 */
@Slf4j
@Service
public class WxOpenOnlineVersionServiceImpl extends ServiceImpl<WxOpenOnlineVersionDao, WxOpenOnlineVersion> implements WxOpenOnlineVersionService {

    @Override
    public Boolean deleteByAppId(String appId, Integer status) {
        LambdaUpdateWrapper<WxOpenOnlineVersion> wrapper = Wrappers.lambdaUpdate();
        wrapper.set(WxOpenOnlineVersion::getStatus, DataStatus.DELETE.getCode())
                .eq(WxOpenOnlineVersion::getAppId, appId)
                .eq(WxOpenOnlineVersion::getStatus, DataStatus.NORMAL.getCode());
        return update(wrapper);
    }
}
