package com.aioveu.oms.aioveu06OrderSetting.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: OmsOrderSettingQuery
 * @Description TODO  订单配置信息分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:13
 * @Version 1.0
 **/

@Schema(description ="订单配置信息查询对象")
@Getter
@Setter
public class OmsOrderSettingQuery extends BasePageQuery {

    @Schema(description = "秒杀订单超时关闭时间(分)")
    private Integer flashOrderOvertime;
    @Schema(description = "正常订单超时时间(分)")
    private Integer normalOrderOvertime;
    @Schema(description = "自动完成交易时间，不能申请退货（天）")
    private Integer finishOvertime;
    @Schema(description = "订单完成后自动好评时间（天）")
    private Integer commentOvertime;
    @Schema(description = "会员等级【0-不限会员等级，全部通用；其他-对应的其他会员等级】")
    private Integer memberLevel;
    @Schema(description = "逻辑删除【0->正常；1->已删除】")
    private Integer deleted;
}
