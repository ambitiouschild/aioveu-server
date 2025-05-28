package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/03/12 11:41
 */
@TableName("sport_operate_log")
@Data
public class OperateLog extends IdNameEntity {

    private String userId;

    private String username;

    private String roleCode;

    private String detail;

    private Long companyId;

    private Long storeId;

    private String productId;

    private String categoryCode;

    /**
     * 操作的相关参数
     */
    private String params;

    /**
     * 操作类型 增 0 删 1 改 2 查 3
     */
    private Integer operateType;

    private Date operateTime;

}
