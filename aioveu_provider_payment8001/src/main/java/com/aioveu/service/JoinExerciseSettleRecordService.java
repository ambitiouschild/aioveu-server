package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.JoinExerciseSettleRecord;

import java.util.Date;

/**
 * 拼单活动规则接口
 * @Author： yao
 * @Date： 2024/10/8 09:08
 * @Describe：
 */
public interface JoinExerciseSettleRecordService extends IService<JoinExerciseSettleRecord> {

    /**
     * 结算拼单
     *
     * @param exerciseId
     * @param categoryId
     * @param endTime
     * @param messageId
     */
    void settleJoinOrder(String exerciseId,Long categoryId, Date endTime, String messageId);


    /**
     * 根据产品id获取记录
     * @return
     */
    JoinExerciseSettleRecord getByExerciseId(String exerciseId);
}
