package com.aioveu.config;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;


/**
 * @Description: TODO
 * @Author: 雒世松
 * @Date: 2025/5/29 0:25
 * @param null
 * @return:
 **/




@Slf4j
@EnableAsync
@Configuration
public class AsyncPoolConfig implements AsyncConfigurer {

    @Bean
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        //缓冲队列任务的个数
        executor.setQueueCapacity(20);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("SportAsync_");

        //是否等待所有线程完毕之后关闭线程池
        executor.setWaitForTasksToCompleteOnShutdown(true);
        //上面的操作等待时间
        executor.setAwaitTerminationSeconds(60);

        //拒绝策略 等待执行
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    /**
     * 处理没有返回值的异步任务异常的处理类
     * @return
     */
    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new AsyncExceptionHandler();
    }

    class AsyncExceptionHandler implements AsyncUncaughtExceptionHandler {

        @Override
        public void handleUncaughtException(Throwable throwable, Method method, Object... objects) {
            log.error("Async Error: {}, method: {}, params: {}", throwable.getMessage(), method.getName(), JSON.toJSONString(objects));
            throwable.printStackTrace();
            //TODO 发送邮件或者短信给相应的负责人
        }
    }
}
