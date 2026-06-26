package com.aioveu.oms.aioveu01Order.model.vo;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: OrderStatusCountDTO
 * @Description TODO  （架构级解法，最优雅）OrderStatusCountDTO 不要在 Mapper 里返回 Map
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/26 18:39
 * @Version 1.0
 **/
@Schema(description ="（架构级解法，最优雅）OrderStatusCountDTO 不要在 Mapper 里返回 Map")
@Data
public class OrderStatusCountDTO {

    /**
     * 订单状态（数据库 tinyint）
     */
    private Integer status;

    /**
     * 数量
     */
    private Integer count;

    private Long amount;
}
