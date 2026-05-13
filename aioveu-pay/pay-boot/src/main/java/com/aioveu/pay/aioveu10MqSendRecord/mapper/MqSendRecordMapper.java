package com.aioveu.pay.aioveu10MqSendRecord.mapper;


import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.Date;
import java.util.List;

/**
 * @ClassName: MqSendRecordMapper
 * @Description TODO MQ消息发送记录Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:40
 * @Version 1.0
 * @Param
 * @return
 **/
@Mapper
public interface MqSendRecordMapper extends BaseMapper<MqSendRecord> {

    /**
     * 获取MQ消息发送记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<MqSendRecordVo>} MQ消息发送记录分页列表
     */
    Page<MqSendRecordVo> getMqSendRecordPage(Page<MqSendRecordVo> page, MqSendRecordQuery queryParams);


    /**
     * 查询发送失败的记录
     * @param maxCount 最大查询数量
     * @return 失败记录列表
     */
    default List<MqSendRecord> selectFailedMessages(int maxCount) {
        LambdaQueryWrapper<MqSendRecord> wrapper = new LambdaQueryWrapper<>();

        // 查询条件：发送失败
        wrapper.eq(MqSendRecord::getSendStatus, 0)  // 0-发送失败
                .eq(MqSendRecord::getIsDeleted, 0)   // 未删除
                .le(MqSendRecord::getRetryCount, 3)  // 重试次数小于等于3
                .isNull(MqSendRecord::getConfirmTime)  // 未确认
                .orderByAsc(MqSendRecord::getSendTime)  // 按发送时间升序
                .last("LIMIT " + maxCount);  // 限制查询数量

        return selectList(wrapper);
    }

    /**
     * 查询需要重试的记录
     * @param maxRetryCount 最大重试次数
     * @param beforeTime 这个时间之前的记录
     * @param maxCount 最大查询数量
     * @return 需要重试的记录列表
     */
    default List<MqSendRecord> selectNeedRetryRecords(int maxRetryCount, Date beforeTime, int maxCount) {
        LambdaQueryWrapper<MqSendRecord> wrapper = new LambdaQueryWrapper<>();

        // 查询条件
        wrapper.eq(MqSendRecord::getSendStatus, 0)  // 发送失败
                .lt(MqSendRecord::getRetryCount, maxRetryCount)  // 重试次数小于最大值
                .lt(MqSendRecord::getNextRetryTime, beforeTime)  // 下次重试时间已到
                .eq(MqSendRecord::getIsDeleted, 0)  // 未删除
                .isNull(MqSendRecord::getConfirmTime)  // 未确认
                .orderByAsc(MqSendRecord::getNextRetryTime)  // 按重试时间排序
                .last("LIMIT " + maxCount);

        return selectList(wrapper);
    }

    /**
     * 查询未确认的记录
     * @param timeoutMinutes 超时分钟数
     * @param maxCount 最大查询数量
     * @return 未确认记录列表
     */
    default List<MqSendRecord> selectUnconfirmedMessages(int timeoutMinutes, int maxCount) {
        Date timeThreshold = new Date(System.currentTimeMillis() - timeoutMinutes * 60 * 1000L);

        LambdaQueryWrapper<MqSendRecord> wrapper = new LambdaQueryWrapper<>();

        wrapper.eq(MqSendRecord::getSendStatus, 1)  // 发送成功
                .isNull(MqSendRecord::getConfirmTime)  // 未确认
                .lt(MqSendRecord::getSendTime, timeThreshold)  // 发送时间超过阈值
                .eq(MqSendRecord::getIsDeleted, 0)  // 未删除
                .orderByAsc(MqSendRecord::getSendTime)  // 按发送时间排序
                .last("LIMIT " + maxCount);

        return selectList(wrapper);
    }

    /**
     * 统计失败的记录数量
     */
    default long countFailedMessages() {
        LambdaQueryWrapper<MqSendRecord> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(MqSendRecord::getSendStatus, 0)
                .eq(MqSendRecord::getIsDeleted, 0);
        return selectCount(wrapper);
    }

    /**
     * 使用注解方式查询
     */
    @Select("""
        SELECT * FROM mq_send_record 
        WHERE send_status = 0 
          AND is_deleted = 0 
          AND retry_count <= 3
          AND confirm_time IS NULL
        ORDER BY send_time ASC 
        LIMIT #{maxCount}
    """)
    List<MqSendRecord> selectFailedMessagesUsingAnnotation(@Param("maxCount") int maxCount);

}
