package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Order;
import com.aioveu.entity.UserBalanceChange;
import com.aioveu.entity.UserVipCard;
import com.aioveu.entity.VipCard;
import com.aioveu.enums.ChangeTypeStatus;
import com.aioveu.vo.UserBalanceChangeItemVO;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserBalanceChangeService extends IService<UserBalanceChange> {

    /**
     * 余额增加记录
     * @param userId
     * @param amount
     * @param orderId
     * @param name
     * @param description
     * @param accountType
     * @return
     */
    boolean addBalanceRecord(String userId, BigDecimal amount, String orderId, String name,
                             String description, Integer accountType);

    /**
     * 余额减少记录
     * @param userId
     * @param amount
     * @param orderId
     * @param name
     * @param description
     * @param accountType
     * @return
     */
    boolean reduceBalanceRecord(String userId, BigDecimal amount, String orderId, String name,
                                String description, Integer accountType);


    /**
     * 获取列表
     * @param page
     * @param size
     * @param type
     * @param userId
     * @param accountType
     * @return
     */
    IPage<UserBalanceChangeItemVO> getAll(int page, int size, Integer type, String userId, Integer accountType, Long userVipCardId);

    /**
     * 获取客资扣除的金额
     * @param orderDetailId
     * @return
     */
    List<UserBalanceChange> getDeduction(String orderDetailId);

    List<UserBalanceChange> findByOrderId(String orderId);

    List<UserBalanceChange> findByVipStatistics(Long storeId, Date start, Date end, String name);

    List<UserBalanceChange> findByFieldStatistics(Long storeId, Date start, Date end);

    boolean insertUserBalanceChange(Order order, UserVipCard userVipCard, BigDecimal amount, ChangeTypeStatus changeType);

    boolean insertUserBalanceChange(UserVipCard userVipCard, BigDecimal amount, ChangeTypeStatus changeType, String userId, String name, String description);
}
