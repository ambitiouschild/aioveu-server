package com.aioveu.refund.aioveu02RefundItem.service;

import com.aioveu.refund.aioveu02RefundItem.model.entity.RefundItem;
import com.aioveu.refund.aioveu02RefundItem.model.form.RefundItemForm;
import com.aioveu.refund.aioveu02RefundItem.model.query.RefundItemQuery;
import com.aioveu.refund.aioveu02RefundItem.model.vo.RefundItemVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: RefundItemService
 * @Description TODO  退款商品明细服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 17:02
 * @Version 1.0
 **/
public interface RefundItemService extends IService<RefundItem> {

    /**
     *退款商品明细分页列表
     *
     * @return {@link IPage<RefundItemVO>} 退款商品明细分页列表
     */
    IPage<RefundItemVO> getRefundItemPage(RefundItemQuery queryParams);

    /**
     * 获取退款商品明细表单数据
     *
     * @param id 退款商品明细ID
     * @return 退款商品明细表单数据
     */
    RefundItemForm getRefundItemFormData(Long id);

    /**
     * 新增退款商品明细
     *
     * @param formData 退款商品明细表单对象
     * @return 是否新增成功
     */
    boolean saveRefundItem(RefundItemForm formData);

    /**
     * 修改退款商品明细
     *
     * @param id   退款商品明细ID
     * @param formData 退款商品明细表单对象
     * @return 是否修改成功
     */
    boolean updateRefundItem(Long id, RefundItemForm formData);

    /**
     * 删除退款商品明细
     *
     * @param ids 退款商品明细ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteRefundItems(String ids);
}
