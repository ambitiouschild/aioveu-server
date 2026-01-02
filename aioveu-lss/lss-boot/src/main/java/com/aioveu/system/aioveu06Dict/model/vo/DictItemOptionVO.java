package com.aioveu.system.aioveu06Dict.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

/**
 * @ClassName: DictItemOptionVO
 * @Description TODO  字典项键值对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 16:59
 * @Version 1.0
 **/

@Schema(description = "字典项键值对象")
@Getter
@Setter
public class DictItemOptionVO {

    @Schema(description = "字典项值")
    private String value;

    @Schema(description = "字典项标签")
    private String label;

    @Schema(description = "标签类型")
    private String tagType;

}
