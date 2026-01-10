package com.aioveu.oms.aioveu04OrderLog.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.common.security.util.SecurityUtils;
import com.aioveu.oms.aioveu04OrderLog.converter.OmsOrderLogConverter;
import com.aioveu.oms.aioveu04OrderLog.model.form.OmsOrderLogForm;
import com.aioveu.oms.aioveu04OrderLog.model.query.OmsOrderLogQuery;
import com.aioveu.oms.aioveu04OrderLog.model.vo.OmsOrderLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.aioveu.oms.aioveu04OrderLog.mapper.OmsOrderLogMapper;
import com.aioveu.oms.aioveu04OrderLog.model.entity.OmsOrderLog;
import com.aioveu.oms.aioveu04OrderLog.service.OmsOrderLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/*
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Description //TODO  订单操作历史记录服务实现类
 * @Date  2026/1/10 16:42
 * @Param
 * @return
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class OmsOmsOrderLogServiceImpl extends ServiceImpl<OmsOrderLogMapper, OmsOrderLog> implements OmsOrderLogService {

    private final OmsOrderLogConverter omsOrderLogConverter;

    /**
     * 获取订单操作历史记录分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<OmsOrderLogVO>} 订单操作历史记录分页列表
     */
    @Override
    public IPage<OmsOrderLogVO> getOmsOrderLogPage(OmsOrderLogQuery queryParams) {
        Page<OmsOrderLogVO> pageVO = this.baseMapper.getOmsOrderLogPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取订单操作历史记录表单数据
     *
     * @param id 订单操作历史记录ID
     * @return 订单操作历史记录表单数据
     */
    @Override
    public OmsOrderLogForm getOmsOrderLogFormData(Long id) {
        OmsOrderLog entity = this.getById(id);
        return omsOrderLogConverter.toForm(entity);
    }

    /**
     * 新增订单操作历史记录
     *
     * @param formData 订单操作历史记录表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveOmsOrderLog(OmsOrderLogForm formData) {
        OmsOrderLog entity = omsOrderLogConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新订单操作历史记录
     *
     * @param id   订单操作历史记录ID
     * @param formData 订单操作历史记录表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateOmsOrderLog(Long id,OmsOrderLogForm formData) {
        OmsOrderLog entity = omsOrderLogConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除订单操作历史记录
     *
     * @param ids 订单操作历史记录ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteOmsOrderLogs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的订单操作历史记录数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }


    /**
     * 添加订单操作日志记录
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param user 操作人员
     * @param detail 描述信息
     */
    @Override
    public void addOrderLogs(Long orderId, Integer orderStatus, String user, String detail) {
        log.info("添加订单操作日志，orderId={}，detail={}", orderId, detail);
        OmsOrderLog orderLog = new OmsOrderLog();
        orderLog.setDetail(detail);
        orderLog.setOrderId(orderId);
        orderLog.setOrderStatus(orderStatus);
        orderLog.setUser(user);
        this.save(orderLog);
    }

    /**
     * 添加订单操作日志记录
     * @param orderId 订单ID
     * @param orderStatus 订单状态
     * @param detail 描述
     */
    @Override
    public void addOrderLogs(Long orderId, Integer orderStatus, String detail) {
        Long memberId = SecurityUtils.getMemberId();
        addOrderLogs(orderId, orderStatus, memberId.toString(), detail);
    }

}
