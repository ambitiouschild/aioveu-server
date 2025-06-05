package com.aioveu.common.factory;

import cn.hutool.core.util.StrUtil;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: TODO 自定义 ThreadFactory 重命名线程池
 * @Author: 雒世松
 * @Date: 2025/6/5 15:39
 * @param
 * @return:
 **/

public class NamedThreadFactory implements ThreadFactory {

    private final AtomicInteger poolNumber = new AtomicInteger(1);

    private final ThreadGroup threadGroup;

    private final AtomicInteger threadNumber = new AtomicInteger(1);

    public final String namePrefix;

    public NamedThreadFactory(String name) {

        this.threadGroup = Thread.currentThread().getThreadGroup();

        if (StrUtil.isBlank(name)) {
            name = "pool";
        }
        namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(threadGroup, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        if (t.isDaemon()) {
            t.setDaemon(false);
        }

        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }
        return t;
    }
}
