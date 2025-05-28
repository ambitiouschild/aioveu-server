package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.ExerciseImageDao;
import com.aioveu.entity.ExerciseImage;
import com.aioveu.service.ExerciseImageService;
import com.aioveu.utils.OssUtil;
import com.aioveu.vo.ExerciseImageDetailVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class ExerciseImageServiceImpl extends ServiceImpl<ExerciseImageDao, ExerciseImage> implements ExerciseImageService {

    @Autowired
    private ExerciseImageDao exerciseImageDao;

    @Override
    public boolean deleteImage(Long id) {
        ExerciseImage exerciseImage = getById(id);
        if (exerciseImage == null) {
            return false;
        }
        OssUtil.deleteFile(exerciseImage.getUrl());
        return removeById(id);
    }

    @Override
    public boolean deleteByExerciseIdAndImageType(Long exerciseId, Integer imageType) {
        QueryWrapper<ExerciseImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseImage::getExerciseId, exerciseId);
        if (imageType != null) {
            queryWrapper.lambda().eq(ExerciseImage::getImageType, imageType);
        }
        List<ExerciseImage> exerciseImages = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(exerciseImages)) {
            for (ExerciseImage item : exerciseImages) {
                deleteImage(item.getId());
            }
        }
        return true;
    }

    @Override
    public List<ExerciseImage> getByExerciseId(Long exerciseId) {
        QueryWrapper<ExerciseImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseImage::getExerciseId, exerciseId);
        return list(queryWrapper);
    }

    @Override
    public ExerciseImageDetailVO managerDetail(Long id) {
        ExerciseImage exerciseImage = getById(id);
        if (exerciseImage != null) {
            ExerciseImageDetailVO exerciseImageDetailVO = new ExerciseImageDetailVO();
            BeanUtils.copyProperties(exerciseImage, exerciseImageDetailVO);
            return exerciseImageDetailVO;
        }
        return null;
    }

    @Override
    public IPage<ExerciseImageDetailVO> getManagerAll(int page, int size, Long exerciseId) {
        return exerciseImageDao.getManagerAll(new Page<>(page, size), exerciseId);
    }

    @Override
    public boolean deleteByExerciseId(Long exerciseId) {
        QueryWrapper<ExerciseImage> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(ExerciseImage::getExerciseId, exerciseId);
        return remove(queryWrapper);
    }
}
