package com.aioveu.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@Data
public abstract class BaseEntity implements Serializable {

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date createDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    private Date updateDate;

    /**
     * 0 删除 1 正常
     */
    private Integer status;
}
