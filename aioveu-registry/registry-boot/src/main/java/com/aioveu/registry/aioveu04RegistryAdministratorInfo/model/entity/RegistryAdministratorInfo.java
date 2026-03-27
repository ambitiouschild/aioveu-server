package com.aioveu.registry.aioveu04RegistryAdministratorInfo.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @ClassName: RegistryAdministratorInfo
 * @Description TODO 管理员信息实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 17:43
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_administrator_info")
public class RegistryAdministratorInfo extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 管理员真实姓名
     */
    private String realName;
    /**
     * 身份证号码
     */
    private String idCardNo;
    /**
     * 身份证正面照片
     */
    private String idCardFrontPath;
    /**
     * 身份证反面照片
     */
    private String idCardBackPath;
    /**
     * 手机号码
     */
    private String phone;
    /**
     * 短信验证码
     */
    private String phoneVerifyCode;
    /**
     * 手机是否已验证：0-未验证，1-已验证
     */
    private Integer phoneVerified;
    /**
     * 管理员微信OpenID
     */
    private String wechatOpenid;
    /**
     * 管理员微信UnionID
     */
    private String wechatUnionid;
    /**
     * 微信扫码是否已验证
     */
    private Integer wechatQrScanned;
    /**
     * 微信扫码验证时间
     */
    private LocalDateTime wechatScannedTime;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
