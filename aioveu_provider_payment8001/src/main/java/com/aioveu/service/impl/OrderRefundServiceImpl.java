package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.SportConstant;
import com.aioveu.dao.OrderRefundDao;
import com.aioveu.entity.Category;
import com.aioveu.entity.Order;
import com.aioveu.entity.OrderRefund;
import com.aioveu.enums.ExerciseCategory;
import com.aioveu.enums.OrderStatus;
import com.aioveu.feign.vo.WxPayOrderRefundNotifyResultVO;
import com.aioveu.service.CategoryService;
import com.aioveu.service.OrderDetailService;
import com.aioveu.service.OrderRefundService;
import com.aioveu.service.OrderService;
import com.aioveu.vo.BasicRefundOrderVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class OrderRefundServiceImpl extends ServiceImpl<OrderRefundDao, OrderRefund> implements OrderRefundService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Override
    public boolean refundSuccess(String refundId, Date refundFinishTime, String account) {
        OrderRefund orderRefund = new OrderRefund();
        orderRefund.setId(refundId);
        orderRefund.setRefundFinish(refundFinishTime);
        orderRefund.setStatus(OrderStatus.REFUND.getCode());
        orderRefund.setErrorMsg("退款成功:" + account);

        OrderRefund or = getById(refundId);
        if (or != null && StringUtils.isNotEmpty(or.getOrderId())) {
            Order order = orderService.getById(or.getOrderId());
            Order updateOrder = new Order();
            updateOrder.setId(order.getId());
            Category category = categoryService.getById(order.getCategoryId());
            if (category.getCode().equals(SportConstant.FIELD_CATEGORY_CODE)) {
                updateOrder.setStatus(OrderStatus.REFUND.getCode());
            } else {
                //如果是拼单产品，并且是结算退钱actualAmount > refundAmount（非取消订单退款），订单状态改为使用中
                if (ExerciseCategory.JOIN_EXERCISE.getCode().equals(category.getCode())
                        && order.getRefundAmount().compareTo(BigDecimal.ZERO) > 0
                        && order.getActualAmount().compareTo(order.getRefundAmount()) > 0){
                    updateOrder.setStatus(OrderStatus.USING.getCode());
                }else {
                    updateOrder.setStatus(OrderStatus.ORDER_FINISH.getCode());
                }
            }
            // 更新订单和订单明细
            orderService.updateById(updateOrder);
            orderDetailService.changeStatusByOrderId(updateOrder.getId(), updateOrder.getStatus());
        }

        if (updateById(orderRefund)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean refundSuccessWechatNotice(WxPayOrderRefundNotifyResultVO result) {
        if (result.getReturnCode().equalsIgnoreCase("SUCCESS")) {
            if (result.getRefundStatus().equalsIgnoreCase("SUCCESS")){
                String refundId = result.getOutRefundNo();
                try {
                    Date refundFinishTIme = org.apache.commons.lang3.time.DateUtils.parseDate(result.getSuccessTime(), "yyyy-MM-dd HH:mm:ss");
                    return refundSuccess(refundId, refundFinishTIme, result.getRefundRecvAccout());
                } catch (ParseException e) {
                    throw new RuntimeException(e);
                }
            }else {
                log.info("退款失败:");
            }
        }
        return false;
    }

    @Override
    public OrderRefund getByOrderId(String orderId) {
        QueryWrapper<OrderRefund> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderRefund::getOrderId, orderId);
        return getOne(queryWrapper);
    }

    @Override
    public List<BasicRefundOrderVO> getDateRangeRefundOrder(Long storeId, Date start, Date end, String categoryCode) {
        Category category = this.categoryService.getOneCategoryByCode(categoryCode);
        return getBaseMapper().getDateRangeRefundOrder(storeId, start, end, category.getId());
    }
}
