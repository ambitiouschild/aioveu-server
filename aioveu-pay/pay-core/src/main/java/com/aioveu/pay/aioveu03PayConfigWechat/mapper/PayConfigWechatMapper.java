package com.aioveu.pay.aioveu03PayConfigWechat.mapper;

import com.aioveu.pay.aioveu03PayConfigWechat.model.entity.PayConfigWechat;
import com.aioveu.pay.aioveu03PayConfigWechat.model.query.PayConfigWechatQuery;
import com.aioveu.pay.aioveu03PayConfigWechat.model.vo.PayConfigWechatVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: PayConfigWechatMapper
 * @Description TODO 微信支付配置Mapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/3/28 16:15
 * @Version 1.0
 **/
@Mapper
public interface PayConfigWechatMapper extends BaseMapper<PayConfigWechat> {

    /**
     * 获取微信支付配置分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<PayConfigWechatVo>} 微信支付配置分页列表
     */
    Page<PayConfigWechatVo> getPayConfigWechatPage(Page<PayConfigWechatVo> page, PayConfigWechatQuery queryParams);

}
