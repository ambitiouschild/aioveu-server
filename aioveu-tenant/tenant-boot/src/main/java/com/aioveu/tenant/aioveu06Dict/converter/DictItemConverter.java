package com.aioveu.tenant.aioveu06Dict.converter;

import com.aioveu.common.model.Option;
import com.aioveu.tenant.aioveu06Dict.model.entity.DictItem;
import com.aioveu.tenant.aioveu06Dict.model.form.DictItemForm;
import com.aioveu.tenant.aioveu06Dict.model.vo.DictPageVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @ClassName: DictItemConverter
 * @Description TODO 字典项对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:12
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface DictItemConverter {

    Page<DictPageVO> toPageVo(Page<DictItem> page);

    DictItemForm toForm(DictItem entity);

    DictItem toEntity(DictItemForm formFata);

    Option<Long> toOption(DictItem dictItem);
    List<Option<Long>> toOption(List<DictItem> dictData);
}
