package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.WaterPoolSaleGroup;
import com.aioveu.vo.user.BaseUser;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface WaterPoolSaleGroupDao extends BaseMapper<WaterPoolSaleGroup> {

    /**
     * 根据组id查找用户
     * @param saleGroupId
     * @return
     */
    List<String> getGroupUserIdList(Long saleGroupId);


}
