package com.aioveu.pay.aioveu04PayConfigDummy.service;

import com.aioveu.pay.aioveu04PayConfigDummy.model.entity.PayConfigDummy;
import com.aioveu.pay.aioveu04PayConfigDummy.model.form.PayConfigDummyForm;
import com.aioveu.pay.aioveu04PayConfigDummy.model.query.PayConfigDummyQuery;
import com.aioveu.pay.aioveu04PayConfigDummy.model.vo.PayConfigDummyVo;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: PayConfigDummyService
 * @Description TODO 模拟支付配置服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:31
 * @Version 1.0
 **/

public interface PayConfigDummyService extends IService<PayConfigDummy> {

    /**
     *模拟支付配置分页列表
     *
     * @return {@link IPage<PayConfigDummyVo>} 模拟支付配置分页列表
     */
    IPage<PayConfigDummyVo> getPayConfigDummyPage(PayConfigDummyQuery queryParams);

    /**
     * 获取模拟支付配置表单数据
     *
     * @param id 模拟支付配置ID
     * @return 模拟支付配置表单数据
     */
    PayConfigDummyForm getPayConfigDummyFormData(Long id);

    /**
     * 新增模拟支付配置
     *
     * @param formData 模拟支付配置表单对象
     * @return 是否新增成功
     */
    boolean savePayConfigDummy(PayConfigDummyForm formData);

    /**
     * 修改模拟支付配置
     *
     * @param id   模拟支付配置ID
     * @param formData 模拟支付配置表单对象
     * @return 是否修改成功
     */
    boolean updatePayConfigDummy(Long id, PayConfigDummyForm formData);

    /**
     * 删除模拟支付配置
     *
     * @param ids 模拟支付配置ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deletePayConfigDummys(String ids);
}
