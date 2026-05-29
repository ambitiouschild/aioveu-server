package com.aioveu.pay.aioveu13PayCallbackRecord.mapper;


import com.aioveu.pay.aioveu13PayCallbackRecord.model.entity.PayCallbackRecord;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.query.PayCallbackRecordQuery;
import com.aioveu.pay.aioveu13PayCallbackRecord.model.vo.PayCallbackRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @ClassName: PayCallbackRecordMapper
 * @Description TODO 支付回调记录Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/29 18:10
 * @Version 1.0
 **/
@Mapper
public interface PayCallbackRecordMapper extends BaseMapper<PayCallbackRecord> {

    /**
     * 获取支付回调记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayCallbackRecordVo>} 支付回调记录分页列表
     */
    Page<PayCallbackRecordVo> getPayCallbackRecordPage(Page<PayCallbackRecordVo> page, PayCallbackRecordQuery queryParams);


    Long countByTransactionId(@Param("transactionId") String transactionId);

}
