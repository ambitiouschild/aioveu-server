package com.aioveu.pay.aioveu01.converter;
import com.aioveu.pay.model.aioveuPayAdapter.AliPayQueryResult;
import com.aioveu.pay.model.aioveuPayAdapter.MockPayQueryResult;
import com.aioveu.pay.model.aioveuPayAdapter.PaymentStatusDTO;
import com.aioveu.pay.model.aioveuPayAdapter.WechatPayQueryResult;
import org.mapstruct.Mapper;

/**
 * @ClassName: MqCompensationTaskConverter
 * @Description TODO MQ补偿任务对象转换器
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:53
 * @Version 1.0
 **/
@Mapper(componentModel = "spring")
public interface PayQueryResultConverter {

    PaymentStatusDTO wxResultToDTO(WechatPayQueryResult wxResult);

    PaymentStatusDTO aliResultToDTO(AliPayQueryResult aliResult);

    PaymentStatusDTO mockResultToDTO(MockPayQueryResult mockResult);
}
