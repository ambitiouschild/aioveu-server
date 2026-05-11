package com.aioveu.pay.aioveu10MqSendRecord.service;


import com.aioveu.pay.aioveu01PayOrder.model.entity.PayOrder;
import com.aioveu.pay.aioveu10MqSendRecord.enums.SendStatusEnum;
import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.aioveu.pay.aioveu10MqSendRecord.utils.MessageIdGenerator;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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

}
