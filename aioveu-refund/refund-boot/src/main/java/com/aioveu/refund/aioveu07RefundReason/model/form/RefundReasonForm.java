package com.aioveu.refund.aioveu07RefundReason.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundReasonForm
 * @Description TODO 退款原因分类表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:52
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "退款原因分类表单对象")
public class RefundReasonForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "原因类型：1-仅退款原因 2-退货退款原因 3-换货原因")
    @NotNull(message = "原因类型：1-仅退款原因 2-退货退款原因 3-换货原因不能为空")
    private Integer reasonType;

    @Schema(description = "原因内容")
    @NotBlank(message = "原因内容不能为空")
    @Size(max=100, message="原因内容长度不能超过100个字符")
    private String reasonContent;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "状态：0-禁用 1-启用")
    @NotNull(message = "状态：0-禁用 1-启用不能为空")
    private Integer status;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;
}
