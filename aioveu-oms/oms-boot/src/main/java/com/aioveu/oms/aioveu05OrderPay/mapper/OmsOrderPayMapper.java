package com.aioveu.oms.aioveu05OrderPay.mapper;

import com.aioveu.oms.aioveu05OrderPay.model.query.OmsOrderPayQuery;
import com.aioveu.oms.aioveu05OrderPay.model.vo.OmsOrderPayVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.oms.aioveu05OrderPay.model.entity.OmsOrderPay;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Description: TODO 支付信息Mapper接口
 * @Author: 雒世松
 * @Date: 2025/6/5 18:09
 * @param
 * @return:
 **/

@Mapper
public interface OmsOrderPayMapper extends BaseMapper<OmsOrderPay> {

    /**
     * 获取支付信息分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<OmsOrderPayVO>} 支付信息分页列表
     */
    Page<OmsOrderPayVO> getOmsOrderPayPage(Page<OmsOrderPayVO> page, OmsOrderPayQuery queryParams);

}
