package com.aioveu.refund.aioveu07RefundReason.model.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: RefundReasonVO
 * @Description TODO 退款原因分类视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:54
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "退款原因分类视图对象")
public class RefundReasonVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;
    @Schema(description = "原因类型：1-仅退款原因 2-退货退款原因 3-换货原因")
    private Integer reasonType;
    @Schema(description = "原因内容")
    private String reasonContent;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "创建时间")
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Schema(description = "更新时间")
    private LocalDateTime updateTime;
}
