package com.aioveu.ums.aioveu01Member.model.entity;

import com.aioveu.ums.aioveu02MemberAddress.model.entity.UmsMemberAddress;
import com.baomidou.mybatisplus.annotation.*;
import com.aioveu.common.base.BaseEntity;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

/**
 * @Description: TODO 会员实体对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:59
 * @param
 * @return:
 **/

@Data
@TableName("ums_member")
public class UmsMember extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 昵称
     */
    private String nickName;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 头像URL
     */
    private String avatarUrl;
    /**
     * 性别(0=未知,1=男,2=女)
     */
    private Integer gender;
    /**
     * 生日
     */
    private LocalDate birthday;
    /**
     * 会员积分
     */
    private Integer point;
    /**
     * 账户余额(单位:分)
     */
    private Long balance;
    /**
     * 状态(0=禁用,1=正常)
     */
    private Integer status;
    /**
     * 删除标志(0=未删除,1=已删除)
     */
    private Integer deleted;
    /**
     * 微信OpenID
     */
    private String openid;
    /**
     * 微信会话密钥
     */
    private String sessionKey;
    /**
     * 国家
     */
    private String country;
    /**
     * 省份
     */
    private String province;
    /**
     * 城市
     */
    private String city;
    /**
     * 语言
     */
    private String language;

    @TableField(exist = false)
    private List<UmsMemberAddress> addressList;


}
