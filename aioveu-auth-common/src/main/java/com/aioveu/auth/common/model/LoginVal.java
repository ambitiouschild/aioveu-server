package com.aioveu.auth.common.model;

import lombok.Data;

/**
 * 保存登录用户的信息，此处可以根据业务需要扩展
 */
@Data
public class LoginVal extends JwtInformation{

    private String userId;

    private String username;

    private String[] authorities;

    private String nickname;

    private Integer gender;

    private String head;

    private String phone;

    private String email;

    /**
     * 是否是假用户
     */
    private boolean fake;
}
