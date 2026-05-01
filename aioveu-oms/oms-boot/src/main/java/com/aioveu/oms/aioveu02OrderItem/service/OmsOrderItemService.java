package com.aioveu.oms.aioveu02OrderItem.service;

import com.aioveu.oms.aioveu02OrderItem.model.form.OmsOrderItemForm;
import com.aioveu.oms.aioveu02OrderItem.model.query.OmsOrderItemQuery;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderDetailVO;
import com.aioveu.oms.aioveu02OrderItem.model.vo.OmsOrderItemVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.oms.aioveu02OrderItem.model.entity.OmsOrderItem;

/**
 * @Description: TODO 订单商品信息服务类
 * @Author: 雒世松
 * @Date: 2025/6/5 18:14
 * @param
 * @return:
 **/

public interface OmsOrderItemService extends IService<OmsOrderItem> {

    /**
     *订单商品信息分页列表
     *
     * @return {@link IPage<OmsOrderItemVO>} 订单商品信息分页列表
     */
    IPage<OmsOrderItemVO> getOmsOrderItemPage(OmsOrderItemQuery queryParams);

    /**
     * 获取订单商品信息表单数据
     *
     * @param id 订单商品信息ID
     * @return 订单商品信息表单数据
     */
    OmsOrderItemForm getOmsOrderItemFormData(Long id);

    /**
     * 新增订单商品信息
     *
     * @param formData 订单商品信息表单对象
     * @return 是否新增成功
     */
    boolean saveOmsOrderItem(OmsOrderItemForm formData);

    /**
     * 修改订单商品信息
     *
     * @param id   订单商品信息ID
     * @param formData 订单商品信息表单对象
     * @return 是否修改成功
     */
    boolean updateOmsOrderItem(Long id, OmsOrderItemForm formData);

    /**
     * 删除订单商品信息
     *
     * @param ids 订单商品信息ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteOmsOrderItems(String ids);




    /**
     * 根据订单编号获取订单详情
     *
     * @param orderSn 订单编号
     * @param channel 渠道标识
     * @return 订单详情VO
     */
    OmsOrderDetailVO getOrderDetailBySn(String orderSn, Integer channel);


    /**
     * 根据订单ID获取订单详情
     *
     * @param orderId 订单id
     * @param channel 渠道标识
     * @return 订单详情VO
     */
    OmsOrderDetailVO getOrderDetailById(Long orderId, Integer channel);




}

