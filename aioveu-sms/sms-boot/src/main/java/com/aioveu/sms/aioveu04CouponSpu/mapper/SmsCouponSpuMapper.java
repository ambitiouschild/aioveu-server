package com.aioveu.sms.aioveu04CouponSpu.mapper;

import com.aioveu.sms.aioveu04CouponSpu.model.query.SmsCouponSpuQuery;
import com.aioveu.sms.aioveu04CouponSpu.model.vo.SmsCouponSpuVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.sms.aioveu04CouponSpu.model.entity.SmsCouponSpu;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 优惠券适用的具体商品Mapper接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:46
 * @param
 * @return:
 **/

@Mapper
public interface SmsCouponSpuMapper extends BaseMapper<SmsCouponSpu> {

    /**
     * 获取优惠券适用的具体商品分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<SmsCouponSpuVO>} 优惠券适用的具体商品分页列表
     */
    Page<SmsCouponSpuVO> getSmsCouponSpuPage(Page<SmsCouponSpuVO> page, SmsCouponSpuQuery queryParams);

}




