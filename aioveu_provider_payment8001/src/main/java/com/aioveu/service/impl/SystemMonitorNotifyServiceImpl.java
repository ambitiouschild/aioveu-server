package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.aioveu.entity.RoleUser;
import com.aioveu.entity.User;
import com.aioveu.feign.MailClient;
import com.aioveu.service.RoleUserService;
import com.aioveu.service.SystemMonitorNotifyService;
import com.aioveu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author： yao
 * @Date： 2024/10/30 15:38
 * @Describe：
 */
@Slf4j
@Service
public class SystemMonitorNotifyServiceImpl implements SystemMonitorNotifyService {
    /**
     * 系统运维角色code
     */
    private static String MONITOR_ROLE_CODE = "system_monitor";
    @Autowired
    private RoleUserService roleUserService;
    @Autowired
    private UserService userService;

    @Autowired
    private MailClient mailClient;

    @Override
    public List<User> getMonitorNotifyUserInfos() {
        //根据角色role_code获取绑定的用户user_id
        QueryWrapper<RoleUser> roleUserQueryWrapper = new QueryWrapper<>();
        roleUserQueryWrapper.lambda().eq(RoleUser::getRoleCode, MONITOR_ROLE_CODE)
                .eq(RoleUser::getStatus, 1);
        List<RoleUser> roleUsers = roleUserService.list(roleUserQueryWrapper);

        if (CollectionUtils.isEmpty(roleUsers)){
            return null;
        }
        List<String> userIds = roleUsers.stream().map(RoleUser::getUserId).collect(Collectors.toList());

        //根据上一步的user_id获取用户信息，通知的邮箱地址信息，不能为空
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.lambda().in(User::getId, userIds)
                .eq(User::getStatus, 1);
        List<User> users = userService.list(userQueryWrapper);
        return users;
    }

    @Override
    public boolean sendMonitorMail(String subject, String content) {
        try {
            List<User> userInfos = this.getMonitorNotifyUserInfos();
            if(CollectionUtils.isNotEmpty(userInfos)){
                Set<String> mails = userInfos.stream().map(User::getMail).filter(s-> StringUtils.isNotEmpty(s)).collect(Collectors.toSet());
                if (CollectionUtils.isNotEmpty(mails)){
                    mailClient.sendSimpleMail(subject, content, StringUtils.join(mails, ","));
                }
            }
            return true;
        }catch (Exception e){
            log.error("邮件发送失败：",e.getMessage());
        }
        return false;
    }
}
