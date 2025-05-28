package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.BusinessUserInfoDao;
import com.aioveu.entity.*;
import com.aioveu.enums.OrderStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.vo.StoreSimpleVO;
import com.aioveu.vo.StoreUserPublicInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class BusinessUserInfoServiceImpl extends ServiceImpl<BusinessUserInfoDao, BusinessUserInfo>
        implements BusinessUserInfoService {

    @Autowired
    private UserInfoPublicService userInfoPublicService;

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyStoreUserService companyStoreUserService;

    @Autowired
    private UserExtensionAccountService userExtensionAccountService;

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserViewPositionService userViewPositionService;

    @Override
    public boolean isNew(Long storeId, String userId, String orderDetailId) {
        User user = userService.getById(userId);
        UserInfoPublic userInfoPublic = userInfoPublicService.getByPhone(user.getPhone());
        if (userInfoPublic == null) {
            log.warn("用户公海无资料:{}", user.getPhone());
            userInfoPublic = new UserInfoPublic();
            userInfoPublic.setPhone(user.getPhone());
            userInfoPublic.setName(user.getName());
            userInfoPublic.setChannelCategory("地推");
            userInfoPublic.setChannelName("用户下单");
            return userInfoPublicService.save(userInfoPublic);
        }
        QueryWrapper<BusinessUserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BusinessUserInfo::getStoreId, storeId)
                .eq(BusinessUserInfo::getUserInfoPublicId, userInfoPublic.getId())
                .orderByDesc(BusinessUserInfo::getCreateDate);
        if (orderDetailId != null) {
            queryWrapper.lambda().eq(BusinessUserInfo::getOrderDetailId, orderDetailId);
        }

        queryWrapper.last("limit 1");

        BusinessUserInfo old = getOne(queryWrapper);
        if (old == null) {
            // 不存在客资记录，新客资
            return true;
        }
        if (orderDetailId != null) {
            // 有订单明细 根据明细对应的客资记录判断
            return old.getNewUser();
        }
        // 最后一条是老客，那么需要判断是否超过6个月 超过还是新客
        return !DateUtils.addMonths(old.getCreateDate(), 6).after(new Date());
    }

    @Override
    public boolean reduceBalance(Long storeId, String orderDetailId, String orderId, String name, String description,
                                 BigDecimal amount) {
        String storeUserId = companyStoreUserService.getStoreAdminUser(storeId);
        if (StringUtils.isNotEmpty(storeUserId)) {
            if (!userExtensionAccountService.reduceBalance(storeUserId, amount, name, description, orderId, true)) {
                log.error("客资费用扣除失败:{}", orderDetailId);
                throw new SportException("添加失败");
            }
            return true;
        } else {
            log.error("店铺:{}客资未扣除，没有配置管理用户:{}", storeId, orderDetailId);
            return false;
        }
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean receive(OrderDetail od, String pushUserId, boolean isNew, String userId) {
        User user = userService.getById(od.getUserId());
        UserInfoPublic userInfoPublic = userInfoPublicService.getByPhone(user.getPhone());
        log.info("用户公海资料:{}", userInfoPublic.getId());

        BusinessUserInfo item = new BusinessUserInfo();
        item.setPhone(userInfoPublic.getPhone());
        item.setStoreId(od.getStoreId());
        item.setOrderDetailId(od.getId());
        item.setExerciseId(NumberUtils.toLong(od.getProductId()));
        item.setUserInfoPublicId(userInfoPublic.getId());
        item.setPushUserId(pushUserId);
        item.setNewUser(isNew);
        item.setAddress(userViewPositionService.getPositionByUserId(userId));
        if (item.getPushUserId() != null) {
            item.setSource("地推用户下单");
        } else {
            item.setSource("平台用户下单");
        }
        if (save(item)) {
            // 跟商户进行结算
            String storeUserId = companyStoreUserService.getStoreAdminUser(od.getStoreId());
            if (StringUtils.isNotEmpty(storeUserId)) {
                userService.addBalance(storeUserId, od.getProductPrice().multiply(new BigDecimal("0.9")),
                        "新订单", "用户下单结算", od.getId());
            }
            if (item.getNewUser()) {
                reduceBalance(od.getStoreId(), od.getId(), od.getOrderId(), "客资扣除", "客资扣除", new BigDecimal(100));
            }
            return true;
        } else {
            throw new SportException("添加失败");
        }
    }

    @Override
    public IPage<StoreUserPublicInfoVO> getList(int page, int size, String username) {
        String userId = userService.getUserIdFromCache(username);
        List<StoreSimpleVO> storeList = companyStoreUserService.getStoreByUserId(userId);
        if (CollectionUtils.isEmpty(storeList)) {
            throw new SportException("当前用户没有权限");
        }
        return getBaseMapper().getList(new Page<>(page, size), storeList);
    }

    @Override
    public boolean active(Long id, String username) {
        User user = userService.findUserByUsername(username);
        BusinessUserInfo businessUserInfo = getById(id);
        if (!companyStoreUserService.checkUserStore(user.getId(), businessUserInfo.getStoreId())) {
            throw new SportException("当前用户没有权限");
        }
        if (user.getBalance().doubleValue() < 0) {
            throw new SportException("当前店铺余额为" + user.getBalance().doubleValue() + ", 请充值后查看!");
        }
        BusinessUserInfo bu = new BusinessUserInfo();
        bu.setId(id);
        bu.setStatus(2);
        return updateById(bu);
    }

    @Override
    public boolean phoneInvalid(Long id, String username) {
        BusinessUserInfo businessUserInfo = getById(id);
        OrderDetail od = orderDetailService.getById(businessUserInfo.getOrderDetailId());
        if (OrderStatus.ORDER_FINISH.getCode().equals(od.getStatus())) {
            throw new SportException("该用户已到店, 不可操作!");
        }
        BusinessUserInfo bu = new BusinessUserInfo();
        bu.setId(id);
        bu.setStatus(8);
        return updateById(bu);
    }

    @Override
    public IPage<StoreUserPublicInfoVO> getPhoneInvalid(Integer page, Integer size, Long id, String name) {
        return getBaseMapper().getPhoneInvalid(new Page<>(page, size), id, name);
    }

    @Override
    public boolean phoneNormal(Long id) {
        BusinessUserInfo bu = new BusinessUserInfo();
        bu.setId(id);
        bu.setStatus(1);
        return updateById(bu);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean examineAdopt(Long id) {
        BusinessUserInfo businessUserInfo = getBaseMapper().selectById(id);
        List<UserBalanceChange> userBalanceChange = userBalanceChangeService.getDeduction(businessUserInfo.getOrderDetailId());
        if (CollectionUtils.isNotEmpty(userBalanceChange)){
            userBalanceChange.forEach(item->{
                if (item.getChangeType() == 1){
                    UserExtensionAccount user = userService.getUserBalance(item.getUserId());
                    user.setBalance(user.getBalance().add(item.getAmount()));
                    userService.updateBalanceById(user);
                    userBalanceChangeService.addBalanceRecord(item.getUserId(), item.getAmount(), item.getOrderId(), "退客资", "退客资增加", 1);
                }else if (item.getChangeType() == 0){
                    User user = userService.getById(item.getUserId());
                    user.setBalance(user.getBalance().subtract(item.getAmount()));
                    user.setTotalBalance(user.getTotalBalance().subtract(item.getAmount()));
                    userService.updateById(user);
                    userBalanceChangeService.reduceBalanceRecord(item.getUserId(), item.getAmount(), item.getOrderId(), "退客资-"+item.getName(), "退客资扣除", 0);
                }
            });
        }
        businessUserInfo.setStatus(10);
        if (getBaseMapper().updateById(businessUserInfo) > 0){
            return true;
        }
        throw new SportException("退客资失败");
    }
}
