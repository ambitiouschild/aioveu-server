package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.User;
import com.aioveu.entity.UserAddress;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UserAddressService extends IService<UserAddress> {

    /**
     * 根据地址创建
     * @param user
     * @param address
     * @return
     */
    Long createByAddress(User user, String address);


}
