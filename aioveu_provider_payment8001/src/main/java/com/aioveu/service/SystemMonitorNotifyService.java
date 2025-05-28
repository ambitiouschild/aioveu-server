package com.aioveu.service;

import com.aioveu.entity.User;

import java.util.List;

/**
 * 系统监控通知运维服务
 * @Author： yao
 * @Date： 2024/10/30 15:36
 * @Describe：
 */
public interface SystemMonitorNotifyService {

    /**
     * 获取系统运维人员信息
     */
    List<User> getMonitorNotifyUserInfos();

    /**
     * 发送邮件通知系统运维
     * @param subject
     * @param content
     * @return
     */
    boolean sendMonitorMail(String subject, String content);


}
