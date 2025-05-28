package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.RechargeOrderDao;
import com.aioveu.entity.Company;
import com.aioveu.entity.RechargeOption;
import com.aioveu.entity.RechargeOrder;
import com.aioveu.entity.User;
import com.aioveu.enums.OrderStatus;
import com.aioveu.enums.PayCategoryEnum;
import com.aioveu.exception.SportException;
import com.aioveu.feign.vo.WeChatPayParamVO;
import com.aioveu.service.*;
import com.aioveu.utils.IdUtils;
import com.aioveu.vo.WxMaPayVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class RechargeOrderServiceImpl extends ServiceImpl<RechargeOrderDao, RechargeOrder> implements RechargeOrderService {

    @Autowired
    private RechargeOptionService rechargeOptionService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private UserExtensionAccountService userExtensionAccountService;

    @Autowired
    private CompanyService companyService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WxMaPayVO createRechargeOrder(String username, Long id) {
        RechargeOption rechargeOption = rechargeOptionService.getById(id);
        if (rechargeOption == null) {
            throw new SportException("充值参数错误");
        }
        //获取用户的余额
        User user = userService.findUserByUsername(username);
        if (user.getBalance() == null) {
            user.setBalance(BigDecimal.ZERO);
        }
        String userId = user.getId();
        RechargeOrder rechargeOrder = new RechargeOrder();
        String orderId = IdUtils.getStrNumberId("CD");
        rechargeOrder.setId(orderId);
        rechargeOrder.setUserId(userId);
        rechargeOrder.setStatus(1);
        rechargeOrder.setName("充值");

        WxMaPayVO wxMaPayVO = new WxMaPayVO();

        // 充值金额
        BigDecimal amount = rechargeOption.getMoney();
        rechargeOrder.setAmount(amount);
        rechargeOrder.setActualAmount(amount);
        if (rechargeOption.getGive() != null) {
            rechargeOrder.setGiveAmount(rechargeOption.getGive());
            rechargeOrder.setTotalAmount(amount.add(rechargeOption.getGive()));
        } else {
            rechargeOrder.setTotalAmount(amount);
        }
        rechargeOrder.setAppId("wxb28b75a3bc0f24ef");
        rechargeOrder.setWxPayId("wxb28b75a3bc0f24ef");

        if (user.getBalance().doubleValue() > 0) {
            BigDecimal balance = user.getBalance().subtract(amount);
            if (balance.doubleValue() >= 0) {
                rechargeOrder.setActualAmount(BigDecimal.ZERO);
                wxMaPayVO.setOrderId(orderId);
                return wxMaPayVO;
            } else {
                // 用户余额不能完全抵扣 实际支付是减去之后的金额
                rechargeOrder.setActualAmount(balance.abs());
            }
        }

        Company company = companyService.getCompanyByMiniAppId("wxe267b90015e11ac8");
        String openId = userOpenIdService.getByAppIdAndUserId(userId, "wxb28b75a3bc0f24ef", true);
        WeChatPayParamVO weChatPayParamVO = orderService.getPayParam(userId, openId, rechargeOrder.getAppId(),
                rechargeOrder.getWxPayId(), orderId, "充值", rechargeOrder.getActualAmount(), "wechat-recharge-pay", company.getPayType(), "T_MINIAPP", PayCategoryEnum.RECHARGE.getCode(), 30);

        BeanUtils.copyProperties(weChatPayParamVO, wxMaPayVO);
        rechargeOrder.setPrepayId(weChatPayParamVO.getPrepayId());
        rechargeOrder.setPayType("wx");
        rechargeOrder.setNonceStr(weChatPayParamVO.getNonceStr());

        if (save(rechargeOrder)) {
            return wxMaPayVO;
        }
        throw new SportException("充值订单创建失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public synchronized boolean updateOrder2Success(String orderId, Date payFinishTime) {
        RechargeOrder order = getById(orderId);
        if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            if (order.getNonceStr() != null) {
                log.info("用户自己查询微信支付更新未完成充值订单{}", orderId);
                try {
                    payFinishTime = orderService.checkOrderPayFinishTime(orderId, order.getWxPayId(), order.getPayType(), DateFormatUtils.format(order.getCreateDate(), "yyyyMMdd"));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
                if (payFinishTime == null) {
                    throw new SportException("未查询到订单支付状态");
                }
            }else {
                payFinishTime = new Date();
                log.info("用户自己查询更新未完成充值订单{}", orderId);
            }
            // 充值支付成功
            // 1、更新充值订单状态
            RechargeOrder rechargeOrder = new RechargeOrder();
            rechargeOrder.setId(orderId);
            rechargeOrder.setStatus(2);
            rechargeOrder.setPayFinishTime(payFinishTime);
            if (updateById(rechargeOrder)) {
                //获取用户的余额
                User user = userService.getById(order.getUserId());
                if (order.getActualAmount().doubleValue() == 0) {
                    // 用户余额 完全抵扣 那么余额减去本次充值的所有金额
                    if (!userService.reduceBalance(user.getId(), order.getAmount(), "充值抵扣", "推广账户充值抵扣", orderId, true)) {
                        log.error("用户余额部分扣减失败:{}", user.getId());
                        throw new SportException("充值订单更新失败!");
                    }
                } else if (order.getAmount().doubleValue() != order.getActualAmount().doubleValue()){
                    if (!userService.reduceBalance(user.getId(), order.getAmount().subtract(order.getActualAmount()), "充值抵扣",
                            "推广账户充值抵扣", orderId, true)) {
                        log.error("用户余额部分扣减失败:{}", user.getId());
                        throw new SportException("充值订单更新失败!");
                    }
                }
                // 2、余额增加 + 充值记录增加
                userExtensionAccountService.addBalance(order.getUserId(), order.getAmount(), "商户充值", "充值" + order.getAmount().doubleValue(), orderId);
                if (order.getGiveAmount() != null) {
                    userExtensionAccountService.addBalance(order.getUserId(), order.getGiveAmount(), "充值赠送余额", "充值赠送" + order.getGiveAmount().doubleValue(), orderId);
                }
                return true;
            }
            return false;
        }
        return true;
    }

}
