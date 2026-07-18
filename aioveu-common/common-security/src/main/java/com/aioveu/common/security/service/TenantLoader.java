package com.aioveu.common.security.service;


/**
 * @ClassName: TenantLoader
 * @Description TODO
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/7/10 18:33
 * @Version 1.0
 **/
/**
 * 租户加载函数接口
 *
 * <p>作用：
 * <ul>
 *   <li>解耦 PublicTenantResolver 与实际查询方式</li>
 *   <li>避免 Filter → Feign → 自调用</li>
 * </ul>
 *
 * <p>实现方：
 * <ul>
 *   <li>由 aioveu-tenant 提供</li>
 *   <li>直接访问 DB / Cache</li>
 *   <li>禁止使用 Feign</li>
 * </ul>
 */
public interface TenantLoader {

    /**
     * 根据 clientId 加载租户ID
     *
     * @param clientId 客户端ID
     * @return 租户ID
     */
    Long load(String clientId);
}
