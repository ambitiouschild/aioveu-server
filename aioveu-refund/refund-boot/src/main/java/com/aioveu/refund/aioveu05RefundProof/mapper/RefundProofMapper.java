package com.aioveu.refund.aioveu05RefundProof.mapper;

import com.aioveu.refund.aioveu05RefundProof.model.entity.RefundProof;
import com.aioveu.refund.aioveu05RefundProof.model.query.RefundProofQuery;
import com.aioveu.refund.aioveu05RefundProof.model.vo.RefundProofVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: RefundProofMapper
 * @Description TODO  退款凭证图片Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/1 13:04
 * @Version 1.0
 **/

@Mapper
public interface RefundProofMapper extends BaseMapper<RefundProof> {

    /**
     * 获取退款凭证图片分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<RefundProofVO>} 退款凭证图片分页列表
     */
    Page<RefundProofVO> getRefundProofPage(Page<RefundProofVO> page, RefundProofQuery queryParams);
}
