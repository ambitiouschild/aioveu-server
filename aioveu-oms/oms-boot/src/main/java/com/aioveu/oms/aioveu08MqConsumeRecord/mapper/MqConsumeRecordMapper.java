package com.aioveu.oms.aioveu08MqConsumeRecord.mapper;


import com.aioveu.oms.aioveu08MqConsumeRecord.model.entity.MqConsumeRecord;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.query.MqConsumeRecordQuery;
import com.aioveu.oms.aioveu08MqConsumeRecord.model.vo.MqConsumeRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: MqConsumeRecordMapper
 * @Description TODO MQ消息消费记录Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 23:29
 * @Version 1.0
 **/
@Mapper
public interface MqConsumeRecordMapper extends BaseMapper<MqConsumeRecord> {

    /**
     * 获取MQ消息消费记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<MqConsumeRecordVo>} MQ消息消费记录分页列表
     */
    Page<MqConsumeRecordVo> getMqConsumeRecordPage(Page<MqConsumeRecordVo> page, MqConsumeRecordQuery queryParams);
}
