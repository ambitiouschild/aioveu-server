package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.entity.UserExtensionAccount;
import com.aioveu.vo.UserExtensionAccountVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserExtensionAccountDao extends BaseMapper<UserExtensionAccount> {

    /**
     * 获取推广账户列表
     * @param iPage
     * @param phone
     * @return
     */
    IPage<UserExtensionAccountVO> getAllList(Page<UserExtensionAccountVO> iPage, String phone);

}
