package com.aioveu.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description 灵活课包
 * @author: 雒世松
 * @date: 2025/8/27 0027 6:20
 */
@Data
public class TopicCategoryForm {

    private Long id;

    @NotEmpty(message = "名称不能为空")
    private String name;

    @NotNull(message = "主题id不能为空")
    private Long topicId;

    @NotEmpty(message = "图标不能为空")
    private String icon;

    @NotEmpty(message = "编号不能为空")
    private String code;

    private Integer priority;

}
