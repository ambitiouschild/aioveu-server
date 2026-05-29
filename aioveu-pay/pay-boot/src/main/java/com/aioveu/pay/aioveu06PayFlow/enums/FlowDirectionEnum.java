package com.aioveu.pay.aioveu06PayFlow.enums;


import lombok.Getter;

/**
 * @ClassName: FlowDirectionEnum
 * @Description TODO 资金方向枚举
 *                      资金方向：IN-入金 OUT-出金
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/5/29 19:18
 * @Version 1.0
 *  FlowDirectionEnum.IN.getValue()
 **/

public enum FlowDirectionEnum {

    IN("IN", "IN-入金"),
    OUT("OUT", "OUT-出金");

    FlowDirectionEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }

    @Getter
    private String value;

    @Getter
    private String label;
}
