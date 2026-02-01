package com.aioveu.refund.aioveu05RefundProof.model.entity;

import com.aioveu.common.base.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundProof
 * @Description TODO 退款凭证图片实体对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:01
 * @Version 1.0
 **/

@Getter
@Setter
@TableName("refund_proof")
public class RefundProof extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 退款申请ID
     */
    private Long refundId;
    /**
     * 凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他
     */
    private Integer proofType;
    /**
     * 图片URL
     */
    private String imageUrl;
    /**
     * 图片描述
     */
    private String imageDesc;
    /**
     * 逻辑删除
     */
    private Integer deleted;
}
