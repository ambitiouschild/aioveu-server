package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.dto.ExtensionUserDTO;
import com.aioveu.entity.User;
import com.aioveu.vo.user.UserVo;

/**
 * @description
 * @author: xiaoyao
 * @date: 2022年10月10日
 */
public interface ExtensionService extends IService<User> {


    /**
     * 注册
     * @param dataDTO
     * @return
     */
    void create(ExtensionUserDTO dataDTO);

    /**
     * 通过地推人员审核状态
     *
     * @param user
     * @return
     */
    void modifyExamine(User user);

    /**
     * 查找地推人员
     * @param page
     * @param size
     * @param username
     * @param id
     * @return
     */
    IPage<UserVo> selExtensionUser(int page, int size, String username, String id);

    /**
     * 修改地推人员信息
     * @param user
     */
    Integer modifyExtensionUser(User user);

    /**
     * 删除地推人员Id
     * @param id
     * @return
     */
    Integer deleteExtensionUser(String id);

}
