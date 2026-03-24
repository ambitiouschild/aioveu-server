package com.aioveu.sms.api.app;

import com.aioveu.common.result.Result;
import com.aioveu.common.web.config.FeignDecoderConfig;
import com.aioveu.sms.dto.BannerVO;
import com.aioveu.sms.dto.SmsHomeAdvertVO;
import com.aioveu.sms.dto.SmsHomeCategoryVO;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Description: TODO 优惠券领券记录APP端Feign接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:39
 * @param
 * @return:
 **/

@FeignClient(value = "aioveu-tenant-sms",
        configuration = {FeignDecoderConfig.class}
)
public interface SmsFeignClient {

    @GetMapping("/api.app/v1/coupon_record/list")
    Result list();

    @GetMapping("/api.app/v1/coupon_record/{id}/detail")
    Result detail(@PathVariable("id") String id);

    @PostMapping("/api.app/v1/coupon_record")
    Result add(@RequestParam("couponId") String couponId);

    @PostMapping("/api.app/v1/coupon_record/push")
    Result add(@RequestParam("couponType") Integer couponType,
               @RequestParam("userId") Long userId);


    @Operation(summary = "首页分类配置分页列表表")
    @GetMapping("/app-api/v1/sms-home-category/page")
    List<SmsHomeCategoryVO> getSmsHomeCategoryList(
            @RequestHeader("X-Tenant-Id") Long tenantId
    );


    @Operation(summary = "首页广告配置（增加跳转路径）分页列表")
    @GetMapping("/app-api/v1/sms-home-advert/page")
    List<SmsHomeAdvertVO> getSmsHomeAdvertList(
            @RequestHeader("X-Tenant-Id") Long tenantId
    );

    @Operation(summary = "首页Banners")
    @GetMapping("/app-api/v1/adverts/banners")
    List<BannerVO> getSmsHomeBannersList(
            @RequestHeader("X-Tenant-Id") Long tenantId
    );

}
