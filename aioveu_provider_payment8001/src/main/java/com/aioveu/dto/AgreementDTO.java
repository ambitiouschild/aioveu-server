package com.aioveu.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/12/3 13:16
 */
@Data
public class AgreementDTO {

    private String fullName;

    private String email;

    private String weChatId;

    private String idCard;

    private Long companyId;

    private String companyName;

    private String companyAddress;

    private String tel;

    private Date startTime;

    private Date endTime;

    private List<Map<String, Object>> list;

}
