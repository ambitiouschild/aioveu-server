package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_group_luck")
@Data
public class GroupLuck extends IdNameEntity {

    private Date groupDate;

    private String uuid;

    private String username;

    private String groupCode;

}
