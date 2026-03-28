package com.aioveu.pay.aioveu04PayConfigDummy.mapper;

import com.aioveu.pay.aioveu04PayConfigDummy.model.entity.PayConfigDummy;
import com.aioveu.pay.aioveu04PayConfigDummy.model.query.PayConfigDummyQuery;
import com.aioveu.pay.aioveu04PayConfigDummy.model.vo.PayConfigDummyVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayConfigDummyMapper
 * @Description TODO 模拟支付配置Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:30
 * @Version 1.0
 **/
@Mapper
public interface PayConfigDummyMapper extends BaseMapper<PayConfigDummy> {

    /**
     * 获取模拟支付配置分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayConfigDummyVo>} 模拟支付配置分页列表
     */
    Page<PayConfigDummyVo> getPayConfigDummyPage(Page<PayConfigDummyVo> page, PayConfigDummyQuery queryParams);

}
