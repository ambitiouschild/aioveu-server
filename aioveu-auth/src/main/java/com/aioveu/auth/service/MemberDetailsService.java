package com.aioveu.auth.service;

import cn.hutool.core.lang.Assert;
import com.aioveu.auth.model.MemberDetails;
import com.aioveu.common.enums.StatusEnum;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.aioveu.ums.api.MemberFeignClient;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberRegisterDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.stereotype.Service;

/**
 * @Description: TODO 商城会员用户认证服务
 * @Author: 雒世松
 * @Date: 2025/6/5 17:52
 * @param
 * @return:
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailsService {

    private final MemberFeignClient memberFeignClient;

    private final TenantFeignClient tenantFeignClient;


    /**
     * 手机号码认证方式
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    public MemberDetails loadUserByMobile(String mobile) {
        Result<MemberAuthDTO> result = memberFeignClient.loadUserByMobile(mobile);

        MemberAuthDTO memberAuthInfo;
        if (!(Result.isSuccess(result) && (memberAuthInfo = result.getData()) != null)) {
            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
        }
        MemberDetails userDetails = new MemberDetails(memberAuthInfo);
        if (!userDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return userDetails;
    }

    /**
     * 根据openId获取用户信息
     *
     * @param openid  微信公众平台唯一身份标识
     * @return {@link MemberDetails}
     */
    public MemberDetails loadUserByOpenid(String openid) {
        // 根据 openid 获取微信用户认证信息
        // 调用会员服务API，查询微信openid对应用户
        // 首先尝试获取用户
        MemberAuthDTO memberAuthInfo = memberFeignClient.loadUserByOpenId(openid).getData();


        // 会员不存在，注册成为新会员
        if (memberAuthInfo==null) {

            MemberRegisterDto memberRegisterInfo = new MemberRegisterDto();
            memberRegisterInfo.setOpenid(openid);
            memberRegisterInfo.setAvatarUrl("https://cdn.aioveu.com/aioveu/aioveu-server/avatar/avatar.png");
            memberRegisterInfo.setNickName("新注册微信用户");
            // 注册会员
            //通过Feign客户端调用会员服务的注册接口，将注册信息发送到会员服务，并接收注册结果。
            Result<Long> registerMemberResult = memberFeignClient.registerMember(memberRegisterInfo);


            //   注册失败处理----------------------
            if (!Result.isSuccess(registerMemberResult)) {
                throw new UsernameNotFoundException("会员注册失败: " + registerMemberResult.getMsg());
            }


            //注册成功后，不要立即使用openid去查询，而是使用注册接口返回的会员ID，再调用根据会员ID获取认证信息的接口。这样避免因为主从同步延迟等问题导致查不到用户。
            // 注册成功将会员信息赋值给会员认证信息
            //提供通过会员ID查询的接口（这样在注册后我们可以立即用注册返回的会员ID去查询，避免使用openid查询可能存在的延迟）

            //避免使用OpenID立即查询可能的数据延迟问题
            //通过会员ID查询是直接的主键查询，没有同步延迟问题
            Long memberId;
            if (Result.isSuccess(registerMemberResult) && (memberId = registerMemberResult.getData()) != null) {
                memberAuthInfo = new MemberAuthDTO(memberId, openid,null, StatusEnum.ENABLE.getValue());
            }
        }

        // 用户不存在
        if (memberAuthInfo == null) {
            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
        }

        MemberDetails userDetails = new MemberDetails(memberAuthInfo);
        if (!userDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return userDetails;
    }

    /**
     * 根据openId和clientId获取用户信息  新增：通过 openid + tenantId 查询用户
     *
     * @param openid  微信公众平台唯一身份标识
     * @param clientId  客户端绑定wxAppId,绑定Tenant
     * @return {@link MemberDetails}
     */
    public MemberDetails loadUserByOpenidAndClientId(String openid,String clientId) {

        // 6. ★ 通过 clientId 查询 wxAppid 和 tenantId

        log.info("开始查询clientId: {}", clientId);

        // 这里需要你实现数据库查询
        TenantWxAppInfo tenantWxAppInfo = tenantFeignClient.getTenantWxAppInfoByClientId(clientId);

        log.info("查询到的tenantWxAppInfo: {}", tenantWxAppInfo);

        if (tenantWxAppInfo == null) {
//            throw new OAuth2AuthenticationException("无效的客户端ID");
            log.info("无效的客户端ID: {}", clientId);
        }

        String wxAppid = tenantWxAppInfo.getWxAppid();
        Long tenantId = tenantWxAppInfo.getTenantId();
        log.info("查询到租户信息 - wxAppid: {}, tenantId: {}", wxAppid, tenantId);

        //用户通用
//        MemberAuthDTO memberAuthInfo = memberFeignClient.loadUserByOpenId(openid).getData();

        // 根据 openid 和 tenantId 获取微信用户认证信息
        // 调用会员服务API，查询微信openid对应用户
        // 首先尝试获取用户
        MemberAuthDTO memberAuthInfo = memberFeignClient.loadUserByOpenIdAndTenantId(openid,tenantId).getData();

        log.info("查询到用户信息 memberAuthInfo {}", memberAuthInfo);
        // 会员不存在，注册成为新会员
        if (memberAuthInfo==null) {

            MemberRegisterDto memberRegisterInfo = new MemberRegisterDto();
            memberRegisterInfo.setOpenid(openid);
            memberRegisterInfo.setAvatarUrl("https://cdn.aioveu.com/aioveu/aioveu-server/avatar/avatar.png");
            memberRegisterInfo.setNickName("新注册微信用户");
            memberRegisterInfo.setTenantId(tenantId);
            // 注册会员
            //通过Feign客户端调用会员服务的注册接口，将注册信息发送到会员服务，并接收注册结果。
            Result<Long> registerMemberResult = memberFeignClient.registerMember(memberRegisterInfo);


            //   注册失败处理----------------------
            if (!Result.isSuccess(registerMemberResult)) {
                throw new UsernameNotFoundException("会员注册失败: " + registerMemberResult.getMsg());
            }


            //注册成功后，不要立即使用openid去查询，而是使用注册接口返回的会员ID，再调用根据会员ID获取认证信息的接口。这样避免因为主从同步延迟等问题导致查不到用户。
            // 注册成功将会员信息赋值给会员认证信息
            //提供通过会员ID查询的接口（这样在注册后我们可以立即用注册返回的会员ID去查询，避免使用openid查询可能存在的延迟）

            //避免使用OpenID立即查询可能的数据延迟问题
            //通过会员ID查询是直接的主键查询，没有同步延迟问题
            Long memberId;
            if (Result.isSuccess(registerMemberResult) && (memberId = registerMemberResult.getData()) != null) {
                memberAuthInfo = new MemberAuthDTO(memberId, openid, tenantId,StatusEnum.ENABLE.getValue());
            }
        }

        // 用户不存在
        if (memberAuthInfo == null) {
            throw new UsernameNotFoundException(ResultCode.USER_NOT_EXIST.getMsg());
        }


        log.info("这里构造用户信息");
        MemberDetails userDetails = new MemberDetails(memberAuthInfo);



        if (!userDetails.isEnabled()) {
            throw new DisabledException("该账户已被禁用!");
        } else if (!userDetails.isAccountNonLocked()) {
            throw new LockedException("该账号已被锁定!");
        } else if (!userDetails.isAccountNonExpired()) {
            throw new AccountExpiredException("该账号已过期!");
        }
        return userDetails;
    }

}
