package com.aioveu.tenant.aioveu06Dict.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: DictItemOptionVO
 * @Description TODO 字典项键值对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 19:51
 * @Version 1.0
 **/
@Schema(description = "字典项键值对象")
@Data
public class DictItemOptionVO {

    @Schema(description = "字典项值")
    private String value;

    @Schema(description = "字典项标签")
    private String label;

    @Schema(description = "标签类型")
    private String tagType;
}
