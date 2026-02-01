package com.aioveu.refund.aioveu05RefundProof.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundProofQuery
 * @Description TODO 退款凭证图片分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:03
 * @Version 1.0
 **/

@Schema(description ="退款凭证图片查询对象")
@Getter
@Setter
public class RefundProofQuery extends BasePageQuery {

    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他")
    private Integer proofType;
    @Schema(description = "图片描述")
    private String imageDesc;
}
