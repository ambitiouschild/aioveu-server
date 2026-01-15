package com.aioveu.system.aioveu07File.controller;

import com.aioveu.common.result.Result;
import com.aioveu.system.aioveu07File.model.vo.FileInfo;
import com.aioveu.system.aioveu07File.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: FileController
 * @Description TODO  文件控制层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:43
 * @Version 1.0
 **/

@Tag(name = "07.文件接口")
@RestController
@RequestMapping("/api/v1/files")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @PostMapping
    @Operation(summary = "文件上传")
    public Result<FileInfo> uploadFile(
            @Parameter(
                    name = "file",
                    description = "表单文件对象",
                    required = true,
                    in = ParameterIn.DEFAULT,
                    schema = @Schema(name = "file", format = "binary")
            )
            //多图上传，后端接口只认特定的字段名  // 这里的 "file" 必须和前端的字段名完全匹配
            @RequestPart(value = "file") MultipartFile file,
            HttpServletRequest request // 添加请求对象获取协议信息
    ) {
        FileInfo fileInfo = fileService.uploadFile(file);

        /*
         * 解决返回的 URL 是 HTTP 导致前端 HTTPS 页面无法访问的问题，需要修改上传逻辑，确保返回的 FileInfo 对象中的 URL 是 HTTPS 格式
         * */
        // 关键修改：将 HTTP URL 转换为 HTTPS

        if (fileInfo.getUrl() != null && fileInfo.getUrl().startsWith("http://")) {
            // 方案1：直接替换协议（适用于固定域名）
            String httpsUrl = fileInfo.getUrl().replace("http://", "https://");

            // 方案2：动态获取当前请求协议（更灵活）
            String currentProtocol = request.getScheme(); // 获取当前协议（http/https）
            String domain = fileInfo.getUrl().substring(7); // 移除 http://
            httpsUrl = currentProtocol + "://" + domain;

            fileInfo.setUrl(httpsUrl);
        }

        return Result.success(fileInfo);
    }

    @DeleteMapping
    @Operation(summary = "文件删除")
    @SneakyThrows
    public Result<?> deleteFile(
            @Parameter(description = "文件路径") @RequestParam String filePath
    ) {
        boolean result = fileService.deleteFile(filePath);
        return Result.judge(result);
    }
}
