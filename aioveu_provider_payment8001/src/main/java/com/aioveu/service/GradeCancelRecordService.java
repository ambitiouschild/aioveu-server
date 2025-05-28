package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.GradeCancelRecord;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface GradeCancelRecordService extends IService<GradeCancelRecord> {

    /**
     * 创建一条记录
     * @param userId
     * @param explain
     * @param gradeId
     * @return
     */
    Boolean create(String userId, String explain, Long gradeId);



}
