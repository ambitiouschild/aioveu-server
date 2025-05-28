package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @description
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_mini_app_store")
@Data
public class MiniAppStore extends IdEntity {

    private Long storeId;

    private String appId;

    private Boolean defaultStore;

}
