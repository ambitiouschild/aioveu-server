package com.aioveu.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @description 公司
 * @author: 雒世松
 * @date: Created in 2025/8/7 11:41
 */
@TableName("sport_company")
@Data
public class Company extends IdNameEntity{

    @NotEmpty(message = "地址不能为空")
    private String address;

    @NotEmpty(message = "法人不能为空")
    private String legalName;

    private String brandName;

    private String brandLogo;

    @NotNull(message = "省份不能为空")
    private Long provinceId;

    @NotNull(message = "城市不能为空")
    private Long cityId;

    @NotNull(message = "区域不能为空")
    private Long regionId;

    @NotEmpty(message = "电话不能为空")
    private String tel;

    /**
     * 公司类型 1 按量 0 包年
     */
    private Integer companyType;

    private String businessLicense;

    /**
     * 公司对应的小程序支付AppId
     */
    private String miniAppPayId;

    /**
     * 公司对应的小程序AppId
     */
    private String miniAppId;

    /**
     * 公司对应的服务号AppId
     */
    private String mpAppId;

    private Integer beforeBookingMinutes;

    private String vipAgreementTemplate;

    private String gradeAgreementTemplate;

    private String invoiceContents;

    private Integer cancelGradeMinutes;

    /**
     * 单次订场数
     */
    private Integer fieldBookNums;

    /**
     * 余额 当前公司账户的实时余额
     */
    private BigDecimal balance;

    /**
     * 汇付商户ID
     */
    private String huiFuId;

    /**
     * 汇付订场商户ID
     */
    private String huiFuFieldId;

    /**
     * 支付方式 默认汇付 hf wx
     */
    private String payType;

}
