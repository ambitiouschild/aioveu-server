package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@Data
@TableName("sport_extension_share")
public class ExtensionShare extends IdEntity {

    @NotNull(message = "themeId can not be null!")
    private Long themeId;

    @NotBlank(message = "shareId can not be null!")
    private String shareId;

    @NotBlank(message = "extensionId can not be null!")
    private String extensionId;

}
