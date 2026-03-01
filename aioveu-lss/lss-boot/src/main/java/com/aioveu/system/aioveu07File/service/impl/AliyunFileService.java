package com.aioveu.system.aioveu07File.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.IdUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.system.aioveu07File.model.vo.FileInfo;
import com.aioveu.system.aioveu07File.service.FileService;
import com.aioveu.system.aioveu07File.utils.FilePathGenerator;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.nacos.shaded.io.grpc.netty.shaded.io.netty.channel.ConnectTimeoutException;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.cdn.model.v20141111.PushObjectCacheRequest;
import com.aliyuncs.cdn.model.v20141111.PushObjectCacheResponse;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.profile.DefaultProfile;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * @ClassName: AliyunFileService
 * @Description TODO  Aliyun 对象存储服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 17:44
 * @Version 1.0
 **/

@Component
@ConditionalOnProperty(value = "oss.type", havingValue = "aliyun")
@ConfigurationProperties(prefix = "oss.aliyun")
@RequiredArgsConstructor
@Data
@Slf4j
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
    private Long tenantId;


    private String cdnRegion;  //shanghai


    private OSS aliyunOssClient;


    // Lombok 会生成构造器
    // 如果没有在构造器中声明，@RequiredArgsConstructor 不会包含这个字

    //修复方法1：添加 @Autowired
    @Autowired
    private FilePathGenerator filePathGenerator;  // 没有 @Autowired，也没有在构造器中

    //修复方法2：使用构造器注入

    @PostConstruct
    public void init() {

        log.info("初始化OSS客户端...");
        log.info("endpoint: {}", endpoint);  // ❌ 错误：内网地址（只能在阿里云ECS内访问） 你的OSS Endpoint使用的是内网地址：oss-cn-shanghai-internal.aliyuncs.com
        // 检查是否是内网地址
        if (endpoint != null && endpoint.contains("-internal.")) {
            log.warn("⚠️ 使用内网Endpoint: {}，本地开发环境可能无法连接", endpoint);
            log.warn("建议在本地开发时使用公网Endpoint");
        }

        log.info("改为公网Endpoint（推荐）: oss-cn-shanghai.aliyuncs.com");
        log.info("accessKeyId: {}", accessKeyId != null ? "已配置" : "未配置");
        log.info("accessKeySecret: {}", accessKeySecret != null ? "已配置" : "未配置");
        log.info("bucketName: {}", bucketName);

        /*
        TODO
         方案2：如果你在阿里云ECS上
                如果你确实在阿里云上海区域的ECS上运行，内网地址是正确的，但需要检查：
                1.ECS和OSS在同一区域：都在上海区域
                2.安全组规则：允许ECS访问OSS内网
                3.网络类型：ECS是经典网络还是VPC
        */

        if (endpoint == null || accessKeyId == null || accessKeySecret == null) {
            log.error("OSS配置不完整，无法初始化客户端");
            return;
        }

        try {
            aliyunOssClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
            log.info("OSS客户端初始化成功");

            // 测试连接
            testOssConnection();

        } catch (Exception e) {
            log.error("OSS客户端初始化失败", e);

            // 如果是内网地址，提示修改
            if (endpoint != null && endpoint.contains("-internal.")) {
                log.error("❌ 内网Endpoint无法在本地访问，请修改为公网Endpoint");
                log.error("当前: {}", endpoint);
                log.error("建议改为: {}", endpoint.replace("-internal.", "."));
            }
        }
    }

    private void testOssConnection() {
        try {
            log.info("测试OSS连接...");

            // 1. 检查Bucket是否存在
            boolean bucketExists = aliyunOssClient.doesBucketExist(bucketName);
            log.info("Bucket[{}] 存在: {}", bucketName, bucketExists);

            if (!bucketExists) {
                log.error("Bucket不存在: {}", bucketName);
                return;
            }

            // 2. 尝试简单操作
            String testKey = "connection-test-" + System.currentTimeMillis() + ".txt";
            String testContent = "Connection test at " + new Date();

            // 上传测试文件
            aliyunOssClient.putObject(bucketName, testKey,
                    new ByteArrayInputStream(testContent.getBytes()));
            log.info("✅ 测试文件上传成功: {}", testKey);

            // 删除测试文件
            aliyunOssClient.deleteObject(bucketName, testKey);
            log.info("✅ 测试文件清理成功");

            log.info("✅ OSS连接测试通过");

        } catch (Exception e) {
            log.error("OSS连接测试失败", e);
        }
    }

    @Override
    @SneakyThrows //代码执行到生成文件名后就停止了，说明在调用OSS上传时出现了异常，但被 @SneakyThrows隐藏了。
    public FileInfo uploadFile(MultipartFile file , String moduleName) {

        // 获取文件名称
        String originalFilename = file.getOriginalFilename();
        log.info("获取文件名称:{}",originalFilename);


        // 从安全上下文获取租户ID 如果你需要多租户，考虑从安全上下文获取：
//        Long tenantId = SecurityUtils.getCurrentTenantId();

        if(tenantId == null){
            // 直接使用配置的租户ID
            Long tenantId = this.tenantId;
            log.info("直接使用配置的租户ID:{}",tenantId);

        }



        // 检查 filePathGenerator
        if (filePathGenerator == null) {
            log.error("filePathGenerator 为 null!");
            throw new IllegalStateException("文件路径生成器未初始化");
        }
        log.info("开始生成文件路径...");
        // 使用路径生成器
        String fileName = filePathGenerator.generatePath(tenantId, bucketName, moduleName, originalFilename);

        log.info("生成的文件名fileName：{}",fileName);

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

        // 2. 构建两种URL
        // 获取文件访问路径（使用加速域名或原始域名） CDN地址（用于后续访问）
        String cdnUrl = buildFileUrl(fileName);

        // OSS直链（用于立即显示）
        String ossDirectUrl = buildOssDirectUrl(fileName);

        //这是CDN的正常行为，新文件需要首次访问触发CDN回源缓存。可以通过预热、刷新缓存或配置短缓存时间来解决。
        log.info("获取文件访问路径（使用加速域名或原始域名）fileUrl：{}", cdnUrl);
        log.info("这是CDN的正常行为，新文件需要首次访问触发CDN回源缓存。可以通过预热、刷新缓存或配置短缓存时间来解决");
        log.info("如果不想修改代码，可以在阿里云控制台手动刷新");
        log.info("1.进入 CDN控制台 → 刷新预热");
        log.info("2.选择 目录刷新 或 URL刷新");
        log.info("3.输入需要刷新的URL或目录");
        log.info("4.提交刷新任务");

        log.info("方案1：上传成功后自动预热（推荐）");
        // ... 3. 自动预热CDN（异步，不阻塞返回） ...  3. 异步预热CDN（不阻塞主流程）
        warmUpCdnAsync(cdnUrl);

        // 4. 使用HEAD请求（更快）,同步触发一次访问（强制预热） 添加强制刷新机制（结合预热）
        warmUpWithThreeLevel(cdnUrl);

        // 4. 返回包含双地址的FileInfo
        FileInfo fileInfo = new FileInfo();
        fileInfo.setName(originalFilename);
        fileInfo.setUrl(cdnUrl);     // 主URL：CDN地址
        fileInfo.setDirectUrl(ossDirectUrl);    // 备选URL：OSS直链  预热后返回OSS直链作为fallback 如果CDN预热需要时间，可先返回OSS直链，确保立即显示
        fileInfo.setPath(fileName); // 保存相对路径，便于删除
//        fileInfo.setModule(moduleName); // 保存模块信息到数据库
        fileInfo.setWarmUpStatus("pending"); // 预热状态
        fileInfo.setCreatedAt(new Date());

        log.info("文件上传成功，返回双地址 - OSS: {}, CDN: {}", ossDirectUrl, cdnUrl);
        return fileInfo;
    }

    /**
     * 构建OSS直链地址
     * 格式：https://bucket.endpoint/file/path
     * 用于确保文件立即可访问，不依赖CDN缓存
     */
    /**
     * 智能构建文件URL（支持双地址返回）
     * 优先返回CDN地址，但确保有可用的OSS直链作为备用
     */
    private String buildOssDirectUrl(String fileName) {

        // 参数验证
        if (StringUtils.isBlank(bucketName)) {
            throw new IllegalArgumentException("Bucket名称不能为空");
        }
        if (StringUtils.isBlank(endpoint)) {
            throw new IllegalArgumentException("OSS Endpoint不能为空");
        }
        if (StringUtils.isBlank(fileName)) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        // 构建OSS直链
        String url = String.format("https://%s.%s/%s",
                bucketName,
                endpoint,
                fileName
        );

        log.debug("构建OSS直链: {}", url);
        return url;
    }

    /**
     * 三级触发策略：
     * 1. HEAD请求快速触发（最快）
     * 2. 官方API全球预热（最全）
     * 3. 多节点并发触发（备份）
     */
    private void warmUpWithThreeLevel(String url) {


        // 第一级：快速HEAD触发
        CompletableFuture.runAsync(() -> triggerUrlAccessWithHeadFast(url));
        log.info("三级触发策略:1. HEAD请求快速触发（最快）");

        // 第二级：官方API预热（全球覆盖）  其他地理区域的用户访问时，仍然需要回源,启用官方API预热，这是最有效的方式  因为会阻塞接口4秒
        CompletableFuture.runAsync(() -> {
            try {
                log.info("三级触发策略:2. 官方API全球预热（最全）");
                log.info("启动官方CDN预热API");
                triggerUrlAccessCdnApi(url);  // 调用阿里云官方API

            } catch (Exception e) {
                log.warn("官方预热失败，使用第三级备份");
                // 第三级：多节点并发触发
//                triggerUrlAccessMultiRegion(url);
                log.info("三级触发策略:3. 多节点并发触发（备份）");
            }
        });
    }



    /**
     * 使用HEAD请求（更快）,同步访问URL，强制触发最近节点缓存
     */
    /**
     * TODO
     *  针对CDN触发优化的HEAD请求
     *  一句话回答：您不主动预热，文件不会自动缓存到您附近的服务器。
     *  用户首次访问时，必须经过“远程回源”（从源站 OSS 拉取）这一步骤，这会很慢。预热就是为了解决“首次访问慢”而主动执行的操作。
     *  不预热，就不会有就近缓存。文件只会安静地待在源站 OSS 里。
     *  预热，就是“用一次慢速访问，换取后续所有快速访问”的投资。
     *  您遇到的“4秒慢”是预热操作本身的耗时，不是上传的耗时。优化方向是让预热操作异步化，不阻塞主流程，同时为前端提供降级方案，保证体验下限。
     *  最终建议：采用“快速返回 + 异步预热 + 前端智能加载”的组合策略。这样既能保证您的操作流畅，又能让用户（包括您自己）在多数情况下享受到 CDN 加速带来的“秒开”体验。
     *
     *  TODO
     *      关键点：CDN 加速地址不代表立即加速。它只是指定了一条“智能路径”，最终的加速效果取决于文件是否已缓存在用户附近的 CDN 节点上。
     *      这确实是 CDN 的典型行为：加速地址的“加速”效果，在文件首次被请求时是不存在的。
     *      方案一：异步预热，立即返回（推荐）
     *          这是目前最合理的架构。您立即返回 CDN 地址，同时在后台异步预热。
     *      方案二：双地址返回，前端智能选择
     *          后端返回两个地址，由前端根据网络情况智能选择。
     *      方案三：分阶段返回（高级体验）
     *          对于用户体验要求极高的场景：
     *      案例：阿里云OSS + CDN 典型配置
     *          根据阿里云的官方实践，标准的图片上传显示流程是：
     *              1.上传阶段：直接传到OSS，返回OSS地址
     *              2.处理阶段：异步进行图片处理（缩略、水印等）
     *              3.分发阶段：异步预热到CDN
     *              4.显示阶段：前端先显示低质量预览，后台加载高质量
     *          您不需要等待缓存完成再回显，但需要：
     *              1.接受“首次访问可能较慢”的现实
     *              2.通过技术手段优化这个“首次”的体验
     *              3.确保后续访问都是快速的
     */
    private void triggerUrlAccessWithHeadFast(String url) {
        try {

            log.info("使用HEAD请求（更快）,同步访问URL，强制触发最近节点缓存");
            // 使用连接池，避免重复创建
            PoolingHttpClientConnectionManager connManager =
                    new PoolingHttpClientConnectionManager();
            connManager.setMaxTotal(5);
            connManager.setDefaultMaxPerRoute(5);

            // 创建HTTP客户端
//            CloseableHttpClient httpClient = HttpClients.createDefault();
//            HttpHead httpHead = new HttpHead(url);  // 使用HEAD而不是GET

            // 极短超时，只验证可达性  // 针对CDN优化的超时
            // 设置更宽容的超时，目标是“完成请求”，而非“极速”
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(2000)      /// 连接超时 2秒
                    .setSocketTimeout(3000)        // 读取超时 3秒
                    .setRedirectsEnabled(false)  // 禁用重定向
                    .setCircularRedirectsAllowed(false)
                    .build();

            //极速触发
            try (CloseableHttpClient httpClient = HttpClients.custom()
                    .setConnectionManager(connManager)
                    .setDefaultRequestConfig(config)
                    .build()) {

                HttpHead httpHead = new HttpHead(url);

                // 添加真实浏览器头，避免被CDN过滤
                httpHead.setHeader("User-Agent",
                        "Mozilla/5.0 (compatible; CDN-WarmUp/1.0)");
                httpHead.setHeader("Accept", "*/*");

                long startTime = System.currentTimeMillis();

                try (CloseableHttpResponse response = httpClient.execute(httpHead)) {
                    long endTime = System.currentTimeMillis();
                    int statusCode = response.getStatusLine().getStatusCode();

                    log.info("✅ 使用HEAD请求（更快）CDN触发成功 - 耗时: {}ms, 状态码: {}, URL: {}",
                            (endTime - startTime), statusCode, url);

                    // 记录CDN缓存状态
                    Header xCache = response.getFirstHeader("X-Cache");
                    Header age = response.getFirstHeader("Age");
                    if (xCache != null) {
                        log.info("CDN状态: {}, Age: {}",
                                xCache.getValue(),
                                age != null ? age.getValue() : "0");
                    }
                }
            }






//            httpHead.setConfig(config);

            // 执行HEAD请求
//            try (CloseableHttpResponse response = httpClient.execute(httpHead)) {
//                // HEAD请求更快，不下载body
//                int statusCode = response.getStatusLine().getStatusCode();
//                log.info("HEAD触发完成，状态码: {}，URL: {}", statusCode, url);
//
//                // 可选：检查响应头，确认是否命中CDN缓存
//                Header xCacheHeader = response.getFirstHeader("X-Cache");
//                if (xCacheHeader != null) {
//                    log.info("CDN缓存状态: {}", xCacheHeader.getValue());
//                }
//
//            }
//
//            httpClient.close();


        } catch (SocketTimeoutException e) {

            // 完全静默失败，不打印任何日志
            // 这个请求的唯一目的是“触发”，成功或失败都不影响核心业务流程
            // 如果需要监控，可以在此处增加计数器
            log.warn("HEAD请求超时: {} - {}", url, e.getMessage());
        } catch (ConnectTimeoutException e) {
            log.warn("连接超时: {} - {}", url, e.getMessage());
        } catch (IOException e) {
            log.warn("网络IO异常: {} - {}", url, e.getMessage());
        } catch (Exception e) {
            log.warn("HEAD请求异常: {} - {}", url, e.getMessage());
        }
    }




    /**
     * 同步访问URL，强制触发最近节点缓存
     */
    private void triggerUrlAccessCdnApi(String url) {
        try {
            log.info("同步访问URL，强制触发最近节点缓存");
            // 简单HTTP GET请求，触发访问
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            httpGet.setHeader("User-Agent", "CDN-WarmUp-Trigger/1.0");

            // 设置短超时，只触发不等待完整响应
            RequestConfig config = RequestConfig.custom()
                    .setConnectTimeout(3000)
                    .setSocketTimeout(3000)
                    .build();
            httpGet.setConfig(config);

            // 执行请求（获取状态码即可）
            try (CloseableHttpResponse response = httpClient.execute(httpGet)) {
                int statusCode = response.getStatusLine().getStatusCode();
                log.info("URL触发访问完成，状态码: {}，URL: {}", statusCode, url);
            }

            httpClient.close();
        } catch (Exception e) {
            log.warn("URL触发访问失败: {} - {}", url, e.getMessage());
        }
    }

    /**
     * 增加并发触发（触发多个节点）
     */
    private void triggerUrlAccessMultiRegion(String url) {
        List<String> cdnEdgeNodes = Arrays.asList(
                "https://cdn-beijing.yourdomain.com",  // 北京节点
                "https://cdn-shanghai.yourdomain.com", // 上海节点
                "https://cdn-guangzhou.yourdomain.com" // 广州节点
        );

        // 并发触发多个节点
        CompletableFuture<?>[] futures = cdnEdgeNodes.stream()
                .map(baseUrl -> CompletableFuture.runAsync(() -> {
                    String nodeUrl = baseUrl + url.substring(url.indexOf("/", 8));
                    triggerUrlAccessCdnApi(nodeUrl);
                }))
                .toArray(CompletableFuture[]::new);

        CompletableFuture.allOf(futures).join();
    }

    /**
     * 异步预热CDN
     */
    private void warmUpCdnAsync(String cdnUrl) {
        CompletableFuture.runAsync(() -> {
            try {
                warmUpCdn(cdnUrl);
            } catch (Exception e) {
                log.warn("CDN预热失败: {}", e.getMessage());
            }
        });
    }

    /**
     * 调用阿里云CDN预热API
     */
    private void warmUpCdn(String cdnUrl) {
        if (StringUtils.isBlank(accessKeyId) || StringUtils.isBlank(accessKeySecret)) {
            log.debug("CDN密钥未配置，跳过预热");
            return;
        }

        try {
            // 使用阿里云SDK
            DefaultProfile profile = DefaultProfile.getProfile(
                    cdnRegion,
                    accessKeyId,
                    accessKeySecret
            );

            DefaultAcsClient client = new DefaultAcsClient(profile);

            // 创建预热请求
            PushObjectCacheRequest request = new PushObjectCacheRequest();
            request.setObjectPath(cdnUrl);
            request.setArea("domestic"); // 国内预热

            // 执行预热
            PushObjectCacheResponse response = client.getAcsResponse(request);

            log.info("CDN预热已提交，任务ID: {}", response.getPushTaskId());
            log.info("预热URL: {}", cdnUrl);

        } catch (ServerException e) {
            log.error("CDN预热服务端异常: {}", e.getMessage());
        } catch (ClientException e) {
            log.error("CDN预热客户端异常: {}", e.getMessage());
        } catch (Exception e) {
            log.error("CDN预热失败: {}", e.getMessage());
        }
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
        String fileHost = "https://" + bucketName + "." + endpoint; // 文件主机域名
        String fileName = filePath.substring(fileHost.length() + 1); // +1 是/占一个字符，截断左闭右开
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
