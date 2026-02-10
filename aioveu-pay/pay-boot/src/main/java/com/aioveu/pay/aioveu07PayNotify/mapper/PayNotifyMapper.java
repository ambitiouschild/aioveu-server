package com.aioveu.pay.aioveu07PayNotify.mapper;

import com.aioveu.pay.aioveu07PayNotify.model.entity.PayNotify;
import com.aioveu.pay.aioveu07PayNotify.model.query.PayNotifyQuery;
import com.aioveu.pay.aioveu07PayNotify.model.vo.PayNotifyVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayNotifyMapper
 * @Description TODO 支付通知Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/10 15:58
 * @Version 1.0
 **/
@Mapper
public interface PayNotifyMapper extends BaseMapper<PayNotify> {

    /**
     * 获取支付通知分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayNotifyVO>} 支付通知分页列表
     */
    Page<PayNotifyVO> getPayNotifyPage(Page<PayNotifyVO> page, PayNotifyQuery queryParams);

}
