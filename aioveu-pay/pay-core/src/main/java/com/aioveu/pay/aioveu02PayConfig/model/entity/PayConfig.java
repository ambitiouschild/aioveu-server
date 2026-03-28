package com.aioveu.pay.aioveu02PayConfig.model.entity;

import com.aioveu.common.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import com.baomidou.mybatisplus.annotation.TableName;
/**
 * @ClassName: PayConfig
 * @Description TODO 支付配置主表实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 15:59
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("pay_config")
public class PayConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 配置编码（唯一标识）
     */
    private String configCode;
    /**
     * 配置名称
     */
    private String configName;
    /**
     * 支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付
     */
    private String platformType;
    /**
     * 支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付
     */
    private String payType;
    /**
     * 是否启用: 0-禁用, 1-启用
     */
    private Integer enabled;
    /**
     * 是否默认配置: 0-否, 1-是
     */
    private Integer isDefault;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 备注
     */
    private String remark;
    /**
     * 逻辑删除：0-未删除 1-已删除
     */
    private Integer isDeleted;
    /**
     * 创建人ID
     */
    private Long createBy;
    /**
     * 更新人ID
     */
    private Long updateBy;
}
