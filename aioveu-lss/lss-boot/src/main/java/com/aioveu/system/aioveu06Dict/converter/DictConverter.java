package com.aioveu.system.aioveu06Dict.converter;

import com.aioveu.system.aioveu06Dict.model.entity.Dict;
import com.aioveu.system.aioveu06Dict.model.form.DictForm;
import com.aioveu.system.aioveu06Dict.model.vo.DictPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @ClassName: DictConverter
 * @Description TODO  字典 对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 12:26
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface DictConverter {

    Page<DictPageVO> toPageVo(Page<Dict> page);

    DictForm toForm(Dict entity);

    Dict toEntity(DictForm entity);
}
