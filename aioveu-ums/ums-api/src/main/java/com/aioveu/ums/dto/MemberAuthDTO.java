package com.aioveu.ums.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description: TODO 会员信息
 * @Author: 雒世松
 * @Date: 2025/6/5 18:53
 * @param
 * @return:
 **/

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MemberAuthDTO {

    /**
     * 会员ID
     */
    private Long id;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 手机号
     */
    private String mobile;


    /**
     * 微信OpenID
     */
    private String openid;

    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 状态(1:正常；0：禁用)
     */
    private Integer status;
}
