package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.UserExtensionAccount;
import com.aioveu.vo.UserExtensionAccountVO;
import com.aioveu.vo.api.CompanyItemVO;

import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserExtensionAccountService extends IService<UserExtensionAccount> {

    /**
     * 增加用户推广余额
     * @param userId
     * @param amount
     * @param name
     * @param description
     * @param orderId
     * @return
     */
    boolean addBalance(String userId, BigDecimal amount, String name, String description, String orderId);

    /**
     * 减去用户推广余额
     * @param userId
     * @param amount
     * @param name
     * @param description
     * @param orderId
     * @param negative 是否可以为负数
     * @return
     */
    boolean reduceBalance(String userId, BigDecimal amount, String name, String description, String orderId, boolean negative);

    /**
     * 获取推广账户
     * @param userId
     * @return
     */
    UserExtensionAccount getByUserId(String userId);

    /**
     * 获取列表
     * @param page
     * @param size
     * @param keyword
     * @return
     */
    IPage<UserExtensionAccountVO> getAll(int page, int size, String keyword);

}
