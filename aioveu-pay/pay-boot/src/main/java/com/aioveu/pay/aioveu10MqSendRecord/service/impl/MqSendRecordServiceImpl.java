package com.aioveu.pay.aioveu10MqSendRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.rabbitmq.producer.model.vo.*;
import com.aioveu.pay.aioveu10MqSendRecord.converter.MqSendRecordConverter;
import com.aioveu.common.rabbitmq.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.SendRecordStats;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.common.util.MessageIdGenerator;
import com.aioveu.pay.aioveu12MqProducerPayment.utils.RetryResultCreator;
import com.aioveu.pay.aioveu12MqProducerPayment.model.query.FailedMessageQuery;
import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
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
    private static final ObjectMapper objectMapper = new ObjectMapper();
    // 正在重试的消息（防止重复重试）
    private final Map<String, Long> retryingMessages = new ConcurrentHashMap<>();

    private final RetryResultCreator retryResultCreator;


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



    //------------------------------------------------------------------------
    /**
     * 记录失败消息（主要方法）
     */
    @Transactional
    public boolean recordFailure(RabbitSendResult result) {
        if (result == null || result.getMessageId() == null) {
            log.warn("无法记录空结果");
            return false;
        }

        try {
            // 1. 查找或创建记录
            MqSendRecord entity = findOrCreateRecord(result);

            // 2. 更新失败信息
            updateFailureInfo(entity, result);

            // 3. 计算下次重试时间
            calculateNextRetryTime(entity);

            // 4. 保存记录
            this.save(entity);

            // 5. 记录日志
            logFailure(entity, result);

            // 6. 发送告警（如果需要）
            sendAlertIfNeeded(entity);

            return true;

        } catch (Exception e) {
            log.error("记录失败消息异常: messageId={}", result.getMessageId(), e);
            return false;
        }
    }

    /**
     * 记录失败消息（带原始Message）
     */
    @Transactional
    public boolean recordFailure(Message rabbitMessage, RabbitSendResult result) {
        boolean recorded = recordFailure(result);

        if (recorded && rabbitMessage != null) {
            try {

                // 方法1：使用自定义的注解查询
//                MqSendRecordEntity entity = mqSendRecordMapper.findByMessageId(messageId);

                // 方法2：使用Wrapper查询
                MqSendRecord entity = this.baseMapper.findByMessageIdWrapper(result.getMessageId());

                if (entity != null) {
                    // 补充消息体信息
                    entity.setMessageBody(new String(rabbitMessage.getBody(), StandardCharsets.UTF_8));
//                    entity.setMessageSize(rabbitMessage.getBody().length);

                    // 保存消息属性
                    Map<String, Object> props = extractMessageProperties(rabbitMessage);
                    if (!props.isEmpty()) {
                        addExtraInfo(entity, "messageProperties", props);
                    }

                    this.save(entity);
                }
            } catch (Exception e) {
                log.error("补充消息体信息失败", e);
            }
        }

        return recorded;
    }

    /**
     * 记录异常
     */
    @Transactional
    public boolean recordException(String messageId, Exception e,
                                   String exchange, String routingKey,
                                   Long tenantId, String messageType) {
        RabbitSendResult result = RabbitSendResult.failure(
                        messageId,
                        e.getMessage(),
                        0L,
                        exchange,
                        routingKey
                )
                .withTenant(tenantId)
                .withMessageType(messageType)
                .addExtraInfo("exceptionClass", e.getClass().getName())
                .addExtraInfo("stackTrace", getStackTrace(e));

        return recordFailure(result);
    }

    /**
     * 重试失败消息
     */
    @Transactional
    public RetryResult retryMessage(String messageId) {
        long startTime = System.currentTimeMillis();

        if (messageId == null || messageId.trim().isEmpty()) {
            // 修复：使用正确的参数调用failure方法
            return retryResultCreator.createInvalidIdResult(startTime);
        }

        // 防止重复重试
        if (retryingMessages.containsKey(messageId)) {
            //       log.info("创建重复重试的结果");
            return retryResultCreator.createDuplicateRetryResult(messageId, startTime);
        }

        try {
            retryingMessages.put(messageId, System.currentTimeMillis());

            // 1. 查找记录
            MqSendRecord mqSendRecord = this.baseMapper.findByMessageId(messageId);

            // 使用 Optional.ofNullable()包装，转换为Optional
            Optional<MqSendRecord> optional = Optional.ofNullable(mqSendRecord);


            if (!optional.isPresent()) {

                return retryResultCreator.createNotFoundResult(messageId, startTime);
            }

            MqSendRecord entity = optional.get();

            // 2. 检查是否可以重试

            // 2. 检查是否可以重试
            RetryCheckResult checkResult = retryResultCreator.checkIfCanRetry(entity);

            if (!checkResult.isCanRetry()) {
                return retryResultCreator.createCannotRetryResult(entity, checkResult, startTime);
            }

            // 3. 更新状态为重试中
            // 3. 更新状态为重试中
            retryResultCreator.updateEntityAsRetrying(entity);

            this.save(entity);

            // 4. 执行重试
            RetryResult retryResult = retryResultCreator.executeRetry(entity , startTime);

            // 5. 更新重试结果
            retryResultCreator.updateEntityAfterRetry(entity, retryResult);

            return retryResult;

        } catch (Exception e) {
            log.error("重试消息异常: messageId={}", messageId, e);
            return // 提供所有必要参数
                    RetryResult.failure(
                            "messageId",      // 原始消息ID
                            "错误信息",        // 错误信息
                            0L,               // 耗时
                            null              // 重试次数
                    );

        } finally {
            retryingMessages.remove(messageId);
        }
    }











    /**
     * 批量重试
     */
    @Transactional
    public BatchRetryResult batchRetry(List<String> messageIds) {
        BatchRetryResult result = new BatchRetryResult();

        for (String messageId : messageIds) {
            try {
                RetryResult retryResult = retryMessage(messageId);
                result.addResult(messageId, retryResult);
            } catch (Exception e) {
                result.addError(messageId, e.getMessage());
            }
        }

        return result;
    }

    /**
     * 获取可重试的消息
     */
    public List<MqSendRecord> getRetryableMessages() {
        // 查询状态为失败、超时、路由失败等的消息
        List<Integer> retryableStatuses = Arrays.asList(
                SendStatus.FAILED.getValue(),
                SendStatus.TIMEOUT.getValue(),
                SendStatus.ROUTING_FAILED.getValue(),
                SendStatus.CONFIRM_TIMEOUT.getValue(),
                SendStatus.CONFIRM_NACK.getValue()
        );

        return this.baseMapper.findBySendStatusInAndRetryCountLessThan(
                retryableStatuses,
                3  // 最大重试次数
        );
    }

    /**
     * 查询失败消息 - 返回 MyBatis-Plus Page
     */
    public com.baomidou.mybatisplus.extension.plugins.pagination.Page<MqSendRecord>
    queryFailedMessages(FailedMessageQuery query, Pageable pageable) {

        try {
            // 创建 MyBatis-Plus Page
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<MqSendRecord> mpPage =
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(
                            pageable.getPageNumber() + 1,
                            pageable.getPageSize()
                    );

            // 构建查询条件
            QueryWrapper<MqSendRecord> wrapper = new QueryWrapper<>();

            // 失败状态
            wrapper.in("send_status",
                    SendStatus.FAILED.getValue(),
                    SendStatus.TIMEOUT.getValue(),
                    SendStatus.ROUTING_FAILED.getValue(),
                    SendStatus.CONFIRM_TIMEOUT.getValue(),
                    SendStatus.CONFIRM_NACK.getValue(),
                    SendStatus.DEAD.getValue()
            );

            // 租户ID
            if (StringUtils.isNotBlank(query.getTenantId())) {
                wrapper.eq("tenant_id", query.getTenantId());
            }

            // 消息类型
            if (StringUtils.isNotBlank(query.getMessageType())) {
                wrapper.eq("message_type", query.getMessageType());
            }

            // 排序
            if (pageable.getSort().isSorted()) {
                pageable.getSort().forEach(order -> {
                    if (order.isAscending()) {
                        wrapper.orderByAsc(order.getProperty());
                    } else {
                        wrapper.orderByDesc(order.getProperty());
                    }
                });
            } else {
                wrapper.orderByDesc("create_time");
            }

            // 执行查询并直接返回 MyBatis-Plus Page
            return this.baseMapper.selectPage(mpPage, wrapper);

        } catch (Exception e) {
            log.error("查询失败消息异常", e);
            throw new RuntimeException("查询失败: " + e.getMessage(), e);
        }
    }

    /**
     * 获取失败统计
     */
    public FailedMessageStats getFailedStats() {
        FailedMessageStats stats = new FailedMessageStats();

        Long failedCount = this.baseMapper.countBySendStatusIn();

        stats.setFailedCount(failedCount);

        stats.setTodayFailed(this.baseMapper.countBySendStatusInAndCreateTimeAfter());


        Map<String, Long> statusCountMap = this.baseMapper.countBySendStatus();
        stats.getStatusStatistics().putAll(statusCountMap);


        // 假设 failedStatuses 是从其他地方获取的
        List<Integer> failedStatuses = Arrays.asList(3, 4, 5 , 6); // 假设 2,3,4 是失败状态

        // 按租户统计
        List<Object[]> tenantStats = this.baseMapper.countFailedByTenant(failedStatuses);
        for (Object[] row : tenantStats) {
            // 注意：根据数据库字段类型，可能需要调整类型转换
            String tenantId = String.valueOf(row[0]);  // 使用 String.valueOf 更安全
            Long count = ((Number) row[1]).longValue(); // 处理不同的数字类型

            // 创建统计对象
            FailedMessageStats tenantStat = FailedMessageStats.createWithCount(count);
            // 可以在这里设置其他统计信息

            stats.getTenantStatistics().put(tenantId, tenantStat);
        }

        // 可重试数量
        stats.setRetryableCount(countRetryableMessages());

        return stats;
    }

    /**
     * 清理旧数据
     */
    @Transactional
    public int cleanupOldRecords(int days) {
        LocalDateTime expireTime = LocalDateTime.now().minusDays(days);

        // 只清理已成功的消息
        int deletedCount = this.baseMapper.deleteBySendStatusAndCreateTimeBefore(
                SendStatus.SUCCESS.getValue(),
                expireTime,
                null  // 不按租户筛选，传 null
//                "your-tenant-id"  // 特定租户
        );

        log.info("清理旧记录: 天数={}, 删除数量={}", days, deletedCount);
        return deletedCount;
    }

    // ========== 私有辅助方法 ==========

    private MqSendRecord findOrCreateRecord(RabbitSendResult result) {
        MqSendRecord record = this.baseMapper.findByMessageId(result.getMessageId());
        if (record == null) {
            record = createNewRecord(result);
        }
        return record;

    }

    private MqSendRecord createNewRecord(RabbitSendResult result) {
        MqSendRecord entity = new MqSendRecord();
//        entity.setId(messageIdGenerator.generateMessageId());
        entity.setMessageId(result.getMessageId());
        entity.setCorrelationId(result.getCorrelationId());
        entity.setTenantId(result.getTenantId());
        entity.setMessageType(result.getMessageType());
        entity.setExchange(result.getExchange());
        entity.setRoutingKey(result.getRoutingKey());
        entity.setCreateTime(LocalDateTime.now());
        return entity;
    }

    private void updateFailureInfo(MqSendRecord entity, RabbitSendResult result) {
        // 转换状态
        SendStatus status = convertToSendStatus(result.getSendStatus());
        entity.setSendStatus(status.getValue());

        // 设置错误信息
        entity.setErrorMsg(buildErrorDetail(result));

        // 分析错误码
        String errorCode = analyzeErrorCode(result);
//        entity.setErrorCode(errorCode);

        // 设置耗时和时间
        entity.setCostTime(result.getCostTime());
        entity.setSendTime(convertToLocalDateTime(result.getSendTime()));
        entity.setConfirmTime(convertToLocalDateTime(result.getConfirmTime()));
        entity.setUpdateTime(LocalDateTime.now());

        // 设置重试信息
        if (entity.getRetryCount() == null) {
            entity.setRetryCount(0);
        }

        // 设置扩展信息
        addExtraInfo(entity, "failureDetails", result.getExtraInfo());
        addExtraInfo(entity, "sendResult", result.toMap());

//        // 设置消息大小
//        if (result.getExtraInfo() != null && result.getExtraInfo().containsKey("messageSize")) {
//            entity.setMessageSize((Integer) result.getExtraInfo().get("messageSize"));
//        }
    }

    private SendStatus convertToSendStatus(SendStatus rabbitStatus) {
        if (rabbitStatus == null) {
            return SendStatus.FAILED;
        }

        switch (rabbitStatus) {
            case SUCCESS:
                return SendStatus.SUCCESS;
            case FAILED:
                return SendStatus.FAILED;
            case TIMEOUT:
            case CONFIRM_TIMEOUT:
                return SendStatus.TIMEOUT;
            case ROUTING_FAILED:
                return SendStatus.ROUTING_FAILED;
            case CONFIRM_NACK:
                return SendStatus.CONFIRM_NACK;
            default:
                return SendStatus.UNKNOWN;
        }
    }

    private String buildErrorDetail(RabbitSendResult result) {
        StringBuilder detail = new StringBuilder();

        if (result.getErrorMessage() != null) {
            detail.append(result.getErrorMessage());
        }

        if (result.getAckCause() != null) {
            detail.append(" [ACK Cause: ").append(result.getAckCause()).append("]");
        }

        if (result.getReplyText() != null) {
            detail.append(" [Reply: ").append(result.getReplyText()).append("]");
        }

        return detail.toString();
    }

    private String analyzeErrorCode(RabbitSendResult result) {
        if (result.getSendStatus() == null) {
            return "UNKNOWN";
        }

        switch (result.getSendStatus()) {
            case TIMEOUT:
            case CONFIRM_TIMEOUT:
                return "TIMEOUT";
            case ROUTING_FAILED:
                if (result.getReplyCode() != null) {
                    switch (result.getReplyCode()) {
                        case 312: return "NO_ROUTE";
                        case 313: return "NO_CONSUMERS";
                        case 403: return "ACCESS_REFUSED";
                        case 404: return "NOT_FOUND";
                        default: return "ROUTING_ERROR";
                    }
                }
                return "ROUTING_FAILED";
            case CONFIRM_NACK:
                return "BROKER_NACK";
            case FAILED:
                if (result.getErrorMessage() != null &&
                        result.getErrorMessage().toLowerCase().contains("network")) {
                    return "NETWORK_ERROR";
                }
                return "SEND_FAILURE";
            default:
                return "UNKNOWN_ERROR";
        }
    }

    private void calculateNextRetryTime(MqSendRecord entity) {
        Integer statusValue = entity.getSendStatus(); // 假设这是从数据库读取的值
        SendStatus status = SendStatus.fromValue(statusValue);

        if (!isRetryable(status)) {
            return;
        }

        // 指数退避算法
        int retryCount = entity.getRetryCount() != null ? entity.getRetryCount() : 0;
        long delaySeconds = (long) Math.pow(2, retryCount);
        delaySeconds = Math.min(delaySeconds, 3600); // 最多1小时

        LocalDateTime nextRetryTime = LocalDateTime.now().plusSeconds(delaySeconds);

        addExtraInfo(entity, "nextRetryTime", nextRetryTime.toString());
        addExtraInfo(entity, "retryDelay", delaySeconds + "s");
    }

    private boolean isRetryable(SendStatus status) {
        return status == SendStatus.FAILED ||
                status == SendStatus.TIMEOUT ||
                status == SendStatus.CONFIRM_TIMEOUT ||
                status == SendStatus.CONFIRM_NACK;
    }

    private boolean canRetry(MqSendRecord entity) {
        // 检查状态是否可重试

        Integer statusValue = entity.getSendStatus(); // 假设这是从数据库读取的值
        SendStatus status = SendStatus.fromValue(statusValue);

        if (!isRetryable(status)) {
            return false;
        }

        // 检查重试次数
        if (entity.getRetryCount() != null && entity.getRetryCount() >= 3) {
            return false;
        }

        // 检查下次重试时间
        String nextRetryTimeStr = getExtraInfo(entity, "nextRetryTime", String.class);
        if (nextRetryTimeStr != null) {
            try {
                LocalDateTime nextRetryTime = LocalDateTime.parse(nextRetryTimeStr);
                if (LocalDateTime.now().isBefore(nextRetryTime)) {
                    return false;
                }
            } catch (Exception e) {
                // 解析失败，允许重试
            }
        }

        return true;
    }



    private void updateRetryResult(MqSendRecord entity, RetryResult retryResult) {
        if (retryResult.isSuccess()) {
            entity.setSendStatus(SendStatus.SUCCESS.getValue());
            entity.setConfirmTime(LocalDateTime.now());
            entity.setCostTime(retryResult.getCostTime());
            entity.setErrorMsg(null); // 清空错误信息

            log.info("重试成功: messageId={}, retryCount={}",
                    entity.getMessageId(), entity.getRetryCount());
        } else {
            entity.setSendStatus(SendStatus.FAILED.getValue());
            entity.setErrorMsg("重试失败: " + retryResult.getError());

            log.warn("重试失败: messageId={}, error={}",
                    entity.getMessageId(), retryResult.getError());
        }

        entity.setUpdateTime(LocalDateTime.now());
        this.save(entity);
    }



    private Map<String, Object> extractMessageProperties(Message rabbitMessage) {
        Map<String, Object> props = new HashMap<>();
        MessageProperties properties = rabbitMessage.getMessageProperties();

        if (properties != null) {
            props.put("contentType", properties.getContentType());
            props.put("contentEncoding", properties.getContentEncoding());
            props.put("headers", properties.getHeaders());
            props.put("correlationId", properties.getCorrelationId());
            props.put("replyTo", properties.getReplyTo());
            props.put("priority", properties.getPriority());
            props.put("deliveryMode", properties.getDeliveryMode());
            props.put("timestamp", properties.getTimestamp());
        }

        return props;
    }

    /**
     * 安全地添加扩展信息
     */
    public static <T> void addExtraInfo(MqSendRecord entity, String key, T value) {
        if (entity == null || key == null) {
            return;
        }

        try {
            Map<String, Object> extraInfo = getExtraInfoMap(entity);
            extraInfo.put(key, value);
            // 直接设置 Map 对象，不转换为字符串
            entity.setExtraInfo(extraInfo);

        } catch (Exception e) {
            log.warn("添加扩展信息失败, key: {}, value: {}", key, value, e);
        }
    }

    /**
     * 获取扩展信息并转换为指定类型
     */
    public static <T> T getExtraInfo(MqSendRecord entity, String key, Class<T> clazz) {
        if (entity == null || key == null || clazz == null) {
            return null;
        }

        try {
            Map<String, Object> extraInfo = getExtraInfoMap(entity);
            Object value = extraInfo.get(key);

            if (value == null) {
                return null;
            }

            // 如果类型匹配，直接返回
            if (clazz.isInstance(value)) {
                return clazz.cast(value);
            }

            // 否则尝试转换
            return objectMapper.convertValue(value, clazz);

        } catch (Exception e) {
            log.warn("获取扩展信息失败, key: {}, class: {}", key, clazz, e);
            return null;
        }
    }

    /**
     * 获取扩展信息 Map
     */
    private static Map<String, Object> getExtraInfoMap(MqSendRecord entity) {
        if (entity == null || entity.getExtraInfo() == null) {
            return new HashMap<>();
        }

        try {
            Map<String, Object> extraInfo = entity.getExtraInfo();
            return extraInfo != null ? extraInfo : new HashMap<>();
        } catch (Exception e) {
            log.warn("解析扩展信息失败: {}", entity.getExtraInfo(), e);
            return new HashMap<>();
        }
    }




    private void logFailure(MqSendRecord entity, RabbitSendResult result) {

        Integer statusValue = entity.getSendStatus(); // 假设这是从数据库读取的值
        SendStatus status = SendStatus.fromValue(statusValue);

        log.error("消息发送失败记录: messageId={}, status={}, error={}, retryCount={}",
                entity.getMessageId(),
                status.getLabel(),
                entity.getErrorMsg(),
                entity.getRetryCount());

        if (log.isDebugEnabled()) {
            log.debug("失败详情: {}", result.getDetailInfo());
        }
    }

    private void sendAlertIfNeeded(MqSendRecord entity) {
        // 重要消息或连续失败发送告警
        if (isImportantMessage(entity) || isContinuousFailure(entity)) {
            sendAlert(entity);
        }
    }

    private boolean isImportantMessage(MqSendRecord entity) {
        return "ORDER_PAY".equals(entity.getMessageType()) ||
                "ORDER_CREATE".equals(entity.getMessageType());
    }

    private boolean isContinuousFailure(MqSendRecord entity) {
        return entity.getRetryCount() != null && entity.getRetryCount() >= 3;
    }

    private void sendAlert(MqSendRecord entity) {
        String alertMsg = String.format(
                "消息发送失败告警\n" +
                        "消息ID: %s\n" +
                        "租户: %s\n" +
                        "类型: %s\n" +
                        "交换机: %s\n" +
                        "路由键: %s\n" +
                        "错误: %s\n" +
                        "重试次数: %d\n" +
                        "时间: %s",
                entity.getMessageId(),
                entity.getTenantId(),
                entity.getMessageType(),
                entity.getExchange(),
                entity.getRoutingKey(),
                entity.getErrorMsg(),
                entity.getRetryCount(),
                LocalDateTime.now()
        );

        log.warn("发送失败告警: {}", alertMsg);
        // TODO: 集成告警系统
    }

    private Long countRetryableMessages() {
        List<MqSendRecord> retryable = getRetryableMessages();
        return (long)retryable.size();
    }


    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) return null;
        return LocalDateTime.ofInstant(date.toInstant(), java.time.ZoneId.systemDefault());
    }

    private String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }


}
