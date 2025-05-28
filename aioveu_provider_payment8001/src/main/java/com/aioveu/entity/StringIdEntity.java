package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@Data
public abstract class StringIdEntity extends BaseEntity{

    @TableId(type = IdType.ASSIGN_UUID)
    private String id;
}
