package com.aioveu.service.impl;

import cn.hutool.core.lang.UUID;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.PhoneCodeConstant;
import com.aioveu.constant.ResultEnum;
import com.aioveu.constant.SysDictConstant;
import com.aioveu.dao.UserDao;
import com.aioveu.entity.*;
import com.aioveu.enums.DataStatus;
import com.aioveu.enums.StoreCoachTypeEnum;
import com.aioveu.enums.UserCouponStatus;
import com.aioveu.exception.SportException;
import com.aioveu.feign.WxMaUserClient;
import com.aioveu.feign.form.TransferMoneyForm;
import com.aioveu.form.UserCashForm;
import com.aioveu.service.*;
import com.aioveu.utils.FileUtil;
import com.aioveu.utils.IdUtils;
import com.aioveu.utils.JacksonUtils;
import com.aioveu.vo.*;
import com.aioveu.vo.user.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import static cn.hutool.core.date.DateTime.now;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserDao, User> implements UserService {

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    @Resource
    private WxMaUserClient wxMaUserClient;

    @Autowired
    private IUserCouponService iUserCouponService;

    @Autowired
    private UserBalanceChangeService userBalanceChangeService;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private Environment environment;

    @Autowired
    private CompanyStoreUserService companyStoreUserService;

    @Autowired
    private CompanyService companyService;

    @Autowired
    @Resource
    private UserCoachService userCoachService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private UserDao userDao;

    @Autowired
    private WaterPoolSaleGroupUserService waterPoolSaleGroupUserService;

    @Autowired
    private CashOrderService cashOrderService;

    @Autowired
    private UserExtensionAccountService userExtensionAccountService;

    @Autowired
    private StoreCoachService storeCoachService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private StoreConfigService storeConfigService;

    @Autowired
    private StoreService storeService;


    private String generateRandomString(int length) {
        // 确保 RandomStringUtils 正确初始化且生成非空字符串
        return RandomStringUtils.randomAlphanumeric(length)
                .toLowerCase();
    }


    public class Constants {
        public static final String TEMP_USER_NAME = "临时账户";
        public static final String ROLE_CODE_ADMIN_ADVANCED = "admin_advanced";
    }

    //如果 tempAccounts 或 createQuShuTempAccounts 涉及多个数据库操作，需确保事务正确提交。
    @Transactional
    public User tempAccounts(String tempPw) {
        // 生成凭证
        String username = generateRandomString(6);
        System.out.println("Generated username: " + username);  // 检查生成值
        String encryptedPassword = passwordEncoder.encode(tempPw);
        String phone = "00" + RandomStringUtils.randomNumeric(9);
        System.out.println("Encrypted password: " + encryptedPassword);

        // 创建账号
        User user = new User();
        user.setName(Constants.TEMP_USER_NAME);
        user.setUsername(username);
        user.setPassword(encryptedPassword);
        user.setPhone(phone);
        user.setCreateDate(now());
        userService.save(user);

        List<Store> stores = storeService.getByAPPId("wxe267b90015e11ac8");
        Store store = stores.stream()
                .findFirst()  // 取第一个元素
                .orElseThrow(() -> new RuntimeException("店铺未找到"));
//        BeanUtils.copyProperties(stores.get(0), store);  // 复制同名属性

        // 添加角色
        RoleUser roleUser = new RoleUser();
        roleUser.setRoleCode(Constants.ROLE_CODE_ADMIN_ADVANCED);
        roleUser.setUserId(user.getId());
        roleUser.setStoreId(store.getId());
        roleUser.setCompanyId(store.getCompanyId());
        roleUserService.saveUserRole(roleUser);

        // 店铺授权
        CompanyStoreUser companyStoreUser = new CompanyStoreUser();
        companyStoreUser.setUserId(user.getId());
        companyStoreUser.setStoreId(store.getId());
        companyStoreUser.setCompanyId(store.getCompanyId());
        companyStoreUserService.create(companyStoreUser);

        return user;
    }

    /**
     * web端创建临时账户
     * @param
     * @return
     */
    @Override
    @Transactional
    public Map<String, String> createQuShuTempAccounts() {
        String rawPassword = generateRandomString(6);
        User user = tempAccounts(rawPassword);
        log.info("创建临时账户 username={}, password={}", user.getUsername(),rawPassword);
        if (user == null || user.getId() == null) {
            throw new SportException("账号创建异常");
        }
        Map<String, String> result = new HashMap<>();
        result.put("username", user.getUsername());
        result.put("password", rawPassword);
        return result;
    }

    @Override
    public boolean webManagerCreateOrUpdate(SportUserForm sportUserForm) {
        User user = new User();
        BeanUtils.copyProperties(sportUserForm, user);
        if (sportUserForm.getId() == null) {
            User oldUser = getByUserPhone(sportUserForm.getPhone());
            if (oldUser != null) {
                throw new SportException("该手机号码已注册");
            }
            oldUser = findUserByUsername(sportUserForm.getUsername());
            if (oldUser != null) {
                throw new SportException("该用户名已注册");
            }
            user.setPassword(passwordEncoder.encode(sportUserForm.getPassword()));
            user.setStatus(DataStatus.NORMAL.getCode());

            if (StringUtils.isEmpty(user.getHead())) {
                user.setHead("default/head_boy.png");
            }
            // 新增用户 角色保存
            if (CollectionUtils.isNotEmpty(sportUserForm.getRoleCodes()) && sportUserForm.getId() == null) {
                List<RoleUser> roleUserList = new ArrayList<>(sportUserForm.getRoleCodes().size());
                for (String code : sportUserForm.getRoleCodes()) {
                    RoleUser roleUser = new RoleUser();
                    roleUser.setRoleCode(code);
                    roleUser.setUserId(user.getId());
                    roleUser.setStoreId(0L);
                    roleUser.setCompanyId(0L);
                    roleUserList.add(roleUser);
                }
                roleUserService.saveBatch(roleUserList);
            }
        } else {
            user.setPassword(null);
        }
        return saveOrUpdate(user);
    }

    @Override
    public boolean resetPassword(String id, String password) {
        User user = new User();
        user.setId(id);
        user.setPassword(passwordEncoder.encode(password));
        return saveOrUpdate(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVo create(BaseUserForm baseUserForm) {
        log.info(baseUserForm.toString());
        if ("null".equals(baseUserForm.getPhone()) ||
                "undefined".equals(baseUserForm.getPhone())) {
            log.error("手机号码错误");
            throw new SportException("系统注册异常");
        }
        User user = getByUserPhone(baseUserForm.getPhone());
        UserVo userVo = new UserVo();
        if (user == null) {
            log.info("新用户注册:" + JacksonUtils.obj2Json(baseUserForm));
            user = new User();
            BeanUtils.copyProperties(baseUserForm, user);
            if (count(new QueryWrapper<User>().lambda().eq(User::getUsername, user.getUsername())) > 0) {
                throw new SportException("用户名重复");
            }
            user.setPassword(passwordEncoder.encode(baseUserForm.getPassword()));
            user.setStatus(DataStatus.NORMAL.getCode());

            // 用于注册完自动登录的
            String code = IdUtils.get4NumberCode();
            userVo.setCode(code);
            ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
            valueOperations.set(PhoneCodeConstant.SPORT_PHONE_CODE + PhoneCodeConstant.CODE_TYPE_LOGIN + baseUserForm.getPhone(), code, 30, TimeUnit.MINUTES);
        }
        if (StringUtils.isEmpty(user.getHead())) {
            user.setHead("default/head_boy.png");
        }
        saveOrUpdate(user);
        BeanUtils.copyProperties(user, userVo);

        if (baseUserForm instanceof UserForm) {
            UserForm userForm = (UserForm) baseUserForm;
            userVo.setOpenId(userForm.getOpenId());
            if (!userOpenIdService.addOrUpdate(user.getId(), userForm.getAppId(), userForm.getOpenId(), userForm.getUnionId())) {
                log.error("注册失败:{}", JacksonUtils.obj2Json(userForm));
                throw new SportException("注册失败");
            }
        }
        return userVo;
    }

    @Override
    public UserVo findByUsername(String username) {
        return findByUsernameAndAppId(username, null);
    }

    @Override
    public UserVo findByUsernameAndAppId(String username, String appId) {
        if (StringUtils.isEmpty(appId)) {
            appId = "wxbb59a459db54ac20";
        }
        User user = findUserByUsername(username);
        UserVo userVo = getRoleUserVo(user);
        userVo.setOpenId(userOpenIdService.getByAppIdAndUserId(user.getId(), appId, false));
        userVo.setHead(FileUtil.getImageFullUrl(user.getHead()));
        return userVo;
    }

    /**
     * 通过用户名查找用户
     *
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        return getOne(getQueryWrapper().eq("username", username));
    }

    @Override
    public User getByUserPhone(String phone) {
        return getOne(getQueryWrapper().eq("phone", phone));
    }


    private QueryWrapper<User> getQueryWrapper() {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", 1);
        return queryWrapper;
    }

    @Override
    public UserVo getByIdAndProvider(String openId, String providerId) {
        QueryWrapper<User> queryWrapper = getQueryWrapper();
        User user;
        if (providerId == null || providerId.equals("openId")) {
            queryWrapper.eq("open_id", openId);
            user = getOne(queryWrapper);
        } else if (providerId.equals("unionId")) {
            queryWrapper.eq("union_id", openId);
            user = getOne(queryWrapper);
        } else if (providerId.equals("appId")) {
            user = getBaseMapper().getFromUserOpenId(openId);
        } else {
            log.error("providerId:{}参数错误", providerId);
            throw new SportException("providerId参数错误");
        }
        if (user == null) {
            throw new SportException("用户不存在");
        }
        return user.buildInfo();
    }

    @Override
    public UserVo getByUserId(String id) {
        User user = getUserByUerId(id);
        return user.buildInfo();
    }

    private User getUserByUerId(String id) {
        User user = getById(id);
        if (user == null) {
            throw new SportException("用户不存在");
        }
        return user;
    }

    @Override
    public IPage<UserItemVO> getAll(int page, int size, String role, String phone, String keyword) {
        if (StringUtils.isEmpty(phone) || "null".equals(phone)) {
            phone = null;
        }
        if (StringUtils.isEmpty(keyword) || "null".equals(keyword)) {
            keyword = null;
        }
        return getBaseMapper().list(new Page<>(page, size), role, phone, keyword);
    }

    @Override
    public SportUserForm getManagerUserById(String id) {
        User user = getUserByUerId(id);
        SportUserForm sportUserForm = new SportUserForm();
        BeanUtils.copyProperties(user, sportUserForm);
        sportUserForm.setRoleCodes(roleUserService.getByUserId(id));
        return sportUserForm;
    }

    @Override
    public UserVo checkRegister(Integer checkType, String openId, String unionId) {
        return getByIdAndProvider(openId, checkType == 0 ? "openId" : "appId");
    }

    private UserVo getRoleUserVo(User user) {
        if (user == null) {
            throw new SportException("用户不存在");
        }
        UserVo userVo = user.buildInfo();
        userVo.setRoles(roleUserService.getByUserId(user.getId()));
        return userVo;
    }

    @Override
    public UserRealTimeVO getRealTimeByUsername(String username, Long companyId) {
        User user = getOne(getQueryWrapper().eq("username", username));
        UserRealTimeVO userRealTimeVO = new UserRealTimeVO();
        BeanUtils.copyProperties(user, userRealTimeVO);

        List<UserCoupon> userCouponList = iUserCouponService.findUserCouponsByStatus(user.getId(), companyId, UserCouponStatus.USABLE.getCode());
        if (userCouponList != null) {
            userRealTimeVO.setCouponCount(userCouponList.size());
        } else {
            userRealTimeVO.setCouponCount(0);
        }
        userRealTimeVO.setOrderNumber(orderService.getUnUseOrderNumber(user.getId(), companyId));
        return userRealTimeVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean userCash(UserCashForm userCashForm) {
        User user = getUserByUerId(userCashForm.getUserId());
        if (user.getBalance().doubleValue() < userCashForm.getMoney().doubleValue()) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "提现金额不能大于余额");
        }
        user.setBalance(user.getBalance().subtract(userCashForm.getMoney()));
        user.setTotalIncome(user.getTotalIncome().add(userCashForm.getMoney()));
        if (updateById(user)) {
            userBalanceChangeService.reduceBalanceRecord(userCashForm.getUserId(), userCashForm.getMoney(), null,
                    "用户提现", "提现金额" + userCashForm.getMoney(), 0);
            if (cashOrderService.create(userCashForm.getUserId(), "提现申请", userCashForm.getMoney(), userCashForm.getAppId())) {
                TransferMoneyForm form = new TransferMoneyForm();
                form.setAppId(userCashForm.getAppId());
                form.setDescription("用户余额提现");
                form.setFullName(userCashForm.getFullName());
                String openId = userOpenIdService.getByAppIdAndUserId(user.getId(), userCashForm.getAppId(), true);
                form.setMiniOpenId(openId);
                form.setMoney(userCashForm.getMoney());
                form.setPartnerTradeNo(IdUtils.getStrNumberId("TD"));
                CommonResponse<Boolean> response = wxMaUserClient.transferMoney(form);
                if (response.isSuccess() && response.getData()) {
                    userBalanceChangeService.reduceBalanceRecord(user.getId(), userCashForm.getMoney(), form.getPartnerTradeNo(), "用户提现",
                            "提现金额" + userCashForm.getMoney().doubleValue(), 0);
                    return true;
                } else {
                    throw new SportException(response.getCode(), response.getMessage());
                }
            }
        }
        throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "提现申请失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addBalance(String userId, BigDecimal amount, String name, String description, String orderId) {
        User user = getById(userId);
        user.setBalance(user.getBalance().add(amount));
        user.setTotalBalance(user.getTotalBalance().add(amount));
        if (updateById(user) && userBalanceChangeService.addBalanceRecord(userId, amount, orderId, name, description, 0)) {
            return true;
        }
        throw new SportException(userId + "余额操作失败！");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reduceBalance(String userId, BigDecimal amount, String name, String description, String orderId, boolean negative) {
        User user = getById(userId);
        user.setBalance(user.getBalance().subtract(amount));
        if (!negative && user.getBalance().doubleValue() < 0) {
            throw new SportException("当前用户余额不足！");
        }
        if (updateById(user) && userBalanceChangeService.reduceBalanceRecord(userId, amount, orderId, name, description, 0)) {
            return true;
        }
        throw new SportException(userId + "余额操作失败！");
    }

    @Override
    public String getUserIdFromCache(String username) {
        //TODO 后面用Redis缓存下
        User user = findUserByUsername(username);
        if (user != null) {
            return user.getId();
        }
        throw new SportException("用户不存在！");
    }

    /**
     * 修改用户的信息 （手机号） 可修改全部信息
     *
     * @param username 用户名
     * @param newPhone 新手机号码
     * @param code     验证码
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserPhone(String username, String newPhone, String code) {
        if (StringUtils.isEmpty(code)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "验证码不能为空!");
        }
        // 对新手机号码进行效验，不能是库里已存在的手机号码
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getPhone, newPhone);
        User u = this.getOne(queryWrapper);
        log.info("newPhone:" + u);
        if (u != null) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "手机号已存在!");
        }
        ValueOperations<String, String> valueOperations = stringRedisTemplate.opsForValue();
        // 拿参数的验证码和redis发送的验证码对比是否正确

        String redisCode = valueOperations.get(PhoneCodeConstant.SPORT_PHONE_CODE + PhoneCodeConstant.CODE_TYPE_CHANGE + newPhone);
        log.info("redisCode验证码:" + redisCode);
        if (!code.equals(redisCode)) {
            throw new SportException(ResultEnum.PARAM_ERROR.getCode(), "验证码输入错误!");
        } else {
            String userId = getUserIdFromCache(username);
            User user = new User();
            user.setId(userId);
            user.setPhone(newPhone);
            return updateById(user);
        }
    }

    @Override
    public IPage<ManagerUserItemVO> getUserByCreatorId(Integer page, Integer size, String creatorId, Long storeId) {
        return getBaseMapper().getUserByCreatorId(new Page<>(page, size), creatorId, storeId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createUser4Store(StoreUserForm form) {
        // 授权店铺
        if (CollectionUtils.isEmpty(form.getStoreIdList())) {
            throw new SportException("授权店铺不能为空!");
        }
        // 角色校验
        if (CollectionUtils.isEmpty(form.getRoleCodes())) {
            throw new SportException("角色不能为空!");
        }
        User user = null;
        if (StringUtils.isEmpty(form.getId())) {
            // 校验手机号码
            user = getByUserPhone(form.getPhone());
            if (user == null && StringUtils.isEmpty(form.getPassword())) {
                throw new SportException("密码不能为空!");
            }
        }
        if (user == null && StringUtils.isNotEmpty(form.getPassword())) {
            // 密码校验
            if (!form.getPassword().equals(form.getNewPassword())) {
                throw new SportException("两次密码不相等");
            }
            // 密码加密
            form.setPassword(passwordEncoder.encode(form.getPassword()));
        }
        // 角色老师 授课教练校验
        if (form.getRoleCodes().contains("teacher") && CollectionUtils.isEmpty(form.getCoachIdList())) {
            throw new SportException("教练必须选择!");
        }
        // 角色老师 授课教练校验
        if (form.getRoleCodes().contains("pre_sale") && CollectionUtils.isEmpty(form.getPreSaleIdList())) {
            throw new SportException("销售必须选择!");
        }
        if (user == null) {
            // 校验昵称
            if (StringUtils.isEmpty(form.getName())) {
                form.setName(form.getPhone());
            }
            if (StringUtils.isEmpty(form.getHead())) {
                // 设置默认头像
                form.setHead("default/head_boy.png");
            }
            user = new User();
            BeanUtils.copyProperties(form, user);
            if (!saveOrUpdate(user)) {
                throw new SportException("用户保存失败!");
            }
        } else if (StringUtils.isEmpty(user.getCreatorId())) {
            user.setCreatorId(form.getCreatorId());
            if (!saveOrUpdate(user)) {
                throw new SportException("用户保存失败!");
            }
        } else {
            throw new SportException("该用户已被添加!");
        }
        // 保存店铺授权
        if (!companyStoreUserService.batchCreateByStoreId(user.getId(), form.getStoreIdList())) {
            log.error("用户授权创建失败:" + JacksonUtils.obj2Json(form));
            throw new SportException("用户保存失败!");
        }
        // 角色保存
        if (!roleUserService.save(user.getId(), form.getRoleCodes(),form.getStoreId(),form.getCompanyId())) {
            log.error("用户角色创建失败:" + JacksonUtils.obj2Json(form));
            throw new SportException("用户保存失败!");
        }
        // 教练老师关系保存
        if (form.getRoleCodes().contains("teacher") && CollectionUtils.isNotEmpty(form.getCoachIdList())) {
            if (!userCoachService.create(user.getId(), form.getCoachIdList(), form.getStoreId(), form.getCompanyId())) {
                log.error("用户教练创建失败:" + JacksonUtils.obj2Json(form));
                throw new SportException("用户保存失败!");
            }
        }
        // 销售关系保存
        if (form.getRoleCodes().contains("pre_sale") && CollectionUtils.isNotEmpty(form.getPreSaleIdList())) {
            if (!userCoachService.create(user.getId(), form.getPreSaleIdList(), form.getStoreId(), form.getCompanyId())) {
                log.error("用户销售创建失败:" + JacksonUtils.obj2Json(form));
                throw new SportException("用户保存失败!");
            }
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUser(StoreUserForm user) {
        // 校验用户id不能为空 根据用户id删除
        if (StringUtils.isEmpty(user.getId())) {
            throw new SportException("用户id不能为空!");
        }
        // 根据userId查询是否关联教练\销售
        List<UserCoach> userCoachList = userCoachService.getByUserId(user.getId(),user.getStoreId());
        // 教练\销售 先删除数据 在添加
        if (CollectionUtils.isNotEmpty(userCoachList)) {
            // 教师 查询有没有课
            List<GradeVO> gradeList = gradeService.getByCoachId(userCoachList.stream().map(UserCoach::getCoachId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(gradeList)) {
                List<String> roleCodes = user.getRoleCodes();
                // 没有老师角色
                if (roleCodes == null || !roleCodes.contains("teacher")) {
                    throw new SportException("当前用户有课程,不可以取消老师角色!");
                }
            }
            // 删除 教师、教练数据
            userCoachService.delUserId(user.getId(),userCoachList.stream().map(UserCoach::getCoachId).collect(Collectors.toList()));
        }
        // 删除用户与商铺关系
        companyStoreUserService.delUserId(user.getId(), user.getStoreId());
        // 删除角色表数据
        roleUserService.delByUserIdAndStoreId(user.getId(),user.getStoreId());
        // 添加数据
        return createUser4Store(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteStoreUser(String userId) {
        // 根据用户id查询是否是教师
        List<UserCoach> userCoachList = userCoachService.getByUserId(userId);
        if (CollectionUtils.isNotEmpty(userCoachList)) {
            // 教师 查询有没有课
            List<GradeVO> gradeList = gradeService.getByCoachId(userCoachList.stream().map(UserCoach::getCoachId).collect(Collectors.toList()));
            if (CollectionUtils.isNotEmpty(gradeList)) {
                // 有课 不能删除
                throw new SportException("当前老师有课, 不可以删除!");
            }
            // 删除教练关联表
            if (!userCoachService.delUserId(userId)) {
                throw new SportException("操作失败!");
            }
        }
        // 根据用户表id删除表数据
        // 删除角色用户关系表数据
        // 删除角色表数据
        if (userService.removeById(userId) && companyStoreUserService.delUserId(userId) &&
                roleUserService.delByUserId(userId)) {
            return true;
        }
        throw new SportException("操作失败!");
    }

    @Override
    public StoreUserForm getStoreUserById(String id) {
        StoreUserForm user = getBaseMapper().getStoreUserById(id);
        if (user != null) {
            user.setHead(FileUtil.getImageFullUrl(user.getHead()));
            return user;
        }
        return null;
    }

    @Override
    public StoreUserForm getStoreUserById(String id, Long storeId) {
        User userVo = userService.getById(id);
        if (userVo == null) {
            return null;
        }
        userVo.setHead(FileUtil.getImageFullUrl(userVo.getHead()));
        userVo.setPassword(null);
        StoreUserForm storeUserForm = new StoreUserForm();
        BeanUtils.copyProperties(userVo, storeUserForm);
        //获取角色
        List<String> roleUserVOList = roleUserService.getByUserId(id, storeId);
        if (CollectionUtils.isNotEmpty(roleUserVOList)){
            storeUserForm.setRoleCodes(roleUserVOList);
        }
        //获取销售、教练数据
        List<StoreCoach> storeCoaches = storeCoachService.getByUserAndStoreId(id, Long.valueOf(storeId));
        if (CollectionUtils.isNotEmpty(storeCoaches)){
            List<Long> coachs = new ArrayList<>();
            List<Long> sales = new ArrayList<>();

            storeCoaches.stream().forEach(item ->{
                if (item.getUserType() == StoreCoachTypeEnum.COACH_TYPE.getCode()){
                    coachs.add(item.getId());
                }else if(item.getUserType() == StoreCoachTypeEnum.SALE_TYPE.getCode()){
                    sales.add(item.getId());
                }
            });
            storeUserForm.setCoachIdList(coachs);
            storeUserForm.setPreSaleIdList(sales);
        }
        return storeUserForm;
    }

    @Override
    public StoreUserForm getStoreRoleByUserId(String id, Long storeId) {
        StoreUserForm storeUserForm = new StoreUserForm();
        //获取角色
        List<String> roleUserVOList = roleUserService.getByUserId(id, storeId);
        if (CollectionUtils.isNotEmpty(roleUserVOList)){
            storeUserForm.setRoleCodes(roleUserVOList);
        }
        return storeUserForm;
    }

    @Override
    public IPage<User> getPresale(int page, int size, Long id) {
        // 根据id查询组下组员的信息
        List<WaterPoolSaleGroupUser> userAll = waterPoolSaleGroupUserService.getUserAll(id);
        // 查询所有组员信息
        IPage<User> userIPage = userDao.getPresale(new Page<>(page, size), id);
        System.out.println(userIPage);
        // 循环比较 如果存在就移除
        List<User> list = userIPage.getRecords();
        for (int i = list.size() - 1; i >= 0; i--) {
            // 获取组员信息id
            String uId = list.get(i).getId();
            System.out.println(uId + "1");
            for (int j = 0; j < userAll.size(); j++) {
                // 获取组下的userid
                String groupId = userAll.get(j).getUserId();
                if (uId.equals(groupId)) {
                    list.remove(i);
                }
            }
        }
        userIPage.setRecords(list);
        return userIPage;
    }

    @Override
    public boolean deleteById(String userId) {
        getBaseMapper().deleteUserById(userId);
        return true;
    }

    @Override
    public String createByPhone(String phone, String name) {
        User user = new User();
        user.setId(IdUtils.getStringId());
        user.setName(StringUtils.isEmpty(name) ? phone : name);
        user.setPhone(phone);
        user.setUsername(phone);
        user.setPassword(passwordEncoder.encode(phone));
        save(user);
        return user.getId();
    }

    @Override
    public List<User> getTestUser(int number) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(User::getIntroduction, "test")
                .orderByAsc(User::getCreateDate);
        queryWrapper.last("limit " + number);
        return list(queryWrapper);
    }

    @Override
    public UserStoreBalanceVO getStoreBalance(String username) {
        UserStoreBalanceVO userStoreBalanceVO = new UserStoreBalanceVO();
        User user = getOne(getQueryWrapper().eq("username", username));
        userStoreBalanceVO.setBalance(user.getBalance());
        UserExtensionAccount userExtensionAccount = userExtensionAccountService.getByUserId(user.getId());
        if (userExtensionAccount != null) {
            userStoreBalanceVO.setExtensionBalance(userExtensionAccount.getBalance());
        }
        return userStoreBalanceVO;
    }

    @Override
    public UserExtensionAccount getUserBalance(String userId) {
        return userDao.getUserBalance(userId);
    }

    @Override
    public Boolean updateBalanceById(UserExtensionAccount user) {
        Integer integer = userDao.updateBalanceById(user);
        return (integer > 0);
    }

    @Override
    public UserExtendVO getUserExtendedInfo(Long companyId) {
        UserExtendVO userExtendVO = new UserExtendVO();
        Company company = companyService.getById(companyId);
        if (company == null) {
            return userExtendVO;
        }
        return this.getUserExtendedInfoByAppId(company);
    }

    @Override
    public UserExtendVO getUserExtendedInfoByAppId(String appId) {
        UserExtendVO userExtendVO = new UserExtendVO();
        Company companyByAppId = companyService.getCompanyByMiniAppId(appId);
        if (companyByAppId == null) {
            return userExtendVO;
        }
        return this.getUserExtendedInfoByAppId(companyByAppId);
    }

    public UserExtendVO getUserExtendedInfoByAppId(Company company) {
        UserExtendVO userExtendVO = new UserExtendVO();
        userExtendVO.setCompanyId(company.getId());
        userExtendVO.setCancelBookingDays(1);
        userExtendVO.setAppointmentCoachLimit(0);
        List<StoreConfig> allCompanyConfig = storeConfigService.getAllCompanyConfig(company.getId());
        for (StoreConfig storeConfig : allCompanyConfig) {
            if (SysDictConstant.FIELD_ORDER_CANCEL_HOURS_LIMIT.equals(storeConfig.getCode())) {
                userExtendVO.setCancelBookingDays(Integer.parseInt(storeConfig.getValue()));
            } else if ("appointment_coach_limit".equals(storeConfig.getCode())) {
                userExtendVO.setAppointmentCoachLimit(Boolean.parseBoolean(storeConfig.getValue()) ? 1 : 0);
            }
        }
        userExtendVO.setBeforeBookingMinutes(company.getBeforeBookingMinutes() == null ? 0 : company.getBeforeBookingMinutes());
        userExtendVO.setBrandLogo(company.getBrandLogo());
        userExtendVO.setBrandName(company.getBrandName());
        userExtendVO.setVipAgreementTemplate(company.getVipAgreementTemplate());
        userExtendVO.setInvoiceContents(company.getInvoiceContents());
        userExtendVO.setCancelGradeMinutes(company.getCancelGradeMinutes());
        userExtendVO.setMiniAppId(company.getMiniAppId());
        userExtendVO.setFieldBookNums(company.getFieldBookNums());
        return userExtendVO;
    }

    @Override
    public WebUserVo getWebUserById(String id) {
        User user = getById(id);
        WebUserVo webUserVo = new WebUserVo();
        BeanUtils.copyProperties(user, webUserVo);
        webUserVo.setRoles(OauthUtils.getCurrentUserRoles());
        List<Map<String, Object>> routers = menuService.getWebMenuByRoleCode(webUserVo.getRoles());
        webUserVo.setRouters(routers);
        webUserVo.setHead(FileUtil.getImageFullUrl(webUserVo.getHead()));
        return webUserVo;
    }

    @Override
    public boolean changeStatus(String id, Integer status) {
        User user = new User();
        user.setId(id);
        user.setStatus(status);
        return updateById(user);
    }

    @Override
    public boolean updateUserBaseInfo(String currentUserId, String name, String head, Integer gender) {
        User user = getUserByUerId(currentUserId);
        user.setHead(head);
        user.setGender(gender);
        user.setName(name);
        return updateById(user);
    }

    @Override
    public User getByOpenId(String openId) {
        return getBaseMapper().getFromUserOpenId(openId);
    }

    @Override
    public User quickRegisterByPhone(String phone, String password) {
        User old = getByUserPhone(phone);
        if (old != null) {
            return old;
        }
        User user = new User();
        user.setHead("default/head_boy.png");
        user.setPhone(phone);
        user.setUsername(phone);
        if (StringUtils.isNotEmpty(password)) {
            user.setPassword(passwordEncoder.encode(password));
        } else {
            user.setPassword(passwordEncoder.encode(UUID.fastUUID().toString(true)));
        }
        user.setStatus(1);
        // 设置昵称 隐藏中间手机号
        user.setName(phone.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2"));
        if (save(user)) {
            return user;
        }
        return null;
    }
}