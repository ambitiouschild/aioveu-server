package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ExerciseCoupon;
import com.aioveu.vo.ExerciseCouponSimpleVO;
import com.aioveu.vo.ExerciseCouponVO;
import com.aioveu.vo.StoreImageDetailVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface ExerciseCouponDao extends BaseMapper<ExerciseCoupon> {

    /**
     * 列表
     * @param page
     * @param exerciseId
     * @return
     */
    IPage<ExerciseCouponVO> getManagerAll(IPage<StoreImageDetailVO> page, Long exerciseId);

    /**
     * 查询优惠券信息
     * @param exerciseId
     * @return
     */
    List<ExerciseCouponSimpleVO> getByExerciseId(Long exerciseId);

}
