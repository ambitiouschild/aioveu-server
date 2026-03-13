package com.aioveu.tenant.aioveu07File.model.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;

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

    /**
     * 主URL - CDN加速地址
     * 用于保存到数据库、分享链接
     */
    @Schema(description = "文件URL")
    private String url;

    /**
     * 备选URL - OSS直链地址
     * 用于立即显示，确保可用性
     */
    @Schema(description = "OSS直链地址")
    private String directUrl;

    /**
     * OSS存储路径
     */
    @Schema(description = "文件相对路径")
    private String path;

    /**
     * 文件大小（字节）
     */
    private Long size;

    /**
     * 文件类型
     */
    private String contentType;

    /**
     * CDN预热状态
     * pending - 待预热
     * processing - 预热中
     * completed - 预热完成
     * failed - 预热失败
     */
    private String warmUpStatus = "pending";

    /**
     * 预热任务ID
     */
    private String warmUpTaskId;

    /**
     * 创建时间
     */
    private Date createdAt;

    /**
     * CDN就绪时间
     */
    private Date cdnReadyAt;

    /**
     * 获取推荐显示的URL
     * 如果CDN已就绪，返回CDN地址，否则返回OSS直链
     */
    public String getDisplayUrl() {
        // 这里可以根据业务逻辑判断
        // 简单实现：总是先返回直链
        return this.directUrl;

        // 或者根据状态判断
        // if ("completed".equals(this.warmUpStatus)) {
        //     return this.url; // CDN地址
        // } else {
        //     return this.directUrl; // OSS直链
        // }
    }


}
