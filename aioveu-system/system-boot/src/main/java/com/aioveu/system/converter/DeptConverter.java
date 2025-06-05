package com.aioveu.system.converter;

import com.aioveu.system.model.entity.SysDept;
import com.aioveu.system.model.form.DeptForm;
import com.aioveu.system.model.vo.DeptVO;
import org.mapstruct.Mapper;

/**
 * @Description: TODO 部门对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 16:56
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface DeptConverter {

    DeptForm entity2Form(SysDept entity);
    DeptVO entity2Vo(SysDept entity);

    SysDept form2Entity(DeptForm deptForm);

}