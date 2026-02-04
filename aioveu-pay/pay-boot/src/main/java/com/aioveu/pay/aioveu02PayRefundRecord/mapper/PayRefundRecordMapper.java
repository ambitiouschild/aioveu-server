package com.aioveu.pay.aioveu02PayRefundRecord.mapper;

import com.aioveu.pay.aioveu02PayRefundRecord.model.entity.PayRefundRecord;
import com.aioveu.pay.aioveu02PayRefundRecord.model.query.PayRefundRecordQuery;
import com.aioveu.pay.aioveu02PayRefundRecord.model.vo.PayRefundRecordVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayRefundRecordMapper
 * @Description TODO 退款记录Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 18:50
 * @Version 1.0
 **/

@Mapper
public interface PayRefundRecordMapper extends BaseMapper<PayRefundRecord> {

    /**
     * 获取退款记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayRefundRecordVO>} 退款记录分页列表
     */
    Page<PayRefundRecordVO> getPayRefundRecordPage(Page<PayRefundRecordVO> page, PayRefundRecordQuery queryParams);
}
