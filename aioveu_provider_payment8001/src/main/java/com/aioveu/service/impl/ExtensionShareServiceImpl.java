package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExtensionShareDao;
import com.aioveu.entity.ExtensionShare;
import com.aioveu.service.ExtensionShareService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: xiaoyao
 */
@Slf4j
@Service
public class ExtensionShareServiceImpl extends ServiceImpl<ExtensionShareDao, ExtensionShare> implements ExtensionShareService {

    @Override
    public boolean recodeShare(ExtensionShare extensionShare) {
        return save(extensionShare);
    }

    @Override
    public int selCountShareCondition(String userId, String themeId) {
        LambdaQueryWrapper<ExtensionShare> wrapper = Wrappers.lambdaQuery();
        if (themeId != null) {
            wrapper.eq(ExtensionShare::getThemeId, themeId);
        }
        if (userId != null) {
            wrapper.eq(ExtensionShare::getExtensionId, userId);
        }
        return count(wrapper);
    }
}
