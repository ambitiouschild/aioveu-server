package com.aioveu.system.service;

import com.aioveu.system.model.vo.FileInfoVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Description: TODO 对象存储服务接口层
 * @Author: 雒世松
 * @Date: 2025/6/5 17:29
 * @param
 * @return:
 **/

public interface OssService {

    /**
     * 上传文件
     * @param file 表单文件对象
     * @return 文件信息
     */
    FileInfoVO uploadFile(MultipartFile file);

    /**
     * 删除文件
     *
     * @param filePath 文件完整URL
     * @return 删除结果
     */
    boolean deleteFile(String filePath);


}
