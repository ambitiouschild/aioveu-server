package com.aioveu.oms.aioveu07UndoLog.service.impl;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import com.aioveu.oms.aioveu07UndoLog.converter.UndoLogConverter;
import com.aioveu.oms.aioveu07UndoLog.mapper.UndoLogMapper;
import com.aioveu.oms.aioveu07UndoLog.model.entity.UndoLog;
import com.aioveu.oms.aioveu07UndoLog.model.form.UndoLogForm;
import com.aioveu.oms.aioveu07UndoLog.model.query.UndoLogQuery;
import com.aioveu.oms.aioveu07UndoLog.model.vo.UndoLogVO;
import com.aioveu.oms.aioveu07UndoLog.service.UndoLogService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName: UndoLogServiceImpl
 * @Description TODO  AT transaction mode undo table服务实现类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:43
 * @Version 1.0
 **/

@Service
@RequiredArgsConstructor
public class UndoLogServiceImpl extends ServiceImpl<UndoLogMapper, UndoLog> implements UndoLogService {

    private final UndoLogConverter undoLogConverter;

    /**
     * 获取AT transaction mode undo table分页列表
     *
     * @param queryParams 查询参数
     * @return {@link IPage<UndoLogVO>} AT transaction mode undo table分页列表
     */
    @Override
    public IPage<UndoLogVO> getUndoLogPage(UndoLogQuery queryParams) {
        Page<UndoLogVO> pageVO = this.baseMapper.getUndoLogPage(
                new Page<>(queryParams.getPageNum(), queryParams.getPageSize()),
                queryParams
        );
        return pageVO;
    }

    /**
     * 获取AT transaction mode undo table表单数据
     *
     * @param id AT transaction mode undo tableID
     * @return AT transaction mode undo table表单数据
     */
    @Override
    public UndoLogForm getUndoLogFormData(Long id) {
        UndoLog entity = this.getById(id);
        return undoLogConverter.toForm(entity);
    }

    /**
     * 新增AT transaction mode undo table
     *
     * @param formData AT transaction mode undo table表单对象
     * @return 是否新增成功
     */
    @Override
    public boolean saveUndoLog(UndoLogForm formData) {
        UndoLog entity = undoLogConverter.toEntity(formData);
        return this.save(entity);
    }

    /**
     * 更新AT transaction mode undo table
     *
     * @param id   AT transaction mode undo tableID
     * @param formData AT transaction mode undo table表单对象
     * @return 是否修改成功
     */
    @Override
    public boolean updateUndoLog(Long id,UndoLogForm formData) {
        UndoLog entity = undoLogConverter.toEntity(formData);
        return this.updateById(entity);
    }

    /**
     * 删除AT transaction mode undo table
     *
     * @param ids AT transaction mode undo tableID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    @Override
    public boolean deleteUndoLogs(String ids) {
        Assert.isTrue(StrUtil.isNotBlank(ids), "删除的AT transaction mode undo table数据为空");
        // 逻辑删除
        List<Long> idList = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        return this.removeByIds(idList);
    }
}
