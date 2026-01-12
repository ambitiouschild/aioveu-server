package com.aioveu.sms.aioveu02Coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.sms.aioveu02Coupon.model.entity.SmsCoupon;
import com.aioveu.sms.aioveu02Coupon.model.query.CouponPageQuery;
import com.aioveu.sms.aioveu02Coupon.model.vo.CouponPageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {

    List<SmsCoupon> getCouponPage(Page<CouponPageVO> page, CouponPageQuery queryParams);
}




