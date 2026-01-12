package com.aioveu.sms.aioveu02Coupon.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.sms.aioveu02Coupon.model.entity.SmsCoupon;
import com.aioveu.sms.aioveu02Coupon.model.query.SmsCouponQuery;
import com.aioveu.sms.aioveu02Coupon.model.vo.SmsCouponVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券Mapper接口
 * @Date  2026/1/12 11:14
 * @Param
 * @return
 **/

@Mapper
public interface SmsCouponMapper extends BaseMapper<SmsCoupon> {

    List<SmsCoupon> getCouponPage(Page<SmsCouponVO> page, SmsCouponQuery queryParams);


    /**
     * 获取优惠券分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<SmsCouponVO>} 优惠券分页列表
     */
    Page<SmsCouponVO> getSmsCouponPage(Page<SmsCouponVO> page, SmsCouponQuery queryParams);
}




