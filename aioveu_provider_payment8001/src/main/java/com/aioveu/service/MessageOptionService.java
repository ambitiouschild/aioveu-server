package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.MessageOption;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2025/4/25 17:33
 * @Describe：
 */
public interface MessageOptionService  extends IService<MessageOption> {
    /**
     * 根据storeId获取所有配置
     * @param storeId
     * @return
     */
    List<MessageOption> getListByStoreId(String storeId);
    /**
     * 根据code获取所有配置
     * @param code
     * @return
     */
    List<MessageOption> getListByCode(String code);

}
