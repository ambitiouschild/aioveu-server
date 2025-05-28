package com.aioveu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Time;
import java.util.Date;
import java.util.List;

@Data
public class FieldPlanVO {

    private Long id;

    private List<Long> ids;

    private Long fieldId;

    private String fieldName;

    private Long venueId;

    private String venueName;

    private String bookingUser;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date fieldDay;

    private Time startTime;

    private Time endTime;

    private BigDecimal price;

    private BigDecimal vipPrice;

    private Integer status;

    private String remark;

    private String lockRemark;

    /**
     * 订场数据同步状态，1成功，0失败
     */
    private Integer syncStatus;

    /**
     * 锁场平台
     * 平台编码对应sport_category分类表code值
     * {@link com.aioveu.enums.FieldPlanLockChannels}
     */
    private String lockChannel;

}
