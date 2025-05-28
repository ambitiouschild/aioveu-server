package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExtensionUserDao;
import com.aioveu.entity.ExtensionUser;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ExtensionUserServiceImpl extends ServiceImpl<ExtensionUserDao, ExtensionUser> implements ExtensionUserService {

    @Autowired
    ExtensionUserDao extensionDao;

    @Override
    public void insterExtensionUser(ExtensionUser extension) {
        try {
            extensionDao.insert(extension);
        }catch (Exception e) {
            throw new SportException("新增出错");
        }
    }
}
