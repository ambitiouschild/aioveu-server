package com.aioveu.tenant.aioveu07File.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: FileInfo
 * @Description TODO 文件信息对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 12:22
 * @Version 1.0
 **/
@Schema(description = "文件对象")
@Data
public class FileInfo {

    @Schema(description = "文件名称")
    private String name;

    @Schema(description = "文件URL")
    private String url;

    @Schema(description = "文件相对路径")
    private String path;
}
