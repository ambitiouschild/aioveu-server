package com.aioveu.tenant.handler;

import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @ClassName: XxlJobSampleHandler
 * @Description TODO xxl-job 测试示例（Bean模式）
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:02
 * @Version 1.0
 **/
@Component
@Slf4j
public class XxlJobSampleHandler {

    @XxlJob("demoJobHandler")
    public void demoJobHandler() {
        log.info("XXL-JOB, Hello World.");
    }

}
