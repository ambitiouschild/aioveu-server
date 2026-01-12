package com.aioveu.sms.aioveu02Coupon.converter;


import com.aioveu.sms.aioveu02Coupon.model.entity.SmsCoupon;
import com.aioveu.sms.aioveu02Coupon.model.form.CouponForm;
import com.aioveu.sms.aioveu02Coupon.model.vo.CouponPageVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

/**
 * @Description: TODO 优惠券对象转换器
 * @Author: 雒世松
 * @Date: 2025/6/5 18:45
 * @param
 * @return:
 **/

@Mapper(componentModel = "spring")
public interface CouponConverter {

    @Mappings({
            @Mapping(target = "platformLabel", expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(entity.getPlatform(), com.aioveu.sms.aioveu02Coupon.enums.PlatformEnum.class))"),
            @Mapping(target = "typeLabel", expression = "java(com.aioveu.common.base.IBaseEnum.getLabelByValue(entity.getType(), com.aioveu.sms.aioveu02Coupon.enums.CouponTypeEnum.class))"),
            @Mapping(target = "faceValueLabel", expression = "java(com.aioveu.sms.aioveu02Coupon.util.CouponUtils.getFaceValue(entity.getType(),entity.getFaceValue(),entity.getDiscount()))"),
            @Mapping(
                    target = "validityPeriodLabel",
                    expression = "java(com.aioveu.sms.aioveu02Coupon.util.CouponUtils.getValidityPeriod(entity.getValidityPeriodType(),entity.getValidityDays(),entity.getValidityBeginTime(),entity.getValidityBeginTime()))"
            ),
            @Mapping(target = "minPointLabel", expression = "java(cn.hutool.core.util.NumberUtil.toStr(cn.hutool.core.util.NumberUtil.div(entity.getMinPoint(),new java.math.BigDecimal(100)).setScale(2)))"),
    })
    CouponPageVO entity2PageVO(SmsCoupon entity);


    List<CouponPageVO> entity2PageVO(List<SmsCoupon> entities);


    @Mappings({
            @Mapping(target = "discount",expression = "java(cn.hutool.core.util.NumberUtil.div(form.getDiscount(),10L))"),
    })
    SmsCoupon form2Entity(CouponForm form);


    @Mappings({
            @Mapping(target = "discount",expression = "java(cn.hutool.core.util.NumberUtil.mul(entity.getDiscount(),10L))"),
    })
    CouponForm entity2Form(SmsCoupon entity);
}