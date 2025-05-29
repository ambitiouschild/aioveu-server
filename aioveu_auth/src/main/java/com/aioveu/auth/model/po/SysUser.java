package com.aioveu.auth.model.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @description 用户实体类
 * @author: 雒世松
 * @date: 2020/2/3 0003 14:37
 */
@Data
@TableName("sport_user")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SysUser {

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;

    private String username;

    private String name;

    private Integer gender;

    private String head;

    private String password;

    private String phone;

    private String mail;

    private Integer status;

    private Date createDate;

    private Date updateDate;

    private Date accountExpiredTime;

    private Date credentialsExpired;
}
