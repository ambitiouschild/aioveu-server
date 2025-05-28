package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserVipCardDao;
import com.aioveu.entity.*;
import com.aioveu.enums.ChangeTypeStatus;
import com.aioveu.enums.DataStatus;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.vo.UserVipCardItemVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2023/01/17 10:42
 */
@Slf4j
@Service
public class UserVipCardServiceImpl extends ServiceImpl<UserVipCardDao, UserVipCard> implements UserVipCardService {

    @Autowired
    private VipCardService vipCardService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    @Autowired
    private UserService userService;

    @Override
    public List<UserVipCard> getActiveList(Long companyId, String userId) {
        QueryWrapper<UserVipCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserVipCard::getStatus, DataStatus.NORMAL.getCode())
                .eq(UserVipCard::getCompanyId, companyId)
                .eq(UserVipCard::getUserId, userId)
                .orderByDesc(UserVipCard::getCreateDate);
        return list(queryWrapper);
    }

    @Override
    public List<UserVipCard> getManageList(Long companyId, String phone, String vipCardNo, String username) {
        return getBaseMapper().getManageList(companyId, phone, vipCardNo, username);
    }

    @Override
    public UserVipCard getOneById(Long id) {
        UserVipCard userVipCard = this.getById(id);
        if (userVipCard != null) {
            User user = this.userService.getById(userVipCard.getUserId());
            if (user != null) {
                userVipCard.setPhone(user.getUsername());
            }
        }
        return userVipCard;
    }

    @Override
    public void saveUserVipCard(UserVipCard userVipCard, String createUserId) {
        if (StringUtils.isEmpty(userVipCard.getUsername())) {
            userVipCard.setUsername(userVipCard.getPhone());
        }
        if (userVipCard.getId() != null) {
            UserVipCard nowUserVipCard = this.getById(userVipCard.getId());
            BigDecimal oldBalance = nowUserVipCard.getBalance();
            userVipCard.setUpdateDate(new Date());
            this.updateById(userVipCard);
            if (userVipCard.getBalance().compareTo(oldBalance) > 0) {
                BigDecimal amount = userVipCard.getBalance().subtract(oldBalance);
                this.userBalanceChangeService.insertUserBalanceChange(userVipCard, amount, ChangeTypeStatus.Recharge, createUserId, "后台操作", null);
            } else if (userVipCard.getBalance().compareTo(oldBalance) < 0) {
                BigDecimal amount = oldBalance.subtract(userVipCard.getBalance());
                this.userBalanceChangeService.insertUserBalanceChange(userVipCard, amount, ChangeTypeStatus.Payment, createUserId, "后台操作", null);
            }
        } else {
            if (userVipCard.getVipCardId() == null) {
                throw new SportException("请选择会员卡");
            }
            if (StringUtils.isEmpty(userVipCard.getPhone())) {
                throw new SportException("用户手机号不能为空");
            }
            User user = this.userService.findUserByUsername(userVipCard.getPhone());
            if (user == null) {
                throw new SportException("手机号对应的用户不存在");
            }
            VipCard vipCard = this.vipCardService.getById(userVipCard.getVipCardId());
            Category category = this.categoryService.getOneCategoryByCode(vipCard.getProductCategoryCode());
            boolean hasPriceRule = !CollectionUtils.isEmpty(vipCard.getPriceRules());
            UserVipCard oldUserVipCard = null;
            if (hasPriceRule) {
                oldUserVipCard = getUserVipCard(user.getId(), vipCard.getId(), vipCard.getCompanyId());
            } else {
                oldUserVipCard = getUserVipCard(user.getId(), vipCard.getProductCategoryCode(), vipCard.getCompanyId());
            }
            if (oldUserVipCard != null) {
                throw new SportException("当前用户已经创建了该会员卡");
            }
            if (hasPriceRule) {
                userVipCard.setName(vipCard.getName());
            } else {
                userVipCard.setName(category.getName() + "会员卡");
            }
            userVipCard.setCompanyId(vipCard.getCompanyId());
            userVipCard.setStoreId(vipCard.getStoreId());
            userVipCard.setCategoryCode(vipCard.getCategoryCode());
            userVipCard.setProductCategoryCode(category.getCode());
            userVipCard.setVipCode(RandomStringUtils.randomNumeric(8));
            userVipCard.setUserId(user.getId());
            this.save(userVipCard);
            this.userBalanceChangeService.insertUserBalanceChange(userVipCard, userVipCard.getBalance(), ChangeTypeStatus.Recharge, createUserId, "后台操作", null);
        }
    }

    @Override
    public boolean receiveVipCard(Long vipCardId, String userId) {
        VipCard vipCard = vipCardService.getById(vipCardId);

        UserVipCard userVipCard = new UserVipCard();
        BeanUtils.copyProperties(vipCard, userVipCard);

        userVipCard.setVipCode(RandomStringUtils.randomNumeric(8));
        userVipCard.setUserId(userId);
        userVipCard.setVipCardId(vipCardId);
        User user = userService.getById(userId);
        userVipCard.setUsername(user.getName());

        // 设置会员卡有效期
        if (vipCard.getFixedTime() != null) {
            userVipCard.setValidTime(vipCard.getFixedTime());
        } else if (vipCard.getReceiveDay() != null) {
            userVipCard.setValidTime(DateUtils.addDays(new Date(), vipCard.getReceiveDay()));
        }
        return save(userVipCard);
    }

    @Override
    public UserVipCard getUserVipCard(String userId, Long productCategoryId, Long companyId, Long storeId) {
        return null;
    }

    @Override
    public synchronized UserVipCard recharge(Order order, VipCard vipCard, String userId, Long companyId, Long storeId) {
        UserVipCard userVipCard = null;
        Category category = this.categoryService.getOneCategoryByCode(vipCard.getProductCategoryCode());
        Boolean hasPriceRule = !CollectionUtils.isEmpty(vipCard.getPriceRules());
        if (hasPriceRule) {
            userVipCard = getUserVipCard(userId, vipCard.getId(), companyId);
        } else {
            userVipCard = getUserVipCard(userId, vipCard.getProductCategoryCode(), companyId);
        }

        if (userVipCard == null) {
            userVipCard = new UserVipCard();
            if (hasPriceRule) {
                userVipCard.setName(vipCard.getName());
                userVipCard.setVipCardId(vipCard.getId());
            } else {
                userVipCard.setName(category.getName() + "会员卡");
            }
            User user = userService.getById(userId);
            userVipCard.setUsername(user.getName());
            userVipCard.setBalance(BigDecimal.ZERO);
            userVipCard.setCompanyId(companyId);
            userVipCard.setStoreId(storeId);
            userVipCard.setCategoryCode(vipCard.getCategoryCode());
            userVipCard.setProductCategoryCode(category.getCode());
            userVipCard.setVipCode(RandomStringUtils.randomNumeric(8));
            userVipCard.setUserId(userId);
            this.save(userVipCard);
        }

        userVipCard.setBalance(userVipCard.getBalance().add(vipCard.getStoredPrice()));
        this.updateById(userVipCard);
        userBalanceChangeService.insertUserBalanceChange(order, userVipCard, vipCard.getStoredPrice(), ChangeTypeStatus.Recharge);
        return userVipCard;
    }

    @Override
    public synchronized boolean refund(UserBalanceChange userBalanceChange) {
        UserVipCard userVipCard = this.getById(userBalanceChange.getUserVipCardId());
        UserVipCard uvc = new UserVipCard();
        uvc.setId(userVipCard.getId());
        BigDecimal actAmount = userBalanceChange.getAmount();
        uvc.setBalance(userVipCard.getBalance().add(actAmount));
        this.updateById(uvc);
        userBalanceChange.setChangeType(ChangeTypeStatus.Refund.getCode());
        userBalanceChange.setId(null);
        userBalanceChange.setCreateDate(null);
        userBalanceChange.setUpdateDate(null);
        userBalanceChange.setDescription("订场取消订场费用退回到储值卡中:" + userVipCard.getVipCode());
        this.userBalanceChangeService.save(userBalanceChange);
        return true;
    }

    @Override
    public synchronized boolean payment(Order order) {
        UserVipCard userVipCard = this.getById(order.getUserVipCardId());
        BigDecimal actAmount = order.getConsumeAmount().subtract(order.getActualAmount());
        if (userVipCard.getBalance().compareTo(actAmount) < 0) {
            return false;
        }
        userVipCard.setBalance(userVipCard.getBalance().subtract(actAmount));
        this.updateById(userVipCard);
        userBalanceChangeService.insertUserBalanceChange(order, userVipCard, actAmount, ChangeTypeStatus.Payment);
        return true;
    }

    @Override
    public UserVipCard getUserVipCard(String userId, String productCategoryCode, Long companyId) {
        QueryWrapper<UserVipCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserVipCard::getUserId, userId)
                .isNull(UserVipCard::getVipCardId)
                .eq(UserVipCard::getProductCategoryCode, productCategoryCode)
                .eq(UserVipCard::getCompanyId, companyId);
        UserVipCard userVipCard = getOne(queryWrapper);
        if (userVipCard != null) {
            if (userVipCard.getValidTime() != null && userVipCard.getValidTime().before(new Date())) {
                log.error("用户:{},产品:{}, 会员卡已过期", userId, productCategoryCode);
                return null;
            }
            return userVipCard;
        }
        return null;
    }

    @Override
    public UserVipCard getUserVipCard(String userId, Long vipCardId, Long companyId) {
        QueryWrapper<UserVipCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserVipCard::getUserId, userId)
                .eq(UserVipCard::getVipCardId, vipCardId)
                .eq(UserVipCard::getCompanyId, companyId);
        UserVipCard userVipCard = getOne(queryWrapper);
        if (userVipCard != null) {
            if (userVipCard.getValidTime() != null && userVipCard.getValidTime().before(new Date())) {
                log.error("用户:{},产品:{}, 会员卡已过期", userId, vipCardId);
                return null;
            }
            return userVipCard;
        }
        return null;
    }

    @Override
    public List<UserVipCardItemVO> getMyVipCardList(String username) {
        String userId = userService.getUserIdFromCache(username);
        return getBaseMapper().getMyVipCardList(userId);
    }

    @Override
    public Double getVipCardAmount(Long companyId) {
        QueryWrapper<UserVipCard> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserVipCard::getCompanyId, companyId)
                .eq(UserVipCard::getStatus, DataStatus.NORMAL.getCode())
                .gt(UserVipCard::getBalance, 0);
        List<UserVipCard> list = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(list)) {
            BigDecimal vipCardAmount = list.stream().map(UserVipCard::getBalance).reduce(BigDecimal.ZERO, BigDecimal::add);
            return vipCardAmount.setScale(2, RoundingMode.HALF_UP).doubleValue();
        }
        return 0.00;
    }
}
