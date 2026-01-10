package com.aioveu.oms.aioveu04OrderLog.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: OmsOrderLogQuery
 * @Description TODO  订单操作历史记录分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:37
 * @Version 1.0
 **/

@Schema(description ="订单操作历史记录查询对象")
@Getter
@Setter
public class OmsOrderLogQuery extends BasePageQuery {

    @Schema(description = "订单id")
    private Long orderId;
    @Schema(description = "操作人[用户；系统；后台管理员]")
    private String user;
    @Schema(description = "操作时订单状态")
    private Integer orderStatus;
    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
}
