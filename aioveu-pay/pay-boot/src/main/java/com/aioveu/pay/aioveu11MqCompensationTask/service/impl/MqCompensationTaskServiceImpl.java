package com.aioveu.pay.aioveu11MqCompensationTask.service.impl;


import cn.binarywang.wx.miniapp.bean.cloud.WxCloudSendSmsV2Result;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatusEnum;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu11MqCompensationTask.converter.MqCompensationTaskConverter;
import com.aioveu.pay.aioveu11MqCompensationTask.mapper.MqCompensationTaskMapper;
import com.aioveu.pay.aioveu11MqCompensationTask.model.entity.MqCompensationTask;
import com.aioveu.pay.aioveu11MqCompensationTask.model.form.MqCompensationTaskForm;
import com.aioveu.pay.aioveu11MqCompensationTask.model.query.MqCompensationTaskQuery;
import com.aioveu.pay.aioveu11MqCompensationTask.model.vo.MqCompensationTaskVo;
import com.aioveu.pay.aioveu11MqCompensationTask.service.MqCompensationTaskService;
import com.aioveu.pay.aioveu12MqProducerPayment.MQMonitorProducer.ProducerMetricsCollector;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.SendStatus;
import org.apache.rocketmq.common.message.MessageConst;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

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

    private final RocketMQTemplate rocketMQTemplate;

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
    @Override
    @Scheduled(fixedDelay = 30000)  // 30秒执行一次
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
                    mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatusEnum.DEAD, "重试超过5次");
                    continue;
                }

                try {
                    // 重新发送消息
                    Message<byte[]> message = MessageBuilder
                            .withPayload(record.getMessageBody().getBytes(StandardCharsets.UTF_8))
                            .setHeader(MessageConst.PROPERTY_KEYS, record.getBizId())
                            .build();

                    SendResult sendResult = rocketMQTemplate.syncSendOrderly(
                            record.getTopic() + ":" + record.getTag(),
                            message,
                            record.getShardingKey(),
                            3000
                    );

                    // ✅ 修复：使用正确的 RocketMQ SendStatus
                    if (sendResult.getSendStatus() == SendStatus.SEND_OK) {
                        mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatusEnum.SUCCESS, null);
                        log.info("补偿发送成功: messageId={}, msgId={}",
                                record.getMessageId(), sendResult.getMsgId());
                        successCount++;
                    } else {
                        mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatusEnum.FAILED,
                                "发送失败, status=" + sendResult.getSendStatus());
                        log.error("补偿发送失败: messageId={}, status={}",
                                record.getMessageId(), sendResult.getSendStatus());
                        failCount++;
                    }
                    log.info("补偿任务完成: 成功={}, 失败={}", successCount, failCount);
                } catch (Exception e) {
                    log.error("补偿发送异常: messageId={}", record.getMessageId(), e);
                    mqSendRecordService.updateSendStatus(record.getMessageId(), SendStatusEnum.FAILED, e.getMessage());
                }
            }

        } catch (Exception e) {
            log.error("重试失败消息异常", e);
        }finally {
            long totalCost = System.currentTimeMillis() - startTime;
            // ✅ 记录补偿任务整体结果
            metricsCollector.recordSendResult(failCount == 0, totalCost);
            log.info("补偿任务完成: 成功={}, 失败={}, 耗时={}ms",
                    successCount, failCount, totalCost);

        }
    }



    // 更详细的处理
    @Override
    public void handleSendResult(SendResult sendResult, MqSendRecord record) {
        SendStatus sendStatus = sendResult.getSendStatus();

        switch (sendStatus) {
            case SEND_OK:
                // 发送成功
                mqSendRecordService.updateSendStatus(
                        record.getMessageId(),
                        SendStatusEnum.SUCCESS,
                        null
                );
                log.info("RocketMQ发送成功: messageId={}, msgId={}, queueOffset={}",
                        record.getMessageId(),
                        sendResult.getMsgId(),
                        sendResult.getQueueOffset()
                );
                break;

            case FLUSH_DISK_TIMEOUT:
            case FLUSH_SLAVE_TIMEOUT:
            case SLAVE_NOT_AVAILABLE:
                // 可重试的失败
                mqSendRecordService.updateSendStatus(
                        record.getMessageId(),
                        SendStatusEnum.FAILED,
                        "发送失败: " + sendStatus
                );
                log.warn("RocketMQ发送失败(可重试): messageId={}, status={}",
                        record.getMessageId(), sendStatus);
                break;

            default:
                // 其他失败
                mqSendRecordService.updateSendStatus(
                        record.getMessageId(),
                        SendStatusEnum.FAILED,
                        "发送失败: " + sendStatus
                );
                log.error("RocketMQ发送失败: messageId={}, status={}",
                        record.getMessageId(), sendStatus);
        }
    }





}
