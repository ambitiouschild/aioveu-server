package com.aioveu.common.enums;

import com.aioveu.common.base.IBaseEnum;
import lombok.Getter;

/**
 * @ClassName: DataScopeEnum
 * @Description TODO  数据权限枚举
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/14 15:06
 * @Version 1.0
 **/
@Getter
public enum DataScopeEnum implements IBaseEnum<Integer> {

    /**
     * value 越小，数据权限范围越大
     */
    ALL(1, "所有数据"),
    DEPT_AND_SUB(2, "部门及子部门数据"),
    DEPT(3, "本部门数据"),
    SELF(4, "本人数据");

    private final Integer value;

    private final String label;

    DataScopeEnum(Integer value, String label) {
        this.value = value;
        this.label = label;
    }
}
