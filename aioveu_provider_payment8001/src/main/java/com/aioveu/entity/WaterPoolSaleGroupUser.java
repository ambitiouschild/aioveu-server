package com.aioveu.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@TableName("sport_water_pool_sale_group_user")
@Data
public class WaterPoolSaleGroupUser extends IdEntity{

    @NotNull(message = "saleGroupId不能为空")
    private Long saleGroupId;

    @NotEmpty(message = "userId不能为空")
    private String userId;

    private Integer status;

}
