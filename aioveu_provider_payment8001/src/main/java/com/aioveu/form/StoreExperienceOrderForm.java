package com.aioveu.form;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/1/13 0013 0:03
 */
@Data
public class StoreExperienceOrderForm {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date start;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date end;

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    private Integer page;

    private Integer size;

}
