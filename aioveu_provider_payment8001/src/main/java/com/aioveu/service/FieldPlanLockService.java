package com.aioveu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.aioveu.entity.FieldPlanLock;
import com.aioveu.entity.FieldPlanTemplate;
import com.aioveu.form.FieldPlanTemplateForm;
import com.aioveu.vo.FieldPlanTemplateVO;

import java.sql.Time;
import java.util.Date;
import java.util.List;

public interface FieldPlanLockService extends IService<FieldPlanLock> {
    List<FieldPlanLock> getByStoreId(Long storeId, Long venueId, String name);

    List<FieldPlanLock> getByVenueId(Long venueId, Date day);

    boolean changeStatus(Long id, Integer status);
    boolean create(FieldPlanLock item);
}
