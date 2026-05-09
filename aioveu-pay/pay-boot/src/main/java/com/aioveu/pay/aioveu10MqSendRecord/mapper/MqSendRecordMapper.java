package com.aioveu.pay.aioveu10MqSendRecord.mapper;


import com.aioveu.pay.aioveu10MqSendRecord.model.entity.MqSendRecord;
import com.aioveu.pay.aioveu10MqSendRecord.model.query.MqSendRecordQuery;
import com.aioveu.pay.aioveu10MqSendRecord.model.vo.MqSendRecordVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: MqSendRecordMapper
 * @Description TODO MQ消息发送记录Mapper接口
 * @Author aioveu
 * @Author 雒世松
 * @Date 2026/5/9 21:40
 * @Version 1.0
 * @Param
 * @return
 **/
@Mapper
public interface MqSendRecordMapper extends BaseMapper<MqSendRecord> {

    /**
     * 获取MQ消息发送记录分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<MqSendRecordVo>} MQ消息发送记录分页列表
     */
    Page<MqSendRecordVo> getMqSendRecordPage(Page<MqSendRecordVo> page, MqSendRecordQuery queryParams);
}
