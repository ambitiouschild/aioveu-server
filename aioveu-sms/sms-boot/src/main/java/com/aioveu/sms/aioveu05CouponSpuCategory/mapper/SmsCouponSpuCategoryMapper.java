package com.aioveu.sms.aioveu05CouponSpuCategory.mapper;

import com.aioveu.sms.aioveu05CouponSpuCategory.model.query.SmsCouponSpuCategoryQuery;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.vo.SmsCouponSpuCategoryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.sms.aioveu05CouponSpuCategory.model.entity.SmsCouponSpuCategory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 优惠券商品分类关联持久层  优惠券适用的具体分类Mapper接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:46
 * @param
 * @return:
 **/

@Mapper
public interface SmsCouponSpuCategoryMapper extends BaseMapper<SmsCouponSpuCategory> {

    /**
     * 获取优惠券适用的具体分类分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<SmsCouponSpuCategoryVO>} 优惠券适用的具体分类分页列表
     */
    Page<SmsCouponSpuCategoryVO> getSmsCouponSpuCategoryPage(Page<SmsCouponSpuCategoryVO> page, SmsCouponSpuCategoryQuery queryParams);

}




