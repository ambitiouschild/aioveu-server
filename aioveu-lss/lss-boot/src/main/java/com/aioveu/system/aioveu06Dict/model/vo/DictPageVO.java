package com.aioveu.system.aioveu06Dict.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: DictPageVO
 * @Description TODO  字典分页VO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:00
 * @Version 1.0
 **/

@Schema(description = "字典分页对象")
@Getter
@Setter
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
