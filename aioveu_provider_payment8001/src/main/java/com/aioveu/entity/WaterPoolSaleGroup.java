package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description 公海销售组
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_water_pool_sale_group")
@Data
public class WaterPoolSaleGroup extends IdNameEntity{

    private Long companyId;

    private Long storeId;

}
