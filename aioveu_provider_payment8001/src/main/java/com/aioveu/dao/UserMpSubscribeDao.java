package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.UserMpSubscribe;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface UserMpSubscribeDao extends BaseMapper<UserMpSubscribe> {


    /**
     * 查询用户是否关注公众号
     * @param openId
     * @return
     */
    UserMpSubscribe getSubscribeStatusByOpenId(String openId);

}
