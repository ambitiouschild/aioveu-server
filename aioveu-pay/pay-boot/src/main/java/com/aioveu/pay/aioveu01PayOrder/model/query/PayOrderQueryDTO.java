package com.aioveu.pay.aioveu01PayOrder.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayOrderQueryDTO
 * @Description TODO 分页查询支付订单查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:49
 * @Version 1.0
 **/

@Schema(description ="分页查询支付订单查询对象")
@Getter
@Setter
public class PayOrderQueryDTO extends BasePageQuery {
}
