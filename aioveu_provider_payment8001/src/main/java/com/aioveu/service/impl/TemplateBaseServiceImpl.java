package com.aioveu.service.impl;

import com.aioveu.entity.CouponTemplate;
import com.aioveu.exception.SportException;
import com.aioveu.service.*;
import com.aioveu.vo.CouponTemplateSDK;
import com.aioveu.vo.CouponTemplateVO;
import com.aioveu.vo.TemplateRule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * <h1>优惠券模板基础服务接口实现</h1>
 * @author: 雒世松
 */
@Slf4j
@Service
public class TemplateBaseServiceImpl implements ITemplateBaseService {

    @Autowired
    private CouponTemplateService couponTemplateService;

    @Autowired
    private IAsyncService iAsyncService;

    @Autowired
    private CompanyService companyService;

    /**
     * <h2>根据优惠券模板 id 获取优惠券模板信息</h2>
     * @param id 模板 id
     * @return {@link CouponTemplate} 优惠券模板实体
     */
    @Override
    public CouponTemplate buildTemplateInfo(Long id) throws SportException {
        CouponTemplate template = couponTemplateService.getById(id);
        if (template == null) {
            log.error("优惠券不存在:" + id);
            throw new SportException(id + "优惠券不存在!");
        }
        return template;
    }

    @Override
    public CouponTemplateSDK getCouponTemplateSdk(Long id, Integer orderLimit, TemplateRule.Expiration expiration) {
        CouponTemplate template = buildTemplateInfo(id);
        TemplateRule templateRule = template.getRule();
        if (templateRule == null) {
            templateRule = new TemplateRule();
        }
        // 外部的有效期高于优惠券本身的
        if (orderLimit != null) {
            templateRule.setOrderLimit(orderLimit);
        }
        if (expiration != null && expiration.getPeriod() != null) {
            templateRule.setExpiration(expiration);
        }
        return template2TemplateSDK(template);
    }

    @Override
    public CouponTemplateVO getCouponTemplateVO(Long id) {
        CouponTemplate template = buildTemplateInfo(id);
        CouponTemplateVO couponTemplateVO = new CouponTemplateVO();
        BeanUtils.copyProperties(template, couponTemplateVO);
        couponTemplateVO.setProductLine(template.getProductLine().getCode());
        couponTemplateVO.setCategory(template.getCategory().getCode());
        couponTemplateVO.setTarget(template.getTarget().getCode());
        couponTemplateVO.setDesc(template.getIntro());
        couponTemplateVO.setCount(template.getCouponCount());
        couponTemplateVO.setShowStore(template.getShowStore());

        if (template.getCompanyId() != null && template.getCompanyId() > 0) {
            couponTemplateVO.setCompanyName(companyService.getById(template.getCompanyId()).getName());
        }
        return couponTemplateVO;
    }

    /**
     * <h2>查找所有可用的优惠券模板</h2>
     * @return {@link CouponTemplateSDK}s
     */
    @Override
    public List<CouponTemplateSDK> findAllUsableTemplate() {

        List<CouponTemplate> templates =
                couponTemplateService.findAllByAvailableAndExpired(
                        true, false);

        return templates.stream()
                .map(this::template2TemplateSDK).collect(Collectors.toList());
    }

    /**
     * <h2>获取模板 ids 到 CouponTemplateSDK 的映射</h2>
     * @param ids 模板 ids
     * @return Map<key: 模板 id, value: CouponTemplateSDK>
     */
    @Override
    public Map<Long, CouponTemplateSDK> findIds2TemplateSDK(
            Collection<Long> ids) {

        List<CouponTemplate> templates = couponTemplateService.listByIds(ids);
        return templates.stream().map(item -> template2TemplateSDK(item))
                .collect(Collectors.toMap(
                        CouponTemplateSDK::getId, Function.identity()
                ));
    }

    @Override
    public String getRandomCouponCode(Long templateId) throws SportException {
        CouponTemplate couponTemplate = buildTemplateInfo(templateId);
        if (couponTemplate.getCouponCount() != -1) {
            return iAsyncService.tryToAcquireCouponCodeFromCache(templateId);
        }
        return iAsyncService.buildOneCouponCode(couponTemplate);
    }

    @Override
    public List<String> getCouponCodeList(Long templateId) {
        CouponTemplate couponTemplate = buildTemplateInfo(templateId);
        if (couponTemplate.getCouponCount() != -1) {
            return iAsyncService.getAllCodeByTemplateId(templateId);
        }
        return null;
    }

    @Override
    public boolean removeCouponCode(Long id, String code) {
        CouponTemplate couponTemplate = buildTemplateInfo(id);
        if (couponTemplate.getCouponCount() != -1) {
            List<String> couponCodeList = iAsyncService.getAllCodeByTemplateId(id);
            if (couponCodeList.contains(code)) {
                return iAsyncService.removeCouponCode(id, code);
            }
            return false;
        }
        return true;
    }

    /**
     * <h2>将 CouponTemplate 转换为 CouponTemplateSDK</h2>
     * */
    private CouponTemplateSDK template2TemplateSDK(CouponTemplate template) {
        return new CouponTemplateSDK(
                template.getId(),
                template.getName(),
                template.getLogo(),
                template.getIntro(),
                template.getCategory().getCode(),
                template.getProductLine().getCode(),
                template.getTemplateKey(),  // 并不是拼装好的 Template Key
                template.getTarget().getCode(),
                template.getActivePrice(),
                template.getRule(),
                template.getCompanyId(),
                template.getStoreId(),
                template.getProductId()
        );
    }
}
