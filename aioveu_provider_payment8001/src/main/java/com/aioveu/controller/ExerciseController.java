package com.aioveu.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.auth.common.utils.OauthUtils;
import com.aioveu.entity.ExerciseImage;
import com.aioveu.form.ExerciseCustomForm;
import com.aioveu.form.ExerciseForm;
import com.aioveu.form.PushExerciseForm;
import com.aioveu.service.CodeService;
import com.aioveu.service.ExerciseImageService;
import com.aioveu.service.ExerciseService;
import com.aioveu.vo.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/2/23 0023 16:00
 */
@Slf4j
@RequestMapping("/api/v1/exercise")
@RestController
public class ExerciseController {

    private final ExerciseService exerciseService;

    @Autowired
    private CodeService codeService;

    @Autowired
    private ExerciseImageService exerciseImageService;

    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }

    @GetMapping("")
    public IPage<ExerciseManagerItemVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             @RequestParam(required = false) Long storeId,
                                             @RequestParam(required = false) Long categoryId,
                                             @RequestParam(required = false) Integer status,
                                             @RequestParam(required = false) String storeName) {
        return exerciseService.getManagerAll(page, size, storeId, storeName, categoryId, status);
    }

    @GetMapping("/{id}")
    public ExerciseManagerDetailVO detail(@PathVariable Long id) {
        return exerciseService.managerDetail(id);
    }

    @PutMapping("")
    public boolean update(@Valid @RequestBody ExerciseForm exercise) {
        return exerciseService.createOrUpdate(exercise);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody ExerciseForm exercise) {
        return exerciseService.createOrUpdate(exercise);
    }

    @PutMapping("/status")
    public boolean status(@RequestParam Long id, @RequestParam Integer status) {
        return exerciseService.changeStatus(id, status);
    }

    @PostMapping("/image")
    public boolean image(@Valid @RequestBody ExerciseImage exerciseImage) {
        return exerciseImageService.save(exerciseImage);
    }

    @GetMapping("/image/{exerciseId}")
    public List<ExerciseImage> imageList(@PathVariable Long exerciseId) {
        return exerciseImageService.list(new QueryWrapper<ExerciseImage>()
                .eq("exercise_id", exerciseId)
                .orderByAsc("imageType", "priority")
        );
    }

    @PutMapping("/image")
    public boolean imageUpdate(@Valid @RequestBody ExerciseImage exerciseImage) {
        return exerciseImageService.saveOrUpdate(exerciseImage);
    }

    @DeleteMapping("/image/{id}")
    public boolean imageDelete(@PathVariable Long id) {
        return exerciseImageService.deleteImage(id);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail/{id}")
    public ExerciseVO miniDetail(@PathVariable Long id, @RequestParam(required = false, defaultValue = "false") Boolean preview) {
        return exerciseService.getDetail(id, preview);
    }

    /**
     * 详情
     *
     * @param id
     * @return
     */
    @GetMapping("/detail-count/{id}")
    public ExerciseCountDetailVO miniCountDetail(@PathVariable Long id) {
        return exerciseService.getCountDetail(id);
    }

    /**
     * 活动列表 老的 新的是课程/服务列表  传storeId 用于管理端的活动列表
     *
     * @param param
     * @return
     */
    @PostMapping("/miniList")
    public IPage<BaseServiceItemVO> list(@RequestParam Map<String, Object> param) throws Exception {
        return exerciseService.getAll(param);
    }

    /**
     * 活动详情页二维码
     *
     * @param id
     * @return
     */
    @GetMapping("/code/{id}")
    public String pageCode(@PathVariable Long id) {
        return codeService.exercisePageCode(id);
    }

    /**
     * 灵活课包 活动详情页二维码
     * @param id
     * @return
     */
    @GetMapping("/custom-code/{id}")
    public FileVO customPageCode(@PathVariable Long id) {
        return codeService.createCustomExercisePageCode(id);
    }


    @DeleteMapping("/{id}")
    public boolean exerciseDelete(@PathVariable Long id) {
        return exerciseService.exerciseDelete(id);
    }

    @GetMapping("/copy/{id}")
    public Long exerciseCopy(@PathVariable Long id) {
        return exerciseService.copyById(id);
    }

    @PostMapping("/custom")
    public boolean createCustom(@Valid @RequestBody ExerciseCustomForm form) {
        return exerciseService.exerciseCustom(form);
    }

    @GetMapping("/custom")
    public IPage<ExerciseManagerItemVO> getCustomList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size,
                                                  @RequestParam(required = false) Long storeId) {
        return exerciseService.getCustomList(page, size, storeId);
    }

    @PutMapping("push")
    public boolean pushUpdate(@Valid @RequestBody PushExerciseForm form) {
        return exerciseService.createOrUpdatePush(form);
    }

    @PostMapping("push")
    public boolean pushCreate(@Valid @RequestBody PushExerciseForm form) {
        return exerciseService.createOrUpdatePush(form);
    }

    @GetMapping("/push/list")
    public IPage<PushExerciseItemVO> pushList(@RequestParam(required = false, defaultValue = "1") Integer page,
                                             @RequestParam(required = false, defaultValue = "10") Integer size,
                                             @RequestParam(required = false) Long storeId) {
        return exerciseService.getPushList(page, size, storeId);
    }

    @GetMapping("/push/detail")
    public PushExerciseForm getPush(@RequestParam Long id, @RequestParam Long topicId) {
        return exerciseService.getPush(id, topicId);
    }

    @GetMapping("/experience")
    public List<IdNameVO> getExperience() {
        return exerciseService.getExperience();
    }

    @GetMapping("/vip-price/{id}")
    public BigDecimal getPush(@PathVariable Long id) {
        return exerciseService.getVipPrice(id, OauthUtils.getCurrentUsername());
    }

}
