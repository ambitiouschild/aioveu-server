package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_user_call_pool")
@Data
public class UserCallPool extends IdEntity {

    private String userId;
    /**
     * 电话池大小
     */
    private Integer poolSize;

}
