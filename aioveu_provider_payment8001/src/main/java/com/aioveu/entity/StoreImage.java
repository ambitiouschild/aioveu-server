package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_store_image")
@Data
public class StoreImage extends IdEntity {

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    private Integer width;

    private Integer height;

    @NotEmpty(message = "地址不能为空")
    private String url;

    private Integer imageType;

    private Integer priority;

}
