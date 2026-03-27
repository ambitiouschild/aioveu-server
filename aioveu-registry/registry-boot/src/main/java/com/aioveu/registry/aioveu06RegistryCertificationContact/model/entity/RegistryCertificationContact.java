package com.aioveu.registry.aioveu06RegistryCertificationContact.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RegistryCertificationContact
 * @Description TODO 认证联系人实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/27 18:24
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("registry_certification_contact")
public class RegistryCertificationContact extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 认证记录ID
     */
    private Long certificationId;
    /**
     * 联系人姓名
     */
    private String contactName;
    /**
     * 联系人身份证号
     */
    private String contactIdCard;
    /**
     * 联系人手机号
     */
    private String contactPhone;
    /**
     * 联系人短信验证码
     */
    private String contactPhoneVerifyCode;
    /**
     * 联系人座机（含分机）
     */
    private String contactLandline;
    /**
     * 联系人微信OpenID
     */
    private String contactWechatOpenid;
    /**
     * 联系人微信扫码是否验证
     */
    private Integer contactWechatScanned;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
}
