package com.aioveu.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author xlfan10
 * @description
 * @date 2025/1/22 17:06
 */
@Data
public class UserCouponTotalVO {

    private Long companyId;

    private Long storeId;

    private String userId;

    private String name;

    private String phone;

    private String username;

    private String lastClassTime;

    private List<UserCouponNumberVO> couponList;

    /**
     * 课券数
     */
    private Integer couponNum;

    /**
     * 教练名称，多个教练名称以逗号隔开
     */
    private String coachName;

    /**
     * 我的池海池
     * 对应分配的教练
     */
    private String seaPoolCoachName;

    /**
     * 销售客户池
     * 对应分配的销售
     */
    private String seaPoolSaleName;

}
