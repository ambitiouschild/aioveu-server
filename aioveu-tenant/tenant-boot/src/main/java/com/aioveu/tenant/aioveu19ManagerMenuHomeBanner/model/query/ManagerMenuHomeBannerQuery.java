package com.aioveu.tenant.aioveu19ManagerMenuHomeBanner.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: ManagerMenuHomeBannerQuery
 * @Description TODO 管理端app首页滚播栏分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/4/4 15:38
 * @Version 1.0
 **/
@Schema(description ="管理端app首页滚播栏查询对象")
@Getter
@Setter
public class ManagerMenuHomeBannerQuery extends BasePageQuery {

    @Schema(description = "滚播栏标题")
    private String title;
    @Schema(description = "状态(1:开启；0:关闭)")
    private Integer status;
    @Schema(description = "排序")
    private Integer sort;
    @Schema(description = "租户ID")
    private Long tenantId;
}
