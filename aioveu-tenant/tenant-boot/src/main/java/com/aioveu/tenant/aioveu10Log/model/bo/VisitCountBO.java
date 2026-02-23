package com.aioveu.tenant.aioveu10Log.model.bo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @ClassName: VisitCountBO
 * @Description TODO 特定日期访问统计
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 20:11
 * @Version 1.0
 **/
@Data
@Schema(description = "特定日期访问统计")
public class VisitCountBO {

    /**
     * 日期 yyyy-MM-dd
     */
    private String date;

    /**
     * 访问次数
     */
    private Integer count;
}
