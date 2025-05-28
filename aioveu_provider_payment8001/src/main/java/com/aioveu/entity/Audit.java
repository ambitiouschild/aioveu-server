package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@TableName("sport_audit")
@Data
public class Audit extends IdEntity {

    private String auditTitle;

    private int auditType;

    private String jsonVal;

    private int auditStatus;

    private Long storeId;

    private String auditRemark;

    private String auditUserId;

    private Date auditDate;

    private String createUserId;

    @TableField(exist = false)
    private String createUserName;

    @TableField(exist = false)
    private String auditUserName;

    /**
     * 审核具体实现类
     */
    private String auditService;

    /**
     * 所属店铺用户
     */
    private String storeUsername;

}
