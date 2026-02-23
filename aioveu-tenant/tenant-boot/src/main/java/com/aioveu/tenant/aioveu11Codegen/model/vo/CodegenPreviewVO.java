package com.aioveu.tenant.aioveu11Codegen.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: CodegenPreviewVO
 * @Description TODO 代码生成代码预览Vo
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:58
 * @Version 1.0
 **/
@Schema(description = "代码生成代码预览Vo")
@Data
public class CodegenPreviewVO {

    @Schema(description = "生成文件路径")
    private String path;

    @Schema(description = "生成文件名称",example = "SysUser.java" )
    private String fileName;

    @Schema(description = "生成文件内容")
    private String content;

    @Schema(description = "文件范围(frontend/backend)")
    private String scope;

    @Schema(description = "文件语言(扩展名)")
    private String language;
}
