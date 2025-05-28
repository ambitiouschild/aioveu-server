package com.aioveu.vo;

import com.aioveu.enums.PeriodType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceRule {
    private Long id;
    /**
     * 0 自由时间
     * 1 每星期
     * 2 节假日
     * 3 调休日
     */
    private Integer type;
    private Integer weekFrom;
    private Integer weekTo;
    private String dateFrom;
    private String dateTo;
    private String timeFrom;
    private String timeTo;
    private String expiryDate;
    /**
     * 散客价
     */
    private BigDecimal price;
    /**
     * vip价格
     */
    private BigDecimal vipPrice;
    /**
     * 教练价格
     */
    private BigDecimal coachPrice;
    private String remark;
    private List<Long> fieldIdList;
    private List<Long> venueIdList;
}
