package com.aioveu.tenant.aioveu07File.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: ModuleResolver
 * @Description TODO 自动推断模块名（智能版） 模块分解器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/13 14:50
 * @Version 1.0
 **/
@Component
public class ModuleResolver {

    /**
     * 根据请求上下文推断模块名
     */
    public String resolveModule(HttpServletRequest request) {
        if (request == null) {
            return "common";
        }

        String uri = request.getRequestURI();

        // 根据URI路径推断模块
        if (uri.contains("/user/") || uri.contains("/avatar/") || uri.contains("/profile/")) {
            return "user";
        } else if (uri.contains("/product/") || uri.contains("/goods/") || uri.contains("/item/")) {
            return "product";
        } else if (uri.contains("/order/") || uri.contains("/trade/")) {
            return "order";
        } else if (uri.contains("/article/") || uri.contains("/news/")) {
            return "content";
        } else if (uri.contains("/temp/") || uri.contains("/tmp/")) {
            return "temp";
        }

        return "common";
    }

    /**
     * 根据文件类型推断模块
     */
    public String resolveModuleByFile(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();

        if (contentType == null) {
            return "common";
        }

        if (contentType.startsWith("image/")) {
            if (filename != null && (filename.contains("avatar") || filename.contains("head"))) {
                return "user-avatar";
            } else if (filename != null && (filename.contains("product") || filename.contains("item"))) {
                return "product-image";
            }
            return "image";
        } else if (contentType.startsWith("video/")) {
            return "video";
        } else if (contentType.contains("pdf") || contentType.contains("document")) {
            return "document";
        } else if (contentType.contains("excel") || contentType.contains("sheet")) {
            return "excel";
        }

        return "file";
    }
}
