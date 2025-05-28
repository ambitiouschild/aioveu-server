package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeCoachDao;
import com.aioveu.entity.GradeCoach;
import com.aioveu.entity.StoreCoach;
import com.aioveu.enums.ExerciseCategory;
import com.aioveu.exception.SportException;
import com.aioveu.service.CategoryService;
import com.aioveu.service.GradeCoachService;
import com.aioveu.vo.GradeVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Slf4j
@Service
public class GradeCoachServiceImpl extends ServiceImpl<GradeCoachDao, GradeCoach> implements GradeCoachService {

    @Autowired
    private CategoryService categoryService;

    @Override
    public List<Long> getByUserId(String userId) {
        return getBaseMapper().getByUserId(userId);
    }

    @Override
    public List<Long> getCoachIdByUserId(String userId) {
        List<Long> coachList = getByUserId(userId);
        if (CollectionUtils.isEmpty(coachList)) {
            throw new SportException("该用户未配置老师权限");
        }
        return coachList;
    }

    @Override
    public IPage<GradeVO> getGradeByCoachId(int page, int size, String userId, Integer type, String date) {
        List<Long> coachList = getCoachIdByUserId(userId);
        return getBaseMapper().getGradeByCoachId(new Page<>(page, size), coachList, type, categoryService.getByCode(ExerciseCategory.EXPERIENCE.getCode()), date);
    }

    @Override
    public boolean deleteUpdateCoach(Long gradeId, List<Long> coachList) {
        QueryWrapper<GradeCoach> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeCoach::getGradeId, gradeId);
        remove(queryWrapper);
        return saveBatch(coachList.stream().map(id -> {
            GradeCoach gc = new GradeCoach();
            gc.setGradeId(gradeId);
            gc.setCoachId(id);
            return gc;
        }).collect(Collectors.toList()));
    }

    @Override
    public List<StoreCoach> getByGradeId(Long gradeId) {
        return getBaseMapper().getByGradeId(gradeId);
    }
}
