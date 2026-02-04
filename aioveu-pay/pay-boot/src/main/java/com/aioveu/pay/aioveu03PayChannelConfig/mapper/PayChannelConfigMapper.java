package com.aioveu.pay.aioveu03PayChannelConfig.mapper;

import com.aioveu.pay.aioveu03PayChannelConfig.model.entity.PayChannelConfig;
import com.aioveu.pay.aioveu03PayChannelConfig.model.query.PayChannelConfigQuery;
import com.aioveu.pay.aioveu03PayChannelConfig.model.vo.PayChannelConfigVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayChannelConfigMapper
 * @Description TODO 支付渠道配置Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/2 20:01
 * @Version 1.0
 **/

@Mapper
public interface PayChannelConfigMapper extends BaseMapper<PayChannelConfig> {

    /**
     * 获取支付渠道配置分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayChannelConfigVO>} 支付渠道配置分页列表
     */
    Page<PayChannelConfigVO> getPayChannelConfigPage(Page<PayChannelConfigVO> page, PayChannelConfigQuery queryParams);

}
