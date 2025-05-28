package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.WaterPoolSaleGroup;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface WaterPoolSaleGroupService extends IService<WaterPoolSaleGroup> {


    /**
     * 通过组名获取用户集合
     * @param name
     * @param companyId
     * @return
     */
    List<String> getGroupUserIdByGroupName(String name, Long companyId);

    /**
     * 根据店铺id获取店铺下销售组列表
     * @return
     */
    IPage<WaterPoolSaleGroup> getByStoreId(int page, int size,Long storeId);

    /**
     * 添加销售组
     * @param waterPoolSaleGroup
     * @return
     */
    Boolean create(WaterPoolSaleGroup waterPoolSaleGroup);


    /**
     * 根据id修改销售组数据
     * @param waterPoolSaleGroup
     * @return
     */
    boolean updGroupById(WaterPoolSaleGroup waterPoolSaleGroup);

    /**
     * 根据id删除销售组数据
     * @param id
     * @return
     */
    boolean deleteGroup(Long id);

}
