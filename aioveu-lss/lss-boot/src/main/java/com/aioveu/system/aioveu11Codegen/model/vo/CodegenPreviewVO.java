package com.aioveu.system.aioveu11Codegen.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: CodegenPreviewVO
 * @Description TODO
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:39
 * @Version 1.0
 **/

@Schema(description = "代码生成代码预览VO")
@Data
public class CodegenPreviewVO {

    @Schema(description = "生成文件路径")
    private String path;

    @Schema(description = "生成文件名称",example = "SysUser.java" )
    private String fileName;

    @Schema(description = "生成文件内容")
    private String content;
}
