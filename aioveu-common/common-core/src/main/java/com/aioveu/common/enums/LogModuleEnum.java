package com.aioveu.common.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

/**
  *@ClassName: LogModuleEnum
  *@Description TODO  日志模块枚举
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/12 21:56
  *@Version 1.0
  **/

@Schema(enumAsRef = true)
@Getter
public enum LogModuleEnum {

    EXCEPTION("异常"),
    LOGIN("登录"),
    USER("用户"),
    DEPT("部门"),
    ROLE("角色"),
    MENU("菜单"),
    DICT("字典"),
    SETTING("系统配置"),
    OTHER("其他"),

    AUTH("认证服务"),

    GATEWAY("网关服务"),

    OMS("订单服务"),
    PMS("商品服务"),
    SMS("营销服务"),
    UMS("会员服务"),

    PAY("支付服务"),
    REFUND("退款服务");





    @JsonValue
    private final String moduleName;

    LogModuleEnum(String moduleName) {
        this.moduleName = moduleName;
    }
}
