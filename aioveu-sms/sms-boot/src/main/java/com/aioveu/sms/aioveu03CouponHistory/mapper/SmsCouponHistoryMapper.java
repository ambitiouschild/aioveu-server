package com.aioveu.sms.aioveu03CouponHistory.mapper;

import com.aioveu.sms.aioveu03CouponHistory.model.query.SmsCouponHistoryQuery;
import com.aioveu.sms.aioveu03CouponHistory.model.vo.SmsCouponHistoryVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.sms.aioveu03CouponHistory.model.entity.SmsCouponHistory;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;


/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO 优惠券领取/使用记录Mapper接口
 * @Date  2026/1/12 11:57
 * @Param
 * @return
 **/

@Mapper
public interface SmsCouponHistoryMapper extends BaseMapper<SmsCouponHistory> {

    /**
     * 获取优惠券领取/使用记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<SmsCouponHistoryVO>} 优惠券领取/使用记录分页列表
     */
    Page<SmsCouponHistoryVO> getSmsCouponHistoryPage(Page<SmsCouponHistoryVO> page, SmsCouponHistoryQuery queryParams);

}




