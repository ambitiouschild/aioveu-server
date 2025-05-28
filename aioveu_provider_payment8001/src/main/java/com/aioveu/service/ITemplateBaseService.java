package com.aioveu.service;


import com.aioveu.entity.CouponTemplate;
import com.aioveu.exception.SportException;
import com.aioveu.vo.CouponTemplateSDK;
import com.aioveu.vo.CouponTemplateVO;
import com.aioveu.vo.TemplateRule;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * <h1>优惠券模板基础(view, delete...)服务定义</h1>
 * @author: 雒世松
 */
public interface ITemplateBaseService {

    /**
     * <h2>根据优惠券模板 id 获取优惠券模板信息</h2>
     * @param id 模板 id
     * @return {@link CouponTemplate} 优惠券模板实体
     * */
    CouponTemplate buildTemplateInfo(Long id) throws SportException;

    /**
     * <h2>查找所有可用的优惠券模板</h2>
     * @return {@link CouponTemplateSDK}s
     * */
    List<CouponTemplateSDK> findAllUsableTemplate();

    /**
     * <h2>获取模板 ids 到 CouponTemplateSDK 的映射</h2>
     * @param ids 模板 ids
     * @return Map<key: 模板 id， value: CouponTemplateSDK>
     * */
    Map<Long, CouponTemplateSDK> findIds2TemplateSDK(Collection<Long> ids);

    /**
     * <h2>随机从优惠券模板中获取优惠券码</h2>
     * @param templateId 模板id
     * @return CouponCode
     * @throws SportException 异常
     */
    String getRandomCouponCode(Long templateId) throws SportException;

    /**
     * 获取优惠券码
     * @param templateId
     * @return
     */
    List<String> getCouponCodeList(Long templateId);

    /**
     * 获取优惠券模板sdk
     * @param id
     * @param orderLimit
     * @param expiration
     * @return
     */
    CouponTemplateSDK getCouponTemplateSdk(Long id, Integer orderLimit, TemplateRule.Expiration expiration);


    /**
     * 获取优惠券详情
     * @param id
     * @return
     */
    CouponTemplateVO getCouponTemplateVO(Long id);

    /**
     * 移除一个优惠券
     * @param id
     * @param code
     * @return
     */
    boolean removeCouponCode(Long id, String code);
}
