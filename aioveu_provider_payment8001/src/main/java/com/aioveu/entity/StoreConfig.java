package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @description 店铺配置
 * @author: 雒世松
 * @date: Created in 2025/02/19 11:41
 */
@Data
@TableName("sport_store_config")
public class StoreConfig extends IdNameEntity {

    @NotEmpty(message = "配置编号不能为空")
    private String code;

    @NotEmpty(message = "配置值不能为空")
    private String value;

    private String categoryCode;

    private String defaultValue;

    private String fieldType;

    private String configDesc;

    @NotNull(message = "店铺Id不能为空")
    private Long storeId;

    @NotNull(message = "公司Id不能为空")
    private Long companyId;

    private String remark;

}
