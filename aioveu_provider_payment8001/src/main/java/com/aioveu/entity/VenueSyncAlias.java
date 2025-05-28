package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @description 场馆同步别名
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_venue_sync_alias")
@Data
public class VenueSyncAlias extends IdEntity{

    @NotNull(message = "公司id不能为空")
    private Long companyId;

    @NotNull(message = "店铺id不能为空")
    private Long storeId;

    private String venueName;

    private Long venueId;

    private String aliasName;

    private String accountConfigId;

    private String platformCode;


}
