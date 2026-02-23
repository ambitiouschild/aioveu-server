package com.aioveu.tenant.aioveu04Menu.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: MenuQuery
 * @Description TODO 菜单查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:45
 * @Version 1.0
 **/
@Schema(description ="菜单查询对象")
@Data
public class MenuQuery extends BasePageQuery {

    @Schema(description="关键字(菜单名称)")
    private String keywords;

    @Schema(description="状态(1->显示；0->隐藏)")
    private Integer status;

}
