package com.aioveu.oms.aioveu04OrderLog.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderLogVO
 * @Description TODO  订单操作历史记录视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 16:38
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "订单操作历史记录视图对象")
public class OmsOrderLogVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;
    @Schema(description = "订单id")
    private Long orderId;
    @Schema(description = "操作人[用户；系统；后台管理员]")
    private String user;
    @Schema(description = "操作详情")
    private String detail;
    @Schema(description = "操作时订单状态")
    private Integer orderStatus;
    @Schema(description = "备注")
    private String remark;
    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
