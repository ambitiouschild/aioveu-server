package com.aioveu.system.converter;

import com.aioveu.system.model.entity.SysDictType;
import com.aioveu.system.model.form.DictTypeForm;
import com.aioveu.system.model.vo.DictTypePageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @Description: TODO 字典类型对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 17:12
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface DictTypeConverter {

    Page<DictTypePageVO> entity2Page(Page<SysDictType> page);

    DictTypeForm entity2Form(SysDictType entity);

    SysDictType form2Entity(DictTypeForm entity);
}
