package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GroupLuck;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GroupLuckService extends IService<GroupLuck> {

    /**
     * 创建数据
     * @return
     */
    boolean createData();

    /**
     * 抽奖
     * @param uuid
     * @param groupName
     * @param username
     * @return
     */
    GroupLuck luck(String uuid, String groupName, String username);


}
