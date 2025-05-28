package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.UserBalanceChange;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserBalanceChangeDao extends BaseMapper<UserBalanceChange> {

    List<UserBalanceChange> getDeduction(String orderDetailId);

    List<UserBalanceChange> findByVipStatistics(Long storeId, Date start, Date end, String name);

    /**
     * 统计店铺指定时间范围内会员卡消费明细
     * @param storeId
     * @param start
     * @param end
     * @return
     */
    List<UserBalanceChange> findByFieldStatistics(Long storeId, Date start, Date end);

}
