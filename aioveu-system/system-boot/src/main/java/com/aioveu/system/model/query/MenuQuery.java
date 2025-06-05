package com.aioveu.system.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 菜单查询对象
 * @Author: 雒世松
 * @Date: 2025/6/5 17:23
 * @param
 * @return:
 **/

@Schema(description ="部门分页查询对象")
@Data
public class MenuQuery {

    @Schema(description="关键字(菜单名称)")
    private String keywords;

    @Schema(description="状态(1->显示；0->隐藏)")
    private Integer status;

}
