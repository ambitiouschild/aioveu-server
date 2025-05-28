package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_report_form")
@Data
public class ReportForm extends IdEntity {

    private Long companyId;

    private Long storeId;

    @NotEmpty(message = "消息不能为空")
    private String content;

    @NotNull(message = "数量不能为空")
    private Integer count;

    @NotNull(message = "金额不能为空")
    private BigDecimal amount;

    private String createUserId;

    private String createUsername;

    // status 1 未读 2 已读

}
