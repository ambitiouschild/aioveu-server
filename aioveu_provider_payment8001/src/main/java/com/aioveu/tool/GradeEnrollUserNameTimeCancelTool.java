package com.aioveu.tool;

import lombok.Data;

/**
 * @description 班级取消约课参数
 * @author: 雒世松
 * @date: 2025/02/06 10:42
 */
@Data
public class GradeEnrollUserNameTimeCancelTool {

    /**
     * 开始时间
     */
    private String startDayTime;

    /**
     * 结束时间
     */
    private String endDayTime;

    /**
     * 课程名称
     */
    private String gradeName;

}
