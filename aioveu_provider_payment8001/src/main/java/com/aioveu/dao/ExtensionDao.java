package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.User;
import com.aioveu.vo.user.UserVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtensionDao extends BaseMapper<User> {

    /**
     * 分页查询地推人员信息
     * @param page
     * @param username
     * @param id
     * @return
     */
    IPage<UserVo> selExtensionUser(IPage<UserVo> page, String username, String id);
}
