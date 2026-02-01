package com.aioveu.refund.aioveu04RefundOperationLog.converter;

import com.aioveu.refund.aioveu04RefundOperationLog.model.entity.RefundOperationLog;
import com.aioveu.refund.aioveu04RefundOperationLog.model.form.RefundOperationLogForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RefundOperationLogConverter
 * @Description TODO  退款操作记录（用于审计）对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:20
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RefundOperationLogConverter {

    RefundOperationLogForm toForm(RefundOperationLog entity);

    RefundOperationLog toEntity(RefundOperationLogForm formData);
}
