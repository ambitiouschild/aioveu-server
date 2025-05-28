package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description 场馆
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_store_venue")
@Data
public class StoreVenue extends IdNameEntity{

    @NotNull(message = "公司id不能为空")
    private Long companyId;

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    private String logo;

    private String tags;

    /**
     * 订场是否开放
     */
    private Boolean bookOpen;

    /**
     * 分类编号
     */
    private String categoryCode;


}
