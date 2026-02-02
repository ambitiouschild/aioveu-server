package com.aioveu.refund.aioveu03RefundDelivery.model.form;

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
 * @ClassName: ConfirmReceiveFormDTO
 * @Description TODO  商家确认收货,退款物流信息（用于退货）表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 14:03
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "商家确认收货,退款物流信息（用于退货）表单对象")
public class ConfirmReceiveFormDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "退款申请ID")
    @NotNull(message = "退款申请ID不能为空")
    private Long refundId;

    @Schema(description = "物流类型：1-买家发货 2-卖家发货 3-换货发货")
    @NotNull(message = "物流类型：1-买家发货 2-卖家发货 3-换货发货不能为空")
    private Integer deliveryType;




    @Schema(description = "物流公司")
    @NotBlank(message = "物流公司不能为空")
    @Size(max=100, message="物流公司长度不能超过100个字符")
    private String deliveryCompany;

    @Schema(description = "物流单号")
    @NotBlank(message = "物流单号不能为空")
    @Size(max=100, message="物流单号长度不能超过100个字符")
    private String deliverySn;

    @Schema(description = "收货人姓名")
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max=100, message="收货人姓名长度不能超过100个字符")
    private String receiverName;

    @Schema(description = "收货人电话")
    @NotBlank(message = "收货人电话不能为空")
    @Size(max=20, message="收货人电话长度不能超过20个字符")
    private String receiverPhone;

    @Schema(description = "收货地址")
    @NotBlank(message = "收货地址不能为空")
    @Size(max=500, message="收货地址长度不能超过500个字符")
    private String receiverAddress;

    @Schema(description = "发货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime deliveryTime;

    @Schema(description = "收货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    @Schema(description = "创建时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    @Schema(description = "更新时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    @Schema(description = "逻辑删除")
    private Integer deleted;


    @Schema(description = "操作人ID")
    private Long operatorId;

    @Schema(description = "操作人名称")
    @Size(max=50, message="操作人名称长度不能超过50个字符")
    private String operatorName;

    @Schema(description = "操作人类型：1-用户 2-客服 3-商家 4-系统")
    private Integer operatorType;
}
