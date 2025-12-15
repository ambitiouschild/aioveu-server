package com.aioveu.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @ClassName: Option
 * @Description TODO  下拉选项对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 16:36
 * @Version 1.0
 **/

@Schema(description ="下拉选项对象")
@Data
@NoArgsConstructor
public class Option<T> {

    public Option(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public Option(T value, String label, List<Option<T>> children) {
        this.value = value;
        this.label = label;
        this.children= children;
    }

    public Option(T value, String label, String tag) {
        this.value = value;
        this.label = label;
        this.tag= tag;
    }


    @Schema(description="选项的值")
    private T value;

    @Schema(description="选项的标签")
    private String label;

    @Schema(description = "标签类型")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private String tag;

    @Schema(description="子选项列表")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<Option<T>> children;

}
