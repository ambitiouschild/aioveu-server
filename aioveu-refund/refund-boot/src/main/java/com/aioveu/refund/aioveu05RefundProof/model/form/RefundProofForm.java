package com.aioveu.refund.aioveu05RefundProof.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundProofForm
 * @Description TODO 退款凭证图片表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:02
 * @Version 1.0
 **/
public class RefundProofForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退款申请ID")
    @NotNull(message = "退款申请ID不能为空")
    private Long refundId;

    @Schema(description = "凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他")
    @NotNull(message = "凭证类型：1-质量问题 2-物流问题 3-描述不符 4-其他不能为空")
    private Integer proofType;

    @Schema(description = "图片URL")
    @NotBlank(message = "图片URL不能为空")
    @Size(max=500, message="图片URL长度不能超过500个字符")
    private String imageUrl;

    @Schema(description = "图片描述")
    @Size(max=200, message="图片描述长度不能超过200个字符")
    private String imageDesc;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "逻辑删除")
    private Integer deleted;
}
