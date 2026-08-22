package com.aioveu.common.core.tenant;

import lombok.extern.slf4j.Slf4j;
import com.alibaba.ttl.TransmittableThreadLocal;

/**
 * @ClassName: TenantContextHolder
 * @Description TODO 租户上下文工具类
 *                      * <p>
 *                      * 使用 TransmittableThreadLocal 存储当前线程的租户ID，确保线程安全
 *                      TransmittableThreadLocal(TTL) 是阿里开源的一个线程上下文传递工具，
 *                      用于解决 线程池/异步场景下的 ThreadLocal 数据丢失问题。
 *                      * 支持异步任务、线程池、消息队列等场景的上下文传递
 *                      * </p>
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/23 13:16
 * @Version 1.0
 **/


/**
 * 租户上下文工具类（基础设施层）
 *
 * 原则：
 * 1. 只存“事实”，不做判断
 * 2. 平台接口 / 业务接口通过标记区分
 * 3. 全链路支持 TTL（线程池 / 异步 / MQ）
 */
@Slf4j
public class TenantContextHolder {

    /**
     * 租户ID线程本地变量
     * 使用 TransmittableThreadLocal 支持父子线程和线程池场景的值传递
     */
    private static final TransmittableThreadLocal<Long> TENANT_ID = new TransmittableThreadLocal<>();

    /* =========================
     * 强制忽略租户（MP 专用）
     * =========================
     * 用于极少数需要“临时绕过 MP”的场景
     * ⚠️ 非平台接口慎用
     * ========================= */
    private static final TransmittableThreadLocal<Boolean> FORCE_IGNORE = new TransmittableThreadLocal<>();


    /* =========================
     * 平台接口标记
     * =========================
     * true  = 当前请求为平台级接口（登录前、公共接口）
     * false = 业务接口（受租户隔离保护）
     * ========================= */
    private static final TransmittableThreadLocal<Boolean> PLATFORM_FLAG =
            new TransmittableThreadLocal<>();


    /**
     * 设置当前租户 ID
     *
     * @param tenantId 租户 ID
     */
    public static void setTenantId(Long tenantId) {
        if (tenantId != null) {
            TENANT_ID.set(tenantId);
            log.debug("【TenantContextHolder】设置当前租户ID: {}", tenantId);
        }
    }

    /**
     * 获取当前租户 ID
     *
     * @return 租户ID，如果未设置则返回 null
     */
    public static Long getTenantId() {
        return TENANT_ID.get();
    }


    /* =======================
     * 平台接口标记
     * ======================= */

    /**
     * 标记当前请求为平台接口
     * 用于登录前、公共接口等不依赖租户上下文的场景
     */
    /**
     * ⚠️ 使用纪律：
     * 1. 平台接口：只调用 markPlatform()
     * 2. 业务接口：不碰 TenantContextHolder
     * 3. forceIgnore()：仅限 MP 内部，禁止业务代码调用
     * 4. 每次请求结束必须 clear()
     */
    public static void markPlatform() {
        PLATFORM_FLAG.set(true);
        log.debug("【TenantContextHolder】 markPlatform=true");
    }

    public static boolean isPlatform() {
        return Boolean.TRUE.equals(PLATFORM_FLAG.get());
    }


    /* =======================
     * 强制忽略租户（MP 用）
     * ======================= */
    /**
     * 设置忽略租户标志
     *
     * @param ignore 是否忽略
     */
    public static void setIgnoreTenant(boolean ignore) {
        if (ignore) {
            FORCE_IGNORE.set(true);
        } else {
            FORCE_IGNORE.remove();
        }
        log.debug("【TenantContextHolder】设置忽略租户标志: {}", ignore);
    }

    public static void clearIgnoreTenant() {
        FORCE_IGNORE.remove();
    }
    /**
     * 是否忽略租户
     *
     * @return true-忽略，false-不忽略
     */
    public static boolean isIgnoreTenant() {
        Boolean ignore = FORCE_IGNORE.get();
        return ignore != null && ignore;
    }

    /* =======================
     * 清除上下文
     * ======================= */


    /**
     * 清除当前线程的租户上下文
     * <p>
     * 必须在请求结束时调用，避免线程池复用导致的数据泄露
     * </p>
     */
    public static void clear() {

        // 严格顺序：业务 → 平台 → 强制
        TENANT_ID.remove();
        PLATFORM_FLAG.remove();
        FORCE_IGNORE.remove();
        log.info("【TenantContextHolder】清除租户上下文");
    }





}
