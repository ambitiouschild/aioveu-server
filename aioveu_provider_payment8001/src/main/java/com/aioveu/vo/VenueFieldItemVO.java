package com.aioveu.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/24 19:29
 */
@Data
public class VenueFieldItemVO {

    private Long id;

    private String startTime;

    private String endTime;

    private Integer status;

    private Long fieldPlanLockId;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private String remark;

    private String lockRemark;

    private String username;

    private String phone;

    private String gradeIds;

    private List<GradeDetailVO> gradeDetailVOList;

}
