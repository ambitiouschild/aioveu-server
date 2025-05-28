package com.aioveu.service.impl;

import com.aioveu.service.UploadService;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.OssUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UploadServiceImpl implements UploadService {

    @Override
    public String upload(String prefix, MultipartFile file) throws Exception {
        String fileType = FileUtil.getFileType(file.getOriginalFilename());
        if(StringUtils.isBlank(fileType)){
            fileType = ".jpg";
        }
        String fileName = prefix + "/" + System.currentTimeMillis() + fileType;
        OssUtil.uploadSingleImage(fileName, file.getInputStream());
        return fileName;
    }
}
