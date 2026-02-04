package com.aioveu.pay.aioveu04PayReconciliation.mapper;

import com.aioveu.pay.aioveu04PayReconciliation.model.entity.PayReconciliation;
import com.aioveu.pay.aioveu04PayReconciliation.model.query.PayReconciliationQuery;
import com.aioveu.pay.aioveu04PayReconciliation.model.vo.PayReconciliationVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayReconciliationMapper
 * @Description TODO 支付对账Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:34
 * @Version 1.0
 **/

@Mapper
public interface PayReconciliationMapper extends BaseMapper<PayReconciliation> {

    /**
     * 获取支付对账分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayReconciliationVO>} 支付对账分页列表
     */
    Page<PayReconciliationVO> getPayReconciliationPage(Page<PayReconciliationVO> page, PayReconciliationQuery queryParams);
}
