package com.aioveu.oms.aioveu05OrderPay.service;

import com.aioveu.oms.aioveu05OrderPay.model.entity.OmsOrderPay;
import com.aioveu.oms.aioveu05OrderPay.model.form.OmsOrderPayForm;
import com.aioveu.oms.aioveu05OrderPay.model.query.OmsOrderPayQuery;
import com.aioveu.oms.aioveu05OrderPay.model.vo.OmsOrderPayVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: OmsOrderPayService
 * @Description TODO  支付信息服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:01
 * @Version 1.0
 **/
public interface OmsOrderPayService extends IService<OmsOrderPay> {


    /**
     *支付信息分页列表
     *
     * @return {@link IPage<OmsOrderPayVO>} 支付信息分页列表
     */
    IPage<OmsOrderPayVO> getOmsOrderPayPage(OmsOrderPayQuery queryParams);

    /**
     * 获取支付信息表单数据
     *
     * @param id 支付信息ID
     * @return 支付信息表单数据
     */
    OmsOrderPayForm getOmsOrderPayFormData(Long id);

    /**
     * 新增支付信息
     *
     * @param formData 支付信息表单对象
     * @return 是否新增成功
     */
    boolean saveOmsOrderPay(OmsOrderPayForm formData);

    /**
     * 修改支付信息
     *
     * @param id   支付信息ID
     * @param formData 支付信息表单对象
     * @return 是否修改成功
     */
    boolean updateOmsOrderPay(Long id, OmsOrderPayForm formData);

    /**
     * 删除支付信息
     *
     * @param ids 支付信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOmsOrderPays(String ids);


}
