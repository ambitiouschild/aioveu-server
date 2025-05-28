package com.aioveu.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GradeCancelRecordVo {

    /**
     * 班级名称
     */
    private String className;

    /**
     * 请假原因
     */
    private String reason;

    /**
     * 取消时间
     */
    private Date cancelTime;

    /**
     * 班级时间
     */
    private Date classTime;

    /**
     * 取消人名称
     */
    private String customerName;
}
