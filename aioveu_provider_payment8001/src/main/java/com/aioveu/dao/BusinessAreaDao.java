package com.aioveu.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.aioveu.entity.BusinessArea;
import com.aioveu.vo.BusinessAreaConditionVO;
import org.springframework.stereotype.Repository;

/**
 * @description
 * @author: 雒世松
 * @date: 2025/11/17 10:42
 */
@Repository
public interface BusinessAreaDao extends BaseMapper<BusinessArea> {

    IPage<BusinessAreaConditionVO> getBusinessAreaListByCondition(IPage<BusinessAreaConditionVO> page, String name, Integer id, Integer parentId);

}
