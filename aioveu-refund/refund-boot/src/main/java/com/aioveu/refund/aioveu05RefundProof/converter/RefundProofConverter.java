package com.aioveu.refund.aioveu05RefundProof.converter;

import com.aioveu.refund.aioveu05RefundProof.model.entity.RefundProof;
import com.aioveu.refund.aioveu05RefundProof.model.form.RefundProofForm;
import org.mapstruct.Mapper;

/**
 * @ClassName: RefundProofConverter
 * @Description TODO 退款凭证图片对象转换器
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:06
 * @Version 1.0
 **/

@Mapper(componentModel = "spring")
public interface RefundProofConverter {

    RefundProofForm toForm(RefundProof entity);

    RefundProof toEntity(RefundProofForm formData);
}
