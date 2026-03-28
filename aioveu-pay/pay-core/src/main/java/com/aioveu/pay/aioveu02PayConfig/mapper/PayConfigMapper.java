package com.aioveu.pay.aioveu02PayConfig.mapper;

import com.aioveu.pay.aioveu02PayConfig.model.entity.PayConfig;
import com.aioveu.pay.aioveu02PayConfig.model.query.PayConfigQuery;
import com.aioveu.pay.aioveu02PayConfig.model.vo.PayConfigVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayConfigMapper
 * @Description TODO 支付配置主表Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:05
 * @Version 1.0
 **/
@Mapper
public interface PayConfigMapper extends BaseMapper<PayConfig> {

    /**
     * 获取支付配置主表分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayConfigVo>} 支付配置主表分页列表
     */
    Page<PayConfigVo> getPayConfigPage(Page<PayConfigVo> page, PayConfigQuery queryParams);
}
