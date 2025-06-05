package com.aioveu.system.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 角色分页查询实体
 * @Author: 雒世松
 * @Date: 2025/6/5 17:24
 * @param
 * @return:
 **/

@Data
public class RolePageQuery extends BasePageQuery {

    @Schema(description="关键字(角色名称/角色编码)")
    private String keywords;
}
