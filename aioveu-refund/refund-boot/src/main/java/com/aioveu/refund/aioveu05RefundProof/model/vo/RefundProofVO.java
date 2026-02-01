package com.aioveu.refund.aioveu05RefundProof.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundProofVO
 * @Description TODO  退款凭证图片视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:03
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "退款凭证图片视图对象")
public class RefundProofVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "退款申请ID")
    private Long refundId;
    @Schema(description = "凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他")
    private Integer proofType;
    @Schema(description = "图片URL")
    private String imageUrl;
    @Schema(description = "图片描述")
    private String imageDesc;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "逻辑删除")
    private Integer deleted;
}
