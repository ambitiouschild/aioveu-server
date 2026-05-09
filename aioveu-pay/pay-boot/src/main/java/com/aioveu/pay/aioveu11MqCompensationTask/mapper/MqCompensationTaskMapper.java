package com.aioveu.pay.aioveu11MqCompensationTask.mapper;


import com.aioveu.pay.aioveu11MqCompensationTask.model.entity.MqCompensationTask;
import com.aioveu.pay.aioveu11MqCompensationTask.model.query.MqCompensationTaskQuery;
import com.aioveu.pay.aioveu11MqCompensationTask.model.vo.MqCompensationTaskVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: MqCompensationTaskMapper
 * @Description TODO MQ补偿任务Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 22:50
 * @Version 1.0
 **/
@Mapper
public interface MqCompensationTaskMapper extends BaseMapper<MqCompensationTask> {

    /**
     * 获取MQ补偿任务分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<MqCompensationTaskVo>} MQ补偿任务分页列表
     */
    Page<MqCompensationTaskVo> getMqCompensationTaskPage(Page<MqCompensationTaskVo> page, MqCompensationTaskQuery queryParams);
}
