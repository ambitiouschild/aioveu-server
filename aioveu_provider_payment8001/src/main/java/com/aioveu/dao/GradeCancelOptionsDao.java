package com.aioveu.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.GradeCancelOptions;
import com.aioveu.entity.GradeSignEvaluate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GradeCancelOptionsDao extends BaseMapper<GradeCancelOptions> {

}
