package com.aioveu.pay.aioveu04PayConfigDummy.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayConfigDummy
 * @Description TODO 模拟支付配置实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:27
 * @Version 1.0
 **/
@Getter
@Setter
@TableName("pay_config_dummy")
public class PayConfigDummy extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 支付配置主表ID
     */
    private Long configId;
    /**
     * 所属租户ID
     */
    private Long tenantId;
    /**
     * 支付成功率(0-100)
     */
    private Integer successRate;
    /**
     * 模拟延迟(毫秒)
     */
    private Integer mockDelay;
    /**
     * 模拟回调地址
     */
    private String callbackUrl;
    /**
     * 回调延迟(毫秒)
     */
    private Integer callbackDelay;
    /**
     * 成功响应模板
     */
    private String successResponse;
    /**
     * 失败响应模板
     */
    private String failResponse;
    /**
     * 是否模拟异常: 0-否, 1-是
     */
    private Integer simulateError;
    /**
     * 模拟错误码
     */
    private String errorCode;
    /**
     * 模拟错误信息
     */
    private String errorMsg;
    /**
     * 是否自动退款: 0-否, 1-是
     */
    private Integer autoRefund;
    /**
     * 退款延迟(毫秒)
     */
    private Integer refundDelay;
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
