package com.aioveu.system.aioveu10Log.service;

import com.aioveu.common.annotation.Log;
import com.aioveu.system.aioveu10Log.model.query.LogPageQuery;
import com.aioveu.system.aioveu10Log.model.vo.LogPageVO;
import com.aioveu.system.aioveu10Log.model.vo.VisitStatsVO;
import com.aioveu.system.aioveu10Log.model.vo.VisitTrendVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
  *@ClassName: ConfigService
  *@Description TODO  系统日志 服务接口
  *@Author 可我不敌可爱
  *@Author 雒世松
  *@Date 2025/12/20 18:01
  *@Version 1.0
  **/
public interface LogService extends IService<Log> {

    /**
     * 获取日志分页列表
     */
    Page<LogPageVO> getLogPage(LogPageQuery queryParams);


    /**
     * 获取访问趋势
     *
     * @param startDate 开始时间
     * @param endDate   结束时间
     */
    VisitTrendVO getVisitTrend(LocalDate startDate, LocalDate endDate);

    /**
     * 获取访问统计
     */
    VisitStatsVO getVisitStats();
}
