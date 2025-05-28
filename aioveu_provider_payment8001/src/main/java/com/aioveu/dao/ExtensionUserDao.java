package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ExtensionUser;
import com.aioveu.entity.User;
import com.aioveu.vo.user.UserVo;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtensionUserDao extends BaseMapper<ExtensionUser> {

}
