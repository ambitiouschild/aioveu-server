package com.aioveu.system.aioveu07File.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.common.result.ResultCode;
import com.aioveu.system.aioveu07File.model.vo.FileInfo;
import com.aioveu.system.aioveu07File.service.FileService;
import io.minio.*;
import io.minio.http.Method;
import io.minio.messages.Bucket;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @ClassName: MinioFileService
 * @Description TODO  MinIO 文件上传服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:45
 * @Version 1.0
 **/

@Component
@ConditionalOnProperty(value = "oss.type", havingValue = "minio")
@ConfigurationProperties(prefix = "oss.minio")
@RequiredArgsConstructor
@Data
@Slf4j
public class MinioFileService implements FileService {

    /**
     * 服务Endpoint
     */
    private String endpoint;
    /**
     * 访问凭据
     */
    private String accessKey;
    /**
     * 凭据密钥
     */
    private String secretKey;
    /**
     * 存储桶名称
     */
    private String bucketName;
    /**
     * 自定义域名
     */
    private String customDomain;

    private MinioClient minioClient;

    // 依赖注入完成之后执行初始化
    //这个init 方法中的 MinIO 客户端初始化存在几个关键问题，会导致 "Access denied" 错误
    @PostConstruct
    public void init() {
        try{
            // 1. 创建 MinIO 客户端
            log.info("初始化MinIO客户端...");
            minioClient = MinioClient.builder()
                    .endpoint(endpoint)   // 确保 endpoint 格式正确 https://minio.aioveu.com
                    .credentials(accessKey, secretKey)
                    //我们使用的是Minio的Java客户端库，其中Region类在较新的版本中已经被引入（兼容旧版 SDK）
                    // .region(Region.of("us-east-1")) // 必须添加区域  MinIO 需要明确的区域配置   us-east-1 是默认区域
                    .build();

//            // 2. 添加中国区域支持（手动设置）
//            if (endpoint.contains("aliyuncs.com") || endpoint.contains("myqcloud.com")) {
//                // 阿里云OSS或腾讯云COS需要区域
//                minioClient.setRegion("cn-north-1");
//            }

//            log.info("MinIO客户端创建成功，开始检查存储桶: {}", bucketName);
//            // 2. 验证连接
//            minioClient.listBuckets(); // 测试连接是否有效
//
//            // 添加权限测试
//            testMinioPermissions();
//
//            // 3. 创建存储桶并设置策略
//            createBucketIfAbsent(bucketName);
            log.info("MinIO 客户端初始化成功");
            // 关键修改：完全跳过存储桶检查
            log.warn("跳过存储桶检查，请确保存储桶 {} 已存在并配置正确", bucketName);

        }catch (Exception e) {
            log.error("MinIO 初始化失败", e);
            throw new RuntimeException("MinIO 初始化失败: " + e.getMessage());
        }
    }


    /**
     * 上传文件
     *
     * @param file 表单文件对象
     * @return 文件信息
     */
    @Override
    public FileInfo uploadFile(MultipartFile file) {

        // 创建存储桶(存储桶不存在)，如果有搭建好的minio服务，建议放在init方法中
        // 关键修改：跳过存储桶检查
        // createBucketIfAbsent(bucketName); // 注释掉这行

        // 获取文件信息
        // 文件原生名称
        String originalFilename = file.getOriginalFilename();
        // 文件后缀
        String suffix = FileUtil.getSuffix(originalFilename);
        // 文件夹名称
        String dateFolder = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");
        // 文件名称
        String fileName = IdUtil.simpleUUID() + "." + suffix;

        //  try-with-resource 语法糖自动释放流
        try (InputStream inputStream = file.getInputStream()) {
            // 文件上传
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .bucket(bucketName)
                    .object(dateFolder + "/"+ fileName)
                    .contentType(file.getContentType())
                    .stream(inputStream, inputStream.available(), -1) // 使用实际文件大小
                    .build();
            minioClient.putObject(putObjectArgs);

            log.info("文件上传成功: Bucket={}, Object={}",
                    bucketName, dateFolder + "/" + fileName);

            // 返回文件路径
            String fileUrl;


            // 未配置自定义域名
            if (StrUtil.isBlank(customDomain)) {
                // 获取文件URL
                GetPresignedObjectUrlArgs getPresignedObjectUrlArgs = GetPresignedObjectUrlArgs.builder()
                        .bucket(bucketName)
                        .object(dateFolder + "/"+ fileName)
                        .method(Method.GET)
                        .build();

                fileUrl = minioClient.getPresignedObjectUrl(getPresignedObjectUrlArgs);
                fileUrl = fileUrl.substring(0, fileUrl.indexOf("?"));


                // 关键修改：移除端口
                if (fileUrl.contains(":")) {
                    fileUrl = fileUrl.replaceFirst(":\\d+", ""); // 移除端口部分
                }

                // 关键修改：确保返回 HTTPS URL
                if (fileUrl.startsWith("http://")) {
                    fileUrl = fileUrl.replace("http://", "https://");
                }

            } else {
                // 配置自定义文件路径域名
                fileUrl = customDomain + "/"+ bucketName + "/"+ dateFolder + "/"+ fileName;

                // 关键修改：确保自定义域名使用 HTTPS
                if (!fileUrl.startsWith("https://")) {
                    // 如果自定义域名没有协议，添加 https://
                    if (!fileUrl.startsWith("http")) {
                        fileUrl = "https://" + fileUrl;
                    }
                    // 如果自定义域名使用 http://，替换为 https://
                    else if (fileUrl.startsWith("http://")) {
//                        fileUrl = fileUrl.replace("http://", "https://");
                        fileUrl = fileUrl.replace("http://", "http://");
                    }
                }

                // 移除端口
//                if (fileUrl.contains(":")) {
//                    fileUrl = fileUrl.replaceFirst(":\\d+", ""); // 移除端口部分
//                }

            }

            FileInfo fileInfo = new FileInfo();
            fileInfo.setName(originalFilename);
            fileInfo.setUrl(fileUrl);
            return fileInfo;
        } catch (Exception e) {
            log.error("上传文件失败", e);
            throw new BusinessException(ResultCode.UPLOAD_FILE_EXCEPTION, e.getMessage());
        }
    }


    /**
     * 删除文件
     *
     * @param filePath 文件完整路径
     * @return 是否删除成功
     */
    @Override
    public boolean deleteFile(String filePath) {
        Assert.notBlank(filePath, "删除文件路径不能为空");
        try {
            String fileName;
            if (StrUtil.isNotBlank(customDomain)) {
                // https://oss.youlai.tech/default/20221120/test.jpg → 20221120/websocket.jpg
                fileName = filePath.substring(customDomain.length() + 1 + bucketName.length() + 1); // 两个/占了2个字符长度
            } else {
                // http://localhost:9000/default/20221120/test.jpg → 20221120/websocket.jpg
                fileName = filePath.substring(endpoint.length() + 1 + bucketName.length() + 1);
            }
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(bucketName)
                    .object(fileName)
                    .build();

            minioClient.removeObject(removeObjectArgs);
            return true;
        } catch (Exception e) {
            log.error("删除文件失败", e);
            throw new BusinessException(ResultCode.DELETE_FILE_EXCEPTION, e.getMessage());
        }
    }


    /**
     * PUBLIC桶策略
     * 如果不配置，则新建的存储桶默认是PRIVATE，则存储桶文件会拒绝访问 Access Denied
     *
     * @param bucketName 存储桶名称
     * @return 存储桶策略
     */
    private static String publicBucketPolicy(String bucketName) {
        // AWS的S3存储桶策略 JSON 格式 https://docs.aws.amazon.com/zh_cn/AmazonS3/latest/userguide/example-bucket-policies.html
        return "{\"Version\":\"2012-10-17\","
                + "\"Statement\":[{\"Effect\":\"Allow\","
                + "\"Principal\":{\"AWS\":[\"*\"]},"
                + "\"Action\":[\"s3:ListBucketMultipartUploads\",\"s3:GetBucketLocation\",\"s3:ListBucket\"],"
                + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "\"]},"
                + "{\"Effect\":\"Allow\"," + "\"Principal\":{\"AWS\":[\"*\"]},"
                + "\"Action\":[\"s3:ListMultipartUploadParts\",\"s3:PutObject\",\"s3:AbortMultipartUpload\",\"s3:DeleteObject\",\"s3:GetObject\"],"
                + "\"Resource\":[\"arn:aws:s3:::" + bucketName + "/*\"]}]}";
    }


    /**
     * 验证 MinIO 凭证权限
     *
     * @param
     */
    private void testMinioPermissions() {
        try {
            log.info("测试MinIO权限...");

            // 1. 测试列出存储桶权限
            List<Bucket> buckets = minioClient.listBuckets();
            log.info("可访问的存储桶数量: {}", buckets.size());

            // 2. 测试创建临时存储桶权限
            String testBucket = "test-bucket-" + System.currentTimeMillis();
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(testBucket).build());
            log.info("临时存储桶创建成功: {}", testBucket);

            // 3. 清理测试存储桶
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(testBucket).build());
            log.info("权限测试通过");
        } catch (Exception e) {
            log.error("MinIO权限测试失败", e);
            throw new RuntimeException("凭证权限不足: " + e.getMessage());
        }
    }

    /**
     * 创建存储桶(存储桶不存在)
     * 创建存储桶并设置公开读策略
     * @param bucketName 存储桶名称
     */
    @SneakyThrows
    private void createBucketIfAbsent(String bucketName) {

        try {
            // 检查存储桶是否存在
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(bucketName)
                    .build());

            if (!exists) {
                // 创建存储桶
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(bucketName)
                        .build());

                log.info("创建存储桶: {}", bucketName);

                // 设置公开读策略
                String policyJson = String.format(
                        "{\"Version\":\"2012-10-17\",\"Statement\":[{\"Effect\":\"Allow\",\"Principal\":\"*\",\"Action\":[\"s3:GetObject\"],\"Resource\":[\"arn:aws:s3:::%s/*\"]}]}",
                        bucketName
                );

                minioClient.setBucketPolicy(SetBucketPolicyArgs.builder()
                        .bucket(bucketName)
                        .config(policyJson)
                        .build());

                log.info("设置存储桶 {} 的公开读策略", bucketName);
                log.info("存储桶策略设置完成");
            }
        } catch (Exception e) {
            log.error("存储桶操作失败", e);
            throw new BusinessException(ResultCode.SYSTEM_ERROR, "存储桶初始化失败");
        }

    }
}
