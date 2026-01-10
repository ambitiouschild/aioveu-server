package com.aioveu.oms.aioveu07UndoLog.converter;

import com.aioveu.oms.aioveu07UndoLog.model.entity.UndoLog;
import com.aioveu.oms.aioveu07UndoLog.model.form.UndoLogForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: UndoLogConverter
 * @Description TODO  AT transaction mode undo table对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:40
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface UndoLogConverter {

    UndoLogForm toForm(UndoLog entity);

    UndoLog toEntity(UndoLogForm formData);
}
