package com.aioveu.refund.aioveu04RefundOperationLog.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.refund.aioveu02RefundItem.model.entity.RefundItem;
import com.aioveu.refund.aioveu04RefundOperationLog.converter.RefundOperationLogConverter;
import com.aioveu.refund.aioveu04RefundOperationLog.mapper.RefundOperationLogMapper;
import com.aioveu.refund.aioveu04RefundOperationLog.model.entity.RefundOperationLog;
import com.aioveu.refund.aioveu04RefundOperationLog.model.form.RefundOperationLogForm;
import com.aioveu.refund.aioveu04RefundOperationLog.model.query.RefundOperationLogQuery;
import com.aioveu.refund.aioveu04RefundOperationLog.model.vo.RefundOperationLogVO;
import com.aioveu.refund.aioveu04RefundOperationLog.service.RefundOperationLogService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: RefundOperationLogServiceImpl
 * @Description TODO 退款操作记录（用于审计）服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/31 18:21
 * @Version 1.0
 **/

@Slf4j
@Service
@RequiredArgsConstructor
public class RefundOperationLogServiceImpl extends ServiceImpl<RefundOperationLogMapper, RefundOperationLog> implements RefundOperationLogService {

    private final RefundOperationLogConverter refundOperationLogConverter;

    /**
     * 获取退款操作记录（用于审计）分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<RefundOperationLogVO>} 退款操作记录（用于审计）分页列表
     */
    @Override
    public IPage<RefundOperationLogVO> getRefundOperationLogPage(RefundOperationLogQuery queryParams) {
        Page<RefundOperationLogVO> pageVO = this.baseMapper.getRefundOperationLogPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取退款操作记录（用于审计）表单数据
     *
     * @param id 退款操作记录（用于审计）ID
     * @return 退款操作记录（用于审计）表单数据
     */
    @Override
    public RefundOperationLogForm getRefundOperationLogFormData(Long id) {
        RefundOperationLog entity = this.getById(id);
        return refundOperationLogConverter.toForm(entity);
    }

    /**
     * 获取退款操作记录（用于审计）实体List
     *
     * @param refundId 退款ID
     * @return 退款操作记录（用于审计）实体List
     */
    @Override
    public List<RefundOperationLog> getRefundOperationLogEntityByRefundId(Long refundId) {
        List<RefundOperationLog> items  = this.list(new LambdaQueryWrapper<RefundOperationLog>()
                .eq(RefundOperationLog::getRefundId, refundId)
        );

        log.info("获取退款操作记录（用于审计）实体List:{}",items);

        return items;
    }


    /**
     * 新增退款操作记录（用于审计）
     *
     * @param formData 退款操作记录（用于审计）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundOperationLog(RefundOperationLogForm formData) {
        RefundOperationLog entity = refundOperationLogConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 新增退款操作记录（用于审计）
     *
     * @param formData 退款操作记录（用于审计）表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveRefundOperationLogWithRefundId(RefundOperationLogForm formData,Long refundId) {
        RefundOperationLog entity = refundOperationLogConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新退款操作记录（用于审计）
     *
     * @param id   退款操作记录（用于审计）ID
     * @param formData 退款操作记录（用于审计）表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateRefundOperationLog(Long id,RefundOperationLogForm formData) {
        RefundOperationLog entity = refundOperationLogConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除退款操作记录（用于审计）
     *
     * @param ids 退款操作记录（用于审计）ID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteRefundOperationLogs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的退款操作记录（用于审计）数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
