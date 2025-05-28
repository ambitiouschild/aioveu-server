package com.aioveu.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ExerciseCoupon;
import com.aioveu.entity.StoreImage;
import com.aioveu.service.ExerciseCouponService;
import com.aioveu.vo.ExerciseCouponVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RequestMapping("/api/v1/exercise-coupon")
@RestController
public class ExerciseCouponController {

    private final ExerciseCouponService exerciseCouponService;

    @Autowired
    private ExerciseCouponService exerciseCouponCouponService;


    public ExerciseCouponController(ExerciseCouponService exerciseCouponService) {
        this.exerciseCouponService = exerciseCouponService;
    }

    @GetMapping("")
    public IPage<ExerciseCouponVO> list(@RequestParam(required = false, defaultValue = "1") Integer page,
                                        @RequestParam(required = false, defaultValue = "10") Integer size,
                                        @RequestParam Long exerciseId) {
        return exerciseCouponCouponService.getManagerAll(page, size, exerciseId);
    }

    @PostMapping("")
    public boolean create(@Valid @RequestBody ExerciseCoupon exerciseCoupon){
        return exerciseCouponCouponService.save(exerciseCoupon);
    }

    @DeleteMapping("/{id}")
    public boolean exerciseDelete(@PathVariable Long id) {
        return exerciseCouponCouponService.deleteExercise(id);
    }

}
