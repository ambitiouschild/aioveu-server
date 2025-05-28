package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.WaterPoolSaleGroupUser;
import com.aioveu.vo.ExerciseCouponVO;
import com.aioveu.vo.StoreImageDetailVO;
import com.aioveu.vo.WaterPoolSaleGroupUserVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WaterPoolSaleGroupUserDao extends BaseMapper<WaterPoolSaleGroupUser> {

    /**
     * 查询公海销售组 组下的用户
     * @return
     */
    List<WaterPoolSaleGroupUser> getUserAll(Long id);

    /**
     * 根据销售组id查询 组下的用户信息
     * @param saleGroupId 销售组id
     * @return
     */
    IPage<WaterPoolSaleGroupUserVO> getGroupUserAll(IPage<StoreImageDetailVO> page,Long saleGroupId);

}
