package com.aioveu.pay.aioveu05PayConfigAlipay.mapper;

import com.aioveu.pay.aioveu05PayConfigAlipay.model.entity.PayConfigAlipay;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.query.PayConfigAlipayQuery;
import com.aioveu.pay.aioveu05PayConfigAlipay.model.vo.PayConfigAlipayVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayConfigAlipayMapper
 * @Description TODO 支付宝支付配置Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 17:10
 * @Version 1.0
 **/
@Mapper
public interface PayConfigAlipayMapper extends BaseMapper<PayConfigAlipay> {


    /**
     * 获取支付宝支付配置分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayConfigAlipayVo>} 支付宝支付配置分页列表
     */
    Page<PayConfigAlipayVo> getPayConfigAlipayPage(Page<PayConfigAlipayVo> page, PayConfigAlipayQuery queryParams);

}
