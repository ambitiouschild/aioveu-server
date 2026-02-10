package com.aioveu.pay.aioveu09PayAccountFlow.mapper;

import com.aioveu.pay.aioveu09PayAccountFlow.model.entity.PayAccountFlow;
import com.aioveu.pay.aioveu09PayAccountFlow.model.query.PayAccountFlowQuery;
import com.aioveu.pay.aioveu09PayAccountFlow.model.vo.PayAccountFlowVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayAccountFlowMapper
 * @Description TODO 账户流水Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 16:29
 * @Version 1.0
 **/
@Mapper
public interface PayAccountFlowMapper extends BaseMapper<PayAccountFlow> {

    /**
     * 获取账户流水分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayAccountFlowVO>} 账户流水分页列表
     */
    Page<PayAccountFlowVO> getPayAccountFlowPage(Page<PayAccountFlowVO> page, PayAccountFlowQuery queryParams);
}
