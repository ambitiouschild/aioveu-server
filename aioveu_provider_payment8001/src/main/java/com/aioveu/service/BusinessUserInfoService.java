package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.BusinessUserInfo;
import com.aioveu.entity.OrderDetail;
import com.aioveu.vo.StoreUserPublicInfoVO;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface BusinessUserInfoService extends IService<BusinessUserInfo> {

    /**
     * 领取客资
     * @param orderDetail
     * @param pushUserId 地推人员
     * @param isNew
     * @param userId
     * @return
     */
    boolean receive(OrderDetail orderDetail, String pushUserId, boolean isNew, String userId);


    /**
     * 商户客资
     * @param page
     * @param size
     * @param username
     * @return
     */
    IPage<StoreUserPublicInfoVO> getList(int page, int size, String username);

    /**
     * 激活
     * @param id
     * @param username
     * @return
     */
    boolean active(Long id, String username);

    /**
     * 电话号码失效
     * @param id
     * @param username
     * @return
     */
    boolean phoneInvalid(Long id, String username);

    /**
     * 是否是新用户
     * @param storeId
     * @param userId
     * @param orderDetailId
     * @return
     */
    boolean isNew(Long storeId, String userId, String orderDetailId);

    /**
     * 商户减余额
     * @param storeId
     * @param orderDetailId
     * @param orderId
     * @param name
     * @param description
     * @param amount
     * @return
     */
    boolean reduceBalance(Long storeId, String orderDetailId, String orderId, String name, String description, BigDecimal amount);

    /**
     * 获取电话失效的客户列表
     * @param page
     * @param size
     * @param id
     * @return
     */
    IPage<StoreUserPublicInfoVO> getPhoneInvalid(Integer page, Integer size, Long id, String name);

    /**
     * 手机号正常
     * @param id
     * @return
     */
    boolean phoneNormal(Long id);

    /**
     * 审核通过
     * @param id
     * @return
     */
    boolean examineAdopt(Long id);
}
