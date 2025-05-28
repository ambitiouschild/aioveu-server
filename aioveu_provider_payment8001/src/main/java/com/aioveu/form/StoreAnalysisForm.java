package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/13 0013 0:03
 */
@Data
public class StoreAnalysisForm {

    @NotEmpty(message = "开始时间")
    private String start;

    @NotEmpty(message = "结束时间")
    private String end;

    private Long storeId;

    private String coachUserId;

    private List<String> orderIdList;

    private boolean multiUser;

}
