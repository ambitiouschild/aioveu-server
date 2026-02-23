package com.aioveu.tenant.aioveu07File.service;

import com.aioveu.tenant.aioveu07File.model.vo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * @ClassName: FileService
 * @Description TODO 对象存储服务接口层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 12:22
 * @Version 1.0
 **/
public interface FileService {

    /**
     * 上传文件
     * @param file 表单文件对象
     * @return 文件信息
     */
    FileInfo uploadFile(MultipartFile file);

    /**
     * 删除文件
     *
     * @param filePath 文件完整URL
     * @return 删除结果
     */
    boolean deleteFile(String filePath);
}
