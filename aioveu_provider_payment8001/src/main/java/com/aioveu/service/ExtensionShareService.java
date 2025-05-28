package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ExtensionShare;

/**
 * @description
 * @author: xy
 */
public interface ExtensionShareService extends IService<ExtensionShare> {


    /**
     * 分享记录
     * @param dataDTO
     * @return
     */
    boolean recodeShare(ExtensionShare dataDTO);

    /**
     * 按条件查询分享次数
     * @param userId
     * @param themeId
     * @return
     */
    int selCountShareCondition(String userId, String themeId);

}
