package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.FieldPlanTemplate;
import com.aioveu.vo.FieldPlanTemplateVO;
import com.aioveu.vo.FieldPlanVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FieldPlanTemplateDao extends BaseMapper<FieldPlanTemplate> {
    List<FieldPlanTemplateVO> getByStoreId(Long storeId, String dateStr);
}
