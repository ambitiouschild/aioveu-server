package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @description 公司店铺用户表
 * @author: 雒世松
 * @date: 2025/2/3 0003 14:37
 */
@Data
@TableName("sport_company_store_user")
public class CompanyStoreUser extends IdEntity {

    @NotBlank(message = "userId can not be null!")
    private String userId;

    @NotNull(message = "companyId can not be null!")
    private Long companyId;

    @NotNull(message = "storeId can not be null!")
    private Long storeId;

}
