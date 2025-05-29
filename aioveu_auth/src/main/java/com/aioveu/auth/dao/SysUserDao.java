package com.aioveu.auth.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.auth.model.po.SysUser;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2017/11/17 10:42
 */
@Repository
public interface SysUserDao extends BaseMapper<SysUser> {

    /**
     * openId查找用户
     * @param openId
     * @return
     */
    SysUser getByOpenId(String openId);


}
