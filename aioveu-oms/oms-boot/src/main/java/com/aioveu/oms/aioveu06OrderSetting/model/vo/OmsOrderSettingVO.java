package com.aioveu.oms.aioveu06OrderSetting.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @ClassName: OmsOrderSettingVO
 * @Description TODO  订单配置信息视图对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:14
 * @Version 1.0
 **/

@Getter
@Setter
@Schema( description = "订单配置信息视图对象")
public class OmsOrderSettingVO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "id")
    private Long id;
    @Schema(description = "秒杀订单超时关闭时间(分)")
    private Integer flashOrderOvertime;
    @Schema(description = "正常订单超时时间(分)")
    private Integer normalOrderOvertime;
    @Schema(description = "发货后自动确认收货时间（天）")
    private Integer confirmOvertime;
    @Schema(description = "自动完成交易时间，不能申请退货（天）")
    private Integer finishOvertime;
    @Schema(description = "订单完成后自动好评时间（天）")
    private Integer commentOvertime;
    @Schema(description = "会员等级【0-不限会员等级，全部通用；其他-对应的其他会员等级】")
    private Integer memberLevel;
    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
    @Schema(description = "创建时间")
    private LocalDateTime createTime;
    @Schema(description = "修改时间")
    private LocalDateTime updateTime;
}
