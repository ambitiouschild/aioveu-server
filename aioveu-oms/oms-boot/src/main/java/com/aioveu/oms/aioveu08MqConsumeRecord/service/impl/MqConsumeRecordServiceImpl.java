package com.aioveu.oms.aioveu08MqConsumeRecord.service.impl;


import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.exception.BusinessException;
import com.aioveu.oms.aioveu08MqConsumeRecord.converter.MqConsumeRecordConverter;
import com.aioveu.oms.aioveu08MqConsumeRecord.enums.ConsumeStatusEnum;
import com.aioveu.oms.aioveu08MqConsumeRecord.mapper.MqConsumeRecordMapper;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.entity.MqConsumeRecord;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.form.MqConsumeRecordForm;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.query.MqConsumeRecordQuery;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.vo.MqConsumeRecordVo;
import com.aioveu.oms.aioveu08MqConsumeRecord.service.MqConsumeRecordService;
import com.alibaba.nacos.common.utils.StringUtils;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @ClassName: MqConsumeRecordServiceImpl
 * @Description TODO MQ消息消费记录服务实现类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:35
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class MqConsumeRecordServiceImpl extends ServiceImpl<MqConsumeRecordMapper, MqConsumeRecord> implements MqConsumeRecordService {

    private final MqConsumeRecordConverter mqConsumeRecordConverter;

    /**
     * 获取MQ消息消费记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<MqConsumeRecordVo>} MQ消息消费记录分页列表
     */
    @Override
    public IPage<MqConsumeRecordVo> getMqConsumeRecordPage(MqConsumeRecordQuery queryParams) {
        Page<MqConsumeRecordVo> page = this.baseMapper.getMqConsumeRecordPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return page;
    }

    /**
     * 获取MQ消息消费记录表单数据
     *
     * @param id MQ消息消费记录ID
     * @return MQ消息消费记录表单数据
     */
    @Override
    public MqConsumeRecordForm getMqConsumeRecordFormData(Long id) {
        MqConsumeRecord entity = this.getById(id);
        return mqConsumeRecordConverter.toForm(entity);
    }

    /**
     * 新增MQ消息消费记录
     *
     * @param formData MQ消息消费记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveMqConsumeRecord(MqConsumeRecordForm formData) {
        MqConsumeRecord entity = mqConsumeRecordConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新MQ消息消费记录
     *
     * @param id   MQ消息消费记录ID
     * @param formData MQ消息消费记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateMqConsumeRecord(Long id,MqConsumeRecordForm formData) {
        MqConsumeRecord entity = mqConsumeRecordConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除MQ消息消费记录
     *
     * @param ids MQ消息消费记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteMqConsumeRecords(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的MQ消息消费记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


    /**
     * 保存消费记录
     */
    @Override
    public boolean saveConsumeRecord(String messageId, String bizId, String topic, String tag) {

    MqConsumeRecord record = new MqConsumeRecord();
        record.setMessageId(messageId);
        record.setTopic(topic);
        record.setTag(tag);
        record.setConsumerGroup("order-service-group");
        record.setBizId(bizId);
        record.setConsumeStatus(ConsumeStatusEnum.PROCESSING.getValue());  // 消费中
        record.setConsumeTime(LocalDateTime.now());
        record.setCreateTime(LocalDateTime.now());

        return this.save(record);
    }


    /**
     * 获取消息消费状态
     * 示例调用：mqConsumeRecordService.getConsumeStatus(messageId, "order_service_group")
     */
    @Override
    public ConsumeStatusEnum getConsumeStatus(String messageId, String consumerGroup) {
        if (!StringUtils.hasText(messageId) || !StringUtils.hasText(consumerGroup)) {
            log.warn("参数为空: messageId={}, consumerGroup={}", messageId, consumerGroup);
            return null;
        }

        try {
            // 查询消费记录
            MqConsumeRecord record = this.baseMapper.selectByMessageAndGroup(messageId, consumerGroup);

            if (record == null) {
                log.debug("消费记录不存在: messageId={}, consumerGroup={}", messageId, consumerGroup);
                return null;
            }

            // 转换为枚举
            ConsumeStatusEnum status = ConsumeStatusEnum.fromValue(record.getConsumeStatus());

            if (status == null) {
                log.warn("未知的消费状态: messageId={}, status={}", messageId, record.getConsumeStatus());
            }

            return status;

        } catch (Exception e) {
            log.error("获取消费状态异常: messageId={}, consumerGroup={}",
                    messageId, consumerGroup, e);
            throw new BusinessException("获取消费状态失败");
        }
    }

    /**
     * 记录消息消费开始
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recordConsumeStart(String messageId, String topic,
                                      String consumerGroup, String bizId) {
        if (!StringUtils.hasText(messageId) || !StringUtils.hasText(consumerGroup)) {
            log.error("记录消费开始失败: 参数为空");
            return false;
        }

        try {
            // 检查是否已存在记录
            MqConsumeRecord existingRecord = this.baseMapper
                    .selectByMessageAndGroup(messageId, consumerGroup);

            if (existingRecord != null) {
                // 已存在记录，更新状态为处理中
                existingRecord.setConsumeStatus(ConsumeStatusEnum.PROCESSING.getValue());
                existingRecord.setConsumeTime(LocalDateTime.now());
                existingRecord.setUpdateTime(LocalDateTime.now());
                existingRecord.setRetryCount(existingRecord.getRetryCount() + 1);

                int rows = this.baseMapper.updateById(existingRecord);
                return rows > 0;
            }

            // 创建新记录
            MqConsumeRecord record = new MqConsumeRecord();
            record.setMessageId(messageId);
            record.setTopic(topic);
            record.setConsumerGroup(consumerGroup);
            record.setBizId(bizId);
            record.setConsumeStatus(ConsumeStatusEnum.PROCESSING.getValue());
            record.setRetryCount(1);
            record.setMaxRetry(3);
            record.setConsumeTime(LocalDateTime.now());
            record.setCreateTime(LocalDateTime.now());
            record.setUpdateTime(LocalDateTime.now());

            int rows = this.baseMapper.insert(record);

            if (rows > 0) {
                log.info("记录消费开始: messageId={}, consumerGroup={}, bizId={}",
                        messageId, consumerGroup, bizId);
                return true;
            }

            return false;

        } catch (Exception e) {
            log.error("记录消费开始异常: messageId={}, consumerGroup={}",
                    messageId, consumerGroup, e);
            return false;
        }
    }

    /**
     * 更新消费状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConsumeStatus(String messageId, String consumerGroup,
                                       ConsumeStatusEnum status, String errorMsg) {
        if (!StringUtils.hasText(messageId) || !StringUtils.hasText(consumerGroup) || status == null) {
            log.error("更新消费状态失败: 参数为空");
            return false;
        }

        try {
            MqConsumeRecord record = this.baseMapper
                    .selectByMessageAndGroup(messageId, consumerGroup);

            if (record == null) {
                log.error("消费记录不存在: messageId={}, consumerGroup={}", messageId, consumerGroup);
                return false;
            }

            // 记录状态变更日志
            ConsumeStatusEnum oldStatus = ConsumeStatusEnum.fromValue(record.getConsumeStatus());
            log.info("消费状态变更: {} -> {}, messageId={}",
                    oldStatus, status, messageId);

            // 更新记录
            record.setConsumeStatus(status.getValue());
            record.setErrorMsg(errorMsg);
            record.setUpdateTime(LocalDateTime.now());

            if (status.isFinalStatus()) {
                record.setFinishTime(LocalDateTime.now());
            }

            // 设置下次重试时间（如果需要重试）
            if (status.isRetryable()) {
                LocalDateTime nextRetryTime = calculateNextRetryTime(record.getRetryCount());
                record.setNextRetryTime(nextRetryTime);
            }

            int rows = this.baseMapper.updateById(record);
            return rows > 0;

        } catch (Exception e) {
            log.error("更新消费状态异常: messageId={}, consumerGroup={}, status={}",
                    messageId, consumerGroup, status, e);
            return false;
        }
    }


    /**
     * 批量更新消费状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateStatus(List<String> messageIds, String consumerGroup,
                                 ConsumeStatusEnum status, String errorMsg) {
        if (messageIds == null || messageIds.isEmpty() || status == null) {
            return 0;
        }

        try {
            int count = 0;
            LocalDateTime updateTime = LocalDateTime.now();

            for (String messageId : messageIds) {
                MqConsumeRecord record = this.baseMapper
                        .selectByMessageAndGroup(messageId, consumerGroup);

                if (record != null) {
                    record.setConsumeStatus(status.getValue());
                    record.setErrorMsg(errorMsg);
                    record.setUpdateTime(updateTime);

                    if (status.isFinalStatus()) {
                        record.setFinishTime(updateTime);
                    }

                    int rows = this.baseMapper.updateById(record);
                    if (rows > 0) {
                        count++;
                    }
                }
            }

            log.info("批量更新消费状态: 共{}条，成功{}条", messageIds.size(), count);
            return count;

        } catch (Exception e) {
            log.error("批量更新消费状态异常", e);
            throw new BusinessException("批量更新消费状态失败");
        }
    }

    /**
     * 查询消费记录
     */
    @Override
    public ConsumeRecordVO getConsumeRecord(String messageId, String consumerGroup) {
        MqConsumeRecord record = this.baseMapper.selectByMessageAndGroup(messageId, consumerGroup);
        return mqConsumeRecordConverter.toVO(record);
    }

    /**
     * 获取需要重试的消费记录
     */
    @Override
    public List<MqConsumeRecordDTO> getNeedRetryRecords(String consumerGroup, Integer maxRetryCount) {
        LocalDateTime now = LocalDateTime.now();

        List<MqConsumeRecord> records = this.baseMapper.selectNeedRetry(
                consumerGroup,
                maxRetryCount != null ? maxRetryCount : 3,
                now
        );

        return records.stream()
                .map(mqConsumeRecordConverter::toDTO)
                .collect(Collectors.toList());
    }

    /**
     * 检查消息是否已消费
     */
    @Override
    public boolean isMessageConsumed(String messageId, String consumerGroup) {
        ConsumeStatusEnum status = getConsumeStatus(messageId, consumerGroup);

        if (status == null) {
            return false;  // 记录不存在，未消费
        }

        return status.isFinalStatus() && status != ConsumeStatusEnum.FAILED;
    }

    /**
     * 获取消费者组的消费统计
     */
    @Override
    public Map<String, Object> getConsumerGroupStats(String consumerGroup, Integer days) {
        LocalDateTime endTime = LocalDateTime.now();
        LocalDateTime startTime = endTime.minusDays(days != null ? days : 7);

        Map<String, Object> stats = this.baseMapper.selectConsumerGroupStats(
                consumerGroup, startTime, endTime
        );

        // 补充计算成功率
        Long successCount = (Long) stats.getOrDefault("successCount", 0L);
        Long totalCount = (Long) stats.getOrDefault("totalCount", 0L);

        if (totalCount > 0) {
            double successRate = (successCount * 100.0) / totalCount;
            stats.put("successRate", String.format("%.2f%%", successRate));
        } else {
            stats.put("successRate", "0.00%");
        }

        return stats;
    }

    /**
     * 计算下次重试时间
     */
    private LocalDateTime calculateNextRetryTime(int retryCount) {
        // 指数退避算法：30秒 * 2^retryCount
        long delaySeconds = (long) (30 * Math.pow(2, retryCount));
        delaySeconds = Math.min(delaySeconds, 3600);  // 最多1小时

        return LocalDateTime.now().plusSeconds(delaySeconds);
    }


}
