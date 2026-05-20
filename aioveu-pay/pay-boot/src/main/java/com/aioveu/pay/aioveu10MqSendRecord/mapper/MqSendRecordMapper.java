package com.aioveu.pay.aioveu10MqSendRecord.mapper;


import com.aioveu.common.rabbitmq.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
     * 统计失败的记录数量
     */
    default Long countBySendStatusIn() {

        // 总失败数
        List<Integer> failedStatuses = Arrays.asList(
                SendStatus.FAILED.getValue(),
                SendStatus.TIMEOUT.getValue(),
                SendStatus.ROUTING_FAILED.getValue(),
                SendStatus.CONFIRM_TIMEOUT.getValue(),
                SendStatus.CONFIRM_NACK.getValue(),
                SendStatus.DEAD.getValue()
        );

        // 使用 QueryWrapper 统计
        QueryWrapper<MqSendRecord> wrapper = new QueryWrapper<>();
        wrapper.in("send_status", failedStatuses);
        Long failedCount = this.selectCount(wrapper);

        return failedCount;
    }

    /**
     * 统计失败的记录数量
     */
    default Long countBySendStatusInAndCreateTimeAfter() {

        // 总失败数
        List<Integer> failedStatuses = Arrays.asList(
                SendStatus.FAILED.getValue(),
                SendStatus.TIMEOUT.getValue(),
                SendStatus.ROUTING_FAILED.getValue(),
                SendStatus.CONFIRM_TIMEOUT.getValue(),
                SendStatus.CONFIRM_NACK.getValue(),
                SendStatus.DEAD.getValue()
        );

        // 获取今日开始时间
        LocalDateTime todayStart = LocalDate.now().atStartOfDay();
        // 使用 QueryWrapper 统计
        QueryWrapper<MqSendRecord> wrapper = new QueryWrapper<>();
        wrapper.in("send_status", failedStatuses)
                .ge("create_time", todayStart);

        Long todayFailedCount = this.selectCount(wrapper);

        return todayFailedCount;
    }


    /**
     * 按状态统计
     */
    default Map<String, Long> countBySendStatus() {

        // 总失败数
        List<Integer> failedStatuses = Arrays.asList(
                SendStatus.FAILED.getValue(),
                SendStatus.TIMEOUT.getValue(),
                SendStatus.ROUTING_FAILED.getValue(),
                SendStatus.CONFIRM_TIMEOUT.getValue(),
                SendStatus.CONFIRM_NACK.getValue(),
                SendStatus.DEAD.getValue()
        );

        // 批量查询统计
        Map<String, Long> statusCountMap = new HashMap<>();
        for (SendStatus status : SendStatus.values()) {
            if (failedStatuses.contains(status.getValue())) {
                QueryWrapper<MqSendRecord> queryWrapper = new QueryWrapper<>();
                queryWrapper.eq("send_status", status.getValue());

                Long count = this.selectCount(queryWrapper);
                statusCountMap.put(status.getLabel(), count);
            }
        }

        return statusCountMap;
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


    /**
     * 根据messageId查询消息记录
     */
    @Select("SELECT * FROM mq_send_record WHERE message_id = #{messageId} LIMIT 1")
    MqSendRecord findByMessageId(@Param("messageId") String messageId);


    /**
     * 根据messageId查询（使用Wrapper）
     */
    default MqSendRecord findByMessageIdWrapper(String messageId) {
        QueryWrapper<MqSendRecord> wrapper = new QueryWrapper<>();
        wrapper.eq("message_id", messageId);
        return selectOne(wrapper);
    }


    /**
     * 批量查询消息记录
     */
    @Select("<script>" +
            "SELECT * FROM mq_send_record WHERE message_id IN " +
            "<foreach collection='messageIds' item='id' open='(' separator=',' close=')'>" +
            "   #{id}" +
            "</foreach>" +
            "</script>")
    List<MqSendRecord> findByMessageIds(@Param("messageIds") List<String> messageIds);


    /**
     * 根据消息ID更新状态
     */
    @Update("UPDATE mq_send_record SET send_status = #{status}, error_msg = #{errorMsg}, update_time = NOW() " +
            "WHERE message_id = #{messageId}")
    int updateStatusByMessageId(@Param("messageId") String messageId,
                                @Param("status") Integer status,
                                @Param("errorMsg") String errorMsg);

    /**
     * 根据消息ID更新重试次数
     */
    @Update("UPDATE mq_send_record SET retry_count = retry_count + 1, update_time = NOW() " +
            "WHERE message_id = #{messageId}")
    int incrementRetryCount(@Param("messageId") String messageId);



    /**
     * 查询可重试的消息记录
     * 条件：状态在指定列表中 且 重试次数小于指定值
     */
    @Select("<script>" +
            "SELECT * FROM mq_send_record " +
            "WHERE send_status IN " +
            "<foreach collection='statuses' item='status' open='(' separator=',' close=')'>" +
            "   #{status}" +
            "</foreach>" +
            " AND (retry_count IS NULL OR retry_count &lt; #{maxRetryCount})" +
            " ORDER BY send_time ASC" +
            "</script>")
    List<MqSendRecord> findBySendStatusInAndRetryCountLessThan(
            @Param("statuses") List<Integer> statuses,
            @Param("maxRetryCount") Integer maxRetryCount
    );


    /**
     * 增强版：查询可重试消息（包含更多过滤条件）
     */
    @Select("<script>" +
            "SELECT * FROM mq_send_record " +
            "WHERE send_status IN " +
            "<foreach collection='statuses' item='status' open='(' separator=',' close=')'>" +
            "   #{status}" +
            "</foreach>" +
            " AND (retry_count IS NULL OR retry_count &lt; #{maxRetryCount})" +
            " AND (next_retry_time IS NULL OR next_retry_time &lt;= NOW())" +  // 下次重试时间已到
            " AND (expire_time IS NULL OR expire_time &gt; NOW())" +           // 未过期
            " ORDER BY send_time ASC" +
            " LIMIT #{limit}" +  // 限制数量
            "</script>")
    List<MqSendRecord> findRetryableMessages(
            @Param("statuses") List<Integer> statuses,
            @Param("maxRetryCount") Integer maxRetryCount,
            @Param("limit") Integer limit
    );


    /**
     * 统计各租户的失败记录数量
     * @param failedStatuses 失败状态列表
     * @return List<Object[]> 每个数组包含 [tenantId, count]
     */
    @Select("<script>" +
            "SELECT tenant_id, COUNT(*) as count " +
            "FROM mq_send_record " +
            "WHERE send_status IN " +
            "<foreach collection='failedStatuses' item='status' open='(' separator=',' close=')'>" +
            "#{status}" +
            "</foreach>" +
            " GROUP BY tenant_id" +
            "</script>")
    List<Object[]> countFailedByTenant(@Param("failedStatuses") List<Integer> failedStatuses);



    /**
     * 根据发送状态和创建时间删除记录
     * @param sendStatus 发送状态
     * @param createTime 创建时间
     * @param tenantId 租户ID（可选）
     * @return 删除的记录数
     */
    int deleteBySendStatusAndCreateTimeBefore(
            @Param("sendStatus") Integer sendStatus,
            @Param("createTime") LocalDateTime  createTime, // 改为 LocalDateTime
            @Param("tenantId") String tenantId
    );

    /**
     * 根据发送状态列表和创建时间删除记录
     * @param sendStatuses 发送状态列表
     * @param createTime 创建时间
     * @return 删除的记录数
     */
    int deleteBySendStatusesAndCreateTimeBefore(
            @Param("sendStatuses") List<Integer> sendStatuses,
            @Param("createTime") Date createTime
    );


    /**
     * 根据条件删除记录
     * @param sendStatus 发送状态（可为空）
     * @param createTime 创建时间（可为空）
     * @param batchSize 批量删除大小（可选，防止删除过多）
     * @return 删除的记录数
     */
    int deleteRecords(
            @Param("sendStatus") Integer sendStatus,
            @Param("createTime") Date createTime,
            @Param("batchSize") Integer batchSize
    );

}
