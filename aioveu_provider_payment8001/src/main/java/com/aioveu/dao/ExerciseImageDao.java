package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.ExerciseImage;
import com.aioveu.vo.ExerciseImageDetailVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface ExerciseImageDao extends BaseMapper<ExerciseImage> {

    /**
     * 列表
     * @param page
     * @param exerciseId
     * @return
     */
    IPage<ExerciseImageDetailVO> getManagerAll(IPage<ExerciseImageDetailVO> page, Long exerciseId);

}
