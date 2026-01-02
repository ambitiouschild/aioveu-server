package com.aioveu.system.aioveu10Log.mapper;

import com.aioveu.common.annotation.Log;
import com.aioveu.system.aioveu10Log.model.query.LogPageQuery;
import com.aioveu.system.aioveu10Log.model.vo.LogPageVO;
import com.aioveu.system.aioveu10Log.model.vo.VisitCount;
import com.aioveu.system.aioveu10Log.model.vo.VisitStatsBO;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @ClassName: LogMapper
 * @Description TODO  系统日志数据访问层
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2025/12/31 18:04
 * @Version 1.0
 **/

@Mapper
public interface LogMapper extends BaseMapper<Log> {

    /**
     * 获取日志分页列表
     */
    Page<LogPageVO> getLogPage(Page<LogPageVO> page, LogPageQuery queryParams);

    /**
     * 统计浏览数(PV)
     *
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     */
    List<VisitCount> getPvCounts(String startDate, String endDate);

    /**
     * 统计IP数
     *
     * @param startDate 开始日期 yyyy-MM-dd
     * @param endDate   结束日期 yyyy-MM-dd
     */
    List<VisitCount> getIpCounts(String startDate, String endDate);

    /**
     * 获取浏览量(PV)统计
     */
    VisitStatsBO getPvStats();

    /**
     * 获取访问IP统计
     */
    VisitStatsBO getUvStats();
}
