package com.aioveu.controller;

import com.aioveu.service.impl.JoinExerciseSettleRecordServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.UUID;

/**
 * @Author： yao
 * @Date： 2024/11/15 23:13
 * @Describe：
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/settle")
public class JoinExerciseSettleRecordController {

    @Autowired
    JoinExerciseSettleRecordServiceImpl joinExerciseSettleRecordService;

    /**
     * 禁止使用该接口
     * 手动执行部分拼单产品返现
     * @param exerciseId
     */
    @GetMapping("/{exerciseId}")
    public boolean repairSettle(@PathVariable String exerciseId){
        log.info("手动修复拼单产品返现问题开始");
        joinExerciseSettleRecordService.settleJoinOrder(exerciseId,1011l,
                new Date() ,
                String.valueOf(UUID.randomUUID()));
        log.info("手动修复拼单产品返现问题结束");
        return true;
    }
}
