package com.aioveu.tenant.aioveu06Dict.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: DictPageVO
 * @Description TODO  字典分页对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:51
 * @Version 1.0
 **/

@Schema(description = "字典分页对象")
@Data
public class DictPageVO {

    @Schema(description = "字典ID")
    private Long id;

    @Schema(description = "字典名称")
    private String name;

    @Schema(description = "字典编码")
    private String dictCode;

    @Schema(description = "字典状态（1-启用，0-禁用）")
    private Integer status;
}
