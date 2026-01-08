package com.aioveu.oms.aioveu01Order.model.vo;

import com.aioveu.oms.aioveu02OrderItem.model.vo.OrderItemDTO;
import com.aioveu.ums.dto.MemberAddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

/**
 * @Description: TODO 订单确认响应对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:12
 * @param
 * @return:
 **/

@Schema(description ="订单确认响应对象")
@Data
public class OrderConfirmVO {

    /**
     * 订单防重提交令牌
     */
    @Schema(description="订单防重提交令牌")
    private String orderToken;

    /**
     * 订单商品
     */
    @Schema(description="订单商品")
    private List<OrderItemDTO> orderItems;

    /**
     * 会员收货地址列表
     */
    @Schema(description="会员收获地址列表")
    private List<MemberAddressDTO> addresses;

}
