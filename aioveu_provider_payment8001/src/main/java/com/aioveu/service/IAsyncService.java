package com.aioveu.service;


import com.aioveu.entity.CouponTemplate;

import java.util.List;

/**
 * <h1>异步服务接口定义</h1>
 * @author: 雒世松
 */
public interface IAsyncService {

    /**
     * <h2>根据模板异步的创建优惠券码</h2>
     * @param template {@link CouponTemplate} 优惠券模板实体
     * */
    void asyncConstructCouponByTemplate(CouponTemplate template);

    /**
     * <h2>尝试从 Cache 中获取一个优惠券码</h2>
     * @param templateId 优惠券模板主键
     * @return 优惠券码
     * */
    String tryToAcquireCouponCodeFromCache(Long templateId);

    /**
     * <h2>构造一个优惠券码</h2>
     * @param template {@link CouponTemplate} 优惠券模板实体
     * @return String 优惠券码
     */
    String buildOneCouponCode(CouponTemplate template);

    /**
     * 根据优惠券模板id查找所有优惠券码
     * @param templateId
     * @return
     */
    List<String> getAllCodeByTemplateId(Long templateId);

    /**
     * 移除优惠券码
     * @param templateId
     * @param code
     * @return
     */
    boolean removeCouponCode(Long templateId, String code);
}
