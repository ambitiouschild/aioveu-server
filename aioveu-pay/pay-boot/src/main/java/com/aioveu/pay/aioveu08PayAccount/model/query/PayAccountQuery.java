package com.aioveu.pay.aioveu08PayAccount.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: PayAccountQuery
 * @Description TODO 支付账户分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:09
 * @Version 1.0
 **/

@Schema(description ="支付账户查询对象")
@Getter
@Setter
public class PayAccountQuery extends BasePageQuery {

    @Schema(description = "账户编号")
    private String accountNo;
    @Schema(description = "用户ID")
    private Long userId;
    @Schema(description = "账户类型：USER-用户账户 MERCHANT-商户账户 PLATFORM-平台账户")
    private String accountType;
    @Schema(description = "账户状态：0-冻结 1-正常 2-注销")
    private Integer status;
}
