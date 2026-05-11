package com.aioveu.oms.aioveu09MqDeadLetter.service;


import com.aioveu.oms.aioveu09MqDeadLetter.model.entity.MqDeadLetter;
import com.aioveu.oms.aioveu09MqDeadLetter.model.form.MqDeadLetterForm;
import com.aioveu.oms.aioveu09MqDeadLetter.model.query.MqDeadLetterQuery;
import com.aioveu.oms.aioveu09MqDeadLetter.model.vo.MqDeadLetterVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: MqDeadLetterService
 * @Description TODO MQ死信队列服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:52
 * @Version 1.0
 **/

public interface MqDeadLetterService extends IService<MqDeadLetter> {

    /**
     *MQ死信队列分页列表
     *
     * @return {@link IPage<MqDeadLetterVo>} MQ死信队列分页列表
     */
    IPage<MqDeadLetterVo> getMqDeadLetterPage(MqDeadLetterQuery queryParams);

    /**
     * 获取MQ死信队列表单数据
     *
     * @param id MQ死信队列ID
     * @return MQ死信队列表单数据
     */
    MqDeadLetterForm getMqDeadLetterFormData(Long id);

    /**
     * 新增MQ死信队列
     *
     * @param formData MQ死信队列表单对象
     * @return 是否新增成功
     */
    boolean saveMqDeadLetter(MqDeadLetterForm formData);

    /**
     * 修改MQ死信队列
     *
     * @param id   MQ死信队列ID
     * @param formData MQ死信队列表单对象
     * @return 是否修改成功
     */
    boolean updateMqDeadLetter(Long id, MqDeadLetterForm formData);

    /**
     * 删除MQ死信队列
     *
     * @param ids MQ死信队列ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteMqDeadLetters(String ids);
}
