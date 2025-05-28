package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ExercisePushRewardConfig;
import com.aioveu.form.ExercisePushRewardConfigForm;
import com.aioveu.form.ExerciseTopicForm;
import com.aioveu.service.ExercisePushRewardConfigService;
import com.aioveu.vo.ExercisePushRewardConfigVO;
import com.aioveu.vo.TopicExerciseVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/exercise-push-reward-config")
@RestController
public class ExercisePushRewardConfigController {

    @Autowired
    private ExercisePushRewardConfigService exercisePushRewardConfigService;

    @GetMapping("")
    public List<ExercisePushRewardConfig> getByTopicAndExercise(@RequestParam Long exerciseId, @RequestParam Long topicId) {
        return exercisePushRewardConfigService.getByTopicAndExercise(exerciseId, topicId);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody ExerciseTopicForm form){
        return exercisePushRewardConfigService.create(form);
    }

    /**
     * 获取所有活动设置的奖励
     * @param page
     * @param size
     * @param exerciseId
     * @return
     */
    @GetMapping("/reward")
    public IPage<ExercisePushRewardConfigVO> getReward(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                      @RequestParam(required = false, defaultValue = "10") Integer size,
                                                      @RequestParam(required = false) Long exerciseId) {
        return exercisePushRewardConfigService.getExerciseAndReward(page,size,exerciseId);
    }

    /**
     * 修改奖励
     *
     * @param id            id
     * @param reward        奖励
     * @param rewardProduct 奖励产品
     * @return {@link Boolean}
     */
    @PutMapping("/change")
    public Boolean changeReward(@RequestParam(required = false) Long id,
                                @RequestParam(required = false) BigDecimal reward,
                                @RequestParam(required = false) String rewardProduct
        ){
        return exercisePushRewardConfigService.changeReward(id,reward,rewardProduct);
    }

    /**
     * 为没设置奖励增加奖励
     *
     * @param exercisePushRewardConfigForm 运动推动奖励配置形式
     * @return {@link Boolean}
     */
    @PostMapping("/reward-add")
    public Boolean addReward(@Valid @RequestBody ExercisePushRewardConfigForm exercisePushRewardConfigForm){
        return exercisePushRewardConfigService.createRewardConfig(exercisePushRewardConfigForm);
    }

    /**
     * 获取未设置到店礼跟奖励金额的活动
     * @param page
     * @param size
     * @param exerciseName
     * @return
     */
    @GetMapping("un-reward")
    public IPage<TopicExerciseVO> getUnSetReward(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                 @RequestParam(required = false, defaultValue = "10") Integer size,
                                                 @RequestParam(required = false) String exerciseName) {
        return exercisePushRewardConfigService.getUnSetReward(page, size, exerciseName);
    }

}
