package com.aioveu.oms.aioveu09MqDeadLetter.mapper;


import com.aioveu.oms.aioveu09MqDeadLetter.model.entity.MqDeadLetter;
import com.aioveu.oms.aioveu09MqDeadLetter.model.query.MqDeadLetterQuery;
import com.aioveu.oms.aioveu09MqDeadLetter.model.vo.MqDeadLetterVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: MqDeadLetterMapper
 * @Description TODO MQ死信队列Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:50
 * @Version 1.0
 **/
@Mapper
public interface MqDeadLetterMapper extends BaseMapper<MqDeadLetter> {

    /**
     * 获取MQ死信队列分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<MqDeadLetterVo>} MQ死信队列分页列表
     */
    Page<MqDeadLetterVo> getMqDeadLetterPage(Page<MqDeadLetterVo> page, MqDeadLetterQuery queryParams);
}
