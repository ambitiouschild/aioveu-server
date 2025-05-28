package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.WxOpenOnlineVersion;

/**
 * @Author： yao
 * @Date： 2025/2/14 15:28
 * @Describe：
 */
public interface WxOpenOnlineVersionService extends IService<WxOpenOnlineVersion> {

    /**
     * 更新线上版本,删除旧版本
     * @param appId
     * @param status
     * @return
     */
    Boolean deleteByAppId(String appId,Integer status);

}
