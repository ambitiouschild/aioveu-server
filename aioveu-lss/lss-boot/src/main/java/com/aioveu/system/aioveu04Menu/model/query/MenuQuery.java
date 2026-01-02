package com.aioveu.system.aioveu04Menu.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: MenuQuery
 * @Description TODO  菜单查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:56
 * @Version 1.0
 **/

@Schema(description ="菜单查询对象")
@Data
public class MenuQuery {

    @Schema(description="关键字(菜单名称)")
    private String keywords;

    @Schema(description="状态(1->显示；0->隐藏)")
    private Integer status;
}
