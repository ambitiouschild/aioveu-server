package com.aioveu.system.aioveu05Dept.mapper;

import com.aioveu.common.mybatis.annotation.DataPermission;
import com.aioveu.system.aioveu05Dept.model.entity.Dept;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
  *@ClassName: DeptMapper
  *@Description TODO
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/31 17:51
  *@Version 1.0
  **/

@Mapper
public interface DeptMapper extends BaseMapper<Dept> {
    @DataPermission(deptIdColumnName = "id")
    @Override
    List<Dept> selectList(@Param(Constants.WRAPPER) Wrapper<Dept> queryWrapper);
}
