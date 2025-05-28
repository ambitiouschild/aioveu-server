package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.TopicExercise;
import com.aioveu.form.TopicExerciseForm;
import com.aioveu.form.TopicExercisePrePayForm;
import com.aioveu.service.TopicExerciseService;
import com.aioveu.vo.ExerciseTopicItemVO;
import com.aioveu.vo.ExerciseTopicPrePayVO;
import com.aioveu.vo.TopicExerciseJoinDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/topic-exercise")
@RestController
public class TopicExerciseController {

    @Autowired
    private TopicExerciseService topicExerciseService;

    @GetMapping("/join-list")
    public TopicExerciseJoinDetailVO joinList(@RequestParam Long topicId, @RequestParam Long storeId) {
        return topicExerciseService.joinList(topicId, storeId);
    }

    @PostMapping("/topic")
    public IPage<ExerciseTopicItemVO> exerciseList(@Valid @RequestBody TopicExerciseForm form) {
        return topicExerciseService.exerciseList(form);
    }

    @PostMapping("")
    public boolean join(@Valid @RequestBody TopicExercise topicExercise) {
        return topicExerciseService.join(topicExercise);
    }

    @DeleteMapping("")
    public boolean unJoin(@Valid @RequestBody TopicExercise topicExercise) {
        return topicExerciseService.unJoin(topicExercise);
    }

    @PostMapping("/pre-pay")
    public ExerciseTopicPrePayVO prePay(@Valid @RequestBody TopicExercisePrePayForm form) {
        return topicExerciseService.prePay(form);
    }

}
