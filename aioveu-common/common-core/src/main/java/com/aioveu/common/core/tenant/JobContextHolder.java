package com.aioveu.common.core.tenant;


import lombok.extern.slf4j.Slf4j;

/**
 * @ClassName: JobContextHolder
 * @Description TODO JobContext工具类
 *                      * <p>
 *                      * 使用 TransmittableThreadLocal 存储当前线程的租户ID，确保线程安全
 *                      TransmittableThreadLocal(TTL) 是阿里开源的一个线程上下文传递工具，
 *                      用于解决 线程池/异步场景下的 ThreadLocal 数据丢失问题。
 *                      * 支持异步任务、线程池、消息队列等场景的上下文传递
 *                      * </p>
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/8/6 18:54
 * @Version 1.0
 **/
/*
 * ✅ Job 上下文识别（非常关键）
 *
 * */
@Slf4j
public class JobContextHolder {


    // 标记当前线程是否为Job执行线程
    private static final ThreadLocal<Boolean> JOB_CONTEXT =
            ThreadLocal.withInitial(() -> Boolean.FALSE);

    public static void setJob() {
        JOB_CONTEXT.set(Boolean.TRUE);
    }

    public static boolean isJob() {
        return Boolean.TRUE.equals(JOB_CONTEXT.get());
    }

    public static void clear() {
        JOB_CONTEXT.remove();
    }

}
