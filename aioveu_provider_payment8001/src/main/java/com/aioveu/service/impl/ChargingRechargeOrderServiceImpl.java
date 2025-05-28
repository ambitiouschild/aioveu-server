package com.aioveu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.SportConstant;
import com.aioveu.dao.ChargingRechargeOrderDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DelayMessageType;
import com.aioveu.enums.OrderStatus;
import com.aioveu.enums.PayCategoryEnum;
import com.aioveu.enums.PayType;
import com.aioveu.exception.SportException;
import com.aioveu.feign.vo.WeChatPayParamVO;
import com.aioveu.form.ChargingRechargeOrderForm;
import com.aioveu.service.*;
import com.aioveu.utils.IdUtils;
import com.aioveu.vo.WxMaPayVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.redis.util.RedisLockRegistry;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ChargingRechargeOrderServiceImpl extends ServiceImpl<ChargingRechargeOrderDao, ChargingRechargeOrder> implements ChargingRechargeOrderService {


    @Autowired
    private ChargingOptionService chargingOptionService;

    @Autowired
    private ChargingChargeOptionService chargingChargeOptionService;

    @Autowired
    private StoreChargingOptionService storeChargingOptionService;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private MQMessageService mqMessageService;

    @Resource
    private RedisLockRegistry redisLockRegistry;

    @Override
    public WxMaPayVO create(ChargingRechargeOrderForm form) {
        LoginVal currentUser = OauthUtils.getCurrentUser();

        StoreChargingOption storeChargingOption = storeChargingOptionService.getById(form.getStoreChargingOptionId());
        ChargingRechargeOrder order = new ChargingRechargeOrder();
        order.setId(IdUtils.getStrNumberId("CZ"));
        order.setStoreChargingOptionId(form.getStoreChargingOptionId());
        order.setCompanyId(storeChargingOption.getCompanyId());
        order.setStoreId(storeChargingOption.getStoreId());
        order.setName(storeChargingOption.getName() + "充值");
        order.setChargingCode(storeChargingOption.getChargingCode());
        order.setUserId(currentUser.getUserId());
        order.setStatus(OrderStatus.UN_PAY.getCode());
        if (form.getChargingChargeOptionId() != null) {
            ChargingChargeOption chargingChargeOption = chargingChargeOptionService.getById(form.getChargingChargeOptionId());
            order.setAmount(chargingChargeOption.getOriginalPrice());
            order.setConsumeAmount(chargingChargeOption.getPrice());
            order.setCouponAmount(order.getAmount().subtract(order.getConsumeAmount()));
            order.setActualAmount(chargingChargeOption.getPrice());
            order.setRefundAmount(BigDecimal.ZERO);
            order.setCanRefundAmount(chargingChargeOption.getPrice());
            order.setCount(chargingChargeOption.getCount());
            order.setGiftCount(chargingChargeOption.getGiftCount());
        } else {
            ChargingOption chargingOption = chargingOptionService.getById(form.getChargingOptionId());
            if (form.getAmount().compareTo(chargingOption.getMinAmount()) < 0) {
                throw new SportException("充值金额不能小于最低金额" + chargingOption.getMinAmount());
            }
            order.setAmount(form.getAmount());
            order.setCouponAmount(BigDecimal.ZERO);
            order.setConsumeAmount(order.getAmount());
            order.setActualAmount(order.getAmount());
            order.setRefundAmount(BigDecimal.ZERO);
            order.setCanRefundAmount(form.getAmount());
            order.setCount(form.getAmount().divide(chargingOption.getPrice(), 0, RoundingMode.HALF_UP).intValue());
            order.setGiftCount(0);
        }

        Company company = companyService.getCompanyByMiniAppId(SportConstant.QU_SHU_MINI_APP);
        company.setMiniAppId(SportConstant.QU_SHU_MANAGE_MINI_APP);
        WxMaPayVO wxMaPayVO = new WxMaPayVO();
        order.setAppId(company.getMiniAppId());
        String noticeUrl;
        String tradeType = null;
        if (PayType.HF.getCode().equals(company.getPayType())) {
            order.setPayAppId(company.getHuiFuId());
            noticeUrl = "hf-pay-callback";
            tradeType = "T_MINIAPP";
        } else {
            order.setPayAppId(company.getMiniAppPayId());
            noticeUrl = "store-recharge-wechat-pay";
        }
        userOpenIdService.addOrUpdate(currentUser.getUserId(), SportConstant.QU_SHU_MANAGE_MINI_APP, form.getOpenId(), null);
        try {
            WeChatPayParamVO weChatPayParamVO = orderService.getPayParam(order.getUserId(), form.getOpenId(), company.getMiniAppId(),
                    order.getPayAppId(), order.getId(), order.getName(), order.getActualAmount(), noticeUrl, company.getPayType(), tradeType, PayCategoryEnum.STORE_RECHARGE.getCode(), 20);
            BeanUtils.copyProperties(weChatPayParamVO, wxMaPayVO);
            order.setPrepayId(weChatPayParamVO.getPrepayId());
            order.setNonceStr(weChatPayParamVO.getNonceStr());
            order.setPayType(company.getPayType());
            save(order);
            // 微信支付 订单超时自动关闭
            Map<String, Object> msgMap = new LinkedHashMap<>();
            msgMap.put("type", DelayMessageType.UNPAY_CANCEL.getCode());
            msgMap.put("orderId", order.getId());
            msgMap.put("payType", PayCategoryEnum.STORE_RECHARGE.getCode());
            mqMessageService.sendDelayMsgByDate(msgMap, DateUtils.addMinutes(new Date(), 10));
            return wxMaPayVO;
        }catch (Exception e) {
            e.printStackTrace();
            throw new SportException("创建订单异常");
        }
    }

    @Override
    public boolean updateOrder2Success(String orderId, Date payFinishTime) {
        String key = "STORE_RECHARGE_" + orderId;
        Lock lock = redisLockRegistry.obtain(key);
        lock.lock();
        try {
            ChargingRechargeOrder order = getById(orderId);
            if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
                log.info("充值订单支付成功:{}", orderId);
                ChargingRechargeOrder update = new ChargingRechargeOrder();
                update.setId(orderId);
                update.setStatus(OrderStatus.PAY.getCode());
                update.setPayFinishTime(payFinishTime);
                if (updateById(update)) {
                    // 增值服务 变动记录 异步MQ调用 增加次数
                    ChargingChange chargingChange = new ChargingChange();
                    chargingChange.setName(order.getName());
                    chargingChange.setCompanyId(order.getCompanyId());
                    chargingChange.setStoreId(order.getStoreId());
                    chargingChange.setChargingCode(order.getChargingCode());
                    chargingChange.setDescription("充值增加次数");
                    chargingChange.setCount(order.getCount());
                    chargingChange.setChangeType(1);
                    chargingChange.setProductId(orderId);
                    mqMessageService.sendChargingChange(chargingChange);
                    // 充值赠送的次数
                    if (order.getGiftCount() != null && order.getGiftCount() > 0) {
                        ChargingChange giveChange = new ChargingChange();
                        giveChange.setName(order.getName() + "赠送");
                        giveChange.setCompanyId(order.getCompanyId());
                        giveChange.setStoreId(order.getStoreId());
                        giveChange.setChargingCode(order.getChargingCode());
                        giveChange.setDescription("充值赠送次数");
                        giveChange.setCount(order.getGiftCount());
                        giveChange.setChangeType(1);
                        giveChange.setProductId(orderId);
                        mqMessageService.sendChargingChange(giveChange);
                    }
                    return true;
                }
            } else {
                log.info("订单状态已支付:{}", orderId);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
        return false;
    }

    @Override
    public boolean checkPayStatus(String orderId) {
        ChargingRechargeOrder order = getById(orderId);
        if (order.getStatus().equals(OrderStatus.UN_PAY.getCode())) {
            try {
                log.info("用户自己查询支付更新充值未完成充值订单{}", orderId);
                Date payFinishTime = orderService.checkOrderPayFinishTime(orderId, order.getPayAppId(), order.getPayType(), DateFormatUtils.format(order.getCreateDate(), "yyyyMMdd"));
                return updateOrder2Success(orderId, payFinishTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (order.getStatus().equals(OrderStatus.PAY.getCode())) {
            log.info("用户自己查询充值订单{}已支付完成", orderId);
            return true;
        }
        return false;
    }


}
