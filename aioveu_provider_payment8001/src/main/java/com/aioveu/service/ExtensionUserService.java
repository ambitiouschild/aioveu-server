package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.ExtensionUserDTO;
import com.aioveu.entity.ExtensionUser;
import com.aioveu.entity.User;
import com.aioveu.vo.ExtensionPageCodeVo;
import com.aioveu.vo.user.UserVo;

/**
 * @description
 * @author: xiaoyao
 * @date: 2022年10月10日
 */
public interface ExtensionUserService extends IService<ExtensionUser> {

    void insterExtensionUser(ExtensionUser extension);
}
