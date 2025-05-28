package com.aioveu.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.dao.GradeAgeDao;
import com.aioveu.entity.GradeAge;
import com.aioveu.service.GradeAgeService;
import com.aioveu.vo.IdNameVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
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
public class GradeAgeServiceImpl extends ServiceImpl<GradeAgeDao, GradeAge> implements GradeAgeService {

    @Override
    public List<IdNameVO> getByStoreId(Long storeId) {
        QueryWrapper<GradeAge> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(GradeAge::getStoreId, storeId)
                .orderByDesc(GradeAge::getCreateDate);
        return list(queryWrapper).stream().map(item -> {
            IdNameVO idNameVO = new IdNameVO();
            BeanUtils.copyProperties(item, idNameVO);
            return idNameVO;
        }).collect(Collectors.toList());
    }

}
