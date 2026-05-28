package com.aioveu.oms.aioveu08MqConsumeRecord.mapper;


import com.aioveu.oms.aioveu08MqConsumeRecord.model.entity.MqConsumeRecord;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.query.MqConsumeRecordQuery;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.vo.MqConsumeRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MqConsumeRecordMapper
 * @Description TODO MQ消息消费记录Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:29
 * @Version 1.0
 **/
@Mapper
public interface MqConsumeRecordMapper extends BaseMapper<MqConsumeRecord> {

    /**
     * 获取MQ消息消费记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<MqConsumeRecordVo>} MQ消息消费记录分页列表
     */
    Page<MqConsumeRecordVo> getMqConsumeRecordPage(Page<MqConsumeRecordVo> page, MqConsumeRecordQuery queryParams);


    @Select("SELECT * FROM mq_consume_record WHERE message_id = #{messageId} AND consumer_group = #{consumerGroup}")
    MqConsumeRecord selectByMessageAndGroup(@Param("messageId") String messageId,
                                            @Param("consumerGroup") String consumerGroup);

    @Select("""
        SELECT * FROM mq_consume_record 
        WHERE consumer_group = #{consumerGroup} 
          AND consume_status IN (3, 6)  -- FAILED, TIMEOUT
          AND retry_count < #{maxRetryCount}
          AND (next_retry_time IS NULL OR next_retry_time <= #{now})
        ORDER BY create_time ASC
        LIMIT 100
    """)
    List<MqConsumeRecord> selectNeedRetry(@Param("consumerGroup") String consumerGroup,
                                          @Param("maxRetryCount") Integer maxRetryCount,
                                          @Param("now") LocalDateTime now);

    @Select("""
        SELECT 
            consumer_group,
            COUNT(*) as totalCount,
            SUM(CASE WHEN consume_status = 2 THEN 1 ELSE 0 END) as successCount,
            SUM(CASE WHEN consume_status IN (3,4,6) THEN 1 ELSE 0 END) as failureCount,
            AVG(retry_count) as avgRetryCount
        FROM mq_consume_record
        WHERE consumer_group = #{consumerGroup}
          AND create_time BETWEEN #{startTime} AND #{endTime}
        GROUP BY consumer_group
    """)
    Map<String, Object> selectConsumerGroupStats(@Param("consumerGroup") String consumerGroup,
                                                 @Param("startTime") LocalDateTime startTime,
                                                 @Param("endTime") LocalDateTime endTime);

    @Update("""
        UPDATE mq_consume_record 
        SET consume_status = #{status}, 
            error_msg = #{errorMsg},
            finish_time = #{finishTime},
            update_time = #{updateTime}
        WHERE message_id = #{messageId} 
          AND consumer_group = #{consumerGroup}
    """)
    int updateStatus(@Param("messageId") String messageId,
                     @Param("consumerGroup") String consumerGroup,
                     @Param("status") Integer status,
                     @Param("errorMsg") String errorMsg,
                     @Param("finishTime") LocalDateTime finishTime,
                     @Param("updateTime") LocalDateTime updateTime);


    @Select("""
        SELECT COUNT(*) 
        FROM mq_consume_record 
        WHERE message_id = #{messageId}
    """)
    int countByMessageId(@Param("messageId") String messageId);


    @Select("""
    SELECT IFNULL(retry_count, 0)
    FROM mq_consume_record
    WHERE message_id = #{messageId}
      AND consumer_group = #{consumerGroup}
    """)
    Integer selectRetryCount(
            @Param("messageId") String messageId,
            @Param("consumerGroup") String consumerGroup
    );


    @Update("""
    UPDATE mq_consume_record
    SET retry_count = retry_count + 1,
        update_time = NOW()
    WHERE message_id = #{messageId}
      AND consumer_group = #{consumerGroup}
    """)
    int incrementRetryCount(
            @Param("messageId") String messageId,
            @Param("consumerGroup") String consumerGroup
    );
}
