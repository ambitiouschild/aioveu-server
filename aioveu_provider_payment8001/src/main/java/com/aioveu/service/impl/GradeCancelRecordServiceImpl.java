package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeCancelRecordDao;
import com.aioveu.entity.GradeCancelRecord;
import com.aioveu.service.GradeCancelRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GradeCancelRecordServiceImpl extends ServiceImpl<GradeCancelRecordDao, GradeCancelRecord> implements GradeCancelRecordService {


    @Override
    public Boolean create(String userId, String explain, Long gradeId) {
        GradeCancelRecord gradeCancelRecord = new GradeCancelRecord();
        gradeCancelRecord.setGradeId(gradeId);
        gradeCancelRecord.setUserId(userId);
        gradeCancelRecord.setExplainReason(explain);
        return save(gradeCancelRecord);
    }
}
