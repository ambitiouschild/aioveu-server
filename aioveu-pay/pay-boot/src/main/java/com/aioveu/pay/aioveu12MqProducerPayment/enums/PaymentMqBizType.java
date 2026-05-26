package com.aioveu.pay.aioveu12MqProducerPayment.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @Description: TODO 消息发送状态枚举
 * @Author: 雒世松
 * @Date: 2026/5/11 20:44
 * @param
 * @return:
 **/
//方案一（✅ 最推荐）：不要实现 IBaseEnum<Integer>
//这是业务枚举，不是字典枚举
@Getter
public enum PaymentMqBizType {

    PAYMENT_SUCCESS("PAYMENT", "payment.success"),
    REFUND_SUCCESS("REFUND", "payment.refund"),
    ;

    private final String bizType;
    private final String topic;

    PaymentMqBizType(String bizType, String topic) {
        this.bizType = bizType;
        this.topic = topic;
    }



}

/*
* ✅ 赋值顺序（非常明确）
            ✅ 第 1 步：JVM 加载枚举类
            加载类
            分配静态内存
            识别枚举常量
*           ✅ 第 2 步：实例化每个枚举常量（按顺序）
* ① 创建 PAYMENT_SUCCESS new PaymentMqBizType("PAYMENT", "payment.success")
* 执行构造函数
* this.bizType = "PAYMENT";
this.topic = "payment.success";
* 到这里，PAYMENT_SUCCESS.bizType已经有值了
*
* 第 3 步：枚举实例全部创建完成
*
* 四、用一句话总结赋值顺序（请记住）
✅ 枚举常量 = 静态变量
1️JVM 加载枚举类
2️按声明顺序实例化枚举值
3️调用构造函数
4️构造函数给字段赋值
5️枚举实例创建完成
*
* */