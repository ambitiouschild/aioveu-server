package com.aioveu.pay.aioveu05PayReconciliationDetail.mapper;

import com.aioveu.pay.aioveu05PayReconciliationDetail.model.entity.PayReconciliationDetail;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.query.PayReconciliationDetailQuery;
import com.aioveu.pay.aioveu05PayReconciliationDetail.model.vo.PayReconciliationDetailVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayReconciliationDetailMapper
 * @Description TODO 对账明细Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 14:25
 * @Version 1.0
 **/
@Mapper
public interface PayReconciliationDetailMapper extends BaseMapper<PayReconciliationDetail> {

    /**
     * 获取对账明细分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayReconciliationDetailVO>} 对账明细分页列表
     */
    Page<PayReconciliationDetailVO> getPayReconciliationDetailPage(Page<PayReconciliationDetailVO> page, PayReconciliationDetailQuery queryParams);

}
