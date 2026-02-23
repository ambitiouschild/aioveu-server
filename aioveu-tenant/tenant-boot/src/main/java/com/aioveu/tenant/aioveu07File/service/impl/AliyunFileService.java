package com.aioveu.tenant.aioveu07File.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.aioveu.tenant.aioveu07File.model.vo.FileInfo;
import com.aioveu.tenant.aioveu07File.service.FileService;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;

/**
 * @ClassName: AliyunFileService
 * @Description TODO liyun 对象存储服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 12:26
 * @Version 1.0
 **/

@Component
@ConditionalOnProperty(value = "oss.type", havingValue = "aliyun")
@ConfigurationProperties(prefix = "oss.aliyun")
@RequiredArgsConstructor
@Data
public class AliyunFileService implements FileService {

    /**
     * 服务Endpoint
     */
    private String endpoint;
    /**
     * 访问凭据
     */
    private String accessKeyId;
    /**
     * 凭据密钥
     */
    private String accessKeySecret;
    /**
     * 存储桶名称
     */
    private String bucketName;

    /**
     * 加速域名（可选）
     */
    private String cdnDomain;

    /**
     * 租户ID（从配置文件或用户上下文获取）
     */
    @Value("${tenant.id:default}")
    private String tenantId;


    private OSS aliyunOssClient;

    @PostConstruct
    public void init() {
        aliyunOssClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    @Override
    @SneakyThrows
    public FileInfo uploadFile(MultipartFile file) {

        // 获取文件名称
        String originalFilename = file.getOriginalFilename();


        // 生成文件名(日期文件夹 + 租户ID + 存储桶名)
        String suffix = FileUtil.getSuffix(originalFilename);

        String uuid = IdUtil.simpleUUID();

        // 新文件路径：租户ID/存储桶名/日期/文件名
//        String fileName = DateUtil.format(LocalDateTime.now(), "yyyyMMdd") + "/" + uuid + "." + suffix;

        String fileName = tenantId + "/" + bucketName + "/" +
                DateUtil.format(LocalDateTime.now(), "yyyyMMdd") + "/" +
                uuid + "." + suffix;


        //  try-with-resource 语法糖自动释放流
        try (InputStream inputStream = file.getInputStream()) {

            // 设置上传文件的元信息，例如Content-Type
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            // 创建PutObjectRequest对象，指定Bucket名称、对象名称和输入流
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream, metadata);
            // 上传文件
            aliyunOssClient.putObject(putObjectRequest);
        } catch (Exception e) {
            throw new RuntimeException("文件上传失败");
        }
        // 获取文件访问路径
//        String fileUrl = "https://" + bucketName + "." + endpoint + "/" + fileName;

        // 获取文件访问路径（使用加速域名或原始域名）
        String fileUrl = buildFileUrl(fileName);

        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(originalFilename);
        fileInfo.setUrl(fileUrl);
        fileInfo.setPath(fileName); // 保存相对路径，便于删除
        return fileInfo;
    }

    /**
     * 构建文件访问URL
     * 优先使用加速域名，如果没有配置则使用原始域名
     */
    private String buildFileUrl(String fileName) {
        String url;

        if (cdnDomain != null && !cdnDomain.trim().isEmpty()) {
            // 使用加速域名：https://cdn.aioveu.com/租户ID/存储桶名/日期/文件名
            url = "https://" + cdnDomain + "/" + fileName;
        } else {
            // 使用原始域名：https://bucket.endpoint/租户ID/存储桶名/日期/文件名
            url = "https://" + bucketName + "." + endpoint + "/" + fileName;
        }

        return url;
    }




    @Override
    public boolean deleteFile(String filePath) {
        Assert.notBlank(filePath, "删除文件路径不能为空");

        // 从完整的URL中提取文件名（支持加速域名和原始域名）
        String fileName = extractFileNameFromUrl(filePath);

        // 删除OSS中的文件
        aliyunOssClient.deleteObject(bucketName, fileName);
        return true;
    }

    /**
     * 从完整URL中提取文件名
     * 支持格式：
     * 1. https://cdn.aioveu.com/租户ID/存储桶名/日期/文件名
     * 2. https://bucket.endpoint/租户ID/存储桶名/日期/文件名
     */
    private String extractFileNameFromUrl(String fileUrl) {
        String fileName;

        if (cdnDomain != null && !cdnDomain.trim().isEmpty() &&
                fileUrl.contains(cdnDomain)) {
            // 从加速域名URL中提取
            fileName = fileUrl.substring(fileUrl.indexOf(cdnDomain) + cdnDomain.length() + 1);
        } else if (fileUrl.contains(bucketName + "." + endpoint)) {
            // 从原始域名URL中提取
            String fileHost = "https://" + bucketName + "." + endpoint;
            fileName = fileUrl.substring(fileHost.length() + 1);
        } else {
            // 如果URL中不包含域名，直接使用
            fileName = fileUrl;
        }

        return fileName;
    }

    /**
     * 批量转换已有文件的URL为加速域名
     */
    public String convertToCdnUrl(String originalUrl) {
        if (cdnDomain == null || cdnDomain.trim().isEmpty()) {
            return originalUrl;
        }

        // 如果已经是加速域名，直接返回
        if (originalUrl.contains(cdnDomain)) {
            return originalUrl;
        }

        // 从原始URL中提取文件名
        String fileName = originalUrl.substring(originalUrl.lastIndexOf("/") + 1);

        // 重新构建URL，使用加速域名
        return buildFileUrl(fileName);
    }
}
