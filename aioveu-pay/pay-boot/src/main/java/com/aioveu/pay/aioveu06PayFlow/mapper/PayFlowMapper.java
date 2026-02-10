package com.aioveu.pay.aioveu06PayFlow.mapper;

import com.aioveu.pay.aioveu06PayFlow.model.entity.PayFlow;
import com.aioveu.pay.aioveu06PayFlow.model.query.PayFlowQuery;
import com.aioveu.pay.aioveu06PayFlow.model.vo.PayFlowVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayFlowMapper
 * @Description TODO 支付流水Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/9 15:50
 * @Version 1.0
 **/
@Mapper
public interface PayFlowMapper extends BaseMapper<PayFlow> {

    /**
     * 获取支付流水分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayFlowVO>} 支付流水分页列表
     */
    Page<PayFlowVO> getPayFlowPage(Page<PayFlowVO> page, PayFlowQuery queryParams);

}
