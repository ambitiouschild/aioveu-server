package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.constant.SportConstant;
import com.aioveu.dao.UserBalanceChangeDao;
import com.aioveu.entity.*;
import com.aioveu.enums.ChangeTypeStatus;
import com.aioveu.enums.DataStatus;
import com.aioveu.service.*;
import com.aioveu.vo.FieldOrderDetailVO;
import com.aioveu.vo.FieldOrderVO;
import com.aioveu.vo.UserBalanceChangeItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserBalanceChangeServiceImpl extends ServiceImpl<UserBalanceChangeDao, UserBalanceChange> implements UserBalanceChangeService {
    @Autowired
    private OrderService orderService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private StoreService storeService;

    @Override
    public boolean addBalanceRecord(String userId, BigDecimal amount, String orderId, String name, String description, Integer accountType) {
        return createUserBalanceChange(userId, amount, orderId, name, description, 0, accountType);
    }

    @Override
    public boolean reduceBalanceRecord(String userId, BigDecimal amount, String orderId, String name, String description, Integer accountType) {
        return createUserBalanceChange(userId, amount, orderId, name, description, 1, accountType);
    }

    @Override
    public IPage<UserBalanceChangeItemVO> getAll(int pageNumber, int size, Integer type, String userId, Integer accountType, Long userVipCardId) {
        QueryWrapper<UserBalanceChange> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda()
                .eq(UserBalanceChange::getStatus, DataStatus.NORMAL.getCode())
                .orderByDesc(UserBalanceChange::getCreateDate);
        if (userVipCardId != null) {
            queryWrapper.lambda().eq(UserBalanceChange::getUserVipCardId, userVipCardId);
        }
        if (accountType != null) {
            queryWrapper.lambda().eq(UserBalanceChange::getAccountType, accountType);
        }
        if (type != null) {
            queryWrapper.lambda().eq(UserBalanceChange::getChangeType, type);
        }
        IPage<UserBalanceChange> userBalanceChangeIPage = page(new Page<>(pageNumber, size), queryWrapper);
        List<UserBalanceChangeItemVO> records = userBalanceChangeIPage.getRecords().stream().map(item -> {
            UserBalanceChangeItemVO itemVO = new UserBalanceChangeItemVO();
            BeanUtils.copyProperties(item, itemVO);
            if (item.getOrderId() != null) {
                Order order = orderService.getById(item.getOrderId());
                Category category = categoryService.getById(order.getCategoryId());
                if (category.getCode().equals(SportConstant.FIELD_CATEGORY_CODE)) {
                    FieldOrderVO orderVO = new FieldOrderVO();
                    Store store = storeService.getById(order.getStoreId());
                    orderVO.setStoreName(store.getName());
                    orderVO.setStoreAddress(store.getAddress());
                    BeanUtils.copyProperties(order, orderVO);
                    Map<String, Map<String, List<FieldOrderDetailVO>>> fieldOrderDetailMap = new HashMap<>();
                    List<FieldOrderDetailVO> orderDetailList = this.orderDetailService.getFieldOrderDetailList(item.getOrderId());
                    for (FieldOrderDetailVO orderDetail : orderDetailList) {
                        String fieldDay = orderDetail.getFieldDay();
                        if (!fieldOrderDetailMap.containsKey(fieldDay)) {
                            fieldOrderDetailMap.put(fieldDay, new HashMap<>());
                        }
                        if (StringUtils.isEmpty(orderVO.getVenueName())) {
                            orderVO.setVenueName(orderDetail.getVenueName());
                        }
                        Map<String, List<FieldOrderDetailVO>> fieldMap = fieldOrderDetailMap.get(fieldDay);
                        if (!fieldMap.containsKey(orderDetail.getFieldName())) {
                            fieldMap.put(orderDetail.getFieldName(), new ArrayList<>());
                        }
                        fieldMap.get(orderDetail.getFieldName()).add(orderDetail);
                    }
                    orderVO.setFieldOrderDetailMap(fieldOrderDetailMap);
                    itemVO.setFieldOrderVO(orderVO);
                }
            }
            return itemVO;
        }).collect(Collectors.toList());

        IPage<UserBalanceChangeItemVO> iPage = new Page<>();
        BeanUtils.copyProperties(userBalanceChangeIPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }

    @Override
    public List<UserBalanceChange> getDeduction(String orderDetailId) {
        return getBaseMapper().getDeduction(orderDetailId);
    }

    @Override
    public List<UserBalanceChange> findByOrderId(String orderId) {
        QueryWrapper<UserBalanceChange> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().orderByDesc(UserBalanceChange::getCreateDate);
        if (orderId != null) {
            queryWrapper.lambda().eq(UserBalanceChange::getOrderId, orderId);
        }
        return list(queryWrapper);
    }

    @Override
    public List<UserBalanceChange> findByVipStatistics(Long storeId, Date start, Date end, String name) {
        return this.getBaseMapper().findByVipStatistics(storeId, start, end, name);
    }

    @Override
    public List<UserBalanceChange> findByFieldStatistics(Long storeId, Date start, Date end) {
        return this.getBaseMapper().findByFieldStatistics(storeId, start, end);
    }

    private boolean createUserBalanceChange(String userId, BigDecimal amount, String orderId, String name,
                                            String description, int changeType, Integer accountType) {
        UserBalanceChange userBalanceChange = new UserBalanceChange();
        userBalanceChange.setUserId(userId);
        userBalanceChange.setOrderId(orderId);
        userBalanceChange.setAmount(amount);
        userBalanceChange.setChangeType(changeType);
        userBalanceChange.setName(name);
        userBalanceChange.setStatus(DataStatus.NORMAL.getCode());
        userBalanceChange.setDescription(description);
        userBalanceChange.setAccountType(accountType);
        return save(userBalanceChange);
    }

    @Override
    public boolean insertUserBalanceChange(Order order, UserVipCard userVipCard, BigDecimal amount, ChangeTypeStatus changeType) {
        UserBalanceChange userBalanceChange = new UserBalanceChange();
        userBalanceChange.setUserId(order.getUserId());
        userBalanceChange.setUserVipCardId(userVipCard.getId());
        userBalanceChange.setName(order.getName());
        userBalanceChange.setDescription("");
        userBalanceChange.setOrderId(order.getId());
        userBalanceChange.setChangeType(changeType.getCode());
        userBalanceChange.setAmount(amount);
        userBalanceChange.setBalance(userVipCard.getBalance());
        return save(userBalanceChange);
    }

    @Override
    public boolean insertUserBalanceChange(UserVipCard userVipCard, BigDecimal amount, ChangeTypeStatus changeType, String userId, String name, String description) {
        UserBalanceChange userBalanceChange = new UserBalanceChange();
        userBalanceChange.setUserId(userId);
        userBalanceChange.setUserVipCardId(userVipCard.getId());
        userBalanceChange.setName(name);
        userBalanceChange.setDescription(description);
        userBalanceChange.setChangeType(changeType.getCode());
        userBalanceChange.setAmount(amount);
        userBalanceChange.setBalance(userVipCard.getBalance());
        return save(userBalanceChange);
    }
}
