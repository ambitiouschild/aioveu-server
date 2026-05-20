package com.aioveu.pay.aioveu11MqCompensationTask.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.rabbitmq.enums.SendStatus;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu11MqCompensationTask.converter.MqCompensationTaskConverter;
import com.aioveu.pay.aioveu11MqCompensationTask.mapper.MqCompensationTaskMapper;
import com.aioveu.pay.aioveu11MqCompensationTask.model.entity.MqCompensationTask;
import com.aioveu.pay.aioveu11MqCompensationTask.model.form.MqCompensationTaskForm;
import com.aioveu.pay.aioveu11MqCompensationTask.model.query.MqCompensationTaskQuery;
import com.aioveu.pay.aioveu11MqCompensationTask.model.vo.MqCompensationTaskVo;
import com.aioveu.pay.aioveu11MqCompensationTask.service.MqCompensationTaskService;
import com.aioveu.common.rabbitmq.producer.monitor.ProducerMetricsCollector;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
// 正确的导入
/**
 * @ClassName: MqCompensationTaskServiceImpl
 * @Description TODO MQ补偿任务服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:54
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class MqCompensationTaskServiceImpl extends ServiceImpl<MqCompensationTaskMapper, MqCompensationTask> implements MqCompensationTaskService {

//    private final RocketMQTemplate rocketMQTemplate;
    private final RabbitTemplate rabbitTemplate;

    private final MqCompensationTaskConverter mqCompensationTaskConverter;

    private final MqSendRecordService mqSendRecordService;
    private final ProducerMetricsCollector metricsCollector;
    /**
     * 获取MQ补偿任务分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqCompensationTaskVo>} MQ补偿任务分页列表
     */
    @Override
    public IPage<MqCompensationTaskVo> getMqCompensationTaskPage(MqCompensationTaskQuery queryParams) {
        Page<MqCompensationTaskVo> page = this.baseMapper.getMqCompensationTaskPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ补偿任务表单数据
     *
     * @param id MQ补偿任务ID
     * @return MQ补偿任务表单数据
     */
    @Override
    public MqCompensationTaskForm getMqCompensationTaskFormData(Long id) {
        MqCompensationTask entity = this.getById(id);
        return mqCompensationTaskConverter.toForm(entity);
    }

    /**
     * 新增MQ补偿任务
     *
     * @param formData MQ补偿任务表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqCompensationTask(MqCompensationTaskForm formData) {
        MqCompensationTask entity = mqCompensationTaskConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ补偿任务
     *
     * @param id   MQ补偿任务ID
     * @param formData MQ补偿任务表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqCompensationTask(Long id,MqCompensationTaskForm formData) {
        MqCompensationTask entity = mqCompensationTaskConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ补偿任务
     *
     * @param ids MQ补偿任务ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqCompensationTasks(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ补偿任务数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


    /**
     * 补偿任务 - 处理发送失败的消息
     * //有 @Scheduled(fixedDelay = 30000)注解，Spring 会自动调用
     */
    /**
     * 补偿任务 - 处理发送失败的消息
     */
    @Override
    @Scheduled(fixedDelay = 30000)
    public void compensateFailedMessages() {
        long startTime = System.currentTimeMillis();
        int successCount = 0;
        int failCount = 0;

        try {
            // 查询发送失败的记录
            List<MqSendRecord> failedRecords = mqSendRecordService.selectFailedMessages(100);

            for (MqSendRecord record : failedRecords) {
                if (record.getNextRetryTime().isAfter(LocalDateTime.now())) {
                    continue;  // 未到重试时间
                }

                if (record.getRetryCount() >= 5) {
                    log.error("消息重试超过5次，进入死信: messageId={}", record.getMessageId());
                    mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatus.DEAD, "重试超过5次");
                    continue;
                }

                try {
                    // 重新发送消息
                    Message<byte[]> message = MessageBuilder
                            .withPayload(record.getMessageBody().getBytes(StandardCharsets.UTF_8))
                            .setHeader("bizId", record.getBizId())  // RabbitMQ使用自定义header
                            .build();

                    // ✅ 最小改动：将RocketMQ的syncSendOrderly改为RabbitMQ的同步发送
                    boolean sendSuccess = sendMessageByRabbitMQ(
                            record.getTopic() + ":" + record.getTag(),
                            message,
                            record.getShardingKey(),
                            3000
                    );

                    if (sendSuccess) {
                        mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatus.SUCCESS, null);
                        log.info("补偿发送成功: messageId={}", record.getMessageId());
                        successCount++;
                    } else {
                        mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatus.FAILED,
                                "RabbitMQ发送失败");
                        log.error("补偿发送失败: messageId={}", record.getMessageId());
                        failCount++;
                    }

                } catch (Exception e) {
                    log.error("补偿发送异常: messageId={}", record.getMessageId(), e);
                    mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatus.FAILED, e.getMessage());
                    failCount++;
                }
            }

        } catch (Exception e) {
            log.error("重试失败消息异常", e);
        } finally {
            long totalCost = System.currentTimeMillis() - startTime;
            metricsCollector.recordSendResult(failCount == 0, totalCost);
            log.info("补偿任务完成: 成功={}, 失败={}, 耗时={}ms",
                    successCount, failCount, totalCost);
        }
    }

    /**
     * RabbitMQ同步发送消息
     */
    private boolean sendMessageByRabbitMQ(String destination, Object message, String shardingKey, long timeoutMs) {
        try {
            String exchange = "compensation.exchange";
            String routingKey = buildRoutingKeyFromDestination(destination, shardingKey);

            String correlationId = UUID.randomUUID().toString();
            org.springframework.amqp.rabbit.connection.CorrelationData correlationData =
                    new org.springframework.amqp.rabbit.connection.CorrelationData(correlationId);

            // 发送消息
            rabbitTemplate.convertAndSend(exchange, routingKey, message, correlationData);

            // 等待确认
            try {
                // ✅ 正确的确认获取
                CorrelationData.Confirm confirm = correlationData.getFuture().get(timeoutMs, TimeUnit.MILLISECONDS);

                if (confirm == null) {
                    log.warn("未收到确认: correlationId={}", correlationId);
                    return false;
                }

                // ✅ 正确的确认检查
                boolean isAck = confirm.isAck();

                if (isAck) {
                    log.debug("消息确认成功: correlationId={}", correlationId);
                    return true;
                } else {
                    log.warn("消息被拒绝: correlationId={}", correlationId);
                    return false;
                }

            } catch (Exception e) {
                log.error("等待确认异常: correlationId={}", correlationId, e);
                return false;
            }

        } catch (Exception e) {
            log.error("RabbitMQ发送失败: destination={}", destination, e);
            return false;
        }
    }

    /**
     * 从destination构建routingKey
     */
    private String buildRoutingKeyFromDestination(String destination, String shardingKey) {
        // 将 "topic:tag" 格式转换为 "topic.tag"
        String baseKey = destination.replace(":", ".");

        if (StrUtil.isNotBlank(shardingKey)) {
            return baseKey + "." + shardingKey;
        }
        return baseKey;
    }

    // 移除或修改handleSendResult方法，因为它接收RocketMQ的SendResult
    // 可以重载为接收boolean参数

    @Override
    public void handleSendResult(org.apache.rocketmq.client.producer.SendResult sendResult, MqSendRecord record) {
        // 这个方法可能需要重构，因为现在使用RabbitMQ
        // 临时实现：调用原始方法但可能不会执行
        log.warn("handleSendResult方法仍然使用RocketMQ SendResult，可能需要重构");
    }

    /**
     * 处理RabbitMQ发送结果
     */
    public void handleRabbitSendResult(boolean success, String correlationId, MqSendRecord record) {
        if (success) {
            mqSendRecordService.updateSendStatus(
                    record.getMessageId(),
                    SendStatus.SUCCESS,
                    null
            );
            log.info("RabbitMQ发送成功: messageId={}, correlationId={}",
                    record.getMessageId(), correlationId);
        } else {
            mqSendRecordService.updateSendStatus(
                    record.getMessageId(),
                    SendStatus.FAILED,
                    "RabbitMQ发送失败"
            );
            log.error("RabbitMQ发送失败: messageId={}, correlationId={}",
                    record.getMessageId(), correlationId);
        }
    }
}


