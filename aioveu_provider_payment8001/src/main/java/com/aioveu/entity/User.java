package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.aioveu.vo.user.UserVo;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @description 用户实体类
 * @author: 雒世松
 * @date: 2025/2/3 0003 14:37
 */
@TableName("sport_user")
@Data
public class User {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String name;

    @NotBlank(message = "username can not be null!")
    private String username;

    private String password;

    /** 创建时间 */
    private Date createDate;

    /** 更新时间 */
    private Date updateDate;

    private String permissions;

    private Integer status;

    private Date accountExpiredTime;

    private Date credentialsExpired;

    private String head;

    private String city;

    private String country;

    private String province;

    /**
     * 自我介绍
     */
    private String introduction;

    /**
     * 0 未知 1 男性 2 女性
     */
    private Integer gender;

    private String phone;

    private String mail;

    /**
     * 创建者id
     */
    private String creatorId;

    /**
     * 积分
     */
    private Integer integral;

    /**
     * 余额 当前用户账户的实时余额
     */
    private BigDecimal balance;

    /**
     * 累计收入
     */
    private BigDecimal totalIncome;

    /**
     * 累计余额
     */
    private BigDecimal totalBalance;

    /**
     * 推荐码
     */
    private String recommendCode;

    public UserVo buildInfo() {
        UserVo info = new UserVo();
        BeanUtils.copyProperties(this, info);
        if (info.getGender() == null) {
            info.setGender(0);
        }
        return info;
    }
}
