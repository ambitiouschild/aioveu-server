package com.aioveu.oms.aioveu12OrderExportTask.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: ExportNoGenerato
 * @Description TODO 订单导出任务生成器（✅ 线程安全、可并发）
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:00
 * @Version 1.0
 **/
@Component
public class ExportTaskNoGenerator {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public String generateExportTaskNo(Long tenantId) {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String key = "export:no:" + date;

        Long seq = redisTemplate.opsForValue().increment(key);
        if (seq == 1) {
            redisTemplate.expire(key, 1, TimeUnit.DAYS);
        }

        return "EXPAIOVEU" + tenantId + date + String.format("%06d", seq);



        /*

        OrderExportTask task = new OrderExportTask();
        task.setExportNo(ExportTaskNoGenerator.generateExportTaskNo());
        task.setStatus(OrderExportTaskStatusEnum.PENDING.getValue());
        * */
    }
}
