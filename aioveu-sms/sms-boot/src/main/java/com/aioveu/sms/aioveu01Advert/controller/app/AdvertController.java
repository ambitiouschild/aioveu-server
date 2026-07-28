package com.aioveu.sms.aioveu01Advert.controller.app;

import com.aioveu.common.core.annotation.PublicApi;
import com.aioveu.common.core.result.Result;
import com.aioveu.sms.aioveu01Advert.model.vo.BannerVO;
import com.aioveu.sms.aioveu01Advert.service.SmsAdvertService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "App-营销广告")
@RestController
@RequestMapping("/aioveu/api/v8/app/sms/adverts")
@Slf4j
@AllArgsConstructor
public class AdvertController {

    private SmsAdvertService smsAdvertService;

    @PublicApi(description = "获取Banners轮播图（公共接口）")
    @Operation(summary= "获取Banners轮播图（公共接口）")
    @GetMapping("/banners")
    public Result<List<BannerVO>> getSmsHomeBannersList() {
        List<BannerVO> list = smsAdvertService.getBannerList();
        return Result.success(list);
    }



}
