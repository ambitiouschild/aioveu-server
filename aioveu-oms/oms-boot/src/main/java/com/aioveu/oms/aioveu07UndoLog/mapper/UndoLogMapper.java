package com.aioveu.oms.aioveu07UndoLog.mapper;

import com.aioveu.oms.aioveu07UndoLog.model.entity.UndoLog;
import com.aioveu.oms.aioveu07UndoLog.model.query.UndoLogQuery;
import com.aioveu.oms.aioveu07UndoLog.model.vo.UndoLogVO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

/**
 * @ClassName: UndoLogMapper
 * @Description TODO AT transaction mode undo tableMapper接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:38
 * @Version 1.0
 **/

@Mapper
public interface UndoLogMapper extends BaseMapper<UndoLog> {

    /**
     * 获取AT transaction mode undo table分页数据
     *
     * @param page 分页对象
     * @param queryParams 查询参数
     * @return {@link Page<UndoLogVO>} AT transaction mode undo table分页列表
     */
    Page<UndoLogVO> getUndoLogPage(Page<UndoLogVO> page, UndoLogQuery queryParams);
}
