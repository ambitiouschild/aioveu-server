package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.Order;
import com.aioveu.entity.UserBalanceChange;
import com.aioveu.entity.UserVipCard;
import com.aioveu.entity.VipCard;
import com.aioveu.vo.UserVipCardItemVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
public interface UserVipCardService extends IService<UserVipCard> {

    List<UserVipCard> getActiveList(Long companyId, String userId);

    List<UserVipCard> getManageList(Long companyId, String phone, String vipCardNo, String username);

    UserVipCard getOneById(Long id);

    void saveUserVipCard(UserVipCard userVipCard, String createUserId);

    /**
     * 领取优惠券
     * @param vipCardId
     * @param userId
     * @return
     */
    boolean receiveVipCard(Long vipCardId, String userId);

    /**
     * 获取指定产品分类的用户的会员卡
     * @param userId
     * @param productCategoryId
     * @param companyId
     * @param storeId
     * @return
     */
    UserVipCard getUserVipCard(String userId, Long productCategoryId, Long companyId, Long storeId);


    /**
     * 会员卡充值
     * @param order
     * @param vipCard
     * @param userId
     * @param companyId
     * @param storeId
     * @return
     */
    UserVipCard recharge(Order order, VipCard vipCard, String userId, Long companyId, Long storeId);

    /**
     * 根据消费记录 会员卡 退款
     * @param userBalanceChange
     * @return
     */
    boolean refund(UserBalanceChange userBalanceChange);

    boolean payment(Order order);

    UserVipCard getUserVipCard(String userId, String productCategoryCode, Long companyId);

    UserVipCard getUserVipCard(String userId, Long vipCardId, Long companyId);

    /**
     * 获取我的会员卡
     * @param username
     * @return
     */
    List<UserVipCardItemVO> getMyVipCardList(String username);

    /**
     * 获取公司会员卡余额总数
     * @param companyId
     * @return
     */
    Double getVipCardAmount(Long companyId);




}
