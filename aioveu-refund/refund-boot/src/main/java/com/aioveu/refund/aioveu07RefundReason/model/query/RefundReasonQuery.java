package com.aioveu.refund.aioveu07RefundReason.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: RefundReasonQuery
 * @Description TODO 退款原因分类分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:53
 * @Version 1.0
 **/

@Schema(description ="退款原因分类查询对象")
@Getter
@Setter
public class RefundReasonQuery extends BasePageQuery {

    @Schema(description = "原因类型：1-仅退款原因 2-退货退款原因 3-换货原因")
    private Integer reasonType;
    @Schema(description = "原因内容")
    private String reasonContent;
    @Schema(description = "状态：0-禁用 1-启用")
    private Integer status;
}
