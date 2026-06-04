package com.aioveu.common.tenant;


import com.alibaba.ttl.TransmittableThreadLocal;
import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: ClientContextHolder
 * @Description TODO  客户端上下文（ThreadLocal）
 *                      * <p>
 *                      * 使用 TransmittableThreadLocal 存储当前线程的ClientId，确保线程安全
 *                      TransmittableThreadLocal(TTL) 是阿里开源的一个线程上下文传递工具，
 *                      用于解决 线程池/异步场景下的 ThreadLocal 数据丢失问题。
 *                      * 支持异步任务、线程池、消息队列等场景的上下文传递
 *                      * </p>
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/4 22:19
 * @Version 1.0
 **/
@Slf4j
public class ClientContextHolder {

    /**
     * clientId线程本地变量
     * 使用 TransmittableThreadLocal 支持父子线程和线程池场景的值传递
     */
    private static final TransmittableThreadLocal<String> CLIENT_ID_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 忽略clientId标志（用于某些场景下临时跳过clientId过滤）
     */
    private static final TransmittableThreadLocal<Boolean> IGNORE_CLIENT_HOLDER = new TransmittableThreadLocal<>();

    /**
     * 设置当前clientId
     *
     * @param clientId 客户端 ID
     */
    public static void setClientId(String clientId) {
        if (clientId != null) {
            CLIENT_ID_HOLDER.set(clientId);
            log.debug("【ClientContextHolder】设置当前clientId: {}", clientId);
        }
    }

    /**
     * 获取当前客户端 ID
     *
     * @return 客户端ID，如果未设置则返回 null
     */
    public static String getClientId() {
        return CLIENT_ID_HOLDER.get();
    }

    /**
     * 设置忽略客户端标志
     *
     * @param ignore 是否忽略
     */
    public static void setIgnoreClient(boolean ignore) {
        IGNORE_CLIENT_HOLDER.set(ignore);
        log.debug("【ClientContextHolder】设置忽略客户端标志: {}", ignore);
    }

    /**
     * 是否忽略客户端
     *
     * @return true-忽略，false-不忽略
     */
    public static boolean isIgnoreClient() {
        Boolean ignore = IGNORE_CLIENT_HOLDER.get();
        return ignore != null && ignore;
    }

    /**
     * 清除当前线程的客户端上下文
     * <p>
     * 必须在请求结束时调用，避免线程池复用导致的数据泄露
     * </p>
     */
    public static void clear() {
        CLIENT_ID_HOLDER.remove();
        IGNORE_CLIENT_HOLDER.remove();
        log.info("【ClientContextHolder】清除客户端上下文");
    }
}
