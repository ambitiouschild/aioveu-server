package com.aioveu.oms.aioveu08MqConsumeRecord.service;


import com.aioveu.oms.aioveu08MqConsumeRecord.enums.ConsumeStatusEnum;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.entity.MqConsumeRecord;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.form.MqConsumeRecordForm;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.query.MqConsumeRecordQuery;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.vo.MqConsumeRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * @ClassName: MqConsumeRecordService
 * @Description TODO MQ消息消费记录服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:32
 * @Version 1.0
 **/

public interface MqConsumeRecordService extends IService<MqConsumeRecord> {

    /**
     *MQ消息消费记录分页列表
     *
     * @return {@link IPage<MqConsumeRecordVo>} MQ消息消费记录分页列表
     */
    IPage<MqConsumeRecordVo> getMqConsumeRecordPage(MqConsumeRecordQuery queryParams);

    /**
     * 获取MQ消息消费记录表单数据
     *
     * @param id MQ消息消费记录ID
     * @return MQ消息消费记录表单数据
     */
    MqConsumeRecordForm getMqConsumeRecordFormData(Long id);

    /**
     * 新增MQ消息消费记录
     *
     * @param formData MQ消息消费记录表单对象
     * @return 是否新增成功
     */
    boolean saveMqConsumeRecord(MqConsumeRecordForm formData);

    /**
     * 修改MQ消息消费记录
     *
     * @param id   MQ消息消费记录ID
     * @param formData MQ消息消费记录表单对象
     * @return 是否修改成功
     */
    boolean updateMqConsumeRecord(Long id, MqConsumeRecordForm formData);

    /**
     * 删除MQ消息消费记录
     *
     * @param ids MQ消息消费记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteMqConsumeRecords(String ids);



    /**
     * 保存消费记录
     */
    boolean saveConsumeRecord(String messageId, String bizId, String topic, String tag);


    /**
     * 获取消息消费状态
     */
    ConsumeStatusEnum getConsumeStatus(String messageId, String consumerGroup);


    /**
     * 记录消息消费
     */
    boolean recordConsumeStart(String messageId, String topic, String consumerGroup, String bizId);

    /**
     * 更新消费状态
     */
    boolean updateConsumeStatus(String messageId, String consumerGroup,
                                ConsumeStatusEnum status, String errorMsg);

    /**
     * 批量更新消费状态
     */
    int batchUpdateStatus(List<String> messageIds, String consumerGroup,
                          ConsumeStatusEnum status, String errorMsg);


    /**
     * 查询消费记录
     */
    ConsumeRecordVO getConsumeRecord(String messageId, String consumerGroup);

    /**
     * 获取需要重试的消费记录
     */
    List<MqConsumeRecordDTO> getNeedRetryRecords(String consumerGroup, Integer maxRetryCount);


    /**
     * 检查消息是否已消费
     */
    boolean isMessageConsumed(String messageId, String consumerGroup);

    /**
     * 获取消费者组的消费统计
     */
    Map<String, Object> getConsumerGroupStats(String consumerGroup, Integer days);

}
