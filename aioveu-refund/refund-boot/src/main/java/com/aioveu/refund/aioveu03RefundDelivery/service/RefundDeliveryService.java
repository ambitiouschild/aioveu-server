package com.aioveu.refund.aioveu03RefundDelivery.service;

import com.aioveu.refund.aioveu03RefundDelivery.model.entity.RefundDelivery;
import com.aioveu.refund.aioveu03RefundDelivery.model.form.RefundDeliveryForm;
import com.aioveu.refund.aioveu03RefundDelivery.model.query.RefundDeliveryQuery;
import com.aioveu.refund.aioveu03RefundDelivery.model.vo.RefundDeliveryVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
  *@ClassName: RefundDeliveryService
  *@Description TODO 退款物流信息（用于退货）服务类
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2026/1/31 18:06
  *@Version 1.0
  **/
public interface RefundDeliveryService extends IService<RefundDelivery> {

    /**
     *退款物流信息（用于退货）分页列表
     *
     * @return {@link IPage<RefundDeliveryVO>} 退款物流信息（用于退货）分页列表
     */
    IPage<RefundDeliveryVO> getRefundDeliveryPage(RefundDeliveryQuery queryParams);

    /**
     * 获取退款物流信息（用于退货）表单数据
     *
     * @param id 退款物流信息（用于退货）ID
     * @return 退款物流信息（用于退货）表单数据
     */
    RefundDeliveryForm getRefundDeliveryFormData(Long id);

    /**
     * 新增退款物流信息（用于退货）
     *
     * @param formData 退款物流信息（用于退货）表单对象
     * @return 是否新增成功
     */
    boolean saveRefundDelivery(RefundDeliveryForm formData);

    /**
     * 修改退款物流信息（用于退货）
     *
     * @param id   退款物流信息（用于退货）ID
     * @param formData 退款物流信息（用于退货）表单对象
     * @return 是否修改成功
     */
    boolean updateRefundDelivery(Long id, RefundDeliveryForm formData);

    /**
     * 删除退款物流信息（用于退货）
     *
     * @param ids 退款物流信息（用于退货）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRefundDeliverys(String ids);
}
