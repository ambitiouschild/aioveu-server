package com.aioveu.oms.aioveu04OrderLog.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: OmsOrderLogForm
 * @Description TODO  订单操作历史记录表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:36
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "订单操作历史记录表单对象")
public class OmsOrderLogForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;

    @Schema(description = "订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @Schema(description = "操作人[用户；系统；后台管理员]")
    @Size(max=100, message="操作人[用户；系统；后台管理员]长度不能超过100个字符")
    private String user;

    @Schema(description = "操作详情")
    @NotBlank(message = "操作详情不能为空")
    @Size(max=255, message="操作详情长度不能超过255个字符")
    private String detail;

    @Schema(description = "操作时订单状态")
    private Integer orderStatus;

    @Schema(description = "备注")
    @NotBlank(message = "备注不能为空")
    @Size(max=500, message="备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
}
