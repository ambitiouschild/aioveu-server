package com.aioveu.pay.aioveu10MqSendRecord.service;


import com.aioveu.common.enums.pay.PaymentSceneEnum;
import com.aioveu.common.rabbitmq.enums.SendStatusEnum;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.SendRecordStats;
import com.aioveu.common.rabbitmq.producer.util.MessageIdGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: MqSendRecordService
 * @Description TODO MQ消息发送记录服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:47
 * @Version 1.0
 **/

public interface MqSendRecordService extends IService<MqSendRecord> {

    /**
     *MQ消息发送记录分页列表
     *
     * @return {@link IPage<MqSendRecordVo>} MQ消息发送记录分页列表
     */
    IPage<MqSendRecordVo> getMqSendRecordPage(MqSendRecordQuery queryParams);

    /**
     * 获取MQ消息发送记录表单数据
     *
     * @param id MQ消息发送记录ID
     * @return MQ消息发送记录表单数据
     */
    MqSendRecordForm getMqSendRecordFormData(Long id);

    /**
     * 新增MQ消息发送记录
     *
     * @param formData MQ消息发送记录表单对象
     * @return 是否新增成功
     */
    boolean saveMqSendRecord(MqSendRecordForm formData);

    /**
     * 修改MQ消息发送记录
     *
     * @param id   MQ消息发送记录ID
     * @param formData MQ消息发送记录表单对象
     * @return 是否修改成功
     */
    boolean updateMqSendRecord(Long id, MqSendRecordForm formData);

    /**
     * 删除MQ消息发送记录
     *
     * @param ids MQ消息发送记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteMqSendRecords(String ids);




    /* 保存MQ消息发送记录
     *
     * @param  topic ,  Topic
     * @param  tag ,    tag
     * @param  bizId ,业务ID(支付单号)
     * @param  message ,MQ消息发送记录表单对象
     * @return 是否新增成功
     */
    String saveMqSendRecord(String topic, String tag, String bizId, Object message);


    /**
     * 更新MQ消息发送记录
     *
     * @param messageId   MQ消息发送记录ID
     * @param status MQ消息发送记录表单对象
     * @return 是否修改成功
     */
    boolean updateSendStatus(String messageId, SendStatusEnum status, String errorMsg);



    /**
     * 调试解析消息ID
     * 返回解析结果，便于调用方使用
     */
    MessageIdGenerator.MessageIdInfo debugMessage(String messageId);



    /**
     * 调试并验证消息ID
     */
    boolean validateAndDebugMessage(String messageId);


    /**
     * 批量调试消息ID
     */
    Map<String, MessageIdGenerator.MessageIdInfo> batchDebugMessages(List<String> messageIds);

    /**
     * 查询发送失败的记录
     * @param maxCount 最大查询数量
     * @return 失败记录列表
     */
    List<MqSendRecord> selectFailedMessages(int maxCount);


    /**
     * 查询需要重试的记录
     */

    List<MqSendRecord> selectNeedRetryRecords(int maxRetryCount, Date beforeTime, int maxCount);


    /**
     * 查询未确认的记录
     */
    List<MqSendRecord> selectUnconfirmedMessages(int timeoutMinutes, int maxCount);


    /**
     * 批量更新重试信息
     */
    int batchUpdateRetryInfo(List<Long> ids, int retryCount, LocalDateTime nextRetryTime);


    /**
     * 统计各类状态的消息数量
     */
    SendRecordStats getSendRecordStats(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 获取详细的发送记录统计
     */
    SendRecordStats getDetailedSendRecordStats(LocalDateTime startTime, LocalDateTime endTime);


    /**
     * ✅ 判断支付成功事件是否已发送（幂等判断）
     */
    boolean bizEventAlreadySent(String paymentNo);

    //插入即幂等（最安全）
    void markBizEventSent(String paymentNo, PaymentSceneEnum scene);


    /**
     * 插入未发送记录（事务内调用）
     */
    void insertUnSent(String paymentNo, PaymentSceneEnum scene);


    /**
     * 标记为已发送
     */
    void markSent(String paymentNo);

    /**
     * 保存重试记录（发送失败时调用）
     */
    void saveRetryRecord(String bizId, Throwable t);

    /**
     * 增加重试次数
     */
    void increaseRetryCount(Long id);
}
