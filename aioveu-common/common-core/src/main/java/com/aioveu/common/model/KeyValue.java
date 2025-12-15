package com.aioveu.common.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @ClassName: KeyValue
 * @Description TODO  键值对
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 16:37
 * @Version 1.0
 **/

@Schema(description = "键值对")
@Data
@NoArgsConstructor
public class KeyValue {

    public KeyValue(String key, String value) {
        this.key = key;
        this.value = value;
    }

    @Schema(description = "选项的值")
    private String key;

    @Schema(description = "选项的标签")
    private String value;

}
