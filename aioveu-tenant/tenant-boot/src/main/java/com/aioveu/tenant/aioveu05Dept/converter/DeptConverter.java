package com.aioveu.tenant.aioveu05Dept.converter;

import com.aioveu.tenant.aioveu05Dept.model.entity.Dept;
import com.aioveu.tenant.aioveu05Dept.model.form.DeptForm;
import com.aioveu.tenant.aioveu05Dept.model.vo.DeptVO;
import org.mapstruct.Mapper;

/**
 * @ClassName: DeptConverter
 * @Description TODO 部门对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:09
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface DeptConverter {

    DeptForm toForm(Dept entity);

    DeptVO toVo(Dept entity);

    Dept toEntity(DeptForm deptForm);
}
