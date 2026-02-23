package com.aioveu.tenant.aioveu02User.service;

import com.aioveu.tenant.aioveu02User.model.dto.UserOnlineDTO;

import java.util.List;

/**
 * @ClassName: UserOnlineService
 * @Description TODO 用户在线状态服务
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 23:26
 * @Version 1.0
 **/
public interface UserOnlineService {

    /**
     * 用户上线
     *
     * @param username  用户名
     * @param sessionId WebSocket会话ID（可选）
     */
    void userConnected(String username, String sessionId);

    /**
     * 用户下线
     *
     * @param username 用户名
     */
    public void userDisconnected(String username);

    /**
     * 获取在线用户列表
     *
     * @return 在线用户名列表
     */
    List<UserOnlineDTO> getOnlineUsers();

    /**
     * 获取在线用户数量
     *
     * @return 在线用户数
     */
    int getOnlineUserCount();

    /**
     * 检查用户是否在线
     *
     * @param username 用户名
     * @return 是否在线
     */
    boolean isUserOnline(String username);

    /**
     * 通知所有客户端在线用户变更
     */
    void notifyOnlineUsersChange();

    /**
     * 发送在线用户数量（简化版，不包含用户详情）
     */
    void sendOnlineUserCount();
}
