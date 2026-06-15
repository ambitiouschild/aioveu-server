package com.aioveu.common.enums.oms;

import com.aioveu.common.base.IBaseEnum;
import com.baomidou.mybatisplus.annotation.IEnum;
import lombok.Getter;

/**
 * @Description: TODO 订单导出任务状态枚举
 * @Author: 雒世松
 * @Date: 2026/6/12 17:38
 * @param
 * @return:
 **/


public enum OrdeExportTaskStatusEnum implements IBaseEnum<String>, IEnum<String> {

    PENDING("PENDING", "待执行"),
    RUNNING("RUNNING", "执行中"),
    SUCCESS("SUCCESS", "成功"),
    FAILED("FAILED", "失败"),
    ;

    OrdeExportTaskStatusEnum(String value, String label) {
        this.value = value;
        this.label = label;
    }


    @Getter
    private String value;

    @Getter
    private String label;
}
