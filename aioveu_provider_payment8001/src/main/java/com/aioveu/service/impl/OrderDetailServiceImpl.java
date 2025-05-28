package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.SportConstant;
import com.aioveu.dao.OrderDetailDao;
import com.aioveu.entity.*;
import com.aioveu.enums.OrderStatus;
import com.aioveu.enums.PayType;
import com.aioveu.exception.SportException;
import com.aioveu.qrcode.QrCodeGenWrapper;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.SportDateUtils;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailDao, OrderDetail> implements OrderDetailService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    @Autowired
    private UserVipCardService userVipCardService;

    @Autowired
    private OrderRefundService orderRefundService;

    @Override
    public OrderDetail getByConsumeCode(String code) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetail::getConsumeCode, code);
        return getOne(queryWrapper);
    }

    @Override
    public Integer getUnUseOrderDetailCount(String orderId) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetail::getOrderId, orderId)
                        .eq(OrderDetail::getStatus, OrderStatus.PAY.getCode());
        return count(queryWrapper);
    }

    @Override
    public boolean changeStatus(String orderDetailId, int status) {
        OrderDetail orderDetail = getById(orderDetailId);
        if (orderDetail == null) {
            log.error("{}订单详情不存在", orderDetailId);
            throw new SportException("订单详情不存在");
        }
        if (!orderDetail.getStatus().equals(OrderStatus.PAY.getCode())) {
            log.error("{}订单状态异常", orderDetailId);
            throw new SportException("订单状态异常");
        }
        OrderDetail update = new OrderDetail();
        update.setId(orderDetailId);
        update.setStatus(status);
        return updateById(update);
    }

    @Override
    public boolean changeStatusByOrderId(String orderId, int status) {
        UpdateWrapper<OrderDetail> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(OrderDetail::getStatus, status)
                        .eq(OrderDetail::getOrderId, orderId);
        return update(updateWrapper);
    }

    @Override
    public Integer getEnrollUserCount(String productId, Long categoryId, String userId) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetail::getProductId, productId)
                .eq(OrderDetail::getCategoryId, categoryId);
        if (StringUtils.isNotEmpty(userId)) {
            queryWrapper.lambda().eq(OrderDetail::getUserId, userId);
        }
        queryWrapper.in("status", 2, 3, 4, 6);
        return count(queryWrapper);
    }

    @Override
    public IPage<UserEnterVO> getEnrollUserList(int page, int size, Collection<Long> productIds, Long categoryId) {
        if (CollectionUtils.isEmpty(productIds) || Objects.isNull(categoryId)) {
            return new Page<>(page, size, 0L);
        }
        return getBaseMapper().getEnrollUserList(new Page<>(page, size), productIds, categoryId);
    }

    @Override
    public List<OrderDetail> getByOrderId(String orderId) {
        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetail::getOrderId, orderId);
        return list(queryWrapper);
    }

    @Override
    public List<OrderDetailVO> getByOrderIdList(String orderId) {
        return getBaseMapper().getOrderDetailList(orderId);
    }

    @Override
    public List<FieldOrderDetailVO> getFieldOrderDetailList(String orderId) {
        return getBaseMapper().getFieldOrderDetailList(orderId);
    }

    @Override
    public OrderDetailVO getOrderDetail(String orderId, String phone) {
        OrderDetailVO orderDetailVO = getBaseMapper().getOrderDetail(orderId);
        if (orderDetailVO.getAgreementUrl() != null) {
            orderDetailVO.setAgreementUrl(FileUtil.getAgreementBase64(orderDetailVO.getAgreementUrl()));
        }
        List<ConsumeVO> consumeList = orderDetailVO.getConsumeList();
        for (ConsumeVO item : consumeList) {
            if (phone.equalsIgnoreCase(item.getPhone())) {
                setDefaultCode(orderId, orderDetailVO, item);
                break;
            }
        }
        if (orderDetailVO.getConsumeCode() == null) {
            setDefaultCode(orderId, orderDetailVO, consumeList.get(0));
        }
        Category category = this.categoryService.getById(orderDetailVO.getCategoryId());
        if (category != null && category.getCode().equals(SportConstant.FIELD_CATEGORY_CODE)) {
            List<FieldOrderDetailVO> orderDetailVOList = this.getFieldOrderDetailList(orderId);
            for (FieldOrderDetailVO fieldOrderDetailVO : orderDetailVOList) {
                if (fieldOrderDetailVO.getStatus().equals(OrderStatus.PAY.getCode())) {
                    setDefaultCode(orderId, fieldOrderDetailVO);
                }
            }
            orderDetailVO.setFieldOrderDetailVOList(orderDetailVOList);
            List<UserBalanceChange> userBalanceChangeList = this.userBalanceChangeService.findByOrderId(orderId);
            List<UserBalanceChangeItemVO> userBalanceChangeItemVOList = new ArrayList<>();
            for (UserBalanceChange userBalanceChange : userBalanceChangeList) {
                UserBalanceChangeItemVO userBalanceChangeItemVO = new UserBalanceChangeItemVO();
                BeanUtils.copyProperties(userBalanceChange, userBalanceChangeItemVO);
                UserVipCard userVipCard = this.userVipCardService.getById(userBalanceChange.getUserVipCardId());
                userBalanceChangeItemVO.setVipCode(userVipCard.getVipCode());
                userBalanceChangeItemVO.setUserVipCardName(userVipCard.getName());
                userBalanceChangeItemVOList.add(userBalanceChangeItemVO);
            }
            orderDetailVO.setUserBalanceChangeItemVOList(userBalanceChangeItemVOList);
        }
        // 如果订单是退款状态 查询退款时间
        if (orderDetailVO.getStatus().equals(OrderStatus.REFUND.getCode())) {
            OrderRefund orderRefund = orderRefundService.getByOrderId(orderDetailVO.getOrderId());
            if (orderRefund != null) {
                orderDetailVO.setRefundTime(orderRefund.getCreateDate());
            }
        }
        return orderDetailVO;
    }

    private void setDefaultCode(String orderId, OrderDetailVO orderDetailVO, ConsumeVO item) {
        orderDetailVO.setConsumeCode(item.getCode());
        try {
            orderDetailVO.setQrCode(getWeMaQrCode(orderDetailVO.getConsumeCode()));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单{}的消费码{}二维码生成错误", orderId, orderDetailVO.getConsumeCode());
        }
    }

    private void setDefaultCode(String orderId, FieldOrderDetailVO orderDetailVO) {
        if (StringUtils.isEmpty(orderDetailVO.getConsumeCode())) return;
        try {
            orderDetailVO.setQrCode(getWeMaQrCode(orderDetailVO.getConsumeCode()));
        } catch (Exception e) {
            e.printStackTrace();
            log.error("订单{}的消费码{}二维码生成错误", orderId, orderDetailVO.getConsumeCode());
        }
    }

    private String getWeMaQrCode(String consumeCode) throws Exception {
        String scanCode = "https://manager.highyundong.com/code?code=" + consumeCode;
        String scanQrCode = QrCodeGenWrapper.of(scanCode).asString();
        return "data:image/png;base64," + scanQrCode;
    }

    @Override
    public IPage<BasicOrderVO> getFieldOrderDetailRangeAndStatus(int page, int size, Long storeId, Date start, Date end, String payType, Integer... status) {
        Long categoryId = categoryService.getByCode(SportConstant.FIELD_CATEGORY_CODE);

        QueryWrapper<OrderDetail> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(OrderDetail::getStoreId, storeId)
                .eq(OrderDetail::getCategoryId, categoryId)
                .in(OrderDetail::getStatus, status)
                .ge(OrderDetail::getStartTime, start)
                .le(OrderDetail::getEndTime, end);
        if (StringUtils.isNotEmpty(payType) && !"null".equals(payType)) {
            queryWrapper.lambda().in(OrderDetail::getPayType, payType, PayType.MIX.getCode());
        }
        Page<OrderDetail> orderDetailPage = this.getBaseMapper().selectPage(new Page<>(page, size), queryWrapper);

        List<BasicOrderVO> records = orderDetailPage.getRecords().stream().map(item -> {
            BasicOrderVO basicOrderVO = new BasicOrderVO();
            BeanUtils.copyProperties(item, basicOrderVO);
            basicOrderVO.setName(item.getProductName());
            basicOrderVO.setId(item.getOrderId());
            return basicOrderVO;
        }).collect(Collectors.toList());

        IPage<BasicOrderVO> iPage = new Page<>();
        BeanUtils.copyProperties(orderDetailPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }

    @Autowired
    private OrderService orderService;

    @Autowired
    private FieldPlanService fieldPlanService;

    @Autowired
    private VipCardService vipCardService;

    @Override
    public void syncOrderDetail() {
        log.info("同步订单明细数据");
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();
        Long categoryId = categoryService.getByCode(SportConstant.FIELD_CATEGORY_CODE);
        queryWrapper.lambda().eq(Order::getCategoryId, categoryId)
                .in(Order::getStatus, OrderStatus.PAY.getCode(), OrderStatus.USED.getCode(), OrderStatus.USING.getCode(), OrderStatus.ORDER_FINISH.getCode());

//        queryWrapper.lambda().eq(Order::getCategoryId, categoryId)
//                .in(Order::getId, "FD17159099570709029");

        List<Order> orderList = orderService.list(queryWrapper);
        for (Order order : orderList) {
//            if (order.getActualAmount().doubleValue() > 0 && order.getActualAmount().doubleValue() != order.getConsumeAmount().doubleValue()) {
//                log.info("订单:{}输入混合支付订单", order.getId());
//                continue;
//            }

            if (order.getCouponAmount().doubleValue() > order.getAmount().doubleValue()) {
                log.error("订单:{}数据异常, 跳过更新", order.getId());
                continue;
            }

            UserVipCard userVipCard;
            VipCard vipCard = null;
            if (order.getUserVipCardId() != null) {
                userVipCard = userVipCardService.getById(order.getUserVipCardId());
                if (userVipCard.getVipCardId() != null) {
                    vipCard = vipCardService.getById(userVipCard.getVipCardId());
                }
            }

            List<OrderDetail> orderDetailList = getByOrderId(order.getId());
            List<OrderDetail> updateList = new ArrayList<>(orderDetailList.size());
            for (OrderDetail item : orderDetailList) {
                // 只更新订单明细为空的
                if (order.getPayType() != null && item.getAmount().doubleValue() == 0) {
                    OrderDetail od = new OrderDetail();
                    od.setId(item.getId());
                    od.setPayType(order.getPayType());
                    od.setStatus(order.getStatus());
                    // 针对订场订单 做开始时间和结束时间的同步
                    FieldPlan fp = fieldPlanService.getById(Long.parseLong(item.getProductId()));
                    if (fp == null) {
                        continue;
                    }
                    od.setStartTime(SportDateUtils.combineDateAndTime(fp.getFieldDay(), fp.getStartTime()));
                    od.setEndTime(SportDateUtils.combineDateAndTime(fp.getFieldDay(), fp.getEndTime()));

                    if (fp.getPrice().doubleValue() > order.getAmount().doubleValue()) {
                        od.setAmount(order.getAmount().divide(BigDecimal.valueOf(orderDetailList.size()), 2, RoundingMode.HALF_UP));
                    } else {
                        od.setAmount(fp.getPrice());
                    }

                    BigDecimal vipPrice = fp.getVipPrice() != null ? fp.getVipPrice() : fp.getPrice();
                    if (vipCard != null) {
                        PriceRule priceRule = vipCardService.matchingPriceRule(vipCard, fp);
                        if (priceRule != null) {
                            //会员卡特殊订场价格按照1小时价格算，兼容订场半小时
                            // 订场价格单位如果是半小时，取会员卡特殊订场价格/2，
                            // 订场价格单位如果是1小时，取会员卡特殊订场价格
                            Long minute = (fp.getEndTime().getTime() - fp.getStartTime().getTime())/(1000*60);
                            if (minute == 30){
                                vipPrice = priceRule.getVipPrice().divide(BigDecimal.valueOf(2), 2 , RoundingMode.HALF_UP);
                            }else {
                                vipPrice = priceRule.getVipPrice();
                            }
                        }
                        od.setActualAmount(BigDecimal.ZERO);
                        od.setCanRefundAmount(BigDecimal.ZERO);
                    } else {
                        // 没有会员卡 需要实际支付
                        od.setActualAmount(vipPrice);
                        od.setCanRefundAmount(od.getActualAmount());
                    }
                    if (vipPrice.doubleValue() > order.getConsumeAmount().doubleValue()) {
                        vipPrice = order.getConsumeAmount().divide(BigDecimal.valueOf(orderDetailList.size()), 2, RoundingMode.HALF_UP);
                    }
                    od.setRefundAmount(BigDecimal.ZERO);
                    // 消费金额是实际价格
                    od.setConsumeAmount(vipPrice);
                    // 优惠金额
                    od.setCouponAmount(od.getAmount().subtract(od.getConsumeAmount()));

                    if (od.getPayType().equals(PayType.MIX.getCode())) {
                        BigDecimal odCost = BigDecimal.ZERO;
                        if (order.getCouponAmount().doubleValue() > 0) {
                            // 根据单个商品的价格比重分配优惠比例
                            odCost = order.getCouponAmount().multiply(od.getAmount().divide(order.getAmount(), 2, RoundingMode.HALF_UP));
                        }
                        od.setPayType(order.getPayType());
                        // 49 -
                        od.setConsumeAmount(od.getConsumeAmount().subtract(odCost));
                        // 订单明细实付金额 = 订单实付金额 * 订单比重
                        od.setActualAmount(order.getActualAmount().multiply(od.getAmount().divide(order.getAmount(), 2, RoundingMode.HALF_UP)));
                        od.setCanRefundAmount(od.getActualAmount());
                        od.setCouponAmount(od.getAmount().subtract(od.getConsumeAmount()));
                    } else if (od.getPayType().equals(PayType.VIPCard.getCode())) {
                        // 完全会员卡支付的
                        BigDecimal odCost = BigDecimal.ZERO;
                        if (order.getCouponAmount().doubleValue() > 0) {
                            // 根据单个商品的价格比重分配优惠比例
                            odCost = order.getCouponAmount().multiply(od.getConsumeAmount().divide(order.getConsumeAmount(), 2, RoundingMode.HALF_UP));
                        }
                        od.setConsumeAmount(od.getConsumeAmount().subtract(odCost));
                        od.setActualAmount(BigDecimal.ZERO);
                        od.setCanRefundAmount(BigDecimal.ZERO);
                        od.setCouponAmount(od.getAmount().subtract(od.getConsumeAmount()));
                    } else {
                        // 完全微信支付的
                        BigDecimal odCost = BigDecimal.ZERO;
                        if (order.getCouponAmount().doubleValue() > 0) {
                            // 根据单个商品的价格比重分配优惠比例 132.99 * (128 / 0.01)
                            odCost = order.getCouponAmount().multiply(od.getConsumeAmount().divide(order.getConsumeAmount(), 2, RoundingMode.HALF_UP));
                        }
                        od.setConsumeAmount(od.getAmount().subtract(odCost));
                        od.setActualAmount(od.getConsumeAmount());
                        od.setCanRefundAmount(od.getActualAmount());
                        od.setCouponAmount(od.getAmount().subtract(od.getConsumeAmount()));
                    }
                    updateList.add(od);
                }
            }
            if (CollectionUtils.isNotEmpty(updateList)) {
                updateBatchById(updateList);
            }
        }
    }
}
