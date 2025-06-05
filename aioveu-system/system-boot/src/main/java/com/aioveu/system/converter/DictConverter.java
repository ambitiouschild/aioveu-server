package com.aioveu.system.converter;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.system.model.entity.SysDict;
import com.aioveu.system.model.form.DictForm;
import com.aioveu.system.model.vo.DictPageVO;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

/**
 * @Description: TODO 字典数据项对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 17:12
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface DictConverter {

    Page<DictPageVO> entity2Page(Page<SysDict> page);

    DictForm entity2Form(SysDict entity);

    @InheritInverseConfiguration(name="entity2Form")
    SysDict form2Entity(DictForm entity);
}
