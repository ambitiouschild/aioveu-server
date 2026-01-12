package com.aioveu.ums.aioveu02MemberAddress.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: UmsMemberAddressQuery
 * @Description TODO 会员收货地址分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 15:29
 * @Version 1.0
 **/


@Schema(description ="会员收货地址查询对象")
@Getter
@Setter
public class UmsMemberAddressQuery extends BasePageQuery {

    @Schema(description = "会员ID")
    private Long memberId;
    @Schema(description = "收货人姓名")
    private String consigneeName;
    @Schema(description = "收货人联系方式")
    private String consigneeMobile;
    @Schema(description = "邮政编码")
    private String postalCode;
    @Schema(description = "地址标签(家,公司,学校等)")
    private String addressTag;
}
