package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.aioveu.entity.FieldPlanLock;
import com.aioveu.entity.FieldPlanTemplate;
import com.aioveu.vo.FieldPlanTemplateVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldPlanLockDao extends BaseMapper<FieldPlanLock> {
    List<FieldPlanLock> getByStoreId(Long storeId, Long venueId, String name);
}
