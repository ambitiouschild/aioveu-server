package com.aioveu.ums.api;

import com.aioveu.common.result.Result;
import com.aioveu.pms.model.vo.ProductHistoryVO;
import com.aioveu.ums.dto.MemberAddressDTO;
import com.aioveu.ums.dto.MemberAuthDTO;
import com.aioveu.ums.dto.MemberRegisterDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO 会员Feign客户端
 * @Author: 雒世松
 * @Date: 2025/6/5 18:53
 * @param
 * @return:
 **/

@FeignClient(name = "mall-ums", contextId = "member")
public interface MemberFeignClient {

    /**
     * 新增会员
     *
     * @param member
     * @return
     */
    @PostMapping("/app-api/v1/members")
    Result<Long> registerMember(@RequestBody MemberRegisterDto member);

    /**
     * 获取会员的 openid
     *
     * @return
     */
    @PostMapping("/app-api/v1/members/{memberId}/openid")
    Result<String> getMemberOpenId(@PathVariable Long memberId);

    /**
     * 扣减会员余额
     */
    @PutMapping("/app-api/v1/members/{memberId}/balances/_deduct")
    <T> Result<T> deductBalance(@PathVariable Long memberId, @RequestParam Long amount);

    /**
     * 添加浏览记录
     */
    @PostMapping("/app-api/v1/members/view/history")
    <T> Result<T> addProductViewHistory(@RequestBody ProductHistoryVO product);

    /**
     * 根据openId获取会员认证信息
     *
     * @param openid
     * @return
     */
    @GetMapping("/app-api/v1/members/openid/{openid}")
    Result<MemberAuthDTO> loadUserByOpenId(@PathVariable String openid);

    /**
     * 根据手机号获取会员认证信息
     *
     * @param mobile 手机号
     * @return 会员认证信息
     */
    @GetMapping("/app-api/v1/members/mobile/{mobile}")
    Result<MemberAuthDTO> loadUserByMobile(@PathVariable String mobile);

    /**
     * 获取会员地址列表
     *
     * @param memberId
     * @return
     */
    @GetMapping("/app-api/v1/members/{memberId}/addresses")
    Result<List<MemberAddressDTO>> listMemberAddresses(@PathVariable Long memberId);

}


