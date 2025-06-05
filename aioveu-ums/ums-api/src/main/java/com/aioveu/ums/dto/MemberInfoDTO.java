package com.aioveu.ums.dto;

import lombok.Data;

/**
 * @Description: TODO 会员传输层对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:53
 * @param
 * @return:
 **/

@Data
public class MemberInfoDTO {

    private String nickName;

    private String avatarUrl;

    private Long balance;

}
