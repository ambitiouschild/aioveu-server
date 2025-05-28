package com.aioveu.controller;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.CouponTemplate;
import com.aioveu.exception.SportException;
import com.aioveu.service.CouponTemplateService;
import com.aioveu.service.IBuildTemplateService;
import com.aioveu.service.ITemplateBaseService;
import com.aioveu.vo.CouponTemplateItemVO;
import com.aioveu.vo.CouponTemplateVO;
import com.aioveu.vo.IdNameVO;
import com.aioveu.vo.TemplateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <h1>优惠券模板相关的功能控制器</h1>
 * @author: 雒世松
 */
@Slf4j
@RequestMapping("/api/v1/couponTemplate")
@RestController
public class CouponTemplateController {

    /** 构建优惠券模板服务 */
    @Autowired
    private IBuildTemplateService buildTemplateService;

    /** 优惠券模板基础服务 */
    @Autowired
    private ITemplateBaseService templateBaseService;

    @Autowired
    private CouponTemplateService couponTemplateService;

    /**
     * <h2>构建优惠券模板</h2>
     * 127.0.0.1:7001/api/api/v1/couponTemplate
     * 127.0.0.1:9000/imooc/coupon-template/template/build
     * */
    @PostMapping("")
    public CouponTemplate buildTemplate(@Valid @RequestBody TemplateRequest request)
            throws SportException {
        log.info("Build Template: {}", JSON.toJSONString(request));
        return buildTemplateService.buildTemplate(request);
    }

    @GetMapping("")
    public IPage<CouponTemplateItemVO> managerList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                   @RequestParam(required = false, defaultValue = "10") Integer size,
                                                   @RequestParam(required = false) Long storeId) {
        return buildTemplateService.managerList(page, size, storeId);
    }

    @GetMapping("/{id}")
    public CouponTemplateVO detail(@PathVariable Long id) {
        return templateBaseService.getCouponTemplateVO(id);
    }

    @PutMapping("")
    public CouponTemplate update(@RequestBody TemplateRequest request) {
        return buildTemplateService.buildTemplate(request);
    }

    @PutMapping("/status")
    public boolean status(@RequestParam Long id, @RequestParam Integer status) {
        return buildTemplateService.changeStatus(id, status);
    }

    @GetMapping("store-list-simple/{storeId}")
    public List<IdNameVO> getSimpleCouponByStoreId(@PathVariable Long storeId) {
        return couponTemplateService.getSimpleCouponByStoreId(storeId);
    }

    @GetMapping("/share")
    public String getShareCouponCode(@RequestParam Long id, @RequestParam String sharingUserId) {
        return couponTemplateService.getShareCouponCode(id, sharingUserId);
    }



//    /**
//     * <h2>构造优惠券模板详情</h2>
//     * 127.0.0.1:7001/coupon-template/template/info?id=1
//     * */
//    @GetMapping("/{id}")
//    public CouponTemplate buildTemplateInfo(@PathVariable("id") Long id){
//        log.info("Build Template Info For: {}", id);
//        return templateBaseService.buildTemplateInfo(id);
//    }

//    /**
//     * <h2>查找所有可用的优惠券模板</h2>
//     * 127.0.0.1:7001/coupon-template/template/sdk/all
//     * */
//    @GetMapping("/sdk/all")
//    public List<CouponTemplateSDK> findAllUsableTemplate() {
//        log.info("Find All Usable Template.");
//        return templateBaseService.findAllUsableTemplate();
//    }

//    /**
//     * <h2>获取模板 ids 到 CouponTemplateSDK 的映射</h2>
//     * 127.0.0.1:7001/coupon-template/template/sdk/infos
//     * */
//    @GetMapping("/sdk/infos")
//    public Map<Long, CouponTemplateSDK> findIds2TemplateSDK(
//            @RequestParam("ids") Collection<Long> ids) {
//        log.info("FindIds2TemplateSDK: {}", JSON.toJSONString(ids));
//        return templateBaseService.findIds2TemplateSDK(ids);
//    }

//    /**
//     * <h2>获取优惠券模板优惠码</h2>
//     * 127.0.0.1:7001/coupon-template/template/code?id=1
//     * */
//    @GetMapping("/code")
//    public String getCouponCode(@RequestParam("id") Long id) {
//        log.info("Get Coupon Template Code For: {}", id);
//        return templateBaseService.getCouponCode(id);
//    }
}
