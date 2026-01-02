package com.aioveu.system.aioveu07File.service;

import com.aioveu.system.aioveu07File.model.vo.FileInfo;
import org.springframework.web.multipart.MultipartFile;

/**
  *@ClassName: ConfigService
  *@Description TODO  对象存储服务接口层
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/20 18:01
  *@Version 1.0
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
