package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Description 同步数据账户配置表，配置同步第三方平台的账户密码
 * @Author luyao
 * @Date: 2024-11-27 10:38:50
 */

@TableName("sport_sync_data_account_config")
@Data
public class SyncDataAccountConfig extends StringIdEntity {

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
     * 小程序同步到第三方平台处理类：syncDataToQyd 趣运动
     */
    private String platformHandler;

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

}
