package com.aioveu.tenant.aioveu02User.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @ClassName: UserSessionDTO
 * @Description TODO 用户会话Dto
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:08
 * @Version 1.0
 **/
@Data
@Schema(description ="用户会话Dto")
public class UserSessionDTO {

    /**
     * 用户名
     */
    private String username;

    /**
     * 用户会话ID集合
     */
    private Set<String> sessionIds;

    /**
     * 最后活动时间
     */
    private long lastActiveTime;

    public UserSessionDTO(String username) {
        this.username = username;
        this.sessionIds = new HashSet<>();
        this.lastActiveTime = System.currentTimeMillis();
    }
}
