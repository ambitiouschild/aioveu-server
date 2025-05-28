package com.aioveu.receive;

import com.aioveu.config.mq.DirectMqConfig;
import com.aioveu.form.GradeEnrollUserForm;
import com.aioveu.service.GradeEnrollUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @description 直连交换槽子约课
 * @author: 雒世松
 * @date: 2025/03/12 0019 22:45
 */
@Slf4j
@Component
@RabbitListener(queues = DirectMqConfig.GRADE_ENROLL_QUEUE)
public class DirectGradeEnrollReceiver {

    @Autowired
    private GradeEnrollUserService gradeEnrollUserService;
 
    @RabbitHandler
    public void process(GradeEnrollUserForm form) {
        try {
            gradeEnrollUserService.create(form);
        }catch (Exception e){
            e.printStackTrace();
            log.error("异步约课异常" + e);
        }
    }
 
}