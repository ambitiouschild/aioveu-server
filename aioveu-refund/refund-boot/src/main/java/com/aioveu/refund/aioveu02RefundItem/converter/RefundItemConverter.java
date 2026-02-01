package com.aioveu.refund.aioveu02RefundItem.converter;

import com.aioveu.refund.aioveu02RefundItem.model.entity.RefundItem;
import com.aioveu.refund.aioveu02RefundItem.model.form.RefundItemForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RefundItemConverter
 * @Description TODO  退款商品明细对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 17:02
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RefundItemConverter {


    RefundItemForm toForm(RefundItem entity);

    RefundItem toEntity(RefundItemForm formData);
}
