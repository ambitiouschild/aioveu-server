package com.aioveu.refund.aioveu07RefundReason.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundReason
 * @Description TODO 退款原因分类实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:52
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("refund_reason")
public class RefundReason extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 原因类型：1-仅退款原因 2-退货退款原因 3-换货原因
     */
    private Integer reasonType;
    /**
     * 原因内容
     */
    private String reasonContent;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 状态：0-禁用 1-启用
     */
    private Integer status;
}
