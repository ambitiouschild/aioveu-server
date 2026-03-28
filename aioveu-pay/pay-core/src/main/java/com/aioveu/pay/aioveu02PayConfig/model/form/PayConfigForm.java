package com.aioveu.pay.aioveu02PayConfig.model.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

/**
 * @ClassName: PayConfigForm
 * @Description TODO 支付配置主表表单对象
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:03
 * @Version 1.0
 **/
@Getter
@Setter
@Schema(description = "支付配置主表表单对象")
public class PayConfigForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "主键ID")
    private Long id;

    @Schema(description = "租户ID")
    @NotNull(message = "租户ID不能为空")
    private Long tenantId;

    @Schema(description = "配置编码（唯一标识）")
    @NotBlank(message = "配置编码（唯一标识）不能为空")
    @Size(max=100, message="配置编码（唯一标识）长度不能超过100个字符")
    private String configCode;

    @Schema(description = "配置名称")
    @Size(max=100, message="配置名称长度不能超过100个字符")
    private String configName;

    @Schema(description = "支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付")
    @NotBlank(message = "支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付不能为空")
    @Size(max=20, message="支付平台类型: WECHAT-微信支付, ALIPAY-支付宝, DUMMY-模拟支付长度不能超过20个字符")
    private String platformType;

    @Schema(description = "支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付")
    @NotBlank(message = "支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付不能为空")
    @Size(max=20, message="支付类型: APP-APP支付, JSAPI-公众号支付, NATIVE-扫码支付, MWEB-H5支付, MINIPROGRAM-小程序支付, FACE-刷脸支付长度不能超过20个字符")
    private String payType;

    @Schema(description = "是否启用: 0-禁用, 1-启用")
    @NotNull(message = "是否启用: 0-禁用, 1-启用不能为空")
    private Integer enabled;

    @Schema(description = "是否默认配置: 0-否, 1-是")
    @NotNull(message = "是否默认配置: 0-否, 1-是不能为空")
    private Integer isDefault;

    @Schema(description = "排序")
    @NotNull(message = "排序不能为空")
    private Integer sort;

    @Schema(description = "备注")
    @Size(max=500, message="备注长度不能超过500个字符")
    private String remark;

    @Schema(description = "创建人ID")
    private Long createBy;

    @Schema(description = "更新人ID")
    private Long updateBy;
}
