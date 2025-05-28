package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.CheckRecord;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface CheckRecordService extends IService<CheckRecord> {

    /**
     * 创建核销码
     * @param checkRecord
     * @param username
     * @return
     */
    boolean create(CheckRecord checkRecord, String username);


    /**
     * 获取核销列表
     * @param page
     * @param size
     * @param username
     * @param storeId
     * @return
     */
    IPage<CheckRecord> getList(int page, int size, String username, Long storeId);

    /**
     * 核销
     * @param id
     * @param username
     * @return
     */
    String check(Long id, String username);

    /**
     * 批量删除
     * @param ids
     * @param username
     * @return
     */
    boolean batchDelete(List<Long> ids, String username);


    /**
     * 上传核销码图片前，校验图片中的二维码是否能识别、距离门店等信息
     * @param prefix
     * @param file
     * @return
     */
    String uploadPreCheck(Long storeId, Long companyId,String prefix, MultipartFile file, String scanText);


}
