package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.aioveu.entity.GradeTemplate;
import com.aioveu.vo.GradeTemplateDetailVO;
import com.aioveu.vo.GradeTemplateVO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface GradeTemplateDao extends BaseMapper<GradeTemplate> {

    /**
     * 分页获取店铺的班级模板列表
     * @param storeId
     * @return
     */
    IPage<GradeTemplateVO> templateList(IPage<GradeTemplateVO> page,  Long storeId, String startDate, String endDate);

    /**
     * 获取模板编辑详情
     * @param id
     * @return
     */
    GradeTemplateDetailVO getDetail(String id);

}
