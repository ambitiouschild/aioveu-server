package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CheckRecordAccountConfig;
import com.aioveu.vo.CheckRecordAccountConfigVO;

import java.util.List;

/**
 * @Author： yao
 * @Date： 2024/11/27 10:43
 * @Describe：
 */
public interface CheckRecordAccountConfigService extends IService<CheckRecordAccountConfig> {

    /**
     * 分页查询
     * @param page
     * @param size
     * @param storeId
     * @return
     */
    IPage<CheckRecordAccountConfigVO> getPageList(Integer page, Integer size, Long storeId);


    /**
     * 获取详情
     * @param id
     * @return
     */
    CheckRecordAccountConfigVO detail(String id);
    /**
     * 根据门店id获取list
     * @param storeId
     * @return
     */
    List<CheckRecordAccountConfig> getListByStoreId(Long storeId);

    /**
     * 根据门店id、类型获取
     * @param storeId
     * @return
     */
    CheckRecordAccountConfig getOneByStoreId(Long storeId,Long code);

    /**
     * 更新状态
     * @param id
     * @param status
     * @return
     */
    boolean changeStatus(String id, Integer status);

    /**
     * 保存
     * @param config
     */
    boolean saveConfig(CheckRecordAccountConfig config);
}
