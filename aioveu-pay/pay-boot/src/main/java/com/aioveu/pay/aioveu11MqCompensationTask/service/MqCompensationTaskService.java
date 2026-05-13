package com.aioveu.pay.aioveu11MqCompensationTask.service;


import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu11MqCompensationTask.model.entity.MqCompensationTask;
import com.aioveu.pay.aioveu11MqCompensationTask.model.form.MqCompensationTaskForm;
import com.aioveu.pay.aioveu11MqCompensationTask.model.query.MqCompensationTaskQuery;
import com.aioveu.pay.aioveu11MqCompensationTask.model.vo.MqCompensationTaskVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.rocketmq.client.producer.SendResult;

/**
 * @ClassName: MqCompensationTaskService
 * @Description TODO MQ补偿任务服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:53
 * @Version 1.0
 **/

public interface MqCompensationTaskService extends IService<MqCompensationTask> {

    /**
     *MQ补偿任务分页列表
     *
     * @return {@link IPage<MqCompensationTaskVo>} MQ补偿任务分页列表
     */
    IPage<MqCompensationTaskVo> getMqCompensationTaskPage(MqCompensationTaskQuery queryParams);

    /**
     * 获取MQ补偿任务表单数据
     *
     * @param id MQ补偿任务ID
     * @return MQ补偿任务表单数据
     */
    MqCompensationTaskForm getMqCompensationTaskFormData(Long id);

    /**
     * 新增MQ补偿任务
     *
     * @param formData MQ补偿任务表单对象
     * @return 是否新增成功
     */
    boolean saveMqCompensationTask(MqCompensationTaskForm formData);

    /**
     * 修改MQ补偿任务
     *
     * @param id   MQ补偿任务ID
     * @param formData MQ补偿任务表单对象
     * @return 是否修改成功
     */
    boolean updateMqCompensationTask(Long id, MqCompensationTaskForm formData);

    /**
     * 删除MQ补偿任务
     *
     * @param ids MQ补偿任务ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteMqCompensationTasks(String ids);


    /**
     * 补偿任务 - 处理发送失败的消息
     */
    void compensateFailedMessages();


    // 更详细的处理
    void handleSendResult(SendResult sendResult, MqSendRecord record);
}
