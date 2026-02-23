package com.aioveu.tenant.aioveu06Dict.converter;

import com.aioveu.tenant.aioveu06Dict.model.entity.Dict;
import com.aioveu.tenant.aioveu06Dict.model.form.DictForm;
import com.aioveu.tenant.aioveu06Dict.model.vo.DictPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

/**
 * @ClassName: DictConverter
 * @Description TODO 字典 对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:11
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface DictConverter {

    Page<DictPageVO> toPageVo(Page<Dict> page);

    DictForm toForm(Dict entity);

    Dict toEntity(DictForm entity);
}
