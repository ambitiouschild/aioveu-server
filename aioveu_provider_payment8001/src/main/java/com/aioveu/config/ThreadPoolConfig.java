package com.aioveu.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @description 线程池的配置
 * @author: 雒世松
 * @date: 2025/4/21 11:12
 */
@Configuration
@EnableAsync
@Slf4j
public class ThreadPoolConfig {

    @Bean("syncDataServiceExecutor")
    public ThreadPoolTaskExecutor syncDataServiceExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        //配置核心线程数
        executor.setCorePoolSize(10);
        //配置最大线程数
        executor.setMaxPoolSize(20);
        //配置队列大小
        executor.setQueueCapacity(1000);
        //配置线程池中的线程的名称前缀
        executor.setThreadNamePrefix("data-sync-pool-");
        // 空闲线程最大存活时间，默认60秒
        executor.setKeepAliveSeconds(30);
        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
        // 抛出RejectedExecutionHandler异常
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
        // 不在新线程中执行任务，而是有调用者所在的线程来执行
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy()); // 丢掉最早未处理的任务
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardPolicy());       // 丢掉新提交的任务
        executor.setRejectedExecutionHandler(new RejectedExecutionHandler() {
            @Override
            public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                log.error("数据同步线程池已满，无法接收新的任务");
            }
        });
        //执行初始化
        executor.initialize();
        return executor;
    }


}
