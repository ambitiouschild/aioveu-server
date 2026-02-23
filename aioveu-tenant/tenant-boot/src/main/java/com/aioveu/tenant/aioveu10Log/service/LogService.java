package com.aioveu.tenant.aioveu10Log.service;

import com.aioveu.tenant.aioveu10Log.model.entity.Log;
import com.aioveu.tenant.aioveu10Log.model.form.LogQuery;
import com.aioveu.tenant.aioveu10Log.model.vo.LogPageVO;
import com.aioveu.tenant.aioveu10Log.model.vo.VisitStatsVO;
import com.aioveu.tenant.aioveu10Log.model.vo.VisitTrendVO;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import java.time.LocalDate;

/**
 * @ClassName: LogService
 * @Description TODO 系统日志 服务接口
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/2/21 21:49
 * @Version 1.0
 **/
public interface LogService extends IService<Log> {

    /**
     * 获取日志分页列表
     */
    Page<LogPageVO> getLogPage(LogQuery queryParams);


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
