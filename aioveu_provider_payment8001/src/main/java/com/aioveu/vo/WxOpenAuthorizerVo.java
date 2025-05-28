package com.aioveu.vo;

import com.aioveu.entity.WxOpenCodeCommit;
import lombok.Data;

import java.util.Date;

/**
 * @Description 微信开放平台授权表
 * @Author luyao
 * @Date: 2025-02-09 10:04:24
 */

@Data
public class WxOpenAuthorizerVo {

    /**
     * 第三方平台appid
     */
    private String componentAppId;
    /**
     * 授权appid
     */
    private String authorizerAppId;
    /**
     * 主体信息
     */
    private String principalName;
    /**
     * 小程序名称
     */
    private String nickName;
    /**
     * 小程序图标
     */
    private String headImg;
    /**
     * 账号介绍
     */
    private String signature;

    /**
     * 授权给开发者的权限集列表
     */
    private String funcInfo;

    /**
     * 上线时间
     */
    private Date onlineDate;
    /**
     * 线上版本对应上传记录
     */
    private Long onlineCodeCommitId;
    /**
     * 线上版本
     */
    private WxOpenCodeCommit onlineInfo;
    /**
     * 审核版本对应上传记录
     */
    private Long auditCodeCommitId;
    /**
     * 审核版本信息
     */
    private WxOpenCodeCommit auditInfo;
    /**
     * 审核状态
     */
    private Integer auditStatus;
    /**
     * 失败原因
     */
    private String failReason;
    /**
     * 授权时间
     */
    private Date auditDate;

    //0 删除 1 授权 2 解除授权
    private Integer status;

    /**
     * 授权时间
     */
    private Date authorizerDate;

    /**
     * 域名是否配置
     */
    private Boolean domainConfig;

    /**
     * webview是否配置
     */
    private Boolean webviewConfig;

    /**
     * 是否进行隐私配置
     */
    private Boolean privacySetting;

}
