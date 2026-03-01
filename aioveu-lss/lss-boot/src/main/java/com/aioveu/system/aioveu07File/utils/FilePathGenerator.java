package com.aioveu.system.aioveu07File.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * @ClassName: FilePathGenerator
 * @Description TODO 创建专门的文件路径生成器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/1 14:24
 * @Version 1.0
 **/

@Slf4j
@Component
public class FilePathGenerator {

    /**
     * 生成文件存储路径
     * 格式：租户ID/模块名/存储桶名/日期/文件名
     */
    public String generatePath(Long tenantId, String bucketName, String moduleName,
                               String originalFilename) {
        String suffix = FileUtil.getSuffix(originalFilename);
        String uuid = IdUtil.simpleUUID();
        String dateStr = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");

        // 1. 处理 tenantId 为 null
        if (tenantId == null) {
            tenantId = 0L;  // 默认值
            log.warn("租户ID为null，使用默认值0");
        }

        // 2. 处理 moduleName 为 null 如果模块名为空，使用默认值
        if (StringUtils.isBlank(moduleName)) {
            moduleName = "common";
        }

        // 3. 处理 bucketName 为 null
        if (bucketName == null) {
            bucketName = "default-bucket";
            log.warn("存储桶名为null，使用默认值");
        }

        String path=  String.format("%s/%s/%s/%s/%s.%s",
                bucketName, tenantId, moduleName, dateStr, uuid, suffix);

        log.info("生成文件存储路径:{}",path);

        return path;
    }

    /**
     * 生成文件存储路径（带子模块）
     * 格式：租户ID/模块名/子模块名/存储桶名/日期/文件名
     */
    public String generatePathWithSubModule(Long tenantId, String bucketName,
                                            String moduleName, String subModule,
                                            String originalFilename) {
        String suffix = FileUtil.getSuffix(originalFilename);
        String uuid = IdUtil.simpleUUID();
        String dateStr = DateUtil.format(LocalDateTime.now(), "yyyyMMdd");

        if (StringUtils.isBlank(moduleName)) {
            moduleName = "common";
        }
        if (StringUtils.isBlank(subModule)) {
            subModule = "default";
        }

        return String.format("%s/%s/%s/%s/%s/%s.%s",
                tenantId, moduleName, subModule, bucketName, dateStr, uuid, suffix);
    }
}
