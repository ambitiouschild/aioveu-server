package com.aioveu.oms.aioveu10MqConsumeIdempotent.service;


import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.entity.MqConsumeIdempotent;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.form.MqConsumeIdempotentForm;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.query.MqConsumeIdempotentQuery;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.vo.MqConsumeIdempotentVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: MqConsumeIdempotentService
 * @Description TODO MQ消费幂等性服务类
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:20
 * @Version 1.0
 **/

public interface MqConsumeIdempotentService extends IService<MqConsumeIdempotent> {

    /**
     *MQ消费幂等性分页列表
     *
     * @return {@link IPage<MqConsumeIdempotentVo>} MQ消费幂等性分页列表
     */
    IPage<MqConsumeIdempotentVo> getMqConsumeIdempotentPage(MqConsumeIdempotentQuery queryParams);

    /**
     * 获取MQ消费幂等性表单数据
     *
     * @param id MQ消费幂等性ID
     * @return MQ消费幂等性表单数据
     */
    MqConsumeIdempotentForm getMqConsumeIdempotentFormData(Long id);

    /**
     * 新增MQ消费幂等性
     *
     * @param formData MQ消费幂等性表单对象
     * @return 是否新增成功
     */
    boolean saveMqConsumeIdempotent(MqConsumeIdempotentForm formData);

    /**
     * 修改MQ消费幂等性
     *
     * @param id   MQ消费幂等性ID
     * @param formData MQ消费幂等性表单对象
     * @return 是否修改成功
     */
    boolean updateMqConsumeIdempotent(Long id, MqConsumeIdempotentForm formData);

    /**
     * 删除MQ消费幂等性
     *
     * @param ids MQ消费幂等性ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteMqConsumeIdempotents(String ids);
}
