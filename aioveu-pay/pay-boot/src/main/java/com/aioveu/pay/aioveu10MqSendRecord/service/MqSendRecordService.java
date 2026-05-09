package com.aioveu.pay.aioveu10MqSendRecord.service;


import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.form.MqSendRecordForm;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
