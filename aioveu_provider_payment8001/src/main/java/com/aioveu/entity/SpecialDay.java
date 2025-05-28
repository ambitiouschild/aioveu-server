package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/2 0002 12:37
 */
@TableName("sport_special_day")
@Data
public class SpecialDay extends IdNameEntity {

    private Date detailDay;

    /**
     * 特殊类型 默认1 节假日 0调休工作日
     */
    private Integer specialType;

}
