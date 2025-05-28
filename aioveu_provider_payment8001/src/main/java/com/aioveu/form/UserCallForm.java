package com.aioveu.form;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/13 15:35
 */
@Data
public class UserCallForm {

    @NotEmpty(message = "userId不能为空")
    private String userId;

    private Integer type;

    private List<Long> userInfoList;

}
