package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserCallDao;
import com.aioveu.dto.UserCouponDTO;
import com.aioveu.entity.*;
import com.aioveu.enums.OrderStatus;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.exception.SportException;
import com.aioveu.form.UserCallFollow;
import com.aioveu.form.UserCallUpdateForm;
import com.aioveu.service.*;
import com.aioveu.utils.ListUtil;
import com.aioveu.vo.NewUserInfoVO;
import com.aioveu.vo.StoreCoachVO;
import com.aioveu.vo.UserCallSimpleVO;
import com.aioveu.vo.UserCallVO;
import com.aioveu.vo.user.SportUserForm;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserCallServiceImpl extends ServiceImpl<UserCallDao, UserCall> implements UserCallService {

    @Autowired
    private UserCallPoolService userCallPoolService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserReceiveCallService userReceiveCallService;

    @Autowired
    private UserService userService;

    @Autowired
    private IUserCouponService userCouponService;

    @Autowired
    private StoreCoachService storeCoachService;

    @Autowired
    private StoreService storeService;

    @Override
    public IPage<NewUserInfoVO> newUserInfoList(int page, int size, String createUserId, String userId, String timeFrom, String timeTo, Integer status, String keyword) {
        if(!StringUtils.isEmpty(timeTo)) {
            timeTo = timeTo + " 23:59:59";
        }
        List<Integer> statusList = Arrays.asList(OrderStatus.PAY.getCode(), OrderStatus.USING.getCode(), OrderStatus.ORDER_FINISH.getCode(), OrderStatus.USED.getCode());
        IPage<NewUserInfoVO> iPage = getBaseMapper().newUserInfoList(new Page<>(page, size),createUserId, userId, timeFrom, timeTo, status, keyword, statusList);
        return iPage;
    }

    @Override
    public boolean updateUserReceiveCallToUser(String userId, String transferPhone) {
        if(StringUtils.isEmpty(transferPhone)) {
            throw new SportException("用户不能为空");
        }
        User user = userService.findUserByUsername(transferPhone);
        if(user == null) {
            throw new SportException("用户不存在");
        }
        this.getBaseMapper().updateUserReceiveCallToUser(user.getId(), userId);
        return true;
    }

    @Override
    public IPage<UserCallVO> getAllListByPage(int page, int size, Long storeId, String keyword, Integer type) {
        List<StoreCoachVO> storeCoachList = storeCoachService.getStoreCoachUser(storeId);
        if (CollectionUtils.isEmpty(storeCoachList)){
            return null;
        }
        String userId = storeCoachList.stream().map(StoreCoachVO::getUserId).collect(Collectors.joining(","));
        return getList(page, size, userId, keyword, type, storeId);
    }

    @Override
    public IPage<UserCallVO> getList(int page, int size, String userId, String keyword, Integer type, Long storeId) {
        Store store = storeService.getById(storeId);
        IPage<UserCallVO> iPage = getBaseMapper().getByUserId(new Page<>(page, size), userId, keyword, type, store.getCompanyId());
        List<UserCallVO> records = iPage.getRecords();
        if (CollectionUtils.isNotEmpty(records)) {
            List<String> userIdList = records.stream().map(UserCallVO::getUserId).filter(t-> !StringUtils.isEmpty(t)).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(userIdList)) {
                // 获取当前页用户所有的优惠券，包括待使用 已使用 和 过期
                List<UserCouponDTO> userCouponDTOList = userCouponService.getCouponByUserList(null, userIdList);
                // 按用户进行分组
                Map<String, List<UserCouponDTO>> userCouponMap = userCouponDTOList.stream().collect(Collectors.groupingBy(UserCouponDTO::getUserId));
                // 遍历用户 设置优惠券信息
                for (UserCallVO ut : records) {
                    if (ut.getLastCall() == null) {
                        ut.setLastCall(ut.getCreateDate());
                    }
                    // 获取单个用户的可用优惠券数量
                    List<UserCouponDTO> userCouponItemList = userCouponMap.get(ut.getUserId());
                    if (CollectionUtils.isNotEmpty(userCouponItemList)) {
                        ut.setUnUsedCouponNumber(userCouponItemList.stream().map(UserCouponDTO::getStatus)
                                .filter(item -> item.equals(UserCouponStatus.USABLE.getCode())).count());
                    }
                }
            }
        }
        return iPage;
    }

    @Override
    public IPage<UserCallSimpleVO> getSimpleList(int page, int size, Long userInfoId) {
        IPage<UserCallSimpleVO> iPage = getBaseMapper().getSimpleByUserId(new Page<>(page, size), userInfoId);
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            iPage.getRecords().forEach(item -> {
                if (item.getIntention() == null || item.getIntention() == 0) {
                    item.setIntentionName("未联系");
                } else if (item.getIntention() == 1) {
                    item.setIntentionName("未接通");
                } else if (item.getIntention() == 2) {
                    item.setIntentionName("无明确意向");
                } else if (item.getIntention() == 3) {
                    item.setIntentionName("有意向");
                }
            });
        }
        return iPage;
    }

    @Override
    public void distributionUserInfo(List<Long> userInfoIdList, List<String> userIdList) {
        List<List<Long>> partitionUserInfoIdList = ListUtil.averageAssign(userInfoIdList, userIdList.size());
        for (int i = 0; i < userIdList.size(); i++) {
            try {
                SportUserForm userForm = userService.getManagerUserById(userIdList.get(i));
                batchAdd(userIdList.get(i), partitionUserInfoIdList.get(i), 2);
            } catch (Exception e) {
                e.printStackTrace();
                log.error("用户:{}分配导入用户:{}个失败", userIdList.get(i), userIdList.size());
                throw e;
            }
        }
    }

    @Override
    public Boolean removeRecords(List<Long> userInfoList, String userId, String nickName) {
        return batchAddRecord(userInfoList, userId, nickName + "移出私海");
    }

    boolean batchAddRecord(List<Long> userInfoList, String userId, String introduce) {
        return saveBatch(userInfoList.stream().map(userInfoId -> {
            UserCall uc = new UserCall();
            uc.setUserId(userId);
            uc.setUserInfoId(userInfoId);
            uc.setIntroduce(introduce);
            return uc;
        }).collect(Collectors.toList()));
    }

    @Override
    public Boolean addRecords(List<Long> userInfoList, String userId, String nickName) {
        return batchAddRecord(userInfoList, userId, nickName + "添加到私海");
    }

    @Override
    public Boolean exportRecords(List<Long> userInfoList, String userId, String nickName) {
        return batchAddRecord(userInfoList, userId, nickName + "添加到公海");
    }

    @Override
    public boolean autoAddMyPool(String userPhone, String userId, Integer type, Long storeId, String nickName) {
        // 用户客资不存在 自动添加
        Store store = storeService.getById(storeId);
        UserInfo userInfo = userInfoService.getByUserPhone(userPhone, store.getCompanyId());
        if (userInfo == null) {
            userInfo = new UserInfo();
            userInfo.setCompanyId(store.getCompanyId());
            userInfo.setPhone(userPhone);
            userInfo.setStatus(1);

            userInfo.setProvinceId(store.getProvinceId());
            userInfo.setCityId(store.getCityId());
            userInfo.setRegionId(store.getRegionId());
            userInfo.setBusinessAreaId(store.getBusinessAreaId());

            userInfo.setChannelCategory("线上下单");
            userInfo.setChannelName(nickName);

            userInfoService.create(userInfo, userId, nickName + "自动添加客资");
        } else {
            Long userInfoId = userInfo.getId();
            // 先检查 客资是否归属其他人
            QueryWrapper<UserReceiveCall> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserReceiveCall::getUserInfoId, userInfoId)
                    .eq(UserReceiveCall::getType, type);
            UserReceiveCall userReceiveCall = this.userReceiveCallService.getOne(queryWrapper);
            if (userReceiveCall != null) {
                if (userReceiveCall.getUserId().equals(userId)) {
                    log.warn("客资:{}已归属当前客户, 不做处理", userPhone);
                    return true;
                }
                // 客资在其他用户池子里面 先移除
                userReceiveCallService.deleteByIds(Collections.singletonList(userReceiveCall.getId()), userReceiveCall.getUserId(), nickName);
            }
        }
        // 添加根据记录
        userInfoService.batchAddUserPool(Collections.singletonList(userInfo.getId()), userId, nickName);
        // 添加到私海池
        return addMyPool(userId, userInfo.getId(), type);
    }

    /**
     * 添加到私海池
     * @param userId
     * @param userInfoId
     * @param type
     * @return
     */
    private boolean addMyPool(String userId, Long userInfoId, Integer type) {
        UserReceiveCall item = new UserReceiveCall();
        item.setUserId(userId);
        item.setUserInfoId(userInfoId);
        item.setType(type);
        return userReceiveCallService.save(item);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchAdd(String userId, List<Long> userInfoList, Integer type) {
        QueryWrapper<UserCallPool> userCallPoolQueryWrapper = new QueryWrapper<>();
        userCallPoolQueryWrapper.lambda().eq(UserCallPool::getUserId, userId);
        UserCallPool userCallPool = userCallPoolService.getOne(userCallPoolQueryWrapper);
        if (userCallPool == null || userCallPool.getPoolSize() == 0) {
            throw new SportException("您无可用数量, 无法添加");
        }
        if (userCallPool.getPoolSize() < userInfoList.size()) {
            throw new SportException("当前私海池只剩下" + userCallPool.getPoolSize() + ", 您选择数量超过可用数量");
        }
        userCallPool.setPoolSize(userCallPool.getPoolSize() - userInfoList.size());
        User user = userService.getById(userId);
        // 添加用户跟进记录
        boolean result = userInfoService.batchAddUserPool(userInfoList, userId, user.getName());

        List<UserReceiveCall> userCalls = new ArrayList<>();
        for (Long id : userInfoList) {
            QueryWrapper<UserReceiveCall> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserReceiveCall::getUserId, userId)
                    .eq(UserReceiveCall::getUserInfoId, id)
                    .eq(UserReceiveCall::getType, type);
            UserReceiveCall userReceiveCall = this.userReceiveCallService.getOne(queryWrapper);
            if (userReceiveCall != null) {
                UserInfo userInfo = this.userInfoService.getById(id);
                throw new SportException(String.format("用户%s,%s已被添加进%s中", userInfo.getName(), userInfo.getPhone(), type == 1 ? "私海池" : "客户池"));
            }
            UserReceiveCall item = new UserReceiveCall();
            item.setUserId(userId);
            item.setUserInfoId(id);
            item.setType(type);
            userCalls.add(item);
        }
        // 批量添加到私海池
        boolean userReceiveResult = userReceiveCallService.saveBatch(userCalls);
        // 更新用户私海池数量
        if (result && userReceiveResult && userCallPoolService.updateById(userCallPool)) {
            return true;
        }
        throw new SportException("添加失败!");
    }

    @Override
    public boolean batchUpdateStatus(List<Long> userCallIds) {
        List<UserCall> userCalls = listByIds(userCallIds);
        for (UserCall item : userCalls) {
            item.setStatus(2);
        }
        List<Long> userInfoIds = userCalls.stream().map(UserCall::getUserInfoId).collect(Collectors.toList());
        boolean result = userInfoService.updateBatchById(userInfoIds.stream().map(id -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setStatus(1);
            return userInfo;
        }).collect(Collectors.toList()));
        if (result && updateBatchById(userCalls)) {
            return true;
        }
        throw new SportException("数据操作失败!");
    }

    @Override
    public boolean updateUserCall(UserCallUpdateForm form) {
        UserCall uc = getById(form.getId());
        BeanUtils.copyProperties(form, uc);
        return updateById(uc);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean userCallFollow(UserCallFollow userCallFollow) {
        UserCall userCall = new UserCall();
        BeanUtils.copyProperties(userCallFollow, userCall);
        if (save(userCall)) {
            List<String> tagList = userCallFollow.getTags();
            if (CollectionUtils.isNotEmpty(tagList)) {
                userTagService.saveBatch(tagList.stream().map(tag -> {
                    UserTag userTag = new UserTag();
                    userTag.setName(tag);
                    userTag.setUserInfoId(userCallFollow.getUserInfoId());
                    return userTag;
                }).collect(Collectors.toList()));
            }
            if (userInfoService.updateLastCallDate(userCallFollow.getUserInfoId(), new Date())) {
                return true;
            }
        }
        throw new SportException("数据保存失败!");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserReceiveCalls(String userId, String phone, String coachUserId, String salesUserId, String companyId) {
        if(StringUtils.isEmpty(coachUserId) && StringUtils.isEmpty(salesUserId)){
            throw new SportException("参数异常");
        }
        QueryWrapper<UserInfo> queryWrapper1 = new QueryWrapper<>();
        queryWrapper1.lambda().eq(UserInfo::getCompanyId, companyId)
                .eq(UserInfo::getPhone, phone);

        UserInfo userInfo = userInfoService.getOne(queryWrapper1);
        if (userInfo == null){
            throw new SportException("未在公海中");
        }
        //先删除原有的私海池、销售池的归属
        List<Integer> types = new ArrayList<>();
        if (StringUtils.isNotEmpty(coachUserId)){
            types.add(1);
        }
        if (StringUtils.isNotEmpty(salesUserId)){
            types.add(2);
        }
        QueryWrapper<UserReceiveCall> queryWrapper2 = new QueryWrapper<>();
        queryWrapper2.lambda().eq(UserReceiveCall::getUserInfoId, userInfo.getId())
                .in(UserReceiveCall::getType, types);
        List<UserReceiveCall> list = userReceiveCallService.list(queryWrapper2);
        if (list != null && list.size()>0){
            List coachList=list.stream().filter(item -> item.getType()==1).map(UserReceiveCall::getId).collect(Collectors.toList());
            List salesList=list.stream().filter(item -> item.getType()==2).map(UserReceiveCall::getId).collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(coachList)){
                userReceiveCallService.justDeleteAndUpdateCount(coachList, coachUserId);
            }
            if (CollectionUtils.isNotEmpty(salesList)){
                userReceiveCallService.justDeleteAndUpdateCount(salesList, salesUserId);
            }
        }
        //插入新的私海池、销售池的归属
        List<Long> userInfoList = new ArrayList<>();
        userInfoList.add(userInfo.getId());

        if (StringUtils.isNotEmpty(coachUserId)){
            batchAdd(coachUserId,userInfoList,1);
        }
        if (StringUtils.isNotEmpty(salesUserId)){
            batchAdd(salesUserId,userInfoList,2);
        }
        return true;
    }
}
