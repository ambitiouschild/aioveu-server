package com.aioveu.sms.aioveu01Advert.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Description: TODO 广告分页列表查询对象
 * @Author: 雒世松
 * @Date: 2025/6/5 18:47
 * @param
 * @return:
 **/

@Schema(description = "广告分页查询对象")
@Data
public class SmsAdvertQuery extends BasePageQuery {

    @Schema(description="关键字")
    private String keywords;

    @Schema(description = "广告标题")
    private String title;
    @Schema(description = "状态(1:开启；0:关闭)")
    private Integer status;

}
