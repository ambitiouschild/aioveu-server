package com.aioveu.ums.aioveu01Member.model.query;

import com.aioveu.common.base.BasePageQuery;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @ClassName: UmsMemberQuery
 * @Description TODO  会员分页查询对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/12 14:43
 * @Version 1.0
 **/

@Schema(description ="会员查询对象")
@Getter
@Setter
public class UmsMemberQuery extends BasePageQuery {

    @Schema(description = "昵称")
    private String nickName;
    @Schema(description = "手机号")
    private String mobile;
    @Schema(description = "性别(0=未知,1=男,2=女)")
    private Integer gender;
    @Schema(description = "生日")
    private List<String> birthday;
    @Schema(description = "状态(0=禁用,1=正常)")
    private Integer status;
    @Schema(description = "微信OpenID")
    private String openid;
    @Schema(description = "国家")
    private String country;
    @Schema(description = "省份")
    private String province;
    @Schema(description = "城市")
    private String city;
    @Schema(description = "语言")
    private String language;
}
