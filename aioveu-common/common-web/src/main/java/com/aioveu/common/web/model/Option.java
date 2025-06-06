package com.aioveu.common.web.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Description: TODO 下拉选项对象
 * @Author: 雒世松
 * @Date: 2025/6/5 16:26
 * @param
 * @return:
 **/

@Schema(description ="下拉选项对象")
@Data
@NoArgsConstructor
public class Option<T> {

    public Option(T value, String label) {
        this.value = value;
        this.label = label;
    }

    public Option(T value, String label, List<Option> children) {
        this.value = value;
        this.label = label;
        this.children= children;
    }

    @Schema(description="选项的值")
    private T value;

    @Schema(description="选项的标签")
    private String label;

    @Schema(description="子选项列表")
    @JsonInclude(value = JsonInclude.Include.NON_EMPTY)
    private List<Option> children;

}