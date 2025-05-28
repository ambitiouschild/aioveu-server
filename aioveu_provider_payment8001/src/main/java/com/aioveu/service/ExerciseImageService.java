package com.aioveu.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.ExerciseImage;
import com.aioveu.vo.ExerciseImageDetailVO;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
public interface ExerciseImageService extends IService<ExerciseImage> {


    /**
     * 删除图片
     * @param id
     * @return
     */
    boolean deleteImage(Long id);

    /**
     * 通过活动id和图片类型删除对应的图片
     * @param exerciseId
     * @param imageType
     * @return
     */
    boolean deleteByExerciseIdAndImageType(Long exerciseId, Integer imageType);

    /**
     * 通过活动id查询图片
     * @param exerciseId
     * @return
     */
    List<ExerciseImage> getByExerciseId(Long exerciseId);

    /**
     * 管理平台活动详情
     * @param id
     * @return
     */
    ExerciseImageDetailVO managerDetail(Long id);

    /**
     * 获取列表
     * @param page
     * @param size
     * @param exerciseId
     * @return
     */
    IPage<ExerciseImageDetailVO> getManagerAll(int page, int size, Long exerciseId);

    /**
     * 通过活动id删除对应的图片
     * @param exerciseId
     * @return
     */
    boolean deleteByExerciseId(Long exerciseId);


}
