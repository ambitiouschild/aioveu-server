package com.aioveu.oms.aioveu07UndoLog.service;

import com.aioveu.oms.aioveu07UndoLog.model.entity.UndoLog;
import com.aioveu.oms.aioveu07UndoLog.model.form.UndoLogForm;
import com.aioveu.oms.aioveu07UndoLog.model.query.UndoLogQuery;
import com.aioveu.oms.aioveu07UndoLog.model.vo.UndoLogVO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @ClassName: UndoLogService
 * @Description TODO  AT transaction mode undo table服务类
 * @Author 可我不敌可爱
 * @Author 雒世松
 * @Date 2026/1/10 17:42
 * @Version 1.0
 **/
public interface UndoLogService extends IService<UndoLog> {

    /**
     *AT transaction mode undo table分页列表
     *
     * @return {@link IPage<UndoLogVO>} AT transaction mode undo table分页列表
     */
    IPage<UndoLogVO> getUndoLogPage(UndoLogQuery queryParams);

    /**
     * 获取AT transaction mode undo table表单数据
     *
     * @param id AT transaction mode undo tableID
     * @return AT transaction mode undo table表单数据
     */
    UndoLogForm getUndoLogFormData(Long id);

    /**
     * 新增AT transaction mode undo table
     *
     * @param formData AT transaction mode undo table表单对象
     * @return 是否新增成功
     */
    boolean saveUndoLog(UndoLogForm formData);

    /**
     * 修改AT transaction mode undo table
     *
     * @param id   AT transaction mode undo tableID
     * @param formData AT transaction mode undo table表单对象
     * @return 是否修改成功
     */
    boolean updateUndoLog(Long id, UndoLogForm formData);

    /**
     * 删除AT transaction mode undo table
     *
     * @param ids AT transaction mode undo tableID，多个以英文逗号(,)分割
     * @return 是否删除成功
     */
    boolean deleteUndoLogs(String ids);
}
