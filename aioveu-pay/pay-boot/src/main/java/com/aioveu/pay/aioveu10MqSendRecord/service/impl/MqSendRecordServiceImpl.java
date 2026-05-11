package com.aioveu.pay.aioveu10MqSendRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.pay.aioveu10MqSendRecord.converter.MqSendRecordConverter;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatusEnum;
import com.aioveu.pay.aioveu10MqSendRecord.mapper.MqSendRecordMapper;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import com.aioveu.pay.aioveu10MqSendRecord.utils.MessageIdGenerator;
import com.alibaba.fastjson.JSON;
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
        record.setSendStatus(SendStatusEnum.PENDING.getValue());
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
    public boolean updateSendStatus(String messageId, SendStatusEnum status, String errorMsg) {

        MqSendRecord record = new MqSendRecord();
        record.setMessageId(messageId);
        record.setSendStatus(status.getValue());
        record.setErrorMsg(errorMsg);
        record.setUpdateTime(LocalDateTime.now());
        if (status == SendStatusEnum.SUCCESS) {
            record.setConfirmTime(LocalDateTime.now());
        } else if (status == SendStatusEnum.FAILED) {
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



}
