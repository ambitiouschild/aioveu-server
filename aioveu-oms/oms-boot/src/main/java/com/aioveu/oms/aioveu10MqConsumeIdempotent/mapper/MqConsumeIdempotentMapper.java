package com.aioveu.oms.aioveu10MqConsumeIdempotent.mapper;


import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.entity.MqConsumeIdempotent;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.query.MqConsumeIdempotentQuery;
import com.aioveu.oms.aioveu10MqConsumeIdempotent.model.vo.MqConsumeIdempotentVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: MqConsumeIdempotentMapper
 * @Description TODO MQ消费幂等性Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/10 18:18
 * @Version 1.0
 **/
@Mapper
public interface MqConsumeIdempotentMapper extends BaseMapper<MqConsumeIdempotent> {

    /**
     * 获取MQ消费幂等性分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<MqConsumeIdempotentVo>} MQ消费幂等性分页列表
     */
    Page<MqConsumeIdempotentVo> getMqConsumeIdempotentPage(Page<MqConsumeIdempotentVo> page, MqConsumeIdempotentQuery queryParams);
}
