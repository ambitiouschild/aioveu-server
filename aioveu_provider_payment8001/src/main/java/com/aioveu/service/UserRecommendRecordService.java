package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserRecommendRecord;

/**
 * @description
 * @author: xiaoyao
 * @date: 2022年10月10日
 */
public interface UserRecommendRecordService extends IService<UserRecommendRecord> {
    /**
     * 绑定地推人员推广信息
     *
     * @param data
     */
    void bindExtensionUser(UserRecommendRecord data);
}
