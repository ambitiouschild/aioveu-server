package com.aioveu.service.impl;

import com.alibaba.excel.EasyExcel;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.UserInfoDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.ExerciseCategory;
import com.aioveu.enums.OrderStatus;
import com.aioveu.excel.bean.UserInfoCallBean;
import com.aioveu.excel.bean.UserOrderBean;
import com.aioveu.excel.listener.UserInfoCallListener;
import com.aioveu.excel.listener.UserOrderListener;
import com.aioveu.exception.SportException;
import com.aioveu.form.OrderForm;
import com.aioveu.service.*;
import com.aioveu.utils.SportDataUtils;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoDao, UserInfo> implements UserInfoService {

    @Autowired
    private ProvinceService provinceService;

    @Autowired
    private CityService cityService;

    @Autowired
    private RegionService regionService;

    @Autowired
    private BusinessAreaService businessAreaService;

    @Autowired
    private UserTagService userTagService;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private WaterPoolSaleGroupService waterPoolSaleGroupService;

    @Autowired
    private UserCallService userCallService;

    @Autowired
    private UserInfoPublicService userInfoPublicService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ExerciseCouponService exerciseCouponService;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private StoreService storeService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String importData(MultipartFile multipartFile, Long companyId, String username) {
        InputStream inputStream = null;
        try {
            inputStream = multipartFile.getInputStream();
            String userId = userService.getUserIdFromCache(username);
            // 解析每行结果在listener中处理
            UserInfoCallListener listener = new UserInfoCallListener();
            listener.setSuccessCount(0);
            listener.setCompanyId(companyId);
            listener.setUserId(userId);
            listener.setUserInfoService(this);
            EasyExcel.read(inputStream, UserInfoCallBean.class, listener).sheet().doRead();
            return "导入成功" + listener.getSuccessCount() + "条数据!";
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new SportException("数据导入失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSave(List<UserInfoCallBean> list, String userId, Integer type) {
        if (CollectionUtils.isEmpty(list)) {
            throw new SportException("未解析到数据");
        }
        List<UserInfoBase> userInfoBaseList = new ArrayList<>();
        for (UserInfoCallBean item : list) {
            UserInfoBase userInfoBase = null;
            if (type == 0) {
                userInfoBase = new UserInfo();
                BeanUtils.copyProperties(item, userInfoBase);
            } else {
                userInfoBase = new UserInfoPublic();
            }

            if (StringUtils.isEmpty(item.getChildSex()) || item.getChildSex().equals("未知")) {
                userInfoBase.setChildGender(0);
            } else if (item.getChildSex().equals("男")) {
                userInfoBase.setChildGender(1);
            } else if (item.getChildSex().equals("女")) {
                userInfoBase.setChildGender(2);
            }
            supplementPosition(userInfoBase);
            userInfoBaseList.add(userInfoBase);
        }
        if (type == 0) {
            List<UserInfo> userInfoList = userInfoBaseList.stream().map(item -> {
                UserInfo userInfo = new UserInfo();
                BeanUtils.copyProperties(item, userInfo);
                return userInfo;
            }).collect(Collectors.toList());
            try {
                saveBatch(userInfoList);
            }catch (DuplicateKeyException e) {
                throw new SportException("手机号码重复!");
            }

            for (int i = 0; i < userInfoList.size(); i++) {
                UserInfo userInfo = userInfoList.get(i);
                userInfoBaseList.get(i).setId(userInfo.getId());
            }
        } else {
            List<UserInfoPublic> userInfoList = userInfoBaseList.stream().map(item -> {
                UserInfoPublic userInfo = new UserInfoPublic();
                BeanUtils.copyProperties(item, userInfo);
                return userInfo;
            }).collect(Collectors.toList());
            userInfoPublicService.saveBatch(userInfoList);
            for (int i = 0; i < userInfoList.size(); i++) {
                UserInfoPublic userInfo = userInfoList.get(i);
                userInfoBaseList.get(i).setId(userInfo.getId());
            }
        }
        Long companyId = userInfoBaseList.get(0).getCompanyId();
        Map<String, List<Long>> groupUser = new HashMap<>();
        List<Long> userInfoIds = new ArrayList<>();
        for (UserInfoBase item : userInfoBaseList) {
            if (StringUtils.isNotEmpty(item.getGroupName())) {
                List<Long> userInfoIdList = groupUser.computeIfAbsent(item.getGroupName(), k -> new ArrayList<>());
                userInfoIdList.add(item.getId());
            }
            userInfoIds.add(item.getId());
        }
        // 添加导入说明
        userCallService.exportRecords(userInfoIds, userId, "");
        if (groupUser.size() > 0) {
            // 导入数据 存在分组
            for (Map.Entry<String, List<Long>> item : groupUser.entrySet()) {
                // 查询组下面的用户
                List<String> userIdList = waterPoolSaleGroupService.getGroupUserIdByGroupName(item.getKey(), companyId);
                if (CollectionUtils.isNotEmpty(userIdList)) {
                    // 分配公海用户给销售
                    userCallService.distributionUserInfo(item.getValue(), userIdList);
                }
            }
        }
        return true;
    }

    /**
     * 补全位置信息id
     * @param userInfo
     */
    private void supplementPosition(UserInfoBase userInfo) {
        if (StringUtils.isNotEmpty(userInfo.getProvinceName())) {
            userInfo.setProvinceId(provinceService.getByName(userInfo.getProvinceName()));
        }
        if (StringUtils.isNotEmpty(userInfo.getCityName())) {
            userInfo.setCityId(cityService.getByName(userInfo.getCityName()));
        }
        if (StringUtils.isNotEmpty(userInfo.getRegionName())) {
            userInfo.setRegionId(regionService.getByName(userInfo.getRegionName()));
        }
        if (StringUtils.isNotEmpty(userInfo.getBusinessAreaName())) {
            userInfo.setBusinessAreaId(businessAreaService.getByName(userInfo.getBusinessAreaName()));
        }
    }

    @Override
    public IPage<UserInfoVO> getList(int page, int size, Long companyId, Integer type, String username, String phone) {
        String userId = userService.getUserIdFromCache(username);
        List<String> roleList = roleUserService.getByUserId(userId);
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfo::getCompanyId, companyId);
        if (CollectionUtils.isNotEmpty(roleList) && roleList.contains("business_admin")) {

        } else {
            queryWrapper.lambda().eq(UserInfo::getStatus, DataStatus.NORMAL.getCode());
        }
        queryWrapper.lambda().orderByDesc(UserInfo::getCreateDate);

        IPage<UserInfo> userInfoIPage = getBaseMapper().userInfoList(new Page<>(page, size), type, companyId, phone);
        //查询结果
        List<UserInfo> list = userInfoIPage.getRecords();
        List<UserInfoVO> records = list.stream().map(item -> {
            UserInfoVO userInfoVO = new UserInfoVO();
            BeanUtils.copyProperties(item, userInfoVO);
            userInfoVO.setPhone(SportDataUtils.phoneHide(userInfoVO.getPhone()));
            return userInfoVO;
        }).collect(Collectors.toList());

        IPage<UserInfoVO> iPage = new Page<>();
        BeanUtils.copyProperties(userInfoIPage, iPage);
        iPage.setRecords(records);
        return iPage;
    }

    @Override
    public UserInfoDetailVO detail(Long id) {
        UserInfo userInfo = getById(id);

        //根据状态判断是否是公海 1是公海 2是私海 是公海处理手机号码
        if (userInfo.getStatus()==1){
            userInfo.setPhone(SportDataUtils.phoneHide(userInfo.getPhone()));
        }
        UserInfoDetailVO userInfoDetailVO = new UserInfoDetailVO();
        // 客户基本信息设置
        UserBaseInfoVO userBaseInfo = new UserBaseInfoVO();
        BeanUtils.copyProperties(userInfo, userBaseInfo);
        userBaseInfo.setParentName(userInfo.getName());
        userBaseInfo.setChannel(userInfo.getChannelName());
        userBaseInfo.setProvinceId(userInfo.getProvinceId());
        userBaseInfo.setCityId(userInfo.getCityId());
        userBaseInfo.setRegionId(userInfo.getRegionId());
        userBaseInfo.setBusinessAreaId(userInfo.getBusinessAreaId());
        userInfoDetailVO.setUserBaseInfo(userBaseInfo);

        List<UserTag> userTagList = userTagService.getByUserInfoId(id);

        userBaseInfo.setTags(userTagList.stream().map(UserTag::getName).collect(Collectors.toList()));
        // 客户主要信息设置
        userInfoDetailVO.setPhone(userInfo.getPhone());
        userInfoDetailVO.setId(id);
        userInfoDetailVO.setName(userInfo.getName());
        userInfoDetailVO.setCreateDate(userInfo.getCreateDate());
        userInfoDetailVO.setStatus(userInfo.getStatus());

        // 已激活 代表 已注册
        // 首单 代表已下单购买
        List<String> userInfoTagList = new ArrayList<>();
        String phone = userInfo.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            //通过手机号码查找用户
            User user = userService.getByUserPhone(phone);
            if (user != null) {
                List<Order> orderList = orderService.getByUserId(user.getId());
                if (CollectionUtils.isNotEmpty(orderList)) {
                    long usefulOrder = orderList.stream().filter(item -> item.getStatus().equals(OrderStatus.PAY.getCode()) ||
                            item.getStatus().equals(OrderStatus.USING.getCode()) ||
                            item.getStatus().equals(OrderStatus.ORDER_FINISH.getCode()) ||
                            item.getStatus().equals(OrderStatus.USED.getCode())).count();
                    if (usefulOrder == 1) {
                        userInfoTagList.add("首单");
                    } else if (usefulOrder == 0) {
                        userInfoTagList.add("已激活");
                    } else {
                        userInfoTagList.add("多单");
                    }
                } else {
                    userInfoTagList.add("已激活");
                }
            } else {
                userInfoTagList.add("未激活");
            }
        } else {
            userInfoTagList.add("无手机号");
        }
        userInfoDetailVO.setTags(userInfoTagList);
        try {
            userInfoDetailVO.setRecoveryDate(DateUtils.parseDate("2022-01-20", "yyyy-MM-dd"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return userInfoDetailVO;
    }

    @Override
    public IPage<UserInfoOrderVO> orderList(int page, int size, String phone) {
        User user = userService.getByUserPhone(phone);
        if (user != null) {
            return getBaseMapper().orderList(new Page<>(page, size), user.getId(), categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode()));
        }
        return new Page<>();
    }

    @Override
    public IPage<UserInfoOrderVO> appointmentList(int page, int size, String phone) {
        User user = userService.getByUserPhone(phone);
        if (user != null) {
            return getBaseMapper().appointmentList(new Page<>(page,size), user.getId(), categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode()));
        }
        return new Page<>();
    }

    @Override
    public boolean updateLastCallDate(Long id, Date lastCall) {
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        userInfo.setLastCall(lastCall);
        return updateById(userInfo);
    }

    @Override
    public UserInfoEditVO edit(Long id) {
        UserInfo userInfo = getById(id);
        UserInfoEditVO userInfoEditVO = new UserInfoEditVO();
        BeanUtils.copyProperties(userInfo, userInfoEditVO);
        return userInfoEditVO;
    }

    @Override
    public String getUserIdByPhoneAndCompanyId(String phone, Long companyId) {
        return getBaseMapper().getUserIdByPhoneAndCompanyId(phone, companyId,1);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean create(UserInfo userInfo, String userId, String introduce) {
        if (userInfo.getCompanyId() == null) {
            throw new SportException("companyId不能为空!");
        }
        if (StringUtils.isEmpty(userInfo.getPhone())) {
            throw new SportException("手机号码不能为空!");
        }
        userInfo.setCreateUserId(userId);
        supplementPosition(userInfo);
        try {
            if (save(userInfo)) {
                String groupName = userInfo.getGroupName();
                String groupUserId = userInfo.getGroupUserId();
                if (StringUtils.isNotEmpty(groupUserId)) {
                    // 分配公海用户给销售
                    List<Long> userInfoIdList = new ArrayList<>();
                    userInfoIdList.add(userInfo.getId());
                    List<String> userIdList = new ArrayList<>();

                    userIdList.add(groupUserId);
                    userCallService.distributionUserInfo(userInfoIdList, userIdList);
                } else if (StringUtils.isNotEmpty(groupName)) {
                    // 查询组下面的用户
                    List<String> userIdList = waterPoolSaleGroupService.getGroupUserIdByGroupName(groupName, userInfo.getCompanyId());
                    if (CollectionUtils.isNotEmpty(userIdList)) {
                        // 分配公海用户给销售
                        List<Long> userInfoIdList = new ArrayList<>();
                        userInfoIdList.add(userInfo.getId());
                        Collections.shuffle(userIdList);
                        userCallService.distributionUserInfo(userInfoIdList, userIdList);
                    }
                }
                UserCall userCall = new UserCall();
                userCall.setUserId(userId);
                userCall.setUserInfoId(userInfo.getId());
                userCall.setIntroduce(introduce);
                userCallService.save(userCall);
                return true;
            }
        }catch (Exception e) {
            if (e instanceof DuplicateKeyException) {
                throw new SportException(userInfo.getPhone() + "重复!");
            }
            throw e;
        }
        return false;
    }

    @Override
    public UserInfo getByUserPhone(String phone, Long companyId) {
        QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(UserInfo::getPhone, phone)
                .eq(UserInfo::getCompanyId, companyId);
        return this.getOne(queryWrapper);
    }

    @Override
    public UserInfoCreateOrderVO getByPhone(String phone, Long companyId) {
        UserInfo userInfo = getByUserPhone(phone, companyId);
        if (userInfo != null) {
            UserInfoCreateOrderVO userInfoCreateOrderVO = new UserInfoCreateOrderVO();
            BeanUtils.copyProperties(userInfo, userInfoCreateOrderVO);
            return userInfoCreateOrderVO;
        }
        return null;
    }

    @Override
    public boolean updateUserAndChildNameAndAge(String phone, String username, String childName, Integer childAge, Long companyId) {
        UpdateWrapper<UserInfo> queryWrapper = new UpdateWrapper<>();
        queryWrapper.lambda()
                .set(UserInfo::getChildName, childName)
                .set(UserInfo::getChildAge, childAge)
                .set(UserInfo::getName, username)
                .eq(UserInfo::getPhone, phone)
                .eq(UserInfo::getCompanyId, companyId);
        return update(queryWrapper);
    }

    @Override
    public boolean batchRemove(List<Long> userInfoList, String userId, String nickName) {
        if (changeUserInfoStatus(userInfoList, DataStatus.NORMAL.getCode())) {
            return userCallService.removeRecords(userInfoList, userId, nickName);
        }
        return false;
    }

    @Override
    public boolean batchAddUserPool(List<Long> userInfoList, String userId, String nickName) {
        return userCallService.addRecords(userInfoList, userId, nickName);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchOrderSave(List<UserOrderBean> list) {
        List<UserInfoBase> userInfoBaseList = new ArrayList<>();
        for (UserOrderBean ub : list) {
            QueryWrapper<UserInfo> queryWrapper = new QueryWrapper<>();
            queryWrapper.lambda().eq(UserInfo::getPhone, ub.getPhone())
                    .eq(UserInfo::getCompanyId, ub.getCompanyId());
            if (count(queryWrapper) == 0) {
                UserInfoBase userInfoBase = new UserInfo();
                BeanUtils.copyProperties(ub, userInfoBase);

                if (StringUtils.isEmpty(ub.getChildSex()) || ub.getChildSex().equals("未知")) {
                    userInfoBase.setChildGender(0);
                } else if (ub.getChildSex().equals("男")) {
                    userInfoBase.setChildGender(1);
                } else if (ub.getChildSex().equals("女")) {
                    userInfoBase.setChildGender(2);
                }
                supplementPosition(userInfoBase);
                userInfoBaseList.add(userInfoBase);
            }
            User user = userService.getByUserPhone(ub.getPhone());
            if (user != null) {
                ub.setUserId(user.getId());
            } else {
                ub.setUserId(userService.createByPhone(ub.getPhone(), ub.getName()));
            }
            OrderForm form = new OrderForm();
            form.setProductId(ub.getProductId() + "");
            form.setPhoneList(ub.getPhone());
            form.setUserId(ub.getUserId());
            form.setRemark("系统导入订单");
            Order order = orderService.createFinishOrder(form);

            exerciseCouponService.sendCoupon(ub.getCouponTemplateId(), ub.getCouponCount(), ub.getUserId(),
                    ub.getProductId(), order.getCategoryId(), order.getAmount(), order.getId());
        }
        List<UserInfo> userInfoList = userInfoBaseList.stream().map(item -> {
            UserInfo userInfo = new UserInfo();
            BeanUtils.copyProperties(item, userInfo);
            return userInfo;
        }).collect(Collectors.toList());
        return saveBatch(userInfoList);
    }

    @Override
    public Integer addUserInfoNumber() {
        if (!redisTemplate.hasKey("userInfoNumber")){
            redisTemplate.opsForValue().set("userInfoNumber",60000);
        }
        Integer userInfoNumber = (Integer) redisTemplate.opsForValue().get("userInfoNumber");
        userInfoNumber += new Random().nextInt(10)+1;
        redisTemplate.opsForValue().set("userInfoNumber",userInfoNumber);
        return userInfoNumber;
    }

    /**
     * 修改用户信息状态
     * @param userInfoList
     * @return
     */
    private boolean changeUserInfoStatus(List<Long> userInfoList, int status) {
        return updateBatchById(userInfoList.stream().map(id -> {
            UserInfo userInfo = new UserInfo();
            userInfo.setId(id);
            userInfo.setStatus(status);
            return userInfo;
        }).collect(Collectors.toList()));
    }

    @Override
    public void importOrder(MultipartFile file, Long companyId) {
        InputStream inputStream = null;
        try {
            inputStream = file.getInputStream();
            // 解析每行结果在listener中处理
            UserOrderListener listener = new UserOrderListener();
            listener.setCompanyId(companyId);
            listener.setUserInfoService(this);
            EasyExcel.read(inputStream, UserOrderBean.class, listener).sheet().doRead();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        throw new SportException("数据导入失败！");
    }

    @Override
    public String getUserId(Long id) {
        return getBaseMapper().getUserId(id);
    }

    @Override
    public String getUserIdByIdAndCompanyId(String userId, Long companyId) {
        return getBaseMapper().getUserIdByIdAndCompanyId(userId,companyId,1);
    }

    @Override
    public boolean updateChildNameByPhoneAndCompanyId(String phone, Long companyId, String childName) {
        UpdateWrapper<UserInfo> updateWrapper = new UpdateWrapper<>();
        updateWrapper.lambda().set(UserInfo::getChildName, childName)
                .eq(UserInfo::getPhone, phone)
                .eq(UserInfo::getCompanyId, companyId);
        return update(updateWrapper);
    }
}
