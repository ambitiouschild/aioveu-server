package com.aioveu.pay.aioveu10MqSendRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu10MqSendRecord.converter.MqSendRecordConverter;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.SendRecordStats;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu10MqSendRecord.utils.MessageIdGenerator;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @ClassName: MqSendRecordServiceImpl
 * @Description TODO MQ消息发送记录服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:48
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class MqSendRecordServiceImpl extends ServiceImpl<MqSendRecordMapper, MqSendRecord> implements MqSendRecordService {

    private final MqSendRecordConverter mqSendRecordConverter;

    private final MessageIdGenerator messageIdGenerator;


    /**
     * 获取MQ消息发送记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqSendRecordVo>} MQ消息发送记录分页列表
     */
    @Override
    public IPage<MqSendRecordVo> getMqSendRecordPage(MqSendRecordQuery queryParams) {
        Page<MqSendRecordVo> page = this.baseMapper.getMqSendRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ消息发送记录表单数据
     *
     * @param id MQ消息发送记录ID
     * @return MQ消息发送记录表单数据
     */
    @Override
    public MqSendRecordForm getMqSendRecordFormData(Long id) {
        MqSendRecord entity = this.getById(id);
        return mqSendRecordConverter.toForm(entity);
    }

    /**
     * 新增MQ消息发送记录
     *
     * @param formData MQ消息发送记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqSendRecord(MqSendRecordForm formData) {
        MqSendRecord entity = mqSendRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ消息发送记录
     *
     * @param id   MQ消息发送记录ID
     * @param formData MQ消息发送记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqSendRecord(Long id,MqSendRecordForm formData) {
        MqSendRecord entity = mqSendRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ消息发送记录
     *
     * @param ids MQ消息发送记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqSendRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ消息发送记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }



    @Override
    @Transactional(rollbackFor = Exception.class)
    public String saveMqSendRecord(String topic, String tag, String bizId, Object message) {

        // 生成消息ID
        String messageId = messageIdGenerator.generatePaymentMessageId(bizId);

        MqSendRecord record = new MqSendRecord();
        record.setMessageId(messageId);
        record.setBizId(bizId);
        record.setTopic(topic);
        record.setTag(tag);
        record.setShardingKey(bizId);  // 使用业务ID作为分片key
        record.setMessageBody(JSON.toJSONString(message));
        record.setSendStatus(SendStatus.PENDING.getValue());
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());

        this.save(record);

        return messageId;
    }


    /**
     * 更新MQ消息发送记录
     *
     * @param messageId   MQ消息发送记录ID
     * @param status MQ消息发送记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateSendStatus(String messageId, SendStatus status, String errorMsg) {

        MqSendRecord record = new MqSendRecord();
        record.setMessageId(messageId);
        record.setSendStatus(status.getValue());
        record.setErrorMsg(errorMsg);
        record.setUpdateTime(LocalDateTime.now());
        if (status == SendStatus.SUCCESS) {
            record.setConfirmTime(LocalDateTime.now());
        } else if (status == SendStatus.FAILED) {
            // 失败时设置下次重试时间（指数退避）
            MqSendRecord oldRecord = this.baseMapper.selectById(messageId);
            int retryCount = oldRecord.getRetryCount() + 1;

            // 计算下次重试时间戳（毫秒）
            long nextRetryTimestamp = System.currentTimeMillis() +
                    (long) (Math.pow(2, retryCount) * 30000);
            // 转换为 LocalDateTime
            LocalDateTime nextRetryTime = Instant.ofEpochMilli(nextRetryTimestamp)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            record.setRetryCount(retryCount);
            record.setNextRetryTime(nextRetryTime);
        }

        return this.updateById(record);

    }



    /**
     * 调试解析消息ID
     * 返回解析结果，便于调用方使用
     */
    @Override
    public MessageIdGenerator.MessageIdInfo debugMessage(String messageId) {
        if (messageId == null || messageId.trim().isEmpty()) {
            log.warn("消息ID为空，无法解析");
            return null;
        }

        MessageIdGenerator.MessageIdInfo info = messageIdGenerator.parseMessageId(messageId);
        if (info == null) {
            log.warn("消息ID格式无效: {}", messageId);
            return null;
        }

        // 详细日志
        log.info("""
            ====== 消息ID解析结果 ======
            原始消息ID: {}
            应用名称: {}
            时间戳: {}
            工作节点: {}
            序列号: {}
            随机数: {}
            业务类型: {}
            业务编号: {}
            ===========================
            """,
                messageId,
                info.getAppName(),
                info.getTimestamp(),
                info.getWorkerId(),
                info.getSequence(),
                info.getRandom(),
                info.getBizType(),
                info.getBizNo()
        );

        return info;
    }


    /**
     * 调试并验证消息ID
     */
    @Override
    public boolean validateAndDebugMessage(String messageId) {
        // 验证格式
        boolean isValid = messageIdGenerator.validateMessageId(messageId);

        if (!isValid) {
            log.error("消息ID格式错误: {}", messageId);
            return false;
        }

        // 解析并调试
        MessageIdGenerator.MessageIdInfo info = debugMessage(messageId);

        if (info == null) {
            return false;
        }

        // 额外验证
        LocalDateTime createTime = parseTimestamp(info.getTimestamp());
        if (createTime == null) {
            log.error("消息ID时间戳解析失败: {}", info.getTimestamp());
            return false;
        }

        // 检查是否过期（比如超过24小时）
        Duration duration = Duration.between(createTime, LocalDateTime.now());
        if (duration.toHours() > 24) {
            log.warn("消息ID已过期，创建于: {}", createTime);
        }

        return true;
    }

    /**
     * 解析时间戳字符串
     */
    private LocalDateTime parseTimestamp(String timestampStr) {
        try {
            if (timestampStr == null || timestampStr.length() != 17) {
                return null;
            }

            // 格式: yyyyMMddHHmmssSSS
            return LocalDateTime.of(
                    Integer.parseInt(timestampStr.substring(0, 4)),   // 年
                    Integer.parseInt(timestampStr.substring(4, 6)),   // 月
                    Integer.parseInt(timestampStr.substring(6, 8)),   // 日
                    Integer.parseInt(timestampStr.substring(8, 10)),  // 时
                    Integer.parseInt(timestampStr.substring(10, 12)), // 分
                    Integer.parseInt(timestampStr.substring(12, 14)), // 秒
                    Integer.parseInt(timestampStr.substring(14, 17)) * 1000000  // 纳秒
            );
        } catch (Exception e) {
            log.error("解析时间戳失败: {}", timestampStr, e);
            return null;
        }
    }


    /**
     * 批量调试消息ID
     */
    @Override
    public Map<String, MessageIdGenerator.MessageIdInfo> batchDebugMessages(List<String> messageIds) {
        Map<String, MessageIdGenerator.MessageIdInfo> result = new HashMap<>();

        for (String messageId : messageIds) {
            MessageIdGenerator.MessageIdInfo info = messageIdGenerator.parseMessageId(messageId);
            result.put(messageId, info);

            if (info != null) {
                log.debug("消息ID解析: {} -> {}", messageId, info.getBizType());
            }
        }

        return result;
    }


    /**
     * 查询发送失败的记录
     * @param maxCount 最大查询数量
     * @return 失败记录列表
     */

    /*
    *   selectFailedMessages方法的核心功能是查询发送失败的消息记录，通常用于：
            1.补偿任务：定时重试发送失败的消息
            2.监控告警：统计失败率，触发告警
            3.人工处理：在管理后台展示失败记录，供人工处理
            4.数据分析：分析失败原因，优化发送策略
    *
    *
    * */
    @Override
    public List<MqSendRecord> selectFailedMessages(int maxCount) {
        try {
            log.info("查询发送失败的记录，最大数量: {}", maxCount);

            List<MqSendRecord> failedRecords = this.baseMapper.selectFailedMessages(maxCount);

            log.info("查询到 {} 条发送失败的记录", failedRecords.size());
            return failedRecords;

        } catch (Exception e) {
            log.error("查询发送失败的记录异常", e);
            throw new RuntimeException("查询发送失败的记录失败", e);
        }

    }

    /**
     * 查询需要重试的记录
     */
    @Override
    public List<MqSendRecord> selectNeedRetryRecords(int maxRetryCount, Date beforeTime, int maxCount) {
        try {
            log.info("查询需要重试的记录: maxRetryCount={}, beforeTime={}, maxCount={}",
                    maxRetryCount, beforeTime, maxCount);

            List<MqSendRecord> retryRecords = this.baseMapper
                    .selectNeedRetryRecords(maxRetryCount, beforeTime, maxCount);

            log.info("查询到 {} 条需要重试的记录", retryRecords.size());
            return retryRecords;

        } catch (Exception e) {
            log.error("查询需要重试的记录异常", e);
            throw new RuntimeException("查询需要重试的记录失败", e);
        }
    }

    /**
     * 查询未确认的记录
     */
    @Override
    public List<MqSendRecord> selectUnconfirmedMessages(int timeoutMinutes, int maxCount) {
        try {
            log.info("查询未确认的记录: timeoutMinutes={}, maxCount={}", timeoutMinutes, maxCount);

            List<MqSendRecord> unconfirmedRecords = this.baseMapper
                    .selectUnconfirmedMessages(timeoutMinutes, maxCount);

            log.info("查询到 {} 条未确认的记录", unconfirmedRecords.size());
            return unconfirmedRecords;

        } catch (Exception e) {
            log.error("查询未确认的记录异常", e);
            throw new RuntimeException("查询未确认的记录失败", e);
        }
    }

    /**
     * 批量更新重试信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateRetryInfo(List<Long> ids, int retryCount, LocalDateTime nextRetryTime) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }

        try {
            int updatedCount = 0;
            LocalDateTime updateTime = LocalDateTime.now();

            for (Long id : ids) {
                MqSendRecord record = new MqSendRecord();
                record.setId(id);
                record.setRetryCount(retryCount);
                record.setNextRetryTime(nextRetryTime);
                record.setUpdateTime(updateTime);

                int rows = this.baseMapper.updateById(record);
                if (rows > 0) {
                    updatedCount++;
                }
            }

            log.info("批量更新重试信息: 共{}条，成功{}条", ids.size(), updatedCount);
            return updatedCount;

        } catch (Exception e) {
            log.error("批量更新重试信息异常", e);
            throw new RuntimeException("批量更新重试信息失败", e);
        }
    }

    /**
     * 统计各类状态的消息数量
     */
    @Override
    public SendRecordStats getSendRecordStats(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            LambdaQueryWrapper<MqSendRecord> wrapper = new LambdaQueryWrapper<>();

            if (startTime != null) {
                wrapper.ge(MqSendRecord::getCreateTime, startTime);
            }

            if (endTime != null) {
                wrapper.le(MqSendRecord::getCreateTime, endTime);
            }

            wrapper.eq(MqSendRecord::getIsDeleted, 0);

            // 统计总数
            long totalCount = this.baseMapper.selectCount(wrapper);

            // 统计成功数
            LambdaQueryWrapper<MqSendRecord> successWrapper = wrapper.clone();
            successWrapper.eq(MqSendRecord::getSendStatus, SendStatus.SUCCESS.getValue());
            long successCount = this.baseMapper.selectCount(successWrapper);

            // 统计失败数
            LambdaQueryWrapper<MqSendRecord> failedWrapper = wrapper.clone();
            failedWrapper.eq(MqSendRecord::getSendStatus, SendStatus.FAILED.getValue());
            long failedCount = this.baseMapper.selectCount(failedWrapper);

            // 统计未确认数
            LambdaQueryWrapper<MqSendRecord> unconfirmedWrapper = wrapper.clone();
            unconfirmedWrapper.eq(MqSendRecord::getSendStatus, SendStatus.SUCCESS.getValue())
                    .isNull(MqSendRecord::getConfirmTime);
            long unconfirmedCount = this.baseMapper.selectCount(unconfirmedWrapper);

            return SendRecordStats.builder()
                    .totalCount(totalCount)
                    .successCount(successCount)
                    .failedCount(failedCount)
                    .unconfirmedCount(unconfirmedCount)
                    .successRate(totalCount > 0 ? (successCount * 100.0 / totalCount) : 0.0)
                    .build();

        } catch (Exception e) {
            log.error("统计发送记录异常", e);
            throw new RuntimeException("统计发送记录失败", e);
        }
    }


    /**
     * 获取详细的发送记录统计
     */
    @Override
    public SendRecordStats getDetailedSendRecordStats(LocalDateTime startTime, LocalDateTime endTime) {
        try {
            LambdaQueryWrapper<MqSendRecord> wrapper = new LambdaQueryWrapper<>();

            if (startTime != null) {
                wrapper.ge(MqSendRecord::getCreateTime, startTime);
            }

            if (endTime != null) {
                wrapper.le(MqSendRecord::getCreateTime, endTime);
            }

            wrapper.eq(MqSendRecord::getIsDeleted, 0);

            // 查询所有记录
            List<MqSendRecord> allRecords = this.baseMapper.selectList(wrapper);

            SendRecordStats stats = SendRecordStats.builder()
                    .startTime(startTime)
                    .endTime(endTime)
                    .totalCount((long) allRecords.size())
                    .statsTime(LocalDateTime.now())
                    .build();

            // 按状态统计
            Map<Integer, Long> statusCounts = allRecords.stream()
                    .collect(Collectors.groupingBy(MqSendRecord::getSendStatus, Collectors.counting()));

            Long successCount = statusCounts.getOrDefault(SendStatus.SUCCESS.getValue(), 0L);
            Long failedCount = statusCounts.getOrDefault(SendStatus.FAILED.getValue(), 0L);

            stats.setSuccessCount(successCount);
            stats.setFailedCount(failedCount);

            // 未确认数
            Long unconfirmedCount = allRecords.stream()
                    .filter(record -> record.getSendStatus() == SendStatus.SUCCESS.getValue())
                    .filter(record -> record.getConfirmTime() == null)
                    .count();
            stats.setUnconfirmedCount(unconfirmedCount);

            // 已确认数
            Long confirmedCount = allRecords.stream()
                    .filter(record -> record.getSendStatus() == SendStatus.SUCCESS.getValue())
                    .filter(record -> record.getConfirmTime() != null)
                    .count();
            stats.setConfirmedCount(confirmedCount);

            // 重试统计
            Long retryTotalCount = allRecords.stream()
                    .mapToLong(MqSendRecord::getRetryCount)
                    .sum();
            stats.setRetryTotalCount(retryTotalCount);

            Integer maxRetryCount = allRecords.stream()
                    .mapToInt(MqSendRecord::getRetryCount)
                    .max()
                    .orElse(0);
            stats.setMaxRetryCount(maxRetryCount);

            // 正在重试中的消息数
            Long retryingCount = allRecords.stream()
                    .filter(record -> record.getSendStatus() == SendStatus.FAILED.getValue())
                    .filter(record -> record.getRetryCount() > 0)
                    //record.getNextRetryTime().after(new Date())
                    //record.getNextRetryTime().isAfter(LocalDateTime.now())
                    .filter(record -> record.getNextRetryTime() != null && record.getNextRetryTime().isAfter(LocalDateTime.now()))
                    .count();
            stats.setRetryingCount(retryingCount);

            // 按Topic统计
            Map<String, List<MqSendRecord>> recordsByTopic = allRecords.stream()
                    .collect(Collectors.groupingBy(MqSendRecord::getTopic));

            List<SendRecordStats.TopicStats> topicStatsList = new ArrayList<>();
            for (Map.Entry<String, List<MqSendRecord>> entry : recordsByTopic.entrySet()) {
                String topic = entry.getKey();
                List<MqSendRecord> topicRecords = entry.getValue();

                long topicTotal = topicRecords.size();
                long topicSuccess = topicRecords.stream()
                        .filter(r -> r.getSendStatus() == SendStatus.SUCCESS.getValue())
                        .count();
                double topicSuccessRate = topicTotal > 0 ? (topicSuccess * 100.0 / topicTotal) : 0.0;

                topicStatsList.add(SendRecordStats.TopicStats.builder()
                        .topic(topic)
                        .totalCount(topicTotal)
                        .successCount(topicSuccess)
                        .successRate(topicSuccessRate)
                        .build());
            }
            stats.setTopicStats(topicStatsList);

            // 按状态统计
            List<SendRecordStats.StatusStats> statusStatsList = new ArrayList<>();
            for (Map.Entry<Integer, Long> entry : statusCounts.entrySet()) {
                Integer status = entry.getKey();
                Long count = entry.getValue();
                // 在需要获取 label 的地方
                String statusDesc = Arrays.stream(SendStatus.values())
                        .filter(s -> s.getValue().equals(status))
                        .findFirst()
                        .map(SendStatus::getLabel)
                        .orElse("未发送");

                double rate = allRecords.size() > 0 ? (count * 100.0 / allRecords.size()) : 0.0;

                statusStatsList.add(SendRecordStats.StatusStats.builder()
                        .status(status)
                        .statusDesc(statusDesc)
                        .count(count)
                        .rate(rate)
                        .build());
            }
            stats.setStatusStats(statusStatsList);

            // 计算比率
            stats.calculateRates();

            return stats;

        } catch (Exception e) {
            log.error("获取详细发送记录统计异常", e);
            throw new RuntimeException("获取详细发送记录统计失败", e);
        }
    }



}
