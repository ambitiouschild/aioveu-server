package com.aioveu.service;


import org.springframework.web.multipart.MultipartFile;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface UploadService {

    /**
     * 文件上传
     * @param prefix
     * @param file
     * @return
     * @throws Exception
     */
    String upload(String prefix, MultipartFile file) throws Exception;

}
