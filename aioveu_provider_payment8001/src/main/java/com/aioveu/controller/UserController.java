package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.model.LoginVal;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.constant.SportConstant;
import com.aioveu.entity.RoleUser;
import com.aioveu.entity.User;
import com.aioveu.exception.SportException;
import com.aioveu.form.UserCashForm;
import com.aioveu.service.CodeService;
import com.aioveu.service.RoleUserService;
import com.aioveu.service.UserOpenIdService;
import com.aioveu.service.UserService;
import com.aioveu.vo.*;
import com.aioveu.vo.user.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/9/5 0005 12:01
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @Autowired
    private UserOpenIdService userOpenIdService;

    @Autowired
    private RoleUserService roleUserService;

    @Autowired
    private CodeService codeService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public IPage<UserItemVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                  @RequestParam(required = false) String role,
                                  @RequestParam(required = false) String phone,
                                  @RequestParam(required = false) String keyword) {
        return userService.getAll(page, size, role, phone, keyword);
    }

    @PostMapping("")
    public UserVo create(@Valid UserForm form) {
        return userService.create(form);
    }

    @PostMapping("/postTempAccounts")
    public Map<String, String> createQuShuTempAccounts() {
        return userService.createQuShuTempAccounts();
    }

    @PostMapping("/manager")
    public boolean create(@Valid @RequestBody SportUserForm form) {
        return userService.webManagerCreateOrUpdate(form);
    }

    @PutMapping("/status")
    public boolean changeStatus(@RequestParam String id, @RequestParam Integer status) {
        return userService.changeStatus(id, status);
    }

    @GetMapping("/manager/{id}")
    public BaseUserForm create(@PathVariable String id) {
        return userService.getManagerUserById(id);
    }

    @PutMapping("/manager")
    public boolean update(@Valid @RequestBody SportUserForm form) {
        return userService.webManagerCreateOrUpdate(form);
    }

    @PutMapping("/password")
    public boolean resetPassword(@RequestParam String id, @RequestParam String password) {
        return userService.resetPassword(id, password);
    }

    @GetMapping("openId")
    public UserVo getByOpenId(@RequestParam("openId") String openId,
                              @RequestParam(value = "providerId", required = false) String providerId) {
        return userService.getByIdAndProvider(openId, providerId);
    }

    @GetMapping("username")
    public UserVo getByUsername(@RequestParam(value = "appId", required = false) String appId) {
        return userService.findByUsernameAndAppId(OauthUtils.getCurrentUsername(), appId);
    }

    @GetMapping("{id}")
    public UserVo getById(@PathVariable("id") String id) {
        return userService.getByUserId(id);
    }

    @GetMapping("checkRegister/{checkType}")
    public UserVo checkRegister(@PathVariable Integer checkType, @RequestParam(required = false) String unionId, @RequestParam(required = false) String openId) {
        return userService.checkRegister(checkType, openId, unionId);
    }

    @GetMapping("realTime")
    public UserRealTimeVO realTime(@RequestParam(required = false) Long companyId) {
        return userService.getRealTimeByUsername(OauthUtils.getCurrentUsername(), companyId);
    }

    @PostMapping("cash")
    public Boolean cash(@Valid UserCashForm form) {
        return userService.userCash(form);
    }

    @GetMapping("me")
    public LoginVal me() {
        return OauthUtils.getCurrentUser();
    }

    @PostMapping("openId/add")
    public Boolean addOpenId(@RequestParam("openId") String openId,
                              @RequestParam(value = "appId") String appId,
                             @RequestParam(value = "unionId", required = false) String unionId) {
        return userOpenIdService.addOrUpdate(OauthUtils.getCurrentUserId(), appId, openId, unionId);
    }

    /**
     * 用户修改手机号  需要给新手机号发送验证码
     * @param newPhone 新手机号
     * @return
     */
    @PutMapping("phone")
    public boolean updPhone(@RequestParam(value = "newPhone") String newPhone,
                        @RequestParam(value = "code") String code ){
        return userService.updateUserPhone(OauthUtils.getCurrentUsername(),newPhone,code);
    }

    /**
     * 商户小程序端账户列表 根据创建者id 和 店铺id查询 店铺id选填
     * @param page
     * @param size
     * @param creatorId 创建者id
     * @param storeId 店铺id
     * @return
     */
    @GetMapping("user-creator-list")
    public IPage<ManagerUserItemVO> userList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                  @RequestParam(required = false) Long storeId,
                                  @RequestParam String creatorId) {
        return userService.getUserByCreatorId(page, size, creatorId, storeId);
    }

    /**
     * 商户小程序端创建用户
     * @param user
     */
    @PostMapping("store")
    public boolean create(@Valid @RequestBody StoreUserForm user){
         return userService.createUser4Store(user);
    }

    @GetMapping("store/{id}")
    public StoreUserForm getStoreUserById(@PathVariable String id,@RequestParam Long storeId) {
        return userService.getStoreUserById(id,storeId);
    }

    @GetMapping("storeRole/{id}")
    public StoreUserForm getStoreRoleByUserId(@PathVariable String id,@RequestParam Long storeId) {
        return userService.getStoreRoleByUserId(id,storeId);
    }

    /**
     * 修改小程序商户端用户
     * @param user
     */
    @PutMapping("store")
    public boolean updUser(@Valid @RequestBody StoreUserForm user){
        return userService.updateUser(user);
    }

    /**
     * 删除小程序商户端用户
     * @param userId
     */
    @DeleteMapping("store/{userId}")
    public boolean deleteStoreUser(@PathVariable String userId){
        return userService.deleteStoreUser(userId);
    }


    @GetMapping("presale")
    public IPage<User> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                            @RequestParam(required = false, defaultValue = "10") Integer size,
                            @RequestParam Long id) {
        return userService.getPresale(page,size,id);
    }


    @DeleteMapping("/{userId}")
    public Boolean deleteUser(@PathVariable String userId) {
        return userService.deleteById(userId);
    }

    @GetMapping("getUserExtendedInfo/{companyId}")
    public UserExtendVO getUserExtendedInfo(@PathVariable Long companyId) {
        return userService.getUserExtendedInfo(companyId);
    }

    @GetMapping("getUserExtendedInfoByAppId/{appId}")
    public UserExtendVO getUserExtendedInfoByAppId(@PathVariable String appId) {
        return userService.getUserExtendedInfoByAppId(appId);
    }

    @GetMapping("store-balance")
    public UserStoreBalanceVO storeBalance() {
        return userService.getStoreBalance(OauthUtils.getCurrentUserId());
    }

    @GetMapping("web-user")
    public WebUserVo getWebUserById() {
        return userService.getWebUserById(OauthUtils.getCurrentUserId());
    }

    @GetMapping("role/{userId}")
    public List<UserRoleVo> getUserRole(@PathVariable String userId) {
        return roleUserService.getUserRole(userId);
    }

    @DeleteMapping("role/{id}")
    public boolean deleteUserRole(@PathVariable Long id) {
        return roleUserService.removeById(id);
    }

    @PostMapping("role")
    public boolean addUserRole(@Valid @RequestBody RoleUser roleUser) {
        return roleUserService.saveUserRole(roleUser);
    }

    @PutMapping("/update/user-info")
    public boolean updateUserBaseInfo(
                               @RequestParam(value = "appId") String appId,
                               @RequestParam(value = "openId") String openId,
                               @RequestParam(value = "unionId",required = false ) String unionId ,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "head") String head ,
                               @RequestParam(value = "gender" ,required = false ) Integer gender ){
        //更新用户头像信息
        userService.updateUserBaseInfo(OauthUtils.getCurrentUserId(),name, head, gender);
        //更新appid
        userOpenIdService.addOrUpdate(OauthUtils.getCurrentUserId(), appId, openId, unionId);
        return true;
    }

    @Autowired
    private Environment environment;

    @GetMapping("/mp-qrcode/{userId}")
    public String getBindMpQrcode(@PathVariable String userId) {
        String[] activeProfiles = environment.getActiveProfiles();
        String activeProfile = activeProfiles[0];
        String appId;
        if (activeProfile.equalsIgnoreCase("dev")) {
            appId = "wx50100f48fa9321e5";
        } else {
            appId = SportConstant.QU_SHU_WECHAT_MP;
        }
        return codeService.bindMpQrcode(userId, appId);
    }
}
