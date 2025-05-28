package com.aioveu.form;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/13 15:35
 */
@Data
public class UserCallFollow {

    @NotEmpty(message = "userId不能为空")
    private String userId;

    @NotNull(message = "userInfoId不能为空")
    private Long userInfoId;

    private Integer callStatus;

    private Integer intention;

    private String introduce;

    private List<String> tags;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date nextContactTime;

}
