package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.Company;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: luyao
 * @date: 2024/12/30 10:42
 */
@Repository
public interface CompanyDao extends BaseMapper<Company> {
    /**
     * 根据门店id获取公司信息
     * @param storeId
     * @return
     */
    Company getOneByStoreId(Long storeId);
}
