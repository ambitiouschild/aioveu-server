package com.aioveu.system.aioveu05Dept.model.query;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: DeptQuery
 * @Description TODO  部门查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:50
 * @Version 1.0
 **/

@Schema(description ="部门分页查询对象")
@Data
public class DeptQuery {

    @Schema(description="关键字(部门名称)")
    private String keywords;

    @Schema(description="状态(1->正常；0->禁用)")
    private Integer status;

}
