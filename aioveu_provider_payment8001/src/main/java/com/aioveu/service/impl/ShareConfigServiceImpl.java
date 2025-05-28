package com.aioveu.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ShareConfigDao;
import com.aioveu.dto.ShareConfigDTO;
import com.aioveu.entity.Order;
import com.aioveu.entity.ShareConfig;
import com.aioveu.entity.Store;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.OrderService;
import com.aioveu.service.ShareConfigService;
import com.aioveu.service.StoreService;
import com.aioveu.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ShareConfigServiceImpl extends ServiceImpl<ShareConfigDao, ShareConfig> implements ShareConfigService {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StoreService storeService;


    @Override
    public ShareConfig getByCategoryIdAndProductId(Long companyId, Long categoryId, String productId, Long storeId, Integer shareType) {
        QueryWrapper<ShareConfig> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ShareConfig::getShareType, shareType)
                .eq(ShareConfig::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(ShareConfig::getProductId, ShareConfig::getStoreId,
                        ShareConfig::getCompanyId, ShareConfig::getCategoryId);

        List<ShareConfig> shareConfigList = list(queryWrapper);
        if (CollectionUtils.isEmpty(shareConfigList)) {
            return null;
        }
        for (ShareConfig shareConfig : shareConfigList) {
            if (shareConfig.getProductId() != null) {
                if (shareConfig.getProductId().equals(productId) && shareConfig.getCategoryId().equals(categoryId)) {
                    return shareConfig;
                }
            } else if (shareConfig.getStoreId() != null) {
                if (shareConfig.getStoreId().equals(storeId)) {
                    if (shareConfig.getCategoryId() != null) {
                        if (shareConfig.getCategoryId().equals(categoryId)) {
                            return shareConfig;
                        }
                        continue;
                    }
                    return shareConfig;
                }
            } else if (shareConfig.getCompanyId() != null) {
                if (shareConfig.getCompanyId().equals(companyId)) {
                    if (shareConfig.getCategoryId() != null) {
                        if (shareConfig.getCategoryId().equals(categoryId)) {
                            return shareConfig;
                        }
                        continue;
                    }
                    return shareConfig;
                }
            } else if (shareConfig.getCategoryId() != null && shareConfig.getCategoryId().equals(categoryId)) {
                return shareConfig;
            }
        }
        return null;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean shareReward(ShareConfigDTO shareConfigDTO, Order order) {
        if (shareConfigDTO.getRewardCondition()) {
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.lambda().eq(Order::getUserId, shareConfigDTO.getShareUserId());
            if (shareConfigDTO.getCategoryId() != null) {
                orderQueryWrapper.lambda().eq(Order::getCategoryId, shareConfigDTO.getCategoryId());
            }
            if (shareConfigDTO.getStoreId() != null) {
                orderQueryWrapper.lambda().eq(Order::getStoreId, shareConfigDTO.getStoreId());
            }
            if (shareConfigDTO.getProductId() != null) {
                orderQueryWrapper.lambda().eq(Order::getProductId, shareConfigDTO.getProductId());
            }
            if (shareConfigDTO.getCompanyId() != null) {
                List<Long> storeIds = storeService.getByCompanyId(shareConfigDTO.getCompanyId()).stream().map(Store::getId).collect(Collectors.toList());
                orderQueryWrapper.lambda().in(Order::getStoreId, storeIds);
            }
            if (orderService.count(orderQueryWrapper) == 0) {
                log.info("{}未下单，{}分享无效", shareConfigDTO.getShareUserId(), JSON.toJSONString(shareConfigDTO));
                return false;
            }
        }
        BigDecimal reward = new BigDecimal(0);
        if (shareConfigDTO.getRewardAmount() != null && shareConfigDTO.getRewardAmount().doubleValue() > 0) {
            reward = reward.add(shareConfigDTO.getRewardAmount());
        } else {
            reward = reward.add(order.getAmount().multiply(BigDecimal.valueOf(shareConfigDTO.getRewardRatio())));
        }
        if (userService.addBalance(shareConfigDTO.getUserId(), reward, "分享下单奖励", order.getName() + "分享下单奖励",
                order.getId()) && userService.addBalance(shareConfigDTO.getShareUserId(), reward, "分享下单奖励",
                order.getName() + "分享下单奖励", order.getId())) {
            return true;
        }
        throw new SportException("分享奖励操作失败");
    }
}
