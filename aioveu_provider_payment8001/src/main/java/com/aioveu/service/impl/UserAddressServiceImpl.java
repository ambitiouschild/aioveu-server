package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserAddressDao;
import com.aioveu.entity.User;
import com.aioveu.entity.UserAddress;
import com.aioveu.exception.SportException;
import com.aioveu.service.UserAddressService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserAddressServiceImpl extends ServiceImpl<UserAddressDao, UserAddress> implements UserAddressService {


    @Override
    public Long createByAddress(User user, String address) {
        UserAddress userAddress = new UserAddress();
        userAddress.setUserId(user.getId());
        userAddress.setAddress(address);
        userAddress.setUsername(user.getName());
        userAddress.setPhone(user.getPhone());
        userAddress.setCityId(-1L);
        userAddress.setProvinceId(-1L);
        userAddress.setRegionId(-1L);
        if (save(userAddress)) {
            return userAddress.getId();
        }
        throw new SportException("地址新建失败!");
    }
}
