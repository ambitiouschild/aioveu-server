package com.aioveu.pay.aioveu10MqSendRecord.controller;


import com.aioveu.pay.aioveu10MqSendRecord.model.vo.SendRecordStats;
import com.aioveu.pay.aioveu10MqSendRecord.service.MqSendRecordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Map;

/**
 * @ClassName: MqStatsController
 * @Description TODO 获取发送记录统计
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/12 19:29
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/aioveu/api/v8/admin/pay/api/mq/stats")
@RequiredArgsConstructor
public class MqStatsController {

    private final MqSendRecordService mqSendRecordService;

    /**
     * 获取发送记录统计
     */
    @GetMapping("/send-record")
    public SendRecordStats getSendRecordStats(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        if (startTime == null) {
            // 默认统计最近24小时
//            startTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
            // 默认统计最近24小时
            startTime = LocalDateTime.now().minusHours(24);
        }

        if (endTime == null) {
//            endTime = new Date();
            endTime = LocalDateTime.now();
        }

        log.info("获取发送记录统计: startTime={}, endTime={}", startTime, endTime);

        return mqSendRecordService.getDetailedSendRecordStats(startTime, endTime);
    }

    /**
     * 获取发送记录统计（Map格式）
     */
    @GetMapping("/send-record/simple")
    public Map<String, Object> getSendRecordStatsSimple(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endTime) {

        SendRecordStats stats = getSendRecordStats(startTime, endTime);
        return stats.toMap();
    }

    /**
     * 获取今日统计
     */
    @GetMapping("/send-record/today")
    public SendRecordStats getTodayStats() {
//        Date startTime = new Date(System.currentTimeMillis() - 24 * 60 * 60 * 1000L);
//        Date endTime = new Date();

        LocalDateTime startTime = LocalDateTime.now().minusHours(24);
        LocalDateTime endTime = LocalDateTime.now();


        return mqSendRecordService.getDetailedSendRecordStats(startTime, endTime);
    }
}
