package com.aioveu.vo;

import com.aioveu.entity.StringIdEntity;
import lombok.Data;

/**
 * @Description 同步数据账户配置表，配置同步第三方平台的账户密码
 * @Author luyao
 * @Date: 2024-11-27 10:38:50
 */

@Data
public class SyncDataAccountConfigVO extends StringIdEntity {

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
     * 小程序同步到第三方平台处理类：syncDataToQyd 趣运动
     */
    private String platformCode;
    /**
     * 平台对应门店id
     */
    private String platformStoreId;
    /**
     * 平台对应门店名称
     */
    private String platformStoreName;
    /**
     * 平台登录账户
     */
    private String platformUsername;
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

    /**
     * 小程序同步到第三方平台处理类：syncDataToQyd 趣运动
     */
    private String platformHandler;

}
