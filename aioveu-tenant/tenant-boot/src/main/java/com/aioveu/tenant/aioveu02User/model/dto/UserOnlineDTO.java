package com.aioveu.tenant.aioveu02User.model.dto;

/**
 * @ClassName: UserOnlineDTO
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 15:10
 * @Version 1.0
 **/

import lombok.Data;


@Data
public class UserOnlineDTO {

    /**
     * 用户在线Dto（用于返回给前端）
     */

    private final String username;
    private final long loginTime;
}
