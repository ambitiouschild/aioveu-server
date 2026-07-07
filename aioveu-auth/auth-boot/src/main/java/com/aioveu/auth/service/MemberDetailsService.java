package com.aioveu.auth.service;

import com.aioveu.auth.model.MemberDetails;
import com.aioveu.common.result.Result;
import com.aioveu.common.result.ResultCode;
import com.aioveu.tenant.api.TenantFeignClient;
import com.aioveu.tenant.dto.TenantWxAppInfo;
import com.aioveu.ums.api.MemberFeignClient;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberRegisterForm;
import com.aioveu.ums.dto.MemberRegisterDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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




    /**
     * 手机号码认证方式
     *
     * @param mobile 手机号
     * @return 用户信息
     */
    public MemberDetails loadUserByMobile(String mobile) {
        Result<MemberAuthDTO> result = memberFeignClient.loadMemberByMobile(mobile);

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
     * @param openId  微信公众平台唯一身份标识
     * @return {@link MemberDetails}
     */
    public MemberDetails loadMemberByOpenId(String openId) {
        // 根据 openId 获取微信用户认证信息
        // 调用会员服务API，查询微信openId对应用户
        // 首先尝试获取用户
        Result<MemberAuthDTO>  memberAuthDTOResult = memberFeignClient.loadMemberByOpenId(openId);
        //   注册失败处理----------------------
        if (!Result.isSuccess(memberAuthDTOResult)) {
            throw new UsernameNotFoundException("会员信息查询失败: " + memberAuthDTOResult.getMsg());
        }
        MemberAuthDTO memberAuthDTO = memberAuthDTOResult.getData();

        // 会员不存在，注册成为新会员
        if (memberAuthDTO == null) {
            MemberRegisterForm memberRegisterInfo = new MemberRegisterForm();
            // 注册会员
            //通过Feign客户端调用会员服务的注册接口，将注册信息发送到会员服务，并接收注册结果。
            Result<MemberRegisterDTO> memberRegisterResult = memberFeignClient.registerMember(memberRegisterInfo);

            //   注册失败处理----------------------
            if (!Result.isSuccess(memberRegisterResult)) {
                throw new UsernameNotFoundException("会员注册失败: " + memberRegisterResult.getMsg());
            }
            MemberRegisterDTO memberRegister = memberRegisterResult.getData();
            //注册成功后，不要立即使用openId去查询，而是使用注册接口返回的会员ID，再调用根据会员ID获取认证信息的接口。这样避免因为主从同步延迟等问题导致查不到用户。
            // 注册成功将会员信息赋值给会员认证信息
            //提供通过会员ID查询的接口（这样在注册后我们可以立即用注册返回的会员ID去查询，避免使用openId查询可能存在的延迟）

            //避免使用openId立即查询可能的数据延迟问题
            //通过会员ID查询是直接的主键查询，没有同步延迟问题
            if (Result.isSuccess(memberRegisterResult) && memberRegister != null) {
                memberAuthDTO.setId(memberRegister.getId());
                memberAuthDTO.setNickName(memberRegister.getNickName());
                memberAuthDTO.setMobile(memberRegister.getMobile());
                memberAuthDTO.setOpenId(memberRegister.getOpenId());
                memberAuthDTO.setTenantId(memberRegister.getTenantId());
                memberAuthDTO.setStatus(memberRegister.getStatus());

            }
        }

        MemberDetails userDetails = new MemberDetails(memberAuthDTO);
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
     * 根据openId和tenantId获取用户信息  新增：通过 openId + tenantId查询用户
     *
     * @param openId  微信公众平台唯一身份标识
     * @param tenantId  租户id
     * @return {@link MemberDetails}
     */
    public MemberDetails loadMemberByOpenIdAndTenantId(String openId,Long tenantId) {

        // 1. ★ 通过 clientId 查询 wxAppid 和 tenantId
        log.info("=======【Auth MemberDetailsService】根据openId和tenantId获取用户信息,Auth没有走资源服务器的TenantFilter=======");
        log.info("【Auth MemberDetailsService】根据openId和tenantId获取用户信息{}, tenantId: {}", openId, tenantId);
        // 根据 openId 和 tenantId 获取微信用户认证信息
        // 调用会员服务API，查询微信openId对应用户
        // 首先尝试获取用户
        Result<MemberAuthDTO>  memberAuthDTOResult = memberFeignClient.loadMemberByOpenIdAndTenantId(openId,tenantId);
        //   注册失败处理----------------------
        if (!Result.isSuccess(memberAuthDTOResult)) {
            throw new UsernameNotFoundException("会员信息查询失败: " + memberAuthDTOResult.getMsg());
        }
        MemberAuthDTO memberAuthDTO = memberAuthDTOResult.getData();
        log.info("【Auth MemberDetailsService】查询到用户信息 memberAuthInfo {}", memberAuthDTO);


        // 会员不存在，注册成为新会员
        if (memberAuthDTO==null) {

            MemberRegisterForm memberRegisterInfo = new MemberRegisterForm();
            memberRegisterInfo.setOpenId(openId);
            memberRegisterInfo.setAvatarUrl("https://cdn.aioveu.com/aioveu/aioveu-server/avatar/avatar.png");
            memberRegisterInfo.setNickName("新注册微信用户");
            memberRegisterInfo.setTenantId(tenantId);
            // 注册会员
            //通过Feign客户端调用会员服务的注册接口，将注册信息发送到会员服务，并接收注册结果。
            Result<MemberRegisterDTO> registerMemberResult = memberFeignClient.registerMember(memberRegisterInfo);


            //   注册失败处理----------------------
            if (!Result.isSuccess(registerMemberResult)) {
                throw new UsernameNotFoundException("【Auth MemberDetailsService】会员注册失败: " + registerMemberResult.getMsg());
            }
            MemberRegisterDTO memberRegister = registerMemberResult.getData();

            //注册成功后，不要立即使用openId去查询，而是使用注册接口返回的会员ID，再调用根据会员ID获取认证信息的接口。这样避免因为主从同步延迟等问题导致查不到用户。
            // 注册成功将会员信息赋值给会员认证信息
            //提供通过会员ID查询的接口（这样在注册后我们可以立即用注册返回的会员ID去查询，避免使用openId查询可能存在的延迟）

            //避免使用openId立即查询可能的数据延迟问题
            //通过会员ID查询是直接的主键查询，没有同步延迟问题
            if (Result.isSuccess(registerMemberResult) && memberRegister != null) {
                memberAuthDTO.setId(memberRegister.getId());
                memberAuthDTO.setNickName(memberRegister.getNickName());
                memberAuthDTO.setMobile(memberRegister.getMobile());
                memberAuthDTO.setOpenId(memberRegister.getOpenId());
                memberAuthDTO.setTenantId(memberRegister.getTenantId());
                memberAuthDTO.setStatus(memberRegister.getStatus());
            }

            log.info("【Auth MemberDetailsService】如果会员不存在，注册成为新会员：{}", memberAuthDTO);
        }
        MemberDetails userDetails = new MemberDetails(memberAuthDTO);
        log.info("【Auth MemberDetailsService】这里构造用户信息");

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
