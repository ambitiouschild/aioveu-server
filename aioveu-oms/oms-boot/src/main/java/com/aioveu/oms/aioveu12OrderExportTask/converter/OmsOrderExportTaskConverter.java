package com.aioveu.oms.aioveu12OrderExportTask.converter;


import com.aioveu.oms.aioveu12OrderExportTask.model.entity.OmsOrderExportTask;
import com.aioveu.oms.aioveu12OrderExportTask.model.form.OmsOrderExportTaskForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: OmsOrderExportTaskConverter
 * @Description TODO 订单导出任务对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/6/12 18:24
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface OmsOrderExportTaskConverter {

    OmsOrderExportTaskForm toForm(OmsOrderExportTask entity);

    OmsOrderExportTask toEntity(OmsOrderExportTaskForm formData);
}
