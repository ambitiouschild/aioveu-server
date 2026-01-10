package com.aioveu.oms.aioveu03OrderDelivery.model.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


import java.io.Serial;
import java.io.Serializable;
import java.util.Date;  // 正确

/**
 * @ClassName: OmsOrderDeliveryForm
 * @Description TODO  订单物流记录表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/8 20:17
 * @Version 1.0
 **/

@Getter
@Setter
@Schema(description = "订单物流记录表单对象")
public class OmsOrderDeliveryForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    @NotNull(message = "id不能为空")
    private Long id;

    @Schema(description = "订单id")
    @NotNull(message = "订单id不能为空")
    private Long orderId;

    @Schema(description = "物流公司(配送方式)")
    @NotBlank(message = "物流公司(配送方式)不能为空")
    @Size(max=64, message="物流公司(配送方式)长度不能超过64个字符")
    private String deliveryCompany;

    @Schema(description = "物流单号")
    @NotBlank(message = "物流单号不能为空")
    @Size(max=64, message="物流单号长度不能超过64个字符")
    private String deliverySn;

    @Schema(description = "收货人姓名")
    @NotBlank(message = "收货人姓名不能为空")
    @Size(max=100, message="收货人姓名长度不能超过100个字符")
    private String receiverName;

    @Schema(description = "收货人电话")
    @NotBlank(message = "收货人电话不能为空")
    @Size(max=32, message="收货人电话长度不能超过32个字符")
    private String receiverPhone;

    @Schema(description = "收货人邮编")
    @NotBlank(message = "收货人邮编不能为空")
    @Size(max=32, message="收货人邮编长度不能超过32个字符")
    private String receiverPostCode;

    @Schema(description = "省份/直辖市")
    @NotBlank(message = "省份/直辖市不能为空")
    @Size(max=32, message="省份/直辖市长度不能超过32个字符")
    private String receiverProvince;

    @Schema(description = "城市")
    @NotBlank(message = "城市不能为空")
    @Size(max=32, message="城市长度不能超过32个字符")
    private String receiverCity;

    @Schema(description = "区")
    @NotBlank(message = "区不能为空")
    @Size(max=32, message="区长度不能超过32个字符")
    private String receiverRegion;

    @Schema(description = "详细地址")
    @NotBlank(message = "详细地址不能为空")
    @Size(max=500, message="详细地址长度不能超过500个字符")
    private String receiverDetailAddress;

    @Schema(description = "备注")
    @NotBlank(message = "备注不能为空")
    @Size(max=500, message="备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "物流状态【0->运输中；1->已收货】")
    private Integer deliveryStatus;

    @Schema(description = "发货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date deliveryTime;

    @Schema(description = "确认收货时间")
    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
    private Date receiveTime;

    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
}
