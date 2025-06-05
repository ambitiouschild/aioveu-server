package com.aioveu.system.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 权限分页查询对象
 * @Author: 雒世松
 * @Date: 2025/6/5 17:24
 * @param
 * @return:
 **/

@Data
@Schema 
public class PermPageQuery extends BasePageQuery {

    @Schema(description="权限名称")
    private String name;

    @Schema(description="菜单ID")
    private Long menuId;

}
