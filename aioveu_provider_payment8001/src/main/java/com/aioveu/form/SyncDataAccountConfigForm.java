package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/4/16 10:50
 */
@Data
public class SyncDataAccountConfigForm {

    private String id;

    /**
     * 公司id
     */
    private Long companyId;

    /**
     * 门店id
     */
    private Long storeId;

    /**
     * 平台名称
     */
    private String platformName;

    /**
     * 分类code
     */
    @NotEmpty(message = "平台分类编号不能为空")
    private String platformCode;

    /**
     * 平台对应门店名称
     */
    @NotEmpty(message = "平台对应门店名称不能为空")
    private String platformStoreName;

    /**
     * 平台登录账户
     */
    @NotEmpty(message = "平台登录账户不能为空")
    private String platformUsername;

    /**
     * 平台登录密码
     */
    private String platformPassword;

    /**
     * 确认新密码
     */
    private String newPlatformPassword;

    /**
     * 平台登录url
     */
    private String platformUrl;

    /**
     * 平台订场支持半小时
     * 1 默认支持
     * 0 不支持，
     * 不支持的话，本系统订场是09:30-10:30,同步的是09:00-11:00
     */
    private Integer platformHalfHour;

    private Integer status;

    /**
     * 是否重新登录
     */
    private Boolean reLogin;


}
